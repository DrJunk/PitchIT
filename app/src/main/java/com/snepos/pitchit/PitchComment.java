
package com.snepos.pitchit;

/**
 * Created by user1 on 22/08/2015.
 */
public class PitchComment {
    int id;
    String head;
    String body;

    public PitchComment (int _id, String _head, String _body)
    {
        id=_id;
        head=_head;
        body=_body;
    }
    public int getId(){ return id;}

    public String getBody() { return body; }

    public String getHead() { return head; }
}
