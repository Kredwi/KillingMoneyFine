package ru.kredwi.kmf.communication;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public enum SendMethod {
    CHAT(new ChatMessage()),
    ACTIONBAR(new BarMessage());

    private final TypeMessage message;

    SendMethod(TypeMessage message) {
        this.message = message;
    }

    public void sendMessage(@NotNull Player player, @NotNull String message) {
        this.message.sendMessage(player, message);
    }
}
