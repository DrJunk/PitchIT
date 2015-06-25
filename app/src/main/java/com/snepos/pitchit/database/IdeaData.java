package com.snepos.pitchit.database;

import org.json.JSONObject;

/**
 * Created by User on 12/06/2015.
 */
public class IdeaData {
    public int id;
    public String title;
    public String text;
    public String publisherName;
    public int upVotes;
    public int spamVotes;
    public int imOnItVotes;
    public IdeaData(int id, String title, String text, String publisherName, int upVotes, int spamVotes, int imOnItVotes)
    {
        this.id = id;
        this.title = title;
        this.text = text;
        this.publisherName = publisherName;
        this.upVotes = upVotes;
        this.spamVotes = spamVotes;
        this.imOnItVotes = imOnItVotes;
    }

    public static IdeaData fromJSON(String json)
    {
        IdeaData res = new IdeaData(-1,"None", "None", "None", -1, -1, -1);
        try {
            JSONObject obj = new JSONObject(json);
            res.title = obj.getString("title");
            res.text = obj.getString("text");
            res.publisherName = obj.getString("user");
            res.upVotes = obj.getInt("up_votes");
            res.spamVotes = obj.getInt("spam_votes");
            res.imOnItVotes = obj.getInt("on_it_votes");
            res.id = obj.getInt("id");
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }

        return res;
    }
}
