package com.snepos.pitchit;

/**
 * Created by user1 on 12/06/2015.
 */
public class Card {
    int id;
    int clicks;
    boolean isLiked;
    boolean isOnIt;
    boolean isReport;
    String head;
    String body;
    int upVotes;
    int onItVotes;


    public Card(int id, String _head, String _body, boolean _isLiked, int _clicks, boolean _isReport, boolean _isOnIt, int _upVotes, int _onItVotes) {
        this.id = id;
        head = _head;
        body = _body;
        isLiked = _isLiked;
        clicks = _clicks;
        isOnIt = _isOnIt;
        isReport = _isReport;
        upVotes = _upVotes;
        onItVotes = _onItVotes;
    }

    public Card() {

    }

    public void setCard(int id, String _head, String _body, boolean _isLiked, int _clicks, boolean _isReport, boolean _isOnIt, int _upVotes, int _onItVotes) {
        this.id = id;
        head = _head;
        body = _body;
        isLiked = _isLiked;
        clicks = _clicks;
        isOnIt = _isOnIt;
        isReport = _isReport;
        upVotes = _upVotes;
        onItVotes = _onItVotes;
    }

    public void setClicks(int _clicks) {
        clicks = _clicks;
    }

    public void clickedLike() {
        if (isLiked)
            isLiked = false;
        else
            isLiked = true;
    }

    public void clickedOnIt() {
        if (isOnIt)
            isOnIt = false;
        else
            isOnIt = true;

    }

    public void clickedReport() {
        if (isReport)
            isReport = false;
        else
            isReport = true;
    }

    public String getBody() {
        return body;
    }

    public String getHead() {
        return head;
    }

    public boolean getIsLiked() {
        return isLiked;
    }

    public int getClickes() {
        return clicks;
    }

    public boolean getIsOnIt() {
        return isOnIt;
    }

    public boolean getIsReport() {
        return isReport;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public int getOnItVotes() {
        return onItVotes;
    }

    public int getId() {
        return id;
    }
}