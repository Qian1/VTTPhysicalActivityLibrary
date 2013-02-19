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

/**
 * Abstract class for getting information from the DataCollector. 
 * <p>
 * Implemented by detection classes. 
 * 
 * @see  fi.vtt.physicalactivitylibrary.internal.FallDetection 
 * @see  fi.vtt.physicalactivitylibrary.internal.LightDetection 
 * @see  fi.vtt.physicalactivitylibrary.internal.OrientationDetection 
 * @see  fi.vtt.physicalactivitylibrary.internal.ProximityDetection 
 * @see  fi.vtt.physicalactivitylibrary.internal.RunAndWalkDetection 
 * @see  fi.vtt.physicalactivitylibrary.internal.RunDetection 
 * @see  fi.vtt.physicalactivitylibrary.internal.StabilityDetection 
 * @see  fi.vtt.physicalactivitylibrary.internal.WalkDetection 
 * 
 */

public interface DataCollectorObserver {

	 /**
      * Method to be implemented in all detection classes. 
      *
      * @return  Detection value (valid range is [0.0, 1.0]). 
      * 
      */

    public abstract double getValue();

    /**
     * Method to be implemented in all detection classes. 
     * 
     * @return  A unique identifier for this detection (detection type). 
     * 
     */

    public abstract int getIdentifier();

    /**
     * Method to be implemented in all detection classes. 
     *
     * @return  Type tag for this detection type. e.g "walking" or "run". 
     * 
     */

	public abstract String getTag();

	/**
	 * Virtual callback to be implemented in all detection classes. 
	 * 
	 * @param  rawDataIncoming  Contains all the snapshot raw data recorded from the sensors. 
	 * 
	 */

	public abstract void dataCollectedNotify(RawData rawDataIncoming);

}