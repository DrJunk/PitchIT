package com.snepos.pitchit;

/**
 * Created by Omer on 25/08/2015.
 */

import android.os.Handler;

import com.snepos.pitchit.database.Response;

import org.json.JSONObject;

public class CommentsResponseHandler
{
    public static void HandleResponse(Handler handler, Response response)
    {
        System.out.println("respmsg: " + response.GetMessage());
        if(!response.IsSucceeded()) {
            if (response.GetMessage().equals("get_comments"))
                handler.obtainMessage(0, 1, 0).sendToTarget();
            else if (response.GetMessage().equals("add_comment"))
                handler.obtainMessage(0, 2, 0).sendToTarget();
            else
                handler.obtainMessage(0, 0, 0).sendToTarget();

            return;
        }
        if(response.GetMessage().equals("get_comments"))
        {
            try
            {
                JSONObject[] jsons = response.GetAsJsonObjectArray();
                PitchComment[] comments = new PitchComment[jsons.length];
                for(int i = 0; i < comments.length; i++)
                {
                    comments[i] = new PitchComment(i, jsons[i].getString("user__nick_name"), jsons[i].getString("text"));
                }
                handler.obtainMessage(1, comments).sendToTarget();
            }
            catch (Exception e)
            {
                handler.obtainMessage(0);
            }
            return;
        }
        if(response.GetMessage().equals("get_comments"))
        {
            handler.obtainMessage(2);
        }
    }
}
