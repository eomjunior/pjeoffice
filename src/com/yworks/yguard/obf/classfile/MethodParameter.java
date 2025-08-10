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
/*    */ public class MethodParameter
/*    */ {
/*    */   final int u2nameIndex;
/*    */   final int u2accessFlags;
/*    */   
/*    */   private MethodParameter(int index, int flags) {
/* 24 */     this.u2nameIndex = index;
/* 25 */     this.u2accessFlags = flags;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static MethodParameter read(DataInput din) throws IOException {
/* 36 */     int index = din.readUnsignedShort();
/* 37 */     int flags = din.readUnsignedShort();
/* 38 */     return new MethodParameter(index, flags);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void write(DataOutput dout) throws IOException {
/* 48 */     dout.writeShort(this.u2nameIndex);
/* 49 */     dout.writeShort(this.u2accessFlags);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/MethodParameter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */