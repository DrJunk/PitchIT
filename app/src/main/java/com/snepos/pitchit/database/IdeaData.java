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

    public static IdeaData fromJSON(JSONObject json)
    {
        IdeaData res = new IdeaData(-1,"None", "None", "None", -1, -1, -1);
        try {
            res.title = json.getString("title");
            res.text = json.getString("text");
            res.publisherName = json.getString("user");
            res.upVotes = json.getInt("up_votes");
            res.spamVotes = json.getInt("spam_votes");
            res.imOnItVotes = json.getInt("on_it_votes");
            res.id = json.getInt("id");
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }

        return res;
    }
}
