package org.hook38.locationfinderandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class LocationFinderAndroidActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	private static final String TAG = "debugg log";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.initialize();
        
        final Button searchButton = (Button) findViewById(R.id.search_button);
        final Button exitButton = (Button)findViewById(R.id.exit_button);
        
        searchButton.setOnClickListener(this);
        
        exitButton.setOnClickListener(this);
        
    }
    
    private void initialize() {
    	   	
    	//radius_input.setText(R.pro);
    }

	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.d(TAG, "on click");
		switch(v.getId()){
		case R.id.search_button:
			Intent i = new Intent(this, SearchActivity.class);			
			i.putExtra("radius_param", this.getRadius());
			i.putExtra("category_param", this.getCategory());
			
			startActivity(i);
			break;
		case R.id.exit_button:
			finish();
			break;
		}
	}
	
	private String getRadius() {
		EditText radius = (EditText)findViewById(R.id.radius_input);
		return radius.getText().toString();
	}
	
	private String getCategory() {
		EditText category = (EditText)findViewById(R.id.category_input);
		return category.getText().toString();
	}
}