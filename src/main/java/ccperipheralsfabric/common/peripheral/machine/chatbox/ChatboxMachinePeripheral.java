package ccperipheralsfabric.common.peripheral.machine.chatbox;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public abstract class ChatboxMachinePeripheral implements IPeripheral {
    @NotNull
    @Override
    public String getType() {
        return "chatbox_machine";
    }

    public abstract World getWorld();

    public abstract Vec3d getPosition();

    /**
     * Send a message to all players.
     * @param message The message to post to all players.
     * @param actionBar If the message should appear on the actionBar
     * @return Whether the message was sent.
     * @throws LuaException If the message was an incorrect format.
     */
    @LuaFunction
    public final boolean sendMessage(String message, Optional<Boolean> actionBar) throws LuaException {
        if (message.length() == 0 || message.length() > 256) return false;
        return this.sendMessageMethod(message, actionBar.orElse(false));
    }

    private synchronized boolean sendMessageMethod(String message, boolean actionBar) throws LuaException {
        World world = this.getWorld();

        List<? extends PlayerEntity> players = world.getPlayers();

        if (players.size() == 0) {
            return false;
        }

        for (PlayerEntity player : players) {
            if (player.getPos().distanceTo(this.getPosition()) < 32) {
                player.sendMessage(new LiteralText(message), actionBar);
            }
        }
        return true;
    }

}
