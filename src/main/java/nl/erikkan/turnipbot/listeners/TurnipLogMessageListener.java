package nl.erikkan.turnipbot.listeners;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import nl.erikkan.turnipbot.model.TurnipLog;
import nl.erikkan.turnipbot.repository.TurnipLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TurnipLogMessageListener extends ListenerAdapter {

    @Autowired
    private TurnipLogRepository turnipLogRepository;


    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        Message msg = event.getMessage();
        MessageChannel channel = event.getChannel();
        String content = msg.getContentRaw();

        if (content.startsWith("!record")) {
            channel.sendMessage(this.recordValue(msg)).queue();
        } else if (content.startsWith("!high")) {
            channel.sendMessage(this.getHigh(msg)).queue();
        } else if (content.startsWith("!low")) {
            channel.sendMessage(this.getLow(msg)).queue();
        }
    }

    private String getHigh(Message msg) {
        Optional<TurnipLog> log = turnipLogRepository.findFirstByOrderByTurnipValueDesc();
        if (log.isPresent()) {
            TurnipLog turnipLog = log.get();
            User user = msg.getJDA().getUserById(turnipLog.getUserId());
            if (user != null) {
                return String.format("Sell your turnips at %s's place for %d bells!", user.getAsMention(), turnipLog.getTurnipValue());
            }
            throw new IllegalStateException("Recorded user could not be found.");
        } else {
            return "No turnip prices have been recorded yet!";
        }
    }

    private String getLow(Message msg) {
        Optional<TurnipLog> log = turnipLogRepository.findFirstByOrderByTurnipValueAsc();
        if (log.isPresent()) {
            TurnipLog turnipLog = log.get();
            User user = msg.getJDA().getUserById(turnipLog.getUserId());
            if (user != null) {
                return String.format("Buy your turnips at %s's place for %d bells!", user.getAsMention(), turnipLog.getTurnipValue());
            }
            throw new IllegalStateException("Recorded user could not be found.");
        } else {
            return "No turnip prices have been recorded yet!";
        }
    }

    private String recordValue(Message msg) {
        TurnipLog log = new TurnipLog();
        log.setGuildId(msg.getGuild().getId());
        log.setUserId(msg.getAuthor().getId());

        int turnipValue = extractValue(msg.getContentRaw());
        log.setTurnipValue(turnipValue);
        turnipLogRepository.save(log);

        return String.format("Set your turnip value to %s for today.", turnipValue);
    }

    private int extractValue(String command) {
        Pattern pattern = Pattern.compile("^!record (\\d+)(?:$| bells$)");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            throw new IllegalArgumentException("Command could not be parsed");
        }
    }
}
