package net.minecraft.ssMineShipMOD;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class debug_Itemtue extends Item
{
	public debug_Itemtue(int par1)
	{
		super(par1);
		this.maxStackSize = 1;
		this.setCreativeTab(CreativeTabs.tabCombat);
	}
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer entityPlayer)
	{
		if (entityPlayer.isRiding() == false && entityPlayer instanceof EntityPlayerMP) {

			EntityPlayerMP thePlayer = (EntityPlayerMP) entityPlayer;
			thePlayer.timeUntilPortal = 10;

			if (entityPlayer.isSneaking()) {
				thePlayer.mcServer.getConfigurationManager()
				.transferPlayerToDimension(thePlayer, 0, new mineshipteleporter(thePlayer.mcServer.worldServerForDimension(0)));
			}
			else if(!entityPlayer.isSneaking()){
				thePlayer.mcServer.getConfigurationManager()
				.transferPlayerToDimension(thePlayer,ssMineShipMOD.インスタンス.データ用ディメンジョン, new mineshipteleporter(thePlayer.mcServer.worldServerForDimension(ssMineShipMOD.インスタンス.データ用ディメンジョン)));
			}
		}
		return par1ItemStack;
	}
}
