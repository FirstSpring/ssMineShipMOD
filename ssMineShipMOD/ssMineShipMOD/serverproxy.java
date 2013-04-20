package net.minecraft.ssMineShipMOD;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class serverproxy {
	public void 登録()
	{
		TickRegistry.registerTickHandler(new servertickhandler(), Side.SERVER);
	}
	public void オンパケット(INetworkManager manager,Packet250CustomPayload packet, Player player){}
}
