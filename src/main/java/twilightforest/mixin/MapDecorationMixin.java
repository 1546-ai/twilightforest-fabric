package twilightforest.mixin;

import org.spongepowered.asm.mixin.Mixin;
import twilightforest.api.extensions.IMapDecorationEx;

import net.minecraft.world.level.saveddata.maps.MapDecoration;

@Mixin(MapDecoration.class)
public class MapDecorationMixin implements IMapDecorationEx {
}
