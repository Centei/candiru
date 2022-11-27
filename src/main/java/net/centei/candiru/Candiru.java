package net.centei.candiru;

import com.github.cliftonlabs.json_simple.JsonObject;
import net.centei.candiru.commands.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.world.GameMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Candiru implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("candiru");
	public static final Map<UUID, Integer> DEATH_MAP = new HashMap<UUID, Integer>();
	public static final Map<UUID, Integer> HEART_MAP = new HashMap<UUID, Integer>();
	private static final JsonWriter jsonWriter = new JsonWriter();

	@Override
	public void onInitialize() {
		String basePath = new File("").getAbsolutePath();
		String filePath = basePath.concat("\\mods\\JsonFileHearts.json");
		String filepathheartsstorage = basePath.concat("\\mods\\JsonFileHeartsStorage.json");
		File fileheartsstorage = new File(filepathheartsstorage);
		File file = new File(filePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				LOGGER.info(String.valueOf(e));
			}
		}
		if (!fileheartsstorage.exists()) {
			try {
				fileheartsstorage.createNewFile();
			} catch (IOException e) {
				LOGGER.info(String.valueOf(e));
			}
		}
		JsonObject hearts_json_file = JsonWriter.loadHearts(fileheartsstorage, filepathheartsstorage);
		JsonObject health_json_file = JsonWriter.loadHearts(file, filePath);
		if (hearts_json_file != null) {
			if (!hearts_json_file.isEmpty()) {
				for (AbstractMap.Entry<String, Object> jsonentry : hearts_json_file.entrySet()) {
					BigDecimal zeus = (BigDecimal) jsonentry.getValue();
					HEART_MAP.put(UUID.fromString(jsonentry.getKey()), zeus.intValue());
				}
			}
		}
		if (health_json_file != null) {
			if (!health_json_file.isEmpty()) {
				for (AbstractMap.Entry<String, Object> jsonentry : health_json_file.entrySet()) {
					BigDecimal zeus = (BigDecimal) jsonentry.getValue();
					DEATH_MAP.put(UUID.fromString(jsonentry.getKey()), zeus.intValue());
				}
			}
		}
		CommandRegistrationCallback.EVENT.register(HeartsCommand::register);
		CommandRegistrationCallback.EVENT.register(HeartsUseCommand::register);
		CommandRegistrationCallback.EVENT.register(HeartsSetCommand::register);
		CommandRegistrationCallback.EVENT.register(HealthSetCommand::register);
		CommandRegistrationCallback.EVENT.register(HeartsHelpCommand::register);
		AdvancementCallback.EVENT.register((player) -> {
			int hearts_storage_int = 1;
			if (HEART_MAP.containsKey(player.getUuid())) {
				hearts_storage_int = HEART_MAP.get(player.getUuid()) + 1;
			}
			HEART_MAP.put(player.getUuid(), hearts_storage_int);
			LOGGER.info(player.getEntityName() + " now has collected " + HEART_MAP.get(player.getUuid()).toString() + " advancements!");
			return ActionResult.PASS;
		});
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			UUID uuid = handler.getPlayer().getUuid();
			if (DEATH_MAP.containsKey(uuid)) {
				if (DEATH_MAP.get(uuid) == 0) {
					GameMode gamemode = GameMode.SPECTATOR;
					handler.getPlayer().changeGameMode(gamemode);
				} else {
					EntityAttributeInstance changehealth = handler.getPlayer().getAttributes().getCustomInstance(EntityAttributes.GENERIC_MAX_HEALTH);
					changehealth.setBaseValue(DEATH_MAP.get(uuid));
				}
			}
		});
		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) ->
		{
			if (oldPlayer.getMaxHealth() - 2 <= 0) {
				DEATH_MAP.put(newPlayer.getUuid(), 0);
				newPlayer.networkHandler.disconnect(Text.literal("You have reached 0 health! Join back to spectate world!"));
			} else {
				EntityAttributeInstance health = (newPlayer.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MAX_HEALTH));
				health.setBaseValue(oldPlayer.getMaxHealth() - 2);
				DEATH_MAP.put(newPlayer.getUuid(), (int) newPlayer.getMaxHealth());
				String s = String.valueOf(newPlayer.getMaxHealth());
				LOGGER.info(newPlayer.getEntityName() + " now has " + s + " health!");
			}
		});
		ServerLifecycleEvents.SERVER_STOPPING.register((server) ->
		{
			if (!DEATH_MAP.isEmpty()) {
				JsonObject jsonObject = new JsonObject();
				for (Map.Entry<UUID, Integer> entry : DEATH_MAP.entrySet()) {
					jsonObject.put(entry.getKey().toString(), entry.getValue());
				}
				jsonWriter.save(jsonObject);

			}
			if (!HEART_MAP.isEmpty()) {
				JsonObject jsonObject = new JsonObject();
				for (Map.Entry<UUID, Integer> entry : HEART_MAP.entrySet()) {
					jsonObject.put(entry.getKey().toString(), entry.getValue());
				}
				jsonWriter.saveHeartsStorage(jsonObject);
			}
		});
	}
}
