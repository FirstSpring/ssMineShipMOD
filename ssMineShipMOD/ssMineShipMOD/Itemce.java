package net.minecraft.ssMineShipMOD;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class Itemce extends Item{

	public Itemce(int par1) {
		super(par1);
		this.setCreativeTab(CreativeTabs.tabTools);
	}

	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
	{
		if(par3Entity instanceof EntityPlayer&&!par2World.isRemote)
		{
			EntityPlayer p = (EntityPlayer)par3Entity;
			if(ssMineShipMOD.インスタンス.入力状態.containsKey(p.username))
			{
				byte[] keys = ssMineShipMOD.インスタンス.入力状態.get(p.username);

				if(!ssMineShipMOD.インスタンス.アイテムの半径.containsKey(p))
				{
					ssMineShipMOD.インスタンス.アイテムの半径.put(p,1);
					ssMineShipMOD.インスタンス.遅延.put(p,1);
				}
				else
				{
					int 半径 = ssMineShipMOD.インスタンス.アイテムの半径.get(p);
					int 遅延 = ssMineShipMOD.インスタンス.遅延.get(p);

					遅延--;
					
					if(keys[10] == 1&&遅延<0)//R
					{
						半径 += 1;
						p.sendChatToPlayer("半径"+半径);
						遅延=5;
					}
					if(keys[11] == 1&&遅延<0)//F
					{
						半径 -= 1;
						p.sendChatToPlayer("半径"+半径);
						遅延=5;
					}
					ssMineShipMOD.インスタンス.アイテムの半径.put(p,半径);
					ssMineShipMOD.インスタンス.遅延.put(p,遅延);
				}
			}
		}
	}

	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		int 半径 = 1;
		if(ssMineShipMOD.インスタンス.アイテムの半径.containsKey(par3EntityPlayer))
			半径= ssMineShipMOD.インスタンス.アイテムの半径.get(par3EntityPlayer);
		float f = 1.0F;
		float f1 = par3EntityPlayer.prevRotationPitch + (par3EntityPlayer.rotationPitch - par3EntityPlayer.prevRotationPitch) * f;
		float f2 = par3EntityPlayer.prevRotationYaw + (par3EntityPlayer.rotationYaw - par3EntityPlayer.prevRotationYaw) * f;
		double d0 = par3EntityPlayer.prevPosX + (par3EntityPlayer.posX - par3EntityPlayer.prevPosX) * (double)f;
		double d1 = par3EntityPlayer.prevPosY + (par3EntityPlayer.posY - par3EntityPlayer.prevPosY) * (double)f + 1.62D - (double)par3EntityPlayer.yOffset;
		double d2 = par3EntityPlayer.prevPosZ + (par3EntityPlayer.posZ - par3EntityPlayer.prevPosZ) * (double)f;
		Vec3 vec3 = par2World.getWorldVec3Pool().getVecFromPool(d0, d1, d2);
		float f3 = MathHelper.cos(-f2 * 0.017453292F - (float)Math.PI);
		float f4 = MathHelper.sin(-f2 * 0.017453292F - (float)Math.PI);
		float f5 = -MathHelper.cos(-f1 * 0.017453292F);
		float f6 = MathHelper.sin(-f1 * 0.017453292F);
		float f7 = f4 * f5;
		float f8 = f3 * f5;
		double d3 = 5.0D;
		Vec3 vec31 = vec3.addVector((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
		MovingObjectPosition movingobjectposition = par2World.rayTraceBlocks_do(vec3, vec31, true);

		if (movingobjectposition == null)
		{
			return par1ItemStack;
		}
		else
		{
			Vec3 vec32 = par3EntityPlayer.getLook(f);
			boolean flag = false;
			float f9 = 1.0F;
			List list = par2World.getEntitiesWithinAABBExcludingEntity(par3EntityPlayer, par3EntityPlayer.boundingBox.addCoord(vec32.xCoord * d3, vec32.yCoord * d3, vec32.zCoord * d3).expand((double)f9, (double)f9, (double)f9));
			int i;

			for (i = 0; i < list.size(); ++i)
			{
				Entity entity = (Entity)list.get(i);

				if (entity.canBeCollidedWith())
				{
					float f10 = entity.getCollisionBorderSize();
					AxisAlignedBB axisalignedbb = entity.boundingBox.expand((double)f10, (double)f10, (double)f10);

					if (axisalignedbb.isVecInside(vec3))
					{
						flag = true;
					}
				}
			}

			if (flag)
			{
				return par1ItemStack;
			}
			else
			{
				if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE)
				{
					i = movingobjectposition.blockX;
					int j = movingobjectposition.blockY;
					int k = movingobjectposition.blockZ;
					if(par2World.getBlockId(i,j,k) != 0)
					{
						if(!par2World.isRemote&&ssMineShipMOD.インスタンス.使っている人一覧.containsKey(par3EntityPlayer))
						{
							posXYZ p = new posXYZ(i-ssMineShipMOD.インスタンス.使っている人一覧.get(par3EntityPlayer).xCoord,j-ssMineShipMOD.インスタンス.使っている人一覧.get(par3EntityPlayer).yCoord,k-ssMineShipMOD.インスタンス.使っている人一覧.get(par3EntityPlayer).zCoord);
							if(!ssMineShipMOD.インスタンス.使っている人一覧.get(par3EntityPlayer).登録されているブロックの相対座標.contains(p))
								ssMineShipMOD.インスタンス.使っている人一覧.get(par3EntityPlayer).登録されているブロックの相対座標.add(p);
							else par3EntityPlayer.sendChatToPlayer("すでに登録されてます");
							int c = 0;
							for(int x = -半径;x<半径;x++){
								for(int y = -半径;y<半径;y++){
									for(int z = -半径;z<半径;z++){
										if(x==半径&&y==半径&&z==半径)
											continue;
										if(par2World.getBlockId(i+x,j+y,k+z) != 0)
										{
											c++;
											posXYZ p1 = new posXYZ(i+x-ssMineShipMOD.インスタンス.使っている人一覧.get(par3EntityPlayer).xCoord,j+y-ssMineShipMOD.インスタンス.使っている人一覧.get(par3EntityPlayer).yCoord,k+z-ssMineShipMOD.インスタンス.使っている人一覧.get(par3EntityPlayer).zCoord);
											if(!ssMineShipMOD.インスタンス.使っている人一覧.get(par3EntityPlayer).登録されているブロックの相対座標.contains(p1))
												ssMineShipMOD.インスタンス.使っている人一覧.get(par3EntityPlayer).登録されているブロックの相対座標.add(p1);
										}
									}	
								}	
							}
							par3EntityPlayer.sendChatToPlayer("登録されたブロック"+c);
						}
						if(!par2World.isRemote&&ssMineShipMOD.インスタンス.使っている人一覧.containsKey(par3EntityPlayer)&&par3EntityPlayer.isSneaking()&&par2World.getBlockId(i,j,k) == ssMineShipMOD.インスタンス.mainBlockID)
						{
							par3EntityPlayer.sendChatToPlayer("変更");
							ssMineShipMOD.インスタンス.使っている人一覧.get(par3EntityPlayer).ブロックをエンティティに();
						}
					}
				}
				return par1ItemStack;
			}
		}
	}

}
