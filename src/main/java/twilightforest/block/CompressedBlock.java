package twilightforest.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import twilightforest.util.TFDamageSources;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class CompressedBlock extends Block {

	public CompressedBlock(Properties props) {
		super(props);
	}

	@Override
	@Deprecated
	public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos) {
		if (this == TFBlocks.fiery_block.get()) {
			return 1.0F;
		}
		return super.getShadeBrightness(state, worldIn, pos);
	}

	@Override
	@Deprecated
	public float getDestroyProgress(BlockState state, Player player, BlockGetter worldIn, BlockPos pos) {
		// ItemShears#getDestroySpeed is really dumb and doesn't check IShearable so we have to do it this way to try to match the wool break speed with shears
		return state.getBlock() == TFBlocks.arctic_fur_block.get() && player.getMainHandItem().getItem() instanceof ShearsItem ? 0.2F : super.getDestroyProgress(state, player, worldIn, pos);
	}

	@Override
	public void stepOn(Level worldIn, BlockPos pos, Entity entityIn) {
		if ((!entityIn.fireImmune())
				&& entityIn instanceof LivingEntity
				&& (!EnchantmentHelper.hasFrostWalker((LivingEntity) entityIn))
				&& this == TFBlocks.fiery_block.get()) {
			entityIn.hurt(TFDamageSources.FIERY, 1.0F);
		}

		super.stepOn(worldIn, pos, entityIn);
	}

	@Override
	public void fallOn(Level worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
		if (this == TFBlocks.steeleaf_block.get()) {
			entityIn.causeFallDamage(fallDistance, 0.75F);
		} else if (this == TFBlocks.arctic_fur_block.get()) {
			entityIn.causeFallDamage(fallDistance, 0.1F);
		}
	}

	@Override
	public boolean isFireSource(BlockState state, LevelReader world, BlockPos pos, Direction side) {
		return this == TFBlocks.fiery_block.get();
	}

	@Override
	public boolean isStickyBlock(BlockState state) {
		return this == TFBlocks.carminite_block.get();
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		if (this == TFBlocks.steeleaf_block.get()) {
			tooltip.add(new TranslatableComponent("block.steeleaf.tooltip"));
		} else if (this == TFBlocks.arctic_fur_block.get()) {
			tooltip.add(new TranslatableComponent("block.arctic.tooltip"));
		}
	}
}
