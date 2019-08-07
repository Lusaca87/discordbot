package eu.pluginn.bot.listeners;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import eu.pluginn.bot.core.*;
import eu.pluginn.bot.utils.Tools;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        boolean isAllowed = false;
        boolean isAllowedforBot = false;
        String discCommand = "";

        if(event.getMessage().getContentRaw().startsWith(Tools.getPrefix()))
        {
            String[] splitBeheaded = event.getMessage().getContentRaw().split(" ");
            discCommand = splitBeheaded[0];
            String Userid = event.getAuthor().getId();

            if(Tools.getAllowedBotCommands().containsKey(discCommand))
            {
                isAllowedforBot = true;
                isAllowed = false;
            }else if(Tools.getAllowedUserCommands().containsKey(discCommand))
            {
                isAllowedforBot = false;
                isAllowed = true;
            }else if(Tools.getAllowedStaffCommands().containsKey(discCommand))
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
            }else if(Tools.getAllowedAdminCommands().containsKey(discCommand))
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
            if(event.getAuthor().getId().equals(Tools.getBotOwnerID()))
            {
                isAllowedforBot = false;
                isAllowed = true;
            }


        }


        if (event.getChannelType().toString().contains("TEXT") && !isAllowed && isAllowedforBot)
        {
            if (event.getMessage().getContentRaw().startsWith(Tools.getPrefix()) && event.getMessage().getAuthor().getId().equals(event.getJDA().getSelfUser().getId()))
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
            if (event.getMessage().getContentRaw().startsWith(Tools.getPrefix()) && !event.getMessage().getAuthor().getId().equals(event.getJDA().getSelfUser().getId()))
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