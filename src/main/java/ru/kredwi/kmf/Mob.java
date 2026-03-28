package ru.kredwi.kmf;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.EntityType;

@AllArgsConstructor
@Data
public class Mob {

    private final EntityType entityType;
    private final String displayName;
    private final double fine;

}
