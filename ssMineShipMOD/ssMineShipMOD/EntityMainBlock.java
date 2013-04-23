package net.minecraft.ssMineShipMOD;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityMainBlock extends ssEntity
{
	private boolean 初回アップデート = true;
	public WorldServer データ用ワールド;
	public int ブロックの位置X;
	public int ブロックの位置Y;
	public int ブロックの位置Z;
	public int ディメンジョンID;
	public int 一番大きいX;
	public HashSet<posXYZ> 登録されているブロックの相対座標;
	public ArrayList<serverDataBlock> 構成しているブロック = new ArrayList<serverDataBlock>();

	public int スピード = 0;

	public EntityMainBlock(World par1World) {
		super(par1World);
		this.ignoreFrustumCheck = true;
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		if(par1World != null&&!par1World.isRemote)
		{
			データ用ワールド = MinecraftServer.getServer().worldServerForDimension(ssMineShipMOD.インスタンス.データ用ディメンジョン);
			this.ディメンジョンID = par1World.provider.dimensionId;
		}
		this.setSize(1.0F,1.0F);//あたり判定はブロックと同じ
	}

	public EntityMainBlock(HashSet<posXYZ> poss,World par1World,int x,int y,int z,int bx,int by,int bz)
	{
		this(par1World);
		一番大きいX = bx;
		this.初回アップデート = false;
		this.登録されているブロックの相対座標 = poss;
		this.prevPosX = x+0.5F;//常に0.5足した位置にある
		this.prevPosY = y;
		this.prevPosZ = z+0.5F;
		this.ブロックの位置X = bx;
		this.ブロックの位置Y = by;
		this.ブロックの位置Z = bz;
		if(!par1World.isRemote)
		{
			this.データ用ワールド.setBlock(ブロックの位置X,ブロックの位置Y,ブロックの位置Z,par1World.getBlockId(x, y, z));
			this.データ用ワールド.setBlockMetadataWithNotify(ブロックの位置X,ブロックの位置Y,ブロックの位置Z,par1World.getBlockMetadata(x, y, z),0);
			if(this.データ用ワールド.getBlockTileEntity(ブロックの位置X,ブロックの位置Y,ブロックの位置Z) != null)
			{
				NBTTagCompound nbt = new NBTTagCompound();
				par1World.getBlockTileEntity(x,y,z).writeToNBT(nbt);
				this.データ用ワールド.getBlockTileEntity(ブロックの位置X,ブロックの位置Y,ブロックの位置Z).readFromNBT(nbt);
			}
			par1World.removeBlockTileEntity(x,y,z);
			par1World.setBlockToAir(x,y,z);
		}
		this.setPosition(x+0.5F,y,z+0.5F);
	}

	public void onUpdate()
	{
		super.onUpdate();
		if(this.worldObj.isRemote)
		{
		}
		else{//サーバー側
			ssMineShipMOD.インスタンス.カレントエンティティ = this;
			if(this.初回アップデート)
			{
				if(!ssMineShipMOD.インスタンス.サーバー描画用データ.containsKey(this.entityId))
				{
					ssMineShipMOD.インスタンス.サーバー描画用データ.put(this.entityId,new HashSet<serverDataBlock>());
				}

				Iterator i = this.登録されているブロックの相対座標.iterator();
				int c = 0;
				while(i.hasNext())
				{
					posXYZ p = (posXYZ) i.next();
					serverDataBlock e = new serverDataBlock(this,(int)this.posX+p.x,(int)this.posY+p.y,(int)this.posZ+p.z,p.x,p.y,p.z);
					ssMineShipMOD.インスタンス.サーバー描画用データ.get(this.entityId).add(e);
					c++;
				}

				i = ssMineShipMOD.インスタンス.サーバー描画用データ.get(this.entityId).iterator();
				while(i.hasNext())
				{
					serverDataBlock sdb = (serverDataBlock)i.next();
					sdb.init();
				}

				this.初回アップデート = false;
			}

			this.motionX = this.motionY = this.motionZ= 0;

			if(this.riddenByEntity != null&&this.riddenByEntity instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer)this.riddenByEntity;
				if(ssMineShipMOD.インスタンス.入力状態.containsKey(player.username))
				{
					byte[] keys = ssMineShipMOD.インスタンス.入力状態.get(player.username);

					this.motionZ=Math.sin((rotationYaw+180)* (float)Math.PI / 180.0F)*this.スピード/40.0F;
					this.motionX=Math.cos((rotationYaw+180)* (float)Math.PI / 180.0F)*this.スピード/40.0F;

					if(keys[11] == 1)
					{
						this.motionX = 0;
						this.motionZ = 0;
						this.motionY = 0;
						this.スピード = 0;
					}

					if(keys[4] == 1)
					{
						this.motionY += 0.1;
					}

					if(keys[5] == 1)
					{
						this.motionY -= 0.1;
					}

					if(keys[0] == 1&&this.スピード < 10)
					{
						this.スピード += 1;
					}

					if(keys[2] == 1)
					{
						this.スピード -= 1;
					}

					if(keys[1] == 1)
					{
						this.setRotation(this.rotationYaw-6, this.rotationPitch);
					}
					if(keys[3] == 1)
					{
						this.setRotation(this.rotationYaw+6, this.rotationPitch);
					}
				}
			}
			else if(ssMineShipMOD.インスタンス.オーナー.containsKey(this))
			{
				EntityPlayer ep = ssMineShipMOD.インスタンス.オーナー.get(this);
				if(ssMineShipMOD.インスタンス.入力状態.containsKey(ep.username))
				{
					byte[] keys = ssMineShipMOD.インスタンス.入力状態.get(ep.username);

					this.motionZ=Math.sin((rotationYaw+180)* (float)Math.PI / 180.0F)*this.スピード/40.0F;
					this.motionX=Math.cos((rotationYaw+180)* (float)Math.PI / 180.0F)*this.スピード/40.0F;

					if(keys[18] == 1)
					{
						this.motionY += 0.2;
					}

					if(keys[19] == 1)
					{
						this.motionY -= 0.2;
					}

					if(keys[13] == 1&&this.スピード < 10)
					{
						this.スピード += 1;
					}

					if(keys[14] == 1)
					{
						this.スピード -= 1;
					}

					if(keys[15] == 1)
					{
						this.setRotation(this.rotationYaw-6, this.rotationPitch);
					}
					if(keys[16] == 1)
					{
						this.setRotation(this.rotationYaw+6, this.rotationPitch);
					}
					if(keys[17] == 1)
					{
						this.motionX = 0;
						this.motionZ = 0;
						this.motionY = 0;
						this.スピード = 0;
					}
				}
			}

			this.moveEntity(this.motionX, this.motionY, this.motionZ);

			for(int i = 0;i<this.構成しているブロック.size();i++)
			{
				serverDataBlock e = this.構成しているブロック.get(i);
				/*
				if(e.あたり判定用 != null){
					double cosx = Math.cos((double)(this.rotationYaw + e.mainとの角度) * Math.PI / 180.0D)*e.mainとの距離;
					double var3 = Math.sin((double)(this.rotationYaw + e.mainとの角度) * Math.PI / 180.0D)*e.mainとの距離;
					e.あたり判定用.setPosition(this.posX - 0.5F + cosx, this.posY + e.mainとの相対座標Y, this.posZ - 0.5F + var3);
				}*/
			}
		}
	}

	public boolean interact(EntityPlayer par1EntityPlayer)
	{
		if(par1EntityPlayer.isSneaking())
			super.interact(par1EntityPlayer);
		if(!par1EntityPlayer.worldObj.isRemote)
		{
			if(par1EntityPlayer.getCurrentItemOrArmor(0) != null&& par1EntityPlayer.getCurrentItemOrArmor(0).getItem() == ssMineShipMOD.インスタンス.Itemce)
			{
				this.Entityをブロックに(par1EntityPlayer);
			}
			else if(par1EntityPlayer.getCurrentItemOrArmor(0) == null)
			{

				if(ssMineShipMOD.インスタンス.オーナー.containsKey(this))
				{
					if(ssMineShipMOD.インスタンス.オーナー.get(this) == par1EntityPlayer)
					{
						ssMineShipMOD.インスタンス.オーナー.remove(this);
						par1EntityPlayer.sendChatToPlayer("解除");
					}
				}
				else{
					ssMineShipMOD.インスタンス.オーナー.put(this, par1EntityPlayer);
					par1EntityPlayer.sendChatToPlayer("遠隔操作的な");
				}
			}
		}
		return false;
	}

	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeEntityToNBT(par1NBTTagCompound);

		par1NBTTagCompound.setInteger("一番大きいX",this.一番大きいX);
		
		par1NBTTagCompound.setInteger("メインブロックの位置X",this.ブロックの位置X);
		par1NBTTagCompound.setInteger("メインブロックの位置Y",this.ブロックの位置Y);
		par1NBTTagCompound.setInteger("メインブロックの位置Z",this.ブロックの位置Z);

		par1NBTTagCompound.setInteger("数",this.登録されているブロックの相対座標.size());

		Iterator i = this.登録されているブロックの相対座標.iterator();

		int c = 0;

		while(i.hasNext())
		{
			posXYZ pos = (posXYZ)i.next();
			par1NBTTagCompound.setInteger("X"+c, pos.x);
			par1NBTTagCompound.setInteger("Y"+c, pos.y);
			par1NBTTagCompound.setInteger("Z"+c, pos.z);
			c++;
		}
	}
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readEntityFromNBT(par1NBTTagCompound);

		this.一番大きいX = par1NBTTagCompound.getInteger("一番大きいX");
		
		this.ブロックの位置X = par1NBTTagCompound.getInteger("メインブロックの位置X");
		this.ブロックの位置Y = par1NBTTagCompound.getInteger("メインブロックの位置Y");
		this.ブロックの位置Z = par1NBTTagCompound.getInteger("メインブロックの位置Z");

		this.登録されているブロックの相対座標 = new HashSet<posXYZ>();
		int 数 = par1NBTTagCompound.getInteger("数");
		for(int i = 0;i<数;i++)
		{
			posXYZ pos = new posXYZ(par1NBTTagCompound.getInteger("X"+i),par1NBTTagCompound.getInteger("Y"+i),par1NBTTagCompound.getInteger("Z"+i));
			this.登録されているブロックの相対座標.add(pos);
		}

		ssMineShipMOD.インスタンス.一番大きいX座標 = Math.max(一番大きいX,ssMineShipMOD.インスタンス.一番大きいX座標);
	}

	public void Entityをブロックに(EntityPlayer player)
	{
		if(this instanceof EntityMainBlock&&!this.worldObj.isRemote)
		{
			EntityMainBlock e = (EntityMainBlock)this;
			int x = e.ブロックの位置X;
			int y = e.ブロックの位置Y;
			int z = e.ブロックの位置Z;
			Iterator i = e.構成しているブロック.iterator();
			while(i.hasNext())
			{
				serverDataBlock eb = (serverDataBlock)i.next();
				int xx = eb.mainとの相対座標X;
				int yy = eb.mainとの相対座標Y;
				int zz = eb.mainとの相対座標Z;
				ssMineShipMOD.インスタンス.ヌルを返す = true;
				e.worldObj.setBlock((int)(e.posX-0.5F)+eb.mainとの相対座標X,(int)e.posY+eb.mainとの相対座標Y,(int)(e.posZ-0.5F)+eb.mainとの相対座標Z, e.データ用ワールド.getBlockId(x+xx, y+yy, z+zz),e.データ用ワールド.getBlockMetadata(x+xx, y+yy, z+zz),0);
				ssMineShipMOD.インスタンス.ヌルを返す = false;
				if(e.worldObj.getBlockTileEntity((int)(e.posX-0.5F)+eb.mainとの相対座標X,(int)e.posY+eb.mainとの相対座標Y,(int)(e.posZ-0.5F)+eb.mainとの相対座標Z) != null)
				{
					NBTTagCompound nbt = new NBTTagCompound();
					e.データ用ワールド.getBlockTileEntity(x+xx, y+yy, z+zz).writeToNBT(nbt);
					e.worldObj.getBlockTileEntity((int)(e.posX-0.5F)+eb.mainとの相対座標X,(int)e.posY+eb.mainとの相対座標Y,(int)(e.posZ-0.5F)+eb.mainとの相対座標Z).readFromNBT(nbt);
					e.worldObj.getBlockTileEntity((int)(e.posX-0.5F)+eb.mainとの相対座標X,(int)e.posY+eb.mainとの相対座標Y,(int)(e.posZ-0.5F)+eb.mainとの相対座標Z).xCoord = (int)(e.posX-0.5F)+eb.mainとの相対座標X;
					e.worldObj.getBlockTileEntity((int)(e.posX-0.5F)+eb.mainとの相対座標X,(int)e.posY+eb.mainとの相対座標Y,(int)(e.posZ-0.5F)+eb.mainとの相対座標Z).yCoord = (int)(e.posY)+eb.mainとの相対座標Y;
					e.worldObj.getBlockTileEntity((int)(e.posX-0.5F)+eb.mainとの相対座標X,(int)e.posY+eb.mainとの相対座標Y,(int)(e.posZ-0.5F)+eb.mainとの相対座標Z).zCoord = (int)(e.posZ-0.5F)+eb.mainとの相対座標Z;
					e.worldObj.getBlockTileEntity((int)(e.posX-0.5F)+eb.mainとの相対座標X,(int)e.posY+eb.mainとの相対座標Y,(int)(e.posZ-0.5F)+eb.mainとの相対座標Z).validate();
				}
			}
			e.worldObj.setBlock((int)(e.posX-0.5F),(int)e.posY,(int)(e.posZ-0.5F), ssMineShipMOD.インスタンス.mainBlockID,0,0);
			e.worldObj.markBlockForUpdate((int)(e.posX-0.5F),(int)e.posY,(int)(e.posZ-0.5F));
			i = e.構成しているブロック.iterator();
			while(i.hasNext())
			{
				serverDataBlock eb = (serverDataBlock)i.next();
				e.worldObj.markBlockForUpdate((int)(e.posX-0.5F)+eb.mainとの相対座標X,(int)e.posY+eb.mainとの相対座標Y,(int)(e.posZ-0.5F)+eb.mainとの相対座標Z);
			}
			e.setDead();
		}
	}
	
	@SideOnly(Side.CLIENT)
    public int getBrightnessForRender(float par1)
    {
        return 15 << 20 | 15 << 4;
    }
	
    public float getBrightness(float par1)//暗さ対策
    {
        return this.worldObj.provider.lightBrightnessTable[15];
    }

	public boolean isInRangeToRenderDist(double par1)//描画が遠距離でも行われるように
	{
		return true;
	}

}
