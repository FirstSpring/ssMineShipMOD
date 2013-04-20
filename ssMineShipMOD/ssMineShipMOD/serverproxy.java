package net.minecraft.ssMineShipMOD;

import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class serverproxy {
	
	public void 登録()
	{
		TickRegistry.registerTickHandler(new servertickhandler(), Side.SERVER);
	}
}
