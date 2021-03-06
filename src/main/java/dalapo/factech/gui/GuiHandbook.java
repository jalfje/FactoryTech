package dalapo.factech.gui;

import java.io.IOException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

import dalapo.factech.FactoryTech;
import dalapo.factech.gui.widget.WidgetCustomButton;
import dalapo.factech.helper.FacGuiHelper;
import dalapo.factech.helper.FacMathHelper;
import dalapo.factech.helper.Logger;
import dalapo.factech.reference.NameList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.Language;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class GuiHandbook extends GuiScreen
{
	private static final int GRID_X_OFFSET = 124;
	private static final int GRID_Y_OFFSET = 96;
	
	private static int[] backgroundLookup = new int[64];
	
	private ItemStack book;
	
	
	private static final int[] pageCounts = new int[7];
	private int guiLeft;
	private int guiTop;
	private int xSize = 256;
	private int ySize = 160;
	private boolean drawGrid = false;
	private boolean drawBoard = false;
	private ItemStack[][] recipe;
	private boolean[][] board;
	private ItemStack result;
	GuiButton[] buttons = new GuiButton[10];
	
	int page = 0;
	int numPages = 0;
	int section = -1;
	
	String title = "";
	String body = "";
	
	List<IRecipe> recipes = new ArrayList<IRecipe>();
	
	public static void initBackgrounds()
	{
		for (int i=0; i<backgroundLookup.length; i++)
		{
			backgroundLookup[i] = FactoryTech.random.nextInt(4);
		}
	}
	
	public static void setPageCount(int section, int count)
	{
		if (FacMathHelper.isInRange(section, 0, pageCounts.length))
		{
			pageCounts[section] = count;
		}
	}
	
	public GuiHandbook(ItemStack is)
	{
		super();
		
		book = is;
		if (is.hasTagCompound())
		{
			NBTTagCompound nbt = is.getTagCompound();
			if (nbt.hasKey("section")) section = nbt.getInteger("section");
			else section = -1;
			page = nbt.getInteger("page");
		}
		recipe = new ItemStack[3][3];
		resetGrid();
	}
	
	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		if (!book.hasTagCompound())
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setInteger("section", section);
			nbt.setInteger("page", page);
			book.setTagCompound(nbt);
		}
		else
		{
			book.getTagCompound().setInteger("section", section);
			book.getTagCompound().setInteger("page", page);
		}
	}
	
	private void resetGrid()
	{
		recipes.clear();
		for (int i=0; i<3; i++)
		{
			for (int j=0; j<3; j++)
			{
				recipe[i][j] = ItemStack.EMPTY;
			}
		}
		result = ItemStack.EMPTY;
	}
	
	private String getFileFromSection()
	{
//		Logger.info(Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getJavaLocale());
		switch (section)
		{
		case 1:
			return "machine";
		case 2:
			return "part";
		case 3:
			return "tool";
		case 4:
			return "automation";
		case 5:
			return "resource";
		case 6:
			return "misc";
		default:
			return "basic";
		}
	}
	
	
	
	@Override
	public void initGui()
	{
		String[] names = new String[] {"The Basics", "Machine info", "Part specifications", "Tools and Devices", "Automation components", "Resources", "Miscellaneous"};
		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
		for (int i=0; i<names.length; i++)
		{
			addButton(buttons[i] = new WidgetCustomButton(this, i, guiLeft + 66, guiTop + 28 + (i*14), 120, 12, names[i], "button_handbook"));
		}
		
		addButton(buttons[7] = new GuiButton(7, guiLeft + 4, guiTop + 136, 30, 20, "<-"));
		addButton(buttons[8] = new GuiButton(8, guiLeft + 34, guiTop + 136, 30, 20, "X"));
		addButton(buttons[9] = new GuiButton(9, guiLeft + 64, guiTop + 136, 30, 20, "->"));
		refresh();
	}
	
	
	
	private boolean[][] getCircuitBoard(int pattern)
	{
		return GuiCircuitScribe.getBoard(pattern);
	}
	
	private void showRecipes()
	{
//		ResourceLocation loc = new ResourceLocation(NameList.MODID, location);
//		IRecipe recipe = CraftingManager.getRecipe(loc);
		
		if (recipes.size() != 0)
		{
//			Logger.info(recipes.size());
			for (int i=0; i<3; i++)
			{
				for (int j=0; j<3; j++)
				{
					recipe[i][j] = ItemStack.EMPTY;
				}
			}
			IRecipe recipe = recipes.get((int)((System.currentTimeMillis() / 1000) % recipes.size()));
			for(int row=0; row<3; row++)
			{
				for (int col=0; col<3; col++)
				{
					// TODO: Fix this in a more elegant way than try/catch
					try {
						if (recipe.getIngredients().get(row*3 + col).getMatchingStacks().length > 0)
						{
							ItemStack is = recipe.getIngredients().get(row*3 + col).getMatchingStacks()[0];
							this.recipe[row][col] = is;
						}
					}
					catch (ArrayIndexOutOfBoundsException | NullPointerException ex)
					{
						break;
					}
				}
			}
			result = recipe.getRecipeOutput();
		}
		else
		{
			resetGrid();
		}
	}
	
	private void refresh()
	{
		drawGrid = false;
		title = "";
		body = "";
		recipes.clear();
		
		if (section == -1)
		{
			page = 0;
			for (int i=0; i<7; i++)
			{
				buttons[i].visible = true;
				buttons[i].enabled = true;
			}
			for (int i=7; i<10; i++)
			{
				buttons[i].visible = false;
				buttons[i].enabled = false;
			}
		}
		else
		{
			for (int i=0; i<7; i++)
			{
				buttons[i].visible = false;
				buttons[i].enabled = false;
			}
			for (int i=7; i<10; i++)
			{
				buttons[i].visible = true;
				buttons[i].enabled = true;
			}
			
			IResource textRes;
			try {
				textRes = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(NameList.MODID, "text/" + getFileFromSection() + ".txt"));
			}
			catch (IOException e)
			{
				Logger.error(String.format("Text file %s.txt not found.", getFileFromSection()));
				section = -1;
				return;
			}
			Scanner in = new Scanner(textRes.getInputStream());
			if (!in.hasNextLine()) // File is empty
			{
				in.close();
				return;
			}
			numPages = pageCounts[section];
			int i = 0;
			while (in.hasNextLine() && i <= page)
			{
				String str = in.nextLine();
				if (str.isEmpty()) continue;
				
				if (str.startsWith("$title "))
				{
					title = str.substring(7);
					drawGrid = false;
					drawBoard = false;
				}
				else if (str.startsWith("$recipe"))
				{
					recipes.add(CraftingManager.getRecipe(new ResourceLocation(NameList.MODID, str.substring(8))));
					drawGrid = true;
				}
				else if (str.startsWith("$pattern"))
				{
					try {
						board = getCircuitBoard(Integer.parseInt(str.substring(9)));
					}
					catch (NumberFormatException e)
					{
						e.printStackTrace();
						continue;
					}
					drawBoard = true;
				}
				else if (str.equals("$end"))
				{
					i++;
				}
				else
				{
					if (i == page) body += "\n" + str;
				}
				
				if (!drawGrid)
				{
//					Logger.info("Resetting grid.");
					resetGrid();
				}
			}
			showRecipes();
//			System.out.println(body);
			in.close();
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		ResourceLocation guiTex = new ResourceLocation(NameList.MODID, "textures/gui/handbook_gui_" + backgroundLookup[((section+1)*8 + page) % 64] + ".png");
		mc.getTextureManager().bindTexture(guiTex);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		if (drawGrid)
		{
			showRecipes();
			this.drawTexturedModalRect(guiLeft + GRID_X_OFFSET - 1, guiTop + GRID_Y_OFFSET - 1, 0, 160, 116, 54);
			
			for (int row=0; row<3; row++)
			{
				for (int col=0; col<3; col++)
				{
					FacGuiHelper.renderItemStack(recipe[row][col], guiLeft + col*18 + GRID_X_OFFSET, guiTop + row*18 + GRID_Y_OFFSET);
				}
			}
			FacGuiHelper.renderItemStack(result, guiLeft + 94 + GRID_X_OFFSET, guiTop + 18 + GRID_Y_OFFSET);
		}
		if (drawBoard)
		{
			int boardLeft = guiLeft + 64;
			int boardTop = guiTop + 120;
			for (int x=0; x<board.length*16; x+=16)
			{
				for (int y=0; y<board[x/16].length*16; y+=16)
				{
					if (board[x/16][y/16])
					{
						drawRect(boardLeft+x, boardTop-y, boardLeft+x+16, boardTop-y-16, 0xFF808080);
					}
					else
					{
						drawRect(boardLeft+x, boardTop-y, boardLeft+x+16, boardTop-y-16, 0xFFFFFFFF);
					}
				}
			}
		}
		for (GuiButton b : buttonList)
		{
			if (b.visible) b.drawButton(mc, mouseX, mouseY, partialTicks);
		}
		
		
		fontRenderer.drawString(title, guiLeft + 4, guiTop + 4, 0xFFFFFFFF);
		fontRenderer.drawSplitString(body, guiLeft + 4, guiTop + 16, 248, 0xFFFFFFFF);
	}
	
	@Override
	public void actionPerformed(GuiButton b)
	{
		if (b.id <= 6)
		{
			section = b.id;
		}
		else
		{
			switch (b.id)
			{
			case 7:
				
				if (page > 0)
				{
					body = "";
					page--;
				}
				break;
			case 8:
				title = "";
				body = "";
				section = -1;
				break;
			case 9:
				if (page < numPages)
				{
					body = "";
					page++;
				}
				break;
			}
		}
		refresh();
	}
}