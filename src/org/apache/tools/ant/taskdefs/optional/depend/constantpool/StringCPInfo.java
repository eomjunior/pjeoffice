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
/*    */ public class StringCPInfo
/*    */   extends ConstantCPInfo
/*    */ {
/*    */   private int index;
/*    */   
/*    */   public StringCPInfo() {
/* 34 */     super(8, 1);
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
/* 47 */     this.index = cpStream.readUnsignedShort();
/* 48 */     setValue("unresolved");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 58 */     return "String Constant Pool Entry for " + 
/* 59 */       getValue() + "[" + this.index + "]";
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
/* 71 */     setValue(((Utf8CPInfo)constantPool.getEntry(this.index)).getValue());
/* 72 */     super.resolve(constantPool);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/depend/constantpool/StringCPInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */