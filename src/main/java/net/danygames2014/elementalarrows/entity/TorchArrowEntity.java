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

public class TorchArrowEntity extends ElementalArrowEntity {
    public TorchArrowEntity(World world) {
        super(world);
    }

    public TorchArrowEntity(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public TorchArrowEntity(World world, LivingEntity owner) {
        super(world, owner);
    }

    @Override
    public void spawnTravelParticles() {
        this.world.addParticle(
                "smoke",
                x,
                y,
                z,
                world.random.nextGaussian() * 0.05F,
                world.random.nextGaussian() * 0.05F + 0.05F,
                world.random.nextGaussian() * 0.05F
        );
    }

    @Override
    public Block getArrowBlock() {
        return Block.TORCH;
    }

    @Override
    public float getArrowBlockScale() {
        return 0.75F;
    }

    @Override
    public float getArrowBlockBrightnessMultiplier() {
        return 2.0F;
    }

    @Override
    public boolean hitEntity(Entity entity) {
        if (entity.damage(this.owner, 2)) {
            entity.fireTicks = 10;
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

        if (world.isAir(x, y, z) && Block.TORCH.canPlaceAt(world, x, y, z)) {
            world.setBlock(x, y, z, Block.TORCH.id);
            return true;
        }
        
        return false;
    }

    @Override
    public ItemStack getPickupStack() {
        return new ItemStack(ElementalArrows.torchArrow, 1);
    }

    @Override
    public Identifier getHandlerIdentifier() {
        return ElementalArrows.NAMESPACE.id("torch_arrow");
    }
}
