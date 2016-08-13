package com.github.maxopoly.Genesis.spawning;

import java.util.ArrayList;
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
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import vg.civcraft.mc.civmodcore.areas.IArea;

import com.github.maxopoly.Genesis.Genesis;
import com.github.maxopoly.Genesis.entities.GenesisLivingEntity;

public class SpawnFinder implements Runnable {

	private static List<BlockFace> cardinalDirections;
	private static Random rng;

	private List<Material> blocksToSpawnOn;
	private List<Material> blocksNotToSpawnOn;
	private List<Material> blocksToSpawnIn;

	private List<IArea> spawnAreas;
	private int chunkSpawnRange;
	private double minimumPlayerDistance;

	private int minimumLightLevelRequired;
	private int maximumLightLevelAllowed;

	private int minimumY;
	private int maximumY;

	private int spawnInSpaceUpwards;
	private int extraSpawnInSpaceSidewards;

	private int attempts;
	private double spawnChance;

	private List<GenesisLivingEntity> entities;

	private int PID;
	private long spawnDelay;

	public SpawnFinder(List<Material> blocksToSpawnOn, List<Material> blocksNotToSpawnOn,
			List<Material> blocksToSpawnIn, List<IArea> spawnAreas, int chunkSpawnRange, int minimumLightLevelRequired,
			int maximumLightLevelAllowed, int spawnInSpaceUpwards, int extraSpawnInSpaceSidewards, int attempts,
			int minimumY, int maximumY, double spawnChance, List<GenesisLivingEntity> entities, long spawnDelay,
			double minimumPlayerDistance) {
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
		this.entities = entities;
		this.minimumPlayerDistance = minimumPlayerDistance;
		if (cardinalDirections == null) {
			// first initialization
			cardinalDirections = new ArrayList<BlockFace>();
			cardinalDirections.add(BlockFace.NORTH);
			cardinalDirections.add(BlockFace.EAST);
			cardinalDirections.add(BlockFace.SOUTH);
			cardinalDirections.add(BlockFace.WEST);
			rng = new Random();
		}
		this.spawnDelay = spawnDelay;
		this.PID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Genesis.getInstance(), this, spawnDelay, spawnDelay);
	}

	/**
	 * Runnable that keeps spawning mobs around players. Each round for every
	 * players, who is within the specified area for this instance, a random
	 * number between 0 and 1 is rolled, which has to be lower than spawnChance
	 * so the spawningAttempt is continued. If it is, a chunk will be randomly
	 * picked, which is a maximum of chunkSpawnRange away in x and z coordinate
	 * and not the chunk the player is in, unless chunkSpawnRange is set to 0.
	 * 
	 * The weight for the chunk chosen will be determined for the primary entity
	 * spawned by this instance (first one listed in the config) and based on
	 * the weight a spawn chance between 0 and 1 is calculated by:
	 * 
	 * 1 + e ^ (currentWeight / spawnModifier)
	 * 
	 * , where e is the euler number, currentWeight is the current weight of the
	 * primary entity in the chosen chunk and spawnModifier is the
	 * spawnChanceModifier of the primary entity. The higher the weight is, the
	 * closer this number get to 1, so a randomly chosen number between 0 and 1
	 * has to bigger than the current chance to allow spawns to happen. If the
	 * random numbers allow an entity to spawn, a random x and z within the
	 * chunk are chosen and a valid y is searched for at those coordinates,
	 * which fulfills all the other criteria specified. If one is found, the
	 * entities are spawned there and the weight is increased
	 */
	@Override
	public void run() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			Location playerLoc = p.getLocation();
			for (IArea area : spawnAreas) {
				if (area.isInArea(playerLoc)) {
					if (rng.nextDouble() > spawnChance) {
						return;
					}
					Chunk c = playerLoc.getWorld().getChunkAt(
							(int) (playerLoc.getChunk().getX() + (Math.round((rng.nextDouble() - 0.5) * 2.0
									* chunkSpawnRange))),
							(int) (playerLoc.getChunk().getZ() + (Math.round((rng.nextDouble() - 0.5) * 2.0
									* chunkSpawnRange))));
					GenesisLivingEntity primaryEntity = entities.get(0);
					int currentWeight = Genesis.getManager().getWeightStorage()
							.getWeightForChunk(c.getWorld().getUID(), c.getX(), c.getZ(), primaryEntity);
					if (rng.nextDouble() > 1 - Math.exp(-(((double) currentWeight) / primaryEntity
							.getSpawnChanceModifier()))) {
						attemptToSpawn(c);
					}
					break;
				}
			}
		}
	}

	public void attemptToSpawn(Chunk c) {
		for (int i = 0; i < attempts; i++) {
			Location loc = findLocation(c.getWorld(), c.getX() * 16 + rng.nextInt(16), c.getZ() * 16 + rng.nextInt(16));
			if (loc != null) {
				spawnEntities(loc);
				break;
			}
		}
	}

	/**
	 * Directly spawns all entities being spawned by this instance in the given
	 * location without performing any additional checks at all
	 * 
	 * @param loc
	 *            Location to spawn in
	 */
	public void spawnEntities(Location loc) {
		for (GenesisLivingEntity gle : entities) {
			Genesis.getManager()
					.getWeightStorage()
					.increaseWeightForChunk(loc.getWorld().getUID(), loc.getChunk().getX(), loc.getChunk().getZ(), gle,
							1);
			gle.spawnAt(loc);
		}
	}

	/**
	 * Attempts to find a valid y-level which fulfills all the criterias
	 * specified in this instance in the given world at the given x and z
	 * coordinates
	 * 
	 * @param w
	 *            World to spawn in
	 * @param x
	 *            X-coordinate to spawn at
	 * @param z
	 *            Z-coordinate to spawn at
	 * @return Complete location to spawn in or null if no valid location was
	 *         found
	 */
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
				// if we are at first block above the solid, check for light and
				// distance to players
				if (foundRange == 1) {
					if (b.getLightLevel() > maximumLightLevelAllowed || b.getLightLevel() < minimumLightLevelRequired) {
						foundRange = 0;
						continue;
					}
					boolean farEnoughAwayFromPlayers = true;
					for (Entity entity : w.getNearbyEntities(b.getLocation(), minimumPlayerDistance,
							minimumPlayerDistance, minimumPlayerDistance)) {
						if (entity.getType() == EntityType.PLAYER) {
							farEnoughAwayFromPlayers = false;
							break;
						}
					}
					if (!farEnoughAwayFromPlayers) {
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
