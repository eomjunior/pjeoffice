/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.lms;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import org.bouncycastle.pqc.crypto.lms.Composer;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMOtsSignature;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSigParameters;
/*     */ import org.bouncycastle.util.Encodable;
/*     */ import org.bouncycastle.util.io.Streams;
/*     */ 
/*     */ class LMSSignature
/*     */   implements Encodable {
/*     */   private final int q;
/*     */   private final LMOtsSignature otsSignature;
/*     */   private final LMSigParameters parameter;
/*     */   private final byte[][] y;
/*     */   
/*     */   public LMSSignature(int paramInt, LMOtsSignature paramLMOtsSignature, LMSigParameters paramLMSigParameters, byte[][] paramArrayOfbyte) {
/*  22 */     this.q = paramInt;
/*  23 */     this.otsSignature = paramLMOtsSignature;
/*  24 */     this.parameter = paramLMSigParameters;
/*  25 */     this.y = paramArrayOfbyte;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static org.bouncycastle.pqc.crypto.lms.LMSSignature getInstance(Object paramObject) throws IOException {
/*  31 */     if (paramObject instanceof org.bouncycastle.pqc.crypto.lms.LMSSignature)
/*     */     {
/*  33 */       return (org.bouncycastle.pqc.crypto.lms.LMSSignature)paramObject;
/*     */     }
/*  35 */     if (paramObject instanceof DataInputStream) {
/*     */       
/*  37 */       int i = ((DataInputStream)paramObject).readInt();
/*  38 */       LMOtsSignature lMOtsSignature = LMOtsSignature.getInstance(paramObject);
/*  39 */       LMSigParameters lMSigParameters = LMSigParameters.getParametersForType(((DataInputStream)paramObject).readInt());
/*     */       
/*  41 */       byte[][] arrayOfByte = new byte[lMSigParameters.getH()][];
/*  42 */       for (byte b = 0; b < arrayOfByte.length; b++) {
/*     */         
/*  44 */         arrayOfByte[b] = new byte[lMSigParameters.getM()];
/*  45 */         ((DataInputStream)paramObject).readFully(arrayOfByte[b]);
/*     */       } 
/*     */       
/*  48 */       return new org.bouncycastle.pqc.crypto.lms.LMSSignature(i, lMOtsSignature, lMSigParameters, arrayOfByte);
/*     */     } 
/*  50 */     if (paramObject instanceof byte[]) {
/*     */       
/*  52 */       DataInputStream dataInputStream = null;
/*     */       
/*     */       try {
/*  55 */         dataInputStream = new DataInputStream(new ByteArrayInputStream((byte[])paramObject));
/*  56 */         return getInstance(dataInputStream);
/*     */       }
/*     */       finally {
/*     */         
/*  60 */         if (dataInputStream != null) dataInputStream.close(); 
/*     */       } 
/*     */     } 
/*  63 */     if (paramObject instanceof InputStream)
/*     */     {
/*  65 */       return getInstance(Streams.readAll((InputStream)paramObject));
/*     */     }
/*     */     
/*  68 */     throw new IllegalArgumentException("cannot parse " + paramObject);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  74 */     if (this == paramObject)
/*     */     {
/*  76 */       return true;
/*     */     }
/*  78 */     if (paramObject == null || getClass() != paramObject.getClass())
/*     */     {
/*  80 */       return false;
/*     */     }
/*     */     
/*  83 */     org.bouncycastle.pqc.crypto.lms.LMSSignature lMSSignature = (org.bouncycastle.pqc.crypto.lms.LMSSignature)paramObject;
/*     */     
/*  85 */     if (this.q != lMSSignature.q)
/*     */     {
/*  87 */       return false;
/*     */     }
/*  89 */     if ((this.otsSignature != null) ? !this.otsSignature.equals(lMSSignature.otsSignature) : (lMSSignature.otsSignature != null))
/*     */     {
/*  91 */       return false;
/*     */     }
/*  93 */     if ((this.parameter != null) ? !this.parameter.equals(lMSSignature.parameter) : (lMSSignature.parameter != null))
/*     */     {
/*  95 */       return false;
/*     */     }
/*  97 */     return Arrays.deepEquals((Object[])this.y, (Object[])lMSSignature.y);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 103 */     int i = this.q;
/* 104 */     i = 31 * i + ((this.otsSignature != null) ? this.otsSignature.hashCode() : 0);
/* 105 */     i = 31 * i + ((this.parameter != null) ? this.parameter.hashCode() : 0);
/* 106 */     i = 31 * i + Arrays.deepHashCode((Object[])this.y);
/* 107 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getEncoded() throws IOException {
/* 113 */     return Composer.compose()
/* 114 */       .u32str(this.q)
/* 115 */       .bytes(this.otsSignature.getEncoded())
/* 116 */       .u32str(this.parameter.getType())
/* 117 */       .bytes(this.y)
/* 118 */       .build();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getQ() {
/* 123 */     return this.q;
/*     */   }
/*     */ 
/*     */   
/*     */   public LMOtsSignature getOtsSignature() {
/* 128 */     return this.otsSignature;
/*     */   }
/*     */ 
/*     */   
/*     */   public LMSigParameters getParameter() {
/* 133 */     return this.parameter;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[][] getY() {
/* 138 */     return this.y;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/lms/LMSSignature.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */