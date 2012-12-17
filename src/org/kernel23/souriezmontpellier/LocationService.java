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
import android.util.Log;
import android.widget.Toast;


public class LocationService extends Service implements LocationListener {
 

    Intent intent;
    int counter = 0;	
	
	LocationManager lm;
	
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
	 
	 int count = 0;
 	 InputStream is = getResources().openRawResource(R.raw.montpellier_cctv_gps_database);
 	 BufferedReader reader = null;
 	
     try
     {
    	 reader = new BufferedReader(new InputStreamReader(is));
    	 String line;
    
     while ((line = reader.readLine()) != null )
     {
    	 	String [] rowData = line.split(";");

				Double myLat = Double.parseDouble(rowData[6]);
				Double myLong = Double.parseDouble(rowData[7]);
				
				LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				Intent i = new Intent(this, MyBroadcastReceiver.class);
				i.setAction(rowData[0]);
				PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),0, i, 0);
				
				lm.addProximityAlert(myLat, myLong, 50, 60000, pi);
				count++;
				
     }
     		Toast.makeText(getBaseContext(),"Mise en place de "+count+" alertes.", Toast.LENGTH_SHORT).show();
     }
     catch (FileNotFoundException e)
     {
    	 Log.e("LocationService.java", "FileNotFoundException", e);

     } catch (IOException e) {
    	 Log.e("LocationService.java", "IOException", e);
     }
     finally{
         try {
     if(reader !=null){
                        reader.close();
                  }
         } catch (IOException e) {
         	Log.e("MainActivity", "Erreur lors de la fermeture du fichier de données", e);
        }
     }
	
	 return START_STICKY;
 }
 
 @Override
 	public void onDestroy() {
	 super.onDestroy();
	 
	 int count = 0;
	 
	 InputStream is = getResources().openRawResource(R.raw.montpellier_cctv_gps_database);
     try
     {
    	 BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    	 String line;
    
     while ((line = reader.readLine()) != null )
     {
    	 	String [] rowData = line.split(";");
				
				LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				Intent i = new Intent(this, MyBroadcastReceiver.class);
				i.setAction(rowData[0]);
				PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),0, i, 0);
				lm.removeProximityAlert(pi);
				count++;
				
     }
     		Toast.makeText(getBaseContext(),"Suppréssion de  "+count+" alertes.", Toast.LENGTH_SHORT).show();
     }
     catch (FileNotFoundException e)
     {
    	 Log.e("LocationService.java", "FileNotFoundException", e);

     } catch (IOException e) {
    	 Log.e("LocationService.java", "IOException", e);
     }
	
}
 
public void onLocationChanged(Location loc) {
		
}
 
public void onProviderEnabled(String provider){
		Toast.makeText(getBaseContext(),provider + " détecté.", Toast.LENGTH_SHORT).show();
}
    
public void onProviderDisabled(String provider){
		Toast.makeText(getBaseContext(),provider + " non détecté.", Toast.LENGTH_SHORT).show();
}
    
public void onStatusChanged(String s, int i, Bundle b){
}
   
public void subscribeToLocationUpdates() {
        this.lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        this.lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        this.lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
}


}