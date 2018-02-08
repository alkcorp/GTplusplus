/* 
 * Copyright (c) CovertJaguar, 2014 http://railcraft.info
 * 
 * This code is the property of CovertJaguar
 * and may only be used with explicit written
 * permission unless otherwise specified on the
 * license page at http://railcraft.info/wiki/info:license.
 */
package gtPlusPlus.api.objects;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gtPlusPlus.core.util.array.BlockPos;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GregtechMetaTileEntityChunkLoader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.OrderedLoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.event.entity.EntityEvent;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class ChunkManager implements LoadingCallback, OrderedLoadingCallback, ForgeChunkManager.PlayerOrderedLoadingCallback {

	private static ChunkManager instance;

	public static Map<BlockPos, GregtechMetaTileEntityChunkLoader> mChunkLoaders = new HashMap<BlockPos, GregtechMetaTileEntityChunkLoader>();

	public static ChunkManager getInstance() {
		if (instance == null) {
			instance = new ChunkManager();
		}
		return instance;
	}

	@SubscribeEvent
	public void entityEnteredChunk(EntityEvent.EnteringChunk event) {

	}

	/**
	 * Returns a Set of ChunkCoordIntPair containing the chunks between the
	 * start and end chunks.
	 * <p/>
	 * One of the pairs of start/end coords need to be equal.
	 * <p/>
	 * Coordinates are in chunk coordinates, not world coordinates.
	 *
	 * @param xChunkA Start Chunk x-Coord
	 * @param zChunkA Start Chunk z-Coord
	 * @param xChunkB End Chunk x-Coord
	 * @param zChunkB End Chunk z-Coord
	 * @param max     Max number of chunks to return
	 * @return A set of chunks.
	 */
	public Set<ChunkCoordIntPair> getChunksBetween(int xChunkA, int zChunkA, int xChunkB, int zChunkB, int max) {
		Set<ChunkCoordIntPair> chunkList = new HashSet<ChunkCoordIntPair>();

		if (xChunkA != xChunkB && zChunkA != zChunkB) {
			return chunkList;
		}

		int xStart = Math.min(xChunkA, xChunkB);
		int xEnd = Math.max(xChunkA, xChunkB);

		int zStart = Math.min(zChunkA, zChunkB);
		int zEnd = Math.max(zChunkA, zChunkB);

		for (int xx = xStart; xx <= xEnd; xx++) {
			for (int zz = zStart; zz <= zEnd; zz++) {
				chunkList.add(new ChunkCoordIntPair(xx, zz));
				if (chunkList.size() >= max) {
					return chunkList;
				}
			}
		}
		return chunkList;
	}

	/**
	 * Returns a Set of ChunkCoordIntPair containing the chunks around point [x,
	 * z]. Coordinates are in chunk coordinates, not world coordinates.
	 *
	 * @param xChunk Chunk x-Coord
	 * @param zChunk Chunk z-Coord
	 * @param radius Distance from [x, z] to include, in number of chunks.
	 * @return A set of chunks.
	 */
	public Set<ChunkCoordIntPair> getChunksAround(int xChunk, int zChunk, int radius) {
		Set<ChunkCoordIntPair> chunkList = new HashSet<ChunkCoordIntPair>();
		for (int xx = xChunk - radius; xx <= xChunk + radius; xx++) {
			for (int zz = zChunk - radius; zz <= zChunk + radius; zz++) {
				chunkList.add(new ChunkCoordIntPair(xx, zz));
			}
		}
		return chunkList;
	}

	/**
	 * Returns a Set of ChunkCoordIntPair containing the chunks around point [x,
	 * z]. Coordinates are in world coordinates, not chunk coordinates.
	 *
	 * @param xWorld World x-Coord
	 * @param zWorld World z-Coord
	 * @param radius Distance from [x, z] to include, in blocks.
	 * @return A set of chunks.
	 */
	public Set<ChunkCoordIntPair> getBufferAround(int xWorld, int zWorld, int radius) {
		int minX = (xWorld - radius) >> 4;
			int maxX = (xWorld + radius) >> 4;
		int minZ = (zWorld - radius) >> 4;
		int maxZ = (zWorld + radius) >> 4;

		Set<ChunkCoordIntPair> chunkList = new HashSet<ChunkCoordIntPair>();
		for (int xx = minX; xx <= maxX; xx++) {
			for (int zz = minZ; zz <= maxZ; zz++) {
				chunkList.add(new ChunkCoordIntPair(xx, zz));
			}
		}
		return chunkList;
	}

	private void printAnchor(String type, int x, int y, int z) {
		Logger.INFO("[Chunk Loader] "+type+" @ [x: "+x+"][y: "+y+"][z: "+z+"]");
	}

	@Override
	public void ticketsLoaded(List<Ticket> tickets, World world) {
		//        System.out.println("Callback 2");
		for (Ticket ticket : tickets) {
			if (ticket.isPlayerTicket())
				continue;
			Entity entity = ticket.getEntity();
			if (entity == null) {
				int x = ticket.getModData().getInteger("xCoord");
				int y = ticket.getModData().getInteger("yCoord");
				int z = ticket.getModData().getInteger("zCoord");

				if (y >= 0) {
					BlockPos tile = new BlockPos(x, y, z);

					if (!mChunkLoaders.isEmpty()) {
						GregtechMetaTileEntityChunkLoader f = mChunkLoaders.get(tile);
						try {
						f.forceChunkLoading(ticket);
						printAnchor("Force Chunk Loading. Chunk Loader has ID of "+f.getLoaderID()+". ",x,y,z);
						}
						catch (Throwable t) {
							t.printStackTrace();
							Logger.INFO("Mild problem with chunk loading, nothing to worry about.");
						}
					}					

					/*if (tile instanceof IGregTechTileEntity) {
						final IGregTechTileEntity tGregTechTileEntity = (IGregTechTileEntity) tile;
						IGregTechTileEntity anchor = (IGregTechTileEntity) tile;
						GregtechMetaTileEntityChunkLoader jun = (GregtechMetaTileEntityChunkLoader) anchor;
						jun.forceChunkLoading(ticket);
						//printAnchor(anchor.getName(), x, y, z);
					}*/
				}
			} 
		}
	}

	@Override
	public List<Ticket> ticketsLoaded(List<Ticket> tickets, World world, int maxTicketCount) {
		//        System.out.println("Callback 1");
		Set<Ticket> adminTickets = new HashSet<Ticket>();
		Set<Ticket> worldTickets = new HashSet<Ticket>();
		Set<Ticket> cartTickets = new HashSet<Ticket>();
		for (Ticket ticket : tickets) {
			Entity entity = ticket.getEntity();
			if (entity == null) {
				int x = ticket.getModData().getInteger("xCoord");
				int y = ticket.getModData().getInteger("yCoord");
				int z = ticket.getModData().getInteger("zCoord");
				if (y >= 0) {
					worldTickets.add(ticket);
				}
			}
		}

		List<Ticket> claimedTickets = new LinkedList<Ticket>();
		claimedTickets.addAll(cartTickets);
		claimedTickets.addAll(adminTickets);
		claimedTickets.addAll(worldTickets);
		return claimedTickets;
	}

	@Override
	public ListMultimap<String, Ticket> playerTicketsLoaded(ListMultimap<String, Ticket> tickets, World world) {
		return LinkedListMultimap.create();
	}
}
