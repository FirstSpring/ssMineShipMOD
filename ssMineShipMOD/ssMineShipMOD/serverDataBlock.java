package net.minecraft.ssMineShipMOD;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
//サーバーだけで!
public class serverDataBlock
{
	public boolean 常に更新と描画するか = false;

	public WorldServer データ用ワールド;
	EntityMainBlock メイン;
	EntityBlockCol あたり判定用;
	public int ブロックID;
	public int メタデータ;
	public int メインのエンティティID;

	public boolean 上見えている = false;
	public boolean 下見えている = false;
	public boolean 右見えている = false;
	public boolean 左見えている = false;
	public boolean 前見えている = false;
	public boolean 後見えている = false;

	public int mainとの相対座標X;
	public int mainとの相対座標Y;
	public int mainとの相対座標Z;
	public double mainとの角度;
	public double mainとの距離;

	public serverDataBlock()
	{
		データ用ワールド = MinecraftServer.getServer().worldServerForDimension(ssMineShipMOD.インスタンス.データ用ディメンジョン);
	}

	public void 同期()
	{
		TileEntity te = this.データ用ワールド.getBlockTileEntity(this.メイン.ブロックの位置X+this.mainとの相対座標X,this.メイン.ブロックの位置Y+this.mainとの相対座標Y,this.メイン.ブロックの位置Z+this.mainとの相対座標Z);
		te.updateEntity();
		Iterator<EntityPlayer> it = ssMineShipMOD.インスタンス.描画してる蔵一覧.iterator();
		while(it.hasNext())
		{
			Packet p = te.getDescriptionPacket();
			if(p != null&&p instanceof Packet132TileEntityData){
				ByteArrayOutputStream bos = new ByteArrayOutputStream(p.getPacketSize());
				DataOutputStream dos = new DataOutputStream(bos);
				try {
					p.writePacketData(dos);
				} catch (IOException e) {
					e.printStackTrace();
				}
				PacketDispatcher.sendPacketToPlayer(new Packet250CustomPayload("データ同期",bos.toByteArray()),(Player)it.next());
			}
			else if(p != null&&p instanceof Packet250CustomPayload){
				ByteArrayOutputStream bos = new ByteArrayOutputStream(p.getPacketSize());
				DataOutputStream dos = new DataOutputStream(bos);
				try {
					p.writePacketData(dos);
				} catch (IOException e) {
					e.printStackTrace();
				}
				PacketDispatcher.sendPacketToPlayer(new Packet250CustomPayload("データ同期250",bos.toByteArray()),(Player)it.next());
			}
		}
	}

	public serverDataBlock(EntityMainBlock emb,World par1World,int x,int y,int z,int px,int py,int pz,int bx,int by,int bz)
	{
		this();
		this.メイン = emb;
		this.メイン.構成しているブロック.add(this);
		this.mainとの相対座標X = px;
		this.mainとの相対座標Y = py;
		this.mainとの相対座標Z = pz;
		ssMineShipMOD.インスタンス.ヌルを返す = true;
		this.データ用ワールド.setBlock(bx,by,bz,par1World.getBlockId(x, y, z),par1World.getBlockMetadata(x, y, z),3);
		ssMineShipMOD.インスタンス.ヌルを返す = false;
		this.データ用ワールド.markBlockForUpdate(bx,by,bz);
		if(par1World.getBlockTileEntity(x,y,z) != null&&this.データ用ワールド.getBlockTileEntity(bx,by,bz) != null)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			par1World.getBlockTileEntity(x,y,z).writeToNBT(nbt);
			this.データ用ワールド.getBlockTileEntity(bx,by,bz).readFromNBT(nbt);
			this.データ用ワールド.getBlockTileEntity(bx,by,bz).xCoord = bx;
			this.データ用ワールド.getBlockTileEntity(bx,by,bz).yCoord = by;
			this.データ用ワールド.getBlockTileEntity(bx,by,bz).zCoord = bz;
		}
	}

	public serverDataBlock(EntityMainBlock emb,int x,int y,int z,int px,int py,int pz)
	{
		this();
		this.メイン = emb;
		this.メイン.構成しているブロック.add(this);
		this.mainとの相対座標X = px;
		this.mainとの相対座標Y = py;
		this.mainとの相対座標Z = pz;
	}

	public void init()
	{
		double r = Math.atan2(this.mainとの相対座標Z,this.mainとの相対座標X);
		this.mainとの角度 = r * 180 / Math.PI;
		this.mainとの距離 = this.getDistance();
		this.メインのエンティティID = this.メイン.entityId;
		this.ブロックID = this.データ用ワールド.getBlockId(this.メイン.ブロックの位置X+this.mainとの相対座標X,this.メイン.ブロックの位置Y+this.mainとの相対座標Y,this.メイン.ブロックの位置Z+this.mainとの相対座標Z);
		this.メタデータ = this.データ用ワールド.getBlockMetadata(this.メイン.ブロックの位置X+this.mainとの相対座標X,this.メイン.ブロックの位置Y+this.mainとの相対座標Y,this.メイン.ブロックの位置Z+this.mainとの相対座標Z);

		//要修正
		if(this.データ用ワールド.getBlockTileEntity(this.メイン.ブロックの位置X+this.mainとの相対座標X,this.メイン.ブロックの位置Y+this.mainとの相対座標Y,this.メイン.ブロックの位置Z+this.mainとの相対座標Z) != null)
			常に更新と描画するか = true;

		posXYZ poss = new posXYZ(this.メイン.ブロックの位置X+this.mainとの相対座標X,this.メイン.ブロックの位置Y+this.mainとの相対座標Y,this.メイン.ブロックの位置Z+this.mainとの相対座標Z);
		ssMineShipMOD.インスタンス.座標toブロックデータ.put(poss,this);

		double cosx = Math.cos((double)(this.メイン.rotationYaw + this.mainとの角度) * Math.PI / 180.0D)*this.mainとの距離;
		double var3 = Math.sin((double)(this.メイン.rotationYaw + this.mainとの角度) * Math.PI / 180.0D)*this.mainとの距離;
		this.あたり判定用 = new EntityBlockCol(this.メイン.worldObj,this.メイン.posX - 0.5F + cosx, this.メイン.posY + this.mainとの相対座標Y,this.メイン.posZ - 0.5F + var3,this);
		this.あたり判定用.rotationYaw = 0.0F;
		this.メイン.worldObj.spawnEntityInWorld(this.あたり判定用);

		if(this.データ用ワールド.getBlockId(this.メイン.ブロックの位置X+this.mainとの相対座標X,this.メイン.ブロックの位置Y+this.mainとの相対座標Y+1,this.メイン.ブロックの位置Z+this.mainとの相対座標Z) == 0)
			this.上見えている = true;
		else if(!Block.blocksList[this.データ用ワールド.getBlockId(this.メイン.ブロックの位置X+this.mainとの相対座標X,this.メイン.ブロックの位置Y+this.mainとの相対座標Y+1,this.メイン.ブロックの位置Z+this.mainとの相対座標Z)].blockMaterial.getCanBlockGrass())
			this.上見えている = true;
		if(this.データ用ワールド.getBlockId(this.メイン.ブロックの位置X+this.mainとの相対座標X,this.メイン.ブロックの位置Y+this.mainとの相対座標Y-1,this.メイン.ブロックの位置Z+this.mainとの相対座標Z) == 0)
			this.下見えている = true;
		else if(!Block.blocksList[this.データ用ワールド.getBlockId(this.メイン.ブロックの位置X+this.mainとの相対座標X,this.メイン.ブロックの位置Y+this.mainとの相対座標Y-1,this.メイン.ブロックの位置Z+this.mainとの相対座標Z)].blockMaterial.getCanBlockGrass())
			this.下見えている = true;
		if(this.データ用ワールド.getBlockId(this.メイン.ブロックの位置X+this.mainとの相対座標X+1,this.メイン.ブロックの位置Y+this.mainとの相対座標Y,this.メイン.ブロックの位置Z+this.mainとの相対座標Z) == 0)
			this.右見えている = true;
		else if(!Block.blocksList[this.データ用ワールド.getBlockId(this.メイン.ブロックの位置X+this.mainとの相対座標X+1,this.メイン.ブロックの位置Y+this.mainとの相対座標Y,this.メイン.ブロックの位置Z+this.mainとの相対座標Z)].blockMaterial.getCanBlockGrass())
			this.右見えている = true;
		if(this.データ用ワールド.getBlockId(this.メイン.ブロックの位置X+this.mainとの相対座標X-1,this.メイン.ブロックの位置Y+this.mainとの相対座標Y,this.メイン.ブロックの位置Z+this.mainとの相対座標Z) == 0)
			this.左見えている = true;
		else if(!Block.blocksList[this.データ用ワールド.getBlockId(this.メイン.ブロックの位置X+this.mainとの相対座標X-1,this.メイン.ブロックの位置Y+this.mainとの相対座標Y,this.メイン.ブロックの位置Z+this.mainとの相対座標Z)].blockMaterial.getCanBlockGrass())
			this.左見えている = true;
		if(this.データ用ワールド.getBlockId(this.メイン.ブロックの位置X+this.mainとの相対座標X,this.メイン.ブロックの位置Y+this.mainとの相対座標Y,this.メイン.ブロックの位置Z+this.mainとの相対座標Z+1) == 0)
			this.前見えている = true;
		else if(!Block.blocksList[this.データ用ワールド.getBlockId(this.メイン.ブロックの位置X+this.mainとの相対座標X,this.メイン.ブロックの位置Y+this.mainとの相対座標Y,this.メイン.ブロックの位置Z+this.mainとの相対座標Z+1)].blockMaterial.getCanBlockGrass())
			this.前見えている = true;
		if(this.データ用ワールド.getBlockId(this.メイン.ブロックの位置X+this.mainとの相対座標X,this.メイン.ブロックの位置Y+this.mainとの相対座標Y,this.メイン.ブロックの位置Z+this.mainとの相対座標Z-1) == 0)
			this.後見えている = true;
		else if(!Block.blocksList[this.データ用ワールド.getBlockId(this.メイン.ブロックの位置X+this.mainとの相対座標X,this.メイン.ブロックの位置Y+this.mainとの相対座標Y,this.メイン.ブロックの位置Z+this.mainとの相対座標Z-1)].blockMaterial.getCanBlockGrass())
			this.後見えている = true;
	}

	public byte[] cb()
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream(25);
		DataOutputStream dos = new DataOutputStream(bos);

		try {
			dos.writeBoolean(this.上見えている);
			dos.writeBoolean(this.下見えている);
			dos.writeBoolean(this.右見えている);
			dos.writeBoolean(this.左見えている);
			dos.writeBoolean(this.前見えている);
			dos.writeBoolean(this.後見えている);

			dos.writeBoolean(this.常に更新と描画するか);

			dos.writeInt(this.ブロックID);
			dos.writeInt(this.メタデータ);
			dos.writeInt(this.メインのエンティティID);
			dos.writeInt(this.mainとの相対座標X);
			dos.writeInt(this.mainとの相対座標Y);
			dos.writeInt(this.mainとの相対座標Z);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}

	public double getDistance()
	{
		double d3 = this.mainとの相対座標X;
		double d4 = 0;
		double d5 = this.mainとの相対座標Z;
		return (double)MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
	}
}
