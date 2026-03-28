package ru.kredwi.kmf.communication;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BarMessage implements TypeMessage {
    @Override
    public void sendMessage(@NotNull Player player, @NotNull String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(message));
    }
}
