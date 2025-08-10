/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.lms;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.bouncycastle.pqc.crypto.lms.Composer;
/*     */ import org.bouncycastle.pqc.crypto.lms.HSSSignature;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMS;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSContext;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSContextBasedVerifier;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSPublicKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSSignature;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSSignedPubKey;
/*     */ import org.bouncycastle.util.io.Streams;
/*     */ 
/*     */ public class HSSPublicKeyParameters extends LMSKeyParameters implements LMSContextBasedVerifier {
/*     */   public HSSPublicKeyParameters(int paramInt, LMSPublicKeyParameters paramLMSPublicKeyParameters) {
/*  19 */     super(false);
/*     */     
/*  21 */     this.l = paramInt;
/*  22 */     this.lmsPublicKey = paramLMSPublicKeyParameters;
/*     */   }
/*     */   private final int l;
/*     */   private final LMSPublicKeyParameters lmsPublicKey;
/*     */   
/*     */   public static org.bouncycastle.pqc.crypto.lms.HSSPublicKeyParameters getInstance(Object paramObject) throws IOException {
/*  28 */     if (paramObject instanceof org.bouncycastle.pqc.crypto.lms.HSSPublicKeyParameters)
/*     */     {
/*  30 */       return (org.bouncycastle.pqc.crypto.lms.HSSPublicKeyParameters)paramObject;
/*     */     }
/*  32 */     if (paramObject instanceof DataInputStream) {
/*     */       
/*  34 */       int i = ((DataInputStream)paramObject).readInt();
/*  35 */       LMSPublicKeyParameters lMSPublicKeyParameters = LMSPublicKeyParameters.getInstance(paramObject);
/*  36 */       return new org.bouncycastle.pqc.crypto.lms.HSSPublicKeyParameters(i, lMSPublicKeyParameters);
/*     */     } 
/*  38 */     if (paramObject instanceof byte[]) {
/*     */       
/*  40 */       DataInputStream dataInputStream = null;
/*     */       
/*     */       try {
/*  43 */         dataInputStream = new DataInputStream(new ByteArrayInputStream((byte[])paramObject));
/*  44 */         return getInstance(dataInputStream);
/*     */       }
/*     */       finally {
/*     */         
/*  48 */         if (dataInputStream != null) dataInputStream.close(); 
/*     */       } 
/*     */     } 
/*  51 */     if (paramObject instanceof InputStream)
/*     */     {
/*  53 */       return getInstance(Streams.readAll((InputStream)paramObject));
/*     */     }
/*     */     
/*  56 */     throw new IllegalArgumentException("cannot parse " + paramObject);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getL() {
/*  61 */     return this.l;
/*     */   }
/*     */ 
/*     */   
/*     */   public LMSPublicKeyParameters getLMSPublicKey() {
/*  66 */     return this.lmsPublicKey;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  72 */     if (this == paramObject)
/*     */     {
/*  74 */       return true;
/*     */     }
/*  76 */     if (paramObject == null || getClass() != paramObject.getClass())
/*     */     {
/*  78 */       return false;
/*     */     }
/*     */     
/*  81 */     org.bouncycastle.pqc.crypto.lms.HSSPublicKeyParameters hSSPublicKeyParameters = (org.bouncycastle.pqc.crypto.lms.HSSPublicKeyParameters)paramObject;
/*     */     
/*  83 */     if (this.l != hSSPublicKeyParameters.l)
/*     */     {
/*  85 */       return false;
/*     */     }
/*  87 */     return this.lmsPublicKey.equals(hSSPublicKeyParameters.lmsPublicKey);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  93 */     int i = this.l;
/*  94 */     i = 31 * i + this.lmsPublicKey.hashCode();
/*  95 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getEncoded() throws IOException {
/* 101 */     return Composer.compose().u32str(this.l)
/* 102 */       .bytes(this.lmsPublicKey.getEncoded())
/* 103 */       .build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LMSContext generateLMSContext(byte[] paramArrayOfbyte) {
/*     */     HSSSignature hSSSignature;
/*     */     try {
/* 111 */       hSSSignature = HSSSignature.getInstance(paramArrayOfbyte, getL());
/*     */     }
/* 113 */     catch (IOException iOException) {
/*     */       
/* 115 */       throw new IllegalStateException("cannot parse signature: " + iOException.getMessage());
/*     */     } 
/*     */     
/* 118 */     LMSSignedPubKey[] arrayOfLMSSignedPubKey = hSSSignature.getSignedPubKey();
/* 119 */     LMSPublicKeyParameters lMSPublicKeyParameters = arrayOfLMSSignedPubKey[arrayOfLMSSignedPubKey.length - 1].getPublicKey();
/*     */     
/* 121 */     return lMSPublicKeyParameters.generateOtsContext(hSSSignature.getSignature()).withSignedPublicKeys(arrayOfLMSSignedPubKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean verify(LMSContext paramLMSContext) {
/* 126 */     boolean bool = false;
/*     */     
/* 128 */     LMSSignedPubKey[] arrayOfLMSSignedPubKey = paramLMSContext.getSignedPubKeys();
/*     */     
/* 130 */     if (arrayOfLMSSignedPubKey.length != getL() - 1)
/*     */     {
/* 132 */       return false;
/*     */     }
/*     */     
/* 135 */     LMSPublicKeyParameters lMSPublicKeyParameters = getLMSPublicKey();
/*     */     
/* 137 */     for (byte b = 0; b < arrayOfLMSSignedPubKey.length; b++) {
/*     */       
/* 139 */       LMSSignature lMSSignature = arrayOfLMSSignedPubKey[b].getSignature();
/* 140 */       byte[] arrayOfByte = arrayOfLMSSignedPubKey[b].getPublicKey().toByteArray();
/* 141 */       if (!LMS.verifySignature(lMSPublicKeyParameters, lMSSignature, arrayOfByte))
/*     */       {
/* 143 */         bool = true;
/*     */       }
/* 145 */       lMSPublicKeyParameters = arrayOfLMSSignedPubKey[b].getPublicKey();
/*     */     } 
/*     */     
/* 148 */     return (!bool) & lMSPublicKeyParameters.verify(paramLMSContext);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/lms/HSSPublicKeyParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */