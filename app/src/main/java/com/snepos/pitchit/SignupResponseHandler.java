package com.snepos.pitchit;

import android.os.Handler;

import com.snepos.pitchit.database.Response;

/**
 * Created by Omer on 07/07/2015.
 */
public class SignupResponseHandler
{
    public static void HandleResponse(Handler handler, Response response)
    {
        if(!response.IsSucceeded())
        {
            handler.obtainMessage(0, 0).sendToTarget();
            return;
        }
        if(response.GetMessage() == "register")
        {
            handler.obtainMessage(1).sendToTarget();
            return;
        }
    }
}
