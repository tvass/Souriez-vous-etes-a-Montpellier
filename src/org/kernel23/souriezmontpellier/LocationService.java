package org.kernel23.souriezmontpellier;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;


public class LocationService extends Service implements LocationListener {
 
	LocationManager lm;
	
	//Note that if the Android device’s screen goes to sleep, the proximity is
	//also checked once every four minutes in order to preserve the battery life of the device.    
	
 public LocationService() {
 }

 @Override
 public IBinder onBind(Intent intent) {
  return null;
 }
 
 @Override
 public void onCreate() {
 subscribeToLocationUpdates();
 }
 
 @Override
 	public int onStartCommand(Intent intent, int flags, int startId) {
	 
		Intent i = new Intent(this, MyBroadcastReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), -1, i, 0);
		int count = 0;
	 
	 // We want this service to continue running until it is explicitly // stopped, so return sticky.
	 Toast.makeText(this, "Location service Started", Toast.LENGTH_LONG).show();
	 
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
				lm.addProximityAlert(myLat, myLong, 100, -1, pi);
				count++;
				
     }
     		Toast.makeText(getBaseContext(),"Mise en place de "+count+" alertes.", Toast.LENGTH_SHORT).show();
     }
     catch (FileNotFoundException e)
     {
    	 e.printStackTrace();

     } catch (IOException e) {
    	 e.printStackTrace();
     }
	
	 return START_STICKY;
 }
 
 @Override
 	public void onDestroy() {
	 super.onDestroy();
	 Toast.makeText(this, "Location service Destroyed", Toast.LENGTH_LONG).show();
 }
 
public void onLocationChanged(Location loc) {
		
}
 
public void onProviderEnabled(String provider){
		Toast.makeText(getBaseContext(),provider + " OK", Toast.LENGTH_SHORT).show();
}
    
public void onProviderDisabled(String provider){
		Toast.makeText(getBaseContext(),provider + " KO", Toast.LENGTH_SHORT).show();
}
    
public void onStatusChanged(String s, int i, Bundle b){
}
   
public void subscribeToLocationUpdates() {
        this.lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        this.lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        this.lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
}
}