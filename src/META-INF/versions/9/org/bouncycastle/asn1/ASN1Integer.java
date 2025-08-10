/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.asn1.ASN1OctetString;
/*     */ import org.bouncycastle.asn1.ASN1OutputStream;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1TaggedObject;
/*     */ import org.bouncycastle.asn1.StreamUtil;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ import org.bouncycastle.util.Properties;
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
/*     */ public class ASN1Integer
/*     */   extends ASN1Primitive
/*     */ {
/*     */   static final int SIGN_EXT_SIGNED = -1;
/*     */   static final int SIGN_EXT_UNSIGNED = 255;
/*     */   private final byte[] bytes;
/*     */   private final int start;
/*     */   
/*     */   public static org.bouncycastle.asn1.ASN1Integer getInstance(Object paramObject) {
/*  31 */     if (paramObject == null || paramObject instanceof org.bouncycastle.asn1.ASN1Integer)
/*     */     {
/*  33 */       return (org.bouncycastle.asn1.ASN1Integer)paramObject;
/*     */     }
/*     */     
/*  36 */     if (paramObject instanceof byte[]) {
/*     */       
/*     */       try {
/*     */         
/*  40 */         return (org.bouncycastle.asn1.ASN1Integer)fromByteArray((byte[])paramObject);
/*     */       }
/*  42 */       catch (Exception exception) {
/*     */         
/*  44 */         throw new IllegalArgumentException("encoding error in getInstance: " + exception.toString());
/*     */       } 
/*     */     }
/*     */     
/*  48 */     throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static org.bouncycastle.asn1.ASN1Integer getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
/*  65 */     ASN1Primitive aSN1Primitive = paramASN1TaggedObject.getObject();
/*     */     
/*  67 */     if (paramBoolean || aSN1Primitive instanceof org.bouncycastle.asn1.ASN1Integer)
/*     */     {
/*  69 */       return getInstance(aSN1Primitive);
/*     */     }
/*     */ 
/*     */     
/*  73 */     return new org.bouncycastle.asn1.ASN1Integer(ASN1OctetString.getInstance(aSN1Primitive).getOctets());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ASN1Integer(long paramLong) {
/*  84 */     this.bytes = BigInteger.valueOf(paramLong).toByteArray();
/*  85 */     this.start = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ASN1Integer(BigInteger paramBigInteger) {
/*  95 */     this.bytes = paramBigInteger.toByteArray();
/*  96 */     this.start = 0;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ASN1Integer(byte[] paramArrayOfbyte) {
/* 123 */     this(paramArrayOfbyte, true);
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1Integer(byte[] paramArrayOfbyte, boolean paramBoolean) {
/* 128 */     if (isMalformed(paramArrayOfbyte))
/*     */     {
/* 130 */       throw new IllegalArgumentException("malformed integer");
/*     */     }
/*     */     
/* 133 */     this.bytes = paramBoolean ? Arrays.clone(paramArrayOfbyte) : paramArrayOfbyte;
/* 134 */     this.start = signBytesToSkip(paramArrayOfbyte);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BigInteger getPositiveValue() {
/* 145 */     return new BigInteger(1, this.bytes);
/*     */   }
/*     */ 
/*     */   
/*     */   public BigInteger getValue() {
/* 150 */     return new BigInteger(this.bytes);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasValue(BigInteger paramBigInteger) {
/* 155 */     return (null != paramBigInteger && 
/*     */       
/* 157 */       intValue(this.bytes, this.start, -1) == paramBigInteger.intValue() && 
/* 158 */       getValue().equals(paramBigInteger));
/*     */   }
/*     */ 
/*     */   
/*     */   public int intPositiveValueExact() {
/* 163 */     int i = this.bytes.length - this.start;
/* 164 */     if (i > 4 || (i == 4 && 0 != (this.bytes[this.start] & 0x80)))
/*     */     {
/* 166 */       throw new ArithmeticException("ASN.1 Integer out of positive int range");
/*     */     }
/*     */     
/* 169 */     return intValue(this.bytes, this.start, 255);
/*     */   }
/*     */ 
/*     */   
/*     */   public int intValueExact() {
/* 174 */     int i = this.bytes.length - this.start;
/* 175 */     if (i > 4)
/*     */     {
/* 177 */       throw new ArithmeticException("ASN.1 Integer out of int range");
/*     */     }
/*     */     
/* 180 */     return intValue(this.bytes, this.start, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   public long longValueExact() {
/* 185 */     int i = this.bytes.length - this.start;
/* 186 */     if (i > 8)
/*     */     {
/* 188 */       throw new ArithmeticException("ASN.1 Integer out of long range");
/*     */     }
/*     */     
/* 191 */     return longValue(this.bytes, this.start, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isConstructed() {
/* 196 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   int encodedLength() {
/* 201 */     return 1 + StreamUtil.calculateBodyLength(this.bytes.length) + this.bytes.length;
/*     */   }
/*     */ 
/*     */   
/*     */   void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/* 206 */     paramASN1OutputStream.writeEncoded(paramBoolean, 2, this.bytes);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 211 */     return Arrays.hashCode(this.bytes);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean asn1Equals(ASN1Primitive paramASN1Primitive) {
/* 216 */     if (!(paramASN1Primitive instanceof org.bouncycastle.asn1.ASN1Integer))
/*     */     {
/* 218 */       return false;
/*     */     }
/*     */     
/* 221 */     org.bouncycastle.asn1.ASN1Integer aSN1Integer = (org.bouncycastle.asn1.ASN1Integer)paramASN1Primitive;
/*     */     
/* 223 */     return Arrays.areEqual(this.bytes, aSN1Integer.bytes);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 228 */     return getValue().toString();
/*     */   }
/*     */ 
/*     */   
/*     */   static int intValue(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/* 233 */     int i = paramArrayOfbyte.length;
/* 234 */     int j = Math.max(paramInt1, i - 4);
/*     */     
/* 236 */     int k = paramArrayOfbyte[j] & paramInt2;
/* 237 */     while (++j < i)
/*     */     {
/* 239 */       k = k << 8 | paramArrayOfbyte[j] & 0xFF;
/*     */     }
/* 241 */     return k;
/*     */   }
/*     */ 
/*     */   
/*     */   static long longValue(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/* 246 */     int i = paramArrayOfbyte.length;
/* 247 */     int j = Math.max(paramInt1, i - 8);
/*     */     
/* 249 */     long l = (paramArrayOfbyte[j] & paramInt2);
/* 250 */     while (++j < i)
/*     */     {
/* 252 */       l = l << 8L | (paramArrayOfbyte[j] & 0xFF);
/*     */     }
/* 254 */     return l;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isMalformed(byte[] paramArrayOfbyte) {
/* 265 */     switch (paramArrayOfbyte.length) {
/*     */       
/*     */       case 0:
/* 268 */         return true;
/*     */       case 1:
/* 270 */         return false;
/*     */     } 
/* 272 */     return (paramArrayOfbyte[0] == paramArrayOfbyte[1] >> 7 && 
/*     */       
/* 274 */       !Properties.isOverrideSet("org.bouncycastle.asn1.allow_unsafe_integer"));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static int signBytesToSkip(byte[] paramArrayOfbyte) {
/* 280 */     byte b = 0; int i = paramArrayOfbyte.length - 1;
/* 281 */     while (b < i && paramArrayOfbyte[b] == paramArrayOfbyte[b + 1] >> 7)
/*     */     {
/*     */       
/* 284 */       b++;
/*     */     }
/* 286 */     return b;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/ASN1Integer.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */