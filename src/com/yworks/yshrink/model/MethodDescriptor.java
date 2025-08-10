/*     */ package com.yworks.yshrink.model;
/*     */ 
/*     */ import com.yworks.yshrink.util.Util;
/*     */ import java.io.File;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class MethodDescriptor
/*     */   extends AbstractDescriptor
/*     */ {
/*     */   private String name;
/*     */   private String desc;
/*     */   private List<Invocation> invocations;
/*     */   private List<String[]> fieldRefs;
/*     */   private List<AbstractMap.SimpleEntry<Object, Object>> typeInstructions;
/*     */   private String[] exceptions;
/*     */   private List<String> localVars;
/*     */   
/*     */   protected MethodDescriptor(String name, int access, String desc, String[] exceptions, File sourceJar) {
/*  39 */     super(access, sourceJar);
/*  40 */     this.name = name;
/*  41 */     this.desc = desc;
/*  42 */     this.invocations = new ArrayList<>();
/*  43 */     this.fieldRefs = (List)new ArrayList<>();
/*  44 */     this.typeInstructions = new ArrayList<>();
/*  45 */     this.localVars = new ArrayList<>();
/*  46 */     this.exceptions = exceptions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  55 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDesc() {
/*  64 */     return this.desc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type[] getArgumentTypes() {
/*  73 */     return Type.getArgumentTypes(this.desc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getArgumentsString() {
/*  82 */     StringBuilder buf = new StringBuilder();
/*  83 */     Type[] argumentTypes = getArgumentTypes();
/*  84 */     for (Type type : argumentTypes) {
/*  85 */       buf.append(type.getDescriptor());
/*     */     }
/*  87 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getReturnType() {
/*  96 */     return Type.getReturnType(this.desc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Invocation> getInvocations() {
/* 105 */     return this.invocations;
/*     */   }
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
/*     */   public void addInvocation(int opcode, String type, String name, String desc) {
/* 118 */     this.invocations.add(InvocationFactory.getInstance().getInvocation(opcode, type, name, desc));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String[]> getFieldRefs() {
/* 128 */     return this.fieldRefs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFieldRef(String type, String name) {
/* 138 */     this.fieldRefs.add(new String[] { type, name });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTypeInstruction(int opcode, String desc) {
/* 148 */     this.typeInstructions.add(new AbstractMap.SimpleEntry<>(Integer.valueOf(opcode), desc));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addLocalVar(String desc) {
/* 157 */     this.localVars.add(desc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<AbstractMap.SimpleEntry<Object, Object>> getTypeInstructions() {
/* 166 */     return this.typeInstructions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getExceptions() {
/* 175 */     return this.exceptions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasFlag(int code) {
/* 185 */     return ((this.access & code) == code);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/* 195 */     return ((this.access & 0x8) == 8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPrivate() {
/* 204 */     return ((this.access & 0x2) == 2);
/*     */   }
/*     */   
/*     */   public int getAccess() {
/* 208 */     return this.access;
/*     */   }
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
/*     */   public boolean overrides(MethodDescriptor md) {
/* 222 */     return overrides(md.getName(), md.getReturnType(), md.getArgumentTypes());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean overrides(Method m) {
/* 232 */     return overrides(m.getName(), Type.getReturnType(m), Type.getArgumentTypes(m));
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean overrides(String mName, Type mReturnType, Type[] mArgumentTypes) {
/* 237 */     if (!mName.equals(getName())) return false;
/*     */     
/* 239 */     if (!getReturnType().equals(mReturnType)) return false;
/*     */     
/* 241 */     Type[] argumentTypes = getArgumentTypes();
/*     */     
/* 243 */     Type[] argumentTypesMd = mArgumentTypes;
/*     */     
/* 245 */     if (argumentTypes.length != argumentTypesMd.length) {
/* 246 */       return false;
/*     */     }
/* 248 */     for (int i = 0; i < argumentTypes.length; i++) {
/* 249 */       if (!argumentTypes[i].equals(argumentTypesMd[i])) {
/* 250 */         return false;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 255 */     return true;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 259 */     return "MethodDescriptor{name='" + this.name + '\'' + ", desc='" + this.desc + '\'' + '}';
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConstructor() {
/* 271 */     return getName().equals("<init>");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSignature() {
/* 281 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 283 */     buf.append(Util.toJavaType(getReturnType().getDescriptor())).append(" ").append(
/* 284 */         getName()).append("(");
/* 285 */     Type[] argumentTypes = getArgumentTypes();
/* 286 */     for (int i = 0; i < argumentTypes.length - 1; i++) {
/* 287 */       Type type = argumentTypes[i];
/* 288 */       buf.append(Util.toJavaType(type.getDescriptor())).append(",");
/*     */     } 
/* 290 */     if (argumentTypes.length > 0) {
/* 291 */       buf.append(Util.toJavaType(argumentTypes[argumentTypes.length - 1].getDescriptor()));
/*     */     }
/* 293 */     buf.append(")");
/*     */     
/* 295 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/model/MethodDescriptor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */