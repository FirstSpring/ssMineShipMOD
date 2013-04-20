package net.minecraft.ssMineShipMOD;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

public class clientBlockData {

	public boolean 常に更新と描画するか;
	public int ブロックID;
	public int メタデータ;
	public int メインのエンティティID;
	public int mainとの相対座標X;
	public int mainとの相対座標Y;
	public int mainとの相対座標Z;

	public int メインのブロックX;
	public int メインのブロックY;
	public int メインのブロックZ;

	public TileEntity te;//ない場合はnull
	
	public boolean 上見えている = false;
	public boolean 下見えている = false;
	public boolean 右見えている = false;
	public boolean 左見えている = false;
	public boolean 前見えている = false;
	public boolean 後見えている = false;

	public clientBlockData(byte[] b,int x,int y,int z){

		メインのブロックX = x;
		メインのブロックY = y;
		メインのブロックZ = z;

		ByteArrayDataInput dat = ByteStreams.newDataInput(b);
		上見えている = dat.readBoolean();
		下見えている = dat.readBoolean();
		右見えている = dat.readBoolean();
		左見えている = dat.readBoolean();
		前見えている = dat.readBoolean();
		後見えている = dat.readBoolean();

		常に更新と描画するか = dat.readBoolean();
		
		ブロックID = dat.readInt();
		メタデータ = dat.readInt();
		メインのエンティティID = dat.readInt();
		mainとの相対座標X = dat.readInt();
		mainとの相対座標Y = dat.readInt();
		mainとの相対座標Z = dat.readInt();
		
		if(Block.blocksList[ブロックID] != null&&Block.blocksList[ブロックID].hasTileEntity(メタデータ))
		{
			te = Block.blocksList[ブロックID].createTileEntity(clientproxy.偽クライアントワールド,メタデータ);
			te.setWorldObj(clientproxy.偽クライアントワールド);
		}
		
		ssMineShipMOD.インスタンス.蔵側座標toブロックデータ.put(new posXYZ(メインのブロックX+mainとの相対座標X,メインのブロックY+mainとの相対座標Y,メインのブロックZ+mainとの相対座標Z),this);
	}
}
