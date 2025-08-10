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
/*    */ public class LineNumberTableAttrInfo
/*    */   extends AttrInfo
/*    */ {
/*    */   private int u2lineNumberTableLength;
/*    */   private LineNumberInfo[] lineNumberTable;
/*    */   
/*    */   protected LineNumberTableAttrInfo(ClassFile cf, int attrNameIndex, int attrLength) {
/* 40 */     super(cf, attrNameIndex, attrLength);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected String getAttrName() {
/* 47 */     return "LineNumberTable";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LineNumberInfo[] getLineNumberTable() {
/* 56 */     return this.lineNumberTable;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setLineNumberTable(LineNumberInfo[] table) {
/* 65 */     this.lineNumberTable = table;
/* 66 */     this.u2lineNumberTableLength = this.lineNumberTable.length;
/* 67 */     this.u4attrLength = 2 + 4 * this.u2lineNumberTableLength;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void readInfo(DataInput din) throws IOException {
/* 73 */     this.u2lineNumberTableLength = din.readUnsignedShort();
/* 74 */     this.lineNumberTable = new LineNumberInfo[this.u2lineNumberTableLength];
/* 75 */     for (int i = 0; i < this.u2lineNumberTableLength; i++)
/*    */     {
/* 77 */       this.lineNumberTable[i] = LineNumberInfo.create(din);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void writeInfo(DataOutput dout) throws IOException {
/* 84 */     dout.writeShort(this.u2lineNumberTableLength);
/* 85 */     for (int i = 0; i < this.u2lineNumberTableLength; i++)
/*    */     {
/* 87 */       this.lineNumberTable[i].write(dout);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/LineNumberTableAttrInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */