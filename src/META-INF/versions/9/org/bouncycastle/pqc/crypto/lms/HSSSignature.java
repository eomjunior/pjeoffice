/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.lms;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import org.bouncycastle.pqc.crypto.lms.Composer;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSPublicKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSSignature;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSSignedPubKey;
/*     */ import org.bouncycastle.util.Encodable;
/*     */ import org.bouncycastle.util.io.Streams;
/*     */ 
/*     */ public class HSSSignature implements Encodable {
/*     */   private final int lMinus1;
/*     */   private final LMSSignedPubKey[] signedPubKey;
/*     */   private final LMSSignature signature;
/*     */   
/*     */   public HSSSignature(int paramInt, LMSSignedPubKey[] paramArrayOfLMSSignedPubKey, LMSSignature paramLMSSignature) {
/*  21 */     this.lMinus1 = paramInt;
/*  22 */     this.signedPubKey = paramArrayOfLMSSignedPubKey;
/*  23 */     this.signature = paramLMSSignature;
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
/*     */   public static org.bouncycastle.pqc.crypto.lms.HSSSignature getInstance(Object paramObject, int paramInt) throws IOException {
/*  36 */     if (paramObject instanceof org.bouncycastle.pqc.crypto.lms.HSSSignature)
/*     */     {
/*  38 */       return (org.bouncycastle.pqc.crypto.lms.HSSSignature)paramObject;
/*     */     }
/*  40 */     if (paramObject instanceof DataInputStream) {
/*     */ 
/*     */       
/*  43 */       int i = ((DataInputStream)paramObject).readInt();
/*  44 */       if (i != paramInt - 1)
/*     */       {
/*  46 */         throw new IllegalStateException("nspk exceeded maxNspk");
/*     */       }
/*  48 */       LMSSignedPubKey[] arrayOfLMSSignedPubKey = new LMSSignedPubKey[i];
/*  49 */       if (i != 0)
/*     */       {
/*  51 */         for (byte b = 0; b < arrayOfLMSSignedPubKey.length; b++)
/*     */         {
/*  53 */           arrayOfLMSSignedPubKey[b] = new LMSSignedPubKey(LMSSignature.getInstance(paramObject), LMSPublicKeyParameters.getInstance(paramObject));
/*     */         }
/*     */       }
/*  56 */       LMSSignature lMSSignature = LMSSignature.getInstance(paramObject);
/*     */       
/*  58 */       return new org.bouncycastle.pqc.crypto.lms.HSSSignature(i, arrayOfLMSSignedPubKey, lMSSignature);
/*     */     } 
/*  60 */     if (paramObject instanceof byte[]) {
/*     */       
/*  62 */       DataInputStream dataInputStream = null;
/*     */       
/*     */       try {
/*  65 */         dataInputStream = new DataInputStream(new ByteArrayInputStream((byte[])paramObject));
/*  66 */         return getInstance(dataInputStream, paramInt);
/*     */       }
/*     */       finally {
/*     */         
/*  70 */         if (dataInputStream != null) dataInputStream.close(); 
/*     */       } 
/*     */     } 
/*  73 */     if (paramObject instanceof InputStream)
/*     */     {
/*  75 */       return getInstance(Streams.readAll((InputStream)paramObject), paramInt);
/*     */     }
/*     */     
/*  78 */     throw new IllegalArgumentException("cannot parse " + paramObject);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getlMinus1() {
/*  84 */     return this.lMinus1;
/*     */   }
/*     */ 
/*     */   
/*     */   public LMSSignedPubKey[] getSignedPubKey() {
/*  89 */     return this.signedPubKey;
/*     */   }
/*     */ 
/*     */   
/*     */   public LMSSignature getSignature() {
/*  94 */     return this.signature;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 100 */     if (this == paramObject)
/*     */     {
/* 102 */       return true;
/*     */     }
/* 104 */     if (paramObject == null || getClass() != paramObject.getClass())
/*     */     {
/* 106 */       return false;
/*     */     }
/*     */     
/* 109 */     org.bouncycastle.pqc.crypto.lms.HSSSignature hSSSignature = (org.bouncycastle.pqc.crypto.lms.HSSSignature)paramObject;
/*     */     
/* 111 */     if (this.lMinus1 != hSSSignature.lMinus1)
/*     */     {
/* 113 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 117 */     if (this.signedPubKey.length != hSSSignature.signedPubKey.length)
/*     */     {
/* 119 */       return false;
/*     */     }
/*     */     
/* 122 */     for (byte b = 0; b < this.signedPubKey.length; b++) {
/*     */       
/* 124 */       if (!this.signedPubKey[b].equals(hSSSignature.signedPubKey[b]))
/*     */       {
/* 126 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 130 */     return (this.signature != null) ? this.signature.equals(hSSSignature.signature) : ((hSSSignature.signature == null));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 136 */     int i = this.lMinus1;
/* 137 */     i = 31 * i + Arrays.hashCode((Object[])this.signedPubKey);
/* 138 */     i = 31 * i + ((this.signature != null) ? this.signature.hashCode() : 0);
/* 139 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getEncoded() throws IOException {
/* 145 */     Composer composer = Composer.compose();
/* 146 */     composer.u32str(this.lMinus1);
/* 147 */     if (this.signedPubKey != null)
/*     */     {
/* 149 */       for (LMSSignedPubKey lMSSignedPubKey : this.signedPubKey)
/*     */       {
/* 151 */         composer.bytes((Encodable)lMSSignedPubKey);
/*     */       }
/*     */     }
/* 154 */     composer.bytes((Encodable)this.signature);
/* 155 */     return composer.build();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/lms/HSSSignature.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */