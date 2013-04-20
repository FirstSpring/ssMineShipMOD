package net.minecraft.ssMineShipMOD;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class mineshipteleporter extends Teleporter {

   public mineshipteleporter(WorldServer par1WorldServer) {
      super(par1WorldServer);
   }

   @Override
   public void placeInPortal(Entity par1Entity, double par2, double par4, double par6, float par8) {
      int x = (int)par1Entity.posX;
      int y = (int)par1Entity.posY;
      int z = (int)par1Entity.posZ;
      for(int j = y; j < 255; ++j) {
         if(par1Entity.worldObj.getBlockId(x, j - 1, z) != 0) continue;
         if(par1Entity.worldObj.getBlockId(x, j, z) != 0) continue;
         if(par1Entity.worldObj.getBlockId(x, j + 1, z) != 0) continue;
         par1Entity.setLocationAndAngles(x, j, z, 0, 0);
         break;
      }
   }
}