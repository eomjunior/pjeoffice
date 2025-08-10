/*    */ package com.yworks.yguard.obf.classfile;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.DataOutput;
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
/*    */ 
/*    */ public class ExceptionsAttrInfo
/*    */   extends AttrInfo
/*    */ {
/*    */   private int u2numberOfExceptions;
/*    */   private int[] u2exceptionIndexTable;
/*    */   
/*    */   protected ExceptionsAttrInfo(ClassFile cf, int attrNameIndex, int attrLength) {
/* 39 */     super(cf, attrNameIndex, attrLength);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected String getAttrName() {
/* 46 */     return "Exceptions";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void readInfo(DataInput din) throws IOException {
/* 52 */     this.u2numberOfExceptions = din.readUnsignedShort();
/* 53 */     this.u2exceptionIndexTable = new int[this.u2numberOfExceptions];
/* 54 */     for (int i = 0; i < this.u2numberOfExceptions; i++)
/*    */     {
/* 56 */       this.u2exceptionIndexTable[i] = din.readUnsignedShort();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void writeInfo(DataOutput dout) throws IOException {
/* 63 */     dout.writeShort(this.u2numberOfExceptions);
/* 64 */     for (int i = 0; i < this.u2numberOfExceptions; i++)
/*    */     {
/* 66 */       dout.writeShort(this.u2exceptionIndexTable[i]);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/ExceptionsAttrInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */