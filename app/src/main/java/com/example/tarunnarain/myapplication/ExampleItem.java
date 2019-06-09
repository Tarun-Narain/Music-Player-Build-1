package com.example.tarunnarain.myapplication;

public class ExampleItem {
    private String title;
    private String author;
    private int mImageResource;
    private int starImageResource;
    private String path;

    public ExampleItem(String title, String author, int mImageResource, int starImageResource, String path) {
        this.title = title;
        this.path=path;
        this.author = author;
        this.mImageResource = mImageResource;
        this.starImageResource=starImageResource;
    }

    public String getTitle() {
        return title;
    }
    public String getPath()
    { return path; }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getmImageResource() {
        return mImageResource;
    }

    public int getstarImageResource() {
        return starImageResource;
    }

    public void setmImageResource(int mImageResource) {
        this.mImageResource = mImageResource;
    }
}
