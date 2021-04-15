package ccperipheralsfabric.common.peripheral.machine.fan;

import ccperipheralsfabric.CCPeripheralsFabric;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.shared.common.TileGeneric;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class TileFanMachine extends TileGeneric implements IPeripheralTile, Tickable {
    private final FanMachinePeripheral peripheral;

    public TileFanMachine() {
        super(CCPeripheralsFabric.TILE_FAN_MACHINE);
        this.peripheral = new Peripheral(this);
    }

    @Override
    public IPeripheral getPeripheral(Direction direction) {
        return this.peripheral;
    }

    @Override
    public void tick() {
        boolean enabled = this.getCachedState().get(BlockFanMachine.ENABLED);
        if (this.world != null && !this.world.isClient) {
            if (enabled) {
                pushEntities();
            }
        }
    }

    private void pushEntities() {
        Direction direction = this.getCachedState().get(BlockFanMachine.FACING);
        Box box = new Box(this.getPos(), this.getPos().offset(direction, 5).add(1, 1, 1));
        List<Entity> entities = this.world.getEntitiesByClass(Entity.class, box, null);
        Vec3d dirVector = new Vec3d(direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ());
        dirVector = dirVector.multiply(0.15);
        for (Entity entity: entities) {
            entity.setVelocity(entity.getVelocity().add(dirVector));
        }
    }


    private static final class Peripheral extends FanMachinePeripheral {
        private final TileFanMachine sensor;
        private Peripheral(TileFanMachine sensor) {
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
