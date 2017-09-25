package dalapo.factech;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dalapo.factech.helper.FacMathHelper;
import dalapo.factech.helper.Logger;
import dalapo.factech.reference.NameList;
import dalapo.factech.tileentity.specialized.TileEntityDisassembler;

@Mod(modid=NameList.MODID, name=NameList.MODNAME, version=NameList.VERSION)
public class FactoryTech {
	public static final boolean DEBUG_PACKETS = false;
	public static final Random random = new Random();
	@Instance
	public static FactoryTech instance;
	
	@SidedProxy(clientSide="dalapo.factech.ClientProxy", serverSide="dalapo.factech.ServerProxy")
	public static CommonProxy proxy;
	
	static {
		FluidRegistry.enableUniversalBucket();
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent evt)
	{
		proxy.preInit(evt);
		MinecraftForge.EVENT_BUS.register(FacTechEventManager.instance);
		MinecraftForge.EVENT_BUS.register(proxy);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent evt)
	{
		proxy.init(evt);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent evt)
	{
		proxy.postInit(evt);
	}
}