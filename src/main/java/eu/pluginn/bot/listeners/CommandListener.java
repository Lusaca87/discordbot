package eu.pluginn.bot.listeners;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import eu.pluginn.bot.core.*;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        boolean isAllowed = false;
        boolean isAllowedforBot = false;
        String discCommand = "";

        if(event.getMessage().getContentRaw().startsWith(Bot.getPrefix()))
        {
            String[] splitBeheaded = event.getMessage().getContentRaw().split(" ");
            discCommand = splitBeheaded[0];
            String Userid = event.getAuthor().getId();

            if(Bot.getAllowedBotCommands().containsKey(discCommand))
            {
                isAllowedforBot = true;
                isAllowed = false;
            }else if(Bot.getAllowedUserCommands().containsKey(discCommand))
            {
                isAllowedforBot = false;
                isAllowed = true;
            }else if(Bot.getAllowedStaffCommands().containsKey(discCommand))
            {

                //StaffLeitung
                for (Member memberList : event.getGuild().getMembersWithRoles(event.getJDA().getRoleById("585570426135248897")))
                {
                    if(memberList.getUser().getId().contains(Userid))
                    {
                        isAllowed = true;
                        isAllowedforBot = false;
                        break;
                    }
                }

                //CommunityLeitung
                for (Member memberList : event.getGuild().getMembersWithRoles(event.getJDA().getRoleById("585570183838564352")))
                {
                    if(memberList.getUser().getId().contains(Userid))
                    {
                        isAllowed = true;
                        isAllowedforBot = false;
                        break;
                    }
                }
            }else if(Bot.getAllowedAdminCommands().containsKey(discCommand))
            {
                //CommunityLeitung
                for (Member memberList : event.getGuild().getMembersWithRoles(event.getJDA().getRoleById("585570183838564352")))
                {
                    if(memberList.getUser().getId().contains(Userid))
                    {
                        isAllowed = true;
                        isAllowedforBot = false;
                        break;
                    }
                }
            }




            splitBeheaded = null;
            discCommand = null;




            //Erlaubt dem BotOwner alle Befehle.
            if(event.getAuthor().getId().equals(Bot.getBotOwnerID()))
            {
                isAllowedforBot = false;
                isAllowed = true;
            }


        }


        if (event.getChannelType().toString().contains("TEXT") && !isAllowed && isAllowedforBot)
        {
            event.getMessage().getChannel().deleteMessageById(event.getMessage().getId()).queue();
            if (event.getMessage().getContentRaw().startsWith(Bot.getPrefix()) && event.getMessage().getAuthor().getId().equals(event.getJDA().getSelfUser().getId()))
            {
                try {
                    commandHandler.handleCommand(commandHandler.parser.parse(event.getMessage().getContentRaw(), event));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
        else if (event.getChannelType().toString().contains("TEXT") && isAllowed && !isAllowedforBot)
        {
            if (event.getMessage().getContentRaw().startsWith(Bot.getPrefix()) && !event.getMessage().getAuthor().getId().equals(event.getJDA().getSelfUser().getId()))
            {
                try {
                    commandHandler.handleCommand(commandHandler.parser.parse(event.getMessage().getContentRaw(), event));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}