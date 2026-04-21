package yt.corazonid.petakUmpet;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class GameLoopTask extends BukkitRunnable {
    private final PetakUmpet plugin;
    private int totalSeconds = 300; // 5 Menit

    public GameLoopTask(PetakUmpet plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        GameManager gm = plugin.getGameManager();
        if (!gm.isGameRunning() || totalSeconds <= 0) {
            this.cancel();
            Bukkit.broadcastMessage("§6§lWAKTU HABIS! Game Selesai.");
            return;
        }

        // Pemicu Kuis setiap 60 detik (1 menit)
        if (totalSeconds % 60 == 0 && totalSeconds < 300) {
            triggerQuiz();
        }

        totalSeconds--;
    }

    private void triggerQuiz() {
        QuizManager qm = plugin.getQuizManager();
        GameManager gm = plugin.getGameManager();
        String question = qm.getRandomQuestion();

        Bukkit.broadcastMessage("§b§l[KUIS] §fPertanyaan: §e" + question);

        for (Player p : gm.getParticipants()) {
            if (p.equals(gm.getHunter())) continue;
            qm.setActiveQuestion(p, question);
        }

        // Countdown 10 detik visual
        new BukkitRunnable() {
            int timeLeft = 10;
            @Override
            public void run() {
                if (timeLeft > 0 && gm.isGameRunning()) {
                    for (Player p : gm.getParticipants()) {
                        if (qm.hasActiveQuestion(p)) {
                            p.sendTitle("§c§l" + timeLeft, "§fCepat jawab di chat!", 0, 21, 0);
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1f, 1.5f);
                        }
                    }
                    timeLeft--;
                } else {
                    this.cancel();
                    executePunishments((300 - totalSeconds) / 60);
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void executePunishments(int menit) {
        GameManager gm = plugin.getGameManager();
        QuizManager qm = plugin.getQuizManager();

        for (Player p : gm.getParticipants()) {
            if (p.equals(gm.getHunter())) continue;

            // Jika masih ada di activeQuestion berarti belum jawab benar
            if (qm.hasActiveQuestion(p)) {
                applyEffect(p, menit);
                qm.removeActiveQuestion(p);
            }
        }
    }

    private void applyEffect(Player p, int menit) {
        p.sendMessage("§c§lSALAH/LAMA! §fKamu kena hukuman menit ke-" + menit);
        p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 1f, 0.5f);

        switch (menit) {
            case 1 -> p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20, 0)); // 1s
            case 2 -> {
                p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 60, 0)); // 3s
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 40, 0)); // 2s
            }
            case 3 -> {
                p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100, 0)); // 5s
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 1)); // 5s
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 1f, 1f);
            }
            case 4 -> {
                p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 200, 0)); // 10s
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 200, 2)); // 10s
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 0)); // 10s
                Bukkit.broadcastMessage("§c§l[BOCOR] §f" + p.getName() + " di: §e" + p.getLocation().getBlockX() + ", " + p.getLocation().getBlockZ());
            }
        }
    }
}
