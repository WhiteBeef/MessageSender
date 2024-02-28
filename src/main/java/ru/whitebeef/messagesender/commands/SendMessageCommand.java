package ru.whitebeef.messagesender.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.AutoCompleteQuery;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.whitebeef.meridianbot.command.discord.AbstractDiscordSlashCommand;
import ru.whitebeef.meridianbot.entities.Permission;
import ru.whitebeef.meridianbot.registry.DiscordSlashCommandRegistry;
import ru.whitebeef.meridianbot.repliers.impl.DefaultRepliers;
import ru.whitebeef.messagesender.MessageType;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
public class SendMessageCommand extends AbstractDiscordSlashCommand {

    private MessageType messageType;

    public SendMessageCommand(@NotNull CommandData commandData, @Nullable Permission permission, @Nullable Consumer<SlashCommandInteractionEvent> onCommand, Map<String, Function<AutoCompleteQuery, List<Command.Choice>>> onTabComplete) {
        super(commandData, permission, onCommand, onTabComplete);
    }

    @Autowired
    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String messageNamespace = Objects.requireNonNull(event.getInteraction().getOption("сообщение")).getAsString();

        event.getChannel().sendMessage(messageType.getMessage(messageNamespace)).queue();
        DefaultRepliers.SUCCESS.reply(event);
    }

    @Bean
    private SendMessageCommand registerCommand(DiscordSlashCommandRegistry discordSlashCommandRegistry) {
        return (SendMessageCommand) AbstractDiscordSlashCommand.builder(
                        new CommandDataImpl("sendmessage", "Отправляет зарегистрированные сообщения")
                                .addOption(OptionType.STRING, "сообщение", "Название сообщения", true, true), SendMessageCommand.class)
                .addAutoComplete("сообщение", (autoCompleteQuery) ->
                        messageType.getRegisteredMessages().keySet().stream()
                                .map(s -> new Command.Choice(s, s))
                                .toList())
                .build().register(discordSlashCommandRegistry);
    }
}
