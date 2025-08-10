/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.asn1.ASN1Integer;
/*     */ import org.bouncycastle.asn1.ASN1OctetString;
/*     */ import org.bouncycastle.asn1.ASN1OutputStream;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1TaggedObject;
/*     */ import org.bouncycastle.asn1.StreamUtil;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ASN1Enumerated
/*     */   extends ASN1Primitive
/*     */ {
/*     */   private final byte[] bytes;
/*     */   private final int start;
/*     */   
/*     */   public static org.bouncycastle.asn1.ASN1Enumerated getInstance(Object paramObject) {
/*  27 */     if (paramObject == null || paramObject instanceof org.bouncycastle.asn1.ASN1Enumerated)
/*     */     {
/*  29 */       return (org.bouncycastle.asn1.ASN1Enumerated)paramObject;
/*     */     }
/*     */     
/*  32 */     if (paramObject instanceof byte[]) {
/*     */       
/*     */       try {
/*     */         
/*  36 */         return (org.bouncycastle.asn1.ASN1Enumerated)fromByteArray((byte[])paramObject);
/*     */       }
/*  38 */       catch (Exception exception) {
/*     */         
/*  40 */         throw new IllegalArgumentException("encoding error in getInstance: " + exception.toString());
/*     */       } 
/*     */     }
/*     */     
/*  44 */     throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
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
/*     */   public static org.bouncycastle.asn1.ASN1Enumerated getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
/*  61 */     ASN1Primitive aSN1Primitive = paramASN1TaggedObject.getObject();
/*     */     
/*  63 */     if (paramBoolean || aSN1Primitive instanceof org.bouncycastle.asn1.ASN1Enumerated)
/*     */     {
/*  65 */       return getInstance(aSN1Primitive);
/*     */     }
/*     */ 
/*     */     
/*  69 */     return fromOctetString(ASN1OctetString.getInstance(aSN1Primitive).getOctets());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ASN1Enumerated(int paramInt) {
/*  80 */     if (paramInt < 0)
/*     */     {
/*  82 */       throw new IllegalArgumentException("enumerated must be non-negative");
/*     */     }
/*     */     
/*  85 */     this.bytes = BigInteger.valueOf(paramInt).toByteArray();
/*  86 */     this.start = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ASN1Enumerated(BigInteger paramBigInteger) {
/*  96 */     if (paramBigInteger.signum() < 0)
/*     */     {
/*  98 */       throw new IllegalArgumentException("enumerated must be non-negative");
/*     */     }
/*     */     
/* 101 */     this.bytes = paramBigInteger.toByteArray();
/* 102 */     this.start = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ASN1Enumerated(byte[] paramArrayOfbyte) {
/* 112 */     if (ASN1Integer.isMalformed(paramArrayOfbyte))
/*     */     {
/* 114 */       throw new IllegalArgumentException("malformed enumerated");
/*     */     }
/* 116 */     if (0 != (paramArrayOfbyte[0] & 0x80))
/*     */     {
/* 118 */       throw new IllegalArgumentException("enumerated must be non-negative");
/*     */     }
/*     */     
/* 121 */     this.bytes = Arrays.clone(paramArrayOfbyte);
/* 122 */     this.start = ASN1Integer.signBytesToSkip(paramArrayOfbyte);
/*     */   }
/*     */ 
/*     */   
/*     */   public BigInteger getValue() {
/* 127 */     return new BigInteger(this.bytes);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasValue(BigInteger paramBigInteger) {
/* 132 */     return (null != paramBigInteger && 
/*     */       
/* 134 */       ASN1Integer.intValue(this.bytes, this.start, -1) == paramBigInteger.intValue() && 
/* 135 */       getValue().equals(paramBigInteger));
/*     */   }
/*     */ 
/*     */   
/*     */   public int intValueExact() {
/* 140 */     int i = this.bytes.length - this.start;
/* 141 */     if (i > 4)
/*     */     {
/* 143 */       throw new ArithmeticException("ASN.1 Enumerated out of int range");
/*     */     }
/*     */     
/* 146 */     return ASN1Integer.intValue(this.bytes, this.start, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isConstructed() {
/* 151 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   int encodedLength() {
/* 156 */     return 1 + StreamUtil.calculateBodyLength(this.bytes.length) + this.bytes.length;
/*     */   }
/*     */ 
/*     */   
/*     */   void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/* 161 */     paramASN1OutputStream.writeEncoded(paramBoolean, 10, this.bytes);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean asn1Equals(ASN1Primitive paramASN1Primitive) {
/* 167 */     if (!(paramASN1Primitive instanceof org.bouncycastle.asn1.ASN1Enumerated))
/*     */     {
/* 169 */       return false;
/*     */     }
/*     */     
/* 172 */     org.bouncycastle.asn1.ASN1Enumerated aSN1Enumerated = (org.bouncycastle.asn1.ASN1Enumerated)paramASN1Primitive;
/*     */     
/* 174 */     return Arrays.areEqual(this.bytes, aSN1Enumerated.bytes);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 179 */     return Arrays.hashCode(this.bytes);
/*     */   }
/*     */   
/* 182 */   private static org.bouncycastle.asn1.ASN1Enumerated[] cache = new org.bouncycastle.asn1.ASN1Enumerated[12];
/*     */ 
/*     */   
/*     */   static org.bouncycastle.asn1.ASN1Enumerated fromOctetString(byte[] paramArrayOfbyte) {
/* 186 */     if (paramArrayOfbyte.length > 1)
/*     */     {
/* 188 */       return new org.bouncycastle.asn1.ASN1Enumerated(paramArrayOfbyte);
/*     */     }
/*     */     
/* 191 */     if (paramArrayOfbyte.length == 0)
/*     */     {
/* 193 */       throw new IllegalArgumentException("ENUMERATED has zero length");
/*     */     }
/* 195 */     int i = paramArrayOfbyte[0] & 0xFF;
/*     */     
/* 197 */     if (i >= cache.length)
/*     */     {
/* 199 */       return new org.bouncycastle.asn1.ASN1Enumerated(paramArrayOfbyte);
/*     */     }
/*     */     
/* 202 */     org.bouncycastle.asn1.ASN1Enumerated aSN1Enumerated = cache[i];
/*     */     
/* 204 */     if (aSN1Enumerated == null)
/*     */     {
/* 206 */       aSN1Enumerated = cache[i] = new org.bouncycastle.asn1.ASN1Enumerated(paramArrayOfbyte);
/*     */     }
/*     */     
/* 209 */     return aSN1Enumerated;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/ASN1Enumerated.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */