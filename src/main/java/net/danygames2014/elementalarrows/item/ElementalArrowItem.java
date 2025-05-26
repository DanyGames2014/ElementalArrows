package net.danygames2014.elementalarrows.item;

import net.danygames2014.elementalarrows.entity.ElementalArrowEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;

import java.lang.reflect.InvocationTargetException;

public class ElementalArrowItem extends TemplateItem {
    Class<? extends ElementalArrowEntity> arrowClass;
    
    public ElementalArrowItem(Identifier identifier, Class<? extends ElementalArrowEntity> arrowClass) {
        super(identifier);
        this.arrowClass = arrowClass;
    }

    public Entity getArrowEntity(World world) {
        try {
            return arrowClass.getDeclaredConstructor(World.class).newInstance(world);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
    
    public Entity getArrowEntity(World world, PlayerEntity owner) {
        try {
            return arrowClass.getDeclaredConstructor(World.class, LivingEntity.class).newInstance(world, owner);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
