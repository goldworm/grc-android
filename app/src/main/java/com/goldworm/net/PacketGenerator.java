package com.goldworm.net;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by goldworm on 2017-07-28.
 */

public class PacketGenerator {
    public static final int PT_JSON = 100;

    private ByteBuffer byteBuffer;

    public PacketGenerator(int capacity) {
        byteBuffer = ByteBuffer.allocate(capacity);
        byteBuffer.order(ByteOrder.BIG_ENDIAN);
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

        int session = 0;
        byteBuffer = ByteBuffer.allocate(1024);

        setPacketHeader(byteBuffer, session);
        try {
            setJsonBody(byteBuffer, body);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return byteBuffer;
    }
}
