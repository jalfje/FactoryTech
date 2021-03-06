package dalapo.factech.plugins.crafttweaker;

import java.util.Map.Entry;

import javax.annotation.Nullable;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import dalapo.factech.auxiliary.MachineRecipes;
import dalapo.factech.helper.FacCraftTweakerHelper;
import dalapo.factech.helper.FacStackHelper;
import dalapo.factech.helper.Pair;
import dalapo.factech.tileentity.specialized.TileEntityAgitator.AgitatorRecipe;

import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.factorytech.Agitator")
@ZenRegister
public class Agitator
{
	@ZenMethod
	public static void addRecipe(ILiquidStack fluid1, ILiquidStack fluid2, IItemStack itemIn, ILiquidStack fluidOut, IItemStack itemOut)
	{
		AgitatorRecipe toAdd = new AgitatorRecipe((ItemStack)FacCraftTweakerHelper.toStack(itemIn), (ItemStack)FacCraftTweakerHelper.toStack(itemOut), (FluidStack)fluidOut.getInternal(), (FluidStack)fluid1.getInternal(), (FluidStack)fluid2.getInternal());
		CraftTweakerAPI.apply(new Add(toAdd));
	}
	
	@ZenMethod
	public static void addRecipe(ILiquidStack fluid1, ILiquidStack fluid2, ILiquidStack fluidOut)
	{
		AgitatorRecipe toAdd = new AgitatorRecipe(ItemStack.EMPTY, ItemStack.EMPTY, (FluidStack)fluidOut.getInternal(), (FluidStack)fluid1.getInternal(), (FluidStack)fluid2.getInternal());
		CraftTweakerAPI.apply(new Add(toAdd));
	}
	
	@ZenMethod
	public static void addRecipe(IItemStack itemIn, ILiquidStack fluid1, ILiquidStack fluid2, ILiquidStack fluidOut)
	{
		AgitatorRecipe toAdd = new AgitatorRecipe(FacCraftTweakerHelper.toStack(itemIn), ItemStack.EMPTY, (FluidStack)fluidOut.getInternal(), (FluidStack)fluid1.getInternal(), (FluidStack)fluid2.getInternal());
		CraftTweakerAPI.apply(new Add(toAdd));
	}
	
	@ZenMethod
	public static void addRecipe(ILiquidStack fluid1, ILiquidStack fluid2, IItemStack itemOut, ILiquidStack fluidOut)
	{
		AgitatorRecipe toAdd = new AgitatorRecipe(ItemStack.EMPTY, FacCraftTweakerHelper.toStack(itemOut), (FluidStack)fluidOut.getInternal(), (FluidStack)fluid1.getInternal(), (FluidStack)fluid2.getInternal());
		CraftTweakerAPI.apply(new Add(toAdd));
	}
	
	@ZenMethod
	public static void removeRecipe(ILiquidStack liquidOut, IItemStack itemOut)
	{
		CraftTweakerAPI.apply(new Remove((FluidStack)liquidOut.getInternal(), (ItemStack)itemOut.getInternal()));
	}
	
	private static class Add implements IAction
	{
		private AgitatorRecipe recipe;
		
		public Add(AgitatorRecipe r)
		{
			recipe = r;
		}
		
		@Override
		public void apply() {
			MachineRecipes.AGITATOR.add(recipe);
		}

		@Override
		public String describe() {
			// TODO Auto-generated method stub
			return "Adding Agitator recipe";
		}
	}
	
	private static class Remove implements IAction
	{
		FluidStack fluidOut;
		ItemStack itemOut;
		
		public Remove(FluidStack fs, ItemStack is)
		{
			fluidOut = fs;
			itemOut = is;
		}
		@Override
		public void apply()
		{
			for (int i=MachineRecipes.AGITATOR.size(); i>=0; i--)
			{
				AgitatorRecipe r = MachineRecipes.AGITATOR.get(i);
				if (r.getOutputFluid().isFluidEqual(fluidOut) &&
					r.getOutputFluid().amount == fluidOut.amount &&
					FacStackHelper.areItemStacksIdentical(r.getOutputItem(), itemOut))
				{
					MachineRecipes.AGITATOR.remove(i);
				}
			}
		}

		@Override
		public String describe() {
			return "Removing Agitator recipe";
		}
	}
}