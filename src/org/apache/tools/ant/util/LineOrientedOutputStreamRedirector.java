/*    */ package org.apache.tools.ant.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
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
/*    */ public class LineOrientedOutputStreamRedirector
/*    */   extends LineOrientedOutputStream
/*    */ {
/*    */   private OutputStream stream;
/*    */   
/*    */   public LineOrientedOutputStreamRedirector(OutputStream stream) {
/* 38 */     this.stream = stream;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void processLine(byte[] b) throws IOException {
/* 43 */     this.stream.write(b);
/* 44 */     this.stream.write(System.lineSeparator().getBytes());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void processLine(String line) throws IOException {
/* 49 */     this.stream.write(String.format("%s%n", new Object[] { line }).getBytes());
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 54 */     super.close();
/* 55 */     this.stream.close();
/*    */   }
/*    */ 
/*    */   
/*    */   public void flush() throws IOException {
/* 60 */     super.flush();
/* 61 */     this.stream.flush();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/LineOrientedOutputStreamRedirector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */