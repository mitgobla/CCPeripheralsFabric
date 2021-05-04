package ccperipheralsfabric.types;

import dan200.computercraft.api.lua.LuaFunction;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class LivingEntityType {
    private final String name;
    private final String identifier;
    private final String customName;
    private final float health;
    private final float maxHealth;
    private final boolean isHostile;

    public LivingEntityType(LivingEntity livingEntity) {
        this.name = I18n.translate(livingEntity.getType().getTranslationKey());
        this.identifier = Registry.ENTITY_TYPE.getId(livingEntity.getType()).toString();
        if (livingEntity.getCustomName() != null) {
            this.customName = livingEntity.getCustomName().asString();
        } else {
            this.customName = this.name;
        }
        this.health = livingEntity.getHealth();
        this.maxHealth = livingEntity.getMaxHealth();
        this.isHostile = livingEntity instanceof HostileEntity;
    }

    @LuaFunction
    public final String getName() {
        return name;
    }

    @LuaFunction
    public final String getIdentifier() {
        return identifier;
    }

    @LuaFunction
    public final String getCustomName() {
        return customName;
    }

    @LuaFunction
    public final float getHealth() {
        return health;
    }

    @LuaFunction
    public final float getMaxHealth() {
        return maxHealth;
    }

    @LuaFunction
    public final boolean isHostile() {
        return isHostile;
    }

    @LuaFunction
    public final Map<String, Object> toTable() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", this.name);
        data.put("identifier", this.identifier);
        data.put("custom_name", this.customName);
        data.put("health", this.health);
        data.put("max_health", this.maxHealth);
        data.put("hostile", this.isHostile);
        return data;
    }
}
