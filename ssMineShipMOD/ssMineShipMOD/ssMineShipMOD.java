package net.minecraft.ssMineShipMOD;

import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "ssMineShipMod", name="飛行船?MOD", version="151_0")
@NetworkMod(clientSideRequired = true ,serverSideRequired = true,channels = {"sskeych","描画要求","描画要求への返事","描画要求への返事完了"},packetHandler = packethandler.class)
public class ssMineShipMOD {	
	public HashMap<posXYZ,serverDataBlock> 座標toブロックデータ = new HashMap<posXYZ,serverDataBlock>();
	
	public EntityMainBlock カレントエンティティ;
	public serverDataBlock カレントcolエンティティ;
	public clientBlockData カレント描画エンティティ;
	public mineship_worldClient 偽クライアントワールド;
	
	public boolean ブロックを更新しない = false;
	
	public int 一番大きいX座標 = 0;
	public HashMap<Integer,Integer> 構築済み = new HashMap<Integer,Integer>();
	public HashMap<EntityPlayer,mainBlockTileEntity> 使っている人一覧 = new HashMap<EntityPlayer,mainBlockTileEntity>();
	public HashMap<EntityPlayer,Integer> アイテムの半径 = new HashMap<EntityPlayer,Integer>();
	public HashMap<EntityPlayer,Integer> 遅延 = new HashMap<EntityPlayer,Integer>();
	
	public boolean ヌルを返す = false;
	
	public boolean エンティティをスポーンさせない = false;
	
	public HashMap<EntityMainBlock,EntityPlayer> オーナー = new HashMap<EntityMainBlock,EntityPlayer>();
	
	public HashMap<Integer,HashSet<clientBlockData>> クライアント描画用データ = new HashMap<Integer,HashSet<clientBlockData>>();
	public HashMap<Integer,HashSet<serverDataBlock>> サーバー描画用データ = new HashMap<Integer,HashSet<serverDataBlock>>();
	
	//IDs
	public int EntityBlockEntityID;
	public int EntityMainBlockEntityID;
	
	public int mainBlockID;
	
	public int debug_ItemtueID;
	public int ItemceID;
	public int Item_ceeID;
	public int データ用ディメンジョン;
	
	//items
	public Item debug_Itemtue;
	public Item Itemce;
	public Item Item_cee;
	//Blocks
	public Block mainBlock;
	
	HashMap<String,byte[]> 入力状態 = new HashMap<String,byte[]>();

	@SidedProxy(clientSide = "net.minecraft.ssmineship.clientproxy", serverSide = "net.minecraft.ssmineship.serverproxy")
	public static serverproxy プロキシ;

	@Mod.Instance("ssMineShipMod")
	public static ssMineShipMOD インスタンス;

	@Mod.PreInit
	public void 初期化前処理(FMLPreInitializationEvent event)
	{
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
		try
		{
			cfg.load();
			this.EntityBlockEntityID = cfg.getItem("EntityID", "EntityBlock", 360).getInt();
			this.EntityMainBlockEntityID = cfg.getItem("EntityID", "EntityMainBlock", 361).getInt();
			this.データ用ディメンジョン = cfg.get("ディメンジョンID", "mineshipdata", 30).getInt();
			this.debug_ItemtueID = cfg.get("アイテムID", "debug_Itemtue", "420").getInt();
			this.ItemceID = cfg.get("アイテムID", "Itemce", "421").getInt();
			this.mainBlockID = cfg.get("ブロックID", "mainBlock", "620").getInt();
			this.Item_ceeID = cfg.get("アイテムID", "itemcee", "422").getInt();
		}
		catch (Exception e)
		{
			FMLLog.log(Level.SEVERE, e, "コンフィグでエラー");
		}
		finally
		{
			cfg.save();
		}
	}

	@Mod.Init
	public void 初期化(FMLInitializationEvent event) {
		
		MinecraftForge.EVENT_BUS.register(new worldevent());
		
		GameRegistry.registerTileEntity(mainBlockTileEntity.class, "main");
		
		DimensionManager.registerProviderType(this.データ用ディメンジョン, mineship_worldprovider.class, true);
		DimensionManager.registerDimension(this.データ用ディメンジョン, this.データ用ディメンジョン);

		EntityRegistry.registerGlobalEntityID(EntityMainBlock.class, "EntityMainBlock", this.EntityMainBlockEntityID);
		EntityRegistry.registerModEntity(EntityMainBlock.class, "EntityMainBlock", 0, this, 800, 3,false);
		
		EntityRegistry.registerGlobalEntityID(EntityBlockCol.class, "EntityBlock", this.EntityBlockEntityID);
		EntityRegistry.registerModEntity(EntityBlockCol.class, "EntityBlock", 1, this, 80, 60000,false);
		
		this.debug_Itemtue = (new debug_Itemtue(this.debug_ItemtueID)).setUnlocalizedName("右クリック用のアイテム");
		LanguageRegistry.addName(debug_Itemtue, "右クリック用のアイテム");
		LanguageRegistry.instance().addNameForObject(debug_Itemtue, "ja_JP", "右クリック用のアイテム");
		
		this.Itemce = (new Itemce(this.ItemceID)).setUnlocalizedName("itemce");
		LanguageRegistry.addName(this.Itemce, "Itemce");
		LanguageRegistry.instance().addNameForObject(this.Itemce, "ja_JP", "アイテム");
		
		this.mainBlock = (new mainBlock(this.mainBlockID,Material.iron)).setUnlocalizedName("mainBlock");
		GameRegistry.registerBlock(mainBlock, "main");
		LanguageRegistry.addName(mainBlock, "メイン");
		
		プロキシ.登録();
	}
}
