package yt.corazonid.petakUmpet;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
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

        // Tampilkan Timer di Action Bar setiap detik
        String timeFormatted = String.format("%02d:%02d", totalSeconds / 60, totalSeconds % 60);
        Bukkit.getOnlinePlayers().forEach(p ->
                p.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                        new net.md_5.bungee.api.chat.TextComponent("§6§lWAKTU BERMAIN: §e" + timeFormatted)));

        // Pemicu Kuis (Ditambah case menit 0 / totalSeconds 300)
        if (totalSeconds % 60 == 0 || totalSeconds == 300) {
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

         new BukkitRunnable() {
            int timeLeft = 10;
            @Override
            public void run() {
                if (timeLeft > 0 && gm.isGameRunning()) {
                    for (Player p : gm.getParticipants()) {
                        // FIX 4: Add online check before sending title
                        if (p.isOnline() && qm.hasActiveQuestion(p)) {
                            p.sendTitle("§c§l" + timeLeft, "§fJawab cepat di chat!", 0, 21, 0);
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
        p.sendMessage("§c§lSALAH/LAMA! §fHukuman Ronde Menit ke-" + menit);
        p.playSound(p.getLocation(), org.bukkit.Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 1f, 0.5f);

        switch (menit) {
            case 0 -> p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.GLOWING, 20, 0)); // 1s
            case 1 -> p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.GLOWING, 60, 0)); // 3s
            case 2 -> {
                p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.GLOWING, 100, 0)); // 5s
                p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SLOWNESS, 100, 0)); // 5s
            }
            case 3 -> {
                p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.GLOWING, 140, 0)); // 7s
                p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SLOWNESS, 140, 1)); // 7s
                p.playSound(p.getLocation(), org.bukkit.Sound.ENTITY_ENDERMAN_SCREAM, 1f, 1f);
            }
            case 4 -> {
                p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.GLOWING, 200, 0)); // 10s
                p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SLOWNESS, 200, 2)); // 10s
                p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.BLINDNESS, 200, 0)); // 10s
                Bukkit.broadcastMessage("§c§l[ADA ORANG DISINI!] §f" + p.getName() + " di: §e" + p.getLocation().getBlockX() + ", " + p.getLocation().getBlockY() + ", " + p.getLocation().getBlockZ());
            }
        }
    }
}
