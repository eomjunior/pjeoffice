/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.lms;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import org.bouncycastle.util.Encodable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Composer
/*     */ {
/*  12 */   private final ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static org.bouncycastle.pqc.crypto.lms.Composer compose() {
/*  21 */     return new org.bouncycastle.pqc.crypto.lms.Composer();
/*     */   }
/*     */ 
/*     */   
/*     */   public org.bouncycastle.pqc.crypto.lms.Composer u64str(long paramLong) {
/*  26 */     u32str((int)(paramLong >>> 32L));
/*  27 */     u32str((int)paramLong);
/*     */     
/*  29 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public org.bouncycastle.pqc.crypto.lms.Composer u32str(int paramInt) {
/*  34 */     this.bos.write((byte)(paramInt >>> 24));
/*  35 */     this.bos.write((byte)(paramInt >>> 16));
/*  36 */     this.bos.write((byte)(paramInt >>> 8));
/*  37 */     this.bos.write((byte)paramInt);
/*  38 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public org.bouncycastle.pqc.crypto.lms.Composer u16str(int paramInt) {
/*  43 */     paramInt &= 0xFFFF;
/*  44 */     this.bos.write((byte)(paramInt >>> 8));
/*  45 */     this.bos.write((byte)paramInt);
/*  46 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public org.bouncycastle.pqc.crypto.lms.Composer bytes(Encodable[] paramArrayOfEncodable) {
/*     */     try {
/*  53 */       for (Encodable encodable : paramArrayOfEncodable)
/*     */       {
/*  55 */         this.bos.write(encodable.getEncoded());
/*     */       }
/*     */     }
/*  58 */     catch (Exception exception) {
/*     */       
/*  60 */       throw new RuntimeException(exception.getMessage(), exception);
/*     */     } 
/*     */     
/*  63 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public org.bouncycastle.pqc.crypto.lms.Composer bytes(Encodable paramEncodable) {
/*     */     try {
/*  71 */       this.bos.write(paramEncodable.getEncoded());
/*     */     }
/*  73 */     catch (Exception exception) {
/*     */       
/*  75 */       throw new RuntimeException(exception.getMessage(), exception);
/*     */     } 
/*     */     
/*  78 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public org.bouncycastle.pqc.crypto.lms.Composer pad(int paramInt1, int paramInt2) {
/*  83 */     for (; paramInt2 >= 0; paramInt2--) {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/*  88 */         this.bos.write(paramInt1);
/*     */       
/*     */       }
/*  91 */       catch (Exception exception) {
/*     */         
/*  93 */         throw new RuntimeException(exception.getMessage(), exception);
/*     */       } 
/*     */     } 
/*     */     
/*  97 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public org.bouncycastle.pqc.crypto.lms.Composer bytes(byte[][] paramArrayOfbyte) {
/*     */     try {
/* 104 */       for (byte[] arrayOfByte : paramArrayOfbyte)
/*     */       {
/* 106 */         this.bos.write(arrayOfByte);
/*     */       }
/*     */     }
/* 109 */     catch (Exception exception) {
/*     */       
/* 111 */       throw new RuntimeException(exception.getMessage(), exception);
/*     */     } 
/*     */     
/* 114 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public org.bouncycastle.pqc.crypto.lms.Composer bytes(byte[][] paramArrayOfbyte, int paramInt1, int paramInt2) {
/*     */     try {
/* 121 */       int i = paramInt1;
/* 122 */       while (i != paramInt2)
/*     */       {
/* 124 */         this.bos.write(paramArrayOfbyte[i]);
/* 125 */         i++;
/*     */       }
/*     */     
/* 128 */     } catch (Exception exception) {
/*     */       
/* 130 */       throw new RuntimeException(exception.getMessage(), exception);
/*     */     } 
/*     */     
/* 133 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public org.bouncycastle.pqc.crypto.lms.Composer bytes(byte[] paramArrayOfbyte) {
/*     */     try {
/* 141 */       this.bos.write(paramArrayOfbyte);
/*     */     }
/* 143 */     catch (Exception exception) {
/*     */       
/* 145 */       throw new RuntimeException(exception.getMessage(), exception);
/*     */     } 
/*     */     
/* 148 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public org.bouncycastle.pqc.crypto.lms.Composer bytes(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/*     */     try {
/* 156 */       this.bos.write(paramArrayOfbyte, paramInt1, paramInt2);
/*     */     }
/* 158 */     catch (Exception exception) {
/*     */       
/* 160 */       throw new RuntimeException(exception.getMessage(), exception);
/*     */     } 
/*     */     
/* 163 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] build() {
/* 168 */     return this.bos.toByteArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public org.bouncycastle.pqc.crypto.lms.Composer padUntil(int paramInt1, int paramInt2) {
/* 173 */     while (this.bos.size() < paramInt2)
/*     */     {
/* 175 */       this.bos.write(paramInt1);
/*     */     }
/*     */     
/* 178 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public org.bouncycastle.pqc.crypto.lms.Composer bool(boolean paramBoolean) {
/* 183 */     this.bos.write(paramBoolean ? 1 : 0);
/* 184 */     return this;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/lms/Composer.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */