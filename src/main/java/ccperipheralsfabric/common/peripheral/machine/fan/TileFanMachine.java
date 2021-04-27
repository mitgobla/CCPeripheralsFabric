package ccperipheralsfabric.common.peripheral.machine.fan;

import ccperipheralsfabric.CCPeripheralsFabric;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.shared.common.TileGeneric;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.FluidDrainable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.*;
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
        if (this.world != null) {
            if (enabled) {
                pushEntities();
            }
        }
    }

    private void pushEntities() {
        if (this.world != null ) {
            Direction direction = this.getCachedState().get(BlockFanMachine.FACING);
            int distance = 0;
            for (int i = 1; i <= 5; i++) {
                if (!this.world.getBlockState(this.getPos().offset(direction, i)).isAir() && !(this.world.getBlockState(this.getPos().offset(direction, i)).getBlock() instanceof FluidBlock)) break;
                distance = i;
            }

            for (int i = 1; i <= distance; i++) {
                if (this.world.getBlockState(this.getPos().offset(direction, i)).getBlock() instanceof FluidBlock || this.world.getBlockState(this.getPos().offset(direction, i)).getBlock() instanceof FluidDrainable) {
                    BlockPos particlePos = new BlockPos(this.getPos()).offset(direction, i).add(0.5, 0.5, 0.5);
                    if (world.random.nextInt((i/distance)*1000 + 10) == 0) {
                        Vec3i particleDir = direction.getVector();
                        world.addParticle(ParticleTypes.BUBBLE, particlePos.getX() + this.world.random.nextDouble(), particlePos.getY() + this.world.random.nextDouble(), particlePos.getZ() + this.world.random.nextDouble(), particleDir.getX() * 2.0, particleDir.getY() * 2.0, particleDir.getZ() * 2.0);
                    }

                    if (world.random.nextInt(2000) == 0) {
                        world.playSound(particlePos.getX(), particlePos.getY(), particlePos.getZ(), SoundEvents.BLOCK_BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundCategory.BLOCKS, 0.2F + world.random.nextFloat() * 0.2F, 0.9F + world.random.nextFloat() * 0.15F, false);
                    }
                }
            }


            Box box = new Box(this.getPos(), this.getPos().offset(direction, distance).add(1, 1, 1));
            List<Entity> entities = this.world.getEntitiesByClass(Entity.class, box, null);
            Vec3d dirVector = new Vec3d(direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ());
            dirVector = dirVector.multiply(0.15);
            for (Entity entity : entities) {
                if (entity instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) entity;
                    if (player.isCreative() || player.isSpectator()) {
                        continue;
                    }
                }
                entity.setVelocity(entity.getVelocity().add(dirVector));
            }
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
