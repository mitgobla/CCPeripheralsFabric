package ccperipheralsfabric.common.peripheral.sensor.entity;

import ccperipheralsfabric.types.ItemType;
import ccperipheralsfabric.types.LivingEntityType;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public abstract class EntitySensorPeripheral implements IPeripheral {
    private final Set<IComputerAccess> m_computers = new HashSet<>(1);
    private boolean enabled = false;

    @NotNull
    @Override
    public String getType() {
        return "entity_sensor";
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
    public final int getEntityCount() throws LuaException {
        if (this.getWorld() != null && this.isEnabled() && !this.getWorld().isClientSide) {
            return getEntityCountMethod();
        }
        return 0;
    }

    public synchronized int getEntityCountMethod() throws LuaException {
        Level world = this.getWorld();
        AABB box = new AABB(new BlockPos(this.getPosition()).offset(-4, -4, -4), new BlockPos(this.getPosition()).offset(5, 5, 5));
        List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, box, null);
        int count = 0;
        for (LivingEntity entity: entities) {
            if (entity instanceof Player) continue;
            count += 1;
        }
        return count;
    }

    @LuaFunction
    public final ArrayList<LivingEntityType> getEntities() throws LuaException {
        if (this.getWorld() != null && this.isEnabled() && !this.getWorld().isClientSide) {
            return getEntitiesMethod();
        }
        return null;
    }

    public synchronized ArrayList<LivingEntityType> getEntitiesMethod() throws LuaException {
        Level world = this.getWorld();
        AABB box = new AABB(new BlockPos(this.getPosition()).offset(-4, -4, -4), new BlockPos(this.getPosition()).offset(5, 5, 5));
        List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, box, null);
        if (entities.size() > 0) {
            ArrayList<LivingEntityType> entityTypes = new ArrayList<>();
            for (LivingEntity entity : entities) {
                if (entity instanceof Player) continue;
                entityTypes.add(new LivingEntityType(entity));
            }
            return entityTypes;
        }
        return null;
    }

}
