package com.github.maxopoly.Genesis.spawning;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import vg.civcraft.mc.civmodcore.areas.IArea;

import com.github.maxopoly.Genesis.Genesis;
import com.github.maxopoly.Genesis.entities.GenesisLivingEntity;

public class SpawnFinder implements Runnable {

	private static List<BlockFace> cardinalDirections;

	private List<Material> blocksToSpawnOn;
	private List<Material> blocksNotToSpawnOn;
	private List<Material> blocksToSpawnIn;

	private List<IArea> spawnAreas;
	private int chunkSpawnRange;

	private int minimumLightLevelRequired;
	private int maximumLightLevelAllowed;

	private int minimumY;
	private int maximumY;

	private int spawnInSpaceUpwards;
	private int extraSpawnInSpaceSidewards;

	private int attempts;
	private double spawnChance;

	private List<GenesisLivingEntity> entities;

	private Random rng;
	private int PID;
	private long spawnDelay;

	public SpawnFinder(List<Material> blocksToSpawnOn, List<Material> blocksNotToSpawnOn,
			List<Material> blocksToSpawnIn, List<IArea> spawnAreas, int chunkSpawnRange, int minimumLightLevelRequired,
			int maximumLightLevelAllowed, int spawnInSpaceUpwards, int extraSpawnInSpaceSidewards, int attempts,
			int minimumY, int maximumY, double spawnChance, List<GenesisLivingEntity> entities, long spawnDelay) {
		this.blocksNotToSpawnOn = blocksNotToSpawnOn;
		this.blocksToSpawnIn = blocksToSpawnIn;
		this.blocksToSpawnOn = blocksToSpawnOn;
		this.minimumLightLevelRequired = minimumLightLevelRequired;
		this.maximumLightLevelAllowed = maximumLightLevelAllowed;
		this.spawnInSpaceUpwards = spawnInSpaceUpwards;
		this.extraSpawnInSpaceSidewards = extraSpawnInSpaceSidewards;
		this.spawnChance = spawnChance;
		this.attempts = attempts;
		this.minimumY = minimumY;
		this.maximumY = maximumY;
		this.spawnAreas = spawnAreas;
		this.chunkSpawnRange = chunkSpawnRange;
		this.rng = new Random();
		this.entities = entities;
		if (cardinalDirections == null) {
			cardinalDirections = new LinkedList<BlockFace>();
			cardinalDirections.add(BlockFace.NORTH);
			cardinalDirections.add(BlockFace.EAST);
			cardinalDirections.add(BlockFace.SOUTH);
			cardinalDirections.add(BlockFace.WEST);
		}
		this.spawnDelay = spawnDelay;
		this.PID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Genesis.getInstance(), this, spawnDelay, spawnDelay);
	}

	@Override
	public void run() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			Location playerLoc = p.getLocation();
			for (IArea area : spawnAreas) {
				if (area.isInArea(playerLoc)) {
					Chunk c = playerLoc.getWorld().getChunkAt(
							(int) (playerLoc.getChunk().getX() + (Math.round((rng.nextDouble() - 0.5) * 2.0
									* chunkSpawnRange))),
							(int) (playerLoc.getChunk().getZ() + (Math.round((rng.nextDouble() - 0.5) * 2.0
									* chunkSpawnRange))));
					attemptToSpawn(c);
					break;
				}
			}
		}
	}

	public void attemptToSpawn(Chunk c) {
		if (rng.nextDouble() > spawnChance) {
			return;
		}
		for (int i = 0; i < attempts; i++) {
			Location loc = findLocation(c.getWorld(), c.getX() * 16 + rng.nextInt(16), c.getZ() * 16 + rng.nextInt(16));
			if (loc != null) {
				spawnEntities(loc);
				break;
			}
		}
	}

	public void spawnEntities(Location loc) {
		for (GenesisLivingEntity gle : entities) {
			gle.spawnAt(loc);
		}
	}

	public Location findLocation(World w, int x, int z) {
		List<Integer> validY = new LinkedList<Integer>();
		int foundRange = 0;
		for (int y = minimumY; y < maximumY; y++) {
			Block b = w.getBlockAt(x, y, z);
			if (foundRange == 0 && isBlockToSpawnOn(b.getType())) {
				foundRange = 1;
				continue;
			}
			if (foundRange > 0 && isBlockToSpawnIn(b.getType())) {
				boolean sidewardsConditionFullFilled = true;
				// check sidewards
				for (int i = 1; i <= extraSpawnInSpaceSidewards && sidewardsConditionFullFilled == true; i++) {
					for (BlockFace bf : cardinalDirections) {
						Block relative = b.getRelative(bf, 1);
						if (!isBlockToSpawnIn(relative.getType())) {
							sidewardsConditionFullFilled = false;
							break;
						}
					}
				}
				if (!sidewardsConditionFullFilled) {
					foundRange = 0;
					continue;
				}
				// if we are at first block above the solid, check for light
				if (foundRange == 1) {
					if (b.getLightLevel() > maximumLightLevelAllowed || b.getLightLevel() < minimumLightLevelRequired) {
						foundRange = 0;
						continue;
					}
				}
				foundRange++;
				if (foundRange > spawnInSpaceUpwards) {
					validY.add((y - (int) Math.ceil(spawnInSpaceUpwards * 0.5)));
					foundRange = 0;
				}
			} else {
				foundRange = 0;
			}
		}
		if (validY.size() > 0) {
			return new Location(w, x, validY.get(rng.nextInt(validY.size())), z);
		}
		return null;
	}

	private boolean isBlockToSpawnOn(Material m) {
		return ((blocksToSpawnOn == null && m.isSolid()
				&& (blocksNotToSpawnOn == null || !blocksNotToSpawnOn.contains(m)) || (blocksToSpawnOn != null && blocksToSpawnOn
				.contains(m))));
	}

	private boolean isBlockToSpawnIn(Material m) {
		return (blocksToSpawnIn == null && m == Material.AIR)
				|| (blocksToSpawnIn != null && blocksToSpawnIn.contains(m));
	}

	/**
	 * @return How many blocks of air or the specified blocks to spawn in are
	 *         required to be sidewards from the center block the mob is spawned
	 *         on. This will apply in both x and z direction, so for example a
	 *         value of 1 will mean a 3x3 area is required
	 */
	public int getExtraSpaceRequiredSidewards() {
		return extraSpawnInSpaceSidewards;
	}

	/**
	 * @return How many blocks of air or the specified blocks to spawn in are
	 *         required above the block to spawn on
	 */
	public int getRequiredSpaceUpwards() {
		return spawnInSpaceUpwards;
	}

	/**
	 * @return Which blocks were specified as material to spawn on, null if
	 *         nothing was specified and every block except the for the
	 *         forbidden ones are valid
	 */
	public List<Material> getBlocksToSpawnOn() {
		return blocksToSpawnOn;
	}

	/**
	 * @return Which blocks are explicitly forbidden to spawn on or null if
	 *         nothing was specified
	 */
	public List<Material> getBlockNotToSpawnOn() {
		return blocksNotToSpawnOn;
	}

	/**
	 * @return Which blocks are considered "air" or space to spawn in, null if
	 *         nothing was specified and only air is accepted
	 */
	public List<Material> getBlockToSpawnIn() {
		return blocksToSpawnIn;
	}

	/**
	 * @return The maximum light level at which this this instance will spawn
	 */
	public int getMaximumLightLevel() {
		return maximumLightLevelAllowed;
	}

	/**
	 * @return The minimum light level required for this instance to spawn mobs
	 */
	public int getMinimumLightLevel() {
		return minimumLightLevelRequired;
	}

	/**
	 * @return How often this instance will try to find a valid spawn location
	 *         before giving up
	 */
	public int getAttempts() {
		return attempts;
	}

	/**
	 * @return The minimum y level at which this instance will spawn
	 */
	public int getMinimumYLevel() {
		return minimumY;
	}

	/**
	 * @return The maximum y level at which this instance will spawn
	 */
	public int getMaxiumYLevel() {
		return maximumY;
	}

	/**
	 * @return The entities spawned by this instance
	 */
	public List<GenesisLivingEntity> getEntities() {
		return entities;
	}

	/**
	 * @return The chance for this instance to attempt to spawn entities when
	 *         it's given a chunk
	 */
	public double getSpawnChance() {
		return spawnChance;
	}

	/**
	 * @return Process id of this instance to cancel it later on
	 */
	private int getPID() {
		return PID;
	}
}
