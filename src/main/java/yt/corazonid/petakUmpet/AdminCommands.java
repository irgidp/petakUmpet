package yt.corazonid.petakUmpet;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.Map;
import java.util.UUID;

public class AdminCommands implements CommandExecutor {
    private final PetakUmpet plugin;

    public AdminCommands(PetakUmpet plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) return true;

        GameManager gm = plugin.getGameManager();

        // --- COMMAND REGIS ---
        if (label.equalsIgnoreCase("regis")) {
            if (args.length < 1) {
                sender.sendMessage("§cGunakan: /regis <nama>");
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                gm.regis(target);
                sender.sendMessage("§a" + target.getName() + " berhasil terdaftar!");
            } else {
                sender.sendMessage("§cPlayer tidak online.");
            }
        }

        // --- COMMAND UNREGIS ---
        else if (label.equalsIgnoreCase("unregis")) {
            if (args.length < 1) {
                sender.sendMessage("§cGunakan: /unregis <nama>");
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                gm.unregis(target);
                sender.sendMessage("§e" + target.getName() + " telah dihapus dari daftar.");
            }
        }

        // --- COMMAND LISTPLAYER ---
        else if (label.equalsIgnoreCase("listplayer")) {
            sender.sendMessage("§6§lDAFTAR PESERTA PETAK UMPET:");
            for (Player p : gm.getParticipants()) {
                sender.sendMessage("§7- §f" + p.getName());
            }
        }

        // Tambahkan case baru di dalam onCommand
        else if (label.equalsIgnoreCase("endgame")) {
            if (!sender.isOp()) return true;

            gm.setGameRunning(false);
            Bukkit.broadcastMessage("§c§lGAME TELAH BERAKHIR!");
            showLeaderboard();
        }

        else if (label.equalsIgnoreCase("listscore")) {
            showLeaderboard();
        }

        // Tambahkan di dalam onCommand AdminCommands
        else if (label.equalsIgnoreCase("resetgame")) {
            if (!sender.isOp()) return true;
            gm.resetGameData();
            sender.sendMessage("§a§lRESET! §fSemua skor dan sejarah Hunter telah dihapus.");
        }

        return true;
    }
    // Buat method ini di dalam AdminCommands.java
    private void showLeaderboard() {
        GameManager gm = plugin.getGameManager();
        Bukkit.broadcastMessage("§8==============================");
        Bukkit.broadcastMessage("§6§lLEADERBOARD PETAK UMPET");
        Bukkit.broadcastMessage("§8==============================");

        // Sort skor dari yang tertinggi
        gm.getScores().entrySet().stream()
                .sorted(Map.Entry.<UUID, Integer>comparingByValue().reversed())
                .forEach(entry -> {
                    String name = Bukkit.getOfflinePlayer(entry.getKey()).getName();
                    int score = entry.getValue();
                    Bukkit.broadcastMessage("§e" + name + ": §f" + score + " Poin");
                });

        Bukkit.broadcastMessage("§8==============================");
    }
}
