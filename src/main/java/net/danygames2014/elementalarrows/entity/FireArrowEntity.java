package net.danygames2014.elementalarrows.entity;

import net.danygames2014.elementalarrows.ElementalArrows;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;

public class FireArrowEntity extends ElementalArrowEntity {
    public FireArrowEntity(World world) {
        super(world);
    }

    public FireArrowEntity(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public FireArrowEntity(World world, LivingEntity owner) {
        super(world, owner);
    }

    @Override
    public void spawnTravelParticles() {
        for (int i = 0; i < 1; i++) {
            this.world.addParticle(
                    "flame",
                    x,
                    y,
                    z,
                    world.random.nextGaussian() * 0.05F,
                    world.random.nextGaussian() * 0.05F + 0.05F,
                    world.random.nextGaussian() * 0.05F
            );
        }
    }

    @Override
    public boolean hitEntity(Entity entity) {
        if (entity.damage(this.owner, 2)) {
            if (ElementalArrows.ARROW_CONFIG.fireArrowConfig.enableEntityFire) {
                entity.fireTicks = ElementalArrows.ARROW_CONFIG.fireArrowConfig.fireTicks;
            }
            this.world.playSound(this, "random.drr", 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            return true;
        }
        return false;
    }

    @Override
    public boolean hitBlock(int x, int y, int z, int side, HitResult hitResult) {
        switch (side) {
            case 0:
                y--;
                break;
            case 1:
                y++;
                break;
            case 2:
                z--;
                break;
            case 3:
                z++;
                break;
            case 4:
                x--;
                break;
            case 5:
                x++;
                break;
        }

        if (world.isAir(x, y, z)) {
            if (ElementalArrows.ARROW_CONFIG.fireArrowConfig.enableBlockFire) {
                world.setBlock(x, y, z, Block.FIRE.id);
            }
            return true;
        }
        return false;
    }

    @Override
    public ItemStack getPickupStack() {
        return new ItemStack(Item.ARROW, 1);
    }

    @Override
    public Identifier getHandlerIdentifier() {
        return ElementalArrows.NAMESPACE.id("fire_arrow");
    }
}
