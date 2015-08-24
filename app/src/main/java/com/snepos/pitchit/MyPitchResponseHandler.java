package com.snepos.pitchit;

import android.os.Handler;

import com.snepos.pitchit.database.Database;
import com.snepos.pitchit.database.IdeaData;
import com.snepos.pitchit.database.Response;

import org.json.JSONArray;
import org.json.JSONObject;

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
            JSONObject[] jsons = response.GetAsJsonObjectArray();
            for (int i = 0; i < jsons.length; i++) {
                idea = IdeaData.fromJSON(jsons[i]);
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

        if (response.GetMessage() == "get_up_votes" ||
                response.GetMessage() == "get_on_it_votes" ||
                response.GetMessage() == "get_spam_votes") {

            if (!response.IsSucceeded()) {
                handler.obtainMessage(0, 1).sendToTarget();
                return;
            }

            List<Integer> ids = new LinkedList<Integer>();
            Integer ideaId;
            JSONObject[] jsons = response.GetAsJsonObjectArray();
            for (int i = 0; i < jsons.length; i++) {
                ideaId = -1;
                try{ideaId = jsons[i].getInt("id");}
                catch (Exception e){System.out.println(e.getMessage());}
                if (ideaId != -1)
                    ids.add(ideaId);
            }

            Integer[] idsArray = new Integer[ids.size()];
            for (int i = 0; i < idsArray.length; i++)
                idsArray[i] = ids.get(idsArray.length - i - 1);

            if (response.GetMessage() == "get_up_votes")
            {
                Database.RefreshUpVotes(idsArray);
            }
            else if (response.GetMessage() == "get_on_it_votes")
            {
                Database.RefreshOnItVotes(idsArray);
            }
            else if (response.GetMessage() == "get_spam_votes")
            {
                Database.RefreshSpamVotes(idsArray);
            }

            return;
        }

        if(response.GetMessage() == "get_profile")
        {
            JSONArray jsonArray = response.GetAsJsonAray();
            try
            {
                JSONArray myIdeas = (JSONArray)jsonArray.get(0);
                JSONArray upVotedIdeas = (JSONArray)jsonArray.get(1);
                JSONArray onItVotedIdeas = (JSONArray)jsonArray.get(2);
                System.out.println("myIdeas: " + myIdeas.toString());
                System.out.println("upVotedIdeas: " + upVotedIdeas.toString());
                System.out.println("onItVotedIdeas: " + onItVotedIdeas.toString());
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }

            return;
        }

        if (!response.IsSucceeded()) {
            handler.obtainMessage(0, 0).sendToTarget();
            return;
        }
    }
}
