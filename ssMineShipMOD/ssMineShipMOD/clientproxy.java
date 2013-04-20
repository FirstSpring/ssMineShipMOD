package net.minecraft.ssMineShipMOD;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class clientproxy extends serverproxy{

	public static renderBlockAccess rwa;
	public static RenderBlocks rb;
	public static mineship_worldClient 偽クライアントワールド;
	public static World クライアントワールド;

	clientPackethandler c = new clientPackethandler();

	@Override
	public void 登録()
	{
		MinecraftForge.EVENT_BUS.register(new worldEventClient());

		RenderingRegistry.registerEntityRenderingHandler(EntityMainBlock.class, new RenderEntityMainBlock());
		RenderingRegistry.registerEntityRenderingHandler(EntityBlockCol.class, new RenderBlockEntity());

		TickRegistry.registerTickHandler(new clienttickhandler(), Side.CLIENT);
	}

	@Override
	public void オンパケット(INetworkManager manager,Packet250CustomPayload packet, Player player){
		c.onPacketData(manager, packet, player);
	}
}