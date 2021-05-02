package ccperipheralsfabric.common.peripheral.machine.vacuum;

import ccperipheralsfabric.CCPeripheralsFabric;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.shared.common.TileGeneric;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Tickable;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class TileVacuumMachine extends TileGeneric implements IPeripheralTile, Tickable {
    private final VacuumMachinePeripheral peripheral;

    public TileVacuumMachine() {
        super(CCPeripheralsFabric.TILE_VACUUM_MACHINE);
        this.peripheral = new Peripheral(this);
    }

    @Override
    public IPeripheral getPeripheral(Direction direction) {
        return this.peripheral;
    }

    @Override
    public void tick() {
        boolean enabled = this.getCachedState().get(BlockVacuumMachine.ENABLED);
        if (this.world != null) {
            if (enabled) {
                pullEntities();
            }
        }
    }

    private void pullEntities() {
        if (this.world != null ) {
            Box box = new Box(this.getPos().add(-4.0, -4.0, -4.0), this.getPos().add(5.0, 5.0, 5.0));
            List<Entity> entities = this.world.getEntitiesByClass(LivingEntity.class, box, null);
            Vec3d dirVector = new Vec3d(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()).add(0.5, 0.5, 0.5);
            Vec3d normVector = new Vec3d(0, 0, 0);
            for (Entity entity : entities) {
                normVector = entity.getPos().subtract(dirVector).normalize();
                if (world.raycast(new RaycastContext(dirVector.add(normVector).add(normVector), entity.getPos(), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity)).getType() != HitResult.Type.MISS) {
                    continue;
                }

                if (entity instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) entity;
                    if (player.isCreative() || player.isSpectator()) {
                        continue;
                    }
                }
                normVector = normVector.multiply(0.12);
                entity.setVelocity(entity.getVelocity().subtract(normVector));
            }
        }
    }


    private static final class Peripheral extends VacuumMachinePeripheral {
        private final TileVacuumMachine sensor;
        private Peripheral(TileVacuumMachine sensor) {
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
