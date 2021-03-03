package eu.pluginn.bot.commands;

import eu.pluginn.bot.core.Bot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class cmdBotRestart implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws Exception
    {
        event.getTextChannel().sendMessage("Bot wird neu gestartet.").queue();
        Bot.restart(true);
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help(MessageReceivedEvent event) {
        return null;
    }
}
