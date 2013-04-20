package net.minecraft.ssMineShipMOD;

import java.util.HashSet;
import java.util.Iterator;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class mainBlockTileEntity extends TileEntity{

	public HashSet<posXYZ> 登録されているブロックの相対座標 = new HashSet<posXYZ>();

	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);

		par1NBTTagCompound.setInteger("数",this.登録されているブロックの相対座標.size());

		Iterator i = this.登録されているブロックの相対座標.iterator();
		int c = 0;
		while(i.hasNext())
		{
			posXYZ pos = (posXYZ)i.next();
			par1NBTTagCompound.setInteger("ssX"+c, pos.x);
			par1NBTTagCompound.setInteger("ssY"+c, pos.y);
			par1NBTTagCompound.setInteger("ssZ"+c, pos.z);
			c++;
		}
	}

	public void applyEntityCollision(Entity par1Entity)
	{}

	public void ブロックをエンティティに()
	{
		if(!this.worldObj.isRemote){
			
			ssMineShipMOD.インスタンス.ブロックを更新しない = true;
			int 最新の端 = ssMineShipMOD.インスタンス.一番大きいX座標;
			int 一番大きいX座標  = ssMineShipMOD.インスタンス.一番大きいX座標;
			Iterator i = this.登録されているブロックの相対座標.iterator();

			while(i.hasNext())
			{
				posXYZ p = (posXYZ) i.next();
				一番大きいX座標 = Math.max(最新の端+p.x,一番大きいX座標);
			}

			一番大きいX座標+=8;

			ssMineShipMOD.インスタンス.一番大きいX座標 = 一番大きいX座標;

			int それの中心X = 最新の端 + (一番大きいX座標-最新の端)/2;

			EntityMainBlock main = new EntityMainBlock(this.登録されているブロックの相対座標,this.worldObj,this.xCoord,this.yCoord,this.zCoord,それの中心X,120,0);

			this.worldObj.spawnEntityInWorld(main);

			i = this.登録されているブロックの相対座標.iterator();

			int c = 0;
			while(i.hasNext())
			{				
				posXYZ p = (posXYZ) i.next();
				serverDataBlock e = new serverDataBlock(main,this.worldObj,this.xCoord+p.x,this.yCoord+p.y,this.zCoord+p.z,p.x,p.y,p.z,それの中心X + p.x,p.y+120,p.z);
				if(!ssMineShipMOD.インスタンス.サーバー描画用データ.containsKey(main.entityId))
				{
					ssMineShipMOD.インスタンス.サーバー描画用データ.put(main.entityId,new HashSet<serverDataBlock>());
				}
				ssMineShipMOD.インスタンス.サーバー描画用データ.get(main.entityId).add(e);
				c++;
			}

			i = main.構成しているブロック.iterator();
			while(i.hasNext())
			{				
				serverDataBlock d = (serverDataBlock) i.next();
				d.init();
			}

			i = this.登録されているブロックの相対座標.iterator();


			ssMineShipMOD.インスタンス.エンティティをスポーンさせない = true;
			while(i.hasNext())
			{				
				posXYZ p = (posXYZ) i.next();
				this.worldObj.setBlockToAir(this.xCoord+p.x,this.yCoord+p.y,this.zCoord+p.z);
			}
			ssMineShipMOD.インスタンス.エンティティをスポーンさせない = false;
			ssMineShipMOD.インスタンス.ブロックを更新しない = false;
		}
	}

	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);

		int 数 = par1NBTTagCompound.getInteger("数");
		for(int i = 0;i<数;i++)
		{
			posXYZ pos = new posXYZ(par1NBTTagCompound.getInteger("ssX"+i),par1NBTTagCompound.getInteger("ssY"+i),par1NBTTagCompound.getInteger("ssZ"+i));
			this.登録されているブロックの相対座標.add(pos);
		}
	}

}
