package com.snepos.pitchit.database;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 13/06/2015.
 */
public class Request {

    public String msg;
    public List<NameValuePair> pairs;

    public enum App{MyPitch, MyPost, Login, Register, Comments};
    public App app;

    public Request(String msg, App app)
    {
        this.msg = msg;
        this.app = app;
        pairs = new ArrayList<NameValuePair>();
    }

    public void put(String name, String value)
    {
        pairs.add(new BasicNameValuePair(name, value));
    }
}
