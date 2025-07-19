package net.danygames2014.elementalarrows.config;

import net.glasslauncher.mods.gcapi3.api.ConfigCategory;
import net.glasslauncher.mods.gcapi3.api.ConfigEntry;

public class Config {
    public static class ArrowConfig {
        @ConfigCategory(name = "Egg Arrow")
        public EggArrowConfig eggArrowConfig = new EggArrowConfig();
        
        @ConfigCategory(name = "Explosive Arrow")
        public ExplosiveArrowConfig explosiveArrowConfig = new ExplosiveArrowConfig();
        
        @ConfigCategory(name = "Fire Arrow")
        public FireArrowConfig fireArrowConfig = new FireArrowConfig();
        
        @ConfigCategory(name = "Ice Arrow")
        public IceArrowConfig iceArrowConfig = new IceArrowConfig();
        
        @ConfigCategory(name = "Lightning Arrow")
        public LightningArrowConfig lightningArrowConfig = new LightningArrowConfig();
        
        @ConfigCategory(name = "Torch Arrow")
        public TorchArrowConfig torchArrowConfig = new TorchArrowConfig();
        
        public static class EggArrowConfig {
            @ConfigEntry(name = "Enable Chicken Spawning")
            public Boolean enableChicken = true;
            
            @ConfigEntry(name = "Chicken Spawn Chance (1 in n)", minValue = 0, maxValue = 100)
            public Integer spawnChance = 5;
        }

        public static class ExplosiveArrowConfig {
            @ConfigEntry(name = "Enable Explosion")
            public Boolean enableExplosion = true;

            @ConfigEntry(name = "Explosion Power", minValue = 0.0F, maxValue = 32.0F)
            public Float explosionPower = 3.0F;
        }
        
        public static class FireArrowConfig {
            @ConfigEntry(name = "Enable Setting Blocks on Fire")
            public Boolean enableBlockFire = true;
            
            @ConfigEntry(name = "Enable Setting Entities on Fire")
            public Boolean enableEntityFire = true;
        
            @ConfigEntry(name = "Entity Fire Ticks", minValue = 0, maxValue = 200)
            public Integer fireTicks = 50;
        }
        
        public static class IceArrowConfig {
            @ConfigEntry(name = "Enable Freezing Water")
            public Boolean enableFreezingWater = true;
            
            @ConfigEntry(name = "Enable Solidifying Lava")
            public Boolean enableSolidifyingLava = true;
            
            @ConfigEntry(name = "Enable Extinguishing Fire")
            public Boolean enableExtinguishingFire = true;
            
            @ConfigEntry(name = "Enable Breaking Torches")
            public Boolean enableBreakingTorches = true;
        }
        
        public static class LightningArrowConfig {
            @ConfigEntry(name = "Enable Lightning")
            public Boolean enableLightning = true;
        }
        
        public static class TorchArrowConfig {
            @ConfigEntry(name = "Enable Damaging Entities")
            public Boolean damageEntities = true;
            
            @ConfigEntry(name = "Entity Fire Ticks", minValue = 0, maxValue = 200)
            public Integer fireTicks = 10;
        }
    }
}
