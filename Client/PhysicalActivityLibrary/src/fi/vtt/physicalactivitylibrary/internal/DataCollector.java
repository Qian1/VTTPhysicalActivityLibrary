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

package fi.vtt.physicalactivitylibrary.internal;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import fi.vtt.physicalactivitylibrary.internal.utils.*;
import fi.vtt.physicalactivitylibrary.VTTPhysicalActivityLibrary;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
 * DataCollector class, that collects the accelerometer etc. data. 
 * <p>
 * Used by InternalClass. 
 * 
 * @see  android.location.LocationListener 
 * @see  android.hardware.SensorEventListener 
 * @see  fi.vtt.physicalactivitylibrary.internal.InternalClass 
 * @see  fi.vtt.physicalactivitylibrary.internal.utils.MyTimerListener 
 *
 */

public final class DataCollector implements LocationListener, MyTimerListener, SensorEventListener {

	private ArrayList<AccelerometerReadings> accelerometerReadingsArrayList;

	private ArrayList<DataCollectorListener> dataCollectorListenersArrayList = new ArrayList<DataCollectorListener>();

	private ArrayList<DataCollectorObserver> DataCollectorObserversArrayList = new ArrayList<DataCollectorObserver>();

	private ArrayList<Float> xFloatArrayList;
	private ArrayList<Float> yFloatArrayList;
	private ArrayList<Float> zFloatArrayList;

	private ArrayList<Long> timeLongArrayList;

	private boolean isRecordingBoolean;

	private Context context;

	private float lightValue = -1.0f; // Ambient light level in SI lux units.  < 0 , if no data available. 
	private float proximityValue = -1.0f; // Proximity sensor distance measured in centimeters. < 0 , if no data available. 

	private MyTimer myTimer;

	private SensorManager sensorManager;

	private static final int ACCELEROMETER_TIMER_TIMER_DELAY = 5 * 1000; // 5 seconds. 

	private WriteLock writeLock;

	// Key: Sensor object identifier, Value: true, if data collected, otherwise false. 

	protected Map<DataSourceID, Boolean> collectedDataMap;

	private Handler handler = new Handler() {
		public void handleMessage(Message messageIncoming) {
			myTimer.cancel();

			stopRecording();

			if (collectedDataMap.containsKey(DataSourceID.ID_ACCELEROMETER)) {
				collectedDataMap.get(DataSourceID.ID_ACCELEROMETER);
			}

			informListenersDataCollectionCompleted();
		};
	};

	/**
	 * Inform all data collectors that data collection is now complete. 
	 * 
	 * @see  fi.vtt.physicalactivitylibrary.internal.DataCollectorListener 
	 *
	 */

	private void informListenersDataCollectionCompleted() {
		for (int i = 0; i < dataCollectorListenersArrayList.size(); i++) {
			DataCollectorListener dataCollectorListener = dataCollectorListenersArrayList.get(i);
			dataCollectorListener.dataCollectionCompleted();
		}
	}

	/**
	 * Inform all data collectors that data collection failed. 
	 * 
	 * @see  fi.vtt.physicalactivitylibrary.internal.DataCollectorListener 
	 *
	 */

	private void informListenersDataCollectionFailed(int errorCodeIntegerIncoming) {
		for (int i = 0; i < dataCollectorListenersArrayList.size(); i++) {
			DataCollectorListener dataCollectorListener = dataCollectorListenersArrayList.get(i);
			dataCollectorListener.dataCollectionFailed(errorCodeIntegerIncoming);
		}
	}

	public ArrayList<Float> getXBuffer() {
		return xFloatArrayList;
	}

	public ArrayList<Float> getYBuffer() {
		return yFloatArrayList;
	}

	public ArrayList<Float> getZBuffer() {
		return zFloatArrayList;
	}

	public ArrayList<Long> getTimeBuffer() {
		return timeLongArrayList;
	}

	public boolean isRecording() {
		writeLock.lock();

		boolean recordingBoolean = isRecordingBoolean;

		writeLock.unlock();

		return recordingBoolean;
	}

	/**
	 * Registers a DataCollectorListener. 
	 * 
	 * @param  dataCollectorListenerIncoming  The listener to register. 
	 * 
	 * @return  True, if success, otherwise false. 
	 * 
	 * @see  fi.vtt.physicalactivitylibrary.internal.DataCollectorListener 
	 * 
	 */

	public boolean registerListener(DataCollectorListener dataCollectorListenerIncoming) {
		return dataCollectorListenersArrayList.add(dataCollectorListenerIncoming);
	}

	/**
	 * Unregisters a DataCollectorListener. 
	 * 
	 * @param  dataCollectorListenerIncoming  The listener to unregister. 
	 * 
	 * @return  True, if success, otherwise false. 
	 *  
	 * @see  fi.vtt.physicalactivitylibrary.internal.DataCollectorListener 
	 * 
	 */

	public boolean unRegisterListener(DataCollectorListener dataCollectorListenerIncoming) {
		return dataCollectorListenersArrayList.remove(dataCollectorListenerIncoming);
	}

	/**
	 * Registers a DataCollectorObserver. 
	 * 
	 * @param  dataCollectorObserverIncoming  The observer to register. 
	 * 
	 * @return  True, if success, otherwise false. 
	 * 
	 * @see  fi.vtt.physicalactivitylibrary.internal.DataCollectorObserver 
	 * 
	 */
	
	public boolean registerObserver(DataCollectorObserver dataCollectorObserverIncoming) {
		return DataCollectorObserversArrayList.add(dataCollectorObserverIncoming);
	}

	/**
	 * Unregisters a DataCollectorObserver. 
	 * 
	 * @param  dataCollectorObserverIncoming  The observer to unregister. 
	 * 
	 * @return  True, if success, otherwise false. 
	 * 
	 * @see  fi.vtt.physicalactivitylibrary.internal.DataCollectorObserver 
	 * 
	 */
	
	public boolean unRegisterObserver(DataCollectorObserver dataCollectorObserverIncoming) {
		return DataCollectorObserversArrayList.remove(dataCollectorObserverIncoming);
	}

	/**
	 * Constructor. 
	 * 
	 * @param  contextIncoming  The application context. 
	 * 
	 */

	public DataCollector(Context contextIncoming) {
		context = contextIncoming;

		writeLock = new ReentrantReadWriteLock().writeLock();

		collectedDataMap = new TreeMap<DataSourceID, Boolean>();

		accelerometerReadingsArrayList = new ArrayList<AccelerometerReadings>();

		xFloatArrayList = new ArrayList<Float>();
		yFloatArrayList = new ArrayList<Float>();
		zFloatArrayList = new ArrayList<Float>();

		timeLongArrayList = new ArrayList<Long>();
	}

	public float getProximityValue() {
		return proximityValue;
	}

	public float getLightValue() {
		return lightValue;
	}

	public int getCellID() {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		GsmCellLocation gsmCellLocation = (GsmCellLocation)telephonyManager.getCellLocation();

		return gsmCellLocation.getCid();
	}

	// Ignore.

	@Override
	public void onAccuracyChanged(Sensor sensorIncoming, int accuracyIntegerIncoming) {}

	// Ignore.

	@Override
	public void onLocationChanged(Location locationIncoming) {}

	// Ignore.

	@Override
	public void onProviderDisabled(String providerStringIncoming) {}

	// Ignore.

	@Override
	public void onProviderEnabled(String providerStringIncoming) {}

	@Override
	public void onSensorChanged(SensorEvent sensorEventIncoming) {
		int typeInteger = sensorEventIncoming.sensor.getType();

		if (Sensor.TYPE_ACCELEROMETER == typeInteger) {
			float[] valuesFloatArray = sensorEventIncoming.values;

			float xFloat = valuesFloatArray[0];
			float yFloat = valuesFloatArray[1];
			float zFloat = valuesFloatArray[2];

			long timeStampLong = sensorEventIncoming.timestamp / 1000;

			AccelerometerReadings accelerometerReadings = new AccelerometerReadings();
			accelerometerReadings.setX(xFloat);
			accelerometerReadings.setY(yFloat);
			accelerometerReadings.setZ(zFloat);

			accelerometerReadingsArrayList.add(accelerometerReadings);

			xFloatArrayList.add(xFloat);
			yFloatArrayList.add(yFloat);
			zFloatArrayList.add(zFloat);

			timeLongArrayList.add(timeStampLong);

		}
		else if (Sensor.TYPE_LIGHT == typeInteger) {
			lightValue = sensorEventIncoming.values[0];
		}
		else if (Sensor.TYPE_PROXIMITY == typeInteger) {
			proximityValue = sensorEventIncoming.values[0];
		}
	}

	// Ignore.

	@Override
	public void onStatusChanged(String providerStringIncoming, int statusIntegerIncoming, Bundle bundleIncoming) {}

	/**
	 * Record snapshot data from all the sensors. 
	 * 
	 */

	public void recordSnapshot() {
		if (!isRecording()) {
			writeLock.lock();

			isRecordingBoolean = true;

			writeLock.unlock();

			collectedDataMap.clear();

			xFloatArrayList.clear();
			yFloatArrayList.clear();
			zFloatArrayList.clear();

			timeLongArrayList.clear();

			// Enable accelerometer data:
			collectedDataMap.put(DataSourceID.ID_ACCELEROMETER, false);

			accelerometerReadingsArrayList.clear();

			sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);

			Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

			boolean successBoolean = sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);

			if (!successBoolean) {
				informListenersDataCollectionFailed(VTTPhysicalActivityLibrary.ERROR_NO_ACCELEROMETER_DATA_AVAILABLE);
			}

			// Enable proximity data:

			Sensor proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

			successBoolean = sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);

			if (!successBoolean) {
				informListenersDataCollectionFailed(VTTPhysicalActivityLibrary.ERROR_NO_PROXIMITY_SENSOR_AVAILABLE);
			}

			// Enable light data:

			Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

			successBoolean = sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);

			if (!successBoolean) {
				informListenersDataCollectionFailed(VTTPhysicalActivityLibrary.ERROR_NO_LIGHT_SENSOR_AVAILABLE);
			}

			myTimer = new MyTimer();
			myTimer.setListener(this);
			myTimer.start(ACCELEROMETER_TIMER_TIMER_DELAY);
		}
	}

	/**
	 * Stops recording. 
	 * 
	 */

	public void stopRecording() {
		if (isRecording()) {
			writeLock.lock();

			isRecordingBoolean = false;

			writeLock.unlock();

			myTimer.cancel();

			sensorManager.unregisterListener(this);
		}
	}

	/**
	 * From MyTimerListener. 
	 * 
	 * @see  fi.vtt.physicalactivitylibrary.internal.utils.MyTimerListener 
	 * 
	 */

	@Override
	public void timeout() {
		handler.sendEmptyMessage(0);
	}

}