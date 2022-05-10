package ccperipheralsfabric.common.peripheral.machine.fan;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public abstract class FanMachinePeripheral implements IPeripheral {
    @NotNull
    @Override
    public String getType() {
        return "fan_machine";
    }

    public abstract Level getWorld();

    public abstract Vec3 getPosition();
    

    /**
     * Toggle the fan.
     * Returns the state of the fan.
     */
    @LuaFunction
    public final boolean toggle() throws LuaException {
        // normally check if inputs are valid here
        // but not necessary for this machine
        return this.toggleMethod();
    }

    private synchronized boolean toggleMethod() throws LuaException {
        Level world = this.getWorld();
        if (world.isClientSide) return false;
        Vec3 pos = this.getPosition();
        BlockState state = world.getBlockState(new BlockPos(pos));
        world.setBlockAndUpdate(new BlockPos(pos), state.setValue(BlockFanMachine.ENABLED, !state.getValue(BlockFanMachine.ENABLED)));
        return world.getBlockState(new BlockPos(pos)).getValue(BlockFanMachine.ENABLED);
    }


    /**
     * Get the current state of the fan.
     */
    @LuaFunction
    public final boolean state() throws LuaException {
        return this.stateMethod();
    }



    private synchronized boolean stateMethod() throws LuaException {
        Level world = this.getWorld();
        if (world.isClientSide) return false;
        Vec3 pos = this.getPosition();
        BlockState state = world.getBlockState(new BlockPos(pos));

        return state.getValue(BlockFanMachine.ENABLED);
    }

}
