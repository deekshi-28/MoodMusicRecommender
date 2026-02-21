# 🎵 Mood Music Recommender System (Java)

## 📌 Project Overview
The **Mood Music Recommender System** is a Java-based desktop application that recommends and plays music based on the user’s mood.  
The system uses a simple dataset (`songs.csv`) and a recommendation engine to filter and suggest songs.  
A Swing-based GUI provides an interactive user interface for mood selection and music playback.

---

## 🚀 Features
- Mood-based music recommendation
- Interactive Java Swing GUI
- CSV-based music dataset
- Play, pause, and stop audio
- Modular and object-oriented design
- Performance monitoring support

---

## 🛠️ Technologies Used
- **Language:** Java  
- **GUI:** Java Swing  
- **Data Storage:** CSV file  
- **Concepts:** OOP, Packages, File Handling  

---

## 📂 Project Structure
MoodMusicRecommender/
│
├── data/
│ └── songs.csv
│
├── src/
│ └── com/example/moodmusic/
│ ├── Main.java
│ ├── MoodGUI.java
│ ├── MusicDatabase.java
│ ├── RecommendationEngine.java
│ ├── AudioPlayer.java
│ ├── Song.java
│ └── PerfMonitor.java
│
├── out/
│ └── com/example/moodmusic/ (compiled .class files)
│
└── README.md


---

## ▶️ How to Run the Project

### ✅ Prerequisites
- Java JDK installed (Java 8 or above)
- VS Code / Command Prompt

---
### to run the entire project
javac -d out src/com/example/moodmusic/*.java

java -cp out com.example.moodmusic.Main


# before running the project need to download song in wav format.
# and also change the path in songs.csv file folder.

### 🔹Run using Terminal (Recommended)

1. Open terminal in the project root directory:
r
2. Compile all Java files:

javac -d out src/com/example/moodmusic/*.java

3. Run the project:
java -cp out com.example.moodmusic.Main


📌 The GUI window will open.

🧠 How It Works:

1. User selects a mood from the GUI
2. Music database filters songs from CSV
3. Recommendation engine suggests suitable songs
4. Audio player handles playback
5. Performance monitor tracks execution metrics

🎯 Applications
1. Mood-based entertainment systems
2. Music recommendation demos
3. Java GUI learning projects
4. College mini / capstone projects

---

🧪 Future Enhancements
1. Real-time emotion detection using AI
2. Online music streaming integration
3. Advanced recommendation algorithms
4. Database-based storage
5. Mobile or web version

👩‍💻 Author
Deekshitha U

📜 License
This project is for educational purposes.

---

### ✅ What you should do now
1. Create a file named **`README.md`**
2. Paste the above content
3. Save
4. Commit & push: ```bash
git add README.md
git commit -m "Added project README"
git push


Testing CodeMate webhook
