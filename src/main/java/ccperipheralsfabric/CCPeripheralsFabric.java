package ccperipheralsfabric;

import ccperipheralsfabric.common.item.SecurityCardItem;
import ccperipheralsfabric.common.peripheral.machine.chatbox.BlockChatboxMachine;
import ccperipheralsfabric.common.peripheral.machine.chatbox.TileChatboxMachine;
import ccperipheralsfabric.common.peripheral.machine.fan.BlockFanMachine;
import ccperipheralsfabric.common.peripheral.machine.fan.TileFanMachine;
import ccperipheralsfabric.common.peripheral.machine.led.BlockLEDMachine;
import ccperipheralsfabric.common.peripheral.machine.led.TileLEDMachine;
import ccperipheralsfabric.common.peripheral.machine.vacuum.BlockVacuumMachine;
import ccperipheralsfabric.common.peripheral.machine.vacuum.TileVacuumMachine;
import ccperipheralsfabric.common.peripheral.sensor.crop.BlockCropSensor;
import ccperipheralsfabric.common.peripheral.sensor.crop.TileCropSensor;
import ccperipheralsfabric.common.peripheral.sensor.entity.BlockEntitySensor;
import ccperipheralsfabric.common.peripheral.sensor.entity.TileEntitySensor;
import ccperipheralsfabric.common.peripheral.sensor.environment.BlockEnvironmentSensor;
import ccperipheralsfabric.common.peripheral.sensor.environment.TileEnvironmentSensor;
import ccperipheralsfabric.common.peripheral.sensor.item.BlockItemSensor;
import ccperipheralsfabric.common.peripheral.sensor.item.TileItemSensor;
import ccperipheralsfabric.common.peripheral.sensor.player.BlockPlayerSensor;
import ccperipheralsfabric.common.peripheral.sensor.player.TilePlayerSensor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.ToIntFunction;

public class CCPeripheralsFabric implements ModInitializer {
	public static Logger LOGGER = LogManager.getLogger();

	public static final String MOD_ID = "CCPeripheralsFabric";
	public static final String MOD_NAME = "CCPeripheralsFabric";

	// Blocks
	// Environment Sensor
	public static final BlockEnvironmentSensor ENVIRONMENT_SENSOR = new BlockEnvironmentSensor(FabricBlockSettings.of(Material.METAL).strength(4.0f));
	public static BlockEntityType<TileEnvironmentSensor> TILE_ENVIRONMENT_SENSOR;
	// Player Sensor
	public static final BlockPlayerSensor PLAYER_SENSOR = new BlockPlayerSensor(FabricBlockSettings.of(Material.METAL).strength(4.0f));
	public static BlockEntityType<TilePlayerSensor> TILE_PLAYER_SENSOR;
	// Item Sensor
	public static final BlockItemSensor ITEM_SENSOR = new BlockItemSensor(FabricBlockSettings.of(Material.METAL).strength(4.0f));
	public static BlockEntityType<TileItemSensor> TILE_ITEM_SENSOR;
	// Crop Sensor
	public static final BlockCropSensor CROP_SENSOR = new BlockCropSensor(FabricBlockSettings.of(Material.METAL).strength(4.0f));
	public static BlockEntityType<TileCropSensor> TILE_CROP_SENSOR;
	// Entity Sensor
	public static final BlockEntitySensor ENTITY_SENSOR = new BlockEntitySensor(FabricBlockSettings.of(Material.METAL).strength(4.0f));
	public static BlockEntityType<TileEntitySensor> TILE_ENTITY_SENSOR;
	// Fan Machine
	public static final BlockFanMachine FAN_MACHINE = new BlockFanMachine(FabricBlockSettings.of(Material.METAL).strength(4.0f));
	public static BlockEntityType<TileFanMachine> TILE_FAN_MACHINE;
	// Chatbox Machine
	public static final BlockChatboxMachine CHATBOX_MACHINE = new BlockChatboxMachine(FabricBlockSettings.of(Material.METAL).strength(4.0f));
	public static BlockEntityType<TileChatboxMachine> TILE_CHATBOX_MACHINE;
	// LED Machine
	public static final BlockLEDMachine LED_MACHINE = new BlockLEDMachine((FabricBlockSettings) FabricBlockSettings.of(Material.METAL).strength(4.0f).sound(SoundType.GLASS).lightLevel(value -> 15));
	public static BlockEntityType<TileLEDMachine> TILE_LED_MACHINE;
	// Vacuum Machine
	public static final BlockVacuumMachine VACUUM_MACHINE = new BlockVacuumMachine(FabricBlockSettings.of(Material.METAL).strength(4.0f));
	public static BlockEntityType<TileVacuumMachine> TILE_VACUUM_MACHINE;
	// ItemGroup
	public static final CreativeModeTab ITEM_GROUP = FabricItemGroupBuilder.build(
			new ResourceLocation("ccperipherals", "general"),
			() -> new ItemStack(ENVIRONMENT_SENSOR));

	// Items
	public static final Item ITEM_PERIPHERAL_BOX = new Item(new FabricItemSettings().tab(ITEM_GROUP));
	public static final Item ITEM_FAN_MACHINE_BLADE = new Item(new FabricItemSettings().tab(ITEM_GROUP).stacksTo(16));
	public static final Item ITEM_CIRCUIT = new Item(new FabricItemSettings().tab(ITEM_GROUP));
	public static final Item ITEM_DHT_SENSOR = new Item(new FabricItemSettings().tab(ITEM_GROUP));
	public static final Item ITEM_METER = new Item(new FabricItemSettings().tab(ITEM_GROUP));
	public static final Item ITEM_MOTION_SENSOR = new Item(new FabricItemSettings().tab(ITEM_GROUP));
	public static final Item ITEM_SPEAKER = new Item(new FabricItemSettings().tab(ITEM_GROUP));
	public static final Item ITEM_COUNTER = new Item(new FabricItemSettings().tab(ITEM_GROUP));
	public static final Item ITEM_RGB_LED = new Item(new FabricItemSettings().tab(ITEM_GROUP));
	public static final SecurityCardItem ITEM_SECURITY_CARD = new SecurityCardItem(new FabricItemSettings().tab(ITEM_GROUP));

	@Override
	public void onInitialize() {
		log(Level.INFO, "Initializing");

		// Environment Sensor
		Registry.register(Registry.BLOCK, new ResourceLocation("ccperipherals", "environment_sensor"), ENVIRONMENT_SENSOR);
		TILE_ENVIRONMENT_SENSOR = Registry.register(Registry.BLOCK_ENTITY_TYPE, "ccperipherals:environment_sensor", FabricBlockEntityTypeBuilder.create(TileEnvironmentSensor::new, ENVIRONMENT_SENSOR).build(null));
		Registry.register(Registry.ITEM, new ResourceLocation("ccperipherals", "environment_sensor"), new BlockItem(ENVIRONMENT_SENSOR, new FabricItemSettings().tab(ITEM_GROUP)));
		// Player Sensor
		Registry.register(Registry.BLOCK, new ResourceLocation("ccperipherals", "player_sensor"), PLAYER_SENSOR);
		TILE_PLAYER_SENSOR = Registry.register(Registry.BLOCK_ENTITY_TYPE, "ccperipherals:player_sensor", FabricBlockEntityTypeBuilder.create(TilePlayerSensor::new, PLAYER_SENSOR).build(null));
		Registry.register(Registry.ITEM, new ResourceLocation("ccperipherals", "player_sensor"), new BlockItem(PLAYER_SENSOR, new FabricItemSettings().tab(ITEM_GROUP)));
		// Item Sensor
		Registry.register(Registry.BLOCK, new ResourceLocation("ccperipherals", "item_sensor"), ITEM_SENSOR);
		TILE_ITEM_SENSOR = Registry.register(Registry.BLOCK_ENTITY_TYPE, "ccperipherals:item_sensor", FabricBlockEntityTypeBuilder.create(TileItemSensor::new, ITEM_SENSOR).build(null));
		Registry.register(Registry.ITEM, new ResourceLocation("ccperipherals", "item_sensor"), new BlockItem(ITEM_SENSOR, new FabricItemSettings().tab(ITEM_GROUP)));
		// Crop Sensor
		Registry.register(Registry.BLOCK, new ResourceLocation("ccperipherals", "crop_sensor"), CROP_SENSOR);
		TILE_CROP_SENSOR = Registry.register(Registry.BLOCK_ENTITY_TYPE, "ccperipherals:crop_sensor", FabricBlockEntityTypeBuilder.create(TileCropSensor::new, CROP_SENSOR).build(null));
		Registry.register(Registry.ITEM, new ResourceLocation("ccperipherals", "crop_sensor"), new BlockItem(CROP_SENSOR, new FabricItemSettings().tab(ITEM_GROUP)));
		// Entity Sensor
		Registry.register(Registry.BLOCK, new ResourceLocation("ccperipherals", "entity_sensor"), ENTITY_SENSOR);
		TILE_ENTITY_SENSOR = Registry.register(Registry.BLOCK_ENTITY_TYPE, "ccperipherals:entity_sensor", FabricBlockEntityTypeBuilder.create(TileEntitySensor::new, ENTITY_SENSOR).build(null));
		Registry.register(Registry.ITEM, new ResourceLocation("ccperipherals", "entity_sensor"), new BlockItem(ENTITY_SENSOR, new FabricItemSettings().tab(ITEM_GROUP)));
		// Fan Machine
		Registry.register(Registry.BLOCK, new ResourceLocation("ccperipherals", "fan_machine"), FAN_MACHINE);
		TILE_FAN_MACHINE = Registry.register(Registry.BLOCK_ENTITY_TYPE, "ccperipherals:fan_machine", FabricBlockEntityTypeBuilder.create(TileFanMachine::new, FAN_MACHINE).build(null));
		Registry.register(Registry.ITEM, new ResourceLocation("ccperipherals", "fan_machine"), new BlockItem(FAN_MACHINE, new FabricItemSettings().tab(ITEM_GROUP)));
		// Chatbox Machine
		Registry.register(Registry.BLOCK, new ResourceLocation("ccperipherals", "chatbox_machine"), CHATBOX_MACHINE);
		TILE_CHATBOX_MACHINE = Registry.register(Registry.BLOCK_ENTITY_TYPE, "ccperipherals:chatbox_machine", FabricBlockEntityTypeBuilder.create(TileChatboxMachine::new, CHATBOX_MACHINE).build(null));
		Registry.register(Registry.ITEM, new ResourceLocation("ccperipherals", "chatbox_machine"), new BlockItem(CHATBOX_MACHINE, new FabricItemSettings().tab(ITEM_GROUP)));
		// LED Machine
		Registry.register(Registry.BLOCK, new ResourceLocation("ccperipherals", "led_machine"), LED_MACHINE);
		TILE_LED_MACHINE = Registry.register(Registry.BLOCK_ENTITY_TYPE, "ccperipherals:led_machine", FabricBlockEntityTypeBuilder.create(TileLEDMachine::new, LED_MACHINE).build(null));
		Registry.register(Registry.ITEM, new ResourceLocation("ccperipherals", "led_machine"), new BlockItem(LED_MACHINE, new Item.Properties().tab(ITEM_GROUP)));
		// Vacuum Machine
		Registry.register(Registry.BLOCK, new ResourceLocation("ccperipherals", "vacuum_machine"), VACUUM_MACHINE);
		TILE_VACUUM_MACHINE = Registry.register(Registry.BLOCK_ENTITY_TYPE, "ccperipherals:vacuum_machine", FabricBlockEntityTypeBuilder.create(TileVacuumMachine::new, VACUUM_MACHINE).build(null));
		Registry.register(Registry.ITEM, new ResourceLocation("ccperipherals", "vacuum_machine"), new BlockItem(VACUUM_MACHINE, new FabricItemSettings().tab(ITEM_GROUP)));
		// Peripheral Box & Crafting Components
		Registry.register(Registry.ITEM, new ResourceLocation("ccperipherals", "peripheral_box"), ITEM_PERIPHERAL_BOX);
		Registry.register(Registry.ITEM, new ResourceLocation("ccperipherals", "fan_blade"), ITEM_FAN_MACHINE_BLADE);
		Registry.register(Registry.ITEM, new ResourceLocation("ccperipherals", "circuit"), ITEM_CIRCUIT);
		Registry.register(Registry.ITEM, new ResourceLocation("ccperipherals", "dht_sensor"), ITEM_DHT_SENSOR);
		Registry.register(Registry.ITEM, new ResourceLocation("ccperipherals", "meter"), ITEM_METER);
		Registry.register(Registry.ITEM, new ResourceLocation("ccperipherals", "motion_sensor"), ITEM_MOTION_SENSOR);
		Registry.register(Registry.ITEM, new ResourceLocation("ccperipherals", "speaker"), ITEM_SPEAKER);
		Registry.register(Registry.ITEM, new ResourceLocation("ccperipherals", "counter"), ITEM_COUNTER);
		Registry.register(Registry.ITEM, new ResourceLocation("ccperipherals", "rgb_led"), ITEM_RGB_LED);
		// Cards
		Registry.register(Registry.ITEM, new ResourceLocation("ccperipherals", "security_card"), ITEM_SECURITY_CARD);
		// Finish
		log(Level.INFO, "Finished initializing");
	}

	public static void log(Level level, String message){
		LOGGER.log(level, "["+MOD_NAME+"] " + message);
	}
}
