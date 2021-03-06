package dalapo.factech.plugins.jei.wrappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import dalapo.factech.auxiliary.MachineRecipes;
import dalapo.factech.helper.Logger;
import dalapo.factech.plugins.jei.BaseRecipeWrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;

public class StandardRecipeWrapper extends BaseRecipeWrapper {
	

	
	public ItemStack input;
	public ItemStack output;
	
	public StandardRecipeWrapper(IGuiHelper guiHelper, ItemStack in, ItemStack out)
	{
		input = in;
		output = out;
	}
	
	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setInput(ItemStack.class, input);
		ingredients.setOutput(ItemStack.class, output);
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		
	}

	@Override
	public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY,
			int mouseButton) {
//		Logger.info(String.format("{%s, %s}", mouseX, mouseY));
		return false;
	}

}
