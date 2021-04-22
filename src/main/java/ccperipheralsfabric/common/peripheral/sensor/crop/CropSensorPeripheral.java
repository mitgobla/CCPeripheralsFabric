package ccperipheralsfabric.common.peripheral.sensor.crop;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public abstract class CropSensorPeripheral implements IPeripheral {
    @NotNull
    @Override
    public String getType() {
        return "crop_sensor";
    }

    public abstract World getWorld();

    public abstract Vec3d getPosition();

    @LuaFunction
    public final int getCropCount() throws LuaException {
        if (this.getWorld() != null && !this.getWorld().isClient) {
            return getCropCountMethod(false, false);
        }
        return 0;
    }

    @LuaFunction
    public final int getMatureCropCount() throws LuaException {
        if (this.getWorld() != null && !this.getWorld().isClient) {
            return getCropCountMethod(true, false);
        }
        return 0;
    }

    @LuaFunction
    public final void showCrops() throws LuaException {
        getCropCountMethod(false, true);
    }

    public synchronized int getCropCountMethod(boolean getMature, boolean showBox) throws LuaException {
        World world = this.getWorld();
        Direction direction = world.getBlockState(new BlockPos(this.getPosition())).get(Properties.FACING);
        BlockPos pos1 = new BlockPos(this.getPosition()).add(-4.0, 0.0, -4.0);
        BlockPos pos2 = new BlockPos(this.getPosition()).add(4.0, 1.0, 4.0);
        pos1 = pos1.offset(direction, 5);
        pos2 = pos2.offset(direction, 5);
        Box box = new Box(pos1, pos2);
        DustParticleEffect particle = new DustParticleEffect(1, 0, 0, 1);

        int count = 0;
        for (double i = box.minX; i <= box.maxX; i++) {
            for (double j = box.minZ; j <= box.maxZ; j++) {
                BlockPos pos = new BlockPos(i, box.minY, j);
                BlockState state = world.getBlockState(pos);
                Block block = state.getBlock();
                if (block instanceof CropBlock) {
                    if (showBox) {
                        if (MinecraftClient.getInstance().world != null) {
                            MinecraftClient.getInstance().world.addParticle(particle, i + 0.5, box.maxY, j + 0.5, 0, 0, 0);
                        }
                    }
                    if (getMature) {
                        CropBlock crop = (CropBlock) block;
                        if (!crop.isMature(state)) {
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
