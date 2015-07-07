package com.snepos.pitchit;

import android.os.Handler;

import com.snepos.pitchit.database.Database;
import com.snepos.pitchit.database.IdeaData;
import com.snepos.pitchit.database.Response;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Omer on 07/07/2015.
 */
public class MyPitchResponseHandler {
    public static void HandleResponse(Handler handler, Response response) {
        if (response.GetMessage() == "get_new_ideas" ||
                response.GetMessage() == "get_trending_ideas" ||
                response.GetMessage() == "get_hot_ideas") {

            if (!response.IsSucceeded()) {
                handler.obtainMessage(0, 1).sendToTarget();
                return;
            }

            List<IdeaData> ideas = new LinkedList<IdeaData>();
            IdeaData idea;
            for (int i = 0; i < response.GetJsonObjects().length; i++) {
                idea = IdeaData.fromJSON(response.GetJsonObjects()[i]);
                if (idea.id != -1)
                    ideas.add(idea);
            }

            IdeaData[] ideasArray = new IdeaData[ideas.size()];
            for (int i = 0; i < ideasArray.length; i++)
                ideasArray[i] = ideas.get(ideasArray.length - i - 1);

            if (response.GetMessage() == "get_new_ideas")
            {
                Database.RefreshNews(ideasArray);
                handler.obtainMessage(1, 0).sendToTarget();
            }
            else if (response.GetMessage() == "get_trending_ideas")
            {
                Database.RefreshTrending(ideasArray);
                handler.obtainMessage(1, 1).sendToTarget();
            }
            else if (response.GetMessage() == "get_hot_ideas")
            {
                Database.RefreshHot(ideasArray);
                handler.obtainMessage(1, 2).sendToTarget();
            }

            return;
        }

        if (!response.IsSucceeded()) {
            handler.obtainMessage(0, 0).sendToTarget();
            return;
        }
    }
}
