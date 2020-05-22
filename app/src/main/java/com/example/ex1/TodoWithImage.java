package com.example.ex1;

import android.widget.ImageView;

import java.util.Date;

public class TodoWithImage
{
    private String todoString;
    private int image;
    private int is_done; // 1 - means not done, 0 - means done
    private long id;
    private String create;
    private String edit;

    public TodoWithImage(String s, int i, int done, long id, String create, String edit)
    {
        this.todoString = s;
        this.image = i;
        this.is_done = done;
        this.id = id;
        this.create = create;
        this.edit = edit;
    }

    public void changeText(String s)
    {
        this.todoString = s;
    }

    public void changeImage(int i)
    {
        this.image = i;
    }

    public void changeIsDone(int i) { this.is_done = i;}

    public void changeDate(String newEdit)
    {
        this.edit = newEdit;
    }

    public String getString()
    {
        return this.todoString;
    }

    public int getImage()
    {
        return this.image;
    }

    public int getIsDone() { return this.is_done;}

    public long getID(){return this.id;}

    public String getDate(String which)
    {
        if(which.equals("create"))
        {
            return this.create;
        }
        else
        {
            return this.edit;
        }
    }
}
