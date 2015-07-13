package com.snepos.pitchit.database;

import android.util.Log;

import com.snepos.pitchit.Login;
import com.snepos.pitchit.MyPitch;
import com.snepos.pitchit.MyPost;
import com.snepos.pitchit.Signup;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.*;
import java.util.logging.ConsoleHandler;

import android.os.Handler;

public class HttpHandler {

    static Queue<Request> requests;
    static Queue<Response> responses;

    public static void Init() {
        requests = new LinkedList<Request>();
        responses = new LinkedList<Response>();

        new Thread() {
            public void run() {
                while (true) {
                    postRequest();
                    handleResponse();

                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        System.out.println("Thread sleep error: " + e.getMessage());
                    }
                }
            }
        }.start();
    }

    public static void addRequest(String msg, Request.App app) {
        Request req = new Request(msg, app);
        requests.add(req);
    }

    public static void addRequest(Request req) {
        requests.add(req);
    }

    public static void handleResponse()
    {
        if (responses.isEmpty())
            return;

        Response response = responses.poll();

        System.out.println("Received response to: " + response.GetMessage());
        System.out.println("Response data: " + response.GetData());

        if(!response.IsSucceeded())
        {
            System.out.println("Error in response: " + response.GetMessage());
            System.out.println("Response's data: " + response.GetData());
        }

        switch (response.GetApp())
        {
            case MyPitch:
                MyPitch.HandleResponse(response);
                break;
            case MyPost:
                MyPost.HandleResponse(response);
                break;
            case Login:
                Login.HandleResponse(response);
                break;
            case Register:
                Signup.HandleResponse(response);
                break;
        }
    }

    public static void postRequest()
    {
        if (requests.isEmpty())
            return;

        System.out.println("Got a request!");
        Request req = requests.poll();
        try {
            String msg = req.msg;
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://pitchitserver-pitchit.rhcloud.com/app/" + msg);
            httpPost.setEntity(new UrlEncodedFormEntity(req.pairs));
            HttpResponse httpresponse = httpclient.execute(httpPost);
            String responseText = null;
            try {
                responseText = EntityUtils.toString(httpresponse.getEntity());
            } catch (ParseException e) {
                e.printStackTrace();
                Log.i("Parse Exception", e + "");
            }
            responses.add(new Response(msg, responseText, req.app));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            responses.add(new Response(req.msg, "Error: code 1", req.app));
        }
    }
}
