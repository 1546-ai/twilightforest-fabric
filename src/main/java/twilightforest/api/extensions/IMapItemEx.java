package twilightforest.api.extensions;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public interface IMapItemEx {
    MapItemSavedData getCustomMapData(ItemStack stack, Level level);
}
