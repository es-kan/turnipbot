package nl.erikkan.turnipbot.config;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;
import java.util.List;

@Configuration
public class JdaConfig {

    @Value("${discord.token}")
    private String token;

    @Autowired
    private List<EventListener> messageListeners;

    @Bean
    public ShardManager shardManager() throws LoginException {
        return new DefaultShardManagerBuilder()
                .setToken(token)
                .addEventListeners(messageListeners.toArray())
                .setActivity(Activity.of(Activity.ActivityType.WATCHING, "turnip prices"))
                .build();
    }
}
