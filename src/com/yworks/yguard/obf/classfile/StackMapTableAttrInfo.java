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
/*    */ public class StackMapTableAttrInfo
/*    */   extends AttrInfo
/*    */ {
/*    */   private int u2NumberOfEntries;
/*    */   private StackMapFrameInfo[] entries;
/*    */   
/*    */   protected StackMapTableAttrInfo(ClassFile cf, int attrNameIndex, int attrLength) {
/* 31 */     super(cf, attrNameIndex, attrLength);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected String getAttrName() {
/* 38 */     return "StackMapTable";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected StackMapFrameInfo[] getEntries() {
/* 48 */     return this.entries;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void markUtf8RefsInInfo(ConstantPool pool) {
/* 54 */     for (int i = 0; i < this.entries.length; i++)
/*    */     {
/* 56 */       this.entries[i].markUtf8Refs(pool);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void readInfo(DataInput din) throws IOException {
/* 63 */     this.u2NumberOfEntries = din.readUnsignedShort();
/* 64 */     this.entries = new StackMapFrameInfo[this.u2NumberOfEntries];
/* 65 */     for (int i = 0; i < this.u2NumberOfEntries; i++)
/*    */     {
/* 67 */       this.entries[i] = StackMapFrameInfo.create(din);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void writeInfo(DataOutput dout) throws IOException {
/* 74 */     dout.writeShort(this.u2NumberOfEntries);
/* 75 */     for (int i = 0; i < this.u2NumberOfEntries; i++)
/*    */     {
/* 77 */       this.entries[i].write(dout);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/StackMapTableAttrInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */