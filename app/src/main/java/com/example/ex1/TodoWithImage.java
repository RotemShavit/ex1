package com.example.ex1;

import android.widget.ImageView;

public class TodoWithImage
{
    private String todoString;
    private int image;

    public TodoWithImage(String s, int i)
    {
        this.todoString = s;
        this.image = i;
    }

    public void changeText(String s)
    {
        this.todoString = s;
    }

    public void changeImage(int i)
    {
        this.image = i;
    }

    public String getString()
    {
        return this.todoString;
    }

    public int getImage()
    {
        return this.image;
    }
}
