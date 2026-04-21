package yt.corazonid.petakUmpet;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.*;

public class GameManager {
    private final List<Player> participants = new ArrayList<>();
    private final Set<UUID> pastHunters = new HashSet<>();
    private final Map<UUID, Integer> scores = new HashMap<>();
    private Player currentHunter;
    private boolean gameRunning = false;
    private int deadCount = 0;

    public void regis(Player p) {
        if (!participants.contains(p)) {
            participants.add(p);
            scores.putIfAbsent(p.getUniqueId(), 0);
        }
    }

    public void unregis(Player p) {
        participants.remove(p);
    }

    public List<Player> getParticipants() { return participants; }

    public void setHunter(Player p) {
        this.currentHunter = p;
        pastHunters.add(p.getUniqueId());
    }

    public Player getHunter() { return currentHunter; }

    public void addScore(UUID id, int amount) {
        scores.put(id, scores.getOrDefault(id, 0) + amount);
    }

    public Map<UUID, Integer> getScores() { return scores; }

    public void setGameRunning(boolean state) {
        this.gameRunning = state;
        if (state) this.deadCount = 0;
    }

    public boolean isGameRunning() { return gameRunning; }

    public int getNextDeathPenalty() {
        deadCount++;
        // Logic dinamis: Penalty mulai dari -(Jumlah Peserta - 1)
        // Misal peserta 6 (5 hider), maka penalti pertama adalah -5, lalu -4, dst.
        int hiderCount = participants.size() - 1;
        int penalty = -(hiderCount - (deadCount - 1));
        return (penalty < -1) ? penalty : -1;
    }

    public Set<UUID> getPastHunters() { return pastHunters; }

    public void resetGameData() {
        pastHunters.clear();
        scores.clear();
        deadCount = 0;
        currentHunter = null;
        gameRunning = false;
    }
}
