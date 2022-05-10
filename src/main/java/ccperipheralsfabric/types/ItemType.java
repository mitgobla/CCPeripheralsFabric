package ccperipheralsfabric.types;

import dan200.computercraft.api.lua.LuaFunction;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.item.ItemEntity;

public class ItemType {
    private final String name;
    private final String customName;
    private final int count;
    private final int maxCount;
    private final int damage;
    private final int maxDamage;
    private final boolean enchanted;

    public ItemType(ItemEntity item) {
        this.name = Registry.ITEM.getKey(item.getItem().getItem()).toString();
        this.customName = item.getItem().getHoverName().getContents();
        this.count = item.getItem().getCount();
        this.maxCount = item.getItem().getMaxStackSize();
        this.damage = item.getItem().getDamageValue();
        this.maxDamage = item.getItem().getMaxDamage();
        this.enchanted = item.getItem().isEnchanted();
    }

    @LuaFunction
    public final String getName() {
        return name;
    }

    @LuaFunction
    public final String getCustomName() {
        return customName.equals("") ? this.name : this.customName;
    }

    @LuaFunction
    public final int getCount() {
        return count;
    }

    @LuaFunction
    public final int getMaxCount() {
        return maxCount;
    }

    @LuaFunction
    public final int getDamage() {
        return damage;
    }

    @LuaFunction
    public final int getMaxDamage() {
        return maxDamage;
    }

    @LuaFunction
    public final boolean isEnchanted() {
        return enchanted;
    }

    @LuaFunction
    public final Map<String, Object> toTable() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", this.name);
        data.put("custom_name", this.customName);
        data.put("count", this.count);
        data.put("max_count", this.maxCount);
        data.put("damage", this.damage);
        data.put("max_damage", this.maxDamage);
        data.put("enchanted", this.enchanted);
        return data;
    }
}
