/*    */ package org.apache.tools.ant.taskdefs.optional.depend.constantpool;
/*    */ 
/*    */ import java.io.DataInputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InvokeDynamicCPInfo
/*    */   extends ConstantCPInfo
/*    */ {
/*    */   private int bootstrapMethodAttrIndex;
/*    */   private int nameAndTypeIndex;
/*    */   private NameAndTypeCPInfo nameAndTypeCPInfo;
/*    */   
/*    */   public InvokeDynamicCPInfo() {
/* 38 */     super(18, 1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void read(DataInputStream cpStream) throws IOException {
/* 51 */     this.bootstrapMethodAttrIndex = cpStream.readUnsignedShort();
/* 52 */     this.nameAndTypeIndex = cpStream.readUnsignedShort();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 62 */     if (isResolved()) {
/* 63 */       return "Name = " + this.nameAndTypeCPInfo.getName() + ", type = " + this.nameAndTypeCPInfo
/* 64 */         .getType();
/*    */     }
/* 66 */     return "BootstrapMethodAttrIndex inx = " + this.bootstrapMethodAttrIndex + "NameAndType index = " + this.nameAndTypeIndex;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void resolve(ConstantPool constantPool) {
/* 78 */     this
/* 79 */       .nameAndTypeCPInfo = (NameAndTypeCPInfo)constantPool.getEntry(this.nameAndTypeIndex);
/* 80 */     this.nameAndTypeCPInfo.resolve(constantPool);
/* 81 */     super.resolve(constantPool);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/depend/constantpool/InvokeDynamicCPInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */