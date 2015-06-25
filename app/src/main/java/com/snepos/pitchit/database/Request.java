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
    public Request(String msg)
    {
        this.msg = msg;
        pairs = new ArrayList<NameValuePair>();
    }

    public void put(String name, String value)
    {
        pairs.add(new BasicNameValuePair(name, value));
    }
}
