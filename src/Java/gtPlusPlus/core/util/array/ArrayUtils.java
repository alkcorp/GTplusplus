package gtPlusPlus.core.util.array;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArrayUtils {

	public static void expandArray(final Object[] someArray, final Object newValueToAdd) {
		Object[] series = someArray;
		series = addElement(series, newValueToAdd);
	}

	private static Object[] addElement(Object[] series, final Object newValueToAdd) {
		series  = Arrays.copyOf(series, series.length + 1);
		series[series.length - 1] = newValueToAdd;
		return series;
	}

	public static ItemStack[] removeNulls(final ItemStack[] v) {
		List<ItemStack> list = new ArrayList<ItemStack>(Arrays.asList(v));
		list.removeAll(Collections.singleton((ItemStack)null));
		return list.toArray(new ItemStack[list.size()]);
	}
}
