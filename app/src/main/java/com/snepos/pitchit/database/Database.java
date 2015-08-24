package com.snepos.pitchit.database;

import android.content.Intent;
import android.util.Log;

import com.snepos.pitchit.Card;
import com.snepos.pitchit.Login;
import com.snepos.pitchit.MyPitch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by User on 12/06/2015.
 */
public class Database {

    public static IdeaData[] news;
    public static IdeaData[] trending;
    public static IdeaData[] hot;

    public static List<Integer> up_votes;
    public static List<Integer> on_it_votes;
    public static List<Integer> spam_votes;

    public static void Init()
    {
        HttpHandler.Init();

        up_votes = new ArrayList<Integer>();
        on_it_votes = new ArrayList<Integer>();
        spam_votes = new ArrayList<Integer>();
    }

    private static IdeaData getIdea(int id)
    {
        for(int i = 0; i < news.length; i++)
            if(news[i].id == id)
                return news[i];
        for(int i = 0; i < trending.length; i++)
            if(trending[i].id == id)
                return trending[i];
        for(int i = 0; i < hot.length; i++)
            if(hot[i].id == id)
                return hot[i];
        return null;
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

    public static void PostRefreshProfile()
    {
        Request req = new Request("get_profile", Request.App.MyPitch);
        req.put("email", Login.GetUserEmail());
        HttpHandler.addRequest(req);
    }

    public static void PostRefreshUpVotes()
    {
        Request req = new Request("get_up_votes", Request.App.MyPitch);
        req.put("email", Login.GetUserEmail());
        HttpHandler.addRequest(req);
    }

    public static void PostRefreshOnItVotes()
    {
        Request req = new Request("get_on_it_votes", Request.App.MyPitch);
        req.put("email", Login.GetUserEmail());
        HttpHandler.addRequest(req);
    }

    public static void PostRefreshSpamVotes()
    {
        Request req = new Request("get_spam_votes", Request.App.MyPitch);
        req.put("email", Login.GetUserEmail());
        HttpHandler.addRequest(req);
    }

    public static void PostUpVote(int ideaId)
    {
        Request req = new Request("add_up_vote", Request.App.MyPitch);
        req.put("idea_id", String.valueOf(ideaId));
        req.put("email", Login.GetUserEmail());
        HttpHandler.addRequest(req);

        if(!up_votes.contains(ideaId))
            up_votes.add(ideaId);
    }

    public static void PostUpVoteCanceled(int ideaId)
    {
        Request req = new Request("remove_up_vote", Request.App.MyPitch);
        req.put("idea_id", String.valueOf(ideaId));
        req.put("email", Login.GetUserEmail());
        HttpHandler.addRequest(req);

        if(up_votes.contains(ideaId))
            up_votes.remove((Integer) ideaId);
    }

    public static void PostSpamVote(int ideaId)
    {
        Request req = new Request("add_spam_vote", Request.App.MyPitch);
        req.put("idea_id", String.valueOf(ideaId));
        req.put("email", Login.GetUserEmail());
        HttpHandler.addRequest(req);

        if(!spam_votes.contains(ideaId))
            spam_votes.add(ideaId);
    }

    public static void PostSpamVoteCanceled(int ideaId)
    {
        Request req = new Request("remove_spam_vote", Request.App.MyPitch);
        req.put("idea_id", String.valueOf(ideaId));
        req.put("email", Login.GetUserEmail());
        HttpHandler.addRequest(req);

        if (spam_votes.contains(ideaId))
            spam_votes.remove((Integer) ideaId);
    }

    public static void PostOnItVote(int ideaId)
    {
        Request req = new Request("add_on_it_vote", Request.App.MyPitch);
        req.put("idea_id", String.valueOf(ideaId));
        req.put("email", Login.GetUserEmail());
        HttpHandler.addRequest(req);

        if(!on_it_votes.contains(ideaId))
            on_it_votes.add(ideaId);
    }

    public static void PostOnItVoteCanceled(int ideaId)
    {
        Request req = new Request("remove_on_it_vote", Request.App.MyPitch);
        req.put("idea_id", String.valueOf(ideaId));
        req.put("email", Login.GetUserEmail());
        HttpHandler.addRequest(req);

        if(on_it_votes.contains(ideaId))
            on_it_votes.remove((Integer) ideaId);
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

    public static void RefreshUpVotes(Integer[] data)
    {
        up_votes = new ArrayList<Integer>(Arrays.asList(data));
    }

    public static void RefreshOnItVotes(Integer[] data)
    {
        on_it_votes=new ArrayList<Integer>(Arrays.asList(data));
    }

    public static void RefreshSpamVotes(Integer[] data)
    {
        spam_votes=new ArrayList<Integer>(Arrays.asList(data));
    }

}