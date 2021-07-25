package twilightforest.structures.minotaurmaze;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import twilightforest.TFFeature;
import twilightforest.block.TFBlocks;

import java.util.Random;

public class MazeRoomFountainComponent extends MazeRoomComponent {

	public MazeRoomFountainComponent(StructureManager manager, CompoundTag nbt) {
		super(MinotaurMazePieces.TFMMRF, nbt);
	}

	public MazeRoomFountainComponent(TFFeature feature, int i, Random rand, int x, int y, int z) {
		super(MinotaurMazePieces.TFMMRF, feature, i, rand, x, y, z);
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		super.postProcess(world, manager, generator, rand, sbb, chunkPosIn, blockPos);

		this.generateBox(world, sbb, 5, 1, 5, 10, 1, 10, TFBlocks.maze_stone_decorative.get().defaultBlockState(), AIR, false);
		this.generateBox(world, sbb, 6, 1, 6, 9, 1, 9, Blocks.WATER.defaultBlockState(), AIR, false);

		return true;
	}
}
