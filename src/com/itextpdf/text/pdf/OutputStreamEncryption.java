/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.pdf.crypto.AESCipher;
/*     */ import com.itextpdf.text.pdf.crypto.ARCFOUREncryption;
/*     */ import com.itextpdf.text.pdf.crypto.IVGenerator;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
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
/*     */ public class OutputStreamEncryption
/*     */   extends OutputStream
/*     */ {
/*     */   protected OutputStream out;
/*     */   protected ARCFOUREncryption arcfour;
/*     */   protected AESCipher cipher;
/*  57 */   private byte[] sb = new byte[1];
/*     */   
/*     */   private static final int AES_128 = 4;
/*     */   private static final int AES_256 = 5;
/*     */   private boolean aes;
/*     */   private boolean finished;
/*     */   
/*     */   public OutputStreamEncryption(OutputStream out, byte[] key, int off, int len, int revision) {
/*     */     try {
/*  66 */       this.out = out;
/*  67 */       this.aes = (revision == 4 || revision == 5);
/*  68 */       if (this.aes) {
/*  69 */         byte[] iv = IVGenerator.getIV();
/*  70 */         byte[] nkey = new byte[len];
/*  71 */         System.arraycopy(key, off, nkey, 0, len);
/*  72 */         this.cipher = new AESCipher(true, nkey, iv);
/*  73 */         write(iv);
/*     */       } else {
/*     */         
/*  76 */         this.arcfour = new ARCFOUREncryption();
/*  77 */         this.arcfour.prepareARCFOURKey(key, off, len);
/*     */       } 
/*  79 */     } catch (Exception ex) {
/*  80 */       throw new ExceptionConverter(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public OutputStreamEncryption(OutputStream out, byte[] key, int revision) {
/*  85 */     this(out, key, 0, key.length, revision);
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
/*     */   public void close() throws IOException {
/*  99 */     finish();
/* 100 */     this.out.close();
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
/*     */   
/*     */   public void flush() throws IOException {
/* 116 */     this.out.flush();
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
/*     */   public void write(byte[] b) throws IOException {
/* 130 */     write(b, 0, b.length);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(int b) throws IOException {
/* 149 */     this.sb[0] = (byte)b;
/* 150 */     write(this.sb, 0, 1);
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
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/* 182 */     if (this.aes) {
/* 183 */       byte[] b2 = this.cipher.update(b, off, len);
/* 184 */       if (b2 == null || b2.length == 0)
/*     */         return; 
/* 186 */       this.out.write(b2, 0, b2.length);
/*     */     } else {
/*     */       
/* 189 */       byte[] b2 = new byte[Math.min(len, 4192)];
/* 190 */       while (len > 0) {
/* 191 */         int sz = Math.min(len, b2.length);
/* 192 */         this.arcfour.encryptARCFOUR(b, off, sz, b2, 0);
/* 193 */         this.out.write(b2, 0, sz);
/* 194 */         len -= sz;
/* 195 */         off += sz;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void finish() throws IOException {
/* 201 */     if (!this.finished) {
/* 202 */       this.finished = true;
/* 203 */       if (this.aes) {
/*     */         byte[] b;
/*     */         try {
/* 206 */           b = this.cipher.doFinal();
/* 207 */         } catch (Exception ex) {
/* 208 */           throw new ExceptionConverter(ex);
/*     */         } 
/* 210 */         this.out.write(b, 0, b.length);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/OutputStreamEncryption.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */