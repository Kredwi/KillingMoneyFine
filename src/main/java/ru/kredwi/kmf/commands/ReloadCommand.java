package ru.kredwi.kmf.commands;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.kredwi.kmf.config.ConfigSection;
import ru.kredwi.kmf.events.KillingEventDependencies;
import ru.kredwi.kmf.manager.RewardKillManager;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ReloadCommand implements CommandExecutor, TabCompleter {

    private static final String PERM_RELOAD_COMMAND = "kmf.reload";

    private static final String MSG_COMMANDS_NO_PERM = "messages.commands.no-permissions";
    private static final String MSG_COMMANDS_NO_ARGS = "messages.commands.no-args";
    private static final String MSG_COMMANDS_UNKNOWN_COMMAND = "messages.commands.unknown-command";
    private static final String MSG_RELOAD_SUCCESS = "messages.commands.plugin-success-reload";

    private static final List<String> commands = Lists.newArrayList("reload");

    private final ConfigSection configSection;
    private final RewardKillManager killManager;
    private final KillingEventDependencies ked;
    private final Plugin plugin;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 0) {
            sender.sendMessage(getMessage(MSG_COMMANDS_NO_ARGS));
            return true;
        }

        if (!args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(getMessage(MSG_COMMANDS_UNKNOWN_COMMAND));
            return true;
        }

        if (!sender.hasPermission(PERM_RELOAD_COMMAND)) {
            sender.sendMessage(getMessage(MSG_COMMANDS_NO_PERM));
            return true;
        }

        if (plugin.getConfig().getBoolean("debug"))
            plugin.getLogger().info(String.format("[DEBUG] Player with name %s reload plugin",
                    sender.getName()));

        plugin.reloadConfig();
        killManager.clear();
        val config = plugin.getConfig();
        configSection.loadSection(config);
        ked.setConfig(config);

        sender.sendMessage(getMessage(MSG_RELOAD_SUCCESS));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return sender.hasPermission(PERM_RELOAD_COMMAND) && args.length == 1
                ? commands
                : Collections.emptyList();
    }

    private @NotNull String getMessage(@NotNull String key) {
        return Optional.ofNullable(plugin.getConfig().getString(key))
                .orElseGet(() -> {
                    plugin.getLogger().warning(String.format("Message with key %s is empty",
                            key));
                    return key;
                });
    }
}
