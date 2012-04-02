package org.hook38.locationfinderandroid;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.hook38.locationfinderandroid.objects.ResultObject;



import android.location.Location;
import android.util.Log;

public class StoreLocationFactory 
{
    public static List<org.hook38.locationfinderandroid.objects.Location> retrieveLocations(URL url)
    {
        //http://maps.google.com/maps/geo?q=40.714224,-73.961452&output=json&oe=utf8&sensor=true_or_false&key=your_api_key
        
        List<org.hook38.locationfinderandroid.objects.Location> locations = new ArrayList<org.hook38.locationfinderandroid.objects.Location>();
        HttpURLConnection connection = null;
        URL serverAddress = null;

        try 
        {
            // build the URL using the latitude & longitude you want to lookup
            // NOTE: I chose XML return format here but you can choose something else
        	
            serverAddress = url;
            Log.d("StoreLocationFactory", serverAddress.toString());
            //set up out communications stuff
            connection = null;
              
            //Set up the initial connection
            connection = (HttpURLConnection)serverAddress.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setReadTimeout(10000);
                          
            connection.connect();
            
            try
            {
                //InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                //InputSource source = new InputSource(isr);
                
                //InputStream source = retrieveStream(urlString);
                Reader reader = new InputStreamReader(connection.getInputStream(), "UTF8");		
        		ObjectMapper mapper = new ObjectMapper();
        		
        		ResultObject result;
        		result = mapper.readValue(reader, ResultObject.class);

        		locations = result.getLocations();
                
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        
        return locations;
    }
    
    /**
	 * This method fire the url to the server using a get method. It than
	 * retrieve the result as inputStream.
	 * 
	 * @param url The url for the request, in this case, the goole getocode
	 * 			webpage.
	 * @return The response received from the get request.
	 */
	private static InputStream retrieveStream(String url){
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(url);
		try{
			HttpResponse getResponse = client.execute(getRequest);
			final int statusCode = getResponse.getStatusLine().getStatusCode();
			if(statusCode != HttpStatus.SC_OK){
				return null;
			}
			HttpEntity getResponseEntity = getResponse.getEntity();
			return getResponseEntity.getContent();
		}catch(IOException e){
			getRequest.abort();
		}
		return null;
	}
    
   
}
