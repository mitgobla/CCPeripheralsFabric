package ccperipheralsfabric;

import ccperipheralsfabric.common.block.EnvironmentSensor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CCPeripheralsFabric implements ModInitializer {
	public static Logger LOGGER = LogManager.getLogger();

	public static final String MOD_ID = "CCPeripheralsFabric";
	public static final String MOD_NAME = "CCPeripheralsFabric";

	public static final EnvironmentSensor ENVIRONMENT_SENSOR = new EnvironmentSensor(FabricBlockSettings.of(Material.METAL).strength(4.0f));

	@Override
	public void onInitialize() {
		log(Level.INFO, "Initializing");
		Registry.register(Registry.BLOCK, new Identifier("ccperipherals", "environment_sensor"), ENVIRONMENT_SENSOR);
		Registry.register(Registry.ITEM, new Identifier("ccperipherals", "environment_sensor"), new BlockItem(ENVIRONMENT_SENSOR, new FabricItemSettings().group(ItemGroup.MISC)));
		log(Level.INFO, "Finished initializing");
	}

	public static void log(Level level, String message){
		LOGGER.log(level, "["+MOD_NAME+"] " + message);
	}
}
