package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.*;

import java.io.IOException;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import gtPlusPlus.preloader.DevHelper;


public class ClassTransformer_GC_EntityAutoRocket {	

	//The qualified name of the class we plan to transform.
	private static final String className = "micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket";
	//micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket

	private final boolean isValid;
	private final ClassReader reader;
	private final ClassWriter writer;
	private final boolean isObfuscated;

	public ClassTransformer_GC_EntityAutoRocket(byte[] basicClass, boolean obfuscated) {
		
		ClassReader aTempReader = null;
		ClassWriter aTempWriter = null;
		
		isObfuscated = obfuscated;
		
		aTempReader = new ClassReader(basicClass);
		aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);
		aTempReader.accept(new localClassVisitor(aTempWriter), 0);	
		
		if (aTempReader != null && aTempWriter != null) {
			isValid = true;
		}
		else {
			isValid = false;
		}
		reader = aTempReader;
		writer = aTempWriter;

		if (reader != null && writer != null) {
			injectMethod();
		}
		else {
			FMLRelaunchLog.log("[GT++ ASM] Galacticraft EntityAutoRocket Patch", Level.INFO, "Failed to Inject new code.");	
		}

	}

	public boolean isValidTransformer() {
		return isValid;
	}

	public ClassReader getReader() {
		return reader;
	}

	public ClassWriter getWriter() {
		return writer;
	}

	public void injectMethod() {

		String aEntityPlayer = isObfuscated ? DevHelper.getObfuscated("net/minecraft/entity/player/EntityPlayer") : "net/minecraft/entity/player/EntityPlayer";
		String aEntityPlayerMP = isObfuscated ? DevHelper.getObfuscated("net/minecraft/entity/player/EntityPlayerMP") : "net/minecraft/entity/player/EntityPlayerMP";
		String aWorld = isObfuscated ? DevHelper.getObfuscated("net/minecraft/world/World") : "net/minecraft/world/World";
		String aItemStack = isObfuscated ? DevHelper.getObfuscated("net/minecraft/item/ItemStack") : "net/minecraft/item/ItemStack";
		String aEntity = isObfuscated ? DevHelper.getObfuscated("net/minecraft/entity/Entity") : "net/minecraft/entity/Entity";
		String aWorldClient = isObfuscated ? DevHelper.getObfuscated("net/minecraft/client/multiplayer/WorldClient") : "net/minecraft/client/multiplayer/WorldClient";
		String aDifficultyEnum = isObfuscated ? DevHelper.getObfuscated("net/minecraft/world/EnumDifficulty") : "net/minecraft/world/EnumDifficulty";
		String aWorldInfo = isObfuscated ? DevHelper.getObfuscated("net/minecraft/world/storage/WorldInfo") : "net/minecraft/world/storage/WorldInfo";
		String aItemInWorldManager = isObfuscated ? DevHelper.getObfuscated("net/minecraft/server/management/ItemInWorldManager") : "net/minecraft/server/management/ItemInWorldManager";
		String aWorldType = isObfuscated ? DevHelper.getObfuscated("net/minecraft/world/WorldType") : "net/minecraft/world/WorldType";
		String aGameType = isObfuscated ? DevHelper.getObfuscated("net/minecraft/world/WorldSettings$GameType") : "net/minecraft/world/WorldSettings$GameType";
		
		if (isValidTransformer()) {
			FMLRelaunchLog.log("[GT++ ASM] Galacticraft EntityAutoRocket Patch", Level.INFO, "Injecting decodePacketdata into "+className+".");	
			MethodVisitor mv = getWriter().visitMethod(ACC_PUBLIC, "decodePacketdata", "(Lio/netty/buffer/ByteBuf;)V", null, null);			

			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(1027, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESPECIAL, "micdoodle8/mods/galacticraft/api/prefab/entity/EntitySpaceshipBase", "decodePacketdata", "(Lio/netty/buffer/ByteBuf;)V", false);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(1029, l1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/xmod/galacticraft/util/GalacticUtils", "getValidFuelForTier", "(L"+aEntity+";)Lnet/minecraftforge/fluids/FluidStack;", false);
			mv.visitVarInsn(ASTORE, 2);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(1030, l2);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readInt", "()I", false);
			mv.visitVarInsn(ISTORE, 3);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(1031, l3);
			mv.visitVarInsn(ALOAD, 2);
			Label l4 = new Label();
			mv.visitJumpInsn(IFNULL, l4);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(1032, l5);
			mv.visitTypeInsn(NEW, "net/minecraftforge/fluids/FluidStack");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraftforge/fluids/FluidStack", "<init>", "(Lnet/minecraftforge/fluids/FluidStack;I)V", false);
			mv.visitVarInsn(ASTORE, 4);
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLineNumber(1033, l6);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitJumpInsn(IFNULL, l4);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLineNumber(1034, l7);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "fuelTank", "Lnet/minecraftforge/fluids/FluidTank;");
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/fluids/FluidTank", "setFluid", "(Lnet/minecraftforge/fluids/FluidStack;)V", false);
			mv.visitLabel(l4);
			mv.visitLineNumber(1038, l4);
			mv.visitFrame(F_APPEND,2, new Object[] {"net/minecraftforge/fluids/FluidStack", INTEGER}, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readBoolean", "()Z", false);
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "landing", "Z");
			Label l8 = new Label();
			mv.visitLabel(l8);
			mv.visitLineNumber(1039, l8);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readInt", "()I", false);
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "destinationFrequency", "I");
			Label l9 = new Label();
			mv.visitLabel(l9);
			mv.visitLineNumber(1041, l9);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readBoolean", "()Z", false);
			Label l10 = new Label();
			mv.visitJumpInsn(IFEQ, l10);
			Label l11 = new Label();
			mv.visitLabel(l11);
			mv.visitLineNumber(1043, l11);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitTypeInsn(NEW, "micdoodle8/mods/galacticraft/api/vector/BlockVec3");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readInt", "()I", false);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readInt", "()I", false);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readInt", "()I", false);
			mv.visitMethodInsn(INVOKESPECIAL, "micdoodle8/mods/galacticraft/api/vector/BlockVec3", "<init>", "(III)V", false);
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "targetVec", "Lmicdoodle8/mods/galacticraft/api/vector/BlockVec3;");
			mv.visitLabel(l10);
			mv.visitLineNumber(1046, l10);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readDouble", "()D", false);
			mv.visitLdcInsn(new Double("8000.0"));
			mv.visitInsn(DDIV);
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "motionX", "D");
			Label l12 = new Label();
			mv.visitLabel(l12);
			mv.visitLineNumber(1047, l12);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readDouble", "()D", false);
			mv.visitLdcInsn(new Double("8000.0"));
			mv.visitInsn(DDIV);
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "motionY", "D");
			Label l13 = new Label();
			mv.visitLabel(l13);
			mv.visitLineNumber(1048, l13);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readDouble", "()D", false);
			mv.visitLdcInsn(new Double("8000.0"));
			mv.visitInsn(DDIV);
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "motionZ", "D");
			Label l14 = new Label();
			mv.visitLabel(l14);
			mv.visitLineNumber(1049, l14);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readDouble", "()D", false);
			mv.visitLdcInsn(new Double("8000.0"));
			mv.visitInsn(DDIV);
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "lastMotionY", "D");
			Label l15 = new Label();
			mv.visitLabel(l15);
			mv.visitLineNumber(1050, l15);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readDouble", "()D", false);
			mv.visitLdcInsn(new Double("8000.0"));
			mv.visitInsn(DDIV);
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "lastLastMotionY", "D");
			Label l16 = new Label();
			mv.visitLabel(l16);
			mv.visitLineNumber(1052, l16);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "cargoItems", "[L"+aItemStack+";");
			Label l17 = new Label();
			mv.visitJumpInsn(IFNONNULL, l17);
			Label l18 = new Label();
			mv.visitLabel(l18);
			mv.visitLineNumber(1054, l18);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "getSizeInventory", "()I", false);
			mv.visitTypeInsn(ANEWARRAY, aItemStack);
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "cargoItems", "[L"+aItemStack+";");
			mv.visitLabel(l17);
			mv.visitLineNumber(1057, l17);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readBoolean", "()Z", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "setWaitForPlayer", "(Z)V", false);
			Label l19 = new Label();
			mv.visitLabel(l19);
			mv.visitLineNumber(1059, l19);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, "cpw/mods/fml/common/network/ByteBufUtils", "readUTF8String", "(Lio/netty/buffer/ByteBuf;)Ljava/lang/String;", false);
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "statusMessage", "Ljava/lang/String;");
			Label l20 = new Label();
			mv.visitLabel(l20);
			mv.visitLineNumber(1060, l20);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "statusMessage", "Ljava/lang/String;");
			mv.visitLdcInsn("");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
			Label l21 = new Label();
			mv.visitJumpInsn(IFEQ, l21);
			mv.visitInsn(ACONST_NULL);
			Label l22 = new Label();
			mv.visitJumpInsn(GOTO, l22);
			mv.visitLabel(l21);
			mv.visitFrame(F_SAME1, 0, null, 1, new Object[] {"micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket"});
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "statusMessage", "Ljava/lang/String;");
			mv.visitLabel(l22);
			mv.visitFrame(F_FULL, 4, new Object[] {"micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "io/netty/buffer/ByteBuf", "net/minecraftforge/fluids/FluidStack", INTEGER}, 2, new Object[] {"micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "java/lang/String"});
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "statusMessage", "Ljava/lang/String;");
			Label l23 = new Label();
			mv.visitLabel(l23);
			mv.visitLineNumber(1061, l23);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readInt", "()I", false);
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "statusMessageCooldown", "I");
			Label l24 = new Label();
			mv.visitLabel(l24);
			mv.visitLineNumber(1062, l24);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readInt", "()I", false);
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "lastStatusMessageCooldown", "I");
			Label l25 = new Label();
			mv.visitLabel(l25);
			mv.visitLineNumber(1063, l25);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readBoolean", "()Z", false);
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "statusValid", "Z");
			Label l26 = new Label();
			mv.visitLabel(l26);
			mv.visitLineNumber(1066, l26);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "worldObj", "L"+aWorld+";");
			mv.visitFieldInsn(GETFIELD, aWorld, "isRemote", "Z");
			Label l27 = new Label();
			mv.visitJumpInsn(IFEQ, l27);
			Label l28 = new Label();
			mv.visitLabel(l28);
			mv.visitLineNumber(1068, l28);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readInt", "()I", false);
			mv.visitVarInsn(ISTORE, 4);
			Label l29 = new Label();
			mv.visitLabel(l29);
			mv.visitLineNumber(1069, l29);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "riddenByEntity", "L"+aEntity+";");
			Label l30 = new Label();
			mv.visitJumpInsn(IFNONNULL, l30);
			Label l31 = new Label();
			mv.visitLabel(l31);
			mv.visitLineNumber(1071, l31);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitInsn(ICONST_M1);
			mv.visitJumpInsn(IF_ICMPLE, l27);
			Label l32 = new Label();
			mv.visitLabel(l32);
			mv.visitLineNumber(1073, l32);
			mv.visitMethodInsn(INVOKESTATIC, "cpw/mods/fml/client/FMLClientHandler", "instance", "()Lcpw/mods/fml/client/FMLClientHandler;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "cpw/mods/fml/client/FMLClientHandler", "getWorldClient", "()L"+aWorldClient+";", false);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aWorldClient+"", "getEntityByID", "(I)L"+aEntity+";", false);
			mv.visitVarInsn(ASTORE, 5);
			Label l33 = new Label();
			mv.visitLabel(l33);
			mv.visitLineNumber(1074, l33);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitJumpInsn(IFNULL, l27);
			Label l34 = new Label();
			mv.visitLabel(l34);
			mv.visitLineNumber(1076, l34);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitFieldInsn(GETFIELD, ""+aEntity+"", "dimension", "I");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "dimension", "I");
			Label l35 = new Label();
			mv.visitJumpInsn(IF_ICMPEQ, l35);
			Label l36 = new Label();
			mv.visitLabel(l36);
			mv.visitLineNumber(1078, l36);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitTypeInsn(INSTANCEOF, ""+aEntityPlayer+"");
			mv.visitJumpInsn(IFEQ, l27);
			Label l37 = new Label();
			mv.visitLabel(l37);
			mv.visitLineNumber(1080, l37);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "dimension", "I");
			mv.visitVarInsn(ALOAD, 5);
			mv.visitFieldInsn(GETFIELD, ""+aEntity+"", "worldObj", "L"+aWorld+";");
			mv.visitFieldInsn(GETFIELD, aWorld, "difficultySetting", "L"+aDifficultyEnum+";");
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aDifficultyEnum+"", "getDifficultyId", "()I", false);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitFieldInsn(GETFIELD, ""+aEntity+"", "worldObj", "L"+aWorld+";");
			mv.visitMethodInsn(INVOKEVIRTUAL, aWorld, "getWorldInfo", "()L"+aWorldInfo+";", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aWorldInfo+"", "getTerrainType", "()L"+aWorldType+";", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, aWorldType, "getWorldTypeName", "()Ljava/lang/String;", false);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitTypeInsn(CHECKCAST, ""+aEntityPlayerMP+"");
			mv.visitFieldInsn(GETFIELD, ""+aEntityPlayerMP+"", "theItemInWorldManager", "L"+aItemInWorldManager+";");
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aItemInWorldManager+"", "getGameType", "()L"+aGameType+";", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aGameType+"", "getID", "()I", false);
			mv.visitMethodInsn(INVOKESTATIC, "micdoodle8/mods/galacticraft/core/util/WorldUtil", "forceRespawnClient", "(IILjava/lang/String;I)L"+aEntityPlayer+";", false);
			mv.visitVarInsn(ASTORE, 5);
			Label l38 = new Label();
			mv.visitLabel(l38);
			mv.visitLineNumber(1081, l38);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aEntity+"", "mountEntity", "(L"+aEntity+";)V", false);
			Label l39 = new Label();
			mv.visitLabel(l39);
			mv.visitLineNumber(1083, l39);
			mv.visitJumpInsn(GOTO, l27);
			mv.visitLabel(l35);
			mv.visitLineNumber(1085, l35);
			mv.visitFrame(F_APPEND,2, new Object[] {INTEGER, ""+aEntity+""}, 0, null);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aEntity+"", "mountEntity", "(L"+aEntity+";)V", false);
			Label l40 = new Label();
			mv.visitLabel(l40);
			mv.visitLineNumber(1088, l40);
			mv.visitJumpInsn(GOTO, l27);
			mv.visitLabel(l30);
			mv.visitLineNumber(1089, l30);
			mv.visitFrame(F_CHOP,1, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "riddenByEntity", "L"+aEntity+";");
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aEntity+"", "getEntityId", "()I", false);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitJumpInsn(IF_ICMPEQ, l27);
			Label l41 = new Label();
			mv.visitLabel(l41);
			mv.visitLineNumber(1091, l41);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitInsn(ICONST_M1);
			Label l42 = new Label();
			mv.visitJumpInsn(IF_ICMPNE, l42);
			Label l43 = new Label();
			mv.visitLabel(l43);
			mv.visitLineNumber(1093, l43);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "riddenByEntity", "L"+aEntity+";");
			mv.visitInsn(ACONST_NULL);
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aEntity+"", "mountEntity", "(L"+aEntity+";)V", false);
			Label l44 = new Label();
			mv.visitLabel(l44);
			mv.visitLineNumber(1094, l44);
			mv.visitJumpInsn(GOTO, l27);
			mv.visitLabel(l42);
			mv.visitLineNumber(1097, l42);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitMethodInsn(INVOKESTATIC, "cpw/mods/fml/client/FMLClientHandler", "instance", "()Lcpw/mods/fml/client/FMLClientHandler;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "cpw/mods/fml/client/FMLClientHandler", "getWorldClient", "()L"+aWorldClient+";", false);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aWorldClient+"", "getEntityByID", "(I)L"+aEntity+";", false);
			mv.visitVarInsn(ASTORE, 5);
			Label l45 = new Label();
			mv.visitLabel(l45);
			mv.visitLineNumber(1098, l45);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitJumpInsn(IFNULL, l27);
			Label l46 = new Label();
			mv.visitLabel(l46);
			mv.visitLineNumber(1100, l46);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitFieldInsn(GETFIELD, ""+aEntity+"", "dimension", "I");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "dimension", "I");
			Label l47 = new Label();
			mv.visitJumpInsn(IF_ICMPEQ, l47);
			Label l48 = new Label();
			mv.visitLabel(l48);
			mv.visitLineNumber(1102, l48);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitTypeInsn(INSTANCEOF, ""+aEntityPlayer+"");
			mv.visitJumpInsn(IFEQ, l27);
			Label l49 = new Label();
			mv.visitLabel(l49);
			mv.visitLineNumber(1104, l49);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "dimension", "I");
			mv.visitVarInsn(ALOAD, 5);
			mv.visitFieldInsn(GETFIELD, ""+aEntity+"", "worldObj", "L"+aWorld+";");
			mv.visitFieldInsn(GETFIELD, aWorld, "difficultySetting", "L"+aDifficultyEnum+";");
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aDifficultyEnum+"", "getDifficultyId", "()I", false);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitFieldInsn(GETFIELD, ""+aEntity+"", "worldObj", "L"+aWorld+";");
			mv.visitMethodInsn(INVOKEVIRTUAL, aWorld, "getWorldInfo", "()L"+aWorldInfo+";", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aWorldInfo+"", "getTerrainType", "()L"+aWorldType+";", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, aWorldType, "getWorldTypeName", "()Ljava/lang/String;", false);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitTypeInsn(CHECKCAST, ""+aEntityPlayerMP+"");
			mv.visitFieldInsn(GETFIELD, ""+aEntityPlayerMP+"", "theItemInWorldManager", "L"+aItemInWorldManager+";");
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aItemInWorldManager+"", "getGameType", "()L"+aGameType+";", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aGameType+"", "getID", "()I", false);
			mv.visitMethodInsn(INVOKESTATIC, "micdoodle8/mods/galacticraft/core/util/WorldUtil", "forceRespawnClient", "(IILjava/lang/String;I)L"+aEntityPlayer+";", false);
			mv.visitVarInsn(ASTORE, 5);
			Label l50 = new Label();
			mv.visitLabel(l50);
			mv.visitLineNumber(1105, l50);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aEntity+"", "mountEntity", "(L"+aEntity+";)V", false);
			Label l51 = new Label();
			mv.visitLabel(l51);
			mv.visitLineNumber(1107, l51);
			mv.visitJumpInsn(GOTO, l27);
			mv.visitLabel(l47);
			mv.visitLineNumber(1109, l47);
			mv.visitFrame(F_APPEND,1, new Object[] {""+aEntity+""}, 0, null);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aEntity+"", "mountEntity", "(L"+aEntity+";)V", false);
			mv.visitLabel(l27);
			mv.visitLineNumber(1114, l27);
			mv.visitFrame(F_CHOP,2, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, "cpw/mods/fml/common/network/ByteBufUtils", "readUTF8String", "(Lio/netty/buffer/ByteBuf;)Ljava/lang/String;", false);
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "statusColour", "Ljava/lang/String;");
			Label l52 = new Label();
			mv.visitLabel(l52);
			mv.visitLineNumber(1115, l52);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "statusColour", "Ljava/lang/String;");
			mv.visitLdcInsn("");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
			Label l53 = new Label();
			mv.visitJumpInsn(IFEQ, l53);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ACONST_NULL);
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "statusColour", "Ljava/lang/String;");
			mv.visitLabel(l53);
			mv.visitLineNumber(1116, l53);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitInsn(RETURN);
			Label l54 = new Label();
			mv.visitLabel(l54);
			mv.visitLocalVariable("this", "Lmicdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket;", null, l0, l54, 0);
			mv.visitLocalVariable("buffer", "Lio/netty/buffer/ByteBuf;", null, l0, l54, 1);
			mv.visitLocalVariable("g", "Lnet/minecraftforge/fluids/FluidStack;", null, l2, l54, 2);
			mv.visitLocalVariable("aBufferData", "I", null, l3, l54, 3);
			mv.visitLocalVariable("s", "Lnet/minecraftforge/fluids/FluidStack;", null, l6, l4, 4);
			mv.visitLocalVariable("shouldBeMountedId", "I", null, l29, l27, 4);
			mv.visitLocalVariable("e", "L"+aEntity+";", null, l33, l40, 5);
			mv.visitLocalVariable("e", "L"+aEntity+";", null, l45, l27, 5);
			mv.visitMaxs(6, 6);
			mv.visitEnd();

		}
	}

	public static final class localClassVisitor extends ClassVisitor {

		public localClassVisitor(ClassVisitor cv) {
			super(ASM5, cv);
			FMLRelaunchLog.log("[GT++ ASM] Galacticraft EntityAutoRocket Patch", Level.INFO, "Inspecting Class "+className);	
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {			
			if (name.equals("decodePacketdata")) {
				FMLRelaunchLog.log("[GT++ ASM] Galacticraft EntityAutoRocket Patch", Level.INFO, "Removing method "+name);	
				return null;
			}
			MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
			return methodVisitor;
		}
	}

}
