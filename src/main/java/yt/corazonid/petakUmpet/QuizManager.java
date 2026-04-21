package yt.corazonid.petakUmpet;

import org.bukkit.entity.Player;

import java.util.*;

public class QuizManager {
    private final Map<String, String> quizBank = new HashMap<>();
    private final Map<Player, String> activeQuestion = new HashMap<>();
    private final Map<Player, String> playerActiveQuestion = new HashMap<>();

    public QuizManager() {
        quizBank.put("Ibu kota Indonesia?", "Jakarta");
        quizBank.put("15 x 4?", "60");
        quizBank.put("Bahasa pemrograman Minecraft?", "Java");
        quizBank.put("Warna kotak hitam pesawat?", "Oranye");
        quizBank.put("Planet ke-3 dari matahari?", "Bumi");
        quizBank.put("Siapa YouTuber Minecraft paling ganteng?", "Corazon"); // Soal bonus buat lucu-lucuan
    }

    public String getRandomQuestion() {
        List<String> questions = new ArrayList<>(quizBank.keySet());
        return questions.get(new Random().nextInt(questions.size()));
    }

    public void setActiveQuestion(Player p, String q) { playerActiveQuestion.put(p, q); }
    public boolean hasActiveQuestion(Player p) { return playerActiveQuestion.containsKey(p); }
    public String getActiveQuestion(Player p) { return playerActiveQuestion.get(p); }
    public void removeActiveQuestion(Player p) { playerActiveQuestion.remove(p); }

    public boolean checkAnswer(String question, String answer) {
        return quizBank.get(question).equalsIgnoreCase(answer);
    }
}
