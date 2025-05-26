package net.danygames2014.elementalarrows.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.server.entity.EntitySpawnDataProvider;
import net.modificationstation.stationapi.api.server.entity.TrackingParametersProvider;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.TriState;

import java.util.List;

@SuppressWarnings("unchecked")
public abstract class ElementalArrowEntity extends Entity implements EntitySpawnDataProvider, TrackingParametersProvider {
    public int blockX = -1;
    public int blockY = -1;
    public int blockZ = -1;
    public int blockId = 0;
    public int blockMeta = 0;
    public boolean inGround = false;
    public boolean pickupAllowed = false;
    public int shake = 0;
    public LivingEntity owner;
    public int life;
    public int inAirTime = 0;

    public ElementalArrowEntity(World world) {
        super(world);
        this.setBoundingBoxSpacing(0.5F, 0.5F);
        this.renderDistanceMultiplier = 2.0D;
    }

    public ElementalArrowEntity(World world, double x, double y, double z) {
        this(world);
        this.setPosition(x, y, z);
        this.standingEyeHeight = 0.0F;
    }

    public ElementalArrowEntity(World world, LivingEntity owner) {
        super(world);
        this.renderDistanceMultiplier = 2.0D;
        this.owner = owner;
        this.pickupAllowed = owner instanceof PlayerEntity;
        this.setBoundingBoxSpacing(0.5F, 0.5F);
        this.setPositionAndAnglesKeepPrevAngles(owner.x, owner.y + (double) owner.getEyeHeight(), owner.z, owner.yaw, owner.pitch);
        this.x -= MathHelper.cos(this.yaw / 180.0F * (float) Math.PI) * 0.16F;
        this.y -= 0.1F;
        this.z -= MathHelper.sin(this.yaw / 180.0F * (float) Math.PI) * 0.16F;
        this.setPosition(this.x, this.y, this.z);
        this.standingEyeHeight = 0.0F;
        this.velocityX = -MathHelper.sin(this.yaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.pitch / 180.0F * (float) Math.PI);
        this.velocityZ = MathHelper.cos(this.yaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.pitch / 180.0F * (float) Math.PI);
        this.velocityY = -MathHelper.sin(this.pitch / 180.0F * (float) Math.PI);
        this.setVelocity(this.velocityX, this.velocityY, this.velocityZ, 1.5F, 1.0F);
    }

    public void spawnTravelParticles() {

    }

    public Block getArrowBlock() {
        return null;
    }

    public float getArrowBlockScale() {
        return 1.0F;
    }

    public float getArrowBlockBrightnessMultiplier() {
        return 1.0F;
    }

    public abstract boolean hitEntity(Entity entity);

    public abstract boolean hitBlock(int blockX, int blockY, int blockZ, int side, HitResult hitResult);

    public abstract ItemStack getPickupStack();

    public void setVelocity(double x, double y, double z, float speed, float divergence) {
        float var9 = MathHelper.sqrt(x * x + y * y + z * z);
        x /= var9;
        y /= var9;
        z /= var9;
        x += this.random.nextGaussian() * (double) 0.0075F * (double) divergence;
        y += this.random.nextGaussian() * (double) 0.0075F * (double) divergence;
        z += this.random.nextGaussian() * (double) 0.0075F * (double) divergence;
        x *= speed;
        y *= speed;
        z *= speed;
        this.velocityX = x;
        this.velocityY = y;
        this.velocityZ = z;
        float var10 = MathHelper.sqrt(x * x + z * z);
        this.prevYaw = this.yaw = (float) (Math.atan2(x, z) * (double) 180.0F / (double) (float) Math.PI);
        this.prevPitch = this.pitch = (float) (Math.atan2(y, var10) * (double) 180.0F / (double) (float) Math.PI);
        this.life = 0;
    }

    @Environment(EnvType.CLIENT)
    public void setVelocityClient(double x, double y, double z) {
        this.velocityX = x;
        this.velocityY = y;
        this.velocityZ = z;
        if (this.prevPitch == 0.0F && this.prevYaw == 0.0F) {
            float var7 = MathHelper.sqrt(x * x + z * z);
            this.prevYaw = this.yaw = (float) (Math.atan2(x, z) * (double) 180.0F / (double) (float) Math.PI);
            this.prevPitch = this.pitch = (float) (Math.atan2(y, var7) * (double) 180.0F / (double) (float) Math.PI);
            this.prevPitch = this.pitch;
            this.prevYaw = this.yaw;
            this.setPositionAndAnglesKeepPrevAngles(this.x, this.y, this.z, this.yaw, this.pitch);
            this.life = 0;
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.prevPitch == 0.0F && this.prevYaw == 0.0F) {
            float velocity = MathHelper.sqrt(this.velocityX * this.velocityX + this.velocityZ * this.velocityZ);
            this.prevYaw = this.yaw = (float) (Math.atan2(this.velocityX, this.velocityZ) * (double) 180.0F / (double) (float) Math.PI);
            this.prevPitch = this.pitch = (float) (Math.atan2(this.velocityY, velocity) * (double) 180.0F / (double) (float) Math.PI);
        }

        int stuckBlockId = this.world.getBlockId(this.blockX, this.blockY, this.blockZ);
        if (stuckBlockId > 0) {
            Block.BLOCKS[stuckBlockId].updateBoundingBox(this.world, this.blockX, this.blockY, this.blockZ);
            Box stuckBlockCollisionShape = Block.BLOCKS[stuckBlockId].getCollisionShape(this.world, this.blockX, this.blockY, this.blockZ);
            if (stuckBlockCollisionShape != null && stuckBlockCollisionShape.contains(Vec3d.createCached(this.x, this.y, this.z))) {
                this.inGround = true;
            }
        }

        if (this.shake > 0) {
            --this.shake;
        }

        if (this.inGround) {
            stuckBlockId = this.world.getBlockId(this.blockX, this.blockY, this.blockZ);
            int stuckBlockMeta = this.world.getBlockMeta(this.blockX, this.blockY, this.blockZ);
            if (stuckBlockId == this.blockId && stuckBlockMeta == this.blockMeta) {
                ++this.life;
                if (this.life == 1200) {
                    this.markDead();
                }

            } else {
                this.inGround = false;
                this.velocityX *= this.random.nextFloat() * 0.2F;
                this.velocityY *= this.random.nextFloat() * 0.2F;
                this.velocityZ *= this.random.nextFloat() * 0.2F;
                this.life = 0;
                this.inAirTime = 0;
            }
        } else {
            if (!world.isRemote) {
                ++this.inAirTime;
                Vec3d pos = Vec3d.createCached(this.x, this.y, this.z);
                Vec3d nextPos = Vec3d.createCached(this.x + this.velocityX, this.y + this.velocityY, this.z + this.velocityZ);
                HitResult hitResult = this.world.raycast(pos, nextPos, false, true);
                pos = Vec3d.createCached(this.x, this.y, this.z);
                nextPos = Vec3d.createCached(this.x + this.velocityX, this.y + this.velocityY, this.z + this.velocityZ);
                if (hitResult != null) {
                    nextPos = Vec3d.createCached(hitResult.pos.x, hitResult.pos.y, hitResult.pos.z);
                }

                Entity hitEntity = null;
                List<Entity> entitiesInPath = this.world.getEntities(this, this.boundingBox.stretch(this.velocityX, this.velocityY, this.velocityZ).expand(1.0F, 1.0F, 1.0F));
                double closestEntityDistance = 0.0F;

                for (Entity entity : entitiesInPath) {
                    if (entity.isCollidable() && (entity != this.owner || this.inAirTime >= 5)) {
                        float hitboxSize = 0.3F;
                        Box hitbox = entity.boundingBox.expand(hitboxSize, hitboxSize, hitboxSize);
                        HitResult hitboxResult = hitbox.raycast(pos, nextPos);
                        if (hitboxResult != null) {
                            double entityDistance = pos.distanceTo(hitboxResult.pos);
                            if (entityDistance < closestEntityDistance || closestEntityDistance == (double) 0.0F) {
                                hitEntity = entity;
                                closestEntityDistance = entityDistance;
                            }
                        }
                    }
                }

                if (hitEntity != null) {
                    hitResult = new HitResult(hitEntity);
                }

                if (hitResult != null) {
                    if (hitResult.entity != null) {
                        if (hitEntity(hitResult.entity)) {
                            this.world.playSound(this, "random.drr", 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
                            this.markDead();
                        } else {
                            this.velocityX *= -0.1F;
                            this.velocityY *= -0.1F;
                            this.velocityZ *= -0.1F;
                            this.prevYaw += 180.0F;
                            this.inAirTime = 0;
                        }
                    } else {
                        if (!hitBlock(hitResult.blockX, hitResult.blockY, hitResult.blockZ, hitResult.side, hitResult)) {
                            this.blockX = hitResult.blockX;
                            this.blockY = hitResult.blockY;
                            this.blockZ = hitResult.blockZ;
                            this.blockId = this.world.getBlockId(this.blockX, this.blockY, this.blockZ);
                            this.blockMeta = this.world.getBlockMeta(this.blockX, this.blockY, this.blockZ);
                            this.velocityX = (float) (hitResult.pos.x - this.x);
                            this.velocityY = (float) (hitResult.pos.y - this.y);
                            this.velocityZ = (float) (hitResult.pos.z - this.z);
                            float velocity = MathHelper.sqrt(this.velocityX * this.velocityX + this.velocityY * this.velocityY + this.velocityZ * this.velocityZ);
                            this.x -= this.velocityX / (double) velocity * (double) 0.05F;
                            this.y -= this.velocityY / (double) velocity * (double) 0.05F;
                            this.z -= this.velocityZ / (double) velocity * (double) 0.05F;
                            this.world.playSound(this, "random.drr", 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
                            this.inGround = true;
                            this.shake = 7;
                        } else {
                            this.markDead();
                        }
                    }
                }
            }

            this.x += this.velocityX;
            this.y += this.velocityY;
            this.z += this.velocityZ;
            float horizontalVelocity = MathHelper.sqrt(this.velocityX * this.velocityX + this.velocityZ * this.velocityZ);
            this.yaw = (float) (Math.atan2(this.velocityX, this.velocityZ) * (double) 180.0F / (double) (float) Math.PI);

            this.pitch = (float) (Math.atan2(this.velocityY, horizontalVelocity) * (double) 180.0F / (double) (float) Math.PI);
            while (this.pitch - this.prevPitch < -180.0F) {
                this.prevPitch -= 360.0F;
            }

            while (this.pitch - this.prevPitch >= 180.0F) {
                this.prevPitch += 360.0F;
            }

            while (this.yaw - this.prevYaw < -180.0F) {
                this.prevYaw -= 360.0F;
            }

            while (this.yaw - this.prevYaw >= 180.0F) {
                this.prevYaw += 360.0F;
            }

            this.pitch = this.prevPitch + (this.pitch - this.prevPitch) * 0.2F;
            this.yaw = this.prevYaw + (this.yaw - this.prevYaw) * 0.2F;
            float var24 = 0.99F;
            float var25 = 0.03F;
            if (this.isSubmergedInWater()) {
                for (int var26 = 0; var26 < 4; ++var26) {
                    float var27 = 0.25F;
                    this.world.addParticle("bubble", this.x - this.velocityX * (double) var27, this.y - this.velocityY * (double) var27, this.z - this.velocityZ * (double) var27, this.velocityX, this.velocityY, this.velocityZ);
                }

                var24 = 0.8F;
            }

            this.velocityX *= var24;
            this.velocityY *= var24;
            this.velocityZ *= var24;
            this.velocityY -= var25;
            this.setPosition(this.x, this.y, this.z);

            spawnTravelParticles();
        }
    }

    public void onPlayerInteraction(PlayerEntity player) {
        if (!this.world.isRemote) {
            if (this.inGround && this.pickupAllowed && this.shake <= 0) {
                ItemStack pickupStack = getPickupStack();
                if (pickupStack != null && player.inventory.addStack(getPickupStack())) {
                    this.world.playSound(this, "random.pop", 0.2F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    player.sendPickup(this, 1);
                }
                this.markDead();
            }

        }
    }

    @Override
    public float getShadowRadius() {
        return 0.0F;
    }

    @Override
    public void initDataTracker() {

    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.blockX = nbt.getShort("xTile");
        this.blockY = nbt.getShort("yTile");
        this.blockZ = nbt.getShort("zTile");
        this.blockId = nbt.getByte("inTile") & 255;
        this.blockMeta = nbt.getByte("inData") & 255;
        this.shake = nbt.getByte("shake") & 255;
        this.inGround = nbt.getByte("inGround") == 1;
        this.pickupAllowed = nbt.getBoolean("player");
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putShort("xTile", (short) this.blockX);
        nbt.putShort("yTile", (short) this.blockY);
        nbt.putShort("zTile", (short) this.blockZ);
        nbt.putByte("inTile", (byte) this.blockId);
        nbt.putByte("inData", (byte) this.blockMeta);
        nbt.putByte("shake", (byte) this.shake);
        nbt.putByte("inGround", (byte) (this.inGround ? 1 : 0));
        nbt.putBoolean("player", this.pickupAllowed);
    }

    @Override
    public boolean syncTrackerAtSpawn() {
        return true;
    }

    @Override
    public int getTrackingDistance() {
        return 64;
    }

    @Override
    public int getUpdatePeriod() {
        return 1;
    }

    @Override
    public TriState sendVelocity() {
        return TriState.TRUE;
    }

    @Override
    public abstract Identifier getHandlerIdentifier();
}
