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
 * Orientation detection, implements DataCollectorObserver. 
 * 
 * @see  fi.vtt.physicalactivitylibrary.internal.DataCollectorObserver 
 *
 */

public class OrientationDetection implements DataCollectorObserver {

	private double currentOrientationDouble;

	@Override
	public double getValue() {
		return currentOrientationDouble;
	}

	@Override
	public int getIdentifier() {
		return VTTPhysicalActivityLibrary.DETECTION_ORIENTATION;
	}

	@Override
	public String getTag() {
		return "OrientationDetection";
	}

	public OrientationDetection() {
		currentOrientationDouble = VTTPhysicalActivityLibrary.ORIENTATION_UNDEFINED;
	}

	@Override
	public void dataCollectedNotify(RawData rawDataIncoming) {
		if (!rawDataIncoming.hasAccelerometerData()) {
			currentOrientationDouble = VTTPhysicalActivityLibrary.ORIENTATION_UNDEFINED;
			return;
		}

		ArrayList<Float> xFloatArrayList = rawDataIncoming.getAccelerometerXBuffer();
		ArrayList<Float> yFloatArrayList = rawDataIncoming.getAccelerometerYBuffer();
		ArrayList<Float> zFloatArrayList = rawDataIncoming.getAccelerometerZBuffer();

		float xMeanFloat = 0.0f;
		float yMeanFloat = 0.0f;
		float zMeanFloat = 0.0f;

		for (int i = 0; i < xFloatArrayList.size(); i++) {
			xMeanFloat += xFloatArrayList.get(i);
			yMeanFloat += yFloatArrayList.get(i);
			zMeanFloat += zFloatArrayList.get(i);
		}

		xMeanFloat = xMeanFloat / xFloatArrayList.size();
		yMeanFloat = yMeanFloat / yFloatArrayList.size();
		zMeanFloat = zMeanFloat / zFloatArrayList.size();

		double orientationDouble = VTTPhysicalActivityLibrary.ORIENTATION_UNDEFINED;

		if (xMeanFloat > 7.35) {
			orientationDouble = VTTPhysicalActivityLibrary.ORIENTATION_RIGHT_UP;
		}
		else if (xMeanFloat < -7.35) {
			orientationDouble = VTTPhysicalActivityLibrary.ORIENTATION_LEFT_UP;
		}
		else if (yMeanFloat > 7.35) {
			orientationDouble = VTTPhysicalActivityLibrary.ORIENTATION_TOP_UP;
		}
		else if (yMeanFloat < -7.35) {
			orientationDouble = VTTPhysicalActivityLibrary.ORIENTATION_TOP_DOWN;
		}
		else if (zMeanFloat > 7.35) {
			orientationDouble = VTTPhysicalActivityLibrary.ORIENTATION_FACE_UP;
		}
		else if (zMeanFloat < -7.35) {
			orientationDouble = VTTPhysicalActivityLibrary.ORIENTATION_FACE_DOWN;
		}

		currentOrientationDouble = orientationDouble;
	}

}