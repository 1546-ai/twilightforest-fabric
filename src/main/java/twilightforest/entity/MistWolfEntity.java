package twilightforest.entity;

import javax.annotation.Nullable;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import twilightforest.TFSounds;

public class MistWolfEntity extends HostileWolfEntity {

	public MistWolfEntity(EntityType<? extends MistWolfEntity> type, Level world) {
		super(type, world);
		setCollarColor(DyeColor.GRAY);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return HostileWolfEntity.registerAttributes()
				.add(Attributes.MAX_HEALTH, 30.0D)
				.add(Attributes.ATTACK_DAMAGE, 6);
	}

	@Override
	public boolean doHurtTarget(Entity entity) {
		if (super.doHurtTarget(entity)) {
			float myBrightness = this.getBrightness();

			if (entity instanceof LivingEntity && myBrightness < 0.10F) {
				int effectDuration;
				switch (level.getDifficulty()) {
					case EASY:
						effectDuration = 0;
						break;
					default:
					case NORMAL:
						effectDuration = 7;
						break;
					case HARD:
						effectDuration = 15;
						break;
				}

				if (effectDuration > 0) {
					((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.BLINDNESS, effectDuration * 20, 0));
				}
			}

			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void setTarget(@Nullable LivingEntity entity) {
		if (entity != null && entity != getTarget())
			playSound(TFSounds.MISTWOLF_TARGET, 4F, getVoicePitch());
		super.setTarget(entity);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.MISTWOLF_IDLE;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return TFSounds.MISTWOLF_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
	      return TFSounds.MISTWOLF_DEATH;
	}

	@Override
	public float getVoicePitch() {
		return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.6F;
	}
}
