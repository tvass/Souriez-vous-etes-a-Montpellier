package org.kernel23.souriezmontpellier;

import android.os.Vibrator;
import android.content.Context;
import android.media.MediaPlayer;
import android.widget.Toast;
import android.content.Intent;
import android.content.BroadcastReceiver;

public class MyBroadcastReceiver extends BroadcastReceiver {
	  @Override
	  public void onReceive(Context context, Intent intent) {
	    Toast.makeText(context, "Souriez vous êtes filmés !",
	    Toast.LENGTH_LONG).show();

	    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	    vibrator.vibrate(3000);

	    MediaPlayer mp = MediaPlayer.create(context, R.raw.sonar);
	    mp.start();
	  }

} 
