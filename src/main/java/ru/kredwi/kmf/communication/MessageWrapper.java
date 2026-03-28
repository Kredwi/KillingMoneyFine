package ru.kredwi.kmf.communication;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class MessageWrapper {

    @NotNull
    @Setter
    private SendMethod method;

    public void sendMessage(@NotNull Player player, @NotNull String message) {


        method.sendMessage(player, message);
    }

}
