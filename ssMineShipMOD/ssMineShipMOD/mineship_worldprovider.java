package net.minecraft.ssMineShipMOD;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderFlat;

public class mineship_worldprovider extends WorldProvider{
	
	@Override
	public String getDimensionName() {
		return "mineshipdata";
	}
	
	public IChunkProvider createChunkGenerator()//岩盤だけ
    {
        return new ChunkProviderFlat(worldObj, worldObj.getSeed(), worldObj.getWorldInfo().isMapFeaturesEnabled(), "2;7;1;");
    }
}
