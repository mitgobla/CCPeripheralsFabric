package ccperipheralsfabric.common.peripheral.machine.chatbox;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class ChatboxMachinePeripheral implements IPeripheral {
    @NotNull
    @Override
    public String getType() {
        return "chatbox_machine";
    }

    public abstract Level getWorld();

    public abstract Vec3 getPosition();

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
        Level world = this.getWorld();

        List<? extends Player> players = world.players();

        if (players.size() == 0) {
            return false;
        }

        for (Player player : players) {
            if (player.position().distanceTo(this.getPosition()) < 32) {
                player.displayClientMessage(new TextComponent(message), actionBar);
            }
        }
        return true;
    }

}
