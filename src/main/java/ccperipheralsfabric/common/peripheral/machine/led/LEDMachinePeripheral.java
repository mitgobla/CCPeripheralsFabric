package ccperipheralsfabric.common.peripheral.machine.led;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public abstract class LEDMachinePeripheral implements IPeripheral {
    @NotNull
    @Override
    public String getType() {
        return "led_machine";
    }

    public abstract World getWorld();

    public abstract Vec3d getPosition();

    @LuaFunction
    public final void setColor(int color) throws LuaException {
        if (color > 15 || color < 0) return;
        setColorMethod(color);
    }

    private synchronized void setColorMethod(int color) throws LuaException {
        World world = this.getWorld();
        if (world.isClient) return;
        Vec3d pos = this.getPosition();
        BlockState state = world.getBlockState(new BlockPos(pos));
        world.setBlockState(new BlockPos(pos), state.with(BlockLEDMachine.COLOUR, color));
    }
}
