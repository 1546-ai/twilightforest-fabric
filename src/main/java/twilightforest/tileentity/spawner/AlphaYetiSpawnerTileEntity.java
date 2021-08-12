package twilightforest.tileentity.spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.entity.TFEntities;
import twilightforest.entity.boss.AlphaYetiEntity;
import twilightforest.tileentity.TFTileEntities;

public class AlphaYetiSpawnerTileEntity extends BossSpawnerTileEntity<AlphaYetiEntity> {

	public AlphaYetiSpawnerTileEntity(BlockPos pos, BlockState state) {
		super(TFTileEntities.ALPHA_YETI_SPAWNER.get(), TFEntities.yeti_alpha, pos, state);
	}

	@Override
	public boolean anyPlayerInRange() {
		Player closestPlayer = level.getNearestPlayer(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D, getRange(), false);
		return closestPlayer != null && closestPlayer.getY() > worldPosition.getY() - 4;
	}
}
