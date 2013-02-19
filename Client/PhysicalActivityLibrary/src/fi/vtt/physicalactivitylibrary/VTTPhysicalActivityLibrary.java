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

package fi.vtt.physicalactivitylibrary;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import fi.vtt.physicalactivitylibrary.internal.InternalClass;

/**
 * VTT Physical Activity Library for Android. 
 * <p> 
 * Version: 1.0 
 * <p> 
 * Provides physical activity detection methods. 
 * <ul> 
 * <li>Fall detection</li> 
 * <li>Light detection</li> 
 * <li>Orientation detection</li> 
 * <li>Proximity detection</li> 
 * <li>Run detection</li> 
 * <li>Stability detection</li> 
 * <li>Walk detection</li> 
 * </ul> 
 * <p> 
 * <b>Usage:</b>
 * <p>
 * Include this PhysicalActivityLibrary Android project in your Android application project and initialize the library in your application's main class (usually an Android Service):
 * <pre>
 * <code>
 * private VTTPhysicalActivityLibrary physicalActivityLibrary = null;
 * </code>
 * </pre>
 * In your Service class implement the PhysicalActivityLibraryCallback...
 * <p>
 * <pre>
 * <code>
 * public class YourService extends Service implements PhysicalActivityLibraryCallback {
 * 
 *     private VTTPhysicalActivityLibrary contextLibrary = null;
 * 
 *     {@literal @}Override
 *     public void onCreate() {
 *         super.onCreate();
 *         
 *         physicalActivityLibrary = new VTTPhysicalActivityLibrary(this);
 *		   physicalActivityLibrary.setCallback(this);
 *		   physicalActivityLibrary.enableDetection(VTTPhysicalActivityLibrary.DETECTION_FALL);
 *		   physicalActivityLibrary.enableDetection(VTTPhysicalActivityLibrary.DETECTION_LIGHT);
 *		   physicalActivityLibrary.enableDetection(VTTPhysicalActivityLibrary.DETECTION_ORIENTATION);
 *		   physicalActivityLibrary.enableDetection(VTTPhysicalActivityLibrary.DETECTION_PROXIMITY);
 *		   physicalActivityLibrary.enableDetection(VTTPhysicalActivityLibrary.DETECTION_RUN_AND_WALK);
 *		   physicalActivityLibrary.enableDetection(VTTPhysicalActivityLibrary.DETECTION_STABILITY);
 *		   physicalActivityLibrary.initializeWakeLock(getApplicationContext());
 *		   physicalActivityLibrary.startRecognition();
 *     }
 * 
 * }
 * </code>
 * </pre>
 * ...more specifically, implement the callback methods to receive data through the (PhysicalActivityLibraryCallback) interface:
 * <pre>
 * <code>
 * {@literal @}Override
 * public void error(int errorCodeIntegerIncoming) {
 *     // Do something with the error.
 * }
 * 
 * {@literal @}Override
 * public void newContextInfo(Map<Integer, Double> infoMapIncoming) {
 *     // The integer in the Map identifies the event type:
 *     
 *     for (Map.Entry<Integer, Double> infoMap : infoMapIncoming.entrySet()) {
 *         infoMap.getKey();
 *     
 *         double valueDouble = infoMap.getValue();
 *         // Do something with the data.
 *     }
 * }
 * </code>
 * </pre>
 * This way, the library will then function as follows:
 * <p>
 * <ul>
 * <li>Error() will called by the library if an error occurs and</li>
 * <li>NewActivityInfo() will called by the library when new activity data is available.</li>
 * </ul>
 * 
 */

public class VTTPhysicalActivityLibrary {

	/**
	 * The binary physical activity library is included with this Java library the using the following clause:
	 * <pre>
	 * <code>
	 * static {
     *     System.loadLibrary("physicalactivitylibrary");
     * }
     * </code>
     * </pre>
	 * 
	 */

	static {
		System.loadLibrary("physicalactivitylibrary");
	}

	/**
	 * The internal physical activity library class.
	 * 
	 * @see  fi.vtt.physicalactivitylibrary.internal.InternalClass
	 * 
	 */

	private InternalClass internalClass;

	/**
	 * Basic Android Wakelock.
	 * 
	 * @see  android.os.PowerManager.WakeLock
	 * 
	 */

	private WakeLock wakeLock = null;

	/**
	 * Disable detection. 
	 * 
	 * @param  typeIntegerIncoming  The detection type integer value. 
	 * 
	 */

	public boolean disableDetection(int typeIntegerIncoming) {
		return internalClass.disableDetectionMethod(typeIntegerIncoming);
	}

	/**
	 * Enable detection. 
	 * 
	 * @param  typeIntegerIncoming  The detection type integer value. 
	 * 
	 */

	public boolean enableDetection(int typeIntegerIncoming) {
		return internalClass.enableDetectionMethod(typeIntegerIncoming);
	}

	/**
     * Initializes wakelock for keeping recognition alive when phone screen is turned off. 
     * <p>
     * Call this from your application's main class (usually an Android Service).
     * 
     * @param  contextIncoming  Context of your application.
     * 
     * @return  true  If wakelock initialized, otherwise false.
     * 
     * @see  android.content.Context
     * 
     */

	public boolean initializeWakeLock(Context contextIncoming) {
		try {
			PowerManager powerManager = (PowerManager)contextIncoming.getSystemService(Context.POWER_SERVICE);

			wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "PhysicalActivityLibrary partial wakelock");

			return true;
		}
		catch (Exception exceptionIncoming) {
			return false;
		}
	}

	/**
	 * Returns true if recognition is running, otherwise false. 
	 * 
	 */

	public boolean isRunning() {
		return internalClass.isRunning();
	}

	/**
	 * Orientation face down constant value is 2.0.
	 * 
	 */

	public static final double ORIENTATION_FACE_DOWN = 2.0;

	/**
	 * Orientation face up constant value is 1.0.
	 * 
	 */

	public static final double ORIENTATION_FACE_UP = 1.0;

	/**
	 * Orientation left up constant value is 105.0.
	 * 
	 */

	public static final double ORIENTATION_LEFT_UP = 105.0;

	/**
	 * Orientation right up constant value is 106.0.
	 * 
	 */

	public static final double ORIENTATION_RIGHT_UP = 106.0;

	/**
	 * Orientation top down constant value is 14.0.
	 * 
	 */

	public static final double ORIENTATION_TOP_DOWN = 14.0;

	/**
	 * Orientation top up constant value is 13.0.
	 * 
	 */

	public static final double ORIENTATION_TOP_UP = 13.0;

	/**
	 * Orientation undefined constant value is -1.0.
	 * 
	 */

	public static final double ORIENTATION_UNDEFINED = -1.0;

	/**
	 * Fall detection constant value is 3.
	 * 
	 */

	public static final int DETECTION_FALL = 3;

	/**
	 * Light sensor constant value is 9. 
	 * <p>
	 * Map value: ambient light level in SI lux units. 
	 * 
	 */

	public static final int DETECTION_LIGHT = 9;

	/**
	 * Orientation detection constant value is 5.
	 * 
	 */

	public static final int DETECTION_ORIENTATION = 5;

	/**
	 * Proximity sensor constant value is 8. 
	 * <p> 
	 * Map value: proximity sensor distance measured in centimeters. 
	 * 
	 */

	public static final int DETECTION_PROXIMITY = 8;

	/**
	 * Run detection constant value is 2.
	 * 
	 */

	public static final int DETECTION_RUN = 2;

	/**
	 * Run and walk detection constant value is 6.
	 * 
	 */

	public static final int DETECTION_RUN_AND_WALK = 6;

	/**
	 * Stability detection constant value is 4.
	 * 
	 */

	public static final int DETECTION_STABILITY = 4;

	/**
	 * Walk detection constant value is 1.
	 * 
	 */

	public static final int DETECTION_WALK = 1;

	/**
	 * Error code for no accelerometer data available is -1. 
	 * <p>
	 * When recognition is started and no accelerometer data is available, the error callback method is called with this error code. 
	 * 
	 */

	public static final int ERROR_NO_ACCELEROMETER_DATA_AVAILABLE = -1;

	/**
	 * Error code for no detections enabled is -2. 
	 * <p>
	 * When recognition is started and no detections are enabled, the error callback method is called with this error code. 
	 * 
	 */

	public static final int ERROR_NO_DETECTIONS_ENABLED = -2;

	/**
	 * Error code for general errors is -3. 
	 * 
	 */

	public static final int ERROR_GENERAL = -3;

	/**
	 * Error code for no proximity sensor available is -111. 
	 * 
	 */

	public static final int ERROR_NO_PROXIMITY_SENSOR_AVAILABLE = - 111;

	/**
	 * Error code for no light sensor available is -222.
	 * 
	 */

	public static final int ERROR_NO_LIGHT_SENSOR_AVAILABLE = -222;
	
	/**
     * Sets the callback for the physical activity library.
     * 
     * @param  physicalActivityLibraryCallbackIncoming  Your application class that implements the PhysicalActivityLibraryCallback.
     * 
     * @see  fi.vtt.physicalactivitylibrary.PhysicalActivityLibraryCallback
     * 
     */

	public void setCallback(PhysicalActivityLibraryCallback physicalActivityLibraryCallbackIncoming) {
		internalClass.setCallback(physicalActivityLibraryCallbackIncoming);
	}

	/**
     * Starts recognition algorithms and acquires a Android Wakelock.
     * 
     */

	public void startRecognition() {
		if (wakeLock != null) {
			wakeLock.acquire();
		}

		internalClass.start();
	}

	/**
     * Stops recognition algorithms and releases the Android Wakelock.
     * 
     */

	public void stopRecognition() {
		if (wakeLock != null) {
			wakeLock.acquire();
		}

		internalClass.stop();
	}

	 /**
	 * Constructor.
	 * <p>
	 * Initializes the internal physical activity library and provides the application context for it.
	 * 
	 * @param  contextIncoming  Context of your application.
	 * 
	 * @see  android.content.Context
	 * 
	 */

	public VTTPhysicalActivityLibrary(Context contextIncoming) {
		internalClass = new InternalClass(contextIncoming);
	}

}