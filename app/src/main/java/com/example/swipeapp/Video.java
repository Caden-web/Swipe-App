package com.example.swipeapp;

/** Immutable metadata describing one online video. */
public final class Video {
    private final String id;
    private final String title;
    private final String description;
    private final String url;

    /** Creates a video entry with a unique ID, text metadata, and HTTPS media URL. */
    public Video(String id, String title, String description, String url) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.url = url;
    }

    /** Returns the unique video identifier. */
    public String getId() { return id; }

    /** Returns the display title. */
    public String getTitle() { return title; }

    /** Returns the short description. */
    public String getDescription() { return description; }

    /** Returns the HTTPS URL of the video file. */
    public String getUrl() { return url; }
}
