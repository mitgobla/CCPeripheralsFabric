package ccperipheralsfabric.common.peripheral.machine.chatbox;

import ccperipheralsfabric.CCPeripheralsFabric;
import org.jetbrains.annotations.Nullable;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.shared.common.TileGeneric;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;


public class TileChatboxMachine extends TileGeneric implements IPeripheralTile {
    private final ChatboxMachinePeripheral peripheral;

    public TileChatboxMachine() {
        super(CCPeripheralsFabric.TILE_CHATBOX_MACHINE);
        this.peripheral = new Peripheral(this);
    }

    @Override
    public IPeripheral getPeripheral(Direction direction) {
        return this.peripheral;
    }


    private static final class Peripheral extends ChatboxMachinePeripheral {
        private final TileChatboxMachine machine;
        private Peripheral(TileChatboxMachine sensor) {
            this.machine = sensor;
        }

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
