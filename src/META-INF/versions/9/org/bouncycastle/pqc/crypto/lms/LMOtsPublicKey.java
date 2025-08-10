/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.lms;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import org.bouncycastle.crypto.Digest;
/*     */ import org.bouncycastle.pqc.crypto.lms.Composer;
/*     */ import org.bouncycastle.pqc.crypto.lms.DigestUtil;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMOtsParameters;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMOtsSignature;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSContext;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSSignature;
/*     */ import org.bouncycastle.pqc.crypto.lms.LmsUtils;
/*     */ import org.bouncycastle.util.Encodable;
/*     */ import org.bouncycastle.util.io.Streams;
/*     */ 
/*     */ class LMOtsPublicKey implements Encodable {
/*     */   private final LMOtsParameters parameter;
/*     */   private final byte[] I;
/*     */   private final int q;
/*     */   private final byte[] K;
/*     */   
/*     */   public LMOtsPublicKey(LMOtsParameters paramLMOtsParameters, byte[] paramArrayOfbyte1, int paramInt, byte[] paramArrayOfbyte2) {
/*  26 */     this.parameter = paramLMOtsParameters;
/*  27 */     this.I = paramArrayOfbyte1;
/*  28 */     this.q = paramInt;
/*  29 */     this.K = paramArrayOfbyte2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static org.bouncycastle.pqc.crypto.lms.LMOtsPublicKey getInstance(Object paramObject) throws Exception {
/*  35 */     if (paramObject instanceof org.bouncycastle.pqc.crypto.lms.LMOtsPublicKey)
/*     */     {
/*  37 */       return (org.bouncycastle.pqc.crypto.lms.LMOtsPublicKey)paramObject;
/*     */     }
/*  39 */     if (paramObject instanceof DataInputStream) {
/*     */       
/*  41 */       LMOtsParameters lMOtsParameters = LMOtsParameters.getParametersForType(((DataInputStream)paramObject).readInt());
/*  42 */       byte[] arrayOfByte1 = new byte[16];
/*  43 */       ((DataInputStream)paramObject).readFully(arrayOfByte1);
/*  44 */       int i = ((DataInputStream)paramObject).readInt();
/*     */       
/*  46 */       byte[] arrayOfByte2 = new byte[lMOtsParameters.getN()];
/*  47 */       ((DataInputStream)paramObject).readFully(arrayOfByte2);
/*     */       
/*  49 */       return new org.bouncycastle.pqc.crypto.lms.LMOtsPublicKey(lMOtsParameters, arrayOfByte1, i, arrayOfByte2);
/*     */     } 
/*     */     
/*  52 */     if (paramObject instanceof byte[]) {
/*     */       
/*  54 */       DataInputStream dataInputStream = null;
/*     */       
/*     */       try {
/*  57 */         dataInputStream = new DataInputStream(new ByteArrayInputStream((byte[])paramObject));
/*  58 */         return getInstance(dataInputStream);
/*     */       }
/*     */       finally {
/*     */         
/*  62 */         if (dataInputStream != null) dataInputStream.close(); 
/*     */       } 
/*     */     } 
/*  65 */     if (paramObject instanceof InputStream)
/*     */     {
/*  67 */       return getInstance(Streams.readAll((InputStream)paramObject));
/*     */     }
/*     */     
/*  70 */     throw new IllegalArgumentException("cannot parse " + paramObject);
/*     */   }
/*     */ 
/*     */   
/*     */   public LMOtsParameters getParameter() {
/*  75 */     return this.parameter;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getI() {
/*  80 */     return this.I;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getQ() {
/*  85 */     return this.q;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getK() {
/*  90 */     return this.K;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  96 */     if (this == paramObject)
/*     */     {
/*  98 */       return true;
/*     */     }
/* 100 */     if (paramObject == null || getClass() != paramObject.getClass())
/*     */     {
/* 102 */       return false;
/*     */     }
/*     */     
/* 105 */     org.bouncycastle.pqc.crypto.lms.LMOtsPublicKey lMOtsPublicKey = (org.bouncycastle.pqc.crypto.lms.LMOtsPublicKey)paramObject;
/*     */     
/* 107 */     if (this.q != lMOtsPublicKey.q)
/*     */     {
/* 109 */       return false;
/*     */     }
/* 111 */     if ((this.parameter != null) ? !this.parameter.equals(lMOtsPublicKey.parameter) : (lMOtsPublicKey.parameter != null))
/*     */     {
/* 113 */       return false;
/*     */     }
/* 115 */     if (!Arrays.equals(this.I, lMOtsPublicKey.I))
/*     */     {
/* 117 */       return false;
/*     */     }
/* 119 */     return Arrays.equals(this.K, lMOtsPublicKey.K);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 125 */     int i = (this.parameter != null) ? this.parameter.hashCode() : 0;
/* 126 */     i = 31 * i + Arrays.hashCode(this.I);
/* 127 */     i = 31 * i + this.q;
/* 128 */     i = 31 * i + Arrays.hashCode(this.K);
/* 129 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getEncoded() throws IOException {
/* 135 */     return Composer.compose()
/* 136 */       .u32str(this.parameter.getType())
/* 137 */       .bytes(this.I)
/* 138 */       .u32str(this.q)
/* 139 */       .bytes(this.K).build();
/*     */   }
/*     */ 
/*     */   
/*     */   LMSContext createOtsContext(LMOtsSignature paramLMOtsSignature) {
/* 144 */     Digest digest = DigestUtil.getDigest(this.parameter.getDigestOID());
/*     */     
/* 146 */     LmsUtils.byteArray(this.I, digest);
/* 147 */     LmsUtils.u32str(this.q, digest);
/* 148 */     LmsUtils.u16str((short)-32383, digest);
/* 149 */     LmsUtils.byteArray(paramLMOtsSignature.getC(), digest);
/*     */     
/* 151 */     return new LMSContext(this, paramLMOtsSignature, digest);
/*     */   }
/*     */ 
/*     */   
/*     */   LMSContext createOtsContext(LMSSignature paramLMSSignature) {
/* 156 */     Digest digest = DigestUtil.getDigest(this.parameter.getDigestOID());
/*     */     
/* 158 */     LmsUtils.byteArray(this.I, digest);
/* 159 */     LmsUtils.u32str(this.q, digest);
/* 160 */     LmsUtils.u16str((short)-32383, digest);
/* 161 */     LmsUtils.byteArray(paramLMSSignature.getOtsSignature().getC(), digest);
/*     */     
/* 163 */     return new LMSContext(this, paramLMSSignature, digest);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/lms/LMOtsPublicKey.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */