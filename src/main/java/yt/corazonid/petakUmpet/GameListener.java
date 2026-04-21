package yt.corazonid.petakUmpet;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class GameListener implements Listener {
    private final PetakUmpet plugin;

    public GameListener(PetakUmpet plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        GameManager gm = plugin.getGameManager();
        if (!gm.isGameRunning()) return;

        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        // 1. Sistem Skor untuk yang Mati (Hider)
        if (gm.getParticipants().contains(victim) && !victim.equals(gm.getHunter())) {
            int penalty = gm.getNextDeathPenalty();
            gm.addScore(victim.getUniqueId(), penalty);
            Bukkit.broadcastMessage("§c" + victim.getName() + " tereliminasi! Skor: §l" + penalty);
        }

        // 2. Sistem Skor untuk Pembunuh
        if (killer != null) {
            // Jika Hunter yang kill
            if (killer.equals(gm.getHunter())) {
                gm.addScore(killer.getUniqueId(), 1);
                killer.sendMessage("§a+1 Poin karena membunuh mangsa!");
            }
            // Jika hantu (player yang sudah mati) membantu membunuh
            else if (gm.getParticipants().contains(killer)) {
                gm.addScore(killer.getUniqueId(), 1);
                killer.sendMessage("§b+1 Poin karena membantu Hunter!");
            }
        }

        // Cek apakah semua Hider sudah mati (Ronde Selesai)
        checkRoundEnd();
    }

    private void checkRoundEnd() {
        GameManager gm = plugin.getGameManager();
        long hidersAlive = gm.getParticipants().stream()
                .filter(p -> !p.equals(gm.getHunter()) && !p.isDead())
                .count();

        if (hidersAlive == 0) {
            gm.setGameRunning(false);
            Bukkit.broadcastMessage("§6§lRONDE SELESAI! §fSemua player telah tertangkap.");
        }
    }
}
