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
/*    */ public class LocalVariableTableAttrInfo
/*    */   extends AttrInfo
/*    */ {
/*    */   private int u2localVariableTableLength;
/*    */   private LocalVariableInfo[] localVariableTable;
/*    */   
/*    */   protected LocalVariableTableAttrInfo(ClassFile cf, int attrNameIndex, int attrLength) {
/* 40 */     super(cf, attrNameIndex, attrLength);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected String getAttrName() {
/* 47 */     return "LocalVariableTable";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected LocalVariableInfo[] getLocalVariableTable() {
/* 57 */     return this.localVariableTable;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setLocalVariableTable(LocalVariableInfo[] lvts) {
/* 66 */     this.localVariableTable = lvts;
/* 67 */     this.u2localVariableTableLength = lvts.length;
/* 68 */     this.u4attrLength = 2 + 10 * this.u2localVariableTableLength;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void markUtf8RefsInInfo(ConstantPool pool) {
/* 74 */     for (int i = 0; i < this.localVariableTable.length; i++)
/*    */     {
/* 76 */       this.localVariableTable[i].markUtf8Refs(pool);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void readInfo(DataInput din) throws IOException {
/* 83 */     this.u2localVariableTableLength = din.readUnsignedShort();
/* 84 */     this.localVariableTable = new LocalVariableInfo[this.u2localVariableTableLength];
/* 85 */     for (int i = 0; i < this.u2localVariableTableLength; i++)
/*    */     {
/* 87 */       this.localVariableTable[i] = LocalVariableInfo.create(din);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void writeInfo(DataOutput dout) throws IOException {
/* 94 */     dout.writeShort(this.u2localVariableTableLength);
/* 95 */     for (int i = 0; i < this.u2localVariableTableLength; i++)
/*    */     {
/* 97 */       this.localVariableTable[i].write(dout);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/LocalVariableTableAttrInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */