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

import fi.vtt.physicalactivitylibrary.VTTPhysicalActivityLibrary;
import java.util.ArrayList;

/**
 * Stability detection, implements DataCollectorObserver. 
 * 
 * @see  fi.vtt.physicalactivitylibrary.internal.DataCollectorObserver 
 *
 */

public final class StabilityDetection implements DataCollectorObserver {

	private double currentStabilityValueDouble;
	private double lastVarianceValueDouble;

	// Threshold value, if the variance is under this device is stabile, otherwise unstabile:

	static final double THRESHOLD_VALUE_DOUBLE = 0.03;

	public double getLastVariance() {
		return lastVarianceValueDouble;
	}

	@Override
	public double getValue() {
		return currentStabilityValueDouble;
	}

	@Override
	public int getIdentifier() {
		return VTTPhysicalActivityLibrary.DETECTION_STABILITY;
	}

	@Override
	public String getTag() {
		return "StabilityDetection";
	}

	@Override
	public void dataCollectedNotify(RawData rawDataIncoming) {
		if (!rawDataIncoming.hasAccelerometerData()) {
			// currentStabilityValueDouble = 0.0; // No data. 
			return; // Not able to continue. 
		}

		double meanDouble = 0.0;

		ArrayList<Double> valuesDoubleArrayList = new ArrayList<Double>(); // Total energy. 

		ArrayList<Float> xFloatArrayList = rawDataIncoming.getAccelerometerXBuffer();
		ArrayList<Float> yFloatArrayList = rawDataIncoming.getAccelerometerYBuffer();
		ArrayList<Float> zFloatArrayList = rawDataIncoming.getAccelerometerZBuffer();

		int accelerometerElementCountInteger = xFloatArrayList.size();

		for (int i = 0; i < accelerometerElementCountInteger; i++) {
			double valueDouble = Math.sqrt(Math.pow(xFloatArrayList.get(i), 2) + Math.pow(yFloatArrayList.get(i), 2) + Math.pow(zFloatArrayList.get(i), 2));
			meanDouble += valueDouble;

			valuesDoubleArrayList.add(valueDouble);
		}

		meanDouble = meanDouble / (double)accelerometerElementCountInteger;
		double sumDouble = 0.0;

		for (int i = 0; i < valuesDoubleArrayList.size(); i++) {
			double valueDouble = Math.pow(valuesDoubleArrayList.get(i) - meanDouble, 2);
			sumDouble += valueDouble;
		}

		double variance = sumDouble / (double) valuesDoubleArrayList.size();
		lastVarianceValueDouble = variance;

		if (variance > THRESHOLD_VALUE_DOUBLE) {
			currentStabilityValueDouble = 0.0; // Unstabile.
		}
		else {
			currentStabilityValueDouble = 1.0; // Stabile.
		}
	}

}