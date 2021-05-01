package ccperipheralsfabric.types;

import dan200.computercraft.api.lua.LuaFunction;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class ItemType {
    private final String name;
    private final String customName;
    private final int count;
    private final int maxCount;
    private final int damage;
    private final int maxDamage;
    private final boolean enchanted;

    public ItemType(ItemEntity item) {
        this.name = Registry.ITEM.getId(item.getStack().getItem()).toString();
        this.customName = item.getStack().getName().asString();
        this.count = item.getStack().getCount();
        this.maxCount = item.getStack().getMaxCount();
        this.damage = item.getStack().getDamage();
        this.maxDamage = item.getStack().getMaxDamage();
        this.enchanted = item.getStack().hasEnchantments();
    }

    @LuaFunction
    public String getName() {
        return name;
    }

    @LuaFunction
    public String getCustomName() {
        return customName.equals("") ? this.name : this.customName;
    }

    @LuaFunction
    public int getCount() {
        return count;
    }

    @LuaFunction
    public int getMaxCount() {
        return maxCount;
    }

    @LuaFunction
    public int getDamage() {
        return damage;
    }

    @LuaFunction
    public int getMaxDamage() {
        return maxDamage;
    }

    @LuaFunction
    public boolean isEnchanted() {
        return enchanted;
    }

    @LuaFunction
    public Map<String, Object> toTable() {
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
