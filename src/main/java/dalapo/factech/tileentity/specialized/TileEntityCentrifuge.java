package dalapo.factech.tileentity.specialized;

import java.util.Map;

import dalapo.factech.auxiliary.MachineRecipes;
import dalapo.factech.reference.PartList;
import dalapo.factech.tileentity.TileEntityProcessorMultiOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class TileEntityCentrifuge extends TileEntityProcessorMultiOutput {

	public TileEntityCentrifuge() {
		super("centrifuge", 3, 3, RelativeSide.BACK);
		
	}
	
	@Override
	public Map<ItemStack, ItemStack[]> getRecipeList() {
		return MachineRecipes.CENTRIFUGE;
	}

	@Override
	protected void fillMachineParts() {
		partsNeeded[0] = new MachinePart(PartList.MOTOR, 0.1F, 1.07F, 0.75F*kValue[0][1], (int)(4*kValue[0][0]));
		partsNeeded[1] = new MachinePart(PartList.GEAR, 0.2F, 1.1F, 0.7F*kValue[1][1], (int)(6*kValue[1][0]));
		partsNeeded[2] = new MachinePart(PartList.SHAFT, 0.05F, 1.3F, 0.75F*kValue[2][1], (int)(16*kValue[2][0]));
	}

	@Override
	public int getOpTime() {
		return 100;
	}

}
