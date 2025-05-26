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

public class ExplosiveArrowEntity extends ElementalArrowEntity {
    public ExplosiveArrowEntity(World world) {
        super(world);
    }

    public ExplosiveArrowEntity(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public ExplosiveArrowEntity(World world, LivingEntity owner) {
        super(world, owner);
    }

    @Override
    public void spawnTravelParticles() {
        for (int i = 0; i < 1; i++) {
            this.world.addParticle(
                    "smoke",
                    x,
                    y,
                    z,
                    world.random.nextGaussian() * 0.05F,
                    world.random.nextGaussian() * 0.05F,
                    world.random.nextGaussian() * 0.05F
            );

            if (random.nextInt(4) == 0) {
                this.world.addParticle(
                        "flame",
                        x,
                        y,
                        z,
                        world.random.nextGaussian() * 0.05F,
                        world.random.nextGaussian() * 0.05F,
                        world.random.nextGaussian() * 0.05F
                );
            }
        }
    }

    @Override
    public Block getArrowBlock() {
        return Block.TNT;
    }

    @Override
    public float getArrowBlockScale() {
        return 0.25F;
    }

    @Override
    public boolean hitEntity(Entity entity) {
        explode();
        return true;
    }

    @Override
    public boolean hitBlock(int x, int y, int z, int side, HitResult hitResult) {
        explode();
        return true;
    }

    public void explode() {
        this.world.createExplosion(this, this.x, this.y, this.z, 3.0F);
    }


    @Override
    public ItemStack getPickupStack() {
        return new ItemStack(Item.ARROW, 1);
    }

    @Override
    public Identifier getHandlerIdentifier() {
        return ElementalArrows.NAMESPACE.id("explosive_arrow");
    }
}
