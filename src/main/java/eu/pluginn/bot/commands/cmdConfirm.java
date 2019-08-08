package eu.pluginn.bot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class cmdConfirm implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws Exception {

        System.out.println("CONFIRM CALLED!");

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help(MessageReceivedEvent event) {
        return null;
    }
}
