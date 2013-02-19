/*
 * Copyright (c) 2013, VTT Technical Research Centre of Finland 
 * All rights reserved. 
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 * 
 * 1. Redistributions of source code must retain the above copyright 
 *    notice, this list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in the 
 *    documentation and/or other materials provided with the distribution. 
 * 3. Neither the name of the VTT Technical Research Centre of Finland nor the 
 *    names of its contributors may be used to endorse or promote products 
 *    derived from this software without specific prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR 
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 * 
 */

/*
 * PhysicalActivityClient. 
 * 
 * A simple Android application demonstrating the usage of the VTT Physical Activity Library (PhysicalActivityLibrary) for Android. 
 * 
 * Version: 1.0 
 * 
 */

package fi.vtt.physicalactivityclient;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import fi.vtt.physicalactivitylibrary.PhysicalActivityLibraryCallback;
import fi.vtt.physicalactivitylibrary.VTTPhysicalActivityLibrary;
import java.text.DecimalFormat;
import java.util.Map;

/**
 * Application main (Service) class. 
 * <p> 
 * Creates the PhysicaActivityLibrary object, implements and sets call backs and starts the physical activity recognition. 
 * <p> 
 * When a shutdown Intent from the application Activity class is received, the PhysicalActivityLibrary is stopped and this Service is destroyed. 
 * 
 * @see  android.app.Service 
 * @see  fi.vtt.physicalactivitylibrary.PhysicalActivityLibraryCallback
 * 
 */

public class PhysicalActivityClientService extends Service implements PhysicalActivityLibraryCallback {

	/**
	 * Basic BroadcastReceiver for receiving Intents. 
	 * 
	 * @see  android.content.BroadcastReceiver 
	 * 
	 */

    private PhysicalActivityBroadcastReceiver physicalActivityBroadcastReceiver = null;

    /**
	 * VTTPhysicalActivityLibrary
	 * 
	 * @see  fi.vtt.physicalactivitylibrary.VTTPhysicalActivityLibrary
	 * 
	 */

	private VTTPhysicalActivityLibrary physicalActivityLibrary = null;

	/**
	 * Basic way of receiving broadcasted Intents. 
	 * <p> 
	 * Grabs the shutdown Intent from the main Activity, stops recognition and shuts down. 
	 * <p> 
	 * Simple demonstration, you can implement stopping etc. any way you like, just remember to call stopRecognition(). 
	 * 
	 * @see  android.content.BroadcastReceiver 
	 * 
	 */

	public class PhysicalActivityBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context contextIncoming, Intent intentIncoming) {
			Log.d(getString(R.string.PhysicalActivityClientServiceString), getString(R.string.OnReceiveString));

			if (intentIncoming.getAction().equals(getString(R.string.StopPhysicalActivityClientServiceString))) {
				Log.d(getString(R.string.PhysicalActivityClientServiceString), getString(R.string.StoppingRecognitionString));

				physicalActivityLibrary.stopRecognition();

				PhysicalActivityClientService.this.stopSelf();
			}
		}

	}

	/**
	 * Basic onBind() -method. 
	 * <p> 
	 * Needed if you want to bind this Service to some Activity or Service. 
	 * 
	 * @param  intentIncoming  The incoming Intent. 
	 * 
	 * @see  android.content.Intent 
	 * 
	 */

    @Override
    public IBinder onBind(Intent intentIncoming) {
        return null;
    }

    /**
	 * Sample number counter. 
	 * 
	 */

	public static int counterInteger = 0;

	/**
	 * From PhysicalActivityLibraryCallback. 
	 * <p> 
	 * This method is called when an error occurs. 
	 * 
	 * @param  errorCodeIntegerIncoming  Type of the error. 
	 * 
	 */

	@Override
	public void error(int errorCodeIntegerIncoming) {
		Log.d(getString(R.string.PhysicalActivityClientServiceString), "Error code: " + errorCodeIntegerIncoming);
	}

	/**
	 * From PhysicalActivityLibraryCallback. 
	 * <p> 
	 * This method is called when new physical data is available. 
	 * <p> 
	 * Implement this however you wish, here the raw data is simply just shown on the UI. 
	 * 
	 * @param  infoMapIncoming  Contains the current physical information. 
	 * 				 			<p>
	 * 				 			Key is detection type, value is value in range of [0.0, 1.0]. 
	 * 				 			<p>
	 *               			For example: Key = 1, Value = 1.0 means walking type and value 1.0 
	 *               			is the value from the walking detection method. 
	 * 
	 * @see  fi.vtt.physicalactivitylibrary.PhysicalActivityLibraryCallback
	 * 
	 */

	@Override
	public void newActivityInfo(Map<Integer, Double> infoMapIncoming) {
		Log.d(getString(R.string.PhysicalActivityClientServiceString), "New activity info, size: " + infoMapIncoming.size());

		counterInteger++;

		String string = "c: " + counterInteger + " ";

		DecimalFormat decimalFormat = new DecimalFormat("###.###");
		decimalFormat.setMinimumIntegerDigits(3);
		decimalFormat.setMinimumFractionDigits(3);

		for (Map.Entry<Integer, Double> infoMap : infoMapIncoming.entrySet()) {
			string += "key: " + infoMap.getKey() + " value: ";

			double valueDouble = infoMap.getValue();

			string += decimalFormat.format(valueDouble);
			string += " ";
		}

		Log.d("Content: ", string);

		Intent intent = new Intent(getString(R.string.SetTextToTextViewString));
		intent.putExtra(getString(R.string.TextViewTextString), string);

		sendBroadcast(intent);
	}

	/**
	 * Basic onCreate() -method. 
	 * <p> 
	 * Creates: 
	 * <p> 
	 * <ol start="1"> 
	 * <li>The PhysicalActivityLibrary object,</li> 
	 * <li>Registers this Service to listen to the shutdown Intent,</li> 
	 * <li>And finally, starts the physical activity recognition.</li> 
	 * </ol> 
	 * 
	 */

	@Override
	public void onCreate() {
		super.onCreate();

		Log.d(getString(R.string.PhysicalActivityClientServiceString), getString(R.string.OnCreateString));

		Notification notification = new Notification();

		startForeground(0, notification);

		physicalActivityLibrary = new VTTPhysicalActivityLibrary(this);
		physicalActivityLibrary.setCallback(this);
		physicalActivityLibrary.enableDetection(VTTPhysicalActivityLibrary.DETECTION_FALL);
		physicalActivityLibrary.enableDetection(VTTPhysicalActivityLibrary.DETECTION_LIGHT);
		physicalActivityLibrary.enableDetection(VTTPhysicalActivityLibrary.DETECTION_ORIENTATION);
		physicalActivityLibrary.enableDetection(VTTPhysicalActivityLibrary.DETECTION_PROXIMITY);
		physicalActivityLibrary.enableDetection(VTTPhysicalActivityLibrary.DETECTION_RUN_AND_WALK);
		physicalActivityLibrary.enableDetection(VTTPhysicalActivityLibrary.DETECTION_STABILITY);
		physicalActivityLibrary.initializeWakeLock(getApplicationContext());

		IntentFilter intentFilter = new IntentFilter(getString(R.string.StopPhysicalActivityClientServiceString));

		physicalActivityBroadcastReceiver = new PhysicalActivityBroadcastReceiver();

		registerReceiver(physicalActivityBroadcastReceiver, intentFilter);

		physicalActivityLibrary.startRecognition();
	}

	/**
	 * Basic onDestroy() -method. 
	 * <p> 
	 * Unregisters this Service from listening to the shutdown Intent. 
	 * 
	 */

	@Override
	public void onDestroy() {
		super.onDestroy();

		Log.d(getString(R.string.PhysicalActivityClientServiceString), getString(R.string.OnDestroyString));

		if (physicalActivityBroadcastReceiver != null) {
        	unregisterReceiver(physicalActivityBroadcastReceiver);
        }
	}

	/**
	 * Basic getter -method. 
	 * <p> 
	 * Returns a handle to the library object. 
	 * 
	 */

	public VTTPhysicalActivityLibrary getContextLibrary() {
		return physicalActivityLibrary;
	}

}