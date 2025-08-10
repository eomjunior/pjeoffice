/*    */ package com.yworks.util.abstractjar;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.jar.Manifest;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ArchiveWriter
/*    */ {
/*    */   protected ArchiveWriter(Manifest man) throws IOException {
/* 14 */     if (man == null)
/* 15 */       throw new NullPointerException("man"); 
/*    */   }
/*    */   
/*    */   public abstract void setComment(String paramString);
/*    */   
/*    */   public abstract void addDirectory(String paramString) throws IOException;
/*    */   
/*    */   public abstract void addFile(String paramString, byte[] paramArrayOfbyte) throws IOException;
/*    */   
/*    */   public abstract void close() throws IOException;
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/util/abstractjar/ArchiveWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */