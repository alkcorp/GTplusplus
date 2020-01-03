package gtPlusPlus.preloader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CORE_Preloader {
	public static final String NAME = "GT++ Preloader";
	public static final String MODID = "GT++_Preloader";
	public static final String VERSION = "0.4-Beta";
	public static boolean enableOldGTcircuits = false;
	public static int enableWatchdogBGM = 0;
	public static List<?> DEPENDENCIES = new ArrayList<>(Arrays.asList(new String[] {"required-before:gregtech;"}));
}
