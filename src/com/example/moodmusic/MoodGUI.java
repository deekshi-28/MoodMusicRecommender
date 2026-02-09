package com.example.moodmusic;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class MoodGUI extends JFrame {
    private final MusicDatabase db = new MusicDatabase();
    private final RecommendationEngine engine = new RecommendationEngine(db);
    private final AudioPlayer player = new AudioPlayer();

    private JComboBox<String> moodCombo;
    private JButton loadBtn, recommendBtn, playBtn, pauseBtn, stopBtn;
    private JList<Song> playlistList;
    private DefaultListModel<Song> playlistModel;
    private JLabel statusLabel;
    private JTextField csvPathField;

    public MoodGUI() {
        setTitle("Mood-based Music Recommender");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(new EmptyBorder(10,10,10,10));
        setContentPane(root);

        // Top panel: load CSV and moods
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        csvPathField = new JTextField("data/songs.csv", 30);
        loadBtn = new JButton("Load DB");
        loadBtn.addActionListener(e -> loadDatabase());
        top.add(new JLabel("CSV:"));
        top.add(csvPathField);
        top.add(loadBtn);

        root.add(top, BorderLayout.NORTH);

        // Left panel: mood selection & actions
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setPreferredSize(new Dimension(260, 0));

        left.add(new JLabel("Select Mood:"));
        moodCombo = new JComboBox<>(new String[] {"happy","sad","relaxed","energetic","concentrate"});
        moodCombo.setEditable(true);
        left.add(moodCombo);

        recommendBtn = new JButton("Recommend Playlist (5)");
        recommendBtn.addActionListener(e -> recommendPlaylist(5));
        left.add(Box.createVerticalStrut(10));
        left.add(recommendBtn);

        left.add(Box.createVerticalStrut(20));
        left.add(new JLabel("Playback Controls:"));
        playBtn = new JButton("Play Selected");
        pauseBtn = new JButton("Pause");
        stopBtn = new JButton("Stop");

        playBtn.addActionListener(e -> playSelected());
        pauseBtn.addActionListener(e -> player.pause());
        stopBtn.addActionListener(e -> player.stop());

        left.add(playBtn);
        left.add(pauseBtn);
        left.add(stopBtn);

        root.add(left, BorderLayout.WEST);

        // Center: playlist
        playlistModel = new DefaultListModel<>();
        playlistList = new JList<>(playlistModel);
        playlistList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane center = new JScrollPane(playlistList);
        root.add(center, BorderLayout.CENTER);

        // Bottom: status
        JPanel bottom = new JPanel(new BorderLayout());
        statusLabel = new JLabel("Status: Ready");
        bottom.add(statusLabel, BorderLayout.WEST);

        JButton perfBtn = new JButton("Measure Perf");
        perfBtn.addActionListener(e -> measurePerf());
        bottom.add(perfBtn, BorderLayout.EAST);

        root.add(bottom, BorderLayout.SOUTH);
    }

    private void loadDatabase() {
        String path = csvPathField.getText().trim();
        try {
            db.loadFromCSV(path);
            statusLabel.setText("Status: Loaded " + db.getAllSongs().size() + " songs.");
            // populate moodCombo with discovered moods (add if missing)
            for (Song s : db.getAllSongs()) {
                String m = s.getMood();
                boolean found = false;
                for (int i = 0; i < moodCombo.getItemCount(); i++) if (moodCombo.getItemAt(i).equals(m)) found = true;
                if (!found) moodCombo.addItem(m);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load CSV: " + ex.getMessage());
            statusLabel.setText("Status: Load failed");
        }
    }

    private void recommendPlaylist(int size) {
        String mood = ((String)moodCombo.getSelectedItem()).trim();
        long t = PerfMonitor.measureMillis(() -> {
            List<Song> pl = engine.generatePlaylist(mood, size);
            SwingUtilities.invokeLater(() -> {
                playlistModel.clear();
                for (Song s : pl) playlistModel.addElement(s);
            });
        });
        statusLabel.setText(String.format("Status: Recommended %d songs for '%s' in %d ms", size, mood, t));
    }

    private void playSelected() {
        Song s = playlistList.getSelectedValue();
        if (s == null) {
            JOptionPane.showMessageDialog(this, "Please select a song in the playlist.");
            return;
        }
        statusLabel.setText("Status: Playing " + s.getTitle());
        // Play in a background thread to avoid UI freeze
        new Thread(() -> player.play(s.getPath())).start();
    }

    private void measurePerf() {
        long memBefore = PerfMonitor.usedMemoryBytes();
        long t = PerfMonitor.measureMillis(() -> engine.generatePlaylist("happy", 10));
        long memAfter = PerfMonitor.usedMemoryBytes();
        statusLabel.setText(String.format("Perf: %d ms, Memory delta: %d bytes", t, memAfter - memBefore));
        JOptionPane.showMessageDialog(this, "Perf: " + t + " ms\nMemory delta: " + (memAfter - memBefore) + " bytes");
    }
}
