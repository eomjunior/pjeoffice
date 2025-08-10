/*    */ package org.apache.tools.ant;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
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
/*    */ public class DemuxInputStream
/*    */   extends InputStream
/*    */ {
/*    */   private static final int MASK_8BIT = 255;
/*    */   private Project project;
/*    */   
/*    */   public DemuxInputStream(Project project) {
/* 45 */     this.project = project;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int read() throws IOException {
/* 55 */     byte[] buffer = new byte[1];
/* 56 */     if (this.project.demuxInput(buffer, 0, 1) == -1) {
/* 57 */       return -1;
/*    */     }
/* 59 */     return buffer[0] & 0xFF;
/*    */   }
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
/*    */   public int read(byte[] buffer, int offset, int length) throws IOException {
/* 73 */     return this.project.demuxInput(buffer, offset, length);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/DemuxInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */