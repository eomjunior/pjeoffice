/*     */ package org.apache.tools.ant.types.selectors.modifiedselector;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.nio.file.Files;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.Locale;
/*     */ import java.util.zip.Adler32;
/*     */ import java.util.zip.CRC32;
/*     */ import java.util.zip.CheckedInputStream;
/*     */ import java.util.zip.Checksum;
/*     */ import org.apache.tools.ant.BuildException;
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
/*     */ public class ChecksumAlgorithm
/*     */   implements Algorithm
/*     */ {
/*  64 */   private String algorithm = "CRC";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   private Checksum checksum = null;
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
/*     */   public void setAlgorithm(String algorithm) {
/*  81 */     this
/*  82 */       .algorithm = (algorithm != null) ? algorithm.toUpperCase(Locale.ENGLISH) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void initChecksum() {
/*  88 */     if (this.checksum != null) {
/*     */       return;
/*     */     }
/*  91 */     if ("CRC".equals(this.algorithm)) {
/*  92 */       this.checksum = new CRC32();
/*  93 */     } else if ("ADLER".equals(this.algorithm)) {
/*  94 */       this.checksum = new Adler32();
/*     */     } else {
/*  96 */       throw new BuildException(new NoSuchAlgorithmException());
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
/*     */   public boolean isValid() {
/* 110 */     return ("CRC".equals(this.algorithm) || "ADLER".equals(this.algorithm));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getValue(File file) {
/* 121 */     initChecksum();
/*     */     
/* 123 */     if (file.canRead()) {
/* 124 */       this.checksum.reset();
/*     */       
/* 126 */       try { CheckedInputStream check = new CheckedInputStream(new BufferedInputStream(Files.newInputStream(file.toPath(), new java.nio.file.OpenOption[0])), this.checksum);
/*     */         
/* 128 */         try { while (check.read() != -1);
/*     */           
/* 130 */           String str = Long.toString(check.getChecksum().getValue());
/* 131 */           check.close(); return str; } catch (Throwable throwable) { try { check.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Exception exception) {}
/*     */     } 
/*     */     
/* 134 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 144 */     return String.format("<ChecksumAlgorithm:algorithm=%s>", new Object[] { this.algorithm });
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/modifiedselector/ChecksumAlgorithm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */