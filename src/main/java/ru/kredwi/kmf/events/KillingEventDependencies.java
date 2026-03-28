package ru.kredwi.kmf.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.Configuration;
import ru.kredwi.kmf.communication.MessageWrapper;
import ru.kredwi.kmf.manager.RewardKillManager;

import java.util.logging.Logger;

@Getter
@AllArgsConstructor
public class KillingEventDependencies {

    private final Logger logger;
    private final RewardKillManager rewardKillManager;
    private final MessageWrapper messageWrapper;
    private final Economy economy;
    @Setter
    private Configuration config;

}
