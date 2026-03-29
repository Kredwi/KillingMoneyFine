package ru.kredwi.kmf.punishment;

import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

public class KickPunishment implements Punishment {

    private final static String MSG_PUNISHMENT_REASON = "fines.punishment.message";

    @Override
    public void accept(Configuration plugin, Player player) {
        if (plugin == null || player == null)
            return;

        String message = plugin.getString(MSG_PUNISHMENT_REASON);

        player.kickPlayer(message == null ? MSG_PUNISHMENT_REASON : message);
    }
}
