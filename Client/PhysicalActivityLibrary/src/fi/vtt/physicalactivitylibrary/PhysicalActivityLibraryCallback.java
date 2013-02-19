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

import java.util.Map;

/**
 * Interface for callbacks, implement this in your application.
 * 
 */

public interface PhysicalActivityLibraryCallback {

	/**
	 * This method is called when an error occurs. 
	 * <p> 
	 * Implement this in your application. 
	 * 
	 * @param  errorCodeIntegerIncoming  Type of the error. 
	 * 
	 */

	abstract void error(int errorCodeIntegerIncoming);

	/**
	 * This method is called when new physical activity data is available.
	 * <p>
	 * Implement this in your application.
	 * 
	 * @param  infoMapIncoming  Contains the current physical information. 
	 * 				 			<p>
	 * 				 			Key is detection type, value is value in range of [0.0, 1.0]. 
	 * 				 			<p>
	 *               			For example: Key = 1, Value = 1.0 means walking type and value 1.0 
	 *               			is the value from the walking detection method. 
	 * 
	 */

	abstract void newActivityInfo(Map<Integer, Double> infoMapIncoming);

}