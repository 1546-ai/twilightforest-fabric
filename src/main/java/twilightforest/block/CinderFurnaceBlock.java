package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import twilightforest.tileentity.CinderFurnaceTileEntity;
import twilightforest.tileentity.TFTileEntities;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class CinderFurnaceBlock extends BaseEntityBlock {

	public static final BooleanProperty LIT = BooleanProperty.create("lit");
	private static final DirectionProperty FACING = TFHorizontalBlock.FACING;

	CinderFurnaceBlock() {
		super(Properties.of(Material.WOOD).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(7.0F).lightLevel((state) -> 15));
		this.registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, false));
	}

	@Override
	public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
		return state.getValue(LIT) ? 15 : 0;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(LIT, FACING);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CinderFurnaceTileEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, TFTileEntities.CINDER_FURNACE.get(), CinderFurnaceTileEntity::tick);
	}

	@Override
	@Deprecated
	public boolean triggerEvent(BlockState state, Level worldIn, BlockPos pos, int id, int param) {
		super.triggerEvent(state, worldIn, pos, id, param);
		BlockEntity tileentity = worldIn.getBlockEntity(pos);
		return tileentity != null && tileentity.triggerEvent(id, param);
	}

	@Override
	@Deprecated
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		if (!world.isClientSide && world.getBlockEntity(pos) instanceof CinderFurnaceTileEntity) {
			player.openMenu((CinderFurnaceTileEntity) world.getBlockEntity(pos));
		}

		return InteractionResult.PASS;
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (stack.hasCustomHoverName()) {
			((FurnaceBlockEntity) world.getBlockEntity(pos)).setCustomName(stack.getHoverName());
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
		if (state.getValue(LIT)) {
			Blocks.FURNACE.animateTick(state, world, pos, random);
		}
	}

	@Override
	@Deprecated
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity tileentity = worldIn.getBlockEntity(pos);
			if (tileentity instanceof CinderFurnaceTileEntity) {
				Containers.dropContents(worldIn, pos, (CinderFurnaceTileEntity)tileentity);
				worldIn.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		tooltip.add(new TranslatableComponent("twilightforest.misc.nyi"));
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}
}
