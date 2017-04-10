package oortcloud.hungrymechanics.configuration.util;

import java.util.ArrayList;
import java.util.Collection;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import oortcloud.hungrymechanics.HungryMechanics;

public class ConfigurationHelper {

	public static ConfigurationHelper instance = new ConfigurationHelper();

	private ConfigurationHelper() {
	}

	/**
	 * 
	 * @param input
	 *            : ((itemstack),(itemstack),...)
	 * @return
	 */
	public ArrayList<ItemStack> getListItemStack(String input) {
		String[] processedInput = StringParser.splitByLevel(StringParser.reduceLevel(input));
		ArrayList<ItemStack> output = new ArrayList<ItemStack>();
		for (String i : processedInput) {
			ItemStack j = getItemStack(i);
			if (j == null) {
				exceptionInvalidFormat(i);
				return null;
			}
			output.add(j);
		}
		if (output.isEmpty()) {
			exceptionEmptyList(input);
			return null;
		}
		return output;
	}

	/**
	 * 
	 * @param input
	 *            : (modid:itemname, number, damage)
	 * @return
	 */
	public ItemStack getItemStack(String input) {
		String[] split = StringParser.splitByLevel(StringParser.reduceLevel(input));
		if (split.length == 1) {
			Item item = (Item) Item.getByNameOrId(split[0]);
			if (item == null) {
				exceptionNameDoesntExist(split[0]);
				return null;
			}
			return new ItemStack(item);
		} else if (split.length == 2) {
			Item item = (Item) Item.getByNameOrId(split[0]);
			if (item == null) {
				exceptionNameDoesntExist(split[0]);
				return null;
			}
			return new ItemStack(item, Integer.parseInt(split[1]));
		} else if (split.length == 3) {
			Item item = (Item) Item.getByNameOrId(split[0]);
			if (item == null) {
				exceptionNameDoesntExist(split[0]);
				return null;
			}
			return new ItemStack(item, Integer.parseInt(split[1]), Integer.parseInt(split[2]));
		} else {
			exceptionInvalidNumberOfArgument(input);
			return null;
		}
	}

	/**
	 * 
	 * @param input
	 *            : ((item),(item),...)
	 * @return
	 */
	public ArrayList<HashItemType> getListHashItem(String input) {
		String[] split = StringParser.splitByLevel(StringParser.reduceLevel(input));
		ArrayList<HashItemType> output = new ArrayList<HashItemType>();
		for (String i : split) {
			HashItemType j = getHashItem(i);
			if (j == null) {
				exceptionInvalidFormat(i);
				return null;
			}
			output.add(j);
		}
		if (output.isEmpty()) {
			exceptionEmptyList(input);
			return null;
		}
		return output;
	}

	/**
	 * 
	 * @param input
	 *            : (modid:itemname,damage)
	 * @return
	 */
	public HashItemType getHashItem(String input) {
		String[] split = StringParser.splitByLevel(StringParser.reduceLevel(input));
		if (split.length == 1) {
			Item item = (Item) Item.getByNameOrId(split[0]);
			if (item == null) {
				exceptionNameDoesntExist(split[0]);
				return null;
			}
			return new HashItemType(item);
		} else if (split.length == 2) {
			Item item = (Item) Item.getByNameOrId(split[0]);
			if (item == null) {
				exceptionNameDoesntExist(split[0]);
				return null;
			}
			return new HashItemType(item, Integer.parseInt(split[1]));
		} else {
			exceptionInvalidNumberOfArgument(input);
			return null;
		}
	}

	/**
	 * 
	 * @param input
	 *            : (probability,(itemstack))
	 * @return
	 */
	public ValueProbabilityItemStack getProbItemStack(String input) {
		String[] split = StringParser.splitByLevel(StringParser.reduceLevel(input));
		if (split.length == 2) {
			ArrayList<ValueProbabilityItemStack> output = new ArrayList<ValueProbabilityItemStack>();
			ItemStack itemStack = getItemStack(split[1]);
			if (itemStack == null) {
				exceptionInvalidFormat(split[1]);
				return null;
			}
			return new ValueProbabilityItemStack(Double.parseDouble(split[0]), itemStack);
		} else {
			exceptionInvalidNumberOfArgument(input);
			return null;
		}
	}

	/**
	 * 
	 * @param input : ((probitemstack),(probitemstack),...)
	 * @return
	 */
	public ArrayList<ValueProbabilityItemStack> getListProbItemStack(String input) {
		String[] split = StringParser.splitByLevel(StringParser.reduceLevel(input));
		ArrayList<ValueProbabilityItemStack> output = new ArrayList<ValueProbabilityItemStack>();
		for (String i : split) {
			ValueProbabilityItemStack j = getProbItemStack(i);
			if (j == null) {
				exceptionInvalidFormat(i);
				return null;
			}
			output.add(j);
		}
		if (output.isEmpty()) {
			exceptionEmptyList(input);
			return null;
		}
		return output;
	}

	public static void exceptionInvalidNumberOfArgument(String input) {
		HungryMechanics.logger.warn("Invalid number of arguments : " + input);
	}

	public static void exceptionNameDoesntExist(String name) {
		HungryMechanics.logger.warn(name + " doesn't exist.");
	}

	public static void exceptionInvalidFormat(String argument) {
		HungryMechanics.logger.warn(argument + " is invalid.");
	}

	public static void exceptionEmptyList(String input) {
		HungryMechanics.logger.warn(input + " is an empty list.");
	}

}
