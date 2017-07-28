package com.goldworm.net;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by goldworm on 2017-07-28.
 */
public class RemoteControlTest {
    @Test
    public void start() throws Exception {

    }

    @Test
    public void asyncSend() throws Exception {
        RemoteControl remoteControl = RemoteControl.getInstance();
        assertTrue(remoteControl != null);

        remoteControl.start();
        Thread.sleep(2000);

        String data = "hello";
        remoteControl.send(data.getBytes());

        Thread.sleep(2000);
        remoteControl.stop();
    }
}