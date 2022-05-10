package ccperipheralsfabric.common.peripheral.sensor.player;

import ccperipheralsfabric.types.PlayerType;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class PlayerSensorPeripheral implements IPeripheral {
    private final Set<IComputerAccess> m_computers = new HashSet<>(1);
    private boolean enabled = false;

    @NotNull
    @Override
    public String getType() {
        return "player_sensor";
    }

    public abstract Level getWorld();

    public abstract Vec3 getPosition();

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public synchronized void attach(@NotNull IComputerAccess computer) {
        synchronized (this.m_computers) {
            this.m_computers.add(computer);
        }
        this.enabled = true;
    }

    @Override
    public synchronized void detach(@NotNull IComputerAccess computer) {
        boolean empty;
        synchronized (this.m_computers) {
            this.m_computers.remove(computer);
            empty = this.m_computers.isEmpty();
        }

        if (empty) {
            this.enabled = false;
        }
    }

    @LuaFunction
    public final int getPlayerCount() throws LuaException {
        if (this.getWorld() != null && this.isEnabled() && !this.getWorld().isClientSide) {
            return getPlayerCountMethod();
        }
        return 0;
    }

    public synchronized int getPlayerCountMethod() throws LuaException {
        Level world = this.getWorld();
        List<? extends Player> players = world.players();
        int count = 0;
        if (players.size() > 0) {
            for (Player player: players) {
                if (player.position().distanceTo(this.getPosition()) < 16) {
                    count++;
                }
            }
        }
        return count;
    }

    @LuaFunction
    public final ArrayList<PlayerType> getPlayers() throws LuaException {
        if (this.getWorld() != null && this.isEnabled() && !this.getWorld().isClientSide) {
            return getPlayersMethod();
        }
        return null;
    }

    public synchronized ArrayList<PlayerType> getPlayersMethod() throws LuaException {
        Level world = this.getWorld();
        List<? extends Player> players = world.players();
        if (players.size() > 0) {
            ArrayList<PlayerType> playerTypes = new ArrayList<>();
            for (Player player: players) {
                if (player.position().distanceTo(this.getPosition()) < 16) {
                    playerTypes.add(new PlayerType(player));
                }
            }
            return playerTypes;
        }
        return null;
    }

    public void broadcastPlayers(int count) {
        synchronized (this.m_computers) {
            for (IComputerAccess computer : this.m_computers) {
                computer.queueEvent("player_sensor",
                        computer.getAttachmentName(),
                        count);
            }
        }
    }

}
