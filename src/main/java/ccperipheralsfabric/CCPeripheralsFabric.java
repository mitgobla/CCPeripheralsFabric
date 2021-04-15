package ccperipheralsfabric;

import ccperipheralsfabric.common.peripheral.sensor.environment.BlockEnvironmentSensor;
import ccperipheralsfabric.common.peripheral.sensor.environment.TileEnvironmentSensor;
import dan200.computercraft.shared.util.FixedPointTileEntityType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.function.Supplier;

public class CCPeripheralsFabric implements ModInitializer {
	public static Logger LOGGER = LogManager.getLogger();

	public static final String MOD_ID = "CCPeripheralsFabric";
	public static final String MOD_NAME = "CCPeripheralsFabric";

	public static final BlockEnvironmentSensor ENVIRONMENT_SENSOR = new BlockEnvironmentSensor(FabricBlockSettings.of(Material.METAL).strength(4.0f));
	public static BlockEntityType<TileEnvironmentSensor> TILE_ENVIRONMENT_SENSOR;

	@Override
	public void onInitialize() {
		log(Level.INFO, "Initializing");
		Registry.register(Registry.BLOCK, new Identifier("ccperipherals", "environment_sensor"), ENVIRONMENT_SENSOR);
		TILE_ENVIRONMENT_SENSOR = Registry.register(Registry.BLOCK_ENTITY_TYPE, "ccperipherals:environment_sensor", BlockEntityType.Builder.create(TileEnvironmentSensor::new, ENVIRONMENT_SENSOR).build(null));
		Registry.register(Registry.ITEM, new Identifier("ccperipherals", "environment_sensor"), new BlockItem(ENVIRONMENT_SENSOR, new FabricItemSettings().group(ItemGroup.MISC)));
		log(Level.INFO, "Finished initializing");
	}

	public static void log(Level level, String message){
		LOGGER.log(level, "["+MOD_NAME+"] " + message);
	}
}
