package ccperipheralsfabric.common.peripheral.machine.fan;

import ccperipheralsfabric.CCPeripheralsFabric;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.shared.common.TileGeneric;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;


public class TileFanMachine extends TileGeneric implements IPeripheralTile {
    private final FanMachinePeripheral peripheral;

    public static final Predicate<Entity> PUSHABLE_ENTITY = entity -> !entity.isSpectator() && entity.getPistonPushReaction() != PushReaction.IGNORE;

    public TileFanMachine(BlockPos pos, BlockState state) {
        super(CCPeripheralsFabric.TILE_FAN_MACHINE, pos, state);
        this.peripheral = new Peripheral(this);
    }

    @Override
    public IPeripheral getPeripheral(Direction direction) {
        return this.peripheral;
    }

    protected void tick() {
        boolean enabled = this.getBlockState().getValue(BlockFanMachine.ENABLED);
        if (this.level != null) {
            if (enabled) {
                pushEntities();
            }
        }
    }


    private void pushEntities() {
        if (this.level != null ) {
            Direction direction = this.getBlockState().getValue(BlockFanMachine.FACING);
            int distance = 0;
            for (int i = 1; i <= 5; i++) {
                if (!this.level.getBlockState(this.getBlockPos().relative(direction, i)).isAir() && !(this.level.getBlockState(this.getBlockPos().relative(direction, i)).getBlock() instanceof LiquidBlock)) break;
                distance = i;
            }

            for (int i = 1; i <= distance; i++) {
                if (this.level.getBlockState(this.getBlockPos().relative(direction, i)).getBlock() instanceof LiquidBlock || this.level.getBlockState(this.getBlockPos().relative(direction, i)).getBlock() instanceof BucketPickup) {
                    BlockPos particlePos = new BlockPos(this.getBlockPos()).relative(direction, i).offset(0.5, 0.5, 0.5);
                    if (level.random.nextInt((i/distance)*1000 + 10) == 0) {
                        Vec3i particleDir = direction.getNormal();
                        level.addParticle(ParticleTypes.BUBBLE, particlePos.getX() + this.level.random.nextDouble(), particlePos.getY() + this.level.random.nextDouble(), particlePos.getZ() + this.level.random.nextDouble(), particleDir.getX() * 2.0, particleDir.getY() * 2.0, particleDir.getZ() * 2.0);
                    }

                    if (level.random.nextInt(2000) == 0 && !level.isClientSide) {
                        level.playSound(null, particlePos.getX(), particlePos.getY(), particlePos.getZ(), SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundSource.BLOCKS, 0.2F + level.random.nextFloat() * 0.2F, 0.9F + level.random.nextFloat() * 0.15F);
                    }
                }
            }


            AABB box = new AABB(this.getBlockPos(), this.getBlockPos().relative(direction, distance).offset(1, 1, 1));
            List<Entity> entities = this.level.getEntitiesOfClass(Entity.class, box, PUSHABLE_ENTITY);
            Vec3 dirVector = new Vec3(direction.getStepX(), direction.getStepY(), direction.getStepZ());
            dirVector = dirVector.scale(0.15);
            for (Entity entity : entities) {
                if (entity instanceof Player player) {
                    if (player.isCreative() || player.isSpectator()) {
                        continue;
                    }
                }
                entity.setDeltaMovement(entity.getDeltaMovement().add(dirVector));
            }
        }
    }




    private static final class Peripheral extends FanMachinePeripheral {
        private final TileFanMachine sensor;
        private Peripheral(TileFanMachine sensor) {
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
