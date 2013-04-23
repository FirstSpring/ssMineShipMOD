package net.minecraft.ssMineShipMOD;

import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;

public class worldevent
{
	@ForgeSubscribe
	public void entityjoin(EntityJoinWorldEvent e)
	{
		if(ssMineShipMOD.インスタンス.エンティティをスポーンさせない)
			e.setCanceled(true);/*
		else if(e.entity.worldObj.provider.dimensionId == ssMineShipMOD.インスタンス.データ用ディメンジョン)
		{
			MinecraftServer mcs = MinecraftServer.getServer();
			mcs.getConfigurationManager().transferEntityToWorld(
					e.entity,
					0,
					mcs.worldServerForDimension(e.entity.worldObj.provider.dimensionId),
					mcs.worldServerForDimension(ssMineShipMOD.インスタンス.カレントcolエンティティ.メイン.worldObj.provider.dimensionId),
					new mineshipteleporter(mcs.worldServerForDimension(ssMineShipMOD.インスタンス.カレントcolエンティティ.メイン.worldObj.provider.dimensionId)
							));
		}*/
		//仮題
	}
/*
	@ForgeSubscribe
	public void entityupdate(EntityEvent.CanUpdate e)
	{
		if(e.entity.worldObj.provider.dimensionId == ssMineShipMOD.インスタンス.データ用ディメンジョン)
			e.canUpdate = false;

	}
*/
	@ForgeSubscribe
	public void worldunSave(WorldEvent.Save l)//データ用ワールドがアンロードされたら、入れ替える
	{
		if(l.world != null&&l.world instanceof WorldServer&&l.world instanceof mineshipWorld&&l.world.provider.dimensionId == ssMineShipMOD.インスタンス.データ用ディメンジョン)
		{
			DimensionManager.setWorld(l.world.provider.dimensionId,null);
			WorldServer w = ((mineshipWorld)l.world).データ用ワールド;
			DimensionManager.setWorld(l.world.provider.dimensionId,w);
			MinecraftForge.EVENT_BUS.post(new WorldEvent.Save(w));
			DimensionManager.setWorld(l.world.provider.dimensionId,(WorldServer) l.world);
		}
	}
	
	@ForgeSubscribe
	public void worldload(WorldEvent.Load l)//データ用ワールドがロードされたら、入れ替える
	{
		if(l.world != null&&l.world instanceof WorldServer&&!(l.world instanceof mineshipWorld)&&l.world.provider.dimensionId == ssMineShipMOD.インスタンス.データ用ディメンジョン)
		{
			DimensionManager.setWorld(l.world.provider.dimensionId,null);
			WorldServer w = new mineshipWorld((WorldServer)l.world);
			MinecraftForge.EVENT_BUS.post(new WorldEvent.Load(w));
		}
	}
}
