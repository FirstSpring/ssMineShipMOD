package net.minecraft.ssMineShipMOD;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntitySkull;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class clientPackethandler implements IPacketHandler
{
	private int 一時的に座標を保存X;
	private int 一時的に座標を保存Y;
	private int 一時的に座標を保存Z;
	Minecraft mc = FMLClientHandler.instance().getClient();
	
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		if(packet.channel.equals("描画要求への返事座標")){
			ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
			this.一時的に座標を保存X = dat.readInt();
			this.一時的に座標を保存Y = dat.readInt();
			this.一時的に座標を保存Z = dat.readInt();
		}
		if(packet.channel.equals("描画要求への返事")){
			clientBlockData c = new clientBlockData(packet.data,一時的に座標を保存X,一時的に座標を保存Y,一時的に座標を保存Z);
			if(!ssMineShipMOD.インスタンス.クライアント描画用データ.containsKey(c.メインのエンティティID))
				ssMineShipMOD.インスタンス.クライアント描画用データ.put(c.メインのエンティティID,new HashSet<clientBlockData>());
 
			ssMineShipMOD.インスタンス.クライアント描画用データ.get(c.メインのエンティティID).add(c);
		}
		if(packet.channel.equals("描画要求への返事完了"))
		{
			clientBlockData c = new clientBlockData(packet.data,一時的に座標を保存X,一時的に座標を保存Y,一時的に座標を保存Z);
			ssMineShipMOD.インスタンス.構築済み.put(c.メインのエンティティID,0);
		}
		if(packet.channel.equals("データ同期"))
		{
			InputStream is = new ByteArrayInputStream(packet.data);
			DataInputStream dis = new DataInputStream(is);
			clientproxy.クライアントワールド = mc.theWorld;
			mc.thePlayer.worldObj = clientproxy.偽クライアントワールド;
			mc.theWorld = clientproxy.偽クライアントワールド;
			Packet132TileEntityData p = new Packet132TileEntityData();
			try {
				p.readPacketData(dis);
			} catch (IOException e) {
				e.printStackTrace();
			}
			handleTileEntityData(p);
			mc.thePlayer.worldObj = clientproxy.クライアントワールド;
			mc.theWorld = (WorldClient) clientproxy.クライアントワールド;
		}
		if(packet.channel.equals("データ同期250"))
		{
			InputStream is = new ByteArrayInputStream(packet.data);
			DataInputStream dis = new DataInputStream(is);
			clientproxy.クライアントワールド = mc.theWorld;
			mc.thePlayer.worldObj = clientproxy.偽クライアントワールド;
			mc.theWorld = clientproxy.偽クライアントワールド;
			Packet250CustomPayload p = new Packet250CustomPayload();
			try {
				p.readPacketData(dis);
			} catch (IOException e) {
				e.printStackTrace();
			}
			mc.thePlayer.sendQueue.handleCustomPayload(p);
			mc.thePlayer.worldObj = clientproxy.クライアントワールド;
			mc.theWorld = (WorldClient) clientproxy.クライアントワールド;
		}
	}

	public void handleTileEntityData(Packet132TileEntityData par1Packet132TileEntityData)
    {
        if (this.mc.theWorld.blockExists(par1Packet132TileEntityData.xPosition, par1Packet132TileEntityData.yPosition, par1Packet132TileEntityData.zPosition))
        {
            TileEntity tileentity = this.mc.theWorld.getBlockTileEntity(par1Packet132TileEntityData.xPosition, par1Packet132TileEntityData.yPosition, par1Packet132TileEntityData.zPosition);

            if (tileentity != null)
            {
                if (par1Packet132TileEntityData.actionType == 1 && tileentity instanceof TileEntityMobSpawner)
                {
                    tileentity.readFromNBT(par1Packet132TileEntityData.customParam1);
                }
                else if (par1Packet132TileEntityData.actionType == 2 && tileentity instanceof TileEntityCommandBlock)
                {
                    tileentity.readFromNBT(par1Packet132TileEntityData.customParam1);
                }
                else if (par1Packet132TileEntityData.actionType == 3 && tileentity instanceof TileEntityBeacon)
                {
                    tileentity.readFromNBT(par1Packet132TileEntityData.customParam1);
                }
                else if (par1Packet132TileEntityData.actionType == 4 && tileentity instanceof TileEntitySkull)
                {
                    tileentity.readFromNBT(par1Packet132TileEntityData.customParam1);
                }
                else
                {
                    tileentity.onDataPacket(mc.thePlayer.sendQueue.getNetManager(),  par1Packet132TileEntityData);
                }
            }
        }
    }
	
}
