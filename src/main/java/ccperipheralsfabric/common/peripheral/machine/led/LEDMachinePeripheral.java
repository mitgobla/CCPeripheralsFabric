package ccperipheralsfabric.common.peripheral.machine.led;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public abstract class LEDMachinePeripheral implements IPeripheral {
    @NotNull
    @Override
    public String getType() {
        return "led_machine";
    }

    public abstract Level getWorld();

    public abstract Vec3 getPosition();

    @LuaFunction
    public final void setColor(int color) throws LuaException {
        if (color > 15 || color < 0) return;
        setColorMethod(color);
    }

    private synchronized void setColorMethod(int color) throws LuaException {
        Level world = this.getWorld();
        if (world.isClientSide) return;
        Vec3 pos = this.getPosition();
        BlockState state = world.getBlockState(new BlockPos(pos));
        world.setBlockAndUpdate(new BlockPos(pos), state.setValue(BlockLEDMachine.COLOUR, color));
    }
}
