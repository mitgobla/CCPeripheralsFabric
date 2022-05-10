package ccperipheralsfabric.common.peripheral.sensor.crop;

import ccperipheralsfabric.CCPeripheralsFabric;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.shared.common.TileGeneric;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TileCropSensor extends TileGeneric implements IPeripheralTile {
    private final CropSensorPeripheral peripheral;

    public TileCropSensor(BlockPos pos, BlockState state) {
        super(CCPeripheralsFabric.TILE_CROP_SENSOR, pos, state);
        this.peripheral = new Peripheral(this);
    }

    @Nullable
    @Override
    public IPeripheral getPeripheral(@NotNull Direction side) {
        return this.peripheral;
    }


    private static final class Peripheral extends CropSensorPeripheral {
        private final TileCropSensor sensor;
        private Peripheral(TileCropSensor sensor) {
            this.sensor = sensor;
        }

        public Level getWorld() {
            return this.sensor.getLevel();
        }

        public Vec3 getPosition() {
            BlockPos pos = this.sensor.getBlockPos();
            return new Vec3(pos.getX(), pos.getY(), pos.getZ());
        }

        public boolean equals(@Nullable IPeripheral other) {
            return this == other || (other instanceof Peripheral && this.sensor == ((Peripheral) other).sensor);
        }



    }
}
