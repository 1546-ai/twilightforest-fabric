package twilightforest.item;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import org.apache.commons.lang3.tuple.Pair;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.extensions.IItem;
import twilightforest.util.WorldUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class CrumbleHornItem extends Item implements IItem {

	private static final int CHANCE_HARVEST = 20;
	private static final int CHANCE_CRUMBLE = 5;

	private final List<Pair<Predicate<BlockState>, UnaryOperator<BlockState>>> crumbleTransforms = new ArrayList<>();
	private final List<Predicate<BlockState>> harvestedStates = new ArrayList<>();

	CrumbleHornItem(Properties props) {
		super(props);
		this.addCrumbleTransforms();
	}

	private void addCrumbleTransforms() {
		addCrumble(() -> Blocks.STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS::defaultBlockState);
		addCrumble(() -> Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS::defaultBlockState);
		addCrumble(() -> Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, Blocks.BLACKSTONE::defaultBlockState);
		addCrumble(() -> Blocks.NETHER_BRICKS, Blocks.CRACKED_NETHER_BRICKS::defaultBlockState);
		addCrumble(() -> TFBlocks.maze_stone_brick, () -> TFBlocks.maze_stone_cracked.defaultBlockState());
		addCrumble(() -> TFBlocks.underbrick, () -> TFBlocks.underbrick_cracked.defaultBlockState());
		addCrumble(() -> TFBlocks.tower_wood, () -> TFBlocks.tower_wood_cracked.defaultBlockState());
		addCrumble(() -> TFBlocks.deadrock, () -> TFBlocks.deadrock_cracked.defaultBlockState());
		addCrumble(() -> TFBlocks.castle_brick, () -> TFBlocks.castle_brick_cracked.defaultBlockState());
		addCrumble(() -> TFBlocks.nagastone_pillar, () -> TFBlocks.nagastone_pillar_weathered.defaultBlockState());
		addCrumble(() -> TFBlocks.etched_nagastone, () -> TFBlocks.etched_nagastone_weathered.defaultBlockState());
		addCrumble(() -> Blocks.STONE, Blocks.COBBLESTONE::defaultBlockState);
		addCrumble(() -> Blocks.COBBLESTONE, Blocks.GRAVEL::defaultBlockState);
		addCrumble(() -> Blocks.SANDSTONE, Blocks.SAND::defaultBlockState);
		addCrumble(() -> Blocks.RED_SANDSTONE, Blocks.RED_SAND::defaultBlockState);
		addCrumble(() -> Blocks.GRASS_BLOCK, Blocks.DIRT::defaultBlockState);
		addCrumble(() -> Blocks.MYCELIUM, Blocks.DIRT::defaultBlockState);
		addCrumble(() -> Blocks.PODZOL, Blocks.DIRT::defaultBlockState);
		addCrumble(() -> Blocks.COARSE_DIRT, Blocks.DIRT::defaultBlockState);
		addCrumble(() -> Blocks.CRIMSON_NYLIUM, Blocks.NETHERRACK::defaultBlockState);
		addCrumble(() -> Blocks.WARPED_NYLIUM, Blocks.NETHERRACK::defaultBlockState);
		addCrumble(() -> Blocks.QUARTZ_BLOCK, Blocks.SAND::defaultBlockState);
		addHarvest(() -> Blocks.GRAVEL);
		addHarvest(() -> Blocks.DIRT);
		addHarvest(() -> Blocks.SAND);
		addHarvest(() -> Blocks.RED_SAND);
		addHarvest(() -> Blocks.CLAY);
		addHarvest(() -> Blocks.ANDESITE);
		addHarvest(() -> Blocks.GRANITE);
		addHarvest(() -> Blocks.DIORITE);

	}

	private void addCrumble(Supplier<Block> block, Supplier<BlockState> result) {
		addCrumble(state -> state.getBlock() == block, state -> result.get());
	}

	private void addCrumble(Predicate<BlockState> test, UnaryOperator<BlockState> transform) {
		crumbleTransforms.add(Pair.of(test, transform));
	}

	private void addHarvest(Supplier<Block> block) {
		addHarvest(state -> state.getBlock() == block);
	}

	private void addHarvest(Predicate<BlockState> test) {
		harvestedStates.add(test);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		player.startUsingItem(hand);
		player.playSound(TFSounds.QUEST_RAM_AMBIENT, 1.0F, 0.8F);
		return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
	}

	@Override
	public void onUsingTick(ItemStack stack, LivingEntity living, int count) {
		if (count > 10 && count % 5 == 0 && !living.level.isClientSide) {
			int crumbled = doCrumble(stack, living.level, living);

			if (crumbled > 0) {
				stack.hurtAndBreak(crumbled, living, (user) -> user.broadcastBreakEvent(living.getUsedItemHand()));
			}

			living.level.playSound(null, living.getX(), living.getY(), living.getZ(), TFSounds.QUEST_RAM_AMBIENT, living.getSoundSource(), 1.0F, 0.8F);
		}
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	//@Override
	public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
		return oldStack.getItem() == newStack.getItem();
	}

	//@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged || newStack.getItem() != oldStack.getItem();
	}

	private int doCrumble(ItemStack stack, Level world, LivingEntity living) {

		final double range = 3.0D;
		final double radius = 2.0D;

		Vec3 srcVec = new Vec3(living.getX(), living.getY() + living.getEyeHeight(), living.getZ());
		Vec3 lookVec = living.getLookAngle().scale(range);
		Vec3 destVec = srcVec.add(lookVec);

		AABB crumbleBox = new AABB(destVec.x - radius, destVec.y - radius, destVec.z - radius, destVec.x + radius, destVec.y + radius, destVec.z + radius);

		return crumbleBlocksInAABB(stack, world, living, crumbleBox);
	}

	private int crumbleBlocksInAABB(ItemStack stack, Level world, LivingEntity living, AABB box) {
		int crumbled = 0;
		for (BlockPos pos : WorldUtil.getAllInBB(box)) {
			if (crumbleBlock(stack, world, living, pos)) crumbled++;
		}
		return crumbled;
	}

	private boolean crumbleBlock(ItemStack stack, Level world, LivingEntity living, BlockPos pos) {

		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();

		if (state.isAir()) return false;

		if(living instanceof Player) {
			if (!PlayerBlockBreakEvents.BEFORE.invoker().beforeBlockBreak(world, (Player) living, pos,state, null)) return false;
		}

		for (Pair<Predicate<BlockState>, UnaryOperator<BlockState>> transform : crumbleTransforms) {
			if (transform.getLeft().test(state) && world.random.nextInt(CHANCE_CRUMBLE) == 0) {
				world.setBlock(pos, transform.getRight().apply(state), 3);
				world.levelEvent(2001, pos, Block.getId(state));

				postTrigger(living);

				return true;
			}
		}

		for (Predicate<BlockState> predicate : harvestedStates) {
			if (predicate.test(state) && world.random.nextInt(CHANCE_HARVEST) == 0) {
				if (living instanceof Player) {
					if (/*block.canHarvestBlock(state, world, pos, (Player) living)*/true) {
						world.removeBlock(pos, false);
						block.playerDestroy(world, (Player) living, pos, state, world.getBlockEntity(pos), ItemStack.EMPTY);
						world.levelEvent(2001, pos, Block.getId(state));

						postTrigger(living);

						return true;
					}
				} else if (world.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
					world.destroyBlock(pos, true);

					postTrigger(living);

					return true;
				}
			}
		}
		return false;
	}

	private void postTrigger(LivingEntity living) {
		if (living instanceof ServerPlayer) {
			Player player = (Player) living;
			player.awardStat(Stats.ITEM_USED.get(this));

			//fallback if the other part doesnt work since its inconsistent
			PlayerAdvancements advancements = ((ServerPlayer) player).getAdvancements();
			ServerAdvancementManager manager = ((ServerLevel) player.getCommandSenderWorld()).getServer().getAdvancements();
			Advancement advancement = manager.getAdvancement(TwilightForestMod.prefix("alt/treasures/crumble_horn_used"));
			if (advancement != null) {
				advancements.award(advancement, "used");
			}
		}
	}
}
