package twilightforest.world.components.structures.courtyard;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.NoiseEffect;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import twilightforest.TFConstants;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.processors.MossyCobbleTemplateProcessor;
import twilightforest.world.components.structures.TwilightDoubleTemplateStructurePiece;

import java.util.Random;

public class CourtyardWallCornerOuter extends TwilightDoubleTemplateStructurePiece {
    public CourtyardWallCornerOuter(ServerLevel level, CompoundTag nbt) {
        super(NagaCourtyardPieces.TFNCWC,
                nbt,
                level,
                readSettings(nbt).addProcessor(CourtyardMain.WALL_PROCESSOR).addProcessor(CourtyardMain.WALL_INTEGRITY_PROCESSOR).addProcessor(BlockIgnoreProcessor.AIR),
                readSettings(nbt).addProcessor(MossyCobbleTemplateProcessor.INSTANCE).addProcessor(CourtyardMain.WALL_DECAY_PROCESSOR)
        );
    }

    public CourtyardWallCornerOuter(int i, int x, int y, int z, Rotation rotation, StructureManager structureManager) {
        super(NagaCourtyardPieces.TFNCWC,
                i,
                structureManager,
                TwilightForestMod.prefix("courtyard/courtyard_wall_corner"),
                makeSettings(rotation).addProcessor(CourtyardMain.WALL_PROCESSOR).addProcessor(CourtyardMain.WALL_INTEGRITY_PROCESSOR).addProcessor(BlockIgnoreProcessor.AIR),
                TwilightForestMod.prefix("courtyard/courtyard_wall_corner_decayed"),
                makeSettings(rotation).addProcessor(MossyCobbleTemplateProcessor.INSTANCE).addProcessor(CourtyardMain.WALL_DECAY_PROCESSOR),
                new BlockPos(x, y, z)
        );
    }

    @Override
    protected void handleDataMarker(String label, BlockPos pos, ServerLevelAccessor levelAccessor, Random random, BoundingBox boundingBox) {

    }

    @Override
    public NoiseEffect getNoiseEffect() {
        return NoiseEffect.BEARD;
    }
}
