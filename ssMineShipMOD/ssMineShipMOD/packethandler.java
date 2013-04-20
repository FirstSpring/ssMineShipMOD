package net.minecraft.ssMineShipMOD;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class packethandler implements IPacketHandler
{
	@Override
	public void onPacketData(INetworkManager manager,Packet250CustomPayload packet, Player player){
		if(player instanceof EntityPlayer){
			if(packet.channel.equals("sskeych")){
				EntityPlayer playerd = (EntityPlayer)player;
				ssMineShipMOD.インスタンス.入力状態.put(String.valueOf(playerd.username), packet.data);
			}//鯖
			if(packet.channel.equals("描画要求")){
				int f = イントへ(packet.data);
				if(ssMineShipMOD.インスタンス.サーバー描画用データ.containsKey(イントへ(packet.data)))
				{
					HashSet<serverDataBlock> i = ssMineShipMOD.インスタンス.サーバー描画用データ.get(イントへ(packet.data));
					Iterator it = i.iterator();
					int c = 0;
					while(it.hasNext())
					{
						byte[] データ = new byte[25];
						serverDataBlock s = (serverDataBlock)it.next();
						if(s.上見えている||s.下見えている||s.前見えている||s.右見えている||s.左見えている||s.後見えている)
							PacketDispatcher.sendPacketToPlayer(new Packet250CustomPayload("描画要求への返事",s.cb()),player);
						else c++;
					}
					((EntityPlayer) player).sendChatToPlayer("描画されない"+ c+"総数"+i.size());
					it = i.iterator();
					PacketDispatcher.sendPacketToPlayer(new Packet250CustomPayload("描画要求への返事完了",((serverDataBlock)it.next()).cb()),player);
				}
			}//蔵
		}
		if(packet.channel.equals("描画要求への返事")){
			clientBlockData c = new clientBlockData(packet.data);
			if(!ssMineShipMOD.インスタンス.クライアント描画用データ.containsKey(c.メインのエンティティID))
				ssMineShipMOD.インスタンス.クライアント描画用データ.put(c.メインのエンティティID,new HashSet<clientBlockData>());

			ssMineShipMOD.インスタンス.クライアント描画用データ.get(c.メインのエンティティID).add(c);
		}
		if(packet.channel.equals("描画要求への返事完了"))
		{
			clientBlockData c = new clientBlockData(packet.data);
			ssMineShipMOD.インスタンス.構築済み.put(c.メインのエンティティID,0);
		}
	}

	static int イントへ(byte[] b) {
		return ByteBuffer.wrap(b).asIntBuffer().get();
	}
}
