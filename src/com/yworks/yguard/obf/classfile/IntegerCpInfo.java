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
/*    */ public class IntegerCpInfo
/*    */   extends CpInfo
/*    */ {
/*    */   private int u4bytes;
/*    */   
/*    */   protected IntegerCpInfo() {
/* 36 */     super(3);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean asBool() {
/* 46 */     return (this.u4bytes != 0);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void readInfo(DataInput din) throws IOException {
/* 52 */     this.u4bytes = din.readInt();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void writeInfo(DataOutput dout) throws IOException {
/* 58 */     dout.writeInt(this.u4bytes);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/IntegerCpInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */