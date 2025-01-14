package com.kryeit.content.jar_of_tips;

import com.kryeit.registry.ModBlocks;
import com.kryeit.registry.ModEntityTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class JarOfTipsItem extends BlockItem {

    public NonNullList<ItemStack> inventory;

    public static JarOfTipsItem empty(Properties properties) {
        return new JarOfTipsItem(ModBlocks.JAR_OF_TIPS.get(), properties);
    }

    public JarOfTipsItem(Block block, Properties properties) {
        super(block, properties);

        this.inventory = NonNullList.withSize(9, ItemStack.EMPTY);
    }


    public static void initInventory(ItemStack itemStack, NonNullList<ItemStack> inventory) {
        if (inventory.equals(NonNullList.withSize(9, ItemStack.EMPTY))) {
            return;
        }

        CompoundTag nbt = itemStack.getOrCreateTag();
        if (!nbt.contains("Items")) {
            CompoundTag inventoryTag = new CompoundTag();
            ContainerHelper.saveAllItems(inventoryTag, inventory, true);
            nbt.put("Items", inventoryTag);
            itemStack.setTag(nbt);
        }
    }

    public static NonNullList<ItemStack> getInventory(ItemStack stack) {
        NonNullList<ItemStack> inventory = NonNullList.withSize(9, ItemStack.EMPTY);
        CompoundTag nbt = stack.getTag();
        if (nbt != null && nbt.contains("Items")) {
            ContainerHelper.loadAllItems(nbt.getCompound("Items"), inventory);
        }
        return inventory;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        super.use(level, player, hand);

        if (!level.isClientSide) {
            Entity entity = ModEntityTypes.JAR_OF_TIPS_PROJECTILE.create(level);

            if (entity instanceof JarOfTipsProjectile projectile) {
                projectile.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
                projectile.setOwner(player);
                projectile.setInventory(inventory);

                float pitch = player.getXRot();
                float yaw = player.getYRot();
                projectile.shootFromRotation(player, pitch, yaw, 0.0F, 0.5F, 0.3F);
                level.addFreshEntity(projectile);
                player.getCooldowns().addCooldown(this, 20);
            }

        }

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
    }
}
