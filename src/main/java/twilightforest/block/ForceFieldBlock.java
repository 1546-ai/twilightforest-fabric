package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class ForceFieldBlock extends Block implements SimpleWaterloggedBlock {
	private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final BooleanProperty DOWN = PipeBlock.DOWN;
	public static final BooleanProperty UP = PipeBlock.UP;
	public static final BooleanProperty NORTH = PipeBlock.NORTH;
	public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
	public static final BooleanProperty WEST = PipeBlock.WEST;
	public static final BooleanProperty EAST = PipeBlock.EAST;

	protected static final VoxelShape BASE_SHAPE = Block.box(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D);

	protected static final VoxelShape WEST_SHAPE = Block.box(0.0D, 7.0D, 7.0D, 7.0D, 9.0D, 9.0D);
	protected static final VoxelShape EAST_SHAPE = Block.box(9.0D, 7.0D, 7.0D, 16.0D, 9.0D, 9.0D);
	protected static final VoxelShape DOWN_SHAPE = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 7.0D, 9.0D);
	protected static final VoxelShape UP_SHAPE = Block.box(7.0D, 9.0D, 7.0D, 9.0D, 16.0D, 9.0D);
	protected static final VoxelShape NORTH_SHAPE = Block.box(7.0D, 7.0D, 0.0D, 9.0D, 9.0D, 7.0D);
	protected static final VoxelShape SOUTH_SHAPE = Block.box(7.0D, 7.0D, 9.0D, 9.0D, 9.0D, 16.0D);

	protected static final VoxelShape DOWN_WEST_SHAPE = Block.box(0.0D, 0.0D, 7.0D, 7.0D, 7.0D, 9.0D);
	protected static final VoxelShape DOWN_EAST_SHAPE = Block.box(9.0D, 0.0D, 7.0D, 16.0D, 7.0D, 9.0D);
	protected static final VoxelShape DOWN_NORTH_SHAPE = Block.box(7.0D, 0.0D, 0.0D, 9.0D, 7.0D, 7.0D);
	protected static final VoxelShape DOWN_SOUTH_SHAPE = Block.box(7.0D, 0.0D, 9.0D, 9.0D, 7.0D, 16.0D);

	protected static final VoxelShape UP_WEST_SHAPE = Block.box(0.0D, 9.0D, 7.0D, 7.0D, 16.0D, 9.0D);
	protected static final VoxelShape UP_EAST_SHAPE = Block.box(9.0D, 9.0D, 7.0D, 16.0D, 16.0D, 9.0D);
	protected static final VoxelShape UP_NORTH_SHAPE = Block.box(7.0D, 9.0D, 0.0D, 9.0D, 16.0D, 7.0D);
	protected static final VoxelShape UP_SOUTH_SHAPE = Block.box(7.0D, 9.0D, 9.0D, 9.0D, 16.0D, 16.0D);

	protected static final VoxelShape NORTH_WEST_SHAPE = Block.box(0.0D, 7.0D, 0.0D, 7.0D, 9.0D, 7.0D);
	protected static final VoxelShape NORTH_EAST_SHAPE = Block.box(9.0D, 7.0D, 0.0D, 16.0D, 9.0D, 7.0D);
	protected static final VoxelShape SOUTH_WEST_SHAPE = Block.box(0.0D, 7.0D, 9.0D, 7.0D, 9.0D, 16.0D);
	protected static final VoxelShape SOUTH_EAST_SHAPE = Block.box(9.0D, 7.0D, 9.0D, 16.0D, 9.0D, 16.0D);

	ForceFieldBlock(BlockBehaviour.Properties props) {
		super(props);
		this.registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false)
				.setValue(DOWN, false).setValue(UP, false)
				.setValue(NORTH, false).setValue(SOUTH, false)
				.setValue(WEST, false).setValue(EAST, false));
	}

	@Override
	public boolean canEntityDestroy(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
		return false;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
		VoxelShape shape = BASE_SHAPE;

		boolean down = state.getValue(DOWN);
		boolean up = state.getValue(UP);
		boolean north = state.getValue(NORTH);
		boolean south = state.getValue(SOUTH);
		boolean west = state.getValue(WEST);
		boolean east = state.getValue(EAST);

		boolean x = west && east;
		boolean y = down && up;
		boolean z = north && south;

		if (down) {
			shape = Shapes.or(shape, DOWN_SHAPE);
			if (north && !x) shape = Shapes.or(shape, DOWN_NORTH_SHAPE);
			if (south && !x) shape = Shapes.or(shape, DOWN_SOUTH_SHAPE);
			if (west && !z) shape = Shapes.or(shape, DOWN_WEST_SHAPE);
			if (east && !z) shape = Shapes.or(shape, DOWN_EAST_SHAPE);
		}
		if (up) {
			shape = Shapes.or(shape, UP_SHAPE);
			if (north && !x) shape = Shapes.or(shape, UP_NORTH_SHAPE);
			if (south && !x) shape = Shapes.or(shape, UP_SOUTH_SHAPE);
			if (west && !z) shape = Shapes.or(shape, UP_WEST_SHAPE);
			if (east && !z) shape = Shapes.or(shape, UP_EAST_SHAPE);
		}
		if (north) {
			shape = Shapes.or(shape, NORTH_SHAPE);
			if (west && !y) shape = Shapes.or(shape, NORTH_WEST_SHAPE);
			if (east && !y) shape = Shapes.or(shape, NORTH_EAST_SHAPE);
		}
		if (south) {
			shape = Shapes.or(shape, SOUTH_SHAPE);
			if (west && !y) shape = Shapes.or(shape, SOUTH_WEST_SHAPE);
			if (east && !y) shape = Shapes.or(shape, SOUTH_EAST_SHAPE);
		}
		if (west) shape = Shapes.or(shape, WEST_SHAPE);
		if (east) shape = Shapes.or(shape, EAST_SHAPE);

		return shape;
	}

	private static boolean checkProperties(BlockState state, BooleanProperty... booleanProperties) {
		List<BooleanProperty> propertyList = Arrays.asList(booleanProperties);
		return PipeBlock.PROPERTY_BY_DIRECTION.values().stream().allMatch(booleanProperty -> (propertyList.contains(booleanProperty) == state.getValue(booleanProperty)));
	}

	@OnlyIn(Dist.CLIENT)
	public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction direction) {
		if (adjacentBlockState.is(this)) {
			if (adjacentBlockState.getValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction.getOpposite()))) return true;
		}
		return super.skipRendering(state, adjacentBlockState, direction);
	}

	@Override
	public float getShadeBrightness(BlockState state, BlockGetter blockGetter, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter blockGetter, BlockPos pos) {
		return true;
	}

	public boolean canConnectTo(BlockGetter blockGetter, BlockPos pos, Direction direction) {
		BlockState state = blockGetter.getBlockState(pos.relative(direction));
		boolean solidSide = state.isFaceSturdy(blockGetter, pos.relative(direction), direction.getOpposite());
		Block block = state.getBlock();

		int betterConnections = 0;
		if (!state.is(this)) {
			for (Direction face : Direction.values()) {
				if (blockGetter.getBlockState(pos.relative(face)).is(this)) betterConnections++;
			}
			if (betterConnections > 3) return false; //Might need to alter this a bit, without this the forcefields in the final castles like to attach to every block
		}

		return !isExceptionForConnection(state) && solidSide || block instanceof ForceFieldBlock || block instanceof IronBarsBlock || state.is(BlockTags.WALLS);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState()
				.setValue(DOWN, this.canConnectTo(context.getLevel(), context.getClickedPos(), Direction.DOWN))
				.setValue(UP, this.canConnectTo(context.getLevel(), context.getClickedPos(), Direction.UP))
				.setValue(NORTH, this.canConnectTo(context.getLevel(), context.getClickedPos(), Direction.NORTH))
				.setValue(EAST, this.canConnectTo(context.getLevel(), context.getClickedPos(), Direction.EAST))
				.setValue(SOUTH, this.canConnectTo(context.getLevel(), context.getClickedPos(), Direction.SOUTH))
				.setValue(WEST, this.canConnectTo(context.getLevel(), context.getClickedPos(), Direction.WEST));
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState facingState, LevelAccessor levelAccessor, BlockPos pos, BlockPos facingPos) {
		if (state.getValue(WATERLOGGED)) {
			levelAccessor.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
		}

		return state.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction), this.canConnectTo(levelAccessor, pos, direction));
	}


	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(WATERLOGGED, NORTH, EAST, SOUTH, WEST, UP, DOWN);
	}
}
