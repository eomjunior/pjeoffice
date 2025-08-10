/*     */ package com.yworks.yshrink.model;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.objectweb.asm.AnnotationVisitor;
/*     */ import org.objectweb.asm.Attribute;
/*     */ import org.objectweb.asm.ClassVisitor;
/*     */ import org.objectweb.asm.FieldVisitor;
/*     */ import org.objectweb.asm.Handle;
/*     */ import org.objectweb.asm.Label;
/*     */ import org.objectweb.asm.MethodVisitor;
/*     */ import org.objectweb.asm.Type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModelVisitor
/*     */   extends ClassVisitor
/*     */ {
/*     */   static final int OPCODES_ASM = 458752;
/*     */   private Model model;
/*     */   private ClassDescriptor currentClass;
/*     */   private final File sourceJar;
/*     */   
/*     */   public ModelVisitor(Model model, File sourceJar) {
/*  38 */     super(458752);
/*  39 */     this.model = model;
/*  40 */     this.sourceJar = sourceJar;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
/*  50 */     if (!this.model.isClassModeled(name)) {
/*  51 */       this.currentClass = this.model.newClassDescriptor(name, superName, interfaces, access, this.sourceJar);
/*     */     } else {
/*  53 */       this.currentClass = this.model.getClassDescriptor(name);
/*  54 */       this.currentClass.setInterfaces(interfaces);
/*  55 */       this.currentClass.setSuperName(superName);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitInnerClass(String name, String outerName, String innerName, int access) {}
/*     */ 
/*     */   
/*     */   public void visitNestMember(String nestMember) {
/*  64 */     this.currentClass.setHasNestMembers(true);
/*     */   }
/*     */   
/*     */   public void visitOuterClass(String owner, String name, String desc) {
/*  68 */     if (name != null) {
/*  69 */       this.currentClass.setEnclosingMethod(name, desc);
/*  70 */       this.currentClass.setEnclosingClass(owner);
/*     */     } else {
/*  72 */       this.currentClass.setEnclosingClass(owner);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
/*  78 */     this.model.newFieldDescriptor(this.currentClass, desc, name, access, this.sourceJar);
/*  79 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
/*  84 */     MethodDescriptor currentMethod = this.model.newMethodDescriptor(this.currentClass, access, name, desc, exceptions, this.sourceJar);
/*  85 */     return new ModelMethodVisitor(currentMethod);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitSource(String source, String debug) {}
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
/*  92 */     return new ModelAnnotationVisitor(this.currentClass, this.currentClass.addAnnotation(desc));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitAttribute(Attribute attr) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitEnd() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class ModelMethodVisitor
/*     */     extends MethodVisitor
/*     */   {
/*     */     private MethodDescriptor currentMethod;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ModelMethodVisitor(MethodDescriptor currentMethod) {
/* 123 */       super(458752);
/* 124 */       this.currentMethod = currentMethod;
/*     */     }
/*     */     
/*     */     public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
/* 128 */       this.currentMethod.addInvocation(opcode, owner, name, desc);
/*     */     }
/*     */     
/*     */     public void visitTypeInsn(int opcode, String desc) {
/* 132 */       this.currentMethod.addTypeInstruction(opcode, desc);
/*     */     }
/*     */     
/*     */     public void visitMultiANewArrayInsn(String desc, int dims) {
/* 136 */       this.currentMethod.addTypeInstruction(197, desc);
/*     */     }
/*     */     
/*     */     public void visitFieldInsn(int opcode, String owner, String name, String desc) {
/* 140 */       this.currentMethod.addFieldRef(owner, name);
/*     */     }
/*     */     
/*     */     public AnnotationVisitor visitAnnotationDefault() {
/* 144 */       return new ModelVisitor.ModelAnnotationVisitor(this.currentMethod, new AnnotationUsage("java.lang.AnnotationDefaultAttribute"));
/*     */     }
/*     */     
/*     */     public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
/* 148 */       return new ModelVisitor.ModelAnnotationVisitor(this.currentMethod, this.currentMethod.addAnnotation(desc));
/*     */     }
/*     */     
/*     */     public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
/* 152 */       return new ModelVisitor.ModelAnnotationVisitor(this.currentMethod, this.currentMethod.addAnnotation(desc));
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitAttribute(Attribute attr) {}
/*     */ 
/*     */     
/*     */     public void visitCode() {}
/*     */ 
/*     */     
/*     */     public void visitFrame(int i, int i1, Object[] objects, int i2, Object[] objects1) {}
/*     */ 
/*     */     
/*     */     public void visitInsn(int opcode) {}
/*     */ 
/*     */     
/*     */     public void visitIntInsn(int opcode, int operand) {}
/*     */ 
/*     */     
/*     */     public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
/* 172 */       if (bootstrapMethodHandle.getOwner().equals("java/lang/invoke/LambdaMetafactory")) {
/* 173 */         String className = descriptor.substring(descriptor.indexOf(")L") + 2, descriptor.length() - 1);
/*     */ 
/*     */ 
/*     */         
/* 177 */         Type methodDescriptor = (Type)bootstrapMethodArguments[0];
/* 178 */         Handle callerHandle = (Handle)bootstrapMethodArguments[1];
/* 179 */         this.currentMethod.addInvocation(186, className, name, methodDescriptor.getDescriptor());
/* 180 */         this.currentMethod.addInvocation(186, callerHandle.getOwner(), callerHandle.getName(), callerHandle.getDesc());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitVarInsn(int opcode, int var) {}
/*     */ 
/*     */     
/*     */     public void visitJumpInsn(int opcode, Label label) {}
/*     */ 
/*     */     
/*     */     public void visitLabel(Label label) {}
/*     */     
/*     */     public void visitLdcInsn(Object cst) {
/* 194 */       if (cst instanceof Type) {
/* 195 */         Type type = (Type)cst;
/* 196 */         this.currentMethod.addTypeInstruction(18, type.getDescriptor());
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void visitIincInsn(int var, int increment) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void visitLineNumber(int line, Label start) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void visitMaxs(int maxStack, int maxLocals) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void visitEnd() {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   class ModelAnnotationVisitor
/*     */     extends AnnotationVisitor
/*     */   {
/*     */     private final AbstractDescriptor currentItem;
/*     */ 
/*     */     
/*     */     private final AnnotationUsage annotationUsage;
/*     */ 
/*     */     
/*     */     public ModelAnnotationVisitor(AbstractDescriptor currentItem, AnnotationUsage annotationUsage) {
/* 245 */       super(458752);
/* 246 */       this.currentItem = currentItem;
/* 247 */       this.annotationUsage = annotationUsage;
/*     */     }
/*     */ 
/*     */     
/*     */     public void visit(String name, Object value) {
/* 252 */       this.annotationUsage.addFieldUsage(name);
/*     */     }
/*     */     
/*     */     public void visitEnum(String name, String desc, String value) {
/* 256 */       this.annotationUsage.addFieldUsage(name);
/*     */     }
/*     */     
/*     */     public AnnotationVisitor visitAnnotation(String name, String desc) {
/* 260 */       this.annotationUsage.addFieldUsage(name);
/* 261 */       return new ModelAnnotationVisitor(this.currentItem, this.currentItem.addAnnotation(desc));
/*     */     }
/*     */     
/*     */     public AnnotationVisitor visitArray(String name) {
/* 265 */       this.annotationUsage.addFieldUsage(name);
/* 266 */       return new ModelAnnotationVisitor(this.currentItem, new AnnotationUsage("array"));
/*     */     }
/*     */     
/*     */     public void visitEnd() {}
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/model/ModelVisitor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */