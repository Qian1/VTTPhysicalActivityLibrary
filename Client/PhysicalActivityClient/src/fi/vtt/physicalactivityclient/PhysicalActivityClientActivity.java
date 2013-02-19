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

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Application main (Activity) class. 
 * <p> 
 * Starts the PhysicalActivityClientService Android Service, no binding of the Service to this Activity is done however. 
 * <p> 
 * When this Activity is destroyed, an Intent is fired, which the PhysicalActivityClientService grabs and initiates shutdown. 
 * 
 * @see  android.app.Activity 
 * 
 */

public class PhysicalActivityClientActivity extends Activity {

	/**
	 * Basic BroadcastReceiver for receiving Intents. 
	 * 
	 * @see  android.content.BroadcastReceiver 
	 * 
	 */

    private PhysicalActivityBroadcastReceiver physicalActivityBroadcastReceiver = null;

    /**
     * TextView for displaying the physical activity data. 
     * 
     * @see android.widget.TextView 
     * 
     */

    private TextView statusTextView = null;

    /**
	 * Basic way of receiving broadcasted Intents. 
	 * <p> 
	 * Grabs the text Intent from the main Service and shows it on the TextView. 
	 * 
	 * @see  android.content.BroadcastReceiver 
	 * 
	 */

	public class PhysicalActivityBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context contextIncoming, Intent intentIncoming) {
			Log.d(getString(R.string.PhysicalActivityClientActivityString), getString(R.string.OnReceiveString));

			if (intentIncoming.getAction().equals(getString(R.string.SetTextToTextViewString))) {
				String string = intentIncoming.getStringExtra(getString(R.string.TextViewTextString));

				if (string != null) {
					statusTextView.setText(string);
				}

			}
		}

	}

	/**
	 * Basic onCreate() -method. 
	 * <p> 
	 * Starts the PhysicalActivityClientService Android Service, no binding to this Activity is done. 
	 * 
	 * @param  bundleIncoming  The incoming application state Bundle. 
	 * 
	 * @see  android.os.Bundle 
	 * 
	 */

	@Override
	public void onCreate(Bundle bundleIncoming) {
		super.onCreate(bundleIncoming);

		Log.d(getString(R.string.PhysicalActivityClientActivityString), getString(R.string.OnCreateString));

		setContentView(R.layout.main);

		statusTextView = (TextView)findViewById(R.id.StatusTextView);
		statusTextView.setText(getString(R.string.StatusString));

		IntentFilter intentFilter = new IntentFilter(getString(R.string.SetTextToTextViewString));

		physicalActivityBroadcastReceiver = new PhysicalActivityBroadcastReceiver();

		registerReceiver(physicalActivityBroadcastReceiver, intentFilter);

		Intent intent = new Intent(getApplicationContext(), PhysicalActivityClientService.class);

		startService(intent);
	}

	/**
	 * Basic onDestroy() -method. 
	 * <p> 
	 * When this Activity is destroyed, an Intent is fired, which the PhysicalActivityClientService grabs and initiates shutdown. 
	 * 
	 */

	@Override
	protected void onDestroy() {
		super.onDestroy();

		Log.d(getString(R.string.PhysicalActivityClientActivityString), getString(R.string.OnDestroyString));

		if (physicalActivityBroadcastReceiver != null) {
        	unregisterReceiver(physicalActivityBroadcastReceiver);
        }

		Intent intent = new Intent(getString(R.string.StopPhysicalActivityClientServiceString));

		sendBroadcast(intent);
	}

}