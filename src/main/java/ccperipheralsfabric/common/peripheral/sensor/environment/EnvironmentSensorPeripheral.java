package ccperipheralsfabric.common.peripheral.sensor.environment;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class EnvironmentSensorPeripheral implements IPeripheral {
    @NotNull
    @Override
    public String getType() {
        return "environment_sensor";
    }

    public abstract Level getWorld();

    public abstract Vec3 getPosition();
    /**
     * Gets data about the world this sensor is in.
     */
    @LuaFunction
    public final Map<String, Object> getData() throws LuaException {
        // normally check if inputs are valid here
        // but not necessary for this sensor
        return this.getDataMethod();
    }

    private synchronized Map<String, Object> getDataMethod() throws LuaException {
        Level world = this.getWorld();
        Vec3 pos = this.getPosition();
        BlockState state = world.getBlockState(new BlockPos(pos));
        Map<String, Object> data = new HashMap<String, Object>();

        if (world.getBiome(new BlockPos(pos)).unwrap().left().isPresent()) data.put("biome", world.getBiome(new BlockPos(pos).offset(state.getValue(BlockStateProperties.FACING).getNormal())).unwrap().left().get().location().getPath());
        data.put("daytime", world.isDay());
        data.put("dimension", world.dimension().location().getPath());
        data.put("light_level", world.getMaxLocalRawBrightness(new BlockPos(pos).offset(state.getValue(BlockStateProperties.FACING).getNormal())));
        data.put("moon_phase", getMoonPhase(world.dayTime()));
        data.put("raining", world.isRaining());
        data.put("sky_angle", world.getTimeOfDay(1.0F));
        data.put("thunder", world.isThundering());
        data.put("time", world.getGameTime());
        return data;
    }

    private static int getMoonPhaseInt(long lunarTime) {
        return (int)(lunarTime / 24000L % 8L + 8L) % 8;
    }

    public static String getMoonPhase(long lunarTime) {
        switch (getMoonPhaseInt(lunarTime)) {
            default:
                return "Full moon";
            case 1:
                return "Waning gibbous";
            case 2:
                return "Third quarter";
            case 3:
                return "Waning crescent";
            case 4:
                return "New moon";
            case 5:
                return "Waxing crescent";
            case 6:
                return "First quarter";
            case 7:
                return "Waxing gibbous";
        }
    }

}
