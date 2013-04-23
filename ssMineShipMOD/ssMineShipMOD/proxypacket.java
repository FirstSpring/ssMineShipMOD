package net.minecraft.ssMineShipMOD;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class proxypacket implements IPacketHandler{
	
	@Override
	public void onPacketData(INetworkManager manager,Packet250CustomPayload packet, Player player){
		if(player instanceof EntityPlayer){
			if(packet.channel.equals("sskeych")){
				EntityPlayer playerd = (EntityPlayer)player;
				ssMineShipMOD.インスタンス.入力状態.put(String.valueOf(playerd.username), packet.data);
			}//鯖
			if(packet.channel.equals("描画要求")){
				if(ssMineShipMOD.インスタンス.サーバー描画用データ.containsKey(イントへ(packet.data)))
				{
					HashSet<serverDataBlock> i = ssMineShipMOD.インスタンス.サーバー描画用データ.get(イントへ(packet.data));

					ssMineShipMOD.インスタンス.描画してる蔵一覧.add((EntityPlayer)player);
					
					Iterator it = i.iterator();
					int c = 0;
					boolean 最初 = true;
					while(it.hasNext())
					{
						serverDataBlock s = (serverDataBlock)it.next();
						if(最初)
						{
							ByteArrayOutputStream bos = new ByteArrayOutputStream(25);
							DataOutputStream dos = new DataOutputStream(bos);
							try {
								dos.writeInt(s.メイン.ブロックの位置X);
								dos.writeInt(s.メイン.ブロックの位置Y);
								dos.writeInt(s.メイン.ブロックの位置Z);
							} catch (IOException e) {
								e.printStackTrace();
							}
							PacketDispatcher.sendPacketToPlayer(new Packet250CustomPayload("描画要求への返事座標",bos.toByteArray()),player);
							最初 = false;
						}
						//どれかの面が見えてたら
						if(s.上見えている||s.下見えている||s.前見えている||s.右見えている||s.左見えている||s.後見えている)
						{
							PacketDispatcher.sendPacketToPlayer(new Packet250CustomPayload("描画要求への返事",s.cb()),player);
							if(s.常に更新と描画するか)
								s.同期(player);
						}
						else c++;
					}
					((EntityPlayer) player).sendChatToPlayer("描画されない"+ c+"総数"+i.size());
					it = i.iterator();
					PacketDispatcher.sendPacketToPlayer(new Packet250CustomPayload("描画要求への返事完了",((serverDataBlock)it.next()).cb()),player);
				}
			}
		}
		//蔵
		ssMineShipMOD.インスタンス.プロキシ.オンパケット(manager, packet, player);
	}

	static int イントへ(byte[] b) {
		return ByteBuffer.wrap(b).asIntBuffer().get();
	}
	
}
