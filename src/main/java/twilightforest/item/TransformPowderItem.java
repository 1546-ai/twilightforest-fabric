package twilightforest.item;

import net.minecraft.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFEntities;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.world.item.Item.Properties;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;

public class TransformPowderItem extends Item {

	private final Map<EntityType<?>, EntityType<?>> transformMap = new HashMap<>();

	protected TransformPowderItem(Properties props) {
		super(props);
	}

	public void initTransformations() {
		addTwoWayTransformation(TFEntities.minotaur,       EntityType.ZOMBIFIED_PIGLIN);
		addTwoWayTransformation(TFEntities.deer,           EntityType.COW);
		addTwoWayTransformation(TFEntities.bighorn_sheep,  EntityType.SHEEP);
		addTwoWayTransformation(TFEntities.wild_boar,      EntityType.PIG);
		addTwoWayTransformation(TFEntities.bunny,          EntityType.RABBIT);
		addTwoWayTransformation(TFEntities.tiny_bird,      EntityType.PARROT);
		addTwoWayTransformation(TFEntities.raven,          EntityType.BAT);
		addTwoWayTransformation(TFEntities.hostile_wolf,   EntityType.WOLF);
		addTwoWayTransformation(TFEntities.penguin,        EntityType.CHICKEN);
		addTwoWayTransformation(TFEntities.hedge_spider,   EntityType.SPIDER);
		addTwoWayTransformation(TFEntities.swarm_spider,   EntityType.CAVE_SPIDER);
		addTwoWayTransformation(TFEntities.wraith,         EntityType.BLAZE);
		addTwoWayTransformation(TFEntities.redcap,         EntityType.VILLAGER);
		addTwoWayTransformation(TFEntities.skeleton_druid, EntityType.WITCH);
	}

	private void addTwoWayTransformation(EntityType<?> from, EntityType<?> to) {
		transformMap.put(from, to);
		transformMap.put(to, from);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
		if (!target.isAlive()) {
			return InteractionResult.PASS;
		}

		EntityType<?> type = transformMap.get(target.getType());
		if (type == null) {
			return InteractionResult.PASS;
		}

		Entity newEntity = type.create(player.level);
		if (newEntity == null) {
			return InteractionResult.PASS;
		}

		newEntity.moveTo(target.getX(), target.getY(), target.getZ(), target.yRot, target.xRot);
		if (newEntity instanceof Mob && target.level instanceof ServerLevelAccessor) {
			ServerLevelAccessor world = (ServerLevelAccessor) target.level;
			((Mob) newEntity).finalizeSpawn(world, target.level.getCurrentDifficultyAt(target.blockPosition()), MobSpawnType.CONVERSION, null, null);
		}

		try { // try copying what can be copied
			UUID uuid = newEntity.getUUID();
			newEntity.load(target.saveWithoutId(newEntity.saveWithoutId(new CompoundTag())));
			newEntity.setUUID(uuid);
		} catch (Exception e) {
			TwilightForestMod.LOGGER.warn("Couldn't transform entity NBT data", e);
		}

		target.level.addFreshEntity(newEntity);
		target.remove();
		stack.shrink(1);

		if (target instanceof Mob) {
			((Mob) target).spawnAnim();
			((Mob) target).spawnAnim();
		}
		target.playSound(TFSounds.POWDER_USE, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F);

		return InteractionResult.SUCCESS;
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
		if (world.isClientSide) {
			AABB area = getEffectAABB(player);

			// particle effect
			for (int i = 0; i < 30; i++) {
				world.addParticle(ParticleTypes.CRIT, area.minX + world.random.nextFloat() * (area.maxX - area.minX),
						area.minY + world.random.nextFloat() * (area.maxY - area.minY),
						area.minZ + world.random.nextFloat() * (area.maxZ - area.minZ),
						0, 0, 0);
			}

		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
	}

	private AABB getEffectAABB(Player player) {
		double range = 2.0D;
		double radius = 1.0D;
		Vec3 srcVec = new Vec3(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
		Vec3 lookVec = player.getLookAngle();
		Vec3 destVec = srcVec.add(lookVec.x * range, lookVec.y * range, lookVec.z * range);

		return new AABB(destVec.x - radius, destVec.y - radius, destVec.z - radius, destVec.x + radius, destVec.y + radius, destVec.z + radius);
	}
}
