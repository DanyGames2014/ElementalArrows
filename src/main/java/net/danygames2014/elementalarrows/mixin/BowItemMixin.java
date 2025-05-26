package net.danygames2014.elementalarrows.mixin;

import net.danygames2014.elementalarrows.item.ElementalArrowItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BowItem.class)
public class BowItemMixin extends Item {
    public BowItemMixin(int id) {
        super(id);
    }

    @Inject(method = "use", at = @At(value = "HEAD"), cancellable = true)
    public void useCustomArrows(ItemStack stack, World world, PlayerEntity player, CallbackInfoReturnable<ItemStack> cir) {
        for (int i = 0; i < player.inventory.main.length; i++) {
            ItemStack item = player.inventory.main[i];
            if (item != null && item.getItem() instanceof ElementalArrowItem elementalArrowItem) {
                world.playSound(player, "random.bow", 1.0F, 1.0F / (random.nextFloat() * 0.4F + 0.8F));
                
                if (!world.isRemote) {
                    world.spawnEntity(elementalArrowItem.getArrowEntity(world, player));
                }

                player.inventory.removeStack(i, 1);

                cir.setReturnValue(stack);
                return;
                
            } else if (item != null && item.itemId == Item.ARROW.id) {
                world.playSound(player, "random.bow", 1.0F, 1.0F / (random.nextFloat() * 0.4F + 0.8F));

                if (!world.isRemote) {
                    world.spawnEntity(new ArrowEntity(world, player));
                }

                player.inventory.removeStack(i, 1);

                cir.setReturnValue(stack);
                return;
            }
        }
    }
}
