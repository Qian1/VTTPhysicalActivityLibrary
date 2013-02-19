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

package fi.vtt.physicalactivitylibrary.internal.utils;

import java.util.EventListener;

public class MyTimer implements Runnable {

    private boolean runningBoolean = false;

    private EventListener eventListener;

    private long elapsedTimeLong;
    private long intervalLong;

    private Thread thread;

    /**
     * Is the timer running or not. 
     * 
     * @return  runningBoolean  True if the time is running, otherwise false. 
     * 
     */

    public boolean isRunning() {
        return runningBoolean;
    }

    /**
     * Get the elapsed timer time. 
     * 
     * @return  elapsedTimeLong  The elapsed time in milliseconds in a Long. 
     * 
     */

    public long elapsed() {
        return elapsedTimeLong;
    }

    /**
     * Constructor. 
     * 
     */

    public MyTimer() {}

    /**
     * Cancel the timer. 
     * 
     */

    public void cancel() {
        runningBoolean = false;
    }

    /**
     * Run the timer thread. 
     * 
     */

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        while ((elapsedTimeLong < intervalLong) && isRunning()) {
            elapsedTimeLong = System.currentTimeMillis() - startTime;

            try {
                Thread.sleep(1);
            }
            catch (InterruptedException interruptedExceptionIncoming) {}
        }

        if (isRunning()) {
            runningBoolean = false;

            intervalLong = 0;

            ((MyTimerListener)eventListener).timeout();
        }
    }

    /**
     * Set the interval time. 
     * 
     * @param  intervalLongIncoming  The interval in milliseconds in a Long. 
     * 
     */

    public void setInterval(final long intervalLongIncoming) {
        intervalLong = intervalLongIncoming;
    }

    /**
     * Set the timer listener. 
     * 
     * @param  myTimerListenerIncoming  The incoming class that implements MyTimerListener. 
     * 
     * @see  fi.vtt.physicalactivitylibrary.internal.utils.MyTimerListener 
     * 
     */

    public void setListener(MyTimerListener myTimerListenerIncoming) {
        eventListener = myTimerListenerIncoming;
    }

    /**
     * Start the timer. 
     * 
     * @param  intervalLong  The interval in milliseconds in a Long. 
     * 
     */

    public void start(long intervalLongIncoming) {
    	runningBoolean = true;

    	elapsedTimeLong = Long.MIN_VALUE;
        intervalLong = intervalLongIncoming;

        thread = new Thread(this);
        thread.start();
    }

}