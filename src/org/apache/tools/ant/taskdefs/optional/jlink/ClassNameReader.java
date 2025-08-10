/*     */ package org.apache.tools.ant.taskdefs.optional.jlink;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassNameReader
/*     */ {
/*     */   private static final int CLASS_MAGIC_NUMBER = -889275714;
/*     */   
/*     */   public static String getClassName(InputStream input) throws IOException {
/* 116 */     DataInputStream data = new DataInputStream(input);
/*     */     
/* 118 */     int cookie = data.readInt();
/* 119 */     if (cookie != -889275714) {
/* 120 */       return null;
/*     */     }
/* 122 */     data.readInt();
/*     */     
/* 124 */     ConstantPool constants = new ConstantPool(data);
/* 125 */     Object[] values = constants.values;
/*     */     
/* 127 */     data.readUnsignedShort();
/* 128 */     int classIndex = data.readUnsignedShort();
/* 129 */     Integer stringIndex = (Integer)values[classIndex];
/* 130 */     return (String)values[stringIndex.intValue()];
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/jlink/ClassNameReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */