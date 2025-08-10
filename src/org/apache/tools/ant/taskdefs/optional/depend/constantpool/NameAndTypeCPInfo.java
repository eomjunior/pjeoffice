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
/*     */ public class NameAndTypeCPInfo
/*     */   extends ConstantPoolEntry
/*     */ {
/*     */   private String name;
/*     */   private String type;
/*     */   private int nameIndex;
/*     */   private int descriptorIndex;
/*     */   
/*     */   public NameAndTypeCPInfo() {
/*  45 */     super(12, 1);
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
/*  58 */     this.nameIndex = cpStream.readUnsignedShort();
/*  59 */     this.descriptorIndex = cpStream.readUnsignedShort();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  69 */     if (isResolved()) {
/*  70 */       return "Name = " + this.name + ", type = " + this.type;
/*     */     }
/*  72 */     return "Name index = " + this.nameIndex + ", descriptor index = " + this.descriptorIndex;
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
/*  85 */     this.name = ((Utf8CPInfo)constantPool.getEntry(this.nameIndex)).getValue();
/*  86 */     this.type = ((Utf8CPInfo)constantPool.getEntry(this.descriptorIndex)).getValue();
/*     */     
/*  88 */     super.resolve(constantPool);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  97 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/* 106 */     return this.type;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/depend/constantpool/NameAndTypeCPInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */