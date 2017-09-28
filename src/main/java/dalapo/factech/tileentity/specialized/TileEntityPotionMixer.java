package dalapo.factech.tileentity.specialized;

import java.util.ArrayList;
import java.util.List;

import dalapo.factech.helper.FacStackHelper;
import dalapo.factech.init.ItemRegistry;
import dalapo.factech.reference.PartList;
import dalapo.factech.tileentity.TileEntityMachine;
import dalapo.factech.tileentity.TileEntityMachine.MachinePart;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class TileEntityPotionMixer extends TileEntityMachine {

	// Fun fact: The Potion Mixer was among the first machines thought up and coded.
	public TileEntityPotionMixer() {
		super("potionmixer", 3, 2, 1, RelativeSide.BACK);
		setDisplayName("Potion Mixer");
	}

	@Override
	protected void fillMachineParts() {
		partsNeeded[0] = new MachinePart(PartList.MIXER, 0.05F, 1.05F, 6);
		partsNeeded[1] = new MachinePart(PartList.GEAR, 0.05F, 1.01F, 0.8F, 0);
	}
	
	@Override
	public void getHasWork()
	{
		hasWork = getOutput().isEmpty() && FacStackHelper.matchAny(true, new ItemStack(Items.POTIONITEM), getInput(0), getInput(1), getInput(2)) != -1;
	}

	private void addEffectsSafely(List<PotionEffect> effects, List<PotionEffect> toAdd)
	{
		for (PotionEffect effect : toAdd)
		{
			boolean flag = true;
			for (PotionEffect existing : effects)
			{
				if (effect.getPotion().equals(existing.getPotion())) flag = false;
			}
			if (flag)
			{
				effects.add(effect);
			}
		}
	}
	
	@Override
	protected boolean performAction() {
		List<PotionEffect> effects = new ArrayList<PotionEffect>();
		for (int i=0; i<3; i++)
		{
			ItemStack is = getInput(i);
			if (!is.getItem().equals(Items.POTIONITEM)) continue;
			addEffectsSafely(effects, PotionUtils.getEffectsFromStack(is));
//			effects.addAll(PotionUtils.getEffectsFromStack(is));
			decrStackSize(i, 1);
		}
		ItemStack potion = new ItemStack(Items.POTIONITEM);
		potion.setStackDisplayName("Iridescent Potion");
		PotionUtils.appendEffects(potion, effects);
		setOutput(potion);
		return true;
	}

	@Override
	public int getOpTime() {
		return 120;
	}
	
	@Override
	protected boolean canRun()
	{
		return super.canRun() && hasWork;
	}

}