package twilightforest.world.components.structures.start;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.world.components.structures.TFStructureComponent;
import twilightforest.world.registration.TFStructures;

import java.util.List;

public class TFStructureStart<C extends FeatureConfiguration> extends StructureStart<C> {
	private boolean conquered = false;

	public TFStructureStart(StructureFeature<C> structureFeature, ChunkPos chunkPos, int references, PiecesContainer pieces) {
		super(structureFeature, chunkPos, references, pieces);
	}

	@Override
	public CompoundTag createTag(StructurePieceSerializationContext level, ChunkPos chunkPos) {
		CompoundTag tag = super.createTag(level, chunkPos);
		if (this.isValid())
			tag.putBoolean("conquered", this.conquered);
		return tag;
	}

	public void load(CompoundTag nbt) {
		this.conquered = nbt.getBoolean("conquered");
	}

	public final void setConquered(boolean flag) {
		this.conquered = flag;
	}

	public final boolean isConquered() {
		return this.conquered;
	}

	private static int getSpawnListIndexAt(StructureStart<?> start, BlockPos pos) {
		int highestFoundIndex = -1;
		for (StructurePiece component : start.getPieces()) {
			if (component.getBoundingBox().isInside(pos)) {
				if (component instanceof TFStructureComponent tfComponent) {
					if (tfComponent.spawnListIndex > highestFoundIndex)
						highestFoundIndex = tfComponent.spawnListIndex;
				} else
					return 0;
			}
		}
		return highestFoundIndex;
	}

	public static List<MobSpawnSettings.SpawnerData> gatherPotentialSpawns(StructureFeatureManager structureManager, MobCategory classification, BlockPos pos) {
		for (StructureFeature<?> structure : TFStructures.SEPARATION_SETTINGS.keySet()) {
			StructureStart<?> start = structureManager.getStructureAt(pos, structure);
			if (!start.isValid())
				continue;

			if (!(structure instanceof LegacyStructureFeature legacyData)) continue;

			if (classification != MobCategory.MONSTER)
				return legacyData.feature.getSpawnableList(classification);
			if (start instanceof TFStructureStart<?> s && s.conquered)
				return null;
			final int index = getSpawnListIndexAt(start, pos);
			if (index < 0)
				return null;
			return legacyData.feature.getSpawnableMonsterList(index);
		}
		return null;
	}
}