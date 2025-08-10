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
/*    */ public class BootstrapMethodsAttrInfo
/*    */   extends AttrInfo
/*    */ {
/*    */   private BootstrapMethod[] bootstrapMethods;
/*    */   
/*    */   protected BootstrapMethodsAttrInfo(ClassFile cf, int attrNameIndex, int attrLength) {
/* 31 */     super(cf, attrNameIndex, attrLength);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected String getAttrName() {
/* 38 */     return "BootstrapMethods";
/*    */   }
/*    */ 
/*    */   
/*    */   protected void readInfo(DataInput din) throws IOException {
/* 43 */     int u2numBootstrapMethods = din.readUnsignedShort();
/* 44 */     this.bootstrapMethods = new BootstrapMethod[u2numBootstrapMethods];
/* 45 */     for (int i = 0; i < u2numBootstrapMethods; i++) {
/* 46 */       this.bootstrapMethods[i] = BootstrapMethod.read(din);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeInfo(DataOutput dout) throws IOException {
/* 52 */     int u2numBootstrapMethods = this.bootstrapMethods.length;
/* 53 */     dout.writeShort(u2numBootstrapMethods);
/* 54 */     for (int i = 0; i < u2numBootstrapMethods; i++) {
/* 55 */       this.bootstrapMethods[i].write(dout);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BootstrapMethod[] getBootstrapMethods() {
/* 65 */     return this.bootstrapMethods;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/BootstrapMethodsAttrInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */