package ccperipheralsfabric.types;

import dan200.computercraft.api.lua.LuaFunction;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public class PlayerType {
    private final String name;
    private final String uuid;
    private final int experienceLevel;
    private final float health;
    private final int armor;
    private final int food;
    private final BlockPos pos;

    public PlayerType(Player player) {
        this.name = player.getGameProfile().getName();
        this.uuid = player.getGameProfile().getId().toString();
        this.experienceLevel = player.experienceLevel;
        this.health = player.getHealth();
        this.armor = player.getArmorValue();
        this.food = player.getFoodData().getFoodLevel();
        this.pos = player.blockPosition();
    }

    @LuaFunction
    public final String getName() {
        return name;
    }

    @LuaFunction
    public final String getUuid() {
        return uuid;
    }

    @LuaFunction
    public final int getExperienceLevel() {
        return experienceLevel;
    }

    @LuaFunction
    public final float getHealth() {
        return health;
    }

    @LuaFunction
    public final int getArmor() {
        return armor;
    }

    @LuaFunction
    public final int getFood() {
        return food;
    }

    @LuaFunction
    public final Map<String, Object> toTable() {
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
