package twilightforest.world.components.processors;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import twilightforest.block.TFBlocks;
import twilightforest.util.FeaturePlacers;
import twilightforest.world.registration.TFStructureProcessors;

import javax.annotation.Nullable;
import java.util.Random;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public final class NagastoneVariants extends StructureProcessor {
	public static final NagastoneVariants INSTANCE = new NagastoneVariants();
	public static final Codec<NagastoneVariants> CODEC = Codec.unit(() -> INSTANCE);

	private NagastoneVariants() {
    }

	@Override
	public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldIn, BlockPos pos, BlockPos piecepos, StructureTemplate.StructureBlockInfo oldInfo, StructureTemplate.StructureBlockInfo modifiedBlockInfo, StructurePlaceSettings settings) {
		Random random = settings.getRandom(modifiedBlockInfo.pos);

		// We use nextBoolean in other processors so this lets us re-seed deterministically
		random.setSeed(random.nextLong() * 5);

		BlockState state = modifiedBlockInfo.state;
		Block block = state.getBlock();

		if (block == TFBlocks.ETCHED_NAGASTONE && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(modifiedBlockInfo.pos, FeaturePlacers.transferAllStateKeys(state, random.nextBoolean() ? TFBlocks.MOSSY_ETCHED_NAGASTONE : TFBlocks.CRACKED_ETCHED_NAGASTONE), null);

		if (block == TFBlocks.NAGASTONE_PILLAR && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(modifiedBlockInfo.pos, FeaturePlacers.transferAllStateKeys(state, random.nextBoolean() ? TFBlocks.MOSSY_NAGASTONE_PILLAR : TFBlocks.CRACKED_NAGASTONE_PILLAR), null);

		if (block == TFBlocks.NAGASTONE_STAIRS_LEFT && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(modifiedBlockInfo.pos, FeaturePlacers.transferAllStateKeys(state, random.nextBoolean() ? TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT : TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT), null);

		if (block == TFBlocks.NAGASTONE_STAIRS_RIGHT && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(modifiedBlockInfo.pos, FeaturePlacers.transferAllStateKeys(state, random.nextBoolean() ? TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT : TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT), null);

		return modifiedBlockInfo;
	}

	@Override
	public StructureProcessorType<?> getType() {
		return TFStructureProcessors.NAGASTONE_VARIANTS;
	}
}
