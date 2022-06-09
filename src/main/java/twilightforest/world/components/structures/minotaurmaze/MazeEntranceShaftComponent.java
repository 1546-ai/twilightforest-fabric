package twilightforest.world.components.structures.minotaurmaze;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.block.TFBlocks;
import twilightforest.world.components.structures.TFStructureComponentOld;
import twilightforest.world.registration.TFFeature;
import twilightforest.world.registration.TFStructurePieceTypes;


public class MazeEntranceShaftComponent extends TFStructureComponentOld {

	public MazeEntranceShaftComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFMMES.get(), nbt);
	}

	private final int averageGroundLevel = Integer.MIN_VALUE;

	public MazeEntranceShaftComponent(TFFeature feature, int i, RandomSource rand, int x, int y, int z) {
		super(TFStructurePieceTypes.TFMMES.get(), feature, i, new BoundingBox(x, y, z, x + 6 - 1, y, z + 6 - 1).encapsulate(new BlockPos(x, 0, z))); // FIXME Swap 0 for Worldgen Sea Level
		this.setOrientation(Direction.Plane.HORIZONTAL.getRandomDirection(rand));
	}

	/**
	 * Initiates construction of the Structure Component picked, at the current Location of StructGen
	 */
	@Override
	public void addChildren(StructurePiece structurecomponent, StructurePieceAccessor list, RandomSource random) {
		// NO-OP
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		BlockPos.MutableBlockPos pos = chunkPosIn.getWorldPosition().mutable().setX(this.boundingBox.minX()).setZ(this.boundingBox.minZ());

		this.boundingBox.encapsulate(pos.setY(generator.getSeaLevel() - 9));

		this.generateBox(world, sbb, 0, 0, 0, 5, this.boundingBox.getYSpan(), 5, TFBlocks.MAZESTONE_BRICK.get().defaultBlockState(), AIR, true);
		this.generateAirBox(world, sbb, 1, 0, 1, 4, this.boundingBox.getYSpan(), 4);
	}
}
