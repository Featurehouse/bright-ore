package org.featurehouse.mcmod.brightore.compat.indigo.asm;

import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import static org.featurehouse.mcmod.brightore.compat.indigo.asm.IndigoMappingConstants.*;

public final class AsmAoLuminanceFix {
    public static void bootstrap(String targetClassName, ClassNode targetClass) {
        MethodNode method = targetClass.methods.stream().filter(m -> "fixed".equals(m.name) &&
                mapMethodType(vpf).equals(m.desc)).findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing method AoLuminanceFix.fixed in"
                        .concat(targetClassName)));
        var l = method.instructions;
        var invokeVirtual = gln(l);
        l.set(invokeVirtual, rl.mapMethod().asInvokeStatic());
    }

    // INVOKEVIRTUAL net/minecraft/block/BlockState.getLuminance ()I
    private static MethodInsnNode gln(InsnList l) {
        for (AbstractInsnNode node : l) {
            if (node instanceof MethodInsnNode m && bl.mapMethod().matchInsn(m))
                return m;
        } throw new IllegalStateException("Method invocation not found in AoLuminanceFix.fixed: " +
                "BlockState.getLuminance()I | class_2680.method_26213()I");
    }
}

interface IndigoMappingConstants {
    // BlockView: class_1922, BlockPos: class_2338
    String vpf = "(Lnet/minecraft/class_1922;Lnet/minecraft/class_2338;)F";

    // OreTagLoader.redirectLuminance(BlockState)I
    MethodRef rl = new MethodRef(
        "org/featurehouse/mcmod/brightore/OreTagLoader", "redirectLuminance", "(L" +
            "net/minecraft/class_2680)I"
    );

    // BlockState.getLuminance()I
    MethodRef bl = new MethodRef("net/minecraft/class_2680", "method_26213", "()I");

    static String mapClass(String intermediary) {
        return FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary", intermediary);
    }

    record MethodRef(@NonNls String clazz, String name, String desc) {
        public MethodInsnNode asInvokeStatic() {
            return new MethodInsnNode(Opcodes.INVOKESTATIC, clazz, name, desc, false);
        }

        public boolean matchInsn(MethodInsnNode node) {
            return  clazz.equals(node.owner) &&
                    name.equals(node.name) &&
                    desc.equals(node.desc);
        }

        public MethodRef mapMethod() {
            var mappingResolver = FabricLoader.getInstance().getMappingResolver();
            String dot = this.clazz.replaceAll("/", "\\.");
            String clazz = mappingResolver.mapClassName("intermediary", dot);
            String name = mappingResolver.mapMethodName("intermediary", dot, this.name, this.desc);
            String desc = mapMethodType(this.desc);
            return new MethodRef(clazz, name, desc);
        }
    }

    static @NotNull String mapMethodType(String desc) {
        Type methodType = Type.getMethodType(desc);
        Type[] args = methodType.getArgumentTypes();
        int len = args.length;
        for (int i = 0; i < len; i++) {
            args[i] = mapTypeDesc(args[i]);
        }

        Type returnType = mapTypeDesc(methodType.getReturnType());

        return Type.getMethodType(returnType, args).getDescriptor();
    }

    static Type mapTypeDesc(Type type) {
        var srt = type.getSort();
        if (srt <= 8) return type;  // primitives
        if (srt == Type.ARRAY) {
            int dimension = type.getDimensions();
            Type mappedComponent = mapTypeDesc(type.getElementType());
            String descriptor = "[".repeat(dimension).concat(mappedComponent.getDescriptor());
            return Type.getType(descriptor);
        } // Object
        String mappedName = mapClass(type.getClassName());
        return Type.getObjectType(mappedName.replaceAll("\\.", "/"));
    }
}