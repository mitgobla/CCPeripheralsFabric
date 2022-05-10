package ccperipheralsfabric.common.peripheral.sensor.item;

import ccperipheralsfabric.CCPeripheralsFabric;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.shared.common.TileGeneric;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class TileItemSensor extends TileGeneric implements IPeripheralTile{
    private final ItemSensorPeripheral peripheral;

    public TileItemSensor(BlockPos pos, BlockState state) {
        super(CCPeripheralsFabric.TILE_ITEM_SENSOR, pos, state);
        this.peripheral = new Peripheral(this);
    }

    @Override
    public void blockTick() {
        if (this.level != null && this.peripheral.isEnabled() && !this.level.isClientSide && this.level.getGameTime()%20==0) {
            AABB box = new AABB(new BlockPos(this.getBlockPos()).offset(-4, 0, -4), new BlockPos(this.getBlockPos()).offset(4, 1, 4));
            List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, box, null);
            if (items.size() > 0) {
                int count = 0;
                for (ItemEntity item: items) {
                    count += item.getItem().getCount();
                }
                this.peripheral.broadcastItems(count);
            }
        }
    }

    @Override
    public IPeripheral getPeripheral(Direction direction) {
        return this.peripheral;
    }


    private static final class Peripheral extends ItemSensorPeripheral {
        private final TileItemSensor sensor;
        private Peripheral(TileItemSensor sensor) {
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
