package com.goldworm.net;

import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created by goldworm on 2017-07-28.
 */

public class RemoteControl {
    private static final String TAG = "grc";

    private static RemoteControl self;
    private boolean running = false;
    private WorkerThread workerThread;
    private PacketGenerator packetGenerator = new PacketGenerator();

    public static RemoteControl getInstance() {
        if (self == null) {
            self = new RemoteControl();
        }

        return self;
    }

    private RemoteControl() {
    }

    public void start() {
        running = true;
        workerThread = new WorkerThread();
        workerThread.start();
    }

    public void stop() {
        running = false;

        if (workerThread != null) {
            try {
                workerThread.interrupt();
                workerThread.join(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int controlKeyboard(int count, int[] keyCodes) {
        ByteBuffer buffer = packetGenerator.createKeyboardData(count, keyCodes);
        if (buffer == null) {
            return -1;
        }

        workerThread.asyncSend(buffer);
        return 0;
    }

    public int controlMouse(int action, int x, int y) {
        ByteBuffer buffer = packetGenerator.createControlMouseData(action, x, y);
        if (buffer == null) {
            return -1;
        }

        workerThread.asyncSend(buffer);
        return 0;
    }

    public void sendText(String text) {

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
        Selector selector;

        public WorkerThread() {
        }

        private void init() {
            InetSocketAddress hostAddress = new InetSocketAddress("192.168.0.2", 1106);
            try {
                selector = Selector.open();

                channel = DatagramChannel.open();
                channel.connect(hostAddress);
                channel.configureBlocking(false);
                channel.register(selector, SelectionKey.OP_READ);
            } catch (IOException e) {
                e.printStackTrace();
                channel = null;
            }
        }

        private void exit() {
            try {
                if (selector != null) {
                    selector.close();
                }

                if (channel != null) {
                    channel.disconnect();
                    channel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                channel = null;
                selector = null;
            }
        }

        @Override
        public void run() {
            init();

            try {
                while (running && !this.isInterrupted()) {
                    int readyChannels = selector.select();
                    if (readyChannels == 0) {
                        continue;
                    }

                    Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    Iterator<SelectionKey> it = selectedKeys.iterator();
                    while (it.hasNext()) {
                        SelectionKey key = it.next();
                        ByteChannel channel = (ByteChannel)key.channel();

                        if (key.isReadable()) {
                            Log.d(TAG, "key is readable.");
                            doRead(key, channel);
                        } else if (key.isWritable()) {
                            Log.d(TAG, "key is writable.");
                            doWrite(key, channel);
                        }

                        it.remove();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            exit();
        }

        private void doRead(SelectionKey key, ByteChannel channel) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            try {
                int size = channel.read(buffer);
                Log.d(TAG, "read() returns " + size);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void doWrite(SelectionKey key, ByteChannel channel) {
            synchronized (sendQueue) {
                if (sendQueue.isEmpty()) {
                    // If there is no bug in this class, the code below shouldn't be called.
                    key.interestOps(SelectionKey.OP_READ);
                    return;
                }

                try {
                    ByteBuffer buffer = sendQueue.removeFirst();
                    if (buffer != null) {
                        channel.write(buffer);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (sendQueue.isEmpty()) {
                        key.interestOps(SelectionKey.OP_READ);
                    }
                }
            }
        }

        public void asyncSend(ByteBuffer data) {
            synchronized (sendQueue) {
                sendQueue.push(data);

                SelectionKey key = channel.keyFor(selector);
                key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            }

            selector.wakeup();
        }
    }
}
