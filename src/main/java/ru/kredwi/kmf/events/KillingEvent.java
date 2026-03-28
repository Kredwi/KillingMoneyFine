package ru.kredwi.kmf.events;

import lombok.AllArgsConstructor;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;
import ru.kredwi.kmf.Mob;

import java.text.MessageFormat;

@AllArgsConstructor
public class KillingEvent implements Listener {

    private static final String MSG_FINE_SUCCESS = "messages.fine.success";
    private static final String PERM_PLAYER_BYPASS = "kmf.bypass";

    @NotNull
    private final KillingEventDependencies dependencies;

    private Player getPlayer(LivingEntity entity) {

        if (entity.getKiller() != null)
            return entity.getKiller();

        if (entity.getLastDamageCause() != null) {
            if (entity.getLastDamageCause() instanceof EntityDamageByEntityEvent) {

                EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) entity.getLastDamageCause();
                if (e.getDamager() instanceof Player)
                    return (Player) e.getDamager();
                return null;
            }
        }
        return null;
    }

    @EventHandler
    public void onPlayerKill(@NotNull EntityDeathEvent e) {
        Player killer = getPlayer(e.getEntity());
        if (killer == null) {
            debug("Killer (Player) is not found");
            return;
        }

        if (killer.hasPermission(PERM_PLAYER_BYPASS)) {
            debug("Player have bypass permission. Skip.");
            return;
        }

        Mob mob = dependencies.getRewardKillManager().getMob(e.getEntityType());
        if (mob.getFine() == -1D) {
            debug(String.format("fine for entity %s is not found (fine == -1D)",
                    e.getEntityType().name()));
            return;
        }

        EconomyResponse resp = dependencies.getEconomy().withdrawPlayer(killer, mob.getFine());
        if (resp.type == EconomyResponse.ResponseType.FAILURE) {
            // TODO: bad_write_off
            return;
        }

        String message = dependencies.getConfig().getString(MSG_FINE_SUCCESS);
        if (message == null) {
            dependencies.getLogger().severe("Config key with name " + MSG_FINE_SUCCESS + " is not found");
            return;
        }

        dependencies.getMessageWrapper()
                .sendMessage(killer, MessageFormat
                        .format(message, mob.getDisplayName(), mob.getFine()));
    }

    private void debug(String msg) {
        if (dependencies.getConfig().getBoolean("debug")) {
            dependencies.getLogger().info("[DEBUG] " + msg);
        }
    }
}
