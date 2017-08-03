package com.goldworm.net;

import com.goldworm.proto.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Created by goldworm on 2017-07-28.
 */

public class PacketGenerator {
    public static final int PT_JSON = 100;

    public PacketGenerator() {
    }

    private void setPacketHeader(ByteBuffer byteBuffer, int session) {
        byteBuffer.clear();
        byteBuffer.putInt(session);
    }

    private void setJsonBody(ByteBuffer byteBuffer, JSONObject body) throws Exception {
        byte[] data = body.toString().getBytes("UTF-8");
        byteBuffer.putInt(PT_JSON);
        byteBuffer.putInt(data.length);
        byteBuffer.put(data);
    }

    public ByteBuffer createControlMouseData(int action, int x, int y) {
        JSONObject body = new JSONObject();
        try {
            body.put("type", Constants.CMD_MOUSE);
            body.put("action", action);
            body.put("x", x);
            body.put("y", y);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return createJsonData(body);
    }

    public ByteBuffer createKeyboardData(int count, int[] keyCodes) {
        JSONObject body = new JSONObject();

        try {
            body.put("type", Constants.CMD_KEYBOARD);

            JSONArray keys = new JSONArray();
            for (int i = 0; i < count; i++) {
                keys.put(keyCodes[i]);
            }
            body.put("keyCodes", keys);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return createJsonData(body);
    }

    private ByteBuffer createJsonData(JSONObject body) {
        byte[] data;

        try {
            data = body.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        return createBinaryData(PT_JSON, data);
    }

    private ByteBuffer createBinaryData(int payloadType, byte[] data) {
        int session = 0;
        int capacity = 3 * 4 + data.length;

        ByteBuffer byteBuffer = ByteBuffer.allocate(capacity);
        byteBuffer.putInt(payloadType);
        byteBuffer.putInt(data.length);
        byteBuffer.putInt(session);
        byteBuffer.put(data);

        byteBuffer.flip();

        return byteBuffer;
    }
}
