package net.minecraft.ssMineShipMOD;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3Pool;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class renderBlockAccess implements IBlockAccess
{

	public IBlockAccess ba;

	public renderBlockAccess(IBlockAccess ba)
	{
		this.ba = ba;
	}

	@Override
	public int getBlockId(int i, int j, int k) {
		if(ssMineShipMOD.インスタンス.カレント描画エンティティ != null)
		{
			return ssMineShipMOD.インスタンス.カレント描画エンティティ.ブロックID;
		}
		return 0;
	}

	@Override
	public TileEntity getBlockTileEntity(int i, int j, int k) {
		return ba.getBlockTileEntity(i, j, k);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getLightBrightnessForSkyBlocks(int i, int j, int k, int l) {
		int i1 = 15;
		int j1 = 15;
		return i1 << 20 | j1 << 4;
	}

	@Override
	public int getBlockMetadata(int i, int j, int k) {
		if(ssMineShipMOD.インスタンス.カレント描画エンティティ != null)
		{
			return ssMineShipMOD.インスタンス.カレント描画エンティティ.メタデータ;
		}
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getBrightness(int i, int j, int k, int l) {
		return 15;//ba.getBrightness(i, j, k, l);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getLightBrightness(int i, int j, int k) {
		return 15 << 20 | 15 << 4;//ba.getLightBrightness(i, j, k);
	}

	@Override
	public Material getBlockMaterial(int i, int j, int k) {
		return ba.getBlockMaterial(i, j, k);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isBlockOpaqueCube(int i, int j, int k) {
		clientBlockData e = ssMineShipMOD.インスタンス.カレント描画エンティティ;
		if(j == e.mainとの相対座標Y+1)
		{
			if(!e.上見えている)
				return true;
		}
		if(j == e.mainとの相対座標Y-1)
		{
			if(!e.下見えている)
				return true;
		}
		if(i == e.mainとの相対座標X+1)
		{
			if(!e.右見えている)
				return true;
		}
		if(i == e.mainとの相対座標X-1)
		{
			if(!e.左見えている)
				return true;
		}
		if(k == e.mainとの相対座標Z+1)
		{
			if(!e.前見えている)
				return true;
		}
		if(k == e.mainとの相対座標Z-1)
		{
			if(!e.後見えている)
				return true;
		}
		return false;
	}

	@Override
	public boolean isBlockNormalCube(int i, int j, int k) {
		return ba.isBlockNormalCube(i, j, k);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isAirBlock(int i, int j, int k) {
		return ba.isAirBlock(i, j, k);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BiomeGenBase getBiomeGenForCoords(int i, int j) {
		return ba.getBiomeGenForCoords(i, j);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getHeight() {
		return ba.getHeight();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean extendedLevelsInChunkCache() {
		return ba.extendedLevelsInChunkCache();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean doesBlockHaveSolidTopSurface(int i, int j, int k) {
		return ba.doesBlockHaveSolidTopSurface(i, j, k);
	}

	@Override
	public Vec3Pool getWorldVec3Pool() {
		return ba.getWorldVec3Pool();
	}

	@Override
	public int isBlockProvidingPowerTo(int i, int j, int k, int l) {
		return ba.isBlockProvidingPowerTo(i, j, k, l);
	}

}
