package ccperipheralsfabric.types;

import dan200.computercraft.api.lua.LuaFunction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class EntityType {
    private final String name;
    private final String identifier;
    private final String customName;
    private final float health;

    public EntityType(LivingEntity livingEntity) {
        this.name = new TranslatableText(livingEntity.getType().getTranslationKey()).toString();
        this.identifier = Registry.ENTITY_TYPE.getId(livingEntity.getType()).toString();
        if (livingEntity.getCustomName() != null) {
            this.customName = livingEntity.getCustomName().toString();
        } else {
            this.customName = this.name;
        }
        this.health = livingEntity.getHealth();
    }

    @LuaFunction
    public String getName() {
        return name;
    }

    @LuaFunction
    public String getIdentifier() {
        return identifier;
    }

    @LuaFunction
    public String getCustomName() {
        return customName;
    }

    @LuaFunction
    public float getHealth() {
        return health;
    }

    @LuaFunction
    public Map<String, Object> toTable() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", this.name);
        data.put("identifier", this.identifier);
        data.put("custom_name", this.customName);
        data.put("health", this.health);
        return data;
    }
}
