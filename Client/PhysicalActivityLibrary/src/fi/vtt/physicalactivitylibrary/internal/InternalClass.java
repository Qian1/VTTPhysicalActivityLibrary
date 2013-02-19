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
import fi.vtt.physicalactivitylibrary.PhysicalActivityLibraryCallback;
import fi.vtt.physicalactivitylibrary.VTTPhysicalActivityLibrary;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * InternalClass, runs the library internal functions, implements DataCollectorListener. 
 * 
 * @see  fi.vtt.physicalactivitylibrary.internal.DataCollectorListener 
 *
 */

public class InternalClass implements DataCollectorListener {

	private ArrayList<DataCollectorObserver> dataCollectorObserversArrayList;

	private DataCollector dataCollector;

	private PhysicalActivityLibraryCallback physicalActivityLibraryCallback = null;

	/**
	 * Disable a specific detection method. 
	 * 
	 * @param  typeIntegerIncoming  The detection method type integer. 
	 * 
	 * @return  returnBoolean  True, if succeeded, otherwise false. 
	 */

	public boolean disableDetectionMethod(int typeIntegerIncoming) {
		boolean returnBoolean = false;

		for (int i = 0; i < dataCollectorObserversArrayList.size(); i++) {
			DataCollectorObserver dataCollectorObserver = dataCollectorObserversArrayList.get(i);

			if (dataCollectorObserver.getIdentifier() == typeIntegerIncoming) {
				dataCollectorObserversArrayList.remove(i);

				returnBoolean = true;

				break;
			}
		}

		return returnBoolean;
	}

	/**
	 * Enable a specific detection method. 
	 * 
	 * @param  typeIntegerIncoming  The detection method type integer. 
	 * 
	 * @return  returnBoolean  True, if succeeded, otherwise false. 
	 */

	public boolean enableDetectionMethod(int typeIntegerIncoming) {
		boolean returnBoolean = false;

		switch (typeIntegerIncoming) {

			case VTTPhysicalActivityLibrary.DETECTION_FALL:
				DataCollectorObserver fallDetection = new FallDetection();

				returnBoolean = dataCollectorObserversArrayList.add(fallDetection);

				break;

			case VTTPhysicalActivityLibrary.DETECTION_LIGHT:
				DataCollectorObserver lightDetection = new LightDetection();

				returnBoolean = dataCollectorObserversArrayList.add(lightDetection);

				break;

			case VTTPhysicalActivityLibrary.DETECTION_ORIENTATION:
				DataCollectorObserver orientationDetection = new OrientationDetection();

				returnBoolean = dataCollectorObserversArrayList.add(orientationDetection);

				break;

			case VTTPhysicalActivityLibrary.DETECTION_PROXIMITY:
				DataCollectorObserver proximityDetection = new ProximityDetection();

				returnBoolean = dataCollectorObserversArrayList.add(proximityDetection);

				break;

			case VTTPhysicalActivityLibrary.DETECTION_RUN:
				DataCollectorObserver runDetection = new RunDetection();

				returnBoolean = dataCollectorObserversArrayList.add(runDetection);

				break;

			case VTTPhysicalActivityLibrary.DETECTION_RUN_AND_WALK:
				DataCollectorObserver runAndWalkDetection = new RunAndWalkDetection();

				returnBoolean = dataCollectorObserversArrayList.add(runAndWalkDetection);

				break;

			case VTTPhysicalActivityLibrary.DETECTION_STABILITY:
				DataCollectorObserver stabilityDetection = new StabilityDetection();

				returnBoolean = dataCollectorObserversArrayList.add(stabilityDetection);

				break;

			case VTTPhysicalActivityLibrary.DETECTION_WALK:
				DataCollectorObserver walkDetection = new WalkDetection();

				returnBoolean = dataCollectorObserversArrayList.add(walkDetection);

				break;

			default:
				break;
		}

		return returnBoolean;
	}

	public boolean isRunning() {
		return dataCollector.isRecording();
	}

	/**
	 * Constructor. 
	 * 
	 * @param  contextIncoming  The application context. 
	 * 
	 */

	public InternalClass(Context contextIncoming) {
		dataCollector = new DataCollector(contextIncoming);
		dataCollector.registerListener(this);
		dataCollectorObserversArrayList = new ArrayList<DataCollectorObserver>();
	}

	/**
	 * Called when data collection is completed. 
	 * 
	 */

	@Override
	public void dataCollectionCompleted() {
		ArrayList<Float> xBuffer = new ArrayList<Float>(dataCollector.getXBuffer());
		ArrayList<Float> yBuffer = new ArrayList<Float>(dataCollector.getYBuffer());
		ArrayList<Float> zBuffer = new ArrayList<Float>(dataCollector.getZBuffer());

		ArrayList<Long> timeBuffer = new ArrayList<Long>(dataCollector.getTimeBuffer());

		if (timeBuffer.size() < 10 && physicalActivityLibraryCallback != null) {
			physicalActivityLibraryCallback.error(VTTPhysicalActivityLibrary.ERROR_NO_ACCELEROMETER_DATA_AVAILABLE);
		}
		else {
			RawData rawData = new RawData();
			rawData.setAccelerometerXBuffer(xBuffer);
			rawData.setAccelerometerYBuffer(yBuffer);
			rawData.setAccelerometerZBuffer(zBuffer);
			rawData.setAccelerometerTimeBuffer(timeBuffer);

			Map<Integer, Double> recognitionsIntegerDoubleMap = new HashMap<Integer, Double>();

			for (int i = 0; i < dataCollectorObserversArrayList.size(); i++) {
				DataCollectorObserver dataCollectorObserver = dataCollectorObserversArrayList.get(i);
				dataCollectorObserver.dataCollectedNotify(rawData);

				int typeInteger = dataCollectorObserver.getIdentifier();

				double valueDouble = dataCollectorObserver.getValue();

				if (typeInteger == VTTPhysicalActivityLibrary.DETECTION_LIGHT) {
					recognitionsIntegerDoubleMap.put(VTTPhysicalActivityLibrary.DETECTION_LIGHT, (double)dataCollector.getLightValue());
				}
				else if (typeInteger == VTTPhysicalActivityLibrary.DETECTION_PROXIMITY) {
					recognitionsIntegerDoubleMap.put(VTTPhysicalActivityLibrary.DETECTION_PROXIMITY, (double)dataCollector.getProximityValue());
				}
				else if (typeInteger == VTTPhysicalActivityLibrary.DETECTION_RUN_AND_WALK) {
					// Special case, because this detector calculates 2 values instead of just one:
					RunAndWalkDetection runAndWalkDetection = (RunAndWalkDetection)dataCollectorObserver;

					double walkValue = runAndWalkDetection.getWalkValue();

					recognitionsIntegerDoubleMap.put(VTTPhysicalActivityLibrary.DETECTION_WALK, walkValue);

					double runValue = runAndWalkDetection.getRunValue();

					recognitionsIntegerDoubleMap.put(VTTPhysicalActivityLibrary.DETECTION_RUN, runValue);

				}
				else {
					recognitionsIntegerDoubleMap.put(typeInteger, valueDouble);
				}
			}

			if (physicalActivityLibraryCallback != null) {
				physicalActivityLibraryCallback.newActivityInfo(recognitionsIntegerDoubleMap);
			}
		}
		dataCollector.recordSnapshot();
	}

	/**
	 * Called if data collection fails. 
	 * 
	 * @param  errorCodeIntegerIncoming  The error type inteer value. 
	 * 
	 */

	@Override
	public void dataCollectionFailed(int errorCodeIntegerIncoming) {
		if (physicalActivityLibraryCallback != null) {
			physicalActivityLibraryCallback.error(errorCodeIntegerIncoming);
		}
	}

	public void setCallback(PhysicalActivityLibraryCallback physicalActivityLibraryCallbackIncoming) {
		physicalActivityLibraryCallback = physicalActivityLibraryCallbackIncoming;
	}

	/**
	 * Starts recording. 
	 * 
	 */

	public void start() {
		if ((dataCollectorObserversArrayList.isEmpty()) && (physicalActivityLibraryCallback != null)) {
			physicalActivityLibraryCallback.error(VTTPhysicalActivityLibrary.ERROR_NO_DETECTIONS_ENABLED);
		}
		else {
			dataCollector.recordSnapshot();
		}
	}

	/**
	 * Stops recording. 
	 * 
	 */

	public void stop() {
		dataCollector.stopRecording();
	}

}