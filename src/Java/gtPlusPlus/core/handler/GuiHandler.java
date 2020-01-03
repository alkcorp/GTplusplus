package gtPlusPlus.core.handler;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.machine.Machine_SuperJukebox.TileEntitySuperJukebox;
import gtPlusPlus.core.container.Container_BackpackBase;
import gtPlusPlus.core.container.Container_CircuitProgrammer;
import gtPlusPlus.core.container.Container_DecayablesChest;
import gtPlusPlus.core.container.Container_FishTrap;
import gtPlusPlus.core.container.Container_Grindle;
import gtPlusPlus.core.container.Container_ModularityTable;
import gtPlusPlus.core.container.Container_PestKiller;
import gtPlusPlus.core.container.Container_ProjectTable;
import gtPlusPlus.core.container.Container_RoundRobinator;
import gtPlusPlus.core.container.Container_SuperJukebox;
import gtPlusPlus.core.container.Container_TradeTable;
import gtPlusPlus.core.container.Container_Workbench;
import gtPlusPlus.core.container.Container_WorkbenchAdvanced;
import gtPlusPlus.core.container.box.LunchBoxContainer;
import gtPlusPlus.core.container.box.MagicBagContainer;
import gtPlusPlus.core.container.box.ToolBoxContainer;
import gtPlusPlus.core.gui.beta.Gui_ID_Registry;
import gtPlusPlus.core.gui.beta.MU_GuiId;
import gtPlusPlus.core.gui.item.GuiBaseBackpack;
import gtPlusPlus.core.gui.item.GuiBaseGrindle;
import gtPlusPlus.core.gui.item.box.LunchBoxGui;
import gtPlusPlus.core.gui.item.box.MagicBagGui;
import gtPlusPlus.core.gui.item.box.ToolBoxGui;
import gtPlusPlus.core.gui.machine.GUI_CircuitProgrammer;
import gtPlusPlus.core.gui.machine.GUI_DecayablesChest;
import gtPlusPlus.core.gui.machine.GUI_FishTrap;
import gtPlusPlus.core.gui.machine.GUI_ModularityTable;
import gtPlusPlus.core.gui.machine.GUI_PestKiller;
import gtPlusPlus.core.gui.machine.GUI_ProjectTable;
import gtPlusPlus.core.gui.machine.GUI_RoundRobinator;
import gtPlusPlus.core.gui.machine.GUI_SuperJukebox;
import gtPlusPlus.core.gui.machine.GUI_TradeTable;
import gtPlusPlus.core.gui.machine.GUI_Workbench;
import gtPlusPlus.core.gui.machine.GUI_WorkbenchAdvanced;
import gtPlusPlus.core.interfaces.IGuiManager;
import gtPlusPlus.core.inventories.BaseInventoryBackpack;
import gtPlusPlus.core.inventories.BaseInventoryGrindle;
import gtPlusPlus.core.inventories.box.LunchBoxInventory;
import gtPlusPlus.core.inventories.box.MagicBagInventory;
import gtPlusPlus.core.inventories.box.ToolBoxInventory;
import gtPlusPlus.core.tileentities.base.TileEntityBase;
import gtPlusPlus.core.tileentities.general.TileEntityCircuitProgrammer;
import gtPlusPlus.core.tileentities.general.TileEntityDecayablesChest;
import gtPlusPlus.core.tileentities.general.TileEntityFishTrap;
import gtPlusPlus.core.tileentities.machines.TileEntityModularityTable;
import gtPlusPlus.core.tileentities.machines.TileEntityPestKiller;
import gtPlusPlus.core.tileentities.machines.TileEntityProjectTable;
import gtPlusPlus.core.tileentities.machines.TileEntityRoundRobinator;
import gtPlusPlus.core.tileentities.machines.TileEntityTradeTable;
import gtPlusPlus.core.tileentities.machines.TileEntityWorkbench;
import gtPlusPlus.core.tileentities.machines.TileEntityWorkbenchAdvanced;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {

	public static final int GUI1 = 0; // Project Table
	public static final int GUI2 = 1; // Helium Generator
	public static final int GUI3 = 2; // BackpackHandler
	public static final int GUI4 = 3; // Workbench
	public static final int GUI5 = 4; // Workbench Adv
	public static final int GUI6 = 5; // Fish trap
	public static final int GUI7 = 6; // Trade table
	public static final int GUI8 = 7; // Circuit Programmer
	public static final int GUI9 = 8; // Grindle
	public static final int GUI10 = 9; // Universal Toolbox
	public static final int GUI11 = 10; // Auto Lunchbox
	public static final int GUI12 = 11; // Bag for Magic Tools
	public static final int GUI13 = 12; // Decayables Chest
	public static final int GUI14 = 13; // Super Jukebox
	public static final int GUI15 = 14; // Pest Killer
	public static final int GUI16 = 15; // Round-Robinator

	public static void init() {

		Logger.INFO("Registering GUIs.");
		NetworkRegistry.INSTANCE.registerGuiHandler(GTplusplus.instance, new GuiHandler());
		// Register GuiHandler
		// NetworkRegistry.INSTANCE.registerGuiHandler(GTplusplus.instance, new
		// GuiHandler());
	}

	@Override // ContainerModTileEntity
	public Object getServerGuiElement(final int ID, final EntityPlayer player, final World world, final int x,
			final int y, final int z) {
		final TileEntity te = world.getTileEntity(x, y, z);

		if (te != null) {
			if (ID == GUI1) {
				return new Container_ProjectTable(player.inventory, (TileEntityProjectTable) te);
			} else if (ID == GUI2) {
				return new Container_ModularityTable(player.inventory, (TileEntityModularityTable) te);
			}
		}

		if (ID == GUI3) {
			// Use the player's held item to create the inventory
			return new Container_BackpackBase(player, player.inventory,
					new BaseInventoryBackpack(player.getHeldItem()));
		}

		if (te != null) {
			if (ID == GUI4) {
				return new Container_Workbench(player.inventory, (TileEntityWorkbench) te);
			} else if (ID == GUI5) {
				Logger.INFO("sad");
				return new Container_WorkbenchAdvanced(player.inventory, (TileEntityWorkbenchAdvanced) te);

			} else if (ID == GUI6) {
				return new Container_FishTrap(player.inventory, (TileEntityFishTrap) te);
			} else if (ID == GUI7) {
				return new Container_TradeTable(player.inventory, (TileEntityTradeTable) te);
			} else if (ID == GUI8) {
				return new Container_CircuitProgrammer(player.inventory, (TileEntityCircuitProgrammer) te);
			} else if (ID == GUI13) {
				return new Container_DecayablesChest(player.inventory, (TileEntityDecayablesChest) te);
			} else if (ID == GUI14) {
				return new Container_SuperJukebox(player.inventory, (TileEntitySuperJukebox) te);
			} else if (ID == GUI15) {
				return new Container_PestKiller(player.inventory, (TileEntityPestKiller) te);
			}  else if (ID == GUI16) {
				return new Container_RoundRobinator(player.inventory, (TileEntityRoundRobinator) te);
			} 
		}

		if (ID == GUI9) {
			return new Container_Grindle(player, player.inventory, new BaseInventoryGrindle(player.getHeldItem()));
		}
		// Tool, lunch, magic
		if (ID == GUI10) {
			return new ToolBoxContainer(player, player.inventory, new ToolBoxInventory(player.getHeldItem()));
		}
		if (ID == GUI11) {
			return new LunchBoxContainer(player, player.inventory, new LunchBoxInventory(player.getHeldItem()));
		}
		if (ID == GUI12) {
			return new MagicBagContainer(player, player.inventory, new MagicBagInventory(player.getHeldItem()));
		}

		return null;
	}

	@Override // GuiModTileEntity
	public Object getClientGuiElement(final int ID, final EntityPlayer player, final World world, final int x,
			final int y, final int z) {
		Logger.WARNING("getClientGuiElement Called by: " + player + ", in world: " + player.dimension + " at x:" + x
				+ ", y:" + y + ", z:" + z + ".");
		final TileEntity te = world.getTileEntity(x, y, z);
		if (te != null) {
			if (ID == GUI1) {
				return new GUI_ProjectTable(player.inventory, (TileEntityProjectTable) te);
			} else if (ID == GUI2) {
				return new GUI_ModularityTable(player.inventory, (TileEntityModularityTable) te);
			}
		}

		if (ID == GUI3) {
			// We have to cast the new container as our custom class
			// and pass in currently held item for the inventory
			return new GuiBaseBackpack(new Container_BackpackBase(player, player.inventory,
					new BaseInventoryBackpack(player.getHeldItem())));
		}

		if (te != null) {
			if (ID == GUI4) {
				return new GUI_Workbench(player.inventory, (TileEntityWorkbench) te);
			} else if (ID == GUI5) {
				Logger.INFO("sad");
				return new GUI_WorkbenchAdvanced(player.inventory, (TileEntityWorkbenchAdvanced) te);
			} else if (ID == GUI6) {
				return new GUI_FishTrap(player.inventory, (TileEntityFishTrap) te);
			} else if (ID == GUI7) {
				return new GUI_TradeTable(player.inventory, (TileEntityTradeTable) te,
						((TileEntityBase) te).getOwner());
			} else if (ID == GUI8) {
				return new GUI_CircuitProgrammer(player.inventory, (TileEntityCircuitProgrammer) te);
			} else if (ID == GUI13) {
				return new GUI_DecayablesChest(player.inventory, (TileEntityDecayablesChest) te);
			} else if (ID == GUI14) {
				return new GUI_SuperJukebox(player.inventory, (TileEntitySuperJukebox) te);
			} else if (ID == GUI15) {
				return new GUI_PestKiller(player.inventory, (TileEntityPestKiller) te);
			}  else if (ID == GUI16) {
				return new GUI_RoundRobinator(player.inventory, (TileEntityRoundRobinator) te);
			} 
		}

		if (ID == GUI9) {
			return new GuiBaseGrindle(
					new Container_Grindle(player, player.inventory, new BaseInventoryGrindle(player.getHeldItem())));
		}

		// Tool, lunch, magic
		if (ID == GUI10) {
			return new ToolBoxGui(
					new ToolBoxContainer(player, player.inventory, new ToolBoxInventory(player.getHeldItem())));
		}
		if (ID == GUI11) {
			return new LunchBoxGui(
					new LunchBoxContainer(player, player.inventory, new LunchBoxInventory(player.getHeldItem())));
		}
		if (ID == GUI12) {
			return new MagicBagGui(
					new MagicBagContainer(player, player.inventory, new MagicBagInventory(player.getHeldItem())));
		}

		return null;
	}

	// New Methods
	public static void openGui(final EntityPlayer entityplayer, final IGuiManager guiHandler) {
		openGui(entityplayer, guiHandler, (short) 0);
	}

	public static void openGui(final EntityPlayer entityplayer, final IGuiManager guiHandler, final short data) {
		final int guiData = encodeGuiData(guiHandler, data);
		final ChunkCoordinates coordinates = guiHandler.getCoordinates();
		entityplayer.openGui(GTplusplus.instance, guiData, entityplayer.worldObj, coordinates.posX, coordinates.posY,
				coordinates.posZ);
	}

	private static int encodeGuiData(final IGuiManager guiHandler, final short data) {
		final MU_GuiId guiId = Gui_ID_Registry.getGuiIdForGuiHandler(guiHandler);
		return (data << 16) | guiId.getId();
	}

	private static MU_GuiId decodeGuiID(final int guiData) {
		final int guiId = guiData & 0xFF;
		return Gui_ID_Registry.getGuiId(guiId);
	}

	private static short decodeGuiData(final int guiId) {
		return (short) (guiId >> 16);
	}

}