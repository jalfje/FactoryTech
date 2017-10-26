package dalapo.factech.tileentity.render;

import net.minecraft.client.renderer.GlStateManager;
import dalapo.factech.helper.FacTesrHelper;
import dalapo.factech.reference.PartList;
import dalapo.factech.tileentity.specialized.TileEntityTemperer;

public class TesrTemperer extends TesrMachine<TileEntityTemperer>
{

	public TesrTemperer(boolean directional) {
		super(directional);
	}

	@Override
	protected String getModelName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doRender(TileEntityTemperer te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if (te.hasPart(0)) // Heating element
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(0, 0.4, 0);
			GlStateManager.rotate(90, 1, 0, 0);
			FacTesrHelper.renderPart(PartList.HEATELEM);
			GlStateManager.popMatrix();
		}
		
		GlStateManager.pushMatrix();
		GlStateManager.rotate(90, 1, 0, 0);
		if (te.successfulWindow())
		{
			FacTesrHelper.renderStack(te.getRecipeOutput());
		}
		else
		{
			FacTesrHelper.renderStack(te.getInput());
		}
		GlStateManager.popMatrix();
		
		
	}

}
