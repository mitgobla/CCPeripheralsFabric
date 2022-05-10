package ccperipheralsfabric.common.item;

import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.UUID;

public class SecurityCardItem extends Item {


    public SecurityCardItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (user.isShiftKeyDown()) {
            CompoundTag tag = user.getItemInHand(hand).getTag();
            if (tag == null) {
                tag = new CompoundTag();
            }
            // Switch to a Player Security Card
            if (tag.contains("entity")) tag.remove("entity");
            tag.putString("player", user.getGameProfile().getId().toString());
            user.getItemInHand(hand).setTag(tag);
            user.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1, 0.5F);
            if (world.isClientSide()) {
                TranslatableComponent text = new TranslatableComponent("item.ccperipherals.security_card.changed");
                user.displayClientMessage(text.append(user.getGameProfile().getName()), true);
            }
            return InteractionResultHolder.success(user.getItemInHand(hand));
        } else {
            HitResult result = user.pick(10, 0, true);
            List<LivingEntity> nearestEntities = world.getEntitiesOfClass(LivingEntity.class, new AABB(result.getLocation().add(-4, -4, -4), result.getLocation().add(5, 5, 5)), LivingEntity::isAlive);
            if (nearestEntities.size() == 0) {
                // Reset Security Card, since there are no entities in ray, and player is not crouching.
                CompoundTag tag = user.getItemInHand(hand).getTag();
                if (tag == null) return InteractionResultHolder.success(user.getItemInHand(hand));
                if (tag.isEmpty()) return InteractionResultHolder.success(user.getItemInHand(hand));
                if (tag.contains("player")) tag.remove("player");
                if (tag.contains("entity")) tag.remove("entity");
                if (world.isClientSide()) {
                    TranslatableComponent text = new TranslatableComponent("item.ccperipherals.security_card.removed");
                    user.displayClientMessage(text, true);
                }
                return InteractionResultHolder.success(user.getItemInHand(hand));
            }
            LivingEntity nearestEntity = nearestEntities.get(0);
            if (nearestEntity != null) {
                // Ignore entity if it is the user
                if (nearestEntity.equals(user)) return InteractionResultHolder.pass(user.getItemInHand(hand));
                // Ignore other players. they must shift-right-click themselves.
                if (nearestEntity instanceof Player) {
                    if (user.getCommandSenderWorld().isClientSide()) {
                        user.displayClientMessage(new TranslatableComponent("item.ccperipherals.security_card.fail"), true);
                    }
                    return InteractionResultHolder.pass(user.getItemInHand(hand));
                }

                // Switch to an Entity Security Card
                CompoundTag tag = user.getItemInHand(hand).getTag();
                if (tag == null) {
                    tag = new CompoundTag();
                }
                if (tag.contains("player")) tag.remove("player");
                if (tag.contains("entity")) {
                    if (tag.getString("entity").equals(Registry.ENTITY_TYPE.getKey(nearestEntity.getType()).toString())) {
                        return InteractionResultHolder.pass(user.getItemInHand(hand));
                    }
                }
                tag.putString("entity", Registry.ENTITY_TYPE.getKey(nearestEntity.getType()).toString());
                user.getItemInHand(hand).setTag(tag);
                user.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1, 0.5F);
                if (user.getCommandSenderWorld().isClientSide()) {
                    TranslatableComponent text = new TranslatableComponent("item.ccperipherals.security_card.changed");
                    user.displayClientMessage(text.append(new TranslatableComponent(nearestEntity.getType().getDescriptionId())), true);
                }
                return InteractionResultHolder.success(user.getItemInHand(hand));
            }
        }
        return InteractionResultHolder.pass(user.getItemInHand(hand));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null) {
            return tag.contains("entity") || tag.contains("player");
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        CompoundTag tag = stack.getTag();
        if (tag != null && world != null) {
            if (tag.contains("player")) {
                Player player = world.getPlayerByUUID(UUID.fromString(tag.getString("player")));
                if (player != null) {
                    tooltip.add(new TextComponent(player.getGameProfile().getName()));
                }
            }
            if (tag.contains("entity")) {
                tooltip.add(new TranslatableComponent(Registry.ENTITY_TYPE.get(ResourceLocation.tryParse(tag.getString("entity"))).getDescriptionId()));
            }
        }
    }
}
