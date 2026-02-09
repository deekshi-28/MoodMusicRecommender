package com.example.moodmusic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class RecommendationEngine {
    private final MusicDatabase db;
    private final Random random = new Random();

    public RecommendationEngine(MusicDatabase db) {
        this.db = db;
    }

    // Simple playlist generation:
    //  1) exact mood matches prioritized
    //  2) then similar by tags (if available)
    //  3) fill remaining with random songs
    public List<Song> generatePlaylist(String mood, int maxSize) {
        long start = System.nanoTime();

        List<Song> exact = new ArrayList<>(db.findByMood(mood));
        List<Song> playlist = new ArrayList<>();

        // shuffle exact matches and add
        Collections.shuffle(exact, random);
        for (Song s : exact) {
            if (playlist.size() >= maxSize) break;
            playlist.add(s);
        }

        // If we need more, find songs sharing tags with mood songs
        if (playlist.size() < maxSize && !exact.isEmpty()) {
            // collect tags from exact matches
            List<String> moodTags = new ArrayList<>();
            for (Song s : exact) moodTags.addAll(s.getTags());

            List<Song> candidates = new ArrayList<>();
            for (Song s : db.getAllSongs()) {
                if (playlist.contains(s)) continue;
                for (String t : s.getTags()) {
                    if (moodTags.contains(t)) {
                        candidates.add(s);
                        break;
                    }
                }
            }
            // score and sort by number of shared tags
            candidates.sort(Comparator.comparingInt((Song s) -> sharedTagCount(s, moodTags)).reversed());
            for (Song s : candidates) {
                if (playlist.size() >= maxSize) break;
                playlist.add(s);
            }
        }

        // If still not full, add randoms
        List<Song> pool = new ArrayList<>(db.getAllSongs());
        pool.removeAll(playlist);
        Collections.shuffle(pool, random);
        for (Song s : pool) {
            if (playlist.size() >= maxSize) break;
            playlist.add(s);
        }

        long end = System.nanoTime();
        // optionally return time taken in logs; but keep method simple
        System.out.printf("Playlist generated in %.3f ms. Size=%d%n", (end - start) / 1e6, playlist.size());
        return playlist;
    }

    private int sharedTagCount(Song s, List<String> tags) {
        int c = 0;
        for (String t : s.getTags()) if (tags.contains(t)) c++;
        return c;
    }
}
