/*     */ package com.yworks.yguard.obf;
/*     */ 
/*     */ import com.yworks.yguard.obf.classfile.ClassFile;
/*     */ import java.lang.reflect.Modifier;
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
/*     */ public abstract class MdFd
/*     */   extends TreeItem
/*     */ {
/*  24 */   private String descriptor = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ObfuscationConfig obfuscationConfig;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String[] parsedTypes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MdFd(TreeItem parent, boolean isSynthetic, String name, String descriptor, int access, ObfuscationConfig obfuscationConfig) {
/*  45 */     super(parent, name);
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
/*     */     
/* 149 */     this.parsedTypes = null;
/*     */     this.descriptor = descriptor;
/*     */     this.obfuscationConfig = obfuscationConfig;
/*     */     this.access = access;
/*     */     this.isSynthetic = isSynthetic;
/*     */     if (name.equals("") || descriptor.equals("") || !(parent instanceof Cl))
/*     */       System.err.println("Internal error: method/field must have name and descriptor, and have Class or Interface as parent"); 
/*     */     if (isSynthetic || Modifier.isNative(access))
/*     */       setOutName(getInName());  } protected String[] parseTypes() {
/* 158 */     if (this.parsedTypes == null) {
/*     */       
/*     */       try {
/*     */         
/* 162 */         this.parsedTypes = ClassFile.parseDescriptor(getDescriptor(), true);
/*     */       }
/* 164 */       catch (Exception e) {
/*     */         
/* 166 */         this.parsedTypes = null;
/*     */       } 
/*     */     }
/* 169 */     return this.parsedTypes;
/*     */   }
/*     */   
/*     */   public ObfuscationConfig getObfuscationConfig() {
/*     */     return this.obfuscationConfig;
/*     */   }
/*     */   
/*     */   public String getDescriptor() {
/*     */     return this.descriptor;
/*     */   }
/*     */   
/*     */   public String toString() {
/*     */     StringBuffer sb = new StringBuffer();
/*     */     int modifiers = getModifiers();
/*     */     if (Modifier.isAbstract(modifiers))
/*     */       sb.append("abstract "); 
/*     */     if (Modifier.isSynchronized(modifiers))
/*     */       sb.append("synchronized "); 
/*     */     if (Modifier.isTransient(modifiers))
/*     */       sb.append("transient "); 
/*     */     if (Modifier.isVolatile(modifiers))
/*     */       sb.append("volatile "); 
/*     */     if (Modifier.isNative(modifiers))
/*     */       sb.append("native "); 
/*     */     if (Modifier.isPublic(modifiers))
/*     */       sb.append("public "); 
/*     */     if (Modifier.isProtected(modifiers))
/*     */       sb.append("protected "); 
/*     */     if (Modifier.isPrivate(modifiers))
/*     */       sb.append("private "); 
/*     */     if (Modifier.isStatic(modifiers))
/*     */       sb.append("static "); 
/*     */     if (Modifier.isFinal(modifiers))
/*     */       sb.append("final "); 
/*     */     sb.append(getReturnTypeName());
/*     */     sb.append(getInName());
/*     */     sb.append(getDescriptorName());
/*     */     return sb.toString();
/*     */   }
/*     */   
/*     */   protected String getReturnTypeName() {
/*     */     String[] types = parseTypes();
/*     */     return ((types.length > 0) ? types[types.length - 1] : "") + " ";
/*     */   }
/*     */   
/*     */   protected abstract String getDescriptorName();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/MdFd.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */