/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.bouncycastle.asn1.LimitedInputStream;
/*     */ import org.bouncycastle.util.io.Streams;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class DefiniteLengthInputStream
/*     */   extends LimitedInputStream
/*     */ {
/*  15 */   private static final byte[] EMPTY_BYTES = new byte[0];
/*     */ 
/*     */   
/*     */   private final int _originalLength;
/*     */ 
/*     */   
/*     */   private int _remaining;
/*     */ 
/*     */ 
/*     */   
/*     */   DefiniteLengthInputStream(InputStream paramInputStream, int paramInt1, int paramInt2) {
/*  26 */     super(paramInputStream, paramInt2);
/*     */     
/*  28 */     if (paramInt1 < 0)
/*     */     {
/*  30 */       throw new IllegalArgumentException("negative lengths not allowed");
/*     */     }
/*     */     
/*  33 */     this._originalLength = paramInt1;
/*  34 */     this._remaining = paramInt1;
/*     */     
/*  36 */     if (paramInt1 == 0)
/*     */     {
/*  38 */       setParentEofDetect(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   int getRemaining() {
/*  44 */     return this._remaining;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*  50 */     if (this._remaining == 0)
/*     */     {
/*  52 */       return -1;
/*     */     }
/*     */     
/*  55 */     int i = this._in.read();
/*     */     
/*  57 */     if (i < 0)
/*     */     {
/*  59 */       throw new EOFException("DEF length " + this._originalLength + " object truncated by " + this._remaining);
/*     */     }
/*     */     
/*  62 */     if (--this._remaining == 0)
/*     */     {
/*  64 */       setParentEofDetect(true);
/*     */     }
/*     */     
/*  67 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
/*  73 */     if (this._remaining == 0)
/*     */     {
/*  75 */       return -1;
/*     */     }
/*     */     
/*  78 */     int i = Math.min(paramInt2, this._remaining);
/*  79 */     int j = this._in.read(paramArrayOfbyte, paramInt1, i);
/*     */     
/*  81 */     if (j < 0)
/*     */     {
/*  83 */       throw new EOFException("DEF length " + this._originalLength + " object truncated by " + this._remaining);
/*     */     }
/*     */     
/*  86 */     if ((this._remaining -= j) == 0)
/*     */     {
/*  88 */       setParentEofDetect(true);
/*     */     }
/*     */     
/*  91 */     return j;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void readAllIntoByteArray(byte[] paramArrayOfbyte) throws IOException {
/*  97 */     if (this._remaining != paramArrayOfbyte.length)
/*     */     {
/*  99 */       throw new IllegalArgumentException("buffer length not right for data");
/*     */     }
/*     */     
/* 102 */     if (this._remaining == 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 108 */     int i = getLimit();
/* 109 */     if (this._remaining >= i)
/*     */     {
/* 111 */       throw new IOException("corrupted stream - out of bounds length found: " + this._remaining + " >= " + i);
/*     */     }
/*     */     
/* 114 */     if ((this._remaining -= Streams.readFully(this._in, paramArrayOfbyte)) != 0)
/*     */     {
/* 116 */       throw new EOFException("DEF length " + this._originalLength + " object truncated by " + this._remaining);
/*     */     }
/* 118 */     setParentEofDetect(true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   byte[] toByteArray() throws IOException {
/* 124 */     if (this._remaining == 0)
/*     */     {
/* 126 */       return EMPTY_BYTES;
/*     */     }
/*     */ 
/*     */     
/* 130 */     int i = getLimit();
/* 131 */     if (this._remaining >= i)
/*     */     {
/* 133 */       throw new IOException("corrupted stream - out of bounds length found: " + this._remaining + " >= " + i);
/*     */     }
/*     */     
/* 136 */     byte[] arrayOfByte = new byte[this._remaining];
/* 137 */     if ((this._remaining -= Streams.readFully(this._in, arrayOfByte)) != 0)
/*     */     {
/* 139 */       throw new EOFException("DEF length " + this._originalLength + " object truncated by " + this._remaining);
/*     */     }
/* 141 */     setParentEofDetect(true);
/* 142 */     return arrayOfByte;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/DefiniteLengthInputStream.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */