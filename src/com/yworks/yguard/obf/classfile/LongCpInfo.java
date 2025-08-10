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
/*    */ public class LongCpInfo
/*    */   extends CpInfo
/*    */ {
/*    */   private int u4highBytes;
/*    */   private int u4lowBytes;
/*    */   
/*    */   protected LongCpInfo() {
/* 35 */     super(5);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void readInfo(DataInput din) throws IOException {
/* 42 */     this.u4highBytes = din.readInt();
/* 43 */     this.u4lowBytes = din.readInt();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void writeInfo(DataOutput dout) throws IOException {
/* 49 */     dout.writeInt(this.u4highBytes);
/* 50 */     dout.writeInt(this.u4lowBytes);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/LongCpInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */