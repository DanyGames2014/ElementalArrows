package net.danygames2014.elementalarrows;

import net.danygames2014.elementalarrows.config.Config;
import net.danygames2014.elementalarrows.entity.*;
import net.danygames2014.elementalarrows.entity.renderer.ElementalArrowEntityRenderer;
import net.danygames2014.elementalarrows.item.ElementalArrowItem;
import net.glasslauncher.mods.gcapi3.api.ConfigRoot;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.modificationstation.stationapi.api.client.event.render.entity.EntityRendererRegisterEvent;
import net.modificationstation.stationapi.api.dispenser.DispenseEvent;
import net.modificationstation.stationapi.api.dispenser.ItemDispenseContext;
import net.modificationstation.stationapi.api.event.entity.EntityRegister;
import net.modificationstation.stationapi.api.event.registry.EntityHandlerRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import org.apache.logging.log4j.Logger;

public class ElementalArrows {
    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @Entrypoint.Logger
    public static Logger LOGGER = Null.get();

    @ConfigRoot(value = "arrow", visibleName = "Arrow")
    public static final Config.ArrowConfig ARROW_CONFIG = new Config.ArrowConfig();

    public static Item explosiveArrow;
    public static Item fireArrow;
    public static Item lightingArrow;
    public static Item iceArrow;
    public static Item torchArrow;
    public static Item eggArrow;

    @EventListener
    public void registerItems(ItemRegistryEvent event) {
        explosiveArrow = new ElementalArrowItem(NAMESPACE.id("explosive_arrow"), ExplosiveArrowEntity.class).setTranslationKey(NAMESPACE.id("explosive_arrow"));
        fireArrow = new ElementalArrowItem(NAMESPACE.id("fire_arrow"), FireArrowEntity.class).setTranslationKey(NAMESPACE.id("fire_arrow"));
        lightingArrow = new ElementalArrowItem(NAMESPACE.id("lighting_arrow"), LightingArrowEntity.class).setTranslationKey(NAMESPACE.id("lighting_arrow"));
        iceArrow = new ElementalArrowItem(NAMESPACE.id("ice_arrow"), IceArrowEntity.class).setTranslationKey(NAMESPACE.id("ice_arrow"));
        torchArrow = new ElementalArrowItem(NAMESPACE.id("torch_arrow"), TorchArrowEntity.class).setTranslationKey(NAMESPACE.id("torch_arrow"));
        eggArrow = new ElementalArrowItem(NAMESPACE.id("egg_arrow"), EggArrowEntity.class).setTranslationKey(NAMESPACE.id("egg_arrow"));
    }

    @EventListener
    public void registerEntities(EntityRegister event) {
        event.register(ExplosiveArrowEntity.class, NAMESPACE.id("explosive_arrow").toString());
        event.register(FireArrowEntity.class, NAMESPACE.id("fire_arrow").toString());
        event.register(LightingArrowEntity.class, NAMESPACE.id("lighting_arrow").toString());
        event.register(IceArrowEntity.class, NAMESPACE.id("ice_arrow").toString());
        event.register(TorchArrowEntity.class, NAMESPACE.id("torch_arrow").toString());
        event.register(EggArrowEntity.class, NAMESPACE.id("egg_arrow").toString());
    }

    @EventListener
    public void registerEntityHandlers(EntityHandlerRegistryEvent event) {
        event.register(NAMESPACE.id("explosive_arrow"), ExplosiveArrowEntity::new);
        event.register(NAMESPACE.id("fire_arrow"), FireArrowEntity::new);
        event.register(NAMESPACE.id("lighting_arrow"), LightingArrowEntity::new);
        event.register(NAMESPACE.id("ice_arrow"), IceArrowEntity::new);
        event.register(NAMESPACE.id("torch_arrow"), TorchArrowEntity::new);
        event.register(NAMESPACE.id("egg_arrow"), EggArrowEntity::new);
    }

    @EventListener
    public void registerEntityRenderer(EntityRendererRegisterEvent event) {
        event.renderers.put(ExplosiveArrowEntity.class, new ElementalArrowEntityRenderer());
        event.renderers.put(FireArrowEntity.class, new ElementalArrowEntityRenderer());
        event.renderers.put(LightingArrowEntity.class, new ElementalArrowEntityRenderer());
        event.renderers.put(IceArrowEntity.class, new ElementalArrowEntityRenderer());
        event.renderers.put(TorchArrowEntity.class, new ElementalArrowEntityRenderer());
        event.renderers.put(EggArrowEntity.class, new ElementalArrowEntityRenderer());
    }

    @EventListener
    public void dispenseArrow(DispenseEvent event) {
        ItemDispenseContext context = event.context;

        if (context.itemStack.getItem() instanceof ElementalArrowItem arrowItem) {
            Entity entity = arrowItem.getArrowEntity(context.dispenser.world);
            BlockPos facingPos = context.getFacingBlockPos();

            context.shootEntity(entity);
            entity.setPosition(facingPos.x + 0.5, facingPos.y + 0.5, facingPos.z + 0.5);
            
            event.setCanceled(true);
        }
    }
}
