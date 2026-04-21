package yt.corazonid.petakUmpet;

import org.bukkit.plugin.java.JavaPlugin;

public final class PetakUmpet extends JavaPlugin {
    private GameManager gameManager;
    private QuizManager quizManager;
    private GameListener gameListener;

    @Override
    public void onEnable() {
        this.gameManager = new GameManager();
        this.quizManager = new QuizManager();

        // Register Commands (dari step sebelumnya)
        getCommand("regis").setExecutor(new AdminCommands(this));
        getCommand("unregis").setExecutor(new AdminCommands(this));
        getCommand("listplayer").setExecutor(new AdminCommands(this));
        getCommand("gacha").setExecutor(new GameCommands(this));
        getCommand("start").setExecutor(new GameCommands(this));
        if (getCommand("resetgame") != null) getCommand("resetgame").setExecutor(new AdminCommands(this));
        if (getCommand("endgame") != null) getCommand("endgame").setExecutor(new AdminCommands(this));
        if (getCommand("listscore") != null) getCommand("listscore").setExecutor(new AdminCommands(this));
        this.gameListener = new GameListener(this);
        getServer().getPluginManager().registerEvents(gameListener, this);

        // REGISTER LISTENERS
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);

        getLogger().info("PetakUmpet Cerdas Cermat Enabled!");
    }

    public GameManager getGameManager() { return gameManager; }
    public QuizManager getQuizManager() { return quizManager; }
    public GameListener getGameListener() { return gameListener; }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
