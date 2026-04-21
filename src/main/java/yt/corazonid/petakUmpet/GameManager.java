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
        return switch (deadCount) {
            case 1 -> -5;
            case 2 -> -4;
            case 3 -> -3;
            case 4 -> -2;
            case 5 -> -1;
            default -> 0;
        };
    }

    public Set<UUID> getPastHunters() { return pastHunters; }
}
