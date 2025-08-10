/*     */ package com.yworks.yshrink.core;
/*     */ 
/*     */ import com.yworks.logging.Logger;
/*     */ import com.yworks.logging.XmlLogger;
/*     */ import com.yworks.yshrink.model.ClassDescriptor;
/*     */ import com.yworks.yshrink.model.FieldDescriptor;
/*     */ import com.yworks.yshrink.model.MethodDescriptor;
/*     */ import com.yworks.yshrink.model.Model;
/*     */ import com.yworks.yshrink.util.Util;
/*     */ import org.objectweb.asm.AnnotationVisitor;
/*     */ import org.objectweb.asm.Attribute;
/*     */ import org.objectweb.asm.ClassVisitor;
/*     */ import org.objectweb.asm.FieldVisitor;
/*     */ import org.objectweb.asm.Handle;
/*     */ import org.objectweb.asm.Label;
/*     */ import org.objectweb.asm.MethodVisitor;
/*     */ import org.objectweb.asm.TypePath;
/*     */ 
/*     */ public class OutputVisitor extends ClassVisitor {
/*     */   private ClassVisitor cv;
/*  21 */   private int numObsoleteMethods = 0; private Model model; private final boolean createStubs;
/*  22 */   private int numObsoleteFields = 0;
/*     */   
/*     */   private ClassDescriptor currentClass;
/*     */   
/*  26 */   private final DoNothingAnnotationVisitor ignoreAnnotation = new DoNothingAnnotationVisitor();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OutputVisitor(ClassVisitor cv, Model model, boolean createStubs) {
/*  36 */     super(458752);
/*  37 */     this.createStubs = createStubs;
/*  38 */     this.cv = cv;
/*  39 */     this.model = model;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
/*  45 */     this.currentClass = this.model.getClassDescriptor(name);
/*  46 */     if (this.model.isObsolete(this.currentClass.getNode())) {
/*  47 */       throw new IllegalArgumentException("Writing obsolete class: " + name);
/*     */     }
/*     */     
/*  50 */     this.cv.visit(version, access, name, signature, superName, interfaces);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitSource(String source, String debug) {
/*  58 */     if (!this.currentClass.getRetainAttribute("SourceFile")) {
/*  59 */       source = null;
/*     */     }
/*     */     
/*  62 */     if (!this.currentClass.getRetainAttribute("SourceDebug")) {
/*  63 */       debug = null;
/*     */     }
/*     */     
/*  66 */     this.cv.visitSource(source, debug);
/*     */   }
/*     */   
/*     */   public void visitOuterClass(String owner, String name, String desc) {
/*  70 */     this.cv.visitOuterClass(owner, name, desc);
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
/*  75 */     if (visible) {
/*  76 */       if (!this.currentClass.getRetainAttribute("RuntimeVisibleAnnotations")) {
/*  77 */         return this.ignoreAnnotation;
/*     */       }
/*     */     }
/*  80 */     else if (!this.currentClass.getRetainAttribute("RuntimeInvisibleAnnotations")) {
/*  81 */       return this.ignoreAnnotation;
/*     */     } 
/*     */ 
/*     */     
/*  85 */     return new OutputAnnotationVisitor(this.cv.visitAnnotation(desc, visible));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
/*  92 */     if (visible) {
/*  93 */       if (!this.currentClass.getRetainAttribute("RuntimeVisibleTypeAnnotations")) {
/*  94 */         return this.ignoreAnnotation;
/*     */       }
/*     */     }
/*  97 */     else if (!this.currentClass.getRetainAttribute("RuntimeInvisibleAnnotations")) {
/*  98 */       return this.ignoreAnnotation;
/*     */     } 
/*     */     
/* 101 */     return new OutputAnnotationVisitor(this.cv.visitTypeAnnotation(typeRef, typePath, descriptor, visible));
/*     */   }
/*     */   
/*     */   public void visitAttribute(Attribute attr) {
/* 105 */     if (this.currentClass.getRetainAttribute(attr.type)) {
/* 106 */       this.cv.visitAttribute(attr);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitInnerClass(String name, String outerName, String innerName, int access) {
/* 112 */     if (this.model.isClassModeled(name)) {
/* 113 */       ClassDescriptor cd = this.model.getClassDescriptor(name);
/* 114 */       if (!this.model.isObsolete(cd.getNode())) {
/* 115 */         this.cv.visitInnerClass(name, outerName, innerName, access);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void visitNestMember(String nestMember) {
/* 121 */     ClassDescriptor cd = this.model.getClassDescriptor(nestMember);
/* 122 */     if (cd.getHasNestMembers() && !this.model.isObsolete(cd.getNode())) {
/* 123 */       this.cv.visitNestMember(nestMember);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
/* 130 */     FieldDescriptor fd = this.currentClass.getField(name);
/* 131 */     if (this.model.isObsolete(fd.getNode())) {
/* 132 */       Logger.shrinkLog("\t\t<field name=\"" + name + "\" class=\"" + 
/* 133 */           Util.toJavaClass(this.currentClass.getName()) + "\" />");
/* 134 */       this.numObsoleteFields++;
/* 135 */       return null;
/*     */     } 
/* 137 */     return new OutputFieldVisitor(this.cv.visitField(access, name, desc, signature, value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
/* 144 */     MethodDescriptor md = this.currentClass.getMethod(name, desc);
/* 145 */     if (this.model.isObsolete(md.getNode())) {
/*     */       
/* 147 */       if (!this.model.isStubNeeded(md.getNode())) {
/* 148 */         this.numObsoleteMethods++;
/* 149 */         Logger.shrinkLog("\t\t<method signature=\"" + 
/* 150 */             XmlLogger.replaceSpecialChars(md.getSignature()) + "\" class=\"" + 
/* 151 */             Util.toJavaClass(this.currentClass.getName()) + "\" />");
/*     */       } 
/*     */       
/* 154 */       if (this.createStubs || this.model.isStubNeeded(md.getNode())) {
/* 155 */         boolean visitStub = !md.hasFlag(1024);
/* 156 */         return new StubOutputMethodVisitor(this.cv.visitMethod(access, name, desc, signature, exceptions), visitStub);
/*     */       } 
/* 158 */       return null;
/*     */     } 
/*     */     
/* 161 */     return new OutputMethodVisitor(this.cv.visitMethod(access, name, desc, signature, exceptions));
/*     */   }
/*     */ 
/*     */   
/*     */   private void visitStub(MethodVisitor mv) {
/* 166 */     mv.visitCode();
/* 167 */     mv.visitTypeInsn(187, "java/lang/InternalError");
/* 168 */     mv.visitInsn(89);
/* 169 */     mv.visitLdcInsn("Badly shrinked");
/* 170 */     mv.visitMethodInsn(183, "java/lang/InternalError", "<init>", "(Ljava/lang/String;)V", this.currentClass.isInterface());
/* 171 */     mv.visitInsn(191);
/* 172 */     mv.visitMaxs(3, 1);
/*     */   }
/*     */   
/*     */   public void visitEnd() {
/* 176 */     this.cv.visitEnd();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumObsoleteMethods() {
/* 185 */     return this.numObsoleteMethods;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumObsoleteFields() {
/* 194 */     return this.numObsoleteFields;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class OutputMethodVisitor
/*     */     extends MethodVisitor
/*     */   {
/*     */     private MethodVisitor delegate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public OutputMethodVisitor(MethodVisitor delegate) {
/* 211 */       super(458752);
/* 212 */       this.delegate = delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public AnnotationVisitor visitAnnotationDefault() {
/* 217 */       return this.delegate.visitAnnotationDefault();
/*     */     }
/*     */     
/*     */     public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
/* 221 */       if (visible) {
/* 222 */         if (!OutputVisitor.this.currentClass.getRetainAttribute("RuntimeVisibleAnnotations")) {
/* 223 */           return OutputVisitor.this.ignoreAnnotation;
/*     */         }
/*     */       }
/* 226 */       else if (!OutputVisitor.this.currentClass.getRetainAttribute("RuntimeInvisibleAnnotations")) {
/* 227 */         return OutputVisitor.this.ignoreAnnotation;
/*     */       } 
/*     */ 
/*     */       
/* 231 */       return new OutputVisitor.OutputAnnotationVisitor(this.delegate.visitAnnotation(desc, visible));
/*     */     }
/*     */     
/*     */     public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
/* 235 */       if (visible) {
/* 236 */         if (!OutputVisitor.this.currentClass.getRetainAttribute("RuntimeVisibleParameterAnnotations")) {
/* 237 */           return OutputVisitor.this.ignoreAnnotation;
/*     */         }
/*     */       }
/* 240 */       else if (!OutputVisitor.this.currentClass.getRetainAttribute("RuntimeInvisibleParameterAnnotations")) {
/* 241 */         return OutputVisitor.this.ignoreAnnotation;
/*     */       } 
/*     */       
/* 244 */       return new OutputVisitor.OutputAnnotationVisitor(this.delegate.visitParameterAnnotation(parameter, desc, visible));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
/* 250 */       if (visible) {
/* 251 */         if (!OutputVisitor.this.currentClass.getRetainAttribute("RuntimeVisibleTypeAnnotations")) {
/* 252 */           return OutputVisitor.this.ignoreAnnotation;
/*     */         }
/*     */       }
/* 255 */       else if (!OutputVisitor.this.currentClass.getRetainAttribute("RuntimeInvisibleTypeAnnotations")) {
/* 256 */         return OutputVisitor.this.ignoreAnnotation;
/*     */       } 
/*     */       
/* 259 */       return new OutputVisitor.OutputAnnotationVisitor(this.delegate.visitInsnAnnotation(typeRef, typePath, descriptor, visible));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
/* 265 */       if (visible) {
/* 266 */         if (!OutputVisitor.this.currentClass.getRetainAttribute("RuntimeVisibleTypeAnnotations")) {
/* 267 */           return OutputVisitor.this.ignoreAnnotation;
/*     */         }
/*     */       }
/* 270 */       else if (!OutputVisitor.this.currentClass.getRetainAttribute("RuntimeInvisibleTypeAnnotations")) {
/* 271 */         return OutputVisitor.this.ignoreAnnotation;
/*     */       } 
/*     */       
/* 274 */       return new OutputVisitor.OutputAnnotationVisitor(this.delegate.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, descriptor, visible));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
/* 280 */       if (visible) {
/* 281 */         if (!OutputVisitor.this.currentClass.getRetainAttribute("RuntimeVisibleTypeAnnotations")) {
/* 282 */           return OutputVisitor.this.ignoreAnnotation;
/*     */         }
/*     */       }
/* 285 */       else if (!OutputVisitor.this.currentClass.getRetainAttribute("RuntimeInvisibleTypeAnnotations")) {
/* 286 */         return OutputVisitor.this.ignoreAnnotation;
/*     */       } 
/*     */       
/* 289 */       return new OutputVisitor.OutputAnnotationVisitor(this.delegate.visitTypeAnnotation(typeRef, typePath, descriptor, visible));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
/* 296 */       if (visible) {
/* 297 */         if (!OutputVisitor.this.currentClass.getRetainAttribute("RuntimeVisibleTypeAnnotations")) {
/* 298 */           return OutputVisitor.this.ignoreAnnotation;
/*     */         }
/*     */       }
/* 301 */       else if (!OutputVisitor.this.currentClass.getRetainAttribute("RuntimeInvisibleTypeAnnotations")) {
/* 302 */         return OutputVisitor.this.ignoreAnnotation;
/*     */       } 
/*     */       
/* 305 */       return new OutputVisitor.OutputAnnotationVisitor(this.delegate.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible));
/*     */     }
/*     */     
/*     */     public void visitAttribute(Attribute attr) {
/* 309 */       if (OutputVisitor.this.currentClass.getRetainAttribute(attr.type)) {
/* 310 */         this.delegate.visitAttribute(attr);
/*     */       }
/*     */     }
/*     */     
/*     */     public void visitCode() {
/* 315 */       this.delegate.visitCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitFrame(int i, int i1, Object[] objects, int i2, Object[] objects1) {
/* 320 */       this.delegate.visitFrame(i, i1, objects, i2, objects1);
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitInsn(int opcode) {
/* 325 */       this.delegate.visitInsn(opcode);
/*     */     }
/*     */     
/*     */     public void visitIntInsn(int opcode, int operand) {
/* 329 */       this.delegate.visitIntInsn(opcode, operand);
/*     */     }
/*     */     
/*     */     public void visitVarInsn(int opcode, int var) {
/* 333 */       this.delegate.visitVarInsn(opcode, var);
/*     */     }
/*     */     
/*     */     public void visitTypeInsn(int opcode, String desc) {
/* 337 */       this.delegate.visitTypeInsn(opcode, desc);
/*     */     }
/*     */     
/*     */     public void visitFieldInsn(int opcode, String owner, String name, String desc) {
/* 341 */       this.delegate.visitFieldInsn(opcode, owner, name, desc);
/*     */     }
/*     */     
/*     */     public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
/* 345 */       this.delegate.visitMethodInsn(opcode, owner, name, desc, itf);
/*     */     }
/*     */     
/*     */     public void visitJumpInsn(int opcode, Label label) {
/* 349 */       this.delegate.visitJumpInsn(opcode, label);
/*     */     }
/*     */     
/*     */     public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
/* 353 */       this.delegate.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
/*     */     }
/*     */     
/*     */     public void visitLabel(Label label) {
/* 357 */       this.delegate.visitLabel(label);
/*     */     }
/*     */     
/*     */     public void visitLdcInsn(Object cst) {
/* 361 */       this.delegate.visitLdcInsn(cst);
/*     */     }
/*     */     
/*     */     public void visitIincInsn(int var, int increment) {
/* 365 */       this.delegate.visitIincInsn(var, increment);
/*     */     }
/*     */     
/*     */     public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
/* 369 */       this.delegate.visitTableSwitchInsn(min, max, dflt, labels);
/*     */     }
/*     */     
/*     */     public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
/* 373 */       this.delegate.visitLookupSwitchInsn(dflt, keys, labels);
/*     */     }
/*     */     
/*     */     public void visitMultiANewArrayInsn(String desc, int dims) {
/* 377 */       this.delegate.visitMultiANewArrayInsn(desc, dims);
/*     */     }
/*     */     
/*     */     public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
/* 381 */       this.delegate.visitTryCatchBlock(start, end, handler, type);
/*     */     }
/*     */     
/*     */     public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
/* 385 */       if (null != signature) {
/* 386 */         if (!OutputVisitor.this.currentClass.getRetainAttribute("LocalVariableTypeTable")) {
/*     */           return;
/*     */         }
/*     */       }
/* 390 */       else if (!OutputVisitor.this.currentClass.getRetainAttribute("LocalVariableTable")) {
/*     */         return;
/*     */       } 
/*     */       
/* 394 */       this.delegate.visitLocalVariable(name, desc, signature, start, end, index);
/*     */     }
/*     */     
/*     */     public void visitLineNumber(int line, Label start) {
/* 398 */       if (OutputVisitor.this.currentClass.getRetainAttribute("LineNumberTable")) {
/* 399 */         this.delegate.visitLineNumber(line, start);
/*     */       }
/*     */     }
/*     */     
/*     */     public void visitMaxs(int maxStack, int maxLocals) {
/* 404 */       this.delegate.visitMaxs(maxStack, maxLocals);
/*     */     }
/*     */     
/*     */     public void visitEnd() {
/* 408 */       this.delegate.visitEnd();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class OutputFieldVisitor
/*     */     extends FieldVisitor
/*     */   {
/*     */     private final FieldVisitor delegate;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public OutputFieldVisitor(FieldVisitor delegate) {
/* 425 */       super(458752);
/* 426 */       this.delegate = delegate;
/*     */     }
/*     */     
/*     */     public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
/* 430 */       if (visible) {
/* 431 */         if (!OutputVisitor.this.currentClass.getRetainAttribute("RuntimeVisibleAnnotations")) {
/* 432 */           return OutputVisitor.this.ignoreAnnotation;
/*     */         }
/*     */       }
/* 435 */       else if (!OutputVisitor.this.currentClass.getRetainAttribute("RuntimeInvisibleAnnotations")) {
/* 436 */         return OutputVisitor.this.ignoreAnnotation;
/*     */       } 
/*     */       
/* 439 */       return new OutputVisitor.OutputAnnotationVisitor(this.delegate.visitAnnotation(desc, visible));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
/* 445 */       if (visible) {
/* 446 */         if (!OutputVisitor.this.currentClass.getRetainAttribute("RuntimeVisibleTypeAnnotations")) {
/* 447 */           return OutputVisitor.this.ignoreAnnotation;
/*     */         }
/*     */       }
/* 450 */       else if (!OutputVisitor.this.currentClass.getRetainAttribute("RuntimeInvisibleTypeAnnotations")) {
/* 451 */         return OutputVisitor.this.ignoreAnnotation;
/*     */       } 
/*     */       
/* 454 */       return new OutputVisitor.OutputAnnotationVisitor(this.delegate.visitTypeAnnotation(typeRef, typePath, descriptor, visible));
/*     */     }
/*     */     
/*     */     public void visitAttribute(Attribute attribute) {
/* 458 */       if (OutputVisitor.this.currentClass.getRetainAttribute(attribute.type)) {
/* 459 */         this.delegate.visitAttribute(attribute);
/*     */       }
/*     */     }
/*     */     
/*     */     public void visitEnd() {
/* 464 */       this.delegate.visitEnd();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class StubOutputMethodVisitor
/*     */     extends MethodVisitor
/*     */   {
/*     */     private MethodVisitor delegate;
/*     */ 
/*     */ 
/*     */     
/*     */     private final boolean visitStub;
/*     */ 
/*     */ 
/*     */     
/*     */     public StubOutputMethodVisitor(MethodVisitor delegate, boolean visitStub) {
/* 483 */       super(458752);
/* 484 */       this.delegate = delegate;
/* 485 */       this.visitStub = visitStub;
/*     */     }
/*     */ 
/*     */     
/*     */     public AnnotationVisitor visitAnnotationDefault() {
/* 490 */       return this.delegate.visitAnnotationDefault();
/*     */     }
/*     */ 
/*     */     
/*     */     public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
/* 495 */       if (visible) {
/* 496 */         if (!OutputVisitor.this.currentClass.getRetainAttribute("RuntimeVisibleAnnotations")) {
/* 497 */           return OutputVisitor.this.ignoreAnnotation;
/*     */         }
/*     */       }
/* 500 */       else if (!OutputVisitor.this.currentClass.getRetainAttribute("RuntimeInvisibleAnnotations")) {
/* 501 */         return OutputVisitor.this.ignoreAnnotation;
/*     */       } 
/*     */ 
/*     */       
/* 505 */       return new OutputVisitor.OutputAnnotationVisitor(this.delegate.visitAnnotation(desc, visible));
/*     */     }
/*     */     
/*     */     public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
/* 509 */       if (visible) {
/* 510 */         if (!OutputVisitor.this.currentClass.getRetainAttribute("RuntimeVisibleParameterAnnotations")) {
/* 511 */           return OutputVisitor.this.ignoreAnnotation;
/*     */         }
/*     */       }
/* 514 */       else if (!OutputVisitor.this.currentClass.getRetainAttribute("RuntimeInvisibleParameterAnnotations")) {
/* 515 */         return OutputVisitor.this.ignoreAnnotation;
/*     */       } 
/*     */       
/* 518 */       return new OutputVisitor.OutputAnnotationVisitor(this.delegate.visitParameterAnnotation(parameter, desc, visible));
/*     */     }
/*     */     
/*     */     public void visitAttribute(Attribute attr) {
/* 522 */       if (OutputVisitor.this.currentClass.getRetainAttribute(attr.type)) {
/* 523 */         this.delegate.visitAttribute(attr);
/*     */       }
/*     */     }
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
/*     */     public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {}
/*     */ 
/*     */     
/*     */     public void visitVarInsn(int opcode, int var) {}
/*     */ 
/*     */     
/*     */     public void visitTypeInsn(int opcode, String desc) {}
/*     */ 
/*     */     
/*     */     public void visitFieldInsn(int opcode, String owner, String name, String desc) {}
/*     */ 
/*     */     
/*     */     public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {}
/*     */ 
/*     */     
/*     */     public void visitJumpInsn(int opcode, Label label) {}
/*     */ 
/*     */     
/*     */     public void visitLabel(Label label) {}
/*     */ 
/*     */     
/*     */     public void visitLdcInsn(Object cst) {}
/*     */ 
/*     */     
/*     */     public void visitIincInsn(int var, int increment) {}
/*     */ 
/*     */     
/*     */     public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {}
/*     */ 
/*     */     
/*     */     public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {}
/*     */ 
/*     */     
/*     */     public void visitMultiANewArrayInsn(String desc, int dims) {}
/*     */ 
/*     */     
/*     */     public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {}
/*     */ 
/*     */     
/*     */     public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {}
/*     */ 
/*     */     
/*     */     public void visitLineNumber(int line, Label start) {}
/*     */ 
/*     */     
/*     */     public void visitMaxs(int maxStack, int maxLocals) {}
/*     */     
/*     */     public void visitEnd() {
/* 588 */       if (this.visitStub) {
/* 589 */         OutputVisitor.this.visitStub(this.delegate);
/*     */       }
/* 591 */       this.delegate.visitEnd();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class DoNothingAnnotationVisitor
/*     */     extends AnnotationVisitor
/*     */   {
/*     */     public DoNothingAnnotationVisitor() {
/* 604 */       super(458752);
/*     */     }
/*     */ 
/*     */     
/*     */     public void visit(String name, Object value) {}
/*     */ 
/*     */     
/*     */     public void visitEnum(String name, String desc, String value) {}
/*     */     
/*     */     public AnnotationVisitor visitAnnotation(String name, String desc) {
/* 614 */       return this;
/*     */     }
/*     */     
/*     */     public AnnotationVisitor visitArray(String name) {
/* 618 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void visitEnd() {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class OutputAnnotationVisitor
/*     */     extends AnnotationVisitor
/*     */   {
/*     */     AnnotationVisitor delegate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public OutputAnnotationVisitor(AnnotationVisitor delegate) {
/* 643 */       super(458752);
/* 644 */       this.delegate = delegate;
/*     */     }
/*     */     
/*     */     public void visit(String name, Object value) {
/* 648 */       this.delegate.visit(name, value);
/*     */     }
/*     */     
/*     */     public void visitEnum(String name, String desc, String value) {
/* 652 */       this.delegate.visitEnum(name, desc, value);
/*     */     }
/*     */     
/*     */     public AnnotationVisitor visitAnnotation(String name, String desc) {
/* 656 */       return new OutputAnnotationVisitor(this.delegate.visitAnnotation(name, desc));
/*     */     }
/*     */     
/*     */     public AnnotationVisitor visitArray(String name) {
/* 660 */       return new OutputAnnotationVisitor(this.delegate.visitArray(name));
/*     */     }
/*     */     
/*     */     public void visitEnd() {
/* 664 */       this.delegate.visitEnd();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/core/OutputVisitor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */