/*     */ package org.apache.tools.ant.taskdefs.optional.depend.constantpool;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
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
/*     */ public class MethodRefCPInfo
/*     */   extends ConstantPoolEntry
/*     */ {
/*     */   private String methodClassName;
/*     */   private String methodName;
/*     */   private String methodType;
/*     */   private int classIndex;
/*     */   private int nameAndTypeIndex;
/*     */   
/*     */   public MethodRefCPInfo() {
/*  44 */     super(10, 1);
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
/*     */   public void read(DataInputStream cpStream) throws IOException {
/*  57 */     this.classIndex = cpStream.readUnsignedShort();
/*  58 */     this.nameAndTypeIndex = cpStream.readUnsignedShort();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  68 */     if (isResolved()) {
/*  69 */       return "Method : Class = " + this.methodClassName + ", name = " + this.methodName + ", type = " + this.methodType;
/*     */     }
/*     */     
/*  72 */     return "Method : Class index = " + this.classIndex + ", name and type index = " + this.nameAndTypeIndex;
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
/*     */   public void resolve(ConstantPool constantPool) {
/*  86 */     ClassCPInfo methodClass = (ClassCPInfo)constantPool.getEntry(this.classIndex);
/*     */     
/*  88 */     methodClass.resolve(constantPool);
/*     */     
/*  90 */     this.methodClassName = methodClass.getClassName();
/*     */ 
/*     */     
/*  93 */     NameAndTypeCPInfo nt = (NameAndTypeCPInfo)constantPool.getEntry(this.nameAndTypeIndex);
/*     */     
/*  95 */     nt.resolve(constantPool);
/*     */     
/*  97 */     this.methodName = nt.getName();
/*  98 */     this.methodType = nt.getType();
/*     */     
/* 100 */     super.resolve(constantPool);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMethodClassName() {
/* 109 */     return this.methodClassName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMethodName() {
/* 118 */     return this.methodName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMethodType() {
/* 127 */     return this.methodType;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/depend/constantpool/MethodRefCPInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */