package yt.corazonid.petakUmpet;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    private final PetakUmpet plugin;

    public ChatListener(PetakUmpet plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        QuizManager qm = plugin.getQuizManager();
        GameManager gm = plugin.getGameManager();

        // Hanya cek jika player sedang punya pertanyaan aktif
        if (qm.hasActiveQuestion(player)) {
            String answer = event.getMessage();
            String question = qm.getActiveQuestion(player);

            if (qm.checkAnswer(question, answer)) {
                // Jawaban Benar
                qm.removeActiveQuestion(player);
                player.sendMessage("§a§lBENAR! §fKamu selamat dari hukuman.");
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);

                // Batalkan pesan chat agar tidak bocor ke player lain (opsional)
                event.setCancelled(true);
            } else {
                player.sendMessage("§c§lSALAH! §fCoba lagi sebelum waktu habis!");
            }
        }
    }
}
