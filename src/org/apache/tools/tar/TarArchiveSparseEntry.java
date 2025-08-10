/*    */ package org.apache.tools.tar;
/*    */ 
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
/*    */ public class TarArchiveSparseEntry
/*    */   implements TarConstants
/*    */ {
/*    */   private boolean isExtended;
/*    */   
/*    */   public TarArchiveSparseEntry(byte[] headerBuf) throws IOException {
/* 55 */     int offset = 0;
/* 56 */     offset += 504;
/* 57 */     this.isExtended = TarUtils.parseBoolean(headerBuf, offset);
/*    */   }
/*    */   
/*    */   public boolean isExtended() {
/* 61 */     return this.isExtended;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/tar/TarArchiveSparseEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */