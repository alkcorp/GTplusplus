package GTplusplus;

import gregtech.api.render.SimpleCubeRenderer;

public class GTppTextures {
	public static SimpleCubeRenderer MACERATION_CASING,
	 WASHPLANT_CASING,
	 THERMAL_CASING,
	 FISHER_CASING,
	 ELECTROLYZER_CASING,
	 CHEMICAL_CASING,
	 CENTRIFUGE_CASING,
	 SIEVE_CASING,
	 SIEVE_GRATE,
	 WIRE_CASING;

//public static OrientedOverlayRenderer INDUSTRIAL_MACERATOR_OVERLAY;

static {
	MACERATION_CASING = new SimpleCubeRenderer("casings/industrial/maceration_casing");
	WASHPLANT_CASING = new SimpleCubeRenderer("casings/industrial/washplant_casing");
	THERMAL_CASING = new SimpleCubeRenderer("casings/industrial/thermal_casing");
	FISHER_CASING = new SimpleCubeRenderer("casings/industrial/fisher_casing");
	ELECTROLYZER_CASING = new SimpleCubeRenderer("casings/industrial/electrolyzer_casing");
	CHEMICAL_CASING = new SimpleCubeRenderer("casings/industrial/chemical_casing");
	CENTRIFUGE_CASING = new SimpleCubeRenderer("casings/industrial/centrifuge_casing");
	SIEVE_CASING = new SimpleCubeRenderer("casings/industrial/sieve_casing");
	SIEVE_GRATE = new SimpleCubeRenderer("casings/industrial/sieve_grate");
	WIRE_CASING = new SimpleCubeRenderer("casings/industrial/wire_casing");

//INDUSTRIAL_MACERATOR_OVERLAY = new OrientedOverlayRenderer("machines/coke_oven", new OverlayFace[]{OverlayFace.FRONT});
}
}
