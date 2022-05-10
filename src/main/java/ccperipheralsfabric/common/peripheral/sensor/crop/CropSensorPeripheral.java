package ccperipheralsfabric.common.peripheral.sensor.crop;

import com.mojang.math.Vector3f;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public abstract class CropSensorPeripheral implements IPeripheral {
    @NotNull
    @Override
    public String getType() {
        return "crop_sensor";
    }

    public abstract Level getWorld();

    public abstract Vec3 getPosition();

    @LuaFunction
    public final int getCropCount() throws LuaException {
        if (this.getWorld() != null && !this.getWorld().isClientSide) {
            return getCropCountMethod(false, false);
        }
        return 0;
    }

    @LuaFunction
    public final int getMatureCropCount() throws LuaException {
        if (this.getWorld() != null && !this.getWorld().isClientSide) {
            return getCropCountMethod(true, false);
        }
        return 0;
    }

    @LuaFunction
    public final void showCrops() throws LuaException {
        getCropCountMethod(false, true);
    }

    public synchronized int getCropCountMethod(boolean getMature, boolean showBox) throws LuaException {
        Level world = this.getWorld();
        Direction direction = world.getBlockState(new BlockPos(this.getPosition())).getValue(BlockStateProperties.FACING);
        BlockPos pos1 = new BlockPos(this.getPosition()).offset(-4.0, 0.0, -4.0);
        BlockPos pos2 = new BlockPos(this.getPosition()).offset(4.0, 1.0, 4.0);
        pos1 = pos1.relative(direction, 5);
        pos2 = pos2.relative(direction, 5);
        AABB box = new AABB(pos1, pos2);
        DustParticleOptions particle = new DustParticleOptions(new Vector3f(0, 0, 1), 1);

        int count = 0;
        for (double i = box.minX; i <= box.maxX; i++) {
            for (double j = box.minZ; j <= box.maxZ; j++) {
                BlockPos pos = new BlockPos(i, box.minY, j);
                BlockState state = world.getBlockState(pos);
                Block block = state.getBlock();
                if (block instanceof CropBlock) {
                    if (showBox) {
                        if (Minecraft.getInstance().level != null) {
                            Minecraft.getInstance().level.addParticle(particle, i + 0.5, box.maxY, j + 0.5, 0, 0, 0);
                        }
                    }
                    if (getMature) {
                        CropBlock crop = (CropBlock) block;
                        if (!crop.isMaxAge(state)) {
                            continue;
                        };
                    }
                    count ++;
                }
            }
        }
        return count;
    }
}
