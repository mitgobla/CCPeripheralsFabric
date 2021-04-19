package ccperipheralsfabric.common.peripheral.sensor.crop;

import ccperipheralsfabric.CCPeripheralsFabric;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.shared.common.TileGeneric;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TileCropSensor extends TileGeneric implements IPeripheralTile {
    private final CropSensorPeripheral peripheral;

    public TileCropSensor() {
        super(CCPeripheralsFabric.TILE_CROP_SENSOR);
        this.peripheral = new Peripheral(this);
    }


    @Override
    public IPeripheral getPeripheral(Direction direction) {
        return this.peripheral;
    }


    private static final class Peripheral extends CropSensorPeripheral {
        private final TileCropSensor sensor;
        private Peripheral(TileCropSensor sensor) {
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
