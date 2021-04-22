package ccperipheralsfabric.common.peripheral.sensor.player;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class PlayerSensorPeripheral implements IPeripheral {
    private final Set<IComputerAccess> m_computers = new HashSet<>(1);
    private boolean enabled = false;

    @NotNull
    @Override
    public String getType() {
        return "player_sensor";
    }

    public abstract World getWorld();

    public abstract Vec3d getPosition();

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
        if (this.getWorld() != null && this.isEnabled() && !this.getWorld().isClient) {
            return getPlayerCountMethod();
        }
        return 0;
    }

    public synchronized int getPlayerCountMethod() throws LuaException {
        World world = this.getWorld();
        List<? extends PlayerEntity> players = world.getPlayers();
        int count = 0;
        if (players.size() > 0) {
            for (PlayerEntity player: players) {
                if (player.getPos().distanceTo(this.getPosition()) < 16) {
                    count++;
                }
            }
        }
        return count;
    }

    @LuaFunction
    public final ArrayList<String> getPlayers() throws LuaException {
        if (this.getWorld() != null && this.isEnabled() && !this.getWorld().isClient) {
            return getPlayersMethod();
        }
        return null;
    }

    public synchronized ArrayList<String> getPlayersMethod() throws LuaException {
        World world = this.getWorld();
        List<? extends PlayerEntity> players = world.getPlayers();
        if (players.size() > 0) {
            ArrayList<String> playerNames = new ArrayList<>();
            for (PlayerEntity player: players) {
                if (player.getPos().distanceTo(this.getPosition()) < 16) {
                    playerNames.add(player.getGameProfile().getName());
                }
            }
            return playerNames;
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
