package com.goldworm.net;

import android.support.test.runner.AndroidJUnit4;
import android.util.JsonReader;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.nio.ByteBuffer;

import static org.junit.Assert.*;

/**
 * Created by goldworm on 2017-07-28.
 */
public class PacketGeneratorTest {
    @Test
    public void createControlMouseData() throws Exception {
        int[] point = {100, 200};

        PacketGenerator generator = new PacketGenerator(1024);
        assertFalse(generator == null);

        ByteBuffer packet = generator.createControlMouseData(Constants.ACTION_MOVE, point[0], point[1]);
        assertFalse(packet == null);

        packet.rewind();

        int session = packet.getInt();
        assertEquals(0, session);
        int payloadType = packet.getInt();
        assertEquals(PacketGenerator.PT_JSON, payloadType);
        int bodySize = packet.getInt();
        assertTrue(bodySize > 0);

        byte[] data = new byte[bodySize];
        packet.get(data);

        String json = new String(data, "UTF-8");
        JSONTokener tokener = new JSONTokener(json);
        JSONObject root = (JSONObject) tokener.nextValue();
        assertTrue(root != null);

        int type = root.getInt("type");
        assertEquals(Constants.CMD_MOUSE, type);
        int action = root.getInt("action");
        assertEquals(Constants.ACTION_MOVE, action);
        int x = root.getInt("x");
        assertEquals(point[0], x);
        int y = root.getInt("y");
        assertEquals(point[1], y);
    }
}