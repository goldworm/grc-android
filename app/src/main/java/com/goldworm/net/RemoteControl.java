package com.goldworm.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.LinkedList;

/**
 * Created by goldworm on 2017-07-28.
 */

public class RemoteControl {
    private static RemoteControl self;
    private WorkerThread workerThread;

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

    public void send(byte[] data) {
        ByteBuffer buffer = ByteBuffer.allocate(data.length);
        buffer.put(data);
        buffer.rewind();
        workerThread.asyncSend(buffer);
    }

    private class WorkerThread extends Thread {
        private DatagramChannel channel;
        LinkedList<ByteBuffer> sendQueue = new LinkedList<>();

        public WorkerThread() {
        }

        private void init() {
            InetSocketAddress hostAddress = new InetSocketAddress("192.168.0.2", 1106);
            try {
                channel = DatagramChannel.open();
                channel.connect(hostAddress);
            } catch (IOException e) {
                e.printStackTrace();
                channel = null;
            }
        }

        private void exit() {
            if (channel != null) {
                try {
                    channel.disconnect();
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    channel = null;
                }
            }
        }

        @Override
        public void run() {
            init();

            while (!this.isInterrupted()) {
                try {
                    ByteBuffer data = getSendDataFromQueue();
                    if (data != null) {
                        channel.write(data);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            exit();
        }

        private synchronized ByteBuffer getSendDataFromQueue() {
            if (sendQueue.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            ByteBuffer data = null;
            if (!sendQueue.isEmpty()) {
                data = sendQueue.removeFirst();
            }

            return data;
        }

        public synchronized void asyncSend(ByteBuffer data) {
            sendQueue.push(data);
            notify();
        }
    }
}
