/*     */ package org.apache.tools.ant.types.selectors.modifiedselector;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.security.DigestInputStream;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.Locale;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DigestAlgorithm
/*     */   implements Algorithm
/*     */ {
/*     */   private static final int BYTE_MASK = 255;
/*     */   private static final int BUFFER_SIZE = 8192;
/*  72 */   private String algorithm = "MD5";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   private String provider = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   private MessageDigest messageDigest = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   private int readBufferSize = 8192;
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
/*  99 */     this
/* 100 */       .algorithm = (algorithm != null) ? algorithm.toUpperCase(Locale.ENGLISH) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProvider(String provider) {
/* 110 */     this.provider = provider;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void initMessageDigest() {
/* 116 */     if (this.messageDigest != null) {
/*     */       return;
/*     */     }
/*     */     
/* 120 */     if (this.provider != null && !this.provider.isEmpty() && !"null".equals(this.provider)) {
/*     */       try {
/* 122 */         this.messageDigest = MessageDigest.getInstance(this.algorithm, this.provider);
/* 123 */       } catch (NoSuchAlgorithmException|java.security.NoSuchProviderException e) {
/* 124 */         throw new BuildException(e);
/*     */       } 
/*     */     } else {
/*     */       try {
/* 128 */         this.messageDigest = MessageDigest.getInstance(this.algorithm);
/* 129 */       } catch (NoSuchAlgorithmException noalgo) {
/* 130 */         throw new BuildException(noalgo);
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
/*     */   public boolean isValid() {
/* 145 */     return ("SHA".equals(this.algorithm) || "MD5".equals(this.algorithm));
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
/* 156 */     if (!file.canRead()) {
/* 157 */       return null;
/*     */     }
/* 159 */     initMessageDigest();
/* 160 */     byte[] buf = new byte[this.readBufferSize];
/* 161 */     this.messageDigest.reset(); 
/* 162 */     try { DigestInputStream dis = new DigestInputStream(Files.newInputStream(file.toPath(), new java.nio.file.OpenOption[0]), this.messageDigest);
/*     */ 
/*     */       
/* 165 */       try { while (dis.read(buf, 0, this.readBufferSize) != -1);
/*     */         
/* 167 */         StringBuilder checksumSb = new StringBuilder();
/* 168 */         for (byte digestByte : this.messageDigest.digest()) {
/* 169 */           checksumSb.append(String.format("%02x", new Object[] { Integer.valueOf(0xFF & digestByte) }));
/*     */         } 
/* 171 */         String str = checksumSb.toString();
/* 172 */         dis.close(); return str; } catch (Throwable throwable) { try { dis.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException ignored)
/* 173 */     { return null; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 183 */     return String.format("<DigestAlgorithm:algorithm=%s;provider=%s>", new Object[] { this.algorithm, this.provider });
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/modifiedselector/DigestAlgorithm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */