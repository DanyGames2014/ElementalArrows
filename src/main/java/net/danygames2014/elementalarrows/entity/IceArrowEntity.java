package net.danygames2014.elementalarrows.entity;

import net.danygames2014.elementalarrows.ElementalArrows;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.server.entity.HasTrackingParameters;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.TriState;
import net.modificationstation.stationapi.api.util.math.Vec3d;

public class IceArrowEntity extends ElementalArrowEntity {
    public IceArrowEntity(World world) {
        super(world);
    }

    public IceArrowEntity(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public IceArrowEntity(World world, LivingEntity owner) {
        super(world, owner);
    }

    @Override
    public void spawnTravelParticles() {
        for (int i = 0; i < 2; i++) {
            this.world.addParticle(
                    "snowshovel",
                    x,
                    y,
                    z,
                    world.random.nextGaussian() * 0.05F,
                    world.random.nextGaussian() * 0.05F + 0.05F,
                    world.random.nextGaussian() * 0.05F
            );

            if (random.nextInt(5) == 0) {
                this.world.addParticle(
                        "snowballpoof",
                        x,
                        y,
                        z,
                        world.random.nextGaussian() * 0.05F,
                        world.random.nextGaussian() * 0.05F + 0.05F,
                        world.random.nextGaussian() * 0.05F
                );
            }
        }
    }

    @Override
    public boolean hitEntity(Entity entity) {
        if (entity.damage(this.owner, 3)) {
            this.world.playSound(this, "random.drr", 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            return true;
        }
        return false;
    }

    @Override
    public boolean hitBlock(int blockX, int blockY, int blockZ, int side, HitResult hitResult) {
        if (side == 0) {
            --blockY;
        }
        if (side == 1) {
            ++blockY;
        }
        if (side == 2) {
            --blockZ;
        }
        if (side == 3) {
            ++blockZ;
        }
        if (side == 4) {
            --blockX;
        }
        if (side == 5) {
            ++blockX;
        }

        int range = 2;
        
        for (int xOffset = -range; xOffset <= range; xOffset++) {
            for (int yOffset = -1; yOffset <= 1; yOffset++) {
                for (int zOffset = -range; zOffset <= range; zOffset++) {
                    int x = blockX + xOffset;
                    int y = blockY + yOffset;
                    int z = blockZ + zOffset;

                    double distance = new Vec3d(blockX, blockY, blockZ).squaredDistanceTo(x,y,z);
                    
                    int odds = (int) Math.max(distance, 1);

                    if(random.nextInt(odds) == 0) {
                        boolean blockDestroyed = false;

                        if (world.getMaterial(x, y, z) == Material.WATER && world.getBlockMeta(x, y, z) == 0) {
                            world.setBlock(x, y, z, Block.ICE.id, 0);
                            blockDestroyed = true;

                        } else if (world.getMaterial(x, y, z) == Material.LAVA && world.getBlockMeta(x, y, z) == 0) {
                            world.setBlock(x, y, z, Block.OBSIDIAN.id, 0);
                            blockDestroyed = true;

                        } else if (world.getBlockId(x, y, z) == Block.FIRE.id) {
                            world.setBlock(x, y, z, 0, 0);
                            blockDestroyed = true;

                        } else if (world.getBlockId(x, y, z) == Block.TORCH.id) {
                            Block.TORCH.dropStacks(world, x, y, z, world.getBlockMeta(x, y, z), 1);
                            world.setBlock(x, y, z, 0, 0);
                            Block.TORCH.getCollisionShape(world, x, y, z);
                            blockDestroyed = true;
                        }

                        if (blockDestroyed) {
                            world.isPosLoaded(x, y, z);
                            for (int i = 0; i < 20; i++) {
                                this.world.addParticle(
                                        "snowballpoof",
                                        x + 0.5D,
                                        y + 0.5D,
                                        z + 0.5D,
                                        world.random.nextGaussian() * 0.25f,
                                        world.random.nextGaussian() * 0.25f + 0.2F,
                                        world.random.nextGaussian() * 0.25f
                                );
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public ItemStack getPickupStack() {
        return new ItemStack(Item.ARROW, 1);
    }

    @Override
    public Identifier getHandlerIdentifier() {
        return ElementalArrows.NAMESPACE.id("ice_arrow");
    }
}
