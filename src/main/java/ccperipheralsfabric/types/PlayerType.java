package ccperipheralsfabric.types;

import dan200.computercraft.api.lua.LuaFunction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class PlayerType {
    private final String name;
    private final String uuid;
    private final int experienceLevel;
    private final float health;
    private final int armor;
    private final int food;
    private final BlockPos pos;

    public PlayerType(PlayerEntity player) {
        this.name = player.getGameProfile().getName();
        this.uuid = player.getGameProfile().getId().toString();
        this.experienceLevel = player.experienceLevel;
        this.health = player.getHealth();
        this.armor = player.getArmor();
        this.food = player.getHungerManager().getFoodLevel();
        this.pos = player.getBlockPos();
    }

    @LuaFunction
    public String getName() {
        return name;
    }

    @LuaFunction
    public String getUuid() {
        return uuid;
    }

    @LuaFunction
    public int getExperienceLevel() {
        return experienceLevel;
    }

    @LuaFunction
    public float getHealth() {
        return health;
    }

    @LuaFunction
    public int getArmor() {
        return armor;
    }

    @LuaFunction
    public int getFood() {
        return food;
    }

    @LuaFunction
    public Map<String, Object> toTable() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", this.name);
        data.put("uuid", this.uuid);
        data.put("experience_level", this.experienceLevel);
        data.put("health", this.health);
        data.put("armor", this.armor);
        data.put("food", this.food);
        return data;
    }


}
