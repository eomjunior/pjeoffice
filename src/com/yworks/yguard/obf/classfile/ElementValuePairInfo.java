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
/*    */ public class ElementValuePairInfo
/*    */ {
/*    */   protected int u2ElementNameIndex;
/*    */   protected ElementValueInfo elementValue;
/*    */   
/*    */   public static ElementValuePairInfo create(DataInput din) throws IOException {
/* 32 */     ElementValuePairInfo evp = new ElementValuePairInfo();
/* 33 */     evp.read(din);
/* 34 */     return evp;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void read(DataInput din) throws IOException {
/* 44 */     this.u2ElementNameIndex = din.readUnsignedShort();
/* 45 */     this.elementValue = ElementValueInfo.create(din);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void markUtf8RefsInInfo(ConstantPool pool) {
/* 54 */     pool.getCpEntry(this.u2ElementNameIndex).incRefCount();
/* 55 */     this.elementValue.markUtf8RefsInInfo(pool);
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
/* 66 */     dout.writeShort(this.u2ElementNameIndex);
/* 67 */     this.elementValue.write(dout);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getU2ElementNameIndex() {
/* 76 */     return this.u2ElementNameIndex;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ElementValueInfo getElementValue() {
/* 85 */     return this.elementValue;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/ElementValuePairInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */