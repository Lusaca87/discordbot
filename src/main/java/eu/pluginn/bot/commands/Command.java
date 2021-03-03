package eu.pluginn.bot.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface Command {

    boolean called(String[] args, MessageReceivedEvent event);
    void action(String[] args, MessageReceivedEvent event) throws Exception;
    void executed(boolean success, MessageReceivedEvent event);
    String help(MessageReceivedEvent event);

}