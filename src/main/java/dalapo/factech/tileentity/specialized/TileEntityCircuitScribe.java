package dalapo.factech.tileentity.specialized;

import java.util.Map;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import dalapo.factech.auxiliary.MachineRecipes;
import dalapo.factech.helper.FacMathHelper;
import dalapo.factech.helper.FacStackHelper;
import dalapo.factech.helper.Logger;
import dalapo.factech.init.ItemRegistry;
import dalapo.factech.reference.PartList;
import dalapo.factech.tileentity.TileEntityBasicProcessor;
import dalapo.factech.tileentity.TileEntityMachine;

public class TileEntityCircuitScribe extends TileEntityBasicProcessor {

	private byte pattern;
	public TileEntityCircuitScribe() {
		super("circuitscribe", 3, RelativeSide.BOTTOM);
		setDisplayName("Circuit Scribe");
		pattern = -1;
	}
	
	// Converts Quartz Plates to Empty Circuit Boards

	public int getPattern()
	{
		return pattern;
	}
	
	public void setPattern(byte p)
	{
		if (FacMathHelper.isInRange(p, -1, 4)) pattern = p;
	}
	
	@Override
	public void getHasWork()
	{
		if (getInput(0).getItem().equals(ItemRegistry.circuitIntermediate) &&
				getInput(0).getItemDamage() == 8 &&
				pattern != -1 && 
				(getOutput().isEmpty() || (getOutput().getItemDamage() == pattern && getOutput().getCount() <= 64))) hasWork = true;
		else hasWork = false;
	}
	
	@Override
	protected void fillMachineParts() {
		partsNeeded[0] = new MachinePart(PartList.HEATELEM, 0.15F, 1.2F, 0.8F*kValue[0][1], (int)(12*kValue[0][0]));
		partsNeeded[1] = new MachinePart(PartList.BLADE, 0.2F, 1.05F, 0.2F*kValue[1][1], (int)(4*kValue[1][0]));
		partsNeeded[2] = new MachinePart(PartList.MOTOR, 0.1F, 1.2F, 0.6F*kValue[2][1], (int)(8*kValue[2][0]));
	}

	@Override
	protected boolean performAction()
	{
		getInput(0).shrink(1);
		if (getOutput().isEmpty())
		{
			setOutput(new ItemStack(ItemRegistry.circuitIntermediate, 1, pattern));
		}
		else getOutput().grow(1);
		getHasWork();
		return true;
	}

	@Override
	public int getOpTime() {
		return 240;
	}

	@Override
	protected Map<ItemStack, ItemStack> getRecipeList() {
		return MachineRecipes.CIRCUIT_SCRIBE;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setByte("pattern", pattern);
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		if (nbt.hasKey("pattern"))
		{
			pattern = nbt.getByte("pattern");
		}
	}
}
