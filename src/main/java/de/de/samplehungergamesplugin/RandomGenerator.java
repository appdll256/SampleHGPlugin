package de.de.samplehungergamesplugin;


import de.de.samplehungergamesplugin.annotations.NotNull;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

final class RandomGenerator {

	private static final Material[] FIGHT_MATERIALS = new Material[]{
		//Swords
		Material.WOOD_SWORD,
		Material.STONE_SWORD,
		Material.IRON_SWORD,
		Material.GOLD_SWORD,
		Material.DIAMOND_SWORD,

		//Axes
		Material.WOOD_AXE,
		Material.STONE_AXE,
		Material.IRON_AXE,
		Material.GOLD_AXE,
		Material.DIAMOND_AXE,

		//Armor
		//Leather
		Material.LEATHER_HELMET,
		Material.LEATHER_CHESTPLATE,
		Material.LEATHER_LEGGINGS,
		Material.LEATHER_BOOTS,

		//Chained
		Material.CHAINMAIL_HELMET,
		Material.CHAINMAIL_CHESTPLATE,
		Material.CHAINMAIL_LEGGINGS,
		Material.CHAINMAIL_BOOTS,

		//Iron
		Material.IRON_HELMET,
		Material.IRON_CHESTPLATE,
		Material.IRON_LEGGINGS,
		Material.IRON_BOOTS,

		//Gold
		Material.GOLD_HELMET,
		Material.GOLD_CHESTPLATE,
		Material.GOLD_LEGGINGS,
		Material.GOLD_BOOTS,

		//Diamond
		Material.DIAMOND_HELMET,
		Material.DIAMOND_CHESTPLATE,
		Material.DIAMOND_LEGGINGS,
		Material.DIAMOND_BOOTS,

		//TNT
		Material.TNT,

		//Fishing Rod
		Material.FISHING_ROD,

		//Spider webs
		Material.WEB

	};


	@NotNull
	static ItemStack randomUsableFightItemStack(){
		Material item = FIGHT_MATERIALS[randomNumber(0, FIGHT_MATERIALS.length - 1)];
		return new ItemStack( item, randomNumber(1, item.getMaxStackSize()) );
	}


	static int randomNumber(){
		return new Random(System.currentTimeMillis()).nextInt();
	}


	static float randomFloat(){
		return new Random(System.currentTimeMillis()).nextFloat();
	}


	static int randomNumber(int min, int max) {
		if (min == max)
			return min;

		if (max < min) {
			int tmp;
			tmp = max;
			max = min;
			min = tmp;
		}

		int diff = max - min;

		return Math.round(min + diff * randomFloat());
	}

}
