package ru.whitebeef.messagesender.commands.registrars;

import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.whitebeef.beefdiscordinjector.commands.AbstractDiscordSlashCommand;
import ru.whitebeef.beefdiscordinjector.registry.DiscordSlashCommandRegistry;
import ru.whitebeef.messagesender.MessageType;
import ru.whitebeef.messagesender.commands.SendMessageCommand;

@Component
public class SendMessageCommandRegistrar {


    @Autowired
    public SendMessageCommandRegistrar(MessageType messageType, DiscordSlashCommandRegistry discordSlashCommandRegistry) {
        AbstractDiscordSlashCommand.builder(
                        new CommandDataImpl("sendmessage", "Отправляет зарегистрированные сообщения")
                                .addOption(OptionType.STRING, "сообщение", "Название сообщения", true, true), SendMessageCommand.class)
                .addAutoComplete("сообщение", (autoCompleteQuery) ->
                        messageType.getRegisteredMessages().keySet().stream()
                                .map(s -> new Command.Choice(s, s))
                                .toList())
                .build().register(discordSlashCommandRegistry);
    }

}
