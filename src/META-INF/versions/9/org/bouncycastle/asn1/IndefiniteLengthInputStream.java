/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.bouncycastle.asn1.LimitedInputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class IndefiniteLengthInputStream
/*     */   extends LimitedInputStream
/*     */ {
/*     */   private int _b1;
/*     */   private int _b2;
/*     */   private boolean _eofReached = false;
/*     */   private boolean _eofOn00 = true;
/*     */   
/*     */   IndefiniteLengthInputStream(InputStream paramInputStream, int paramInt) throws IOException {
/*  20 */     super(paramInputStream, paramInt);
/*     */     
/*  22 */     this._b1 = paramInputStream.read();
/*  23 */     this._b2 = paramInputStream.read();
/*     */     
/*  25 */     if (this._b2 < 0)
/*     */     {
/*     */       
/*  28 */       throw new EOFException();
/*     */     }
/*     */     
/*  31 */     checkForEof();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void setEofOn00(boolean paramBoolean) {
/*  37 */     this._eofOn00 = paramBoolean;
/*  38 */     checkForEof();
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean checkForEof() {
/*  43 */     if (!this._eofReached && this._eofOn00 && this._b1 == 0 && this._b2 == 0) {
/*     */       
/*  45 */       this._eofReached = true;
/*  46 */       setParentEofDetect(true);
/*     */     } 
/*  48 */     return this._eofReached;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
/*  55 */     if (this._eofOn00 || paramInt2 < 3)
/*     */     {
/*  57 */       return super.read(paramArrayOfbyte, paramInt1, paramInt2);
/*     */     }
/*     */     
/*  60 */     if (this._eofReached)
/*     */     {
/*  62 */       return -1;
/*     */     }
/*     */     
/*  65 */     int i = this._in.read(paramArrayOfbyte, paramInt1 + 2, paramInt2 - 2);
/*     */     
/*  67 */     if (i < 0)
/*     */     {
/*     */       
/*  70 */       throw new EOFException();
/*     */     }
/*     */     
/*  73 */     paramArrayOfbyte[paramInt1] = (byte)this._b1;
/*  74 */     paramArrayOfbyte[paramInt1 + 1] = (byte)this._b2;
/*     */     
/*  76 */     this._b1 = this._in.read();
/*  77 */     this._b2 = this._in.read();
/*     */     
/*  79 */     if (this._b2 < 0)
/*     */     {
/*     */       
/*  82 */       throw new EOFException();
/*     */     }
/*     */     
/*  85 */     return i + 2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*  91 */     if (checkForEof())
/*     */     {
/*  93 */       return -1;
/*     */     }
/*     */     
/*  96 */     int i = this._in.read();
/*     */     
/*  98 */     if (i < 0)
/*     */     {
/*     */       
/* 101 */       throw new EOFException();
/*     */     }
/*     */     
/* 104 */     int j = this._b1;
/*     */     
/* 106 */     this._b1 = this._b2;
/* 107 */     this._b2 = i;
/*     */     
/* 109 */     return j;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/IndefiniteLengthInputStream.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */