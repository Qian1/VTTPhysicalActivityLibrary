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
 * Walk detection, implements DataCollectorObserver. 
 * 
 * @see  fi.vtt.physicalactivitylibrary.internal.DataCollectorObserver 
 *
 */

public final class WalkDetection implements DataCollectorObserver {

	private double walkValueDouble = 0.0;

	@Override
	public double getValue() {
		return walkValueDouble;
	}

	@Override
	public int getIdentifier() {
		return VTTPhysicalActivityLibrary.DETECTION_WALK;
	}

	public native double doWalkDetection(float[] xFloatArrayIncoming, float[] yFloatArrayIncoming, float[] zFloatArrayIncoming, long[] timeLongArrayIncoming);

	@Override
	public String getTag() {
		return "WalkDetection";
	}

	@Override
	public void dataCollectedNotify(RawData rawDataIncoming) {
		if (!rawDataIncoming.hasAccelerometerData()) {
			walkValueDouble = 0.0; // No data. 
			return; // No reason to continue without data. 
		}

		ArrayList<Float> xFloatArrayList = rawDataIncoming.getAccelerometerXBuffer();
		ArrayList<Float> yFloatArrayList = rawDataIncoming.getAccelerometerYBuffer();
		ArrayList<Float> zFloatArrayList = rawDataIncoming.getAccelerometerZBuffer();

		ArrayList<Long> timeLongArrayList = rawDataIncoming.getAccelerometerTimeBuffer();

		if (timeLongArrayList.size() < 16) {
			walkValueDouble = 0.0; // No data. 
			return; // Cannot continue with too few data points. 
		}

		float[] xFloatArray = new float[xFloatArrayList.size()];
		float[] yFloatArray = new float[yFloatArrayList.size()];
		float[] zFloatArray = new float[zFloatArrayList.size()];

		long[] timeLongArray = new long[timeLongArrayList.size()];

		for (int i = 0; i < xFloatArrayList.size(); i++) {
			xFloatArray[i] = xFloatArrayList.get(i);
		}

		for (int i = 0; i < yFloatArrayList.size(); i++) {
			yFloatArray[i] = yFloatArrayList.get(i);
		}

		for (int i = 0; i < zFloatArrayList.size(); i++) {
			zFloatArray[i] = zFloatArrayList.get(i);
		}

		for (int i = 0; i < timeLongArrayList.size(); i++) {
			timeLongArray[i] = timeLongArrayList.get(i);
		}

		walkValueDouble = doWalkDetection(xFloatArray, yFloatArray, zFloatArray, timeLongArray);
	}

}