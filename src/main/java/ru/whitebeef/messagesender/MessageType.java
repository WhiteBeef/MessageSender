package ru.whitebeef.messagesender;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.hjson.JsonValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.whitebeef.beefcore.utils.GsonUtils;
import ru.whitebeef.messagesender.utils.JsonUtils;

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

@Slf4j
@Component
public class MessageType {

    @Getter
    private final Map<String, MessageCreateData> registeredMessages = new HashMap<>();
    private final MessageSender messageSender;

    @Autowired
    public MessageType(MessageSender messageSender) {
        this.messageSender = messageSender;
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
            loadFromJsonElement(namespace, jsonElement);
        } catch (IOException e) {
            log.error("Ошибка при загрузке файла " + file.getName() + "! Пропускаем его!");
        }
    }

    public void loadFromJsonElement(String namespace, JsonElement jsonElement) {
        if (jsonElement.getAsJsonObject().has("content")) {
            registeredMessages.put(namespace, MessageCreateData.fromContent(jsonElement.getAsJsonObject().get("content").getAsString()));
        } else {
            registeredMessages.put(namespace, MessageCreateData.fromEmbeds(JsonUtils.jsonToEmbed(jsonElement.getAsJsonObject())));
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
}
