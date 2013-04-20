package net.minecraft.ssMineShipMOD;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

public class clientBlockData {
	
	public int ブロックID;
	public int メタデータ;
	public int メインのエンティティID;
	public int mainとの相対座標X;
	public int mainとの相対座標Y;
	public int mainとの相対座標Z;
	public boolean 上見えている = false;
	public boolean 下見えている = false;
	public boolean 右見えている = false;
	public boolean 左見えている = false;
	public boolean 前見えている = false;
	public boolean 後見えている = false;
	
	public clientBlockData(byte[] b){
		ByteArrayDataInput dat = ByteStreams.newDataInput(b);
		上見えている = dat.readBoolean();
		下見えている = dat.readBoolean();
		右見えている = dat.readBoolean();
		左見えている = dat.readBoolean();
		前見えている = dat.readBoolean();
		後見えている = dat.readBoolean();
		
		ブロックID = dat.readInt();
		メタデータ = dat.readInt();
		メインのエンティティID = dat.readInt();
		mainとの相対座標X = dat.readInt();
		mainとの相対座標Y = dat.readInt();
		mainとの相対座標Z = dat.readInt();
	}
}
