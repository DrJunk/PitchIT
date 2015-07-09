package com.snepos.pitchit.database;

import com.snepos.pitchit.Card;
import com.snepos.pitchit.MyPitch;

/**
 * Created by User on 12/06/2015.
 */
public class Database {

    public static IdeaData[] news;
    public static IdeaData[] trending;
    public static IdeaData[] hot;

    public static void Init()
    {
        HttpHandler.Init();
    }

    public static void PostRefreshNews()
    {
        HttpHandler.addRequest("get_new_ideas", Request.App.MyPitch);
    }

    public static void PostRefreshTrending()
    {
        HttpHandler.addRequest("get_trending_ideas", Request.App.MyPitch);
    }

    public static void PostRefreshHot()
    {
        HttpHandler.addRequest("get_hot_ideas", Request.App.MyPitch);
    }

    public static void RefreshNews(IdeaData[] data)
    {
        news = data;
    }

    public static void RefreshTrending(IdeaData[] data)
    {
        trending = data;
    }

    public static void RefreshHot(IdeaData[] data)
    {
        hot = data;
    }

    public static void PostUpVote(int ideaId)
    {
        Request req = new Request("add_up_vote", Request.App.MyPitch);
        req.put("idea_id", String.valueOf(ideaId));
        req.put("email", "omer934@walla.co.il");
        HttpHandler.addRequest(req);
    }

    public static void PostUpVoteCanceled(int ideaId)
    {
        Request req = new Request("remove_up_vote", Request.App.MyPitch);
        req.put("idea_id", String.valueOf(ideaId));
        req.put("email", "omer934@walla.co.il");
        HttpHandler.addRequest(req);
    }

    public static void PostSpamVote(int ideaId)
    {
        Request req = new Request("add_spam_vote", Request.App.MyPitch);
        req.put("idea_id", String.valueOf(ideaId));
        req.put("email", "omer934@walla.co.il");
        HttpHandler.addRequest(req);
    }

    public static void PostSpamVoteCanceled(int ideaId)
    {
        Request req = new Request("remove_spam_vote", Request.App.MyPitch);
        req.put("idea_id", String.valueOf(ideaId));
        req.put("email", "omer934@walla.co.il");
        HttpHandler.addRequest(req);
    }

    public static void PostOnItVote(int ideaId)
    {
        Request req = new Request("add_on_it_vote", Request.App.MyPitch);
        req.put("idea_id", String.valueOf(ideaId));
        req.put("email", "omer934@walla.co.il");
        HttpHandler.addRequest(req);
    }

    public static void PostOnItVoteCanceled(int ideaId)
    {
        Request req = new Request("remove_on_it_vote", Request.App.MyPitch);
        req.put("idea_id", String.valueOf(ideaId));
        req.put("email", "omer934@walla.co.il");
        HttpHandler.addRequest(req);
    }
}