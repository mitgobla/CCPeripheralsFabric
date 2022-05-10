package ccperipheralsfabric.common.peripheral.machine.led;

import ccperipheralsfabric.CCPeripheralsFabric;
import ccperipheralsfabric.common.peripheral.machine.fan.TileFanMachine;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.shared.common.TileGeneric;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class TileLEDMachine extends TileGeneric implements IPeripheralTile {
    private final LEDMachinePeripheral peripheral;

    public TileLEDMachine(BlockPos pos, BlockState state) {
        super(CCPeripheralsFabric.TILE_LED_MACHINE, pos, state);
        this.peripheral = new Peripheral(this);
    }

    @Override
    public IPeripheral getPeripheral(Direction direction) {
        return this.peripheral;
    }

    private static final class Peripheral extends LEDMachinePeripheral {
        private final TileLEDMachine machine;
        private Peripheral(TileLEDMachine machine) { this.machine = machine; }

        public Level getWorld() {
            return this.machine.getLevel();
        }

        public Vec3 getPosition() {
            BlockPos pos = this.machine.getBlockPos();
            return new Vec3(pos.getX(), pos.getY(), pos.getZ());
        }

        public boolean equals(@Nullable IPeripheral other) {
            return this == other || (other instanceof Peripheral && this.machine == ((Peripheral) other).machine);
        }
    }
}
