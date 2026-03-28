package ru.kredwi.kmf.communication;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface TypeMessage {
    void sendMessage(@NotNull Player player, @NotNull String message);
}
