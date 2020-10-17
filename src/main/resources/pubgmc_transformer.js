function initializeCoreMod() {
    Opcodes = Java.type('org.objectweb.asm.Opcodes');
    ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
    InsnList = Java.type('org.objectweb.asm.tree.InsnList');
	LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');
	FieldNode = Java.type('org.objectweb.asm.tree.FieldNode');
	MethodNode = Java.type('org.objectweb.asm.tree.MethodNode');
	AbstractInsnNode = Java.type('org.objectweb.asm.tree.AbstractInsnNode');
	InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
	VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
	FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
	MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
	JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
	TypeInsnNode = Java.type('org.objectweb.asm.tree.TypeInsnNode');

	ACC_PUBLIC = Opcodes.ACC_PUBLIC;
	INVOKESTATIC = Opcodes.INVOKESTATIC;
	INVOKEVIRTUAL = Opcodes.INVOKEVIRTUAL;
	ALOAD = Opcodes.ALOAD;
	ILOAD = Opcodes.ILOAD;
	FLOAD = Opcodes.FLOAD;
	DLOAD = Opcodes.DLOAD;
	ISTORE = Opcodes.ISTORE;
	RETURN = Opcodes.RETURN;
	ARETURN = Opcodes.ARETURN;
	IRETURN = Opcodes.IRETURN;
	DRETURN = Opcodes.DRETURN;
	NEW = Opcodes.NEW;
	ACONST_NULL = Opcodes.ACONST_NULL;
	ICONST_0 = Opcodes.ICONST_0;
	IFEQ = Opcodes.IFEQ;
	IFNE = Opcodes.IFNE;
	IF_ACMPEQ = Opcodes.IF_ACMPEQ;
	GETFIELD = Opcodes.GETFIELD;
	GETSTATIC = Opcodes.GETSTATIC;
	GOTO = Opcodes.GOTO;
	LABEL = AbstractInsnNode.LABEL;
	METHOD_INSN = AbstractInsnNode.METHOD_INSN;

	return {
		'living_renderer': {
			'target': {
				'type': 'METHOD',
				'class': 'net.minecraft.client.renderer.entity.LivingRenderer',
				'methodName': '<init>',
				'methodDesc': '(Lnet/minecraft/client/renderer/entity/EntityRendererManager;Lnet/minecraft/client/renderer/entity/model/EntityModel;F)V'
			},
			'transformer': function(methodNode) {
			    /*
                public LivingRenderer(EntityRendererManager rendererManager, M entityModelIn, float shadowSizeIn) {
                  super(rendererManager);
                  this.entityModel = entityModelIn;
                  this.shadowSize = shadowSizeIn;

                  =====[ PUBGMC START ]=====
                  PubgmcHooks.onLivingRenderCreated(this);
                  =====[ PUBGMC  END  ]=====
                }
                */

			    var instructions = methodNode.instructions;
			    for(var i = instructions.size() - 1; i >= 0; --i) {
			        var instruction = instructions.get(i);
			        if(instruction.getOpcode() === RETURN) {
			            var list = new InsnList();
			            list.add(new VarInsnNode(ALOAD, 0));
			            list.add(new MethodInsnNode(
			                INVOKESTATIC,
			                'dev/toma/pubgmc/PubgmcHooks',
			                'onLivingRenderCreated',
			                '(Lnet/minecraft/client/renderer/entity/LivingRenderer;)V',
			                false
			            ));
			            instructions.insertBefore(instruction, list);
                        break;
			        }
			    }
			    return methodNode;
			}
		},
		'bipedModel': {
		    'target': {
		        'type': 'METHOD',
		        'class': 'net.minecraft.client.renderer.entity.model.BipedModel',
		        'methodName': 'func_212844_a_',
		        'methodDesc': '(Lnet/minecraft/entity/LivingEntity;FFFFFF)V'
		    },
		    'transformer': function(methodNode) {
		        var instructions = methodNode.instructions;
		        for(var i = instructions.size() - 1; i >= 0; i--) {
		            var instruction = instructions.get(i);
		            if(instruction.getOpcode() === RETURN) {
		                var list = new InsnList();
		                list.add(new VarInsnNode(ALOAD, 0));
		                list.add(new VarInsnNode(ALOAD, 1));
		                list.add(new MethodInsnNode(
		                    INVOKESTATIC,
		                    'dev/toma/pubgmc/PubgmcHooks',
		                    'onSetupRotationAngles',
		                    '(Lnet/minecraft/client/renderer/entity/model/BipedModel;Lnet/minecraft/entity/LivingEntity;)V',
		                    false
		                ));
		                instructions.insertBefore(instruction, list);
		                break;
		            }
		        }
		        return methodNode;
		    }
		},
		/*LINENUMBER 99 L31
            ALOAD 0
            ALOAD 1
            FLOAD 9
            INVOKEVIRTUAL net/minecraft/client/renderer/entity/LivingRenderer.prepareScale (Lnet/minecraft/entity/LivingEntity;F)F
            FSTORE 16*/
		'livingRenderer': {
		    'target': {
		        'type': 'METHOD',
		        'class': 'net.minecraft.client.renderer.entity.PlayerRenderer',
		        'methodName': 'func_77043_a',
		        'methodDesc': '(Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;FFF)V'
		    },
		    'transformer': function(methodNode) {
                var instructions = methodNode.instructions;
                for(var i = instructions.size() - 1; i >= 0; i--) {
                    var instruction = instructions.get(i);
                	if(instruction.getOpcode() === RETURN) {
                	    var list = new InsnList();
                		list.add(new VarInsnNode(ALOAD, 1));
                		list.add(new MethodInsnNode(
                		    INVOKESTATIC,
                		    'dev/toma/pubgmc/PubgmcHooks',
                		    'setupRotations',
                		    '(Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;)V',
                		    false
                		));
                		instructions.insertBefore(instruction, list);
                		break;
                	}
                }
		        return methodNode;
		    }
		}
	}
}