/*    */ package org.apache.log4j;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.LineNumberReader;
/*    */ import java.io.PrintWriter;
/*    */ import java.io.StringReader;
/*    */ import java.io.StringWriter;
/*    */ import java.util.ArrayList;
/*    */ import org.apache.log4j.spi.ThrowableRenderer;
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
/*    */ public final class DefaultThrowableRenderer
/*    */   implements ThrowableRenderer
/*    */ {
/*    */   public String[] doRender(Throwable throwable) {
/* 46 */     return render(throwable);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String[] render(Throwable throwable) {
/* 56 */     StringWriter sw = new StringWriter();
/* 57 */     PrintWriter pw = new PrintWriter(sw);
/*    */     try {
/* 59 */       throwable.printStackTrace(pw);
/* 60 */     } catch (RuntimeException runtimeException) {}
/*    */     
/* 62 */     pw.flush();
/* 63 */     LineNumberReader reader = new LineNumberReader(new StringReader(sw.toString()));
/* 64 */     ArrayList<String> lines = new ArrayList();
/*    */     try {
/* 66 */       String line = reader.readLine();
/* 67 */       while (line != null) {
/* 68 */         lines.add(line);
/* 69 */         line = reader.readLine();
/*    */       } 
/* 71 */     } catch (IOException ex) {
/* 72 */       if (ex instanceof java.io.InterruptedIOException) {
/* 73 */         Thread.currentThread().interrupt();
/*    */       }
/* 75 */       lines.add(ex.toString());
/*    */     } 
/* 77 */     String[] tempRep = new String[lines.size()];
/* 78 */     lines.toArray(tempRep);
/* 79 */     return tempRep;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/DefaultThrowableRenderer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */