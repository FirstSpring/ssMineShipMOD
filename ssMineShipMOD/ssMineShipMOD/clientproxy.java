package net.minecraft.ssMineShipMOD;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class clientproxy extends serverproxy{
	
	static renderBlockAccess rwa;
	static RenderBlocks rb;
	
	@Override
	public void 登録()
	{
		MinecraftForge.EVENT_BUS.register(new worldEventClient());
		
		RenderingRegistry.registerEntityRenderingHandler(EntityMainBlock.class, new RenderEntityMainBlock());
		RenderingRegistry.registerEntityRenderingHandler(EntityBlockCol.class, new RenderBlockEntity());
		
		TickRegistry.registerTickHandler(new clienttickhandler(), Side.CLIENT);
	}
}