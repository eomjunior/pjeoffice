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
/*     */ public class FieldRefCPInfo
/*     */   extends ConstantPoolEntry
/*     */ {
/*     */   private String fieldClassName;
/*     */   private String fieldName;
/*     */   private String fieldType;
/*     */   private int classIndex;
/*     */   private int nameAndTypeIndex;
/*     */   
/*     */   public FieldRefCPInfo() {
/*  41 */     super(9, 1);
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
/*  54 */     this.classIndex = cpStream.readUnsignedShort();
/*  55 */     this.nameAndTypeIndex = cpStream.readUnsignedShort();
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
/*  68 */     ClassCPInfo fieldClass = (ClassCPInfo)constantPool.getEntry(this.classIndex);
/*     */     
/*  70 */     fieldClass.resolve(constantPool);
/*     */     
/*  72 */     this.fieldClassName = fieldClass.getClassName();
/*     */ 
/*     */     
/*  75 */     NameAndTypeCPInfo nt = (NameAndTypeCPInfo)constantPool.getEntry(this.nameAndTypeIndex);
/*     */     
/*  77 */     nt.resolve(constantPool);
/*     */     
/*  79 */     this.fieldName = nt.getName();
/*  80 */     this.fieldType = nt.getType();
/*     */     
/*  82 */     super.resolve(constantPool);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  92 */     if (isResolved()) {
/*  93 */       return "Field : Class = " + this.fieldClassName + ", name = " + this.fieldName + ", type = " + this.fieldType;
/*     */     }
/*     */     
/*  96 */     return "Field : Class index = " + this.classIndex + ", name and type index = " + this.nameAndTypeIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFieldClassName() {
/* 106 */     return this.fieldClassName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFieldName() {
/* 115 */     return this.fieldName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFieldType() {
/* 124 */     return this.fieldType;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/depend/constantpool/FieldRefCPInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */