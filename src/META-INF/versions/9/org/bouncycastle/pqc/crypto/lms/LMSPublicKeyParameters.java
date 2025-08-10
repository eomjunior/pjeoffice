/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.lms;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMOtsParameters;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMOtsPublicKey;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMS;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSContext;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSContextBasedVerifier;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSParameters;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSSignature;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSigParameters;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ import org.bouncycastle.util.io.Streams;
/*     */ 
/*     */ public class LMSPublicKeyParameters extends LMSKeyParameters implements LMSContextBasedVerifier {
/*     */   private final LMSigParameters parameterSet;
/*     */   
/*     */   public LMSPublicKeyParameters(LMSigParameters paramLMSigParameters, LMOtsParameters paramLMOtsParameters, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
/*  22 */     super(false);
/*     */     
/*  24 */     this.parameterSet = paramLMSigParameters;
/*  25 */     this.lmOtsType = paramLMOtsParameters;
/*  26 */     this.I = Arrays.clone(paramArrayOfbyte2);
/*  27 */     this.T1 = Arrays.clone(paramArrayOfbyte1);
/*     */   }
/*     */   private final LMOtsParameters lmOtsType; private final byte[] I;
/*     */   private final byte[] T1;
/*     */   
/*     */   public static org.bouncycastle.pqc.crypto.lms.LMSPublicKeyParameters getInstance(Object paramObject) throws IOException {
/*  33 */     if (paramObject instanceof org.bouncycastle.pqc.crypto.lms.LMSPublicKeyParameters)
/*     */     {
/*  35 */       return (org.bouncycastle.pqc.crypto.lms.LMSPublicKeyParameters)paramObject;
/*     */     }
/*  37 */     if (paramObject instanceof DataInputStream) {
/*     */       
/*  39 */       int i = ((DataInputStream)paramObject).readInt();
/*  40 */       LMSigParameters lMSigParameters = LMSigParameters.getParametersForType(i);
/*  41 */       LMOtsParameters lMOtsParameters = LMOtsParameters.getParametersForType(((DataInputStream)paramObject).readInt());
/*     */       
/*  43 */       byte[] arrayOfByte1 = new byte[16];
/*  44 */       ((DataInputStream)paramObject).readFully(arrayOfByte1);
/*     */       
/*  46 */       byte[] arrayOfByte2 = new byte[lMSigParameters.getM()];
/*  47 */       ((DataInputStream)paramObject).readFully(arrayOfByte2);
/*  48 */       return new org.bouncycastle.pqc.crypto.lms.LMSPublicKeyParameters(lMSigParameters, lMOtsParameters, arrayOfByte2, arrayOfByte1);
/*     */     } 
/*  50 */     if (paramObject instanceof byte[]) {
/*     */ 
/*     */       
/*  53 */       DataInputStream dataInputStream = null;
/*     */       
/*     */       try {
/*  56 */         dataInputStream = new DataInputStream(new ByteArrayInputStream((byte[])paramObject));
/*  57 */         return getInstance(dataInputStream);
/*     */       }
/*     */       finally {
/*     */         
/*  61 */         if (dataInputStream != null)
/*     */         {
/*  63 */           dataInputStream.close();
/*     */         }
/*     */       } 
/*     */     } 
/*  67 */     if (paramObject instanceof InputStream)
/*     */     {
/*  69 */       return getInstance(Streams.readAll((InputStream)paramObject));
/*     */     }
/*     */     
/*  72 */     throw new IllegalArgumentException("cannot parse " + paramObject);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getEncoded() throws IOException {
/*  78 */     return toByteArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public LMSigParameters getSigParameters() {
/*  83 */     return this.parameterSet;
/*     */   }
/*     */ 
/*     */   
/*     */   public LMOtsParameters getOtsParameters() {
/*  88 */     return this.lmOtsType;
/*     */   }
/*     */ 
/*     */   
/*     */   public LMSParameters getLMSParameters() {
/*  93 */     return new LMSParameters(getSigParameters(), getOtsParameters());
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getT1() {
/*  98 */     return Arrays.clone(this.T1);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean matchesT1(byte[] paramArrayOfbyte) {
/* 103 */     return Arrays.constantTimeAreEqual(this.T1, paramArrayOfbyte);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getI() {
/* 108 */     return Arrays.clone(this.I);
/*     */   }
/*     */ 
/*     */   
/*     */   byte[] refI() {
/* 113 */     return this.I;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 119 */     if (this == paramObject)
/*     */     {
/* 121 */       return true;
/*     */     }
/* 123 */     if (paramObject == null || getClass() != paramObject.getClass())
/*     */     {
/* 125 */       return false;
/*     */     }
/*     */     
/* 128 */     org.bouncycastle.pqc.crypto.lms.LMSPublicKeyParameters lMSPublicKeyParameters = (org.bouncycastle.pqc.crypto.lms.LMSPublicKeyParameters)paramObject;
/*     */     
/* 130 */     if (!this.parameterSet.equals(lMSPublicKeyParameters.parameterSet))
/*     */     {
/* 132 */       return false;
/*     */     }
/* 134 */     if (!this.lmOtsType.equals(lMSPublicKeyParameters.lmOtsType))
/*     */     {
/* 136 */       return false;
/*     */     }
/* 138 */     if (!Arrays.areEqual(this.I, lMSPublicKeyParameters.I))
/*     */     {
/* 140 */       return false;
/*     */     }
/* 142 */     return Arrays.areEqual(this.T1, lMSPublicKeyParameters.T1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 148 */     int i = this.parameterSet.hashCode();
/* 149 */     i = 31 * i + this.lmOtsType.hashCode();
/* 150 */     i = 31 * i + Arrays.hashCode(this.I);
/* 151 */     i = 31 * i + Arrays.hashCode(this.T1);
/* 152 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   byte[] toByteArray() {
/* 157 */     return Composer.compose()
/* 158 */       .u32str(this.parameterSet.getType())
/* 159 */       .u32str(this.lmOtsType.getType())
/* 160 */       .bytes(this.I)
/* 161 */       .bytes(this.T1)
/* 162 */       .build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LMSContext generateLMSContext(byte[] paramArrayOfbyte) {
/*     */     try {
/* 169 */       return generateOtsContext(LMSSignature.getInstance(paramArrayOfbyte));
/*     */     }
/* 171 */     catch (IOException iOException) {
/*     */       
/* 173 */       throw new IllegalStateException("cannot parse signature: " + iOException.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   LMSContext generateOtsContext(LMSSignature paramLMSSignature) {
/* 179 */     int i = getOtsParameters().getType();
/* 180 */     if (paramLMSSignature.getOtsSignature().getType().getType() != i)
/*     */     {
/* 182 */       throw new IllegalArgumentException("ots type from lsm signature does not match ots signature type from embedded ots signature");
/*     */     }
/*     */ 
/*     */     
/* 186 */     return (new LMOtsPublicKey(LMOtsParameters.getParametersForType(i), this.I, paramLMSSignature.getQ(), null)).createOtsContext(paramLMSSignature);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean verify(LMSContext paramLMSContext) {
/* 191 */     return LMS.verifySignature(this, paramLMSContext);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/lms/LMSPublicKeyParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */