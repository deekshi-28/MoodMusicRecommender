package com.example.moodmusic;

import java.util.Arrays;
import java.util.List;

public class Song {
    private String id;
    private String title;
    private String artist;
    private String mood;
    private List<String> tags;
    private String path;

    public Song(String id, String title, String artist, String mood, String tags, String path) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.mood = mood.toLowerCase();
        this.tags = tags == null ? List.of() : Arrays.stream(tags.split("\\|"))
                                                     .map(String::trim)
                                                     .filter(s -> !s.isEmpty())
                                                     .map(String::toLowerCase)
                                                     .toList();
        this.path = path;
    }

    // getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getMood() { return mood; }
    public List<String> getTags() { return tags; }
    public String getPath() { return path; }

    @Override
    public String toString() {
        return title + " - " + artist + " [" + mood + "]";
    }
}
