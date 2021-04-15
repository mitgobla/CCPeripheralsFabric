package ccperipheralsfabric.common.peripheral.sensor.player;

import ccperipheralsfabric.CCPeripheralsFabric;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.shared.common.TileGeneric;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;


public class TilePlayerSensor extends TileGeneric implements IPeripheralTile {
    private final PlayerSensorPeripheral peripheral;

    public TilePlayerSensor() {
        super(CCPeripheralsFabric.TILE_ENVIRONMENT_SENSOR);
        this.peripheral = new Peripheral(this);
    }

    @Override
    public IPeripheral getPeripheral(Direction direction) {
        return this.peripheral;
    }


    private static final class Peripheral extends PlayerSensorPeripheral {
        private final TilePlayerSensor sensor;
        private Peripheral(TilePlayerSensor sensor) {
            this.sensor = sensor;
        }

        public World getWorld() {
            return this.sensor.getWorld();
        }

        public Vec3d getPosition() {
            BlockPos pos = this.sensor.getPos();
            return new Vec3d(pos.getX(), pos.getY(), pos.getZ());
        }

        public boolean equals(@Nullable IPeripheral other) {
            return this == other || (other instanceof Peripheral && this.sensor == ((Peripheral) other).sensor);
        }

    }
}
