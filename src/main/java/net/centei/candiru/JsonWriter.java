package net.centei.candiru;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.*;
import java.math.BigDecimal;

public class JsonWriter {
    public static void save(JsonObject deathmappings) {
        try {
            File file = new File("D:\\Documents\\minecraftfabric\\fabric-example-mod-1.18\\run\\mods\\JsonFileHearts.json");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            Candiru.LOGGER.info("Writing JSON to " + file.getAbsolutePath() + ".");
            assert deathmappings != null;
            Jsoner.serialize(deathmappings, fileWriter);
            fileWriter.close();
            Candiru.LOGGER.info("This was written to file.");
            Candiru.LOGGER.info(Jsoner.serialize(deathmappings));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveHeartsStorage(JsonObject heartmappings) {
        try {
            String basePath = new File("").getAbsolutePath();
            File file = new File(basePath.concat("\\mods\\JsonFileHeartsStorage.json"));
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            Candiru.LOGGER.info("Writing JSON to " + file.getAbsolutePath() + ".");
            assert heartmappings != null;
            Jsoner.serialize(heartmappings, fileWriter);
            fileWriter.close();
            Candiru.LOGGER.info("This was written to file.");
            Candiru.LOGGER.info(Jsoner.serialize(heartmappings));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int heartsInStorage(JsonObject hearts_remaining, ServerPlayerEntity player) {
        Integer hearts_storage_int = 0;
        if (!hearts_remaining.containsKey(player.getUuid())) {
            return -1;
        }
        BigDecimal hearts_storage = (BigDecimal) hearts_remaining.get(player.getUuid().toString());
        hearts_storage_int = hearts_storage.intValue();
        Candiru.LOGGER.info(hearts_storage_int.toString());
        return hearts_storage_int;
    }

    public static JsonObject loadHearts(File fileheartsstorage, String filepathheartsstorage) {
        if (fileheartsstorage.exists() && fileheartsstorage.length() != 0) {
            try {
                Reader reader = new FileReader(filepathheartsstorage);
                JsonObject hearts_json_file = (JsonObject) Jsoner.deserialize(reader);
                reader.close();
                return hearts_json_file;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JsonException j) {
                j.printStackTrace();
            }
        }
        return null;
    }
}
