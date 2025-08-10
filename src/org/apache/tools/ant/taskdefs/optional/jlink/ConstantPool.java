/*    */ package org.apache.tools.ant.taskdefs.optional.jlink;
/*    */ 
/*    */ import java.io.DataInput;
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
/*    */ 
/*    */ class ConstantPool
/*    */ {
/*    */   static final byte UTF8 = 1;
/*    */   static final byte UNUSED = 2;
/*    */   static final byte INTEGER = 3;
/*    */   static final byte FLOAT = 4;
/*    */   static final byte LONG = 5;
/*    */   static final byte DOUBLE = 6;
/*    */   static final byte CLASS = 7;
/*    */   static final byte STRING = 8;
/*    */   static final byte FIELDREF = 9;
/*    */   static final byte METHODREF = 10;
/*    */   static final byte INTERFACEMETHODREF = 11;
/*    */   static final byte NAMEANDTYPE = 12;
/*    */   byte[] types;
/*    */   Object[] values;
/*    */   
/*    */   ConstantPool(DataInput data) throws IOException {
/* 51 */     int count = data.readUnsignedShort();
/* 52 */     this.types = new byte[count];
/* 53 */     this.values = new Object[count];
/*    */     
/* 55 */     for (int i = 1; i < count; i++) {
/* 56 */       byte type = data.readByte();
/* 57 */       this.types[i] = type;
/* 58 */       switch (type) {
/*    */         case 1:
/* 60 */           this.values[i] = data.readUTF();
/*    */           break;
/*    */ 
/*    */ 
/*    */ 
/*    */         
/*    */         case 3:
/*    */         case 9:
/*    */         case 10:
/*    */         case 11:
/*    */         case 12:
/* 71 */           this.values[i] = Integer.valueOf(data.readInt());
/*    */           break;
/*    */         
/*    */         case 4:
/* 75 */           this.values[i] = Float.valueOf(data.readFloat());
/*    */           break;
/*    */         
/*    */         case 5:
/* 79 */           this.values[i] = Long.valueOf(data.readLong());
/* 80 */           i++;
/*    */           break;
/*    */         
/*    */         case 6:
/* 84 */           this.values[i] = Double.valueOf(data.readDouble());
/* 85 */           i++;
/*    */           break;
/*    */         
/*    */         case 7:
/*    */         case 8:
/* 90 */           this.values[i] = Integer.valueOf(data.readUnsignedShort());
/*    */           break;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/jlink/ConstantPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */