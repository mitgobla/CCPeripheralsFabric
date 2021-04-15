package ccperipheralsfabric.common.peripheral.sensor.player;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class PlayerSensorPeripheral implements IPeripheral {
    @NotNull
    @Override
    public String getType() {
        return "player_sensor";
    }

    public abstract World getWorld();

    public abstract Vec3d getPosition();
}
