package org.hook38.locationfinderandroid;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class SearchActivity extends MapActivity {

	private static final String TAG = "debugg log";
	
	private Context context;
	
	private MapController mapController;
	private MapView mapView;
	private TextView radiusText;
	private TextView latText;
	private TextView lngText;
	private TextView localityText;
	private String category;
	private String radius;
	private List<Overlay> mapOverlays;
	private MapItemizedOverlay itemizedoverlay;
	private LocationListener locationListener;
	
	private ArrayList<org.hook38.locationfinderandroid.objects.Location> retrievedLocations;
	
	private Location currentLocation;
	
	private LocationManager locationManager;
	@Override
	public void onCreate(Bundle savedInstanceState) {    	
		super.onCreate(savedInstanceState);
        Log.w(getClass().getSimpleName(), "onCreate");
        setContentView(R.layout.search);
        this.initiate();        
        
        this.initiateMap();
        this.handleStoreFinder();
            
        locationManager.removeUpdates(this.locationListener);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Log.w(getClass().getSimpleName(), "onStart");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.w(getClass().getSimpleName(), "onResume");
	}
	
	@Override
	public void onRestart() {
		super.onRestart();
		Log.w(getClass().getSimpleName(), "onRestart");
	}
	
	@Override
	public void onStop() {
		super.onStart();
		Log.w(getClass().getSimpleName(), "onStop");
	}
	
	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		Log.w(getClass().getSimpleName(), "onSaveInstanceState");
	}
	
	@Override
	public void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
		Log.w(getClass().getSimpleName(), "onRestoreInstanceState");
	}
	
	public void setLocalityText(String text) {
		this.localityText.setText(text);
	}
	
	@Override
	public void onDestroy(){
		Log.d("Search", "On destroy");
		super.onDestroy();		
	}
	
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void initiate() {		
		this.context = this;
		this.latText = (TextView) findViewById(R.id.latitude_text);
		this.lngText = (TextView) findViewById(R.id.longitude_text);
		this.radiusText = (TextView) findViewById(R.id.radius_text);
		this.localityText = (TextView) findViewById(R.id.locality_text);
		Intent i = this.getIntent();
        this.radius = i.getStringExtra("radius_param");
        this.radiusText.setText(this.radius);
        this.category = i.getStringExtra("category_param");
        
        
        
	}
	
	private void initiateMap(){
		this.initiateMapView();
        this.initiateLocationManager();
        

        double lat = this.currentLocation.getLatitude();
        double lng = this.currentLocation.getLongitude();
        
        this.latText.setText(Double.toString(lat));
        this.lngText.setText(Double.toString(lng));
        
        this.focusMap(lat, lng);
        
        
        mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.arrow);
        itemizedoverlay = new MapItemizedOverlay(drawable, this);
        
        GeoPoint point = new GeoPoint(this.processLocation(lat),this.processLocation(lng));        
        OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
        
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
    
	}
	
	private void initiateMapView(){
		this.mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		this.mapController = mapView.getController();

	}
	
	private void handleLocationChanged(Location location) {
		this.currentLocation = location;
		//this.handleReverseGeocode();
		
	}
	
	private void handleStoreFinder() {
		//http://hook38locationfinder.appspot.com/fetchlocation?lat=1&lng=1
		if(this.currentLocation != null) {
			try {				
				URL fetchLocationsURL = new URL(
						"http://hook38locationfinder.appspot.com/fetchlocation?lat=" + 
						Double.toString(this.currentLocation.getLatitude()) + "&lng=" + 
						Double.toString(this.currentLocation.getLongitude()));				
				StoreLocationLookupTask task = new StoreLocationLookupTask();
				task.applicationContext = this;
				task.execute(fetchLocationsURL);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				
				Log.d("Search", "Invalid URL");
			}
			
		}else{ 
			this.localityText.setText("Current Location unknown");
		}
	}
	
	private void handleReverseGeocode(){
		if(this.currentLocation != null) {
			try {
				String map_api_key = 
						getBaseContext().
						getResources().
						getText(R.string.map_api_key).
						toString();
				URL reverseGeocodeURL = new URL("http://maps.google.com/maps/geo?q=" + 
						Double.toString(this.currentLocation.getLatitude()) + "," + 
						Double.toString(this.currentLocation.getLongitude()) +
				        "&output=xml&oe=utf8&sensor=true&key=" + map_api_key);
				
				ReverseGeocodeLookupTask task = new ReverseGeocodeLookupTask();
				task.applicationContext = this;
				task.execute(reverseGeocodeURL);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				
				Log.d("Search", "Invalid URL");
			}
			
		}else{ 
			this.localityText.setText("Current Location unknown");
		}
	}
	
	private void initiateLocationManager() {
		this.locationManager = 
				(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		
		this.locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				Log.d("Search", "Location changed");
				handleLocationChanged(location);
			}

			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				Log.d("Search", "provider disabled");
			}

			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				Log.d("Search", "provider enabled");
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
				Log.d("Search", "status changed");
				
			}			
		};
		this.locate();
	}
	
	private void locate(){
		//2nd parameter is the minimum time interval between notification
		//3rd parameter is the minimum change in distance between notifications
		this.locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 1000, this.locationListener);
		this.currentLocation = 
				this.locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	}
	
	private void focusMap(double lat, double lng){
		String default_zoom = 
				getBaseContext().
				getResources().
				getText(R.string.map_zoom_level).
				toString();
		
		int zoom;
		try{
			zoom = Integer.parseInt(default_zoom);
		}catch(NumberFormatException e){
			zoom = 15;
		}
		GeoPoint startLocation = new GeoPoint(processLocation(lat), processLocation(lng));
		this.mapController.animateTo(startLocation);
		this.mapController.setZoom(zoom);
	}
	
	private void focusMap(){
		String default_latitute = 
				getBaseContext().
				getResources().
				getText(R.string.default_latitute).
				toString();
		String default_longitude = 
				getBaseContext().
				getResources().
				getText(R.string.default_longitude).
				toString();
		String default_zoom = 
				getBaseContext().
				getResources().
				getText(R.string.map_zoom_level).
				toString();
		double latitute;
		double longitude;
		int zoom;
		try{
			latitute = Double.parseDouble(default_latitute);
			longitude = Double.parseDouble(default_longitude);
			zoom = Integer.parseInt(default_zoom);
		}catch(NumberFormatException e){
			latitute = 25.037977;
			longitude = 121.548829;
			zoom = 15;
		}
		GeoPoint startLocation = new GeoPoint(processLocation(latitute), processLocation(longitude));
		this.mapController.animateTo(startLocation);
		this.mapController.setZoom(zoom);
	}
	
	private int processLocation(double point){
		return (int)(point * 1E6);
	}
	
	public class ReverseGeocodeLookupTask extends
			AsyncTask <URL, Void, String> {
		
		private ProgressDialog dialog;
		protected Context applicationContext;
		
		@Override
		protected void onPreExecute() {
			this.dialog = ProgressDialog.show(applicationContext, 
					"Please wait...", 
					"Requesting reverse geocode lookup", 
					true);
		}
		
		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			setLocalityText(result);
		}
		
		@Override
		protected String doInBackground(URL ... params) {
			// TODO Auto-generated method stub
			String localityName = "";
			
			if (currentLocation != null) {
				
				localityName = Geocoder.reverseGeocode(params[0]);
			}
			return localityName;
		}

	}
	
	public class StoreLocationLookupTask extends
			AsyncTask <URL, Void, ArrayList<org.hook38.locationfinderandroid.objects.Location>> {
		
		private ProgressDialog dialog;
		protected Context applicationContext;
		
		@Override
		protected void onPreExecute() {
			this.dialog = ProgressDialog.show(applicationContext, 
					"Please wait...", 
					"Requesting stores location lookup", 
					true);
		}
		
		@Override
		protected void onPostExecute(ArrayList<org.hook38.locationfinderandroid.objects.Location> locations) {
			dialog.dismiss();
			retrievedLocations = locations;
			MapItemizedOverlay locationitemizedoverlay;
			for(org.hook38.locationfinderandroid.objects.Location location: locations) {
				Drawable drawable = getResources().getDrawable(R.drawable.reddot);
				locationitemizedoverlay = new MapItemizedOverlay(drawable, context);
		        try{
		        	double lat = Double.parseDouble(location.getLat());
		        	double lng = Double.parseDouble(location.getLng());
		        	GeoPoint point = new GeoPoint(processLocation(lat), processLocation(lng));   
	
		        	String location_label = 
							getBaseContext().
							getResources().
							getText(R.string.location_dialog_label).
							toString();
			        OverlayItem overlayitem = new OverlayItem(point, location_label, location.getName());
			        
			        locationitemizedoverlay.addOverlay(overlayitem);
			        mapOverlays.add(locationitemizedoverlay);
			        //break;
		        }catch(NumberFormatException ex) {
		        	
		        }
		        
			}
		}
		
		@Override
		protected ArrayList<org.hook38.locationfinderandroid.objects.Location> doInBackground(URL ... params) {
			// TODO Auto-generated method stub
			ArrayList<org.hook38.locationfinderandroid.objects.Location> locations = new ArrayList<org.hook38.locationfinderandroid.objects.Location>();
			
			if (currentLocation != null) {
				
				locations = (ArrayList<org.hook38.locationfinderandroid.objects.Location>) StoreLocationFactory.retrieveLocations(params[0]);
			}
			return locations;
		}
		
	}
}
