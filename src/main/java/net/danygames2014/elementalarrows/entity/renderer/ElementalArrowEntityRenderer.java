package net.danygames2014.elementalarrows.entity.renderer;

import net.danygames2014.elementalarrows.entity.ElementalArrowEntity;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class ElementalArrowEntityRenderer extends ArrowEntityRenderer {
    BlockRenderManager blockRenderer = new BlockRenderManager();

    @Override
    public void render(Entity entity, double x, double y, double z, float yaw, float pitch) {
        if (entity instanceof ElementalArrowEntity arrowEntity) {
            if (arrowEntity.prevYaw != 0.0F || arrowEntity.prevPitch != 0.0F) {
                if (arrowEntity.getArrowBlock() != null) {
                    GL11.glPushMatrix();
                    GL11.glTranslatef((float) x, (float) y, (float) z);
                    GL11.glRotatef(arrowEntity.prevYaw + (arrowEntity.yaw - arrowEntity.prevYaw) * pitch - 90.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(arrowEntity.prevPitch + (arrowEntity.pitch - arrowEntity.prevPitch) * pitch, 0.0F, 0.0F, 1.0F);
                    this.bindTexture("/terrain.png");
                    float scale = arrowEntity.getArrowBlockScale();
                    GL11.glScalef(scale, scale, scale);
                    GL11.glTranslatef(0.5f, 0.125f, 0.0f);
                    GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
                    blockRenderer.render(arrowEntity.getArrowBlock(), 0, arrowEntity.getArrowBlockBrightnessMultiplier());
                    GL11.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f);
                    GL11.glScalef(1.0f / scale, 1.0f / scale, 1.0f / scale);
                    GL11.glPopMatrix();
                }

                this.bindTexture("/item/arrows.png");
                GL11.glPushMatrix();
                GL11.glTranslatef((float) x, (float) y, (float) z);
                GL11.glRotatef(arrowEntity.prevYaw + (arrowEntity.yaw - arrowEntity.prevYaw) * pitch - 90.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(arrowEntity.prevPitch + (arrowEntity.pitch - arrowEntity.prevPitch) * pitch, 0.0F, 0.0F, 1.0F);
                Tessellator t = Tessellator.INSTANCE;
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                float var21 = (float) arrowEntity.shake - pitch;
                if (var21 > 0.0F) {
                    float var22 = -MathHelper.sin(var21 * 3.0F) * var21;
                    GL11.glRotatef(var22, 0.0F, 0.0F, 1.0F);
                }

                GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
                GL11.glScalef(0.05625f, 0.05625f, 0.05625f);
                GL11.glTranslatef(-4.0F, 0.0F, 0.0F);
                GL11.glNormal3f(0.05625f, 0.0F, 0.0F);
                t.startQuads();
                t.vertex(-7.0F, -2.0F, -2.0F, 0.0f, 0.15625f);
                t.vertex(-7.0F, -2.0F, 2.0F, 0.15625f, 0.15625f);
                t.vertex(-7.0F, 2.0F, 2.0F, 0.15625f, 0.3125f);
                t.vertex(-7.0F, 2.0F, -2.0F, 0.0f, 0.3125f);
                t.draw();
                GL11.glNormal3f(-0.05625f, 0.0F, 0.0F);
                t.startQuads();
                t.vertex(-7.0F, 2.0F, -2.0F, 0.0f, 0.15625f);
                t.vertex(-7.0F, 2.0F, 2.0F, 0.15625f, 0.15625f);
                t.vertex(-7.0F, -2.0F, 2.0F, 0.15625f, 0.3125f);
                t.vertex(-7.0F, -2.0F, -2.0F, 0.0f, 0.3125f);
                t.draw();

                for (int var23 = 0; var23 < 4; ++var23) {
                    GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                    GL11.glNormal3f(0.0F, 0.0F, 0.05625f);
                    t.startQuads();
                    t.vertex(-8.0F, -2.0F, 0.0F, 0.0f, 0.0f);
                    t.vertex(8.0F, -2.0F, 0.0F, 0.5f, 0.0f);
                    t.vertex(8.0F, 2.0F, 0.0F, 0.5f, 0.15625f);
                    t.vertex(-8.0F, 2.0F, 0.0F, 0.0f, 0.15625f);
                    t.draw();
                }

                GL11.glDisable(GL12.GL_RESCALE_NORMAL);
                GL11.glPopMatrix();
            }
        }
    }
}
