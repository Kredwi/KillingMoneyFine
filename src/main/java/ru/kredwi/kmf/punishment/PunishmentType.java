package ru.kredwi.kmf.punishment;

import lombok.Getter;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import ru.kredwi.kmf.KillingMoneyFine;

public enum PunishmentType {
    KICK(new KickPunishment());

    @Getter
    private final Punishment punishment;

    PunishmentType(Punishment punishment) {
        this.punishment = punishment;
    }

    public static void execute(@NotNull String key, @NotNull Configuration p, @NotNull Player pl) {
        try {
            PunishmentType punishmentType = valueOf(key);
            punishmentType.accept(p, pl);
        } catch (IllegalArgumentException e) {
            Plugin plugin = KillingMoneyFine.getPlugin(KillingMoneyFine.class);
            plugin.getLogger().severe(e.getMessage());
        }
    }

    public void accept(Configuration p, Player pl) {
        punishment.accept(p, pl);
    }
}
