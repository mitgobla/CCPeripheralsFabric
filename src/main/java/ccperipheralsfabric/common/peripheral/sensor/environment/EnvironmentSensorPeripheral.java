package ccperipheralsfabric.common.peripheral.sensor.environment;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class EnvironmentSensorPeripheral implements IPeripheral {
    @NotNull
    @Override
    public String getType() {
        return "environment_sensor";
    }

    public abstract World getWorld();

    public abstract Vec3d getPosition();
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
        World world = this.getWorld();
        Vec3d pos = this.getPosition();
        BlockState state = world.getBlockState(new BlockPos(pos));
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("time", world.getTime());
        data.put("daytime", world.isDay());
        data.put("dimension", world.getDimension());
        data.put("biome", world.getBiome(new BlockPos(pos).add(state.get(Properties.FACING).getVector())));
        data.put("raining", world.isRaining());
        data.put("light_level", world.getLightLevel(new BlockPos(pos).add(state.get(Properties.FACING).getVector())));
        data.put("thunder", world.isThundering());
        data.put("moon_phase", getMoonPhase(world.getLunarTime()));
        data.put("sky_angle", world.getSkyAngle(1.0F));
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
