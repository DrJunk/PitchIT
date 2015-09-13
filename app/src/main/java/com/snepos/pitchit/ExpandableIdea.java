package com.snepos.pitchit;

import org.json.JSONObject;

/**
 * Created by Omer on 31/08/2015.
 */
public class ExpandableIdea
{
    private String title;

    public ExpandableIdea(String title)
    {
        this.title = title;
    }

    public ExpandableIdea(JSONObject jsonObject)
    {
        try
        {
            this.title = jsonObject.getString("title");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public String getTitle()
    {
        return title;
    }
}
