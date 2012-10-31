package org.kernel23.souriezmontpellier;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SubmitApp extends Activity  {

	private static final boolean DEVELOPER_MODE = (true);
	int code_http;
	Button b1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 if (DEVELOPER_MODE) {
             StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                     .detectDiskReads()
                     .detectDiskWrites()
                     .detectNetwork()   // or .detectAll() for all detectable problems
                     .penaltyLog()
                     .build());
           
         }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_app);
        
        b1 = (Button) findViewById(R.id.button1);
        b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
                HttpResponse response;

				// TODO Auto-generated method stub
				try {
								
			    	LocationManager locationManager;
			    	locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			    	Location loc;
			    	if ( locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
			    		loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			    	}else{
			    		loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			    	}
			    	double lat = loc.getLatitude();
			        double lng = loc.getLongitude();
			    	String mylat = Double.toString(lat);
			    	String mylng = Double.toString(lng);
			    	
					HttpPost post = new HttpPost("http://souriez.kernel23.org/submit.app/");
					
					EditText edit = (EditText)findViewById(R.id.editText3);
					String desc = edit.getText().toString();
					
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("desc", desc));
					nameValuePairs.add(new BasicNameValuePair("lat",mylat));
					nameValuePairs.add(new BasicNameValuePair("long", mylng));
					post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    
					response = client.execute(post);
                    
					code_http = response.getStatusLine().getStatusCode();
					}
					catch (Exception e) {
				        Log.e("HTTP", "Error in http connection " + e.toString());
				    }
				
					if(code_http == 200) {
						Toast.makeText(getBaseContext(),"Votre demande est enregistrée.", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(getBaseContext(),"Demande NON enregistrée.", Toast.LENGTH_SHORT).show();
					}
					
					SubmitApp.this.finish();
			}
        });
    }
        	  
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_submit_app, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.title:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }  
    }
