package twilightforest.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.EncasedSmokerBlock;
import twilightforest.block.TFBlocks;
import twilightforest.client.particle.TFParticleType;

public class TFSmokerTileEntity extends BlockEntity {

	private long counter = 0;

	public TFSmokerTileEntity(BlockPos pos, BlockState state) {
		super(TFTileEntities.SMOKER.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, TFSmokerTileEntity te) {
		if (level.isClientSide && ++te.counter % 4 == 0) {
			if (state.getBlock() == TFBlocks.encased_smoker.get() && state.getValue(EncasedSmokerBlock.ACTIVE)) {
				particles(level, pos, te);
			} else if (state.getBlock() == TFBlocks.smoker.get()) {
				particles(level, pos, te);
			}
		}
	}

	public static void particles(Level level, BlockPos pos, TFSmokerTileEntity te) {
		level.addParticle(TFParticleType.HUGE_SMOKE.get(), pos.getX() + 0.5, pos.getY() + 0.95, pos.getZ() + 0.5,
				Math.cos(te.counter / 10.0) * 0.05, 0.25D, Math.sin(te.counter / 10.0) * 0.05
		);
	}
}
