package twilightforest.item;

import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import twilightforest.entity.projectile.SeekerArrow;
import twilightforest.api.extensions.IBowItemEx;

public class SeekerBowItem extends BowItem implements IBowItemEx {

	public SeekerBowItem(Properties props) {
		super(props);
	}

	@Override
	public AbstractArrow customArrow(AbstractArrow arrow) {
		return new SeekerArrow(arrow.level, arrow.getOwner());
	}
}
