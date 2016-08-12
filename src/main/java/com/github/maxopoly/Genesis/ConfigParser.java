package com.github.maxopoly.Genesis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.inventory.ItemStack;

import vg.civcraft.mc.civmodcore.areas.IArea;
import vg.civcraft.mc.civmodcore.itemHandling.ItemMap;
import vg.civcraft.mc.civmodcore.util.ConfigParsing;
import static vg.civcraft.mc.civmodcore.util.ConfigParsing.parseItemMapDirectly;

import com.github.maxopoly.Genesis.combatEffects.CombatEffect;
import com.github.maxopoly.Genesis.combatEffects.EffectCause;
import com.github.maxopoly.Genesis.entities.GenesisLivingEntity;
import com.github.maxopoly.Genesis.entities.ambient.GenesisBat;
import com.github.maxopoly.Genesis.entities.ambient.GenesisSquid;
import com.github.maxopoly.Genesis.entities.animals.GenesisChicken;
import com.github.maxopoly.Genesis.entities.animals.GenesisCow;
import com.github.maxopoly.Genesis.entities.animals.GenesisHorse;
import com.github.maxopoly.Genesis.entities.animals.GenesisMushroomCow;
import com.github.maxopoly.Genesis.entities.animals.GenesisOcelot;
import com.github.maxopoly.Genesis.entities.animals.GenesisPig;
import com.github.maxopoly.Genesis.entities.animals.GenesisRabbit;
import com.github.maxopoly.Genesis.entities.animals.GenesisSheep;
import com.github.maxopoly.Genesis.entities.animals.GenesisWolf;
import com.github.maxopoly.Genesis.entities.hostile.GenesisBlaze;
import com.github.maxopoly.Genesis.entities.hostile.GenesisCaveSpider;
import com.github.maxopoly.Genesis.entities.hostile.GenesisCreeper;
import com.github.maxopoly.Genesis.entities.hostile.GenesisEnderman;
import com.github.maxopoly.Genesis.entities.hostile.GenesisGuardian;
import com.github.maxopoly.Genesis.entities.hostile.GenesisSpider;
import com.github.maxopoly.Genesis.entities.hostile.human.GenesisSkeleton;
import com.github.maxopoly.Genesis.entities.hostile.human.GenesisZombie;
import com.github.maxopoly.Genesis.entities.hostile.human.GenesisZombiePigman;
import com.github.maxopoly.Genesis.entities.splitables.GenesisMagmaCube;
import com.github.maxopoly.Genesis.entities.splitables.GenesisSlime;
import com.github.maxopoly.Genesis.listener.SpawnPreventionListener;
import com.github.maxopoly.Genesis.misc.Drops;
import com.github.maxopoly.Genesis.spawning.SpawnFinder;

public class ConfigParser {

	private Genesis plugin;

	public ConfigParser(Genesis plugin) {
		this.plugin = plugin;
	}

	public void parse() {
		plugin.info("Loading Genesis config");
		plugin.saveDefaultConfig();
		plugin.reloadConfig();
		FileConfiguration config = plugin.getConfig();
		parseSpawnFinders(config.getConfigurationSection("spawns"));
		boolean disableNormalSpawns = config.getBoolean("disableNaturalSpawns", false);
		if (disableNormalSpawns) {
			new SpawnPreventionListener(true);
			// will register itself
		}
	}

	public void parseSpawnFinders(ConfigurationSection config) {
		if (config == null) {
			plugin.severe("No spawns specified in config");
			return;
		}
		for (String key : config.getKeys(false)) {
			ConfigurationSection current = config.getConfigurationSection(key);
			if (current == null) {
				plugin.warning("Invalid value found at " + config.getCurrentPath() + "." + key
						+ ". Only config sections allowed on this level");
				continue;
			}
			List<Material> blocksToSpawnOn = parseMaterialList(current, "blocksToSpawnOn");
			List<Material> blocksNotToSpawnOn = parseMaterialList(current, "blocksNotToSpawnOn");
			List<Material> blocksToSpawnIn = parseMaterialList(current, "blocksToSpawnIn");
			if (blocksToSpawnOn != null && blocksNotToSpawnOn != null) {
				plugin.warning("You specified both blocks to spawn on and not to spawn on at "
						+ current.getCurrentPath() + " this doesn't make much sense, please read the documentation");
			}
			ConfigurationSection areaSection = current.getConfigurationSection("spawnAreas");
			if (areaSection == null) {
				plugin.warning("No area specified at " + current.getCurrentPath() + " skipping it");
				continue;
			}
			List<IArea> spawnAreas = new ArrayList<IArea>();
			for (String areaKey : areaSection.getKeys(false)) {
				IArea area = ConfigParsing.parseArea(areaSection.getConfigurationSection(areaKey));
				if (area == null) {
					plugin.warning("Failed to parse area with key " + areaKey + " at " + areaSection);
					continue;
				}
				spawnAreas.add(area);
			}
			int chunkSpawnRange = current.getInt("chunkSpawnRange", 4);
			int minimumLightLevelRequired = current.getInt("minimumLightLevel", 0);
			int maximumLightLevelAllowed = current.getInt("maximumLightLevel", 15);
			int spawnInSpaceUpwards = current.getInt("spawnInSpaceUpwards", 2);
			int extraSpawnInSpaceSidewards = current.getInt("extraSpawnInSpaceSidewards", 0);
			int attempts = current.getInt("attempts", 5);
			int minimumY = current.getInt("minimumY", 0);
			int maximumY = current.getInt("maximumY", 255);
			double spawnChance = current.getDouble("spawnChance", 1.0);
			List<GenesisLivingEntity> entities = parseEntities(current.getConfigurationSection("entities"));
			if (entities == null) {
				plugin.warning("Failed to parse entities at " + current.getCurrentPath() + ". Skipping it");
				continue;
			}
			long spawnDelay = ConfigParsing.parseTime(current.getString("spawnDelay", "1m"));
			// default once a minute

			Genesis.getManager()
					.registerSpawnFinder(
							new SpawnFinder(blocksToSpawnOn, blocksNotToSpawnOn, blocksToSpawnIn, spawnAreas,
									chunkSpawnRange, minimumLightLevelRequired, maximumLightLevelAllowed,
									spawnInSpaceUpwards, extraSpawnInSpaceSidewards, attempts, minimumY, maximumY,
									spawnChance, entities, spawnDelay));
		}
	}

	public List<GenesisLivingEntity> parseEntities(ConfigurationSection config) {
		if (config == null) {
			plugin.warning("No entities specified in config");
			return null;
		}
		List<GenesisLivingEntity> entities = new ArrayList<GenesisLivingEntity>();
		for (String key : config.getKeys(false)) {
			ConfigurationSection current = config.getConfigurationSection(key);
			if (current == null) {
				plugin.warning("Invalid value found at " + config.getCurrentPath() + "." + key
						+ ". Only config sections allowed on this level");
				continue;
			}

			GenesisLivingEntity result = null;

			// parse general stuff that applies for every entity
			String typeString = current.getString("type");
			if (typeString == null) {
				plugin.warning("No entity type specified at " + current.getCurrentPath() + ". Skipping it.");
				continue;
			}
			EntityType type = EntityType.fromName(typeString);
			if (type == null) {
				plugin.warning("Invalid entity type specified at " + current.getCurrentPath() + ": " + typeString
						+ ".Skipping it.");
				continue;
			}
			String uniqueTag = current.getString("tag");
			if (uniqueTag == null) {
				plugin.warning("No unique tag specified at " + current.getCurrentPath() + ". Skipping it.");
				continue;
			}
			Map<List<Drops>, Double> drops = parseDrops(current.getConfigurationSection("drops"));
			String customName = current.getString("customName");

			Map<EffectCause, List<CombatEffect>> effects = parseCombatEffects(current
					.getConfigurationSection("effects"));
			switch (type) {
			// normal hostiles
			case CREEPER:
				boolean isPowered = current.getBoolean("powered", false);
				result = new GenesisCreeper(uniqueTag, customName, drops, effects, isPowered);
				break;
			case BLAZE:
				result = new GenesisBlaze(uniqueTag, customName, drops, effects);
				break;
			case CAVE_SPIDER:
				result = new GenesisCaveSpider(uniqueTag, customName, drops, effects);
				break;
			case GUARDIAN:
				boolean isElder = current.getBoolean("isElder", false);
				result = new GenesisGuardian(uniqueTag, customName, drops, effects, isElder);
				break;
			case ENDERMAN:
				result = new GenesisEnderman(uniqueTag, customName, drops, effects);
				break;
			case SPIDER:
				result = new GenesisSpider(uniqueTag, customName, drops, effects);
				break;
			// ambient
			case SQUID:
				result = new GenesisSquid(uniqueTag, customName, drops, effects);
				break;
			case BAT:
				result = new GenesisBat(uniqueTag, customName, drops, effects);
				break;
			// human entities
			case ZOMBIE:
			case SKELETON:
			case PIG_ZOMBIE:
				boolean canPickupItems = current.getBoolean("canPickupItems", false);
				ItemStack helmet = null;
				ItemStack chestPlate = null;
				ItemStack leggings = null;
				ItemStack boots = null;
				ItemStack hand = null;
				double helmetDropChance = 0.0;
				double chestPlateDropChance = 0.0;
				double leggingsDropChance = 0.0;
				double bootsDropChance = 0.0;
				double handDropChance = 0.0;
				ItemMap helmetMap = parseItemMapDirectly(current.getConfigurationSection("helmet"));
				if (helmetMap.getTotalItemAmount() != 0) {
					helmet = helmetMap.getItemStackRepresentation().get(0);
					helmetDropChance = current.getConfigurationSection("helmet").getDouble("dropChance", 0.0);
				}
				ItemMap chestPlateMap = parseItemMapDirectly(current.getConfigurationSection("chestPlate"));
				if (chestPlateMap.getTotalItemAmount() != 0) {
					chestPlate = chestPlateMap.getItemStackRepresentation().get(0);
					chestPlateDropChance = current.getConfigurationSection("chestPlate").getDouble("dropChance", 0.0);
				}
				ItemMap leggingsMap = parseItemMapDirectly(current.getConfigurationSection("leggings"));
				if (leggingsMap.getTotalItemAmount() != 0) {
					leggings = leggingsMap.getItemStackRepresentation().get(0);
					leggingsDropChance = current.getConfigurationSection("leggings").getDouble("dropChance", 0.0);
				}
				ItemMap bootsMap = parseItemMapDirectly(current.getConfigurationSection("boots"));
				if (bootsMap.getTotalItemAmount() != 0) {
					boots = bootsMap.getItemStackRepresentation().get(0);
					bootsDropChance = current.getConfigurationSection("boots").getDouble("dropChance", 0.0);
				}
				ItemMap handMap = parseItemMapDirectly(current.getConfigurationSection("hand"));
				if (handMap.getTotalItemAmount() != 0) {
					hand = handMap.getItemStackRepresentation().get(0);
					handDropChance = current.getConfigurationSection("hand").getDouble("dropChance", 0.0);
				}
				if (type == EntityType.SKELETON) {
					String skeleTypeString = current.getString("skeletonType", "NORMAL");
					SkeletonType skeleType;
					try {
						skeleType = SkeletonType.valueOf(skeleTypeString);
					} catch (IllegalArgumentException e) {
						plugin.warning("Found invalid skeleton type specified at " + current.getCurrentPath() + ". "
								+ skeleTypeString + " couldnt be parsed and was defaulted to NORMAL");
						skeleType = SkeletonType.NORMAL;
					}
					result = new GenesisSkeleton(uniqueTag, customName, drops, effects, helmet, chestPlate, leggings,
							boots, hand, helmetDropChance, chestPlateDropChance, leggingsDropChance, bootsDropChance,
							handDropChance, canPickupItems, skeleType);
				} else {
					boolean child = current.getBoolean("child", false);
					if (type == EntityType.PIG_ZOMBIE) {
						int initialAnger = current.getInt("initialAnger", 0);
						result = new GenesisZombiePigman(uniqueTag, customName, drops, effects, helmet, chestPlate,
								leggings, boots, hand, helmetDropChance, chestPlateDropChance, leggingsDropChance,
								bootsDropChance, handDropChance, canPickupItems, child, initialAnger);
					} else { // Zombie
						result = new GenesisZombie(uniqueTag, customName, drops, effects, helmet, chestPlate, leggings,
								boots, hand, helmetDropChance, chestPlateDropChance, leggingsDropChance,
								bootsDropChance, handDropChance, canPickupItems, child);
					}
				}
				break;
			// animals
			case CHICKEN:
			case COW:
			case HORSE:
			case MUSHROOM_COW:
			case OCELOT:
			case PIG:
			case RABBIT:
			case WOLF:
			case SHEEP:
				boolean ageLocked = current.getBoolean("ageLocked", false);
				boolean isBaby = current.getBoolean("child", false);
				switch (type) {
				case CHICKEN:
					result = new GenesisChicken(uniqueTag, customName, drops, effects, ageLocked, isBaby);
					break;
				case COW:
					result = new GenesisCow(uniqueTag, customName, drops, effects, ageLocked, isBaby);
					break;
				case HORSE:
					Map<String, Double> color = parseDynamicChanceMap(current, "color");
					Map<Horse.Color, Double> colors = new TreeMap<Horse.Color, Double>();
					for (Entry<String, Double> entry : color.entrySet()) {
						Horse.Color tempCol;
						try {
							tempCol = Horse.Color.valueOf(entry.getKey());
						} catch (IllegalArgumentException e) {
							plugin.warning(entry.getKey() + " is not a valid horse color at "
									+ current.getCurrentPath());
							continue;
						}
						colors.put(tempCol, entry.getValue());
					}
					Map<String, Double> style = parseDynamicChanceMap(current, "style");
					Map<Horse.Style, Double> styles = new TreeMap<Horse.Style, Double>();
					for (Entry<String, Double> entry : style.entrySet()) {
						Horse.Style tempCol;
						try {
							tempCol = Horse.Style.valueOf(entry.getKey());
						} catch (IllegalArgumentException e) {
							plugin.warning(entry.getKey() + " is not a valid horse style at "
									+ current.getCurrentPath());
							continue;
						}
						styles.put(tempCol, entry.getValue());
					}
					Map<String, Double> variant = parseDynamicChanceMap(current, "variant");
					Map<Horse.Variant, Double> variants = new TreeMap<Horse.Variant, Double>();
					for (Entry<String, Double> entry : variant.entrySet()) {
						Horse.Variant tempCol;
						try {
							tempCol = Horse.Variant.valueOf(entry.getKey());
						} catch (IllegalArgumentException e) {
							plugin.warning(entry.getKey() + " is not a valid horse color at "
									+ current.getCurrentPath());
							continue;
						}
						variants.put(tempCol, entry.getValue());
					}
					boolean hasChest = current.getBoolean("hasChest", false);
					int maximumDomestication = current.getInt("maximumDomestication", -1);
					result = new GenesisHorse(uniqueTag, customName, drops, effects, ageLocked, isBaby, colors, styles,
							variants, hasChest, maximumDomestication);
					break;
				case MUSHROOM_COW:
					result = new GenesisMushroomCow(uniqueTag, customName, drops, effects, ageLocked, isBaby);
					break;
				case OCELOT:
					Map<String, Double> oceTypes = parseDynamicChanceMap(current, "type");
					Map<Ocelot.Type, Double> oceType = new TreeMap<Ocelot.Type, Double>();
					for (Entry<String, Double> entry : oceTypes.entrySet()) {
						Ocelot.Type tempCol;
						try {
							tempCol = Ocelot.Type.valueOf(entry.getKey());
						} catch (IllegalArgumentException e) {
							plugin.warning(entry.getKey() + " is not a valid ocelote type at "
									+ current.getCurrentPath());
							continue;
						}
						oceType.put(tempCol, entry.getValue());
					}
					result = new GenesisOcelot(uniqueTag, customName, drops, effects, ageLocked, isBaby, oceType);
					break;
				case PIG:
					boolean hasSaddle = current.getBoolean("hasSaddle", false);
					result = new GenesisPig(uniqueTag, customName, drops, effects, ageLocked, isBaby, hasSaddle);
					break;
				case RABBIT:
					Map<String, Double> rabbitTypes = parseDynamicChanceMap(current, "type");
					Map<Rabbit.Type, Double> rabbitType = new TreeMap<Rabbit.Type, Double>();
					for (Entry<String, Double> entry : rabbitTypes.entrySet()) {
						Rabbit.Type tempCol;
						try {
							tempCol = Rabbit.Type.valueOf(entry.getKey());
						} catch (IllegalArgumentException e) {
							plugin.warning(entry.getKey() + " is not a valid rabbit type at "
									+ current.getCurrentPath());
							continue;
						}
						rabbitType.put(tempCol, entry.getValue());
					}
					result = new GenesisRabbit(uniqueTag, customName, drops, effects, ageLocked, isBaby, rabbitType);
					break;
				case WOLF:
					Map<String, Double> dogColors = parseDynamicChanceMap(current, "collarColor");
					Map<DyeColor, Double> dogColor = new TreeMap<DyeColor, Double>();
					for (Entry<String, Double> entry : dogColors.entrySet()) {
						DyeColor tempCol;
						try {
							tempCol = DyeColor.valueOf(entry.getKey());
						} catch (IllegalArgumentException e) {
							plugin.warning(entry.getKey() + " is not a valid rabbit type at "
									+ current.getCurrentPath());
							continue;
						}
						dogColor.put(tempCol, entry.getValue());
					}
					result = new GenesisWolf(uniqueTag, customName, drops, effects, ageLocked, isBaby, dogColor);
					break;
				case SHEEP:
					Map<String, Double> sheepColors = parseDynamicChanceMap(current, "collarColor");
					Map<DyeColor, Double> sheepColor = new TreeMap<DyeColor, Double>();
					for (Entry<String, Double> entry : sheepColors.entrySet()) {
						DyeColor tempCol;
						try {
							tempCol = DyeColor.valueOf(entry.getKey());
						} catch (IllegalArgumentException e) {
							plugin.warning(entry.getKey() + " is not a valid rabbit type at "
									+ current.getCurrentPath());
							continue;
						}
						sheepColor.put(tempCol, entry.getValue());
					}
					boolean sheared = current.getBoolean("isSheared", false);
					result = new GenesisSheep(uniqueTag, customName, drops, effects, ageLocked, isBaby, sheared,
							sheepColor);
					break;
				case SLIME:
				case MAGMA_CUBE:
					int size = current.getInt("size", 3);
					boolean recursiveSplit = current.getBoolean("recursiveSplit");
					int childrenCount = current.getInt("childrenAmount", 3);
					if (type == EntityType.SLIME) {
						result = new GenesisSlime(uniqueTag, customName, drops, effects, childrenCount, size,
								recursiveSplit);
					} else { // magma cube
						result = new GenesisMagmaCube(uniqueTag, customName, drops, effects, childrenCount, size,
								recursiveSplit);
					}
				}
				break;
			}
			if (result != null) {
				entities.add(result);
			}
		}
		return entities;
	}

	private Map<List<Drops>, Double> parseDrops(ConfigurationSection config) {
		if (config == null) {
			return null;
		}
		double totalChance = 0.0;
		Map<List<Drops>, Double> dropMap = new HashMap<List<Drops>, Double>();
		for (String key : config.getKeys(false)) {
			ConfigurationSection current = config.getConfigurationSection(key);
			if (current == null) {
				plugin.warning("Found invalid key " + key + " at " + config.getCurrentPath());
				continue;
			}
			double chance = current.getDouble("chance");
			totalChance += chance;
			ConfigurationSection items = current.getConfigurationSection("items");
			if (items == null) {
				plugin.warning("No items specified at " + current.getCurrentPath() + ". Skipping it");
				continue;
			}
			List<Drops> drops = new ArrayList<Drops>();
			for (String itemKey : items.getKeys(false)) {
				ConfigurationSection currentItem = items.getConfigurationSection(itemKey);
				if (currentItem == null) {
					plugin.warning("Found invalid key " + itemKey + " at " + items.getCurrentPath());
					continue;
				}
				ItemMap im = ConfigParsing.parseItemMapDirectly(currentItem);
				if (im.getTotalItemAmount() == 0) {
					plugin.warning("No actual items specified at " + currentItem.getCurrentPath());
					continue;
				}
				ItemStack is = im.getItemStackRepresentation().get(0);
				int lowerLimit = currentItem.getInt("lowerLimit", 0);
				int upperLimit = currentItem.getInt("upperLimit", 5);
				Drops d = new Drops(is, lowerLimit, upperLimit);
				drops.add(d);
			}
			dropMap.put(drops, chance);
		}
		if (Math.abs(totalChance - 1.0) > 0.0000001) {
			plugin.warning("Chances for drops at " + config.getCurrentPath()
					+ " dont add up to 1.0 properly, they may not work as inteded");
		}
		return dropMap;
	}

	private List<Material> parseMaterialList(ConfigurationSection config, String key) {
		if (config == null || key == null) {
			return null;
		}
		List<String> stringParsed = config.getStringList(key);
		if (stringParsed == null || stringParsed.size() == 0) {
			return null;
		}
		List<Material> materials = new ArrayList<Material>();
		for (String s : stringParsed) {
			Material m;
			try {
				m = Material.valueOf(s);
			} catch (IllegalArgumentException e) {
				plugin.warning(s + " is specified at " + config
						+ " as material, but is no valid material. It was skipped");
				continue;
			}
			materials.add(m);
		}
		return materials;
	}

	private Map<String, Double> parseDynamicChanceMap(ConfigurationSection config, String option) {
		if (config == null || option == null) {
			return null;
		}
		Map<String, Double> result = new TreeMap<String, Double>();
		if (config.isString(option)) {
			result.put(config.getString(option), 1.0);
		} else if (config.isConfigurationSection(option)) {
			ConfigurationSection subSection = config.getConfigurationSection(option);
			for (String key : subSection.getKeys(false)) {
				ConfigurationSection current = subSection.getConfigurationSection(key);
				String type = current.getString(option);
				if (type == null) {
					plugin.warning("No type specified at " + current.getCurrentPath() + ". Skipping it.");
					continue;
				}
				if (!current.isDouble("chance")) {
					plugin.warning("No chance specified at " + current.getCurrentPath() + ". Skipping it.");
					continue;
				}
				double chance = current.getDouble("chance");
				result.put(type, chance);
			}
		}
		return result;
	}

	private Map<EffectCause, List<CombatEffect>> parseCombatEffects(ConfigurationSection config) {
		return null; // TODO
	}
}
