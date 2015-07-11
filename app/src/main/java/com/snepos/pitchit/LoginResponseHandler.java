package com.snepos.pitchit;

import android.os.Handler;

import com.snepos.pitchit.database.Response;

/**
 * Created by Omer on 07/07/2015.
 */
public class LoginResponseHandler
{
    public static void HandleResponse(Handler handler, Response response)
    {
        if(!response.IsSucceeded())
        {
            if(response.GetData().equals("11")) {
                handler.obtainMessage(2).sendToTarget();
                return;
            }

            handler.obtainMessage(0);
            return;
        }
        if(response.GetMessage() == "login")
        {
            handler.obtainMessage(1).sendToTarget();
            return;
        }
    }
}
