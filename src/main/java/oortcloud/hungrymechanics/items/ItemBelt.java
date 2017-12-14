package oortcloud.hungrymechanics.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import oortcloud.hungrymechanics.HungryMechanics;
import oortcloud.hungrymechanics.core.lib.References;
import oortcloud.hungrymechanics.core.lib.Strings;
import oortcloud.hungrymechanics.energy.PowerNetwork;
import oortcloud.hungrymechanics.tileentities.TileEntityAxle;

public class ItemBelt extends Item {

	public static int MAX_LENGTH = 8;

	public ItemBelt() {
		super();
		setRegistryName(Strings.itemBeltName);
		setUnlocalizedName(References.MODID+"."+Strings.itemBeltName);
		setCreativeTab(HungryMechanics.tabHungryMechanics);

		setMaxStackSize(1);
		GameRegistry.register(this);
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
		ItemStack itemStack = new ItemStack(this,1,16);
		subItems.add(itemStack);
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltip.add("Length: " + stack.getItemDamage() + " m");
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("SelectedBlockPos")) {
			tooltip.add("Connected to: " + BlockPos.fromLong(stack.getTagCompound().getLong("SelectedBlockPos")));
		}
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX,
			float hitY, float hitZ) {
		ItemStack stack = playerIn.getHeldItem(hand);
		
		// TODO Study EnumActionResult in detail
		if (!TileEntityAxle.isValidAxle(worldIn, pos)) {
			return EnumActionResult.PASS;
		}
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("SelectedBlockPos")) {
			BlockPos selectedPos = BlockPos.fromLong(stack.getTagCompound().getLong("SelectedBlockPos"));
			if (pos.equals(selectedPos)) {
				return EnumActionResult.PASS;
			}
			if (pos.getY()!=selectedPos.getY()) {
				return EnumActionResult.PASS;
			}
			if (!TileEntityAxle.isValidAxle(worldIn, selectedPos)) {
				stack.getTagCompound().removeTag("SelectedBlockPos");
				return EnumActionResult.PASS;
			}
			TileEntityAxle axle1 = (TileEntityAxle) worldIn.getTileEntity(selectedPos);
			TileEntityAxle axle2 = (TileEntityAxle) worldIn.getTileEntity(pos);
			if (axle1 == null || axle1.isConnected()) {
				stack.getTagCompound().removeTag("SelectedBlockPos");
				return EnumActionResult.PASS;
			}
			if (axle2 == null || axle2.isConnected()) {
				return EnumActionResult.PASS;
			}
			double dist = pos.distanceSq(selectedPos);
			int requiredBelt = (int) (Math.ceil(Math.sqrt(dist)));
			if (requiredBelt <= Math.min(stack.getItemDamage(), MAX_LENGTH)) {
				stack.setItemDamage(stack.getItemDamage() - requiredBelt);
				axle1.setConnectedAxle(pos);
				axle2.setConnectedAxle(selectedPos);
				axle1.mergePowerNetwork(new PowerNetwork(0));
				stack.getTagCompound().removeTag("SelectedBlockPos");
				if (stack.getItemDamage() == 0) {
					playerIn.inventory.deleteStack(stack);
				}
				return EnumActionResult.SUCCESS;
			}
			return EnumActionResult.FAIL;
		} else {
			TileEntityAxle axle = (TileEntityAxle) worldIn.getTileEntity(pos);
			if (axle != null && !axle.isConnected()) {
				if (!stack.hasTagCompound()) {
					stack.setTagCompound(new NBTTagCompound());
				}
				stack.getTagCompound().setLong("SelectedBlockPos", pos.toLong());
				return EnumActionResult.SUCCESS;
			}
			return EnumActionResult.PASS;
		}
	}
	
}
