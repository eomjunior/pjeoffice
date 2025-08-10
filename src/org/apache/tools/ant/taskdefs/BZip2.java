/*    */ package org.apache.tools.ant.taskdefs;
/*    */ 
/*    */ import java.io.BufferedOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.nio.file.Files;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.util.FileUtils;
/*    */ import org.apache.tools.bzip2.CBZip2OutputStream;
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
/*    */ public class BZip2
/*    */   extends Pack
/*    */ {
/*    */   protected void pack() {
/* 44 */     CBZip2OutputStream zOut = null;
/*    */     
/*    */     try {
/* 47 */       BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(this.zipFile.toPath(), new java.nio.file.OpenOption[0]));
/* 48 */       bos.write(66);
/* 49 */       bos.write(90);
/* 50 */       zOut = new CBZip2OutputStream(bos);
/* 51 */       zipResource(getSrcResource(), (OutputStream)zOut);
/* 52 */     } catch (IOException ioe) {
/* 53 */       String msg = "Problem creating bzip2 " + ioe.getMessage();
/* 54 */       throw new BuildException(msg, ioe, getLocation());
/*    */     } finally {
/* 56 */       FileUtils.close((OutputStream)zOut);
/*    */     } 
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
/*    */   
/*    */   protected boolean supportsNonFileResources() {
/* 72 */     return getClass().equals(BZip2.class);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/BZip2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */