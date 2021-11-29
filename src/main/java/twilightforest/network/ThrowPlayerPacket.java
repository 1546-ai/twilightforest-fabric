package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import twilightforest.lib.BasePacket;

import java.util.function.Supplier;

public class ThrowPlayerPacket implements BasePacket<ThrowPlayerPacket> {
	private final float motionX;
	private final float motionY;
	private final float motionZ;

	public ThrowPlayerPacket(float motionX, float motionY, float motionZ) {
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
	}

	public ThrowPlayerPacket(FriendlyByteBuf buf) {
		motionX = buf.readFloat();
		motionY = buf.readFloat();
		motionZ = buf.readFloat();
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeFloat(motionX);
		buf.writeFloat(motionY);
		buf.writeFloat(motionZ);
	}

	@Override
	public void handle(ThrowPlayerPacket packet, Context context) {
		Handler.onMessage(packet, context);
	}

	public static class Handler {

		public static boolean onMessage(ThrowPlayerPacket message, Supplier<BasePacket.Context> ctx) {
			ctx.get().enqueueWork(new Runnable() {
				@Override
				public void run() {
					Minecraft.getInstance().player.push(message.motionX, message.motionY, message.motionZ);
				}
			});

			return true;
		}
	}
}
