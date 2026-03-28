package ru.kredwi.kmf.config;

import lombok.AllArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.PluginManager;
import ru.kredwi.kmf.KillingMoneyFine;
import ru.kredwi.kmf.Mob;
import ru.kredwi.kmf.communication.MessageWrapper;
import ru.kredwi.kmf.communication.SendMethod;

import java.util.Set;
import java.util.logging.Logger;

@AllArgsConstructor
public class ConfigSection {

    private final Logger logger;
    private final KillingMoneyFine mainPluginClass;
    private final PluginManager pm;

    public void loadSection(FileConfiguration c) {
        ConfigurationSection cs = c.getConfigurationSection("fines");
        if (cs == null) {
            logger.severe("Error of config: Section with name 'fines' is not found");
            pm.disablePlugin(mainPluginClass);
            return;
        }

        ConfigurationSection settings = cs.getConfigurationSection("settings");
        if (settings == null) {
            sectionNotFound("settings");
            return;
        }
        settingsSection(settings);

        ConfigurationSection mobs = cs.getConfigurationSection("mobs");
        if (mobs == null) {
            sectionNotFound("mobs");
            return;
        }
        modsSection(mobs);

    }

    private void sectionNotFound(String name) {
        logger.severe("Section with name '" + name + "' is not found");
        pm.disablePlugin(mainPluginClass);
    }

    private void settingsSection(ConfigurationSection settings) {
        String snowMessageType = settings.getString("show-message-in");
        SendMethod method = SendMethod.CHAT;

        try {
            method = SendMethod.valueOf(snowMessageType);
        } catch (IllegalArgumentException e) {
            logger.warning(snowMessageType + " is not found. Used default " + method.name());
        }

        mainPluginClass.setMessageWrapper(new MessageWrapper(method));

        String name = settings.getString("default-name");
        double fine = settings.getDouble("default-fine", -1D);
        boolean enable = settings.getBoolean("enable-default");
        if (enable)
            mainPluginClass.getRewardKillManager()
                    .setDefaultMob(new Mob(null, name, fine));
    }

    private void modsSection(ConfigurationSection mobs) {
        if (mobs == null) {
            logger.severe("Error of config: Section with name 'mobs' is not found");
            pm.disablePlugin(mainPluginClass);
            return;
        }


        Set<String> keys = mobs.getKeys(false);
        if (keys.isEmpty())
            return;
        registerMobs(keys, mobs);
    }

    private void registerMobs(Set<String> keys, ConfigurationSection mobs) {
        keys.forEach(key -> {
            ConfigurationSection mobSection = mobs.getConfigurationSection(key);
            if (mobSection == null)
                return;
            if (!mobSection.getBoolean("enable"))
                return;

            EntityType et = null;
            try {
                et = EntityType.valueOf(key);
            } catch (IllegalArgumentException er) {
                logger.severe(er.getMessage());
            }
            if (et == null)
                return;
            String displayName = mobSection.getString("name");
            double payment = mobSection.getDouble("fine");

            if (displayName == null) {
                logger.warning(key + " display name is null");
                return;
            }

            if (payment < 0) {
                logger.warning(key + " have negative fine");
                return;
            }

            Mob mob = new Mob(et, displayName, payment);
            mainPluginClass.getRewardKillManager().addMob(mob);
        });
    }

}
