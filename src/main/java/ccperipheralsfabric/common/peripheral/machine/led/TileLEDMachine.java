package ccperipheralsfabric.common.peripheral.machine.led;

import ccperipheralsfabric.CCPeripheralsFabric;
import ccperipheralsfabric.common.peripheral.machine.fan.TileFanMachine;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.shared.common.TileGeneric;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TileLEDMachine extends TileGeneric implements IPeripheralTile {
    private final LEDMachinePeripheral peripheral;

    public TileLEDMachine() {
        super(CCPeripheralsFabric.TILE_LED_MACHINE);
        this.peripheral = new Peripheral(this);
    }

    @Override
    public IPeripheral getPeripheral(Direction direction) {
        return this.peripheral;
    }

    private static final class Peripheral extends LEDMachinePeripheral {
        private final TileLEDMachine machine;
        private Peripheral(TileLEDMachine machine) { this.machine = machine; }

        public World getWorld() {
            return this.machine.getWorld();
        }

        public Vec3d getPosition() {
            BlockPos pos = this.machine.getPos();
            return new Vec3d(pos.getX(), pos.getY(), pos.getZ());
        }

        public boolean equals(@Nullable IPeripheral other) {
            return this == other || (other instanceof Peripheral && this.machine == ((Peripheral) other).machine);
        }
    }
}
