package com.kryeit.content.jar_of_tips;

import com.kryeit.registry.ModBlocks;
import com.kryeit.registry.ModEntityTypes;
import com.kryeit.registry.ModItems;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class JarOfTipsProjectile extends ThrowableItemProjectile {

    public NonNullList<ItemStack> inventory ;
    public JarOfTipsProjectile(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);

        inventory = NonNullList.withSize(9, ItemStack.EMPTY);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.JAR_OF_TIPS.get();
    }

    public JarOfTipsProjectile(Player player, Level level) {
        super(ModEntityTypes.JAR_OF_TIPS_PROJECTILE.get(), player, level);
        inventory = NonNullList.withSize(9, ItemStack.EMPTY);
    }

    public void setInventory(NonNullList<ItemStack> inventory) {
        this.inventory = inventory;
    }

    public NonNullList<ItemStack> getInventory() {
        return this.inventory;
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);

        this.kill();
        BlockPos hitPos = blockHitResult.getBlockPos();
        BlockPos placePos = blockHitResult.getBlockPos().offset(blockHitResult.getDirection().getNormal());

        Block block = level().getBlockState(placePos).getBlock();

        if (block == Blocks.WATER || block == Blocks.AIR) {
            level().setBlock(placePos, ModBlocks.JAR_OF_TIPS.get().defaultBlockState(), 3);
            level().playSound(null, placePos, SoundEvents.GLASS_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);

        } else {
            level().playSound(null, placePos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);

            this.drops();
            this.kill();
            return;
        }

        BlockEntity blockEntity = level().getBlockEntity(placePos);
        if (blockEntity instanceof JarOfTipsBlockEntity) {
            ((JarOfTipsBlockEntity) blockEntity).setInventory(this.inventory);
        }
    }

    public void drops() {
        Containers.dropContents(level(), blockPosition(), inventory);
    }

    @SuppressWarnings("unchecked")
    public static FabricEntityTypeBuilder<?> build(FabricEntityTypeBuilder<?> builder) {
        return builder.dimensions(EntityDimensions.fixed(0.25f, 0.25f));
    }

    @Override
    public boolean isNoGravity() {
        return false;
    }

}
