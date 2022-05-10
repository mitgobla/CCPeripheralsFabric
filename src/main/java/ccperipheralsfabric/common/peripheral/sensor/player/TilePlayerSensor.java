package ccperipheralsfabric.common.peripheral.sensor.player;

import ccperipheralsfabric.CCPeripheralsFabric;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.shared.common.TileGeneric;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;



public class TilePlayerSensor extends TileGeneric implements IPeripheralTile {
    private final PlayerSensorPeripheral peripheral;

    public TilePlayerSensor(BlockPos pos, BlockState state) {
        super(CCPeripheralsFabric.TILE_PLAYER_SENSOR, pos, state);
        this.peripheral = new Peripheral(this);
    }

    @Override
    public IPeripheral getPeripheral(Direction direction) {
        return this.peripheral;
    }

    @Override
    public void blockTick() {
        if (this.level != null && this.peripheral.isEnabled() && !this.level.isClientSide && this.level.getGameTime()%20==0) {
            List<? extends Player> players = this.level.players();
            if (players.size() > 0) {
                int count = 0;
                for (Player player: players) {
                    if (player.position().distanceTo(peripheral.getPosition()) < 16) {
                        count++;
                    }
                }
                if (count > 0) {
                    this.peripheral.broadcastPlayers(count);
                }
            }
        }
    }

    private static final class Peripheral extends PlayerSensorPeripheral {
        private final TilePlayerSensor sensor;
        private Peripheral(TilePlayerSensor sensor) {
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
