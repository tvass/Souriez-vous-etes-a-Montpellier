package org.kernel23.souriezmontpellier;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import com.google.android.maps.MapActivity;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;


public class MainActivity extends  MapActivity {
	
	MapView mapView;
	MapController mc;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     
        mapView = (MapView) findViewById(R.id.mapView);
        mc = mapView.getController();
    

        mapView = (MapView) findViewById(R.id.mapView);
        final MyLocationOverlay myLocationOverlay = new MyLocationOverlay(this, mapView);
        mapView.getOverlays().add(myLocationOverlay);
        mapView.postInvalidate();
        myLocationOverlay.enableCompass();
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.runOnFirstFix(new Runnable() {
            public void run() {
        	mc.animateTo(myLocationOverlay.getMyLocation());
        	mc.setZoom(17);
            }
        });
        
        mapView.invalidate();
        
        List<Overlay> mapOverlays = mapView.getOverlays();

        Drawable drawable = this.getResources().getDrawable(R.drawable.cctv);
        HelloItemizedOverlay itemizedoverlay = new HelloItemizedOverlay(drawable,this);    

        InputStream is = getResources().openRawResource(R.raw.montpellier_cctv_gps_database);
        try
        {
       	 BufferedReader reader = new BufferedReader(new InputStreamReader(is));
       	 String line;
       
        while ((line = reader.readLine()) != null )
        {
       	 	String [] rowData = line.split(";");

   				Double myLat = Double.parseDouble(rowData[6]);
   				Double myLong = Double.parseDouble(rowData[7]);
   				GeoPoint geopoint = new GeoPoint((int)(myLat*1E6), (int)(myLong*1E6));
   				OverlayItem overlayitem2 = new OverlayItem(geopoint, "Informations", rowData[2]+"\n"+rowData[3]+"\n"+rowData[4]+"\n"+rowData[5]);
   				itemizedoverlay.addOverlay(overlayitem2);
        }
        
        mapOverlays.add(itemizedoverlay);
	       
        }
        catch (FileNotFoundException e)
        {
       	 e.printStackTrace();

        } catch (IOException e) {
       	 e.printStackTrace();
        }
         
    }

    public void MystartService(View view) {
        startService(new Intent(getBaseContext(), LocationService.class));
    }

    public void MystopService(View view) {
        stopService(new Intent(getBaseContext(), LocationService.class));
    }

    
    @Override
    public void onStop() {
    	super.onStop();
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    }     
    
    @Override
    public void onPause() {
    	super.onPause();
    }     

    @Override
	public void onResume() {
    	super.onResume();
    	
    	

   }
  
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.startApp:
        	this.MystartService(mapView);
            return true;
        case R.id.stopApp:
        	this.MystopService(mapView);
            return true;       
        case R.id.submitApp:
        	 Intent myIntent = new Intent(this,SubmitApp.class);
             startActivity(myIntent);
            return true;               
        default:
            return super.onOptionsItemSelected(item);
        }
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
