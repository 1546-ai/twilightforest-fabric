package twilightforest.tileentity.spawner;

import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.ServerLevelAccessor;
import twilightforest.entity.TFEntities;
import twilightforest.entity.boss.KnightPhantomEntity;
import twilightforest.item.TFItems;
import twilightforest.tileentity.TFTileEntities;

public class KnightPhantomSpawnerTileEntity extends BossSpawnerTileEntity<KnightPhantomEntity> {

	private static final int COUNT = 6;

	private int spawned = 0;

	public KnightPhantomSpawnerTileEntity() {
		super(TFTileEntities.KNIGHT_PHANTOM_SPAWNER.get(), TFEntities.knight_phantom);
	}

	@Override
	public boolean anyPlayerInRange() {
		Player closestPlayer = level.getNearestPlayer(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D, getRange(), false);
		return closestPlayer != null && closestPlayer.getY() > worldPosition.getY() - 2;
	}

	@Override
	protected boolean spawnMyBoss(ServerLevelAccessor world) {
		for (int i = spawned; i < COUNT; i++) {
			// create creature
			KnightPhantomEntity myCreature = makeMyCreature();

			float angle = (360F / COUNT) * i;
			final float distance = 4F;

			double rx = worldPosition.getX() + 0.5D + Math.cos(angle * Math.PI / 180.0D) * distance;
			double ry = worldPosition.getY();
			double rz = worldPosition.getZ() + 0.5D + Math.sin(angle * Math.PI / 180.0D) * distance;

			myCreature.moveTo(rx, ry, rz, world.getLevel().random.nextFloat() * 360F, 0.0F);
			myCreature.finalizeSpawn(world, world.getCurrentDifficultyAt(new BlockPos(myCreature.blockPosition())), MobSpawnType.SPAWNER, null, null);

			if(i == 5 && world.getDifficulty() == Difficulty.HARD){
				myCreature.setItemSlot(EquipmentSlot.OFFHAND,new ItemStack(TFItems.knightmetal_shield.get()));
			}

			// set creature's home to this
			initializeCreature(myCreature);

			myCreature.setNumber(i);

			// spawn it
			if (world.addFreshEntity(myCreature)) {
				spawned++;
			}
		}
		return spawned == COUNT;
	}
}
