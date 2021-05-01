package ccperipheralsfabric.common.peripheral.sensor.item;

import ccperipheralsfabric.types.ItemType;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class ItemSensorPeripheral implements IPeripheral {
    private final Set<IComputerAccess> m_computers = new HashSet<>(1);
    private boolean enabled = false;

    @NotNull
    @Override
    public String getType() {
        return "item_sensor";
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
    public final int getItemCount() throws LuaException {
        if (this.getWorld() != null && this.isEnabled() && !this.getWorld().isClient) {
            return getItemCountMethod();
        }
        return 0;
    }

    public synchronized int getItemCountMethod() throws LuaException {
        World world = this.getWorld();
        Box box = new Box(new BlockPos(this.getPosition()).add(-4, 0, -4), new BlockPos(this.getPosition()).add(4, 1, 4));
        List<ItemEntity> items = world.getEntitiesByClass(ItemEntity.class, box, null);
        int count = 0;
        for (ItemEntity item: items) {
            count += item.getStack().getCount();
        }
        return count;
    }

    @LuaFunction
    public final ArrayList<ItemType> getItems() throws LuaException {
        if (this.getWorld() != null && this.isEnabled() && !this.getWorld().isClient) {
            return getItemsMethod();
        }
        return null;
    }

    public synchronized ArrayList<ItemType> getItemsMethod() throws LuaException {
        World world = this.getWorld();
        Box box = new Box(new BlockPos(this.getPosition()).add(-4, 0, -4), new BlockPos(this.getPosition()).add(4, 1, 4));
        List<ItemEntity> items = world.getEntitiesByClass(ItemEntity.class, box, null);
        if (items.size() > 0) {
            ArrayList<ItemType> itemTypes = new ArrayList<>();
            for (ItemEntity item : items) {
                itemTypes.add(new ItemType(item));
            }
            return itemTypes;
        }
        return null;
    }

    public void broadcastItems(int count) {
        synchronized (this.m_computers) {
            for (IComputerAccess computer : this.m_computers) {
                computer.queueEvent("item_sensor",
                        computer.getAttachmentName(),
                        count);
            }
        }
    }

}
