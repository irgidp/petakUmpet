package yt.corazonid.petakUmpet;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class GameCommands implements CommandExecutor {
    private final PetakUmpet plugin;
    private final Random random = new Random();

    public GameCommands(PetakUmpet plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) return true;
        GameManager gm = plugin.getGameManager();

        if (label.equalsIgnoreCase("gacha")) {
            List<Player> available = gm.getParticipants().stream()
                    .filter(p -> !gm.getPastHunters().contains(p.getUniqueId()))
                    .toList();

            if (available.isEmpty()) {
                sender.sendMessage("§cSemua peserta sudah pernah jadi Hunter! Gunakan /resetgame.");
                return true;
            }

            new BukkitRunnable() {
                int ticks = 0;
                @Override
                public void run() {
                    if (ticks < 20) {
                        Player randomName = gm.getParticipants().get(random.nextInt(gm.getParticipants().size()));

                        // Loop semua player untuk kirim title (Pengganti broadcastTitle)
                        for (Player online : Bukkit.getOnlinePlayers()) {
                            online.sendTitle("§7Memilih Hunter...", "§e" + randomName.getName(), 0, 7, 0);
                            online.playSound(online.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 2f);
                        }
                        ticks++;
                    } else {
                        this.cancel();
                        Player hunter = available.get(random.nextInt(available.size()));
                        gm.setHunter(hunter);

                        // Pengganti broadcastTitle saat pengumuman pemenang
                        for (Player online : Bukkit.getOnlinePlayers()) {
                            online.sendTitle("§c§l" + hunter.getName(), "§fTerpilih menjadi HUNTER!", 10, 40, 10);
                        }
                    }
                }
            }.runTaskTimer(plugin, 0L, 2L);
        }

        else if (label.equalsIgnoreCase("start")) {
            if (gm.getHunter() == null) {
                sender.sendMessage("§cGacha hunter dulu!");
                return true;
            }
            startHidePhase();
        }
        return true;
    }

    private void startHidePhase() {
        GameManager gm = plugin.getGameManager();
        gm.setGameRunning(true);

        new BukkitRunnable() {
            int count = 60;
            @Override
            public void run() {
                if (count > 0) {
                    if (count <= 5) {
                        Bukkit.getOnlinePlayers().forEach(p -> {
                            p.sendTitle("§c" + count, "§fSiapkan diri!", 0, 21, 0);
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1f, 1f);
                        });
                    }
                    Bukkit.getOnlinePlayers().forEach(p -> p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§eWaktu Ngumpet: §f" + count + "s")));
                    count--;
                } else {
                    this.cancel();
                    Bukkit.broadcastMessage("§c§lHUNTER DILEPASKAN!");
                    new GameLoopTask(plugin).runTaskTimer(plugin, 0L, 20L);
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
}
