package nl.erikkan.turnipbot.listeners;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class TurnipLogMessageListenerTest {

    @Test
    void parseTurnipValue() {
        String command = "!record 100 bells";
        Pattern pattern = Pattern.compile("^!record (\\d+)(?:$| bells$)");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            assertEquals(100, Integer.parseInt(matcher.group(1)));
        } else {
            fail("Matcher should find.");
        }
    }

}