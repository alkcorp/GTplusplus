package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ACC_SYNCHRONIZED;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ASM5;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

import java.lang.reflect.Field;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class ClassTransformer_LWJGL_Keyboard {

	private final boolean isValid;
	private final ClassReader reader;
	private final ClassWriter writer;

	/**
	 * Gets a key's name
	 * 
	 * @param key The key
	 * @return a String with the key's human readable name in it or null if the key
	 *         is unnamed
	 */
	public static synchronized String getKeyName(int key) {
		if (init()) {
			String[] aTemp = getKeyName();
			if (key < aTemp.length && key >= 0) {
				return aTemp[key];
			}
		}
		return getKeyName()[0x00]; // Return nothing
	}

	@SuppressWarnings("rawtypes")
	private static Class mKeyboard;
	private static Field mKeyName;

	@SuppressWarnings("rawtypes")
	private static boolean init() {
		if (mKeyName != null) {
			return true;
		}
		Class aKeyboard = ReflectionUtils.getClass("org.lwjgl.input.Keyboard");
		if (aKeyboard != null) {
			mKeyboard = aKeyboard;
			Field aKeyName = ReflectionUtils.getField(mKeyboard, "keyName");
			if (aKeyName != null) {
				mKeyName = aKeyName;
			}
		}
		return mKeyName != null;
	}

	private static String[] getKeyName() {
		if (init()) {
			try {
				Object o = mKeyName.get(null);
				if (o instanceof String[]) {
					String[] y = (String[]) o;
					return y;
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
			}
		}
		return new String[] {};
	}

	public ClassTransformer_LWJGL_Keyboard(byte[] basicClass) {
		ClassReader aTempReader = null;
		ClassWriter aTempWriter = null;
		aTempReader = new ClassReader(basicClass);
		aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);
		aTempReader.accept(new AddFieldAdapter(aTempWriter), 0);
		injectMethod("getKeyName", aTempWriter);
		if (aTempReader != null && aTempWriter != null) {
			isValid = true;
		} else {
			isValid = false;
		}
		FMLRelaunchLog.log("[GT++ ASM] LWJGL Keybinding index out of bounds fix", Level.INFO,
				"Valid? " + isValid + ".");
		reader = aTempReader;
		writer = aTempWriter;
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

	public boolean injectMethod(String aMethodName, ClassWriter cw) {
		MethodVisitor mv;
		boolean didInject = false;
		FMLRelaunchLog.log("[GT++ ASM] LWJGL Keybinding index out of bounds fix", Level.INFO,
				"Injecting " + aMethodName + ".");
		if (aMethodName.equals("getKeyName")) {
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC + ACC_SYNCHRONIZED, "getKeyName", "(I)Ljava/lang/String;", null,
					null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(49, l0);
			mv.visitVarInsn(ILOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/preloader/asm/transformers/ClassTransformer_LWJGL_Keyboard",
					"getKeyName", "(I)Ljava/lang/String;", false);
			mv.visitInsn(ARETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("key", "I", null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
			didInject = true;
		}
		FMLRelaunchLog.log("[GT++ ASM] LWJGL Keybinding index out of bounds fix", Level.INFO,
				"Method injection complete.");
		return didInject;
	}

	public class AddFieldAdapter extends ClassVisitor {

		public AddFieldAdapter(ClassVisitor cv) {
			super(ASM5, cv);
			this.cv = cv;
		}

		private final String[] aMethodsToStrip = new String[] { "getKeyName" };

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor methodVisitor;
			boolean found = false;

			for (String s : aMethodsToStrip) {
				if (name.equals(s)) {
					found = true;
					break;
				}
			}
			if (!found) {
				methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
			} else {
				methodVisitor = null;
			}
			if (found) {
				FMLRelaunchLog.log("[GT++ ASM] LWJGL Keybinding index out of bounds fix", Level.INFO,
						"Found method " + name + ", removing.");
			}
			return methodVisitor;
		}

	}

}
