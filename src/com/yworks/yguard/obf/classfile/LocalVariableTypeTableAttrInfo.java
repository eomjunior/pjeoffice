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
/*    */ public class LocalVariableTypeTableAttrInfo
/*    */   extends AttrInfo
/*    */ {
/*    */   private int u2localVariableTypeTableLength;
/*    */   private LocalVariableTypeInfo[] localVariableTypeTable;
/*    */   
/*    */   protected LocalVariableTypeTableAttrInfo(ClassFile cf, int attrNameIndex, int attrLength) {
/* 40 */     super(cf, attrNameIndex, attrLength);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected String getAttrName() {
/* 46 */     return "LocalVariableTypeTable";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected LocalVariableTypeInfo[] getLocalVariableTypeTable() {
/* 56 */     return this.localVariableTypeTable;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setLocalVariableTypeTable(LocalVariableTypeInfo[] lvts) {
/* 65 */     this.localVariableTypeTable = lvts;
/* 66 */     this.u2localVariableTypeTableLength = lvts.length;
/* 67 */     this.u4attrLength = 2 + 10 * this.u2localVariableTypeTableLength;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void markUtf8RefsInInfo(ConstantPool pool) {
/* 73 */     for (int i = 0; i < this.localVariableTypeTable.length; i++)
/*    */     {
/* 75 */       this.localVariableTypeTable[i].markUtf8Refs(pool);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void readInfo(DataInput din) throws IOException {
/* 82 */     this.u2localVariableTypeTableLength = din.readUnsignedShort();
/* 83 */     this.localVariableTypeTable = new LocalVariableTypeInfo[this.u2localVariableTypeTableLength];
/* 84 */     for (int i = 0; i < this.u2localVariableTypeTableLength; i++)
/*    */     {
/* 86 */       this.localVariableTypeTable[i] = LocalVariableTypeInfo.create(din);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void writeInfo(DataOutput dout) throws IOException {
/* 93 */     dout.writeShort(this.u2localVariableTypeTableLength);
/* 94 */     for (int i = 0; i < this.u2localVariableTypeTableLength; i++)
/*    */     {
/* 96 */       this.localVariableTypeTable[i].write(dout);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/LocalVariableTypeTableAttrInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */