package net.danygames2014.elementalarrows.entity;

import net.danygames2014.elementalarrows.ElementalArrows;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;

public class LightingArrowEntity extends ElementalArrowEntity {
    public LightingArrowEntity(World world) {
        super(world);
    }

    public LightingArrowEntity(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public LightingArrowEntity(World world, LivingEntity owner) {
        super(world, owner);
    }

    @Override
    public boolean hitEntity(Entity entity) {
        summonLighting();
        return true;
    }

    @Override
    public boolean hitBlock(int blockX, int blockY, int blockZ, int side, HitResult hitResult) {
        summonLighting();
        return true;
    }

    public void summonLighting() {
        if (ElementalArrows.ARROW_CONFIG.lightningArrowConfig.enableLightning) {
            world.spawnGlobalEntity(new LightningEntity(world, this.x, this.y, this.z));
        }
    }

    @Override
    public ItemStack getPickupStack() {
        return new ItemStack(Item.ARROW, 1);
    }

    @Override
    public Identifier getHandlerIdentifier() {
        return ElementalArrows.NAMESPACE.id("lighting_arrow");
    }
}
