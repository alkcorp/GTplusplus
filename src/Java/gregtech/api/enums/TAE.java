package gregtech.api.enums;

import java.lang.reflect.Field;

import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.objects.GT_RenderedTexture;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class TAE {

	//TAE stands for Texture Array Expansion.

	public static int gtTexturesArrayStartOrigin;
	public static int gtPPLastUsedIndex = 64;
	public static int secondaryIndex = 0;
	public static boolean hasArrayBeenExpanded = false;

	public static boolean hookGtTextures() {
		/*ITexture[] textureArrayDump = Textures.BlockIcons.CASING_BLOCKS;
		ITexture[] newTextureArray = new ITexture[1024];
		Utils.LOG_INFO("|======| Texture Array Start Length: "+textureArrayDump.length+" |======|");
		for (int r=0;r<textureArrayDump.length;r++){
			if (textureArrayDump[r] == null){
				Utils.LOG_WARNING("Texture slot "+r+" is empty.");
			}
		}
		gtTexturesArrayStartOrigin = textureArrayDump.length;
		System.arraycopy(textureArrayDump, 0, newTextureArray, 0, textureArrayDump.length);
		Textures.BlockIcons.CASING_BLOCKS  = newTextureArray;
		if (Textures.BlockIcons.CASING_BLOCKS.length == 1024){
			hasArrayBeenExpanded = true;
		}
		else {
			hasArrayBeenExpanded = false;
		}
		return hasArrayBeenExpanded;*/	
		return true;
	}

	/*public static boolean registerTextures(GT_RenderedTexture textureToRegister) {
		Textures.BlockIcons.CASING_BLOCKS[gtPPLastUsedIndex] = textureToRegister;
		//Just so I know registration is done.
		return true;
	}*/

	public static boolean registerTextures(GT_CopiedBlockTexture gt_CopiedBlockTexture) {
		try {
			
			//Handle page 2.
			if (gtPPLastUsedIndex >= 128) {
				if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK && Utils.getGregtechSubVersion() > 30) {
					Field x = ReflectionUtils.getField(Textures.BlockIcons.class, "casingTexturePages");
					if (x != null) {
						ITexture[][] h = (ITexture[][]) x.get(null);
						if (h != null) {
							h[64][secondaryIndex++]  = gt_CopiedBlockTexture;
							x.set(null, h);
							return true;
						}
					}
				}
			}
			
			//set to page 1.
			else {
				Textures.BlockIcons.CASING_BLOCKS[gtPPLastUsedIndex] = gt_CopiedBlockTexture;
				gtPPLastUsedIndex++;
				return true;
			}
		} 
		catch (Throwable t) {
			t.printStackTrace();
		}
		return false;
	}

	public static ITexture getTexture(int index){
		if (gtPPLastUsedIndex >= 128) {
			if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK && Utils.getGregtechSubVersion() > 30) {
				return Textures.BlockIcons.CASING_BLOCKS[((64*128)+index)];
			}
		}
		return Textures.BlockIcons.CASING_BLOCKS[(64+index)];
	}

	public static int GTPP_INDEX(int ID){
		if (gtPPLastUsedIndex >= 128) {
			if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK && Utils.getGregtechSubVersion() > 30) {
				return (ID);
			}
		}
		return (64+ID);		
	}
}
