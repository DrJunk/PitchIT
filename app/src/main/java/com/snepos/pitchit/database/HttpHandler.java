package com.snepos.pitchit.database;

import android.util.Log;

import com.snepos.pitchit.MyPitch;
import com.snepos.pitchit.MyPost;

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

        System.out.println("Received response to: " + response.msg);
        System.out.println("Response data: " + response.data);

        Handler handler = null;

        switch (response.app)
        {
            case MyPitch:
                handler = MyPitch.mHandler;
                break;
            case MyPost:
                handler = MyPost.mHandler;
                break;
        }

        if(!response.data.startsWith("[200"))
        {
            System.out.println("Error in response: " + response.msg);
            System.out.println("Response's data: " + response.data);
            handler.obtainMessage(0, 0).sendToTarget();
            return;
        }

        if(response.msg == "add_idea")
        {
            handler.obtainMessage(1).sendToTarget();
            return;
        }

        if(     response.msg == "get_new_ideas" ||
                response.msg == "get_trending_ideas" ||
                response.msg == "get_hot_ideas")
        {
            String data = response.data;
            data = data.substring(data.lastIndexOf("["), data.indexOf("]"));
            System.out.println("Data: " + data);
            int left = -1;
            List<IdeaData> ideas = new LinkedList<IdeaData>();
            IdeaData idea;
            for(int i = 0; i < data.length(); i++)
            {
                if(data.charAt(i) == '{')
                {
                    left = i;
                }
                else if(left != -1 && data.charAt(i) == '}')
                {
                    idea = IdeaData.fromJSON(data.substring(left, i + 1));
                    left = -1;
                    if(idea.id != -1)
                        ideas.add(idea);
                }
            }

            IdeaData[] ideasArray = new IdeaData[ideas.size()];
            for(int i = 0; i < ideasArray.length; i++)
                ideasArray[i] = ideas.get(ideasArray.length - i - 1);

            if(response.msg == "get_new_ideas")
                Database.RefreshNews(ideasArray);
            else if(response.msg == "get_trending_ideas")
                Database.RefreshTrending(ideasArray);
            else if(response.msg == "get_hot_ideas")
                Database.RefreshHot(ideasArray);

            return;
        }
    }

    public static void postRequest()
    {
        if (requests.isEmpty())
            return;

        System.out.println("Got a request!");
        try {
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            Request req = requests.poll();
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
        }
    }
}
