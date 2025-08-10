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
/*    */ public class StringCpInfo
/*    */   extends CpInfo
/*    */ {
/*    */   private int u2stringIndex;
/*    */   
/*    */   protected StringCpInfo() {
/* 35 */     super(8);
/*    */   }
/*    */   
/*    */   public int getStringIndex() {
/* 39 */     return this.u2stringIndex;
/*    */   }
/*    */   
/*    */   public void setStringIndex(int stringIndex) {
/* 43 */     this.u2stringIndex = stringIndex;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void markUtf8Refs(ConstantPool pool) {
/* 50 */     pool.incRefCount(this.u2stringIndex);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void readInfo(DataInput din) throws IOException {
/* 56 */     this.u2stringIndex = din.readUnsignedShort();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void writeInfo(DataOutput dout) throws IOException {
/* 62 */     dout.writeShort(this.u2stringIndex);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/StringCpInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */