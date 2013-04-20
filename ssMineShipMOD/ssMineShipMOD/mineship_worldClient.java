package net.minecraft.ssMineShipMOD;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldSettings;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class mineship_worldClient extends WorldClient
{
	public WorldClient w;
	
	public mineship_worldClient(WorldClient w) {
		super(
				FMLClientHandler.instance().getClient().playerController.func_78754_a(w).sendQueue,
				new WorldSettings(
						w.getWorldInfo()
						),
						w.provider.dimensionId,
						w.difficultySetting,
						FMLClientHandler.instance().getClient().mcProfiler,
						FMLClientHandler.instance().getClient().getLogAgent()		
				);
		this.w = w;
	}
	
	public TileEntity getBlockTileEntity(int i, int j, int k)
	{
		return w.getBlockTileEntity(i, j, k);
	}
}