package de.scribble.lp.TASTools.cape;

import java.util.HashMap;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CustomPlayerInfo {
	public static HashMap<String, Boolean> playersCape = new HashMap();
	private static final ResourceLocation bottlecape = new ResourceLocation("tastools:textures/capes/bottlecape.png");
	private static final String cacheLocation = "capestt/";
	
	@SubscribeEvent
	  public void onPlayerJoin(EntityJoinWorldEvent event)
	  {
	    if(event.getEntity() instanceof EntityPlayer)
	    {
	      EntityPlayer player = (EntityPlayer)event.getEntity();
	      String uuid = player.getGameProfile().toString();
	      CapeDownloader.download(uuid, cacheLocation);
	    }
	  }
	
	public static ResourceLocation getResourceLocation(EntityLivingBase entitylivingbaseIn){
		String playerUUID = entitylivingbaseIn.getUniqueID().toString();
		if(hasCape(entitylivingbaseIn)) {
			return new ResourceLocation(cacheLocation+CapeDownloader.getCapeName(playerUUID));
		}else {
			return bottlecape;
		}
//		return new ResourceLocation(cacheLocation+CapeDownloader.getCapeName(playerUUID));
	}
	
	private static Boolean hasCape(EntityLivingBase entitylivingbaseIn)
	  {
	    Boolean hashMapResult = (Boolean)playersCape.get(((EntityPlayer) entitylivingbaseIn).getGameProfile().getId().toString().replace("-", ""));
	    if (hashMapResult == null) {
	      return false;
	    }
	    return hashMapResult;
	  }
}