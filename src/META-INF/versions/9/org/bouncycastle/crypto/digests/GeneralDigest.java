/*     */ package META-INF.versions.9.org.bouncycastle.crypto.digests;
/*     */ 
/*     */ import org.bouncycastle.crypto.ExtendedDigest;
/*     */ import org.bouncycastle.util.Memoable;
/*     */ import org.bouncycastle.util.Pack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class GeneralDigest
/*     */   implements ExtendedDigest, Memoable
/*     */ {
/*     */   private static final int BYTE_LENGTH = 64;
/*  16 */   private final byte[] xBuf = new byte[4];
/*     */ 
/*     */   
/*     */   private int xBufOff;
/*     */ 
/*     */   
/*     */   private long byteCount;
/*     */ 
/*     */   
/*     */   protected GeneralDigest() {
/*  26 */     this.xBufOff = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected GeneralDigest(org.bouncycastle.crypto.digests.GeneralDigest paramGeneralDigest) {
/*  36 */     copyIn(paramGeneralDigest);
/*     */   }
/*     */ 
/*     */   
/*     */   protected GeneralDigest(byte[] paramArrayOfbyte) {
/*  41 */     System.arraycopy(paramArrayOfbyte, 0, this.xBuf, 0, this.xBuf.length);
/*  42 */     this.xBufOff = Pack.bigEndianToInt(paramArrayOfbyte, 4);
/*  43 */     this.byteCount = Pack.bigEndianToLong(paramArrayOfbyte, 8);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void copyIn(org.bouncycastle.crypto.digests.GeneralDigest paramGeneralDigest) {
/*  48 */     System.arraycopy(paramGeneralDigest.xBuf, 0, this.xBuf, 0, paramGeneralDigest.xBuf.length);
/*     */     
/*  50 */     this.xBufOff = paramGeneralDigest.xBufOff;
/*  51 */     this.byteCount = paramGeneralDigest.byteCount;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(byte paramByte) {
/*  57 */     this.xBuf[this.xBufOff++] = paramByte;
/*     */     
/*  59 */     if (this.xBufOff == this.xBuf.length) {
/*     */       
/*  61 */       processWord(this.xBuf, 0);
/*  62 */       this.xBufOff = 0;
/*     */     } 
/*     */     
/*  65 */     this.byteCount++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/*  73 */     paramInt2 = Math.max(0, paramInt2);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  78 */     byte b = 0;
/*  79 */     if (this.xBufOff != 0)
/*     */     {
/*  81 */       while (b < paramInt2) {
/*     */         
/*  83 */         this.xBuf[this.xBufOff++] = paramArrayOfbyte[paramInt1 + b++];
/*  84 */         if (this.xBufOff == 4) {
/*     */           
/*  86 */           processWord(this.xBuf, 0);
/*  87 */           this.xBufOff = 0;
/*     */ 
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*  96 */     int i = (paramInt2 - b & 0xFFFFFFFC) + b;
/*  97 */     for (; b < i; b += 4)
/*     */     {
/*  99 */       processWord(paramArrayOfbyte, paramInt1 + b);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 105 */     while (b < paramInt2)
/*     */     {
/* 107 */       this.xBuf[this.xBufOff++] = paramArrayOfbyte[paramInt1 + b++];
/*     */     }
/*     */     
/* 110 */     this.byteCount += paramInt2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void finish() {
/* 115 */     long l = this.byteCount << 3L;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 120 */     update(-128);
/*     */     
/* 122 */     while (this.xBufOff != 0)
/*     */     {
/* 124 */       update((byte)0);
/*     */     }
/*     */     
/* 127 */     processLength(l);
/*     */     
/* 129 */     processBlock();
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/* 134 */     this.byteCount = 0L;
/*     */     
/* 136 */     this.xBufOff = 0;
/* 137 */     for (byte b = 0; b < this.xBuf.length; b++)
/*     */     {
/* 139 */       this.xBuf[b] = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void populateState(byte[] paramArrayOfbyte) {
/* 145 */     System.arraycopy(this.xBuf, 0, paramArrayOfbyte, 0, this.xBufOff);
/* 146 */     Pack.intToBigEndian(this.xBufOff, paramArrayOfbyte, 4);
/* 147 */     Pack.longToBigEndian(this.byteCount, paramArrayOfbyte, 8);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getByteLength() {
/* 152 */     return 64;
/*     */   }
/*     */   
/*     */   protected abstract void processWord(byte[] paramArrayOfbyte, int paramInt);
/*     */   
/*     */   protected abstract void processLength(long paramLong);
/*     */   
/*     */   protected abstract void processBlock();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/crypto/digests/GeneralDigest.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */