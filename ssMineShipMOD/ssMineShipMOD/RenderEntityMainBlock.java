package net.minecraft.ssMineShipMOD;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;

public class RenderEntityMainBlock  extends Render
{
	ArrayList<Integer> 送ったの = new ArrayList<Integer>();

	@Override
	public void doRender(Entity entity, double par2, double par4, double par6,float par8, float par9) {

		if(clientproxy.rb == null)
		{
			clientproxy.rwa = new renderBlockAccess( FMLClientHandler.instance().getClient().renderGlobal.globalRenderBlocks.blockAccess);
			clientproxy.rb = new RenderBlocks(clientproxy.rwa);
		}

		EntityMainBlock e = (EntityMainBlock)entity;

		GL11.glPushMatrix();
		GL11.glTranslatef((float)par2, (float)par4+0.5F, (float)par6);
		GL11.glRotatef(-par8, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-entity.rotationPitch, 1.0F, 0.0F, 0.0F);
		this.loadTexture("/terrain.png");

		clientproxy.rb.renderBlockAsItem(Block.blocksList[ssMineShipMOD.インスタンス.mainBlockID],0,1);
		GL11.glPopMatrix();

		Iterator i = null;
		if(ssMineShipMOD.インスタンス.クライアント描画用データ.containsKey(e.entityId)&&ssMineShipMOD.インスタンス.構築済み.containsKey(e.entityId)&&ssMineShipMOD.インスタンス.構築済み.get(e.entityId) == 0)
		{
			i = ssMineShipMOD.インスタンス.クライアント描画用データ.get(e.entityId).iterator();
			描画データ用意(e,i);
		}
		else{
			if(!送ったの.contains(e.entityId))
			{
				PacketDispatcher.sendPacketToServer(new Packet250CustomPayload("描画要求",バイトへ(e.entityId)));
				送ったの.add(e.entityId);
				return;
			}
		}

		if(ssMineShipMOD.インスタンス.構築済み.containsKey(e.entityId)&&ssMineShipMOD.インスタンス.構築済み.get(e.entityId)==1)
		{
			GL11.glPushMatrix();
			GL11.glTranslatef((float)par2, (float)par4+0.5F, (float)par6);
			GL11.glRotatef(-par8, 0.0F, 1.0F, 0.0F);
			i = ssMineShipMOD.インスタンス.クライアント描画用データ.get(e.entityId).iterator();
			while(i.hasNext())
			{
				clientBlockData eb = (clientBlockData)i.next();
				if(eb.te != null)
				{
					eb.te.updateEntity();
					TileEntityRenderer.instance.renderTileEntityAt(eb.te,eb.mainとの相対座標X-0.5F,eb.mainとの相対座標Y-0.5F,eb.mainとの相対座標Z-0.5F, par9);
				}
			}
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glTranslatef((float)par2, (float)par4+0.5F, (float)par6);
			GL11.glRotatef(-par8, 0.0F, 1.0F, 0.0F);
			GL11.glCallList(e.entityId);
			GL11.glPopMatrix();
		}
	}

	public void 描画データ用意(Entity e,Iterator i)
	{
		GL11.glNewList(e.entityId, GL11.GL_COMPILE);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)-0.5F, (float)-0.5F, (float)-0.5F);
		Tessellator.instance.startDrawingQuads();
		this.loadTexture("/terrain.png");
		while(i.hasNext())
		{
			clientBlockData eb = (clientBlockData)i.next();
			ssMineShipMOD.インスタンス.カレント描画エンティティ = eb;
			if(eb.ブロックID != 0)
			{
				if(Block.blocksList[eb.ブロックID].getRenderType() == 4)
					this.水だけ特別(Block.blocksList[eb.ブロックID],eb.mainとの相対座標X,eb.mainとの相対座標Y,eb.mainとの相対座標Z);
				else clientproxy.rb.renderBlockByRenderType(Block.blocksList[eb.ブロックID],eb.mainとの相対座標X,eb.mainとの相対座標Y,eb.mainとの相対座標Z);
			}
		}
		Class<Tessellator> c  = Tessellator.class;
		Field a = null;
		try {
			a = c.getDeclaredField("hasBrightness");
		}catch (NoSuchFieldException e1) {
			try {
				a = c.getDeclaredField("field_78414_p");
			}catch (NoSuchFieldException e2) {
				e2.printStackTrace();
			}
		}

		if(a != null)
		{
			a.setAccessible(true);
			try {
				a.setBoolean(Tessellator.instance, false);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		Tessellator.instance.draw();
		GL11.glPopMatrix();
		GL11.glEndList();
		ssMineShipMOD.インスタンス.構築済み.put(e.entityId,1);
	}

	public boolean 水だけ特別(Block par1Block, int par2, int par3, int par4)
	{
		par1Block.setBlockBoundsBasedOnState(clientproxy.rwa, par2, par3, par4);
		clientproxy.rb.setRenderBoundsFromBlock(par1Block);

		Tessellator tessellator = Tessellator.instance;
		int l = par1Block.colorMultiplier(clientproxy.rwa, par2, par3, par4);
		float f = (float)(l >> 16 & 255) / 255.0F;
		float f1 = (float)(l >> 8 & 255) / 255.0F;
		float f2 = (float)(l & 255) / 255.0F;
		boolean flag = par1Block.shouldSideBeRendered(clientproxy.rwa, par2, par3 + 1, par4, 1);
		boolean flag1 = par1Block.shouldSideBeRendered(clientproxy.rwa, par2, par3 - 1, par4, 0);
		boolean[] aboolean = new boolean[] {par1Block.shouldSideBeRendered(clientproxy.rwa, par2, par3, par4 - 1, 2), par1Block.shouldSideBeRendered(clientproxy.rwa, par2, par3, par4 + 1, 3), par1Block.shouldSideBeRendered(clientproxy.rwa, par2 - 1, par3, par4, 4), par1Block.shouldSideBeRendered(clientproxy.rwa, par2 + 1, par3, par4, 5)};

		if (!flag && !flag1 && !aboolean[0] && !aboolean[1] && !aboolean[2] && !aboolean[3])
		{
			return false;
		}
		else
		{
			boolean flag2 = false;
			float f3 = 0.5F;
			float f4 = 1.0F;
			float f5 = 0.8F;
			float f6 = 0.6F;
			double d0 = 0.0D;
			double d1 = 1.0D;
			Material material = par1Block.blockMaterial;
			int i1 = clientproxy.rwa.getBlockMetadata(par2, par3, par4);
			double d2 = 1;
			double d3 = 1;
			double d4 = 1;
			double d5 = 1;
			double d6 = 0.0010000000474974513D;
			float f7;
			float f8;

			if (clientproxy.rb.renderAllFaces || flag)
			{
				flag2 = true;
				Icon icon = clientproxy.rb.getBlockIconFromSideAndMetadata(par1Block, 1, i1);
				float f9 = (float)BlockFluid.getFlowDirection(clientproxy.rwa, par2, par3, par4, material);

				if (f9 > -999.0F)
				{
					icon = clientproxy.rb.getBlockIconFromSideAndMetadata(par1Block, 2, i1);
				}

				d2 -= d6;
				d3 -= d6;
				d4 -= d6;
				d5 -= d6;
				double d7;
				double d8;
				double d9;
				double d10;
				double d11;
				double d12;
				double d13;
				double d14;

				if (f9 < -999.0F)
				{
					d8 = (double)icon.getInterpolatedU(0.0D);
					d12 = (double)icon.getInterpolatedV(0.0D);
					d7 = d8;
					d11 = (double)icon.getInterpolatedV(16.0D);
					d10 = (double)icon.getInterpolatedU(16.0D);
					d14 = d11;
					d9 = d10;
					d13 = d12;
				}
				else
				{
					f8 = MathHelper.sin(f9) * 0.25F;
					f7 = MathHelper.cos(f9) * 0.25F;
					d8 = (double)icon.getInterpolatedU((double)(8.0F + (-f7 - f8) * 16.0F));
					d12 = (double)icon.getInterpolatedV((double)(8.0F + (-f7 + f8) * 16.0F));
					d7 = (double)icon.getInterpolatedU((double)(8.0F + (-f7 + f8) * 16.0F));
					d11 = (double)icon.getInterpolatedV((double)(8.0F + (f7 + f8) * 16.0F));
					d10 = (double)icon.getInterpolatedU((double)(8.0F + (f7 + f8) * 16.0F));
					d14 = (double)icon.getInterpolatedV((double)(8.0F + (f7 - f8) * 16.0F));
					d9 = (double)icon.getInterpolatedU((double)(8.0F + (f7 - f8) * 16.0F));
					d13 = (double)icon.getInterpolatedV((double)(8.0F + (-f7 - f8) * 16.0F));
				}

				tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(clientproxy.rwa, par2, par3, par4));
				f8 = 1.0F;
				tessellator.setColorOpaque_F(f4 * f8 * f, f4 * f8 * f1, f4 * f8 * f2);
				tessellator.addVertexWithUV((double)(par2 + 0), (double)par3 + d2, (double)(par4 + 0), d8, d12);
				tessellator.addVertexWithUV((double)(par2 + 0), (double)par3 + d3, (double)(par4 + 1), d7, d11);
				tessellator.addVertexWithUV((double)(par2 + 1), (double)par3 + d4, (double)(par4 + 1), d10, d14);
				tessellator.addVertexWithUV((double)(par2 + 1), (double)par3 + d5, (double)(par4 + 0), d9, d13);
			}

			if (clientproxy.rb.renderAllFaces || flag1)
			{
				tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(clientproxy.rwa, par2, par3 - 1, par4));
				float f10 = 1.0F;
				tessellator.setColorOpaque_F(f3 * f10, f3 * f10, f3 * f10);
				clientproxy.rb.renderBottomFace(par1Block, (double)par2, (double)par3 + d6, (double)par4, clientproxy.rb.getBlockIconFromSide(par1Block, 0));
				flag2 = true;
			}

			for (int j1 = 0; j1 < 4; ++j1)
			{
				int k1 = par2;
				int l1 = par4;

				if (j1 == 0)
				{
					l1 = par4 - 1;
				}

				if (j1 == 1)
				{
					++l1;
				}

				if (j1 == 2)
				{
					k1 = par2 - 1;
				}

				if (j1 == 3)
				{
					++k1;
				}

				Icon icon1 = clientproxy.rb.getBlockIconFromSideAndMetadata(par1Block, j1 + 2, i1);

				if (clientproxy.rb.renderAllFaces || aboolean[j1])
				{
					double d15;
					double d16;
					double d17;
					double d18;
					double d19;
					double d20;

					if (j1 == 0)
					{
						d15 = d2;
						d17 = d5;
						d16 = (double)par2;
						d18 = (double)(par2 + 1);
						d19 = (double)par4 + d6;
						d20 = (double)par4 + d6;
					}
					else if (j1 == 1)
					{
						d15 = d4;
						d17 = d3;
						d16 = (double)(par2 + 1);
						d18 = (double)par2;
						d19 = (double)(par4 + 1) - d6;
						d20 = (double)(par4 + 1) - d6;
					}
					else if (j1 == 2)
					{
						d15 = d3;
						d17 = d2;
						d16 = (double)par2 + d6;
						d18 = (double)par2 + d6;
						d19 = (double)(par4 + 1);
						d20 = (double)par4;
					}
					else
					{
						d15 = d5;
						d17 = d4;
						d16 = (double)(par2 + 1) - d6;
						d18 = (double)(par2 + 1) - d6;
						d19 = (double)par4;
						d20 = (double)(par4 + 1);
					}

					flag2 = true;
					float f11 = icon1.getInterpolatedU(0.0D);
					f8 = icon1.getInterpolatedU(8.0D);
					f7 = icon1.getInterpolatedV((1.0D - d15) * 16.0D * 0.5D);
					float f12 = icon1.getInterpolatedV((1.0D - d17) * 16.0D * 0.5D);
					float f13 = icon1.getInterpolatedV(8.0D);
					tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(clientproxy.rwa, k1, par3, l1));
					float f14 = 1.0F;

					if (j1 < 2)
					{
						f14 *= f5;
					}
					else
					{
						f14 *= f6;
					}

					tessellator.setColorOpaque_F(f4 * f14 * f, f4 * f14 * f1, f4 * f14 * f2);
					tessellator.addVertexWithUV(d16, (double)par3 + d15, d19, (double)f11, (double)f7);
					tessellator.addVertexWithUV(d18, (double)par3 + d17, d20, (double)f8, (double)f12);
					tessellator.addVertexWithUV(d18, (double)(par3 + 0), d20, (double)f8, (double)f13);
					tessellator.addVertexWithUV(d16, (double)(par3 + 0), d19, (double)f11, (double)f13);
				}
			}
			return flag2;
		}
	}

	static byte[] バイトへ(int a) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(25);
		DataOutputStream dos = new DataOutputStream(bos);

		try {

			dos.writeInt(a);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}

}
