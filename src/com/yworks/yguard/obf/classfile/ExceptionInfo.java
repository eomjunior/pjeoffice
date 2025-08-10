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
/*    */ 
/*    */ 
/*    */ public class ExceptionInfo
/*    */ {
/*    */   public static final int CONSTANT_FIELD_SIZE = 8;
/*    */   private int u2startpc;
/*    */   private int u2endpc;
/*    */   private int u2handlerpc;
/*    */   private int u2catchType;
/*    */   
/*    */   public static ExceptionInfo create(DataInput din) throws IOException {
/* 43 */     ExceptionInfo ei = new ExceptionInfo();
/* 44 */     ei.read(din);
/* 45 */     return ei;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void read(DataInput din) throws IOException {
/* 53 */     this.u2startpc = din.readUnsignedShort();
/* 54 */     this.u2endpc = din.readUnsignedShort();
/* 55 */     this.u2handlerpc = din.readUnsignedShort();
/* 56 */     this.u2catchType = din.readUnsignedShort();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(DataOutput dout) throws IOException {
/* 67 */     dout.writeShort(this.u2startpc);
/* 68 */     dout.writeShort(this.u2endpc);
/* 69 */     dout.writeShort(this.u2handlerpc);
/* 70 */     dout.writeShort(this.u2catchType);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/ExceptionInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */