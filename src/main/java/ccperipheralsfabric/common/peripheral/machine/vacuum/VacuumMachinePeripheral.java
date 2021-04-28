package ccperipheralsfabric.common.peripheral.machine.vacuum;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public abstract class VacuumMachinePeripheral implements IPeripheral {
    @NotNull
    @Override
    public String getType() {
        return "vacuum_machine";
    }

    public abstract World getWorld();

    public abstract Vec3d getPosition();
    

    /**
     * Toggle the vacuum.
     * Returns the state of the vacuum.
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
        world.setBlockState(new BlockPos(pos), state.with(BlockVacuumMachine.ENABLED, !state.get(BlockVacuumMachine.ENABLED)));
        return world.getBlockState(new BlockPos(pos)).get(BlockVacuumMachine.ENABLED);
    }


    /**
     * Get the current state of the vacuum.
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

        return state.get(BlockVacuumMachine.ENABLED);
    }

}
