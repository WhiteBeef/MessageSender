package ru.whitebeef.messagesender.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.AutoCompleteQuery;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.whitebeef.beefcore.entities.Permission;
import ru.whitebeef.beefdiscordinjector.commands.AbstractDiscordSlashCommand;
import ru.whitebeef.beefdiscordinjector.repliers.DefaultRepliers;
import ru.whitebeef.messagesender.MessageSender;
import ru.whitebeef.messagesender.MessageType;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class SendMessageCommand extends AbstractDiscordSlashCommand {

    private MessageType messageType;

    public SendMessageCommand(@NotNull CommandData commandData, @Nullable Permission permission, @Nullable Consumer<SlashCommandInteractionEvent> onCommand, Map<String, Function<AutoCompleteQuery, List<Command.Choice>>> onTabComplete) {
        super(commandData, permission, onCommand, onTabComplete);

        setMessageType(MessageSender.getInstance().getPluginApplicationContext().getBean(MessageType.class));
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String messageNamespace = Objects.requireNonNull(event.getInteraction().getOption("сообщение")).getAsString();
        event.getChannel().sendMessage(messageType.getMessage(messageNamespace)).queue();
        DefaultRepliers.SUCCESS.reply(event);
    }


}

