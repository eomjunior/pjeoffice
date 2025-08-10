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
/*    */ public class SignatureAttrInfo
/*    */   extends AttrInfo
/*    */ {
/*    */   private int u2signatureIndex;
/*    */   
/*    */   protected SignatureAttrInfo(ClassFile cf, int attrNameIndex, int attrLength) {
/* 38 */     super(cf, attrNameIndex, attrLength);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected String getAttrName() {
/* 44 */     return "Signature";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected int getSignatureIndex() {
/* 54 */     return this.u2signatureIndex;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void setSignatureIndex(int index) {
/* 63 */     this.u2signatureIndex = index;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void markUtf8RefsInInfo(ConstantPool pool) {
/* 69 */     pool.incRefCount(this.u2signatureIndex);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void readInfo(DataInput din) throws IOException {
/* 75 */     this.u2signatureIndex = din.readUnsignedShort();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void writeInfo(DataOutput dout) throws IOException {
/* 81 */     dout.writeShort(this.u2signatureIndex);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 85 */     return super.toString() + ((Utf8CpInfo)this.owner.getCpEntry(this.u2signatureIndex)).getString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/SignatureAttrInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */