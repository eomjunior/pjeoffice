/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.lms;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import org.bouncycastle.pqc.crypto.lms.Composer;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMOtsParameters;
/*     */ import org.bouncycastle.util.Encodable;
/*     */ import org.bouncycastle.util.io.Streams;
/*     */ 
/*     */ class LMOtsSignature
/*     */   implements Encodable
/*     */ {
/*     */   private final LMOtsParameters type;
/*     */   private final byte[] C;
/*     */   private final byte[] y;
/*     */   
/*     */   public LMOtsSignature(LMOtsParameters paramLMOtsParameters, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
/*  21 */     this.type = paramLMOtsParameters;
/*  22 */     this.C = paramArrayOfbyte1;
/*  23 */     this.y = paramArrayOfbyte2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static org.bouncycastle.pqc.crypto.lms.LMOtsSignature getInstance(Object paramObject) throws IOException {
/*  29 */     if (paramObject instanceof org.bouncycastle.pqc.crypto.lms.LMOtsSignature)
/*     */     {
/*  31 */       return (org.bouncycastle.pqc.crypto.lms.LMOtsSignature)paramObject;
/*     */     }
/*  33 */     if (paramObject instanceof DataInputStream) {
/*     */ 
/*     */ 
/*     */       
/*  37 */       LMOtsParameters lMOtsParameters = LMOtsParameters.getParametersForType(((DataInputStream)paramObject).readInt());
/*  38 */       byte[] arrayOfByte1 = new byte[lMOtsParameters.getN()];
/*     */       
/*  40 */       ((DataInputStream)paramObject).readFully(arrayOfByte1);
/*     */       
/*  42 */       byte[] arrayOfByte2 = new byte[lMOtsParameters.getP() * lMOtsParameters.getN()];
/*  43 */       ((DataInputStream)paramObject).readFully(arrayOfByte2);
/*     */ 
/*     */       
/*  46 */       return new org.bouncycastle.pqc.crypto.lms.LMOtsSignature(lMOtsParameters, arrayOfByte1, arrayOfByte2);
/*     */     } 
/*  48 */     if (paramObject instanceof byte[]) {
/*     */       
/*  50 */       DataInputStream dataInputStream = null;
/*     */       
/*     */       try {
/*  53 */         dataInputStream = new DataInputStream(new ByteArrayInputStream((byte[])paramObject));
/*  54 */         return getInstance(dataInputStream);
/*     */       }
/*     */       finally {
/*     */         
/*  58 */         if (dataInputStream != null) dataInputStream.close(); 
/*     */       } 
/*     */     } 
/*  61 */     if (paramObject instanceof InputStream)
/*     */     {
/*  63 */       return getInstance(Streams.readAll((InputStream)paramObject));
/*     */     }
/*     */     
/*  66 */     throw new IllegalArgumentException("cannot parse " + paramObject);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LMOtsParameters getType() {
/*  72 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getC() {
/*  77 */     return this.C;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getY() {
/*  82 */     return this.y;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  89 */     if (this == paramObject)
/*     */     {
/*  91 */       return true;
/*     */     }
/*  93 */     if (paramObject == null || getClass() != paramObject.getClass())
/*     */     {
/*  95 */       return false;
/*     */     }
/*     */     
/*  98 */     org.bouncycastle.pqc.crypto.lms.LMOtsSignature lMOtsSignature = (org.bouncycastle.pqc.crypto.lms.LMOtsSignature)paramObject;
/*     */     
/* 100 */     if ((this.type != null) ? !this.type.equals(lMOtsSignature.type) : (lMOtsSignature.type != null))
/*     */     {
/* 102 */       return false;
/*     */     }
/* 104 */     if (!Arrays.equals(this.C, lMOtsSignature.C))
/*     */     {
/* 106 */       return false;
/*     */     }
/* 108 */     return Arrays.equals(this.y, lMOtsSignature.y);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 114 */     int i = (this.type != null) ? this.type.hashCode() : 0;
/* 115 */     i = 31 * i + Arrays.hashCode(this.C);
/* 116 */     i = 31 * i + Arrays.hashCode(this.y);
/* 117 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getEncoded() throws IOException {
/* 123 */     return Composer.compose()
/* 124 */       .u32str(this.type.getType())
/* 125 */       .bytes(this.C)
/* 126 */       .bytes(this.y)
/* 127 */       .build();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/lms/LMOtsSignature.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */