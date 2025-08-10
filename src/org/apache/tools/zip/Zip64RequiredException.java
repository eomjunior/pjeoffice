/*    */ package org.apache.tools.zip;
/*    */ 
/*    */ import java.util.zip.ZipException;
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
/*    */ public class Zip64RequiredException
/*    */   extends ZipException
/*    */ {
/*    */   private static final long serialVersionUID = 20110809L;
/*    */   static final String ARCHIVE_TOO_BIG_MESSAGE = "archive's size exceeds the limit of 4GByte.";
/*    */   static final String TOO_MANY_ENTRIES_MESSAGE = "archive contains more than 65535 entries.";
/*    */   
/*    */   static String getEntryTooBigMessage(ZipEntry ze) {
/* 37 */     return ze.getName() + "'s size exceeds the limit of 4GByte.";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Zip64RequiredException(String reason) {
/* 47 */     super(reason);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/zip/Zip64RequiredException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */