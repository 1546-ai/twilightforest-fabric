package twilightforest.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.block.TFBlocks;

public class TrophyTileEntity extends BlockEntity {

	private int animatedTicks;
	private boolean animated;

	public TrophyTileEntity(BlockPos pos, BlockState state) {
		super(TFTileEntities.TROPHY.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, TrophyTileEntity te) {
		if (state.is(TFBlocks.ur_ghast_trophy.get()) || state.is(TFBlocks.ur_ghast_wall_trophy.get())) {
			te.animated = true;
			++te.animatedTicks;
		}
	}

	@OnlyIn(Dist.CLIENT)
	public float getAnimationProgress(float time) {
		return this.animated ? this.animatedTicks + time : (float) this.animatedTicks;
	}
}
