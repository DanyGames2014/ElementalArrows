package net.danygames2014.elementalarrows.entity;

import net.danygames2014.elementalarrows.ElementalArrows;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;

public class EggArrowEntity extends ElementalArrowEntity {
    public EggArrowEntity(World world) {
        super(world);
    }

    public EggArrowEntity(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public EggArrowEntity(World world, LivingEntity owner) {
        super(world, owner);
    }

    @Override
    public boolean hitEntity(Entity entity) {
        if(!(entity instanceof ChickenEntity)) {
            if (entity.damage(this.owner, 1)) {
                this.world.playSound(this, "random.drr", 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            }
        }
        spawnChicken(entity.x, entity.y + 1.0D, entity.z);
        return true;
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
        
        spawnChicken(x + 0.5D, y + 0.5D, z + 0.5D);
        return true;
    }

    public void spawnChicken(double x, double y, double z) {
        if (!ElementalArrows.ARROW_CONFIG.eggArrowConfig.enableChicken) {
            return;
        }
        
        if (this.random.nextInt(ElementalArrows.ARROW_CONFIG.eggArrowConfig.spawnChance) != 0){
            return;
        }
        
        ChickenEntity chicken = new ChickenEntity(world);
        chicken.setPosition(x,y,z);
        world.spawnEntity(chicken);
        
        for (int i = 0; i < 15; i++) {
            this.world.addParticle(
                    "explode",
                    x,
                    y,
                    z,
                    world.random.nextGaussian() * 0.05F,
                    world.random.nextGaussian() * 0.10F + 0.05F,
                    world.random.nextGaussian() * 0.05F
            );
        }
        
    }

    @Override
    public ItemStack getPickupStack() {
        return new ItemStack(Item.ARROW, 1);
    }

    @Override
    public Identifier getHandlerIdentifier() {
        return ElementalArrows.NAMESPACE.id("egg_arrow");
    }
}
