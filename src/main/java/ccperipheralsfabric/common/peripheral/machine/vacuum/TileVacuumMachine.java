package ccperipheralsfabric.common.peripheral.machine.vacuum;

import ccperipheralsfabric.CCPeripheralsFabric;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.shared.common.TileGeneric;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;


public class TileVacuumMachine extends TileGeneric implements IPeripheralTile {
    private final VacuumMachinePeripheral peripheral;

    public static final Predicate<Entity> PUSHABLE_ENTITY = entity -> !entity.isSpectator() && entity.getPistonPushReaction() != PushReaction.IGNORE;

    public TileVacuumMachine(BlockPos pos, BlockState state) {
        super(CCPeripheralsFabric.TILE_VACUUM_MACHINE, pos, state);
        this.peripheral = new Peripheral(this);
    }

    @Override
    public IPeripheral getPeripheral(Direction direction) {
        return this.peripheral;
    }

    public void tick() {
        boolean enabled = this.getBlockState().getValue(BlockVacuumMachine.ENABLED);
        if (this.level != null) {
            if (enabled) {
                pullEntities();
            }
        }
    }

    private void pullEntities() {
        if (this.level != null ) {
            AABB box = new AABB(this.getBlockPos().offset(-4.0, -4.0, -4.0), this.getBlockPos().offset(5.0, 5.0, 5.0));
            List<LivingEntity> entities = this.level.getEntitiesOfClass(LivingEntity.class, box, PUSHABLE_ENTITY);
            Vec3 dirVector = new Vec3(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ()).add(0.5, 0.5, 0.5);
            Vec3 normVector = new Vec3(0, 0, 0);
            for (LivingEntity entity : entities) {
                normVector = entity.position().subtract(dirVector).normalize();

                if (level.clip(new ClipContext(dirVector.add(normVector).add(normVector), entity.position(), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)).getType() != HitResult.Type.MISS) {
                    continue;
                }

                if (entity instanceof Player player) {
                    if (player.isCreative() || player.isSpectator()) {
                        continue;
                    }
                }
                normVector = normVector.scale(0.12);
                entity.setDeltaMovement(entity.getDeltaMovement().subtract(normVector));
            }
        }
    }


    private static final class Peripheral extends VacuumMachinePeripheral {
        private final TileVacuumMachine sensor;
        private Peripheral(TileVacuumMachine sensor) {
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
