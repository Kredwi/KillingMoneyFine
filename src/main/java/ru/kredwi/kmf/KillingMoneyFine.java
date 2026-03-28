package ru.kredwi.kmf;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import ru.kredwi.kmf.commands.ReloadCommand;
import ru.kredwi.kmf.communication.MessageWrapper;
import ru.kredwi.kmf.config.ConfigSection;
import ru.kredwi.kmf.events.KillingEvent;
import ru.kredwi.kmf.events.KillingEventDependencies;
import ru.kredwi.kmf.manager.RewardKillManager;

import java.util.Optional;

public class KillingMoneyFine extends JavaPlugin {

    private static final int METRIC_PLUGIN_ID = 30389;
    private final ConfigSection configSection = new ConfigSection(getLogger(), this, Bukkit.getPluginManager());
    @Getter
    private final RewardKillManager rewardKillManager = new RewardKillManager();
    @Setter
    private MessageWrapper messageWrapper;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        PluginManager pm = getServer().getPluginManager();
        RegisteredServiceProvider<Economy> vault = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (vault == null) {
            super.getLogger().severe("Vault is not found");
            pm.disablePlugin(this);
            return;
        }
        Economy e = vault.getProvider();

        configSection.loadSection(getConfig());

        if (getConfig().getBoolean("debug"))
            getLogger().info("[DEBUG] debug mode enabled");

        // create bstats metric
        new Metrics(this, METRIC_PLUGIN_ID);

        val ked = new KillingEventDependencies(
                super.getLogger(),
                rewardKillManager,
                this.messageWrapper,
                e,
                getConfig()
        );

        pm.registerEvents(new KillingEvent(
                ked
        ), this);

        Optional.ofNullable(getCommand("killingmobfine"))
                .ifPresent((command) -> command.setExecutor(new ReloadCommand(
                        configSection,
                        rewardKillManager,
                        ked,
                        this
                )));
    }

    @Override
    public void onDisable() {
        rewardKillManager.clear();
    }
}
