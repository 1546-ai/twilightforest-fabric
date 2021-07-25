package twilightforest.entity.ai;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.EntitySelector;
import twilightforest.entity.LowerGoblinKnightEntity;
import twilightforest.entity.UpperGoblinKnightEntity;

import java.util.EnumSet;

import net.minecraft.world.entity.ai.goal.Goal.Flag;

public class RiderSpearAttackGoal extends Goal {

	private LowerGoblinKnightEntity entity;

	public RiderSpearAttackGoal(LowerGoblinKnightEntity lowerKnight) {
		this.entity = lowerKnight;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK)); // Prevent moving
	}

	@Override
	public boolean canUse() {
		if (!this.entity.getPassengers().isEmpty() && this.entity.getPassengers().get(0) instanceof UpperGoblinKnightEntity && EntitySelector.ATTACK_ALLOWED.test(entity.getTarget())) {
			int timer = ((UpperGoblinKnightEntity) this.entity.getPassengers().get(0)).heavySpearTimer;
			return timer > 0 && timer < UpperGoblinKnightEntity.HEAVY_SPEAR_TIMER_START;
		} else {
			return false;
		}
	}
}
