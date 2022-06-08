package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.TFSounds;

import javax.annotation.Nullable;
import java.util.Random;

public class HostileWolf extends Monster {

	public HostileWolf(EntityType<? extends HostileWolf> type, Level world) {
		super(type, world);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.ATTACK_DAMAGE, 2.0D);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, new LeapGoal(this, 0.4F));
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, true));
		this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this, HostileWolf.class));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public static boolean getCanSpawnHere(EntityType<? extends HostileWolf> entity, ServerLevelAccessor accessor, MobSpawnType type, BlockPos pos, RandomSource random) {
		return accessor.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(accessor, pos, random) && checkMobSpawnRules(entity, accessor, type, pos, random);
	}

	@Override
	public void setTarget(@Nullable LivingEntity entity) {
		if (entity != null && entity != getTarget())
			playSound(getTargetSound(), 4F, getVoicePitch());
		super.setTarget(entity);
	}

	protected SoundEvent getTargetSound() {
		return TFSounds.HOSTILE_WOLF_TARGET.get();
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.HOSTILE_WOLF_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return TFSounds.HOSTILE_WOLF_HURT.get();
	}
	
	@Override
	protected SoundEvent getDeathSound() {
	      return TFSounds.HOSTILE_WOLF_DEATH.get();
	}

	@Override
	protected void playStepSound(BlockPos p_30415_, BlockState p_30416_) {
		this.playSound(SoundEvents.WOLF_STEP, 0.15F, 1.0F);
	}

	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}

	@Override
	protected boolean shouldDespawnInPeaceful() {
		return true;
	}

	public float getTailAngle() {
		if (this.getTarget() != null) {
			return 1.5393804F;
		} else {
			return ((float)Math.PI / 5F);
		}
	}

	//add agressive flags so its face doesnt turn passive when it jumps
	public static class LeapGoal extends LeapAtTargetGoal {

		private final Mob mob;

		public LeapGoal(Mob mob, float jump) {
			super(mob, jump);
			this.mob = mob;
		}

		@Override
		public void start() {
			super.start();
			mob.setAggressive(true);
		}

		@Override
		public void stop() {
			super.stop();
			mob.setAggressive(false);
		}
	}
}
