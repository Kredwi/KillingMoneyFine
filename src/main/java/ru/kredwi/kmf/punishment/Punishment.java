package ru.kredwi.kmf.punishment;

import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

public interface Punishment extends BiConsumer<Configuration, Player> {

}
