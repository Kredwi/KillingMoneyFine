package ru.kredwi.kmf.manager;

import lombok.Setter;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import ru.kredwi.kmf.Mob;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RewardKillManager {
    private final Map<EntityType, Mob> moneys = new HashMap<>();

    @Setter
    private Mob defaultMob = null;

    public Mob getMob(@NotNull EntityType e) {
        return Optional
                .ofNullable(moneys.get(e))
                .orElse(defaultMob);
    }

    public void addMob(@NotNull Mob mob) {
        moneys.put(mob.getEntityType(), mob);
    }

    public void clear() {
        moneys.clear();
    }

}
