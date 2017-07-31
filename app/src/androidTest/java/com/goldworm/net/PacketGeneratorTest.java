package com.goldworm.net;

import android.support.test.runner.AndroidJUnit4;
import android.util.JsonReader;

import org.json.JSONArray;
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

        PacketGenerator generator = new PacketGenerator();
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

    @Test
    public void createKeyboardData() throws Exception {
        int[] point = {100, 200};

        PacketGenerator generator = new PacketGenerator();
        assertFalse(generator == null);

        int[] keyCodes = new int[2];
        keyCodes[0] = VirtualKey.VK_ALT;
        keyCodes[1] = VirtualKey.VK_F4;

        ByteBuffer packet = generator.createKeyboardData(keyCodes.length, keyCodes);
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
        assertEquals(Constants.CMD_KEYBOARD, type);
        JSONArray keyArray = root.getJSONArray("keyCodes");
        assertEquals(keyCodes.length, keyArray.length());
        for (int i = 0; i < keyCodes.length; i++) {
            assertEquals(keyCodes[i], keyArray.getInt(i));
        }
    }
}