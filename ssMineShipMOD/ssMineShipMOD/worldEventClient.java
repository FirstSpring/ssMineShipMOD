package net.minecraft.ssMineShipMOD;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class worldEventClient {
	@ForgeSubscribe
	public void worldload(WorldEvent.Load l)
	{
		if(l.world != null&&l.world instanceof WorldClient&&!(l.world instanceof mineship_worldClient))
		{
			ssMineShipMOD.インスタンス.偽クライアントワールド = new mineship_worldClient((WorldClient)l.world);
		}
	}
}
