package ccperipheralsfabric.common.peripheral.sensor.player;

import ccperipheralsfabric.CCPeripheralsFabric;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.shared.common.TileGeneric;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;



public class TilePlayerSensor extends TileGeneric implements IPeripheralTile, Tickable {
    private final PlayerSensorPeripheral peripheral;

    public TilePlayerSensor() {
        super(CCPeripheralsFabric.TILE_PLAYER_SENSOR);
        this.peripheral = new Peripheral(this);
    }

    @Override
    public IPeripheral getPeripheral(Direction direction) {
        return this.peripheral;
    }

    @Override
    public void tick() {
        if (this.world != null && this.peripheral.isEnabled() && !this.world.isClient && this.world.getTime()%20==0) {
            List<? extends PlayerEntity> players = this.world.getPlayers();
            if (players.size() > 0) {
                int count = 0;
                for (PlayerEntity player: players) {
                    if (player.getPos().distanceTo(peripheral.getPosition()) < 16) {
                        count++;
                    }
                }
                if (count > 0) {
                    this.peripheral.broadcastPlayers(count);
                }
            }
        }
    }

    @Nullable
    @Override
    public IPeripheral getPeripheral(@NotNull net.minecraft.class_2350 side) {
        return null;
    }

    private static final class Peripheral extends PlayerSensorPeripheral {
        private final TilePlayerSensor sensor;
        private Peripheral(TilePlayerSensor sensor) {
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
