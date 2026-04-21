package yt.corazonid.petakUmpet;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GameListener implements Listener {
    private final PetakUmpet plugin;
    private final Set<UUID> eliminatedPlayers = new HashSet<>(); // Mencatat hider yang sudah mati

    public GameListener(PetakUmpet plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        GameManager gm = plugin.getGameManager();
        if (!gm.isGameRunning()) return;

        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        // 1. Sistem Skor & Eliminasi Hider
        if (gm.getParticipants().contains(victim) && !victim.equals(gm.getHunter())) {
            if (!eliminatedPlayers.contains(victim.getUniqueId())) {
                eliminatedPlayers.add(victim.getUniqueId());
                int penalty = gm.getNextDeathPenalty();
                gm.addScore(victim.getUniqueId(), penalty);
                Bukkit.broadcastMessage("§c" + victim.getName() + " tereliminasi! Skor: §l" + penalty);

                // Kasih gear "Ghost" ke yang mati biar bisa bantu Hunter
                giveHunterGear(victim);
            }
        }

// 2. Sistem Skor untuk Pembunuh
        if (killer != null) {
            if (gm.getParticipants().contains(killer)) {
                gm.addScore(killer.getUniqueId(), 1);
                killer.sendMessage("§a+1 Poin Kill!");
            }
        }

        checkRoundEnd();
    }

    // CEGAH HIDER MATI BUNUH HUNTER
    @EventHandler
    public void onHunterDamage(EntityDamageByEntityEvent event) {
        GameManager gm = plugin.getGameManager();
        if (!gm.isGameRunning()) return;

        if (event.getEntity() instanceof Player victim && event.getDamager() instanceof Player attacker) {
            // Jika yang dipukul adalah Hunter
            if (victim.equals(gm.getHunter())) {
                // Jika yang memukul adalah hider (baik hidup maupun sudah mati/ghost)
                if (gm.getParticipants().contains(attacker) && !attacker.equals(gm.getHunter())) {
                    event.setCancelled(true);
                    attacker.sendMessage("§cKamu tidak bisa menyakiti Hunter!");
                }
            }
        }
    }

    private void checkRoundEnd() {
        GameManager gm = plugin.getGameManager();
        // Hider yang masih hidup = Total Peserta - 1 (Hunter) - Yang sudah eliminasi
        int totalHiders = gm.getParticipants().size() - 1;
        int currentDead = eliminatedPlayers.size();

        if (currentDead >= totalHiders) {
            gm.setGameRunning(false);
            eliminatedPlayers.clear(); // Reset untuk ronde depan
            Bukkit.broadcastMessage("§6§lRONDE SELESAI! §fSemua hider tertangkap.");
        }
    }

    public void giveHunterGear(Player p) {
        p.getInventory().clear();
        p.getInventory().addItem(new ItemStack(Material.NETHERITE_SWORD));
        p.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 99999, 255)); // One Hit
        p.sendMessage("§eKamu diberikan Kekuatan Maksimal!");
    }
}
