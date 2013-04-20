package net.minecraft.ssMineShipMOD

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityBlockCol extends ssEntity{

	
	serverDataBlock メイン;
	
	public EntityBlockCol(World par1World) {
		super(par1World);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.setSize(1.0F,1.0F);//あたり判定はブロックと同じ
	}
	
	public EntityBlockCol(World par1World,double x,double y,double z,serverDataBlock s)
	{
		this(par1World);
		this.メイン = s;
		this.prevPosX = x+0.5F;//常に0.5足した位置にある
		this.prevPosY = y;
		this.prevPosZ = z+0.5F;
		this.setPosition(x+0.5F,y,z+0.5F);
	}
	
	public void onUpdate()
	{
		super.onUpdate();

		if(this.worldObj.isRemote)
		{

		}
		else{//サーバー側

			if(this.メイン == null||this.メイン.メイン.isDead)
			{
				this.setDead();
				return;
			}
		}
	}

	public boolean interact(EntityPlayer par1EntityPlayer)
	{
		if(par1EntityPlayer.isSneaking())
			super.interact(par1EntityPlayer);
		else if(!this.worldObj.isRemote && this.メイン.メイン.データ用ワールド != null)
		{
			if(Block.blocksList[this.メイン.メイン.データ用ワールド.getBlockId(this.メイン.メイン.ブロックの位置X+this.メイン.mainとの相対座標X,this.メイン.メイン.ブロックの位置Y+this.メイン.mainとの相対座標Y,this.メイン.メイン.ブロックの位置Z+this.メイン.mainとの相対座標Z)] != null)
			{
				ssMineShipMOD.インスタンス.カレントcolエンティティ = this.メイン;
				Block.blocksList[this.メイン.メイン.データ用ワールド.getBlockId(this.メイン.メイン.ブロックの位置X+this.メイン.mainとの相対座標X,this.メイン.メイン.ブロックの位置Y+this.メイン.mainとの相対座標Y,this.メイン.メイン.ブロックの位置Z+this.メイン.mainとの相対座標Z)].onBlockActivated(this.メイン.メイン.データ用ワールド,this.メイン.メイン.ブロックの位置X+this.メイン.mainとの相対座標X,this.メイン.メイン.ブロックの位置Y+this.メイン.mainとの相対座標Y,this.メイン.メイン.ブロックの位置Z+this.メイン.mainとの相対座標Z, par1EntityPlayer, 0, 0, 0, 0);
			}
		}
		return true;
	}
}
