package twilightforest.world.components.feature.templates;

import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFEntities;
import twilightforest.loot.TFTreasure;

import java.util.Random;

public class DruidHutFeature extends TemplateFeature<NoneFeatureConfiguration> {
	public DruidHutFeature(Codec<NoneFeatureConfiguration> config) {
		super(config);
	}

	@Override
    protected StructureTemplate getTemplate(StructureManager templateManager, Random random) {
	    return templateManager.getOrCreate(Util.getRandom(DruidHutFeature.HutType.values(), random).RL);
    }

    @Override
    protected StructureProcessor getProcessor(Random random) {
        return new HutTemplateProcessor(0.0F, random.nextInt(), random.nextInt(), random.nextInt());
    }

    @Override
    protected void postProcess(WorldGenLevel world, Random random, StructureManager templateManager, Rotation rotation, Mirror mirror, StructurePlaceSettings placementSettings, BlockPos placementPos) {
        if (random.nextBoolean()) {
            StructureTemplate template = templateManager.getOrCreate(DruidHutFeature.BasementType.values()[random.nextInt(DruidHutFeature.BasementType.size)].getBasement(random.nextBoolean()));

            if(template == null)
                return;

            placementPos = placementPos.below(12).relative(rotation.rotate(mirror.mirror(Direction.NORTH)), 1).relative(rotation.rotate(mirror.mirror(Direction.EAST)), 1);

            template.placeInWorld(world, placementPos, placementPos, placementSettings.clearProcessors().addProcessor(new HutTemplateProcessor(0.0F, random.nextInt(14), random.nextInt(14), random.nextInt(14))), random, 20);

            for (StructureTemplate.StructureBlockInfo info : template.filterBlocks(placementPos, placementSettings, Blocks.STRUCTURE_BLOCK)) {
                this.processData(info, world, rotation, mirror);
            }
        }
    }

	@Override
	protected void processData(StructureTemplate.StructureBlockInfo info, WorldGenLevel world, Rotation rotation, Mirror mirror) {
        if (info.nbt != null && StructureMode.valueOf(info.nbt.getString("mode")) == StructureMode.DATA) {
            String s = info.nbt.getString("metadata");
            BlockPos blockPos = info.pos;
            /**
             `spawner` will place a Druid spawner.

             `loot` will place a chest facing the was-North.

             `lootT` will place a trapped chest facing the was-North.

             `lootW` will place a chest facing the was-West.
             `lootS` will place a chest facing the was-South.

             `lootET` will place a trapped chest facing the was-East.
             `lootNT` will place a trapped chest facing the was-North.
             */
            // removeBlock calls are required due to WorldGenRegion jank with cached TEs, this ensures the correct TE is used
            if ("spawner".equals(s)) {
                if (world.removeBlock(blockPos, false) && world.setBlock(blockPos, Blocks.SPAWNER.defaultBlockState(), 16 | 2)) {
                    BlockEntity tile = world.getBlockEntity(blockPos);

                    if (tile instanceof SpawnerBlockEntity ms) {
                        ms.getSpawner().setEntityId(TFEntities.skeleton_druid);
                    }
                }
            } else if (s.startsWith("loot")) {
                world.removeBlock(blockPos, false);
                BlockState chest = s.endsWith("T") ? Blocks.TRAPPED_CHEST.defaultBlockState() : Blocks.CHEST.defaultBlockState();

                switch (s.substring(5, 6)) {
                    case "L":
                        chest = chest.setValue(ChestBlock.TYPE, mirror != Mirror.NONE ? ChestType.RIGHT : ChestType.LEFT);
                        break;
                    case "R":
                        chest = chest.setValue(ChestBlock.TYPE, mirror != Mirror.NONE ? ChestType.LEFT : ChestType.RIGHT);
                        break;
                    default:
                        chest = chest.setValue(ChestBlock.TYPE, ChestType.SINGLE);
                        break;
                }

                switch (s.substring(4, 5)) {
                    case "W":
                        chest = chest.setValue(HorizontalDirectionalBlock.FACING, rotation.rotate(mirror.mirror(Direction.WEST)));
                        break;
                    case "E":
                        chest = chest.setValue(HorizontalDirectionalBlock.FACING, rotation.rotate(mirror.mirror(Direction.EAST)));
                        break;
                    case "S":
                        chest = chest.setValue(HorizontalDirectionalBlock.FACING, rotation.rotate(mirror.mirror(Direction.SOUTH)));
                        break;
                    default:
                        chest = chest.setValue(HorizontalDirectionalBlock.FACING, rotation.rotate(mirror.mirror(Direction.NORTH)));
                        break;
                }

                if (world.setBlock(blockPos, chest, 16 | 2)) {
                    TFTreasure.basement.generateChestContents(world, blockPos);
                }
            }
        }
    }

    private enum HutType {
        REGULAR    (TwilightForestMod.prefix("landscape/druid_hut/druid_hut"       )),
        SIDEWAYS   (TwilightForestMod.prefix("landscape/druid_hut/druid_sideways"  )),
        DOUBLE_DECK(TwilightForestMod.prefix("landscape/druid_hut/druid_doubledeck"));

        private final ResourceLocation RL;

        HutType(ResourceLocation rl) {
            this.RL = rl;
            increment();
        }

        private static int size;

        private static void increment() {
            ++size;
        }
    }

    private enum BasementType {
        STUDY  (TwilightForestMod.prefix("landscape/druid_hut/basement_study"  ), TwilightForestMod.prefix("landscape/druid_hut/basement_study_trap"  )),
        SHELVES(TwilightForestMod.prefix("landscape/druid_hut/basement_shelves"), TwilightForestMod.prefix("landscape/druid_hut/basement_shelves_trap")),
        GALLERY(TwilightForestMod.prefix("landscape/druid_hut/basement_gallery"), TwilightForestMod.prefix("landscape/druid_hut/basement_gallery_trap"));

        private final ResourceLocation RL;
        private final ResourceLocation RL_TRAP;

        BasementType(ResourceLocation rl, ResourceLocation rlTrap) {
            this.RL = rl;
            this.RL_TRAP = rlTrap;
            increment();
        }

        private static int size;

        private static void increment() {
            ++size;
        }

        private ResourceLocation getBasement(boolean trapped) {
            return trapped ? RL_TRAP : RL;
        }
    }
}
