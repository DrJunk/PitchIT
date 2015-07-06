package com.snepos.pitchit.database;

/**
 * Created by User on 12/06/2015.
 */
public class Response {
    public String msg;
    public String data;
    public Request.App app;

    public Response(String msg, String data, Request.App app)
    {
        this.msg = msg;
        this.data = data;
        this.app = app;
    }
}
