package ccperipheralsfabric.common.peripheral.sensor.entity;

import ccperipheralsfabric.types.ItemType;
import ccperipheralsfabric.types.LivingEntityType;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class EntitySensorPeripheral implements IPeripheral {
    private final Set<IComputerAccess> m_computers = new HashSet<>(1);
    private boolean enabled = false;

    @NotNull
    @Override
    public String getType() {
        return "entity_sensor";
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
    public final int getEntityCount() throws LuaException {
        if (this.getWorld() != null && this.isEnabled() && !this.getWorld().isClient) {
            return getEntityCountMethod();
        }
        return 0;
    }

    public synchronized int getEntityCountMethod() throws LuaException {
        World world = this.getWorld();
        Box box = new Box(new BlockPos(this.getPosition()).add(-4, -4, -4), new BlockPos(this.getPosition()).add(5, 5, 5));
        List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, box, null);
        int count = 0;
        for (LivingEntity entity: entities) {
            if (entity instanceof PlayerEntity) continue;
            count += 1;
        }
        return count;
    }

    @LuaFunction
    public final ArrayList<LivingEntityType> getEntities() throws LuaException {
        if (this.getWorld() != null && this.isEnabled() && !this.getWorld().isClient) {
            return getEntitiesMethod();
        }
        return null;
    }

    public synchronized ArrayList<LivingEntityType> getEntitiesMethod() throws LuaException {
        World world = this.getWorld();
        Box box = new Box(new BlockPos(this.getPosition()).add(-4, -4, -4), new BlockPos(this.getPosition()).add(5, 5, 5));
        List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, box, null);
        if (entities.size() > 0) {
            ArrayList<LivingEntityType> entityTypes = new ArrayList<>();
            for (LivingEntity entity : entities) {
                if (entity instanceof PlayerEntity) continue;
                entityTypes.add(new LivingEntityType(entity));
            }
            return entityTypes;
        }
        return null;
    }

}
