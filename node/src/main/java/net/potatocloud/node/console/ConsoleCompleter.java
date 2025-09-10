package net.potatocloud.node.console;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.potatocloud.node.Node;
import net.potatocloud.node.command.Command;
import net.potatocloud.node.command.CommandManager;
import net.potatocloud.node.command.TabCompleter;
import net.potatocloud.node.screen.Screen;
import net.potatocloud.node.screen.ScreenManager;
import net.potatocloud.node.setup.Setup;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ConsoleCompleter implements Completer {

    private final CommandManager commandManager;

    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        final ScreenManager screenManager = Node.getInstance().getScreenManager();
        final Screen currentScreen = screenManager.getCurrentScreen();

        // add leave and exit options for all screens except node and setup screens
        if (currentScreen != null && !currentScreen.getName().equals(Screen.NODE_SCREEN) && !currentScreen.getName().startsWith("setup_")) {
            candidates.add(new Candidate("leave"));
            candidates.add(new Candidate("exit"));
            return;
        }

        final Setup currentSetup = Node.getInstance().getSetupManager().getCurrentSetup();
        if (currentSetup != null) {
            if (currentSetup.isInSummary()) {
                candidates.add(new Candidate("back"));
                candidates.add(new Candidate("confirm"));
                candidates.add(new Candidate("cancel"));
            } else {
                candidates.add(new Candidate("back"));
                candidates.add(new Candidate("cancel"));

                final List<String> possibleChoices = currentSetup.getQuestions().get(currentSetup.getCurrentIndex()).getPossibleChoices(currentSetup.getAnswers());
                if (possibleChoices != null) {
                    for (String possibleChoice : possibleChoices) {
                        candidates.add(new Candidate(possibleChoice));
                    }
                }
            }
            return;
        }

        final List<String> words = line.words();
        final String currentWord = line.word();

        if (line.wordIndex() == 0) {
            for (String cmd : commandManager.getAllCommandNames()) {
                if (cmd.startsWith(currentWord)) {
                    candidates.add(new Candidate(cmd));
                }
            }
        } else {
            final String commandName = words.get(0);
            final Command command = commandManager.getCommand(commandName);
            if (command == null) {
                return;
            }

            if (command instanceof TabCompleter completer) {

                final String[] args = words.subList(1, words.size()).toArray(new String[0]);

                for (String suggestion : completer.complete(args)) {
                    if (suggestion.startsWith(currentWord)) {
                        candidates.add(new Candidate(suggestion));
                    }
                }
            }
        }
    }
}
