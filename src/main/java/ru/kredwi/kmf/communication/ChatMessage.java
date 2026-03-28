package ru.kredwi.kmf.communication;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChatMessage implements TypeMessage {

    @Override
    public void sendMessage(@NotNull Player player, @NotNull String message) {
        player.sendMessage(message);
    }
}
