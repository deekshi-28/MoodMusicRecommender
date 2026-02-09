package com.example.moodmusic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicDatabase {
    private final List<Song> songs = new ArrayList<>();

    public void loadFromCSV(String csvPath) throws IOException {
        songs.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String line = br.readLine(); // header
            while ((line = br.readLine()) != null) {
                String[] parts = splitCsvLine(line);
                if (parts.length < 6) continue;
                Song s = new Song(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                songs.add(s);
            }
        }
    }

    // Flexible CSV split (no quotes handling for simplicity)
    private String[] splitCsvLine(String line) {
        return line.split(",", -1);
    }

    public List<Song> getAllSongs() {
        return List.copyOf(songs);
    }

    public List<Song> findByMood(String mood) {
        mood = mood.toLowerCase();
        List<Song> out = new ArrayList<>();
        for (Song s : songs) if (s.getMood().equals(mood)) out.add(s);
        return out;
    }
}
