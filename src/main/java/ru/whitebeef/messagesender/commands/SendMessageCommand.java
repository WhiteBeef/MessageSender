package ru.whitebeef.messagesender.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.AutoCompleteQuery;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.whitebeef.meridianbot.command.discord.AbstractDiscordSlashCommand;
import ru.whitebeef.meridianbot.entities.Permission;
import ru.whitebeef.meridianbot.plugin.PluginRegistry;
import ru.whitebeef.meridianbot.provider.ApplicationContextProvider;
import ru.whitebeef.messagesender.MessageSender;
import ru.whitebeef.messagesender.MessageType;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class SendMessageCommand extends AbstractDiscordSlashCommand {


    public SendMessageCommand(@NotNull CommandData commandData, @Nullable Permission permission, @Nullable Consumer<SlashCommandInteractionEvent> onCommand, Map<String, Function<AutoCompleteQuery, List<Command.Choice>>> onTabComplete) {
        super(commandData, permission, onCommand, onTabComplete);
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        MessageType messageType = ApplicationContextProvider.getContext()
                .getBean(PluginRegistry.class)
                .getPlugin(MessageSender.class)
                .getPluginApplicationContext()
                .getBean(MessageType.class);

        String messageNamespace = Objects.requireNonNull(event.getInteraction().getOption("сообщение")).getAsString();

        event.getChannel().sendMessage(messageType.getMessage(messageNamespace)).queue();
        event.reply(MessageCreateData.fromContent("Успех!")).setEphemeral(true).queue();
    }
}
