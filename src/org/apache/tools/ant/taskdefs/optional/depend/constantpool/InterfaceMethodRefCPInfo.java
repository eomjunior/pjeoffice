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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InterfaceMethodRefCPInfo
/*     */   extends ConstantPoolEntry
/*     */ {
/*     */   private String interfaceMethodClassName;
/*     */   private String interfaceMethodName;
/*     */   private String interfaceMethodType;
/*     */   private int classIndex;
/*     */   private int nameAndTypeIndex;
/*     */   
/*     */   public InterfaceMethodRefCPInfo() {
/*  47 */     super(11, 1);
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
/*  60 */     this.classIndex = cpStream.readUnsignedShort();
/*  61 */     this.nameAndTypeIndex = cpStream.readUnsignedShort();
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
/*     */   public void resolve(ConstantPool constantPool) {
/*  74 */     ClassCPInfo interfaceMethodClass = (ClassCPInfo)constantPool.getEntry(this.classIndex);
/*     */     
/*  76 */     interfaceMethodClass.resolve(constantPool);
/*     */     
/*  78 */     this.interfaceMethodClassName = interfaceMethodClass.getClassName();
/*     */ 
/*     */     
/*  81 */     NameAndTypeCPInfo nt = (NameAndTypeCPInfo)constantPool.getEntry(this.nameAndTypeIndex);
/*     */     
/*  83 */     nt.resolve(constantPool);
/*     */     
/*  85 */     this.interfaceMethodName = nt.getName();
/*  86 */     this.interfaceMethodType = nt.getType();
/*     */     
/*  88 */     super.resolve(constantPool);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  98 */     if (isResolved()) {
/*  99 */       return "InterfaceMethod : Class = " + this.interfaceMethodClassName + ", name = " + this.interfaceMethodName + ", type = " + this.interfaceMethodType;
/*     */     }
/*     */ 
/*     */     
/* 103 */     return "InterfaceMethod : Class index = " + this.classIndex + ", name and type index = " + this.nameAndTypeIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInterfaceMethodClassName() {
/* 114 */     return this.interfaceMethodClassName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInterfaceMethodName() {
/* 123 */     return this.interfaceMethodName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInterfaceMethodType() {
/* 132 */     return this.interfaceMethodType;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/depend/constantpool/InterfaceMethodRefCPInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */