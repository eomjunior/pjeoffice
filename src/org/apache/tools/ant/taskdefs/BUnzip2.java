/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.file.Files;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ import org.apache.tools.bzip2.CBZip2InputStream;
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
/*     */ public class BUnzip2
/*     */   extends Unpack
/*     */ {
/*     */   private static final int BUFFER_SIZE = 8192;
/*     */   private static final String DEFAULT_EXTENSION = ".bz2";
/*     */   
/*     */   protected String getDefaultExtension() {
/*  53 */     return ".bz2";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void extract() {
/*  60 */     if (this.srcResource.getLastModified() > this.dest.lastModified()) {
/*  61 */       log("Expanding " + this.srcResource.getName() + " to " + this.dest
/*  62 */           .getAbsolutePath());
/*     */       
/*  64 */       OutputStream out = null;
/*  65 */       CBZip2InputStream zIn = null;
/*  66 */       InputStream fis = null;
/*  67 */       BufferedInputStream bis = null;
/*     */       try {
/*  69 */         out = Files.newOutputStream(this.dest.toPath(), new java.nio.file.OpenOption[0]);
/*  70 */         fis = this.srcResource.getInputStream();
/*  71 */         bis = new BufferedInputStream(fis);
/*  72 */         int b = bis.read();
/*  73 */         if (b != 66) {
/*  74 */           throw new BuildException("Invalid bz2 file.", getLocation());
/*     */         }
/*  76 */         b = bis.read();
/*  77 */         if (b != 90) {
/*  78 */           throw new BuildException("Invalid bz2 file.", getLocation());
/*     */         }
/*  80 */         zIn = new CBZip2InputStream(bis, true);
/*  81 */         byte[] buffer = new byte[8192];
/*  82 */         int count = 0;
/*     */         do {
/*  84 */           out.write(buffer, 0, count);
/*  85 */           count = zIn.read(buffer, 0, buffer.length);
/*  86 */         } while (count != -1);
/*  87 */       } catch (IOException ioe) {
/*  88 */         String msg = "Problem expanding bzip2 " + ioe.getMessage();
/*  89 */         throw new BuildException(msg, ioe, getLocation());
/*     */       } finally {
/*  91 */         FileUtils.close(bis);
/*  92 */         FileUtils.close(fis);
/*  93 */         FileUtils.close(out);
/*  94 */         FileUtils.close((InputStream)zIn);
/*     */       } 
/*     */     } 
/*     */   }
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
/*     */   protected boolean supportsNonFileResources() {
/* 111 */     return getClass().equals(BUnzip2.class);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/BUnzip2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */