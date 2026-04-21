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

        if (qm.hasActiveQuestion(player)) {
            // Sembunyikan chat agar tidak dicontek
            event.setCancelled(true);

            String answer = event.getMessage().trim();
            String question = qm.getActiveQuestion(player);

            if (qm.checkAnswer(question, answer)) {
                qm.removeActiveQuestion(player);
                player.sendMessage("§a§lBENAR! §fKamu selamat.");
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            } else {
                player.sendMessage("§c§lSALAH! §fCoba lagi cepat!");
            }
        }
    }
}
