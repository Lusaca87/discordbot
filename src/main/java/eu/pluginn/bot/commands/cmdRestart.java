package eu.pluginn.bot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class cmdRestart implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws Exception {
        event.getGuild().getTextChannelById("608191656243757057").sendMessage("!forceBotRestart").queue();
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help(MessageReceivedEvent event) {
        return null;
    }
}
