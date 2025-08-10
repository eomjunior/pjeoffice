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
/*    */ public class SourceFileAttrInfo
/*    */   extends AttrInfo
/*    */ {
/*    */   private int u2sourceFileIndex;
/*    */   
/*    */   protected SourceFileAttrInfo(ClassFile cf, int attrNameIndex, int attrLength) {
/* 38 */     super(cf, attrNameIndex, attrLength);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected String getAttrName() {
/* 45 */     return "SourceFile";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void markUtf8RefsInInfo(ConstantPool pool) {
/* 51 */     pool.incRefCount(this.u2sourceFileIndex);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void readInfo(DataInput din) throws IOException {
/* 57 */     this.u2sourceFileIndex = din.readUnsignedShort();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void setSourceFileIndex(int index) {
/* 66 */     this.u2sourceFileIndex = index;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected int getSourceFileIndex() {
/* 75 */     return this.u2sourceFileIndex;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void writeInfo(DataOutput dout) throws IOException {
/* 81 */     dout.writeShort(this.u2sourceFileIndex);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/SourceFileAttrInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */