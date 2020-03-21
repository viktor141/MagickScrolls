package ru.vixtor141.MagickScrolls.Custom;

import com.google.common.base.Preconditions;
import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityArrow;
import org.apache.commons.lang.Validate;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.AbstractProjectile;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftArrow;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.Arrow.Spigot;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CustomProjectaile extends AbstractProjectile implements Arrow {

    private final Arrow.Spigot spigot = new Arrow.Spigot() {
        public double getDamage() {
            return CustomProjectaile.this.getHandle().k();
        }

        public void setDamage(double damage) {
            CustomProjectaile.this.getHandle().c(damage);
        }
    };

    public CustomProjectaile(CraftServer server, EntityArrow entity) {
        super(server, entity);
    }

    public CustomProjectaile(CraftServer server, Entity entity) {
        super(server, entity);
    }

    public void setKnockbackStrength(int knockbackStrength) {
        Validate.isTrue(knockbackStrength >= 0, "Knockback cannot be negative");
        this.getHandle().setKnockbackStrength(knockbackStrength);
    }

    public int getKnockbackStrength() {
        return this.getHandle().knockbackStrength;
    }

    public boolean isCritical() {
        return this.getHandle().isCritical();
    }

    public void setCritical(boolean critical) {
        this.getHandle().setCritical(critical);
    }

    public ProjectileSource getShooter() {
        return this.getHandle().projectileSource;
    }

    public void setShooter(ProjectileSource shooter) {
        if (shooter instanceof LivingEntity) {
            this.getHandle().shooter = ((CraftLivingEntity)shooter).getHandle();
        } else {
            this.getHandle().shooter = null;
        }

        this.getHandle().projectileSource = shooter;
    }

    public boolean isInBlock() {
        return this.getHandle().inGround;
    }

    public Block getAttachedBlock() {
        if (!this.isInBlock()) {
            return null;
        } else {
            EntityArrow handle = this.getHandle();
            return this.getWorld().getBlockAt(handle.h, handle.at, handle.au);
        }
    }

    public PickupStatus getPickupStatus() {
        return PickupStatus.values()[this.getHandle().fromPlayer.ordinal()];
    }

    public void setPickupStatus(PickupStatus status) {
        Preconditions.checkNotNull(status, "status");
        this.getHandle().fromPlayer = net.minecraft.server.v1_12_R1.EntityArrow.PickupStatus.a(status.ordinal());
    }

    public EntityArrow getHandle() {
        return (EntityArrow)this.entity;
    }

    public String toString() {
        return "CraftArrow";
    }

    public EntityType getType() {
        return EntityType.ARROW;
    }

    public Arrow.Spigot spigot() {
        return this.spigot;
    }

}
