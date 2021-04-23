package ccperipheralsfabric.common.peripheral.machine.fan;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public abstract class FanMachinePeripheral implements IPeripheral {
    @NotNull
    @Override
    public String getType() {
        return "fan_machine";
    }

    public abstract World getWorld();

    public abstract Vec3d getPosition();
    

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
        World world = this.getWorld();
        if (world.isClient) return false;
        Vec3d pos = this.getPosition();
        BlockState state = world.getBlockState(new BlockPos(pos));
        world.setBlockState(new BlockPos(pos), state.with(BlockFanMachine.ENABLED, !state.get(BlockFanMachine.ENABLED)));
        return world.getBlockState(new BlockPos(pos)).get(BlockFanMachine.ENABLED);
    }


    /**
     * Get the current state of the fan.
     */
    @LuaFunction
    public final boolean state() throws LuaException {
        return this.stateMethod();
    }



    private synchronized boolean stateMethod() throws LuaException {
        World world = this.getWorld();
        if (world.isClient) return false;
        Vec3d pos = this.getPosition();
        BlockState state = world.getBlockState(new BlockPos(pos));

        return state.get(BlockFanMachine.ENABLED);
    }

}
