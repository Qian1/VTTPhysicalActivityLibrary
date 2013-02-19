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

import java.util.ArrayList;

/**
 * A simple container class for collected raw data with getters and setters. 
 * 
 */

public final class RawData {

	private ArrayList<Float> xFloatArrayList;
	private ArrayList<Float> yFloatArrayList;
	private ArrayList<Float> zFloatArrayList;

	private ArrayList<Long> timeLongArrayList;

	public ArrayList<Float> getAccelerometerXBuffer() {
		return xFloatArrayList;
	}

	public ArrayList<Float> getAccelerometerYBuffer() {
		return yFloatArrayList;
	}

	public ArrayList<Float> getAccelerometerZBuffer() {
		return zFloatArrayList;
	}

	public ArrayList<Long> getAccelerometerTimeBuffer() {
		return timeLongArrayList;
	}

	public boolean hasAccelerometerData() {
		return xFloatArrayList.size() > 0;
	}

	public boolean hasCellIDData() {
		return false;
	}

	public boolean hasDevicePositionData() {
		return false;
	}

	public boolean hasDeviceStabilityData() {
		return false;
	}

	public boolean hasGPSData() {
		return false;
	}

	public void setAccelerometerTimeBuffer(ArrayList<Long> timeLongArrayListIncoming) {
		timeLongArrayList = timeLongArrayListIncoming;
	}

	public void setAccelerometerXBuffer(ArrayList<Float> xFloatArrayListIncoming) {
		xFloatArrayList = xFloatArrayListIncoming;
	}

	public void setAccelerometerYBuffer(ArrayList<Float> yFloatArrayListIncoming) {
		yFloatArrayList = yFloatArrayListIncoming;
	}

	public void setAccelerometerZBuffer(ArrayList<Float> zFloatArrayListIncoming) {
		zFloatArrayList = zFloatArrayListIncoming;
	}

}