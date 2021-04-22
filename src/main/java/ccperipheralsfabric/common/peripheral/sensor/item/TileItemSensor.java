package ccperipheralsfabric.common.peripheral.sensor.item;

import ccperipheralsfabric.CCPeripheralsFabric;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.shared.common.TileGeneric;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class TileItemSensor extends TileGeneric implements IPeripheralTile, Tickable {
    private final ItemSensorPeripheral peripheral;

    public TileItemSensor() {
        super(CCPeripheralsFabric.TILE_ITEM_SENSOR);
        this.peripheral = new Peripheral(this);
    }

    @Override
    public void tick() {
        if (this.world != null && this.peripheral.isEnabled() && !this.world.isClient && this.world.getTime()%20==0) {
            Box box = new Box(new BlockPos(this.getPos()).add(-4, 0, -4), new BlockPos(this.getPos()).add(4, 1, 4));
            List<ItemEntity> items = world.getEntitiesByClass(ItemEntity.class, box, null);
            if (items.size() > 0) {
                int count = 0;
                for (ItemEntity item: items) {
                    count += item.getStack().getCount();
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
