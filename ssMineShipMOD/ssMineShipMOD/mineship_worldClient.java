package net.minecraft.ssMineShipMOD;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
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

	public boolean canBlockSeeTheSky(int par1, int par2, int par3)
    {
        return true;
    }
	
	public TileEntity getBlockTileEntity(int i, int j, int k)
	{
		if(ssMineShipMOD.インスタンス.蔵側座標toブロックデータ.get(new posXYZ(i,j,k)) != null)
			return ssMineShipMOD.インスタンス.蔵側座標toブロックデータ.get(new posXYZ(i,j,k)).te;
		return null;
	}

	@Override
	public int getBlockId(int i, int j, int k) {
		if(ssMineShipMOD.インスタンス.蔵側座標toブロックデータ.get(new posXYZ(i,j,k)) != null)
			return ssMineShipMOD.インスタンス.蔵側座標toブロックデータ.get(new posXYZ(i,j,k)).ブロックID;
		return 0;
	}

	@Override
	public int getBlockMetadata(int i, int j, int k) {
		if(ssMineShipMOD.インスタンス.蔵側座標toブロックデータ.get(new posXYZ(i,j,k)) != null)
			return ssMineShipMOD.インスタンス.蔵側座標toブロックデータ.get(new posXYZ(i,j,k)).メタデータ;
		return 0;
	}

	@Override
	public Material getBlockMaterial(int i, int j, int k) {
		if(ssMineShipMOD.インスタンス.蔵側座標toブロックデータ.get(new posXYZ(i,j,k)) != null)
			return Block.blocksList[ssMineShipMOD.インスタンス.蔵側座標toブロックデータ.get(new posXYZ(i,j,k)).ブロックID].blockMaterial;
		return null;
	}

}