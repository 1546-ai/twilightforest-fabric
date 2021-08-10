package twilightforest.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import twilightforest.block.TFBlocks;
import twilightforest.util.FeatureUtil;
import twilightforest.util.HugeMushroomUtil;

import java.util.Random;

public class TFGenBigMushgloom extends Feature<NoneFeatureConfiguration> {

	public TFGenBigMushgloom(Codec<NoneFeatureConfiguration> config) {
		super(config);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
		WorldGenLevel world = ctx.level();
		BlockPos pos = ctx.origin();
		Random rand = ctx.random();

		int height = 3 + rand.nextInt(2) + rand.nextInt(2);

		if (!FeatureUtil.isAreaSuitable(world, pos.offset(-1, 0, -1), 3, height, 3)) {
			return false;
		}

		Block blockUnder = world.getBlockState(pos.below()).getBlock();
		if (blockUnder != Blocks.DIRT && blockUnder != Blocks.GRASS_BLOCK && blockUnder != Blocks.MYCELIUM) {
			return false;
		}

		// generate!
		for (int dy = 0; dy < height - 2; dy++) {
			world.setBlock(pos.above(dy), TFBlocks.huge_mushgloom_stem.get().defaultBlockState(), 3);
		}

		makeMushroomCap(world, pos.above(height - 2));
		if (rand.nextBoolean()) {
			makeMushroomCap(world, pos.above(height - 1));
		}

		return true;
	}

	private void makeMushroomCap(LevelAccessor world, BlockPos pos) {
		BlockState defState = TFBlocks.huge_mushgloom.get().defaultBlockState();
		world.setBlock(pos.offset(-1, 0, -1), HugeMushroomUtil.getState(HugeMushroomUtil.HugeMushroomType.NORTH_WEST, defState), 3);
		world.setBlock(pos.offset(0, 0, -1), HugeMushroomUtil.getState(HugeMushroomUtil.HugeMushroomType.NORTH, defState), 3);
		world.setBlock(pos.offset(1, 0, -1), HugeMushroomUtil.getState(HugeMushroomUtil.HugeMushroomType.NORTH_EAST, defState), 3);
		world.setBlock(pos.offset(-1, 0, 0), HugeMushroomUtil.getState(HugeMushroomUtil.HugeMushroomType.WEST, defState), 3);
		world.setBlock(pos, HugeMushroomUtil.getState(HugeMushroomUtil.HugeMushroomType.CENTER, defState), 3);
		world.setBlock(pos.offset(1, 0, 0), HugeMushroomUtil.getState(HugeMushroomUtil.HugeMushroomType.EAST, defState), 3);
		world.setBlock(pos.offset(-1, 0, 1), HugeMushroomUtil.getState(HugeMushroomUtil.HugeMushroomType.SOUTH_WEST, defState), 3);
		world.setBlock(pos.offset(0, 0, 1), HugeMushroomUtil.getState(HugeMushroomUtil.HugeMushroomType.SOUTH, defState), 3);
		world.setBlock(pos.offset(1, 0, 1), HugeMushroomUtil.getState(HugeMushroomUtil.HugeMushroomType.SOUTH_EAST, defState), 3);
	}
}
