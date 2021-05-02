package ccperipheralsfabric.common.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class SecurityCardItem extends Item {


    public SecurityCardItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.isSneaking()) {
            CompoundTag tag = user.getStackInHand(hand).getTag();
            if (tag == null) {
                tag = new CompoundTag();
            }
            // Switch to a Player Security Card
            if (tag.contains("entity")) tag.remove("entity");
            tag.putString("player", user.getGameProfile().getId().toString());
            user.getStackInHand(hand).setTag(tag);
            user.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.5F);
            if (world.isClient()) {
                TranslatableText text = new TranslatableText("item.ccperipherals.security_card.changed");
                user.sendMessage(text.append(user.getGameProfile().getName()), true);
            }
            return TypedActionResult.success(user.getStackInHand(hand));
        } else {
            HitResult result = user.raycast(10, 0, true);
            List<LivingEntity> nearestEntities = world.getEntitiesByClass(LivingEntity.class, new Box(result.getPos().add(-4, -4, -4), result.getPos().add(5, 5, 5)), LivingEntity::isAlive);
            if (nearestEntities.size() == 0) {
                // Reset Security Card, since there are no entities in ray, and player is not crouching.
                CompoundTag tag = user.getStackInHand(hand).getTag();
                if (tag == null) return TypedActionResult.success(user.getStackInHand(hand));
                if (tag.isEmpty()) return TypedActionResult.success(user.getStackInHand(hand));
                if (tag.contains("player")) tag.remove("player");
                if (tag.contains("entity")) tag.remove("entity");
                if (world.isClient()) {
                    TranslatableText text = new TranslatableText("item.ccperipherals.security_card.removed");
                    user.sendMessage(text, true);
                }
                return TypedActionResult.success(user.getStackInHand(hand));
            }
            LivingEntity nearestEntity = nearestEntities.get(0);
            if (nearestEntity != null) {
                // Ignore entity if it is the user
                if (nearestEntity.equals(user)) return TypedActionResult.pass(user.getStackInHand(hand));
                // Ignore other players. they must shift-right-click themselves.
                if (nearestEntity instanceof PlayerEntity) {
                    if (user.getEntityWorld().isClient()) {
                        user.sendMessage(new TranslatableText("item.ccperipherals.security_card.fail"), true);
                    }
                    return TypedActionResult.pass(user.getStackInHand(hand));
                }

                // Switch to an Entity Security Card
                CompoundTag tag = user.getStackInHand(hand).getTag();
                if (tag == null) {
                    tag = new CompoundTag();
                }
                if (tag.contains("player")) tag.remove("player");
                if (tag.contains("entity")) {
                    if (tag.getString("entity").equals(Registry.ENTITY_TYPE.getId(nearestEntity.getType()).toString())) {
                        return TypedActionResult.pass(user.getStackInHand(hand));
                    }
                }
                tag.putString("entity", Registry.ENTITY_TYPE.getId(nearestEntity.getType()).toString());
                user.getStackInHand(hand).setTag(tag);
                user.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.5F);
                if (user.getEntityWorld().isClient()) {
                    TranslatableText text = new TranslatableText("item.ccperipherals.security_card.changed");
                    user.sendMessage(text.append(new TranslatableText(nearestEntity.getType().getTranslationKey())), true);
                }
                return TypedActionResult.success(user.getStackInHand(hand));
            }
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null) {
            return tag.contains("entity") || tag.contains("player");
        }
        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        CompoundTag tag = stack.getTag();
        if (tag != null && world != null) {
            if (tag.contains("player")) {
                PlayerEntity player = world.getPlayerByUuid(UUID.fromString(tag.getString("player")));
                if (player != null) {
                    tooltip.add(new LiteralText(player.getGameProfile().getName()));
                }
            }
            if (tag.contains("entity")) {
                tooltip.add(new TranslatableText(Registry.ENTITY_TYPE.get(Identifier.tryParse(tag.getString("entity"))).getTranslationKey()));
            }
        }
    }
}
