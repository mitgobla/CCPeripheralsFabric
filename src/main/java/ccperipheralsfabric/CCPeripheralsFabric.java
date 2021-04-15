package ccperipheralsfabric;

import ccperipheralsfabric.common.peripheral.machine.fan.BlockFanMachine;
import ccperipheralsfabric.common.peripheral.machine.fan.TileFanMachine;
import ccperipheralsfabric.common.peripheral.sensor.environment.BlockEnvironmentSensor;
import ccperipheralsfabric.common.peripheral.sensor.environment.TileEnvironmentSensor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CCPeripheralsFabric implements ModInitializer {
	public static Logger LOGGER = LogManager.getLogger();

	public static final String MOD_ID = "CCPeripheralsFabric";
	public static final String MOD_NAME = "CCPeripheralsFabric";

	// Blocks
	// Environment Sensor
	public static final BlockEnvironmentSensor ENVIRONMENT_SENSOR = new BlockEnvironmentSensor(FabricBlockSettings.of(Material.METAL).strength(4.0f));
	public static BlockEntityType<TileEnvironmentSensor> TILE_ENVIRONMENT_SENSOR;
	// Fan Machine
	public static final BlockFanMachine FAN_MACHINE = new BlockFanMachine(FabricBlockSettings.of(Material.METAL).strength(4.0f));
	public static BlockEntityType<TileFanMachine> TILE_FAN_MACHINE;

	// ItemGroup
	public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(
			new Identifier("ccperipherals", "general"),
			() -> new ItemStack(ENVIRONMENT_SENSOR));

	// Items
	public static final Item ITEM_FAN_MACHINE_BLADE = new Item(new FabricItemSettings().group(ITEM_GROUP).maxCount(16));

	@Override
	public void onInitialize() {
		log(Level.INFO, "Initializing");
		// Environment Sensor
		Registry.register(Registry.BLOCK, new Identifier("ccperipherals", "environment_sensor"), ENVIRONMENT_SENSOR);
		TILE_ENVIRONMENT_SENSOR = Registry.register(Registry.BLOCK_ENTITY_TYPE, "ccperipherals:environment_sensor", BlockEntityType.Builder.create(TileEnvironmentSensor::new, ENVIRONMENT_SENSOR).build(null));
		Registry.register(Registry.ITEM, new Identifier("ccperipherals", "environment_sensor"), new BlockItem(ENVIRONMENT_SENSOR, new FabricItemSettings().group(ITEM_GROUP)));
		// Fan Machine
		Registry.register(Registry.BLOCK, new Identifier("ccperipherals", "fan_machine"), FAN_MACHINE);
		TILE_FAN_MACHINE = Registry.register(Registry.BLOCK_ENTITY_TYPE, "ccperipherals:fan_machine", BlockEntityType.Builder.create(TileFanMachine::new, FAN_MACHINE).build(null));
		Registry.register(Registry.ITEM, new Identifier("ccperipherals", "fan_machine"), new BlockItem(FAN_MACHINE, new FabricItemSettings().group(ITEM_GROUP)));
		Registry.register(Registry.ITEM, new Identifier("ccperipherals", "fan_blade"), ITEM_FAN_MACHINE_BLADE);
		// Finish
		log(Level.INFO, "Finished initializing");
	}

	public static void log(Level level, String message){
		LOGGER.log(level, "["+MOD_NAME+"] " + message);
	}
}
