package de.scribble.lp.TASTools.mixin;

import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.scribble.lp.TASTools.CommonProxy;
import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.savestates.SavestateEvents;
import de.scribble.lp.TASTools.savestates.SavestatePacket;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiShareToLan;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;

@Mixin(GuiIngameMenu.class)
public class MixinGuiIngameMenu extends GuiScreen{
	@Shadow
    private int saveStep;
    
	@Inject(method="initGui", at=@At("HEAD"), cancellable = true)
	public void redoInitGui(CallbackInfo ci) {
		if(SavestateEvents.savestatepauseenabled) {
			this.saveStep = 0;
	        this.buttonList.clear();
	        int i = -16;
	        int j = 98;
	        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 144 + -16, I18n.format("menu.returnToMenu")));
	        this.setFocused(true);
	        if (!this.mc.isIntegratedServerRunning())
	        {
	            (this.buttonList.get(0)).displayString = I18n.format("menu.disconnect");
	        }
	        this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 + -16, I18n.format("menu.returnToGame")));
	        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + -16, 98, 20, I18n.format("menu.options")));
	        this.buttonList.add(new GuiButton(12, this.width / 2 + 2, this.height / 4 + 120 + i, 98, 20, I18n.format("fml.menu.modoptions")));
	        GuiButton guibutton = this.addButton(new GuiButton(7, this.width / 2 - 100, this.height / 4 + 72 + -16, 200, 20, I18n.format("menu.shareToLan", new Object[0])));
	        guibutton.enabled = this.mc.isSingleplayer() && !this.mc.getIntegratedServer().getPublic();
	        this.addButton(new GuiButton(13, this.width / 2 - 100, this.height / 4 + 96 + -16, 200, 20, I18n.format("gui.savestate.ingamemenu")));
	        this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 48 + -16, 98, 20, I18n.format("gui.advancements")));
	        this.buttonList.add(new GuiButton(6, this.width / 2 + 2, this.height / 4 + 48 + -16, 98, 20, I18n.format("gui.stats")));
		}else {
	        this.saveStep = 0;
	        this.buttonList.clear();
	        int i = -16;
	        int j = 98;
	        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + -16, I18n.format("menu.returnToMenu")));
	
	        if (!this.mc.isIntegratedServerRunning())
	        {
	            (this.buttonList.get(0)).displayString = I18n.format("menu.disconnect");
	        }
	
	        this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 + -16, I18n.format("menu.returnToGame")));
	        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + -16, 98, 20, I18n.format("menu.options")));
	        this.buttonList.add(new GuiButton(12, this.width / 2 + 2, this.height / 4 + 96 + i, 98, 20, I18n.format("fml.menu.modoptions")));
	        GuiButton guibutton = this.addButton(new GuiButton(7, this.width / 2 - 100, this.height / 4 + 72 + -16, 200, 20, I18n.format("menu.shareToLan", new Object[0])));
	        guibutton.enabled = this.mc.isSingleplayer() && !this.mc.getIntegratedServer().getPublic();
	        this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 48 + -16, 98, 20, I18n.format("gui.advancements")));
	        this.buttonList.add(new GuiButton(6, this.width / 2 + 2, this.height / 4 + 48 + -16, 98, 20, I18n.format("gui.stats")));
		}
		ci.cancel();
	}
	@Inject(method="actionPerformed", at=@At("HEAD"), cancellable = true)
	public void redoActionPerformed(GuiButton button, CallbackInfo ci) throws IOException{
		if(SavestateEvents.savestatepauseenabled) {
			switch (button.id)
	        {
	            case 0:
	                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
	                break;
	            case 1:
	                boolean flag = this.mc.isIntegratedServerRunning();
	                boolean flag1 = this.mc.isConnectedToRealms();
	                button.enabled = false;
	                this.mc.world.sendQuittingDisconnectingPacket();
	                this.mc.loadWorld((WorldClient)null);

	                if (flag)
	                {
	                    this.mc.displayGuiScreen(new GuiMainMenu());
	                }
	                else if (flag1)
	                {
	                    RealmsBridge realmsbridge = new RealmsBridge();
	                    realmsbridge.switchToRealms(new GuiMainMenu());
	                }
	                else
	                {
	                    this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
	                }

	            case 2:
	            case 3:
	            default:
	                break;
	            case 4:
	                this.mc.displayGuiScreen((GuiScreen)null);
	                this.mc.setIngameFocus();
	                break;
	            case 5:
	                if (this.mc.player != null)
	                this.mc.displayGuiScreen(new GuiScreenAdvancements(this.mc.player.connection.getAdvancementManager()));
	                break;
	            case 6:
	                if (this.mc.player != null)
	                this.mc.displayGuiScreen(new GuiStats(this, this.mc.player.getStatFileWriter()));
	                break;
	            case 7:
	                this.mc.displayGuiScreen(new GuiShareToLan(this));
	                break;
	            case 12:
	                net.minecraftforge.fml.client.FMLClientHandler.instance().showInGameModOptions(new GuiIngameMenu());
	                break;
	            case 13:
					if (mc.player.canUseCommand(2, "savestate")) {
						ModLoader.NETWORK.sendToServer(new SavestatePacket(true));
					} else {
						CommonProxy.logger.info("You don't have the required permissions to use the savestate button!");
					}
					break;
	        }
		}else {
			switch (button.id)
	        {
	            case 0:
	                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
	                break;
	            case 1:
	                boolean flag = this.mc.isIntegratedServerRunning();
	                boolean flag1 = this.mc.isConnectedToRealms();
	                button.enabled = false;
	                this.mc.world.sendQuittingDisconnectingPacket();
	                this.mc.loadWorld((WorldClient)null);

	                if (flag)
	                {
	                    this.mc.displayGuiScreen(new GuiMainMenu());
	                }
	                else if (flag1)
	                {
	                    RealmsBridge realmsbridge = new RealmsBridge();
	                    realmsbridge.switchToRealms(new GuiMainMenu());
	                }
	                else
	                {
	                    this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
	                }

	            case 2:
	            case 3:
	            default:
	                break;
	            case 4:
	                this.mc.displayGuiScreen((GuiScreen)null);
	                this.mc.setIngameFocus();
	                break;
	            case 5:
	                if (this.mc.player != null)
	                this.mc.displayGuiScreen(new GuiScreenAdvancements(this.mc.player.connection.getAdvancementManager()));
	                break;
	            case 6:
	                if (this.mc.player != null)
	                this.mc.displayGuiScreen(new GuiStats(this, this.mc.player.getStatFileWriter()));
	                break;
	            case 7:
	                this.mc.displayGuiScreen(new GuiShareToLan(this));
	                break;
	            case 12:
	                net.minecraftforge.fml.client.FMLClientHandler.instance().showInGameModOptions((GuiIngameMenu)(Object)this);
	                break;
	        }
		}
		ci.cancel();
	}
}
