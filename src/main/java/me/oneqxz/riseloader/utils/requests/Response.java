package me.oneqxz.riseloader.utils.requests;

import org.json.JSONObject;

public class Response {

    private byte[] resp;
    private int statusCode;

    public int getStatusCode() {
        return statusCode;
    }

    public Response(byte[] resp, int statusCode) {
        this.resp = resp;
        this.statusCode = statusCode;
    }

    public byte[] getBytes() {
        return resp;
    }

    public String getString()
    {
        return new String(resp);
    }

    public JSONObject getJSON()
    {
        return new JSONObject(getString());
    }
}
