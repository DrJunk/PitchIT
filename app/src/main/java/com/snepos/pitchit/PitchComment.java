
package com.snepos.pitchit;

/**
 * Created by user1 on 22/08/2015.
 */
public class PitchComment {
    int id;
    String userNickname;
    String text;

    public PitchComment (int _id, String _userNickname, String _text)
    {
        id=_id;
        userNickname = _userNickname;
        text = _text;
    }
    public int getId(){ return id;}

    public String getUserNickname() { return userNickname; }

    public String getText() { return text; }
}
