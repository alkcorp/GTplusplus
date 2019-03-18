package gtPlusPlus.core.util.minecraft.gregtech;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import gregtech.GT_Mod;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.common.GT_Proxy;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.world.chunk.Chunk;

public class PollutionUtils {

	private static boolean mIsPollutionEnabled = true;

	private static Method mAddPollution;
	private static Method mAddPollution2;

	private static Method mGetPollution;
	private static Method mGetPollution2;

	static {
		if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK || CORE.GTNH) {
			mIsPollutionEnabled = mPollution();
		} else {
			mIsPollutionEnabled = false;
		}
	}

	public static boolean mPollution() {
		try {
			GT_Proxy GT_Pollution = GT_Mod.gregtechproxy;
			if (GT_Pollution != null) {
				Field mPollution = ReflectionUtils.getField(GT_Pollution.getClass(), "mPollution");
				if (mPollution != null) {
					return mPollution.getBoolean(GT_Pollution);
				}
			}
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
		}
		return false;
	}

	public static boolean addPollution(IGregTechTileEntity te, int pollutionValue) {
		if (mIsPollutionEnabled)
			try {
				if (te == null) {
					return false;
				}
				if (mAddPollution != null) {
					mAddPollution.invoke(null, te, pollutionValue);
				}
				Class<?> GT_Pollution = ReflectionUtils.getClass("gregtech.common.GT_Pollution");
				if (GT_Pollution != null) {
					Method addPollution = ReflectionUtils.getMethod(GT_Pollution, "addPollution", IGregTechTileEntity.class, int.class);
					if (addPollution != null) {
						mAddPollution = addPollution;
						addPollution.invoke(null, te, pollutionValue);
						return true;
					}
				}
			} catch (SecurityException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
			}
		return false;
	}

	public static boolean addPollution(IHasWorldObjectAndCoords aTileOfSomeSort, int pollutionValue) {
		if (mIsPollutionEnabled)
			try {
				if (aTileOfSomeSort == null) {
					return false;
				}
				IHasWorldObjectAndCoords j = (IHasWorldObjectAndCoords) aTileOfSomeSort;
				Chunk c = j.getWorld().getChunkFromBlockCoords(j.getXCoord(), j.getZCoord());
				return addPollution(c, pollutionValue);
			} catch (SecurityException | IllegalArgumentException e) {
			}
		return false;
	}

	public static boolean addPollution(Chunk aChunk, int pollutionValue) {
		if (mIsPollutionEnabled)
			try {
				if (aChunk == null) {
					return false;
				}
				if (mAddPollution2 != null) {
					mAddPollution2.invoke(null, aChunk, pollutionValue);
					return true;
				}
				Class<?> GT_Pollution = ReflectionUtils.getClass("gregtech.common.GT_Pollution");
				if (GT_Pollution != null) {
					Method addPollution = ReflectionUtils.getMethod(GT_Pollution, "addPollution", Chunk.class, int.class);
					if (addPollution != null) {
						mAddPollution2 = addPollution;
						mAddPollution2.invoke(null, aChunk, pollutionValue);
						return true;
					}
				}
			} catch (SecurityException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
			}
		return false;
	}

	public static boolean removePollution(IGregTechTileEntity te, int pollutionValue) {
		return addPollution(te, -pollutionValue);
	}

	public static boolean removePollution(IHasWorldObjectAndCoords aTileOfSomeSort, int pollutionValue) {
		return addPollution(aTileOfSomeSort, -pollutionValue);
	}

	public static boolean removePollution(Chunk aChunk, int pollutionValue) {
		return addPollution(aChunk, -pollutionValue);
	}

	public static int getPollution(IGregTechTileEntity te) {
		if (mIsPollutionEnabled)
			try {
				if (te == null) {
					return 0;
				}
				if (mGetPollution != null) {
					mGetPollution.invoke(null, te);
				}
				Class<?> GT_Pollution = ReflectionUtils.getClass("gregtech.common.GT_Pollution");
				if (GT_Pollution != null) {
					Method addPollution = ReflectionUtils.getMethod(GT_Pollution, "getPollution", IGregTechTileEntity.class);
					if (addPollution != null) {
						mGetPollution = addPollution;
						return (int) addPollution.invoke(null, te);
					}
				}
			} catch (SecurityException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
			}
		return 0;
	}

	public static int getPollution(Chunk te) {
		if (mIsPollutionEnabled)
			try {
				if (te == null) {
					return 0;
				}
				if (mGetPollution2 != null) {
					mGetPollution2.invoke(null, te);
				}
				Class<?> GT_Pollution = ReflectionUtils.getClass("gregtech.common.GT_Pollution");
				if (GT_Pollution != null) {
					Method addPollution = ReflectionUtils.getMethod(GT_Pollution, "getPollution", Chunk.class);
					if (addPollution != null) {
						mGetPollution2 = addPollution;
						return (int) addPollution.invoke(null, te);
					}
				}
			} catch (SecurityException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
			}
		return 0;
	}

}
