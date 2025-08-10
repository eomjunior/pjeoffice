/*    */ package org.apache.tools.ant.taskdefs.email;
/*    */ 
/*    */ import java.io.BufferedInputStream;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.PrintStream;
/*    */ import java.nio.file.Files;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.util.UUEncoder;
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
/*    */ class UUMailer
/*    */   extends PlainMailer
/*    */ {
/*    */   protected void attach(File file, PrintStream out) throws IOException {
/* 39 */     if (!file.exists() || !file.canRead()) {
/* 40 */       throw new BuildException("File \"%s\" does not exist or is not readable.", new Object[] { file
/*    */             
/* 42 */             .getAbsolutePath() });
/*    */     }
/*    */ 
/*    */     
/* 46 */     InputStream in = new BufferedInputStream(Files.newInputStream(file.toPath(), new java.nio.file.OpenOption[0])); try {
/* 47 */       (new UUEncoder(file.getName())).encode(in, out);
/* 48 */       in.close();
/*    */     } catch (Throwable throwable) {
/*    */       try {
/*    */         in.close();
/*    */       } catch (Throwable throwable1) {
/*    */         throwable.addSuppressed(throwable1);
/*    */       } 
/*    */       throw throwable;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/email/UUMailer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */