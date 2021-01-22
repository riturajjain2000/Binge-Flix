package com.skf.bingeflix.Models;

public class Slide {

    private  int Image;
    private String Title;

    public Slide(int image, String title) {
        Image = image;
        Title = title;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }
}
