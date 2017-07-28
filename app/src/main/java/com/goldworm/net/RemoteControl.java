package com.goldworm.net;

import android.animation.ObjectAnimator;
import android.util.Log;

/**
 * Created by goldworm on 2017-07-28.
 */

public class RemoteControl {
    private static RemoteControl self;
    private Thread workerThread;

    public static RemoteControl getInstance() {
        if (self == null) {
            self = new RemoteControl();
        }

        return self;
    }

    private RemoteControl() {
    }

    public void start() {
        workerThread = new WorkerThread();
        workerThread.start();
    }

    public void stop() {
        if (workerThread != null) {
            try {
                workerThread.interrupt();
                workerThread.join(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class WorkerThread extends Thread {
        public WorkerThread() {
        }

        @Override
        public void run() {
            try {
                synchronized (this) {
                    wait();
                }
                Log.d("grc", "Worker wakes up");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
