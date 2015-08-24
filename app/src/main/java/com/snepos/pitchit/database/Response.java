package com.snepos.pitchit.database;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by User on 12/06/2015.
 */
public class Response {
    private String msg;
    private String data;
    private Request.App app;
    private boolean success;
    //private JSONObject[] jsons;

    public Response(String msg, String data, Request.App app)
    {
        this.msg = msg;
        this.data = data;
        this.app = app;

        success = false;
        if(data.startsWith("[200"))
        {
            success = true;
            //TODO: After upgrading the server-client communication there will be no need to delete [200, ...]
            this.data = data.substring(5, data.length()-1);
            /*
            data = data.substring(data.lastIndexOf("["), data.indexOf("]"));
            int left = -1;
            List<JSONObject> jsonsList = new LinkedList<JSONObject>();
            for(int i = 0; i < data.length(); i++)
            {
                if(data.charAt(i) == '{')
                {
                    left = i;
                }
                else if(left != -1 && data.charAt(i) == '}')
                {
                    try
                    {
                        jsonsList.add(new JSONObject(data.substring(left, i + 1)));
                    }
                    catch (Exception e){ System.out.println(e.getMessage()); }

                    left = -1;
                }
            }
            jsons = new JSONObject[jsonsList.size()];
            jsonsList.toArray(jsons);*/
        }
    }

    public String GetMessage()
    {
        return msg;
    }

    public String GetData()
    {
        return data;
    }

    public Request.App GetApp()
    {
        return app;
    }

    public boolean IsSucceeded()
    {
        return success;
    }

    public JSONObject[] GetAsJsonObjectArray()
    {
        try
        {
            JSONArray jsonArray = new JSONArray(data);
            JSONObject res[] = new JSONObject[jsonArray.length()];
            for(int i = 0; i < res.length; i++)
                res[i] = jsonArray.getJSONObject(i);
            return res;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return new JSONObject[0];
        }
    }

    public JSONObject GetAsJsonObject()
    {
        try
        {
            return new JSONObject(data);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return new JSONObject();
        }
    }

    public JSONArray GetAsJsonAray()
    {
        try
        {
            return new JSONArray(data);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return new JSONArray();
        }
    }
}
