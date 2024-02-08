package ru.whitebeef.messagesender;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.hjson.JsonValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.whitebeef.meridianbot.command.discord.AbstractDiscordSlashCommand;
import ru.whitebeef.meridianbot.registry.DiscordSlashCommandRegistry;
import ru.whitebeef.meridianbot.utils.GsonUtils;
import ru.whitebeef.meridianbot.utils.JsonUtils;
import ru.whitebeef.messagesender.commands.SendMessageCommand;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Log4j2
@Component
public class MessageType {

    private final Map<String, MessageCreateData> registeredMessages = new HashMap<>();
    private final MessageSender messageSender;
    private final DiscordSlashCommandRegistry discordSlashCommandRegistry;

    @Autowired
    public MessageType(MessageSender messageSender, DiscordSlashCommandRegistry discordSlashCommandRegistry) {
        this.messageSender = messageSender;
        this.discordSlashCommandRegistry = discordSlashCommandRegistry;
    }

    @PostConstruct
    public void postConstruct() {
        loadHJsonFiles();
    }

    public void loadHJsonFiles() {
        registeredMessages.clear();
        File messagesFolder = new File(messageSender.getDataFolder().getPath() + "/messages/");
        if (!messagesFolder.exists()) {
            messagesFolder.mkdir();
        }
        try {
            File[] files = getFilesArrayFromFolder(messagesFolder.getAbsolutePath());
            Arrays.stream(files).filter(file -> file.getName().endsWith(".hjson") || file.getName().endsWith(".json")).forEach(this::loadMessageFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Зарегистрировано " + registeredMessages.size() + " сообщений!");
        registerDiscordCommand();
    }

    @Nullable
    public MessageCreateData getMessage(String namespace) {
        return registeredMessages.get(namespace);
    }

    private void loadMessageFile(File file) {
        try {
            String namespace = file.getName();
            JsonElement jsonElement;
            if (file.getName().endsWith(".hjson")) {
                jsonElement = new JsonParser().parse(JsonValue.readHjson(new FileReader(file)).toString());
                namespace = namespace.substring(0, namespace.length() - 6);
            } else {
                namespace = namespace.substring(0, namespace.length() - 5);
                jsonElement = GsonUtils.getJsonObject(file);
            }
            if (jsonElement.getAsJsonObject().has("content")) {
                registeredMessages.put(namespace, MessageCreateData.fromContent(jsonElement.getAsJsonObject().get("content").getAsString()));
            } else {
                registeredMessages.put(namespace, MessageCreateData.fromEmbeds(JsonUtils.jsonToEmbed(jsonElement.getAsJsonObject())));
            }
        } catch (IOException e) {
            log.error("Ошибка при загрузке файла " + file.getName() + "! Пропускаем его!");
        }
    }

    private File[] getFilesArrayFromFolder(String folder) throws IOException {
        try (Stream<Path> stream = Files.list(Paths.get(folder))) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::toFile)
                    .toArray(File[]::new);
        }
    }

    private void registerDiscordCommand() {
        AbstractDiscordSlashCommand.builder(
                        new CommandDataImpl("sendmessage", "Отправляет зарегистрированные сообщения")
                                .addOption(OptionType.STRING, "сообщение", "Название сообщения", true, true), SendMessageCommand.class)
                .addAutoComplete("сообщение", (autoCompleteQuery) ->
                        registeredMessages.keySet().stream()
                                .map(s -> new Command.Choice(s, s))
                                .toList())
                .build().register(discordSlashCommandRegistry);
    }
}
