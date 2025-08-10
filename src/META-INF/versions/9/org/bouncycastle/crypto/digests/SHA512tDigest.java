/*     */ package META-INF.versions.9.org.bouncycastle.crypto.digests;
/*     */ 
/*     */ import org.bouncycastle.crypto.digests.LongDigest;
/*     */ import org.bouncycastle.util.Memoable;
/*     */ import org.bouncycastle.util.MemoableResetException;
/*     */ import org.bouncycastle.util.Pack;
/*     */ 
/*     */ public class SHA512tDigest
/*     */   extends LongDigest
/*     */ {
/*     */   private int digestLength;
/*     */   private long H1t;
/*     */   private long H2t;
/*     */   private long H3t;
/*     */   private long H4t;
/*     */   private long H5t;
/*     */   private long H6t;
/*     */   private long H7t;
/*     */   private long H8t;
/*     */   
/*     */   public SHA512tDigest(int paramInt) {
/*  22 */     if (paramInt >= 512)
/*     */     {
/*  24 */       throw new IllegalArgumentException("bitLength cannot be >= 512");
/*     */     }
/*     */     
/*  27 */     if (paramInt % 8 != 0)
/*     */     {
/*  29 */       throw new IllegalArgumentException("bitLength needs to be a multiple of 8");
/*     */     }
/*     */     
/*  32 */     if (paramInt == 384)
/*     */     {
/*  34 */       throw new IllegalArgumentException("bitLength cannot be 384 use SHA384 instead");
/*     */     }
/*     */     
/*  37 */     this.digestLength = paramInt / 8;
/*     */     
/*  39 */     tIvGenerate(this.digestLength * 8);
/*     */     
/*  41 */     reset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SHA512tDigest(org.bouncycastle.crypto.digests.SHA512tDigest paramSHA512tDigest) {
/*  50 */     super(paramSHA512tDigest);
/*     */     
/*  52 */     this.digestLength = paramSHA512tDigest.digestLength;
/*     */     
/*  54 */     reset((Memoable)paramSHA512tDigest);
/*     */   }
/*     */ 
/*     */   
/*     */   public SHA512tDigest(byte[] paramArrayOfbyte) {
/*  59 */     this(readDigestLength(paramArrayOfbyte));
/*  60 */     restoreState(paramArrayOfbyte);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int readDigestLength(byte[] paramArrayOfbyte) {
/*  65 */     return Pack.bigEndianToInt(paramArrayOfbyte, paramArrayOfbyte.length - 4);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAlgorithmName() {
/*  70 */     return "SHA-512/" + Integer.toString(this.digestLength * 8);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDigestSize() {
/*  75 */     return this.digestLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int doFinal(byte[] paramArrayOfbyte, int paramInt) {
/*  82 */     finish();
/*     */     
/*  84 */     longToBigEndian(this.H1, paramArrayOfbyte, paramInt, this.digestLength);
/*  85 */     longToBigEndian(this.H2, paramArrayOfbyte, paramInt + 8, this.digestLength - 8);
/*  86 */     longToBigEndian(this.H3, paramArrayOfbyte, paramInt + 16, this.digestLength - 16);
/*  87 */     longToBigEndian(this.H4, paramArrayOfbyte, paramInt + 24, this.digestLength - 24);
/*  88 */     longToBigEndian(this.H5, paramArrayOfbyte, paramInt + 32, this.digestLength - 32);
/*  89 */     longToBigEndian(this.H6, paramArrayOfbyte, paramInt + 40, this.digestLength - 40);
/*  90 */     longToBigEndian(this.H7, paramArrayOfbyte, paramInt + 48, this.digestLength - 48);
/*  91 */     longToBigEndian(this.H8, paramArrayOfbyte, paramInt + 56, this.digestLength - 56);
/*     */     
/*  93 */     reset();
/*     */     
/*  95 */     return this.digestLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 103 */     super.reset();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 108 */     this.H1 = this.H1t;
/* 109 */     this.H2 = this.H2t;
/* 110 */     this.H3 = this.H3t;
/* 111 */     this.H4 = this.H4t;
/* 112 */     this.H5 = this.H5t;
/* 113 */     this.H6 = this.H6t;
/* 114 */     this.H7 = this.H7t;
/* 115 */     this.H8 = this.H8t;
/*     */   }
/*     */ 
/*     */   
/*     */   private void tIvGenerate(int paramInt) {
/* 120 */     this.H1 = -3482333909917012819L;
/* 121 */     this.H2 = 2216346199247487646L;
/* 122 */     this.H3 = -7364697282686394994L;
/* 123 */     this.H4 = 65953792586715988L;
/* 124 */     this.H5 = -816286391624063116L;
/* 125 */     this.H6 = 4512832404995164602L;
/* 126 */     this.H7 = -5033199132376557362L;
/* 127 */     this.H8 = -124578254951840548L;
/*     */     
/* 129 */     update((byte)83);
/* 130 */     update((byte)72);
/* 131 */     update((byte)65);
/* 132 */     update((byte)45);
/* 133 */     update((byte)53);
/* 134 */     update((byte)49);
/* 135 */     update((byte)50);
/* 136 */     update((byte)47);
/*     */     
/* 138 */     if (paramInt > 100) {
/*     */       
/* 140 */       update((byte)(paramInt / 100 + 48));
/* 141 */       paramInt %= 100;
/* 142 */       update((byte)(paramInt / 10 + 48));
/* 143 */       paramInt %= 10;
/* 144 */       update((byte)(paramInt + 48));
/*     */     }
/* 146 */     else if (paramInt > 10) {
/*     */       
/* 148 */       update((byte)(paramInt / 10 + 48));
/* 149 */       paramInt %= 10;
/* 150 */       update((byte)(paramInt + 48));
/*     */     }
/*     */     else {
/*     */       
/* 154 */       update((byte)(paramInt + 48));
/*     */     } 
/*     */     
/* 157 */     finish();
/*     */     
/* 159 */     this.H1t = this.H1;
/* 160 */     this.H2t = this.H2;
/* 161 */     this.H3t = this.H3;
/* 162 */     this.H4t = this.H4;
/* 163 */     this.H5t = this.H5;
/* 164 */     this.H6t = this.H6;
/* 165 */     this.H7t = this.H7;
/* 166 */     this.H8t = this.H8;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void longToBigEndian(long paramLong, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/* 171 */     if (paramInt2 > 0) {
/*     */       
/* 173 */       intToBigEndian((int)(paramLong >>> 32L), paramArrayOfbyte, paramInt1, paramInt2);
/*     */       
/* 175 */       if (paramInt2 > 4)
/*     */       {
/* 177 */         intToBigEndian((int)(paramLong & 0xFFFFFFFFL), paramArrayOfbyte, paramInt1 + 4, paramInt2 - 4);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void intToBigEndian(int paramInt1, byte[] paramArrayOfbyte, int paramInt2, int paramInt3) {
/* 184 */     int i = Math.min(4, paramInt3);
/* 185 */     while (--i >= 0) {
/*     */       
/* 187 */       int j = 8 * (3 - i);
/* 188 */       paramArrayOfbyte[paramInt2 + i] = (byte)(paramInt1 >>> j);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Memoable copy() {
/* 194 */     return (Memoable)new org.bouncycastle.crypto.digests.SHA512tDigest(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset(Memoable paramMemoable) {
/* 199 */     org.bouncycastle.crypto.digests.SHA512tDigest sHA512tDigest = (org.bouncycastle.crypto.digests.SHA512tDigest)paramMemoable;
/*     */     
/* 201 */     if (this.digestLength != sHA512tDigest.digestLength)
/*     */     {
/* 203 */       throw new MemoableResetException("digestLength inappropriate in other");
/*     */     }
/*     */     
/* 206 */     copyIn(sHA512tDigest);
/*     */     
/* 208 */     this.H1t = sHA512tDigest.H1t;
/* 209 */     this.H2t = sHA512tDigest.H2t;
/* 210 */     this.H3t = sHA512tDigest.H3t;
/* 211 */     this.H4t = sHA512tDigest.H4t;
/* 212 */     this.H5t = sHA512tDigest.H5t;
/* 213 */     this.H6t = sHA512tDigest.H6t;
/* 214 */     this.H7t = sHA512tDigest.H7t;
/* 215 */     this.H8t = sHA512tDigest.H8t;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getEncodedState() {
/* 220 */     int i = getEncodedStateSize();
/* 221 */     byte[] arrayOfByte = new byte[i + 4];
/* 222 */     populateState(arrayOfByte);
/* 223 */     Pack.intToBigEndian(this.digestLength * 8, arrayOfByte, i);
/* 224 */     return arrayOfByte;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/crypto/digests/SHA512tDigest.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */