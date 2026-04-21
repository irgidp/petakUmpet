package yt.corazonid.petakUmpet;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameCommands implements CommandExecutor {
    private final PetakUmpet plugin;
    private final Random random = new Random();

    public GameCommands(PetakUmpet plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) return true;
        GameManager gm = plugin.getGameManager();

        if (label.equalsIgnoreCase("gacha")) {
            List<Player> available = gm.getParticipants().stream()
                    .filter(p -> !gm.getPastHunters().contains(p.getUniqueId()))
                    .toList();

            if (available.isEmpty()) {
                sender.sendMessage("§cSemua peserta sudah pernah jadi Hunter!");
                return true;
            }

            Player hunter = available.get(random.nextInt(available.size()));
            gm.setHunter(hunter);

            // Efek suara biar dramatis
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 2f);
            }

            Bukkit.broadcastMessage("§8==============================");
            Bukkit.broadcastMessage("§e§lGACHA HUNTER SELESAI!");
            Bukkit.broadcastMessage("§fHunter Ronde Ini: §c§l" + hunter.getName());
            Bukkit.broadcastMessage("§8==============================");
        }

        else if (label.equalsIgnoreCase("start")) {
            if (gm.getHunter() == null) {
                sender.sendMessage("§cGacha hunter dulu sebelum start!");
                return true;
            }
            startHidePhase();
        }

        return true;
    }

    private void startHidePhase() {
        GameManager gm = plugin.getGameManager();
        gm.setGameRunning(true);

        // Timer 1 Menit Ngumpet
        new BukkitRunnable() {
            int count = 60;
            @Override
            public void run() {
                if (count > 0) {
                    if (count <= 10 || count % 10 == 0) {
                        Bukkit.broadcastMessage("§eWaktu ngumpet sisa §f" + count + " detik!");
                    }
                    count--;
                } else {
                    this.cancel();
                    Bukkit.broadcastMessage("§c§lHUNTER DILEPASKAN! GAME DIMULAI!");
                    startMainGameLoop();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void startMainGameLoop() {
        new GameLoopTask(plugin).runTaskTimer(plugin, 0L, 20L); // Cek setiap detik
    }
}
