/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.bouncycastle.asn1.ASN1BitString;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1OctetString;
/*     */ import org.bouncycastle.asn1.ASN1OutputStream;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1TaggedObject;
/*     */ import org.bouncycastle.asn1.DLBitString;
/*     */ import org.bouncycastle.asn1.StreamUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DERBitString
/*     */   extends ASN1BitString
/*     */ {
/*     */   public static org.bouncycastle.asn1.DERBitString getInstance(Object paramObject) {
/*  21 */     if (paramObject == null || paramObject instanceof org.bouncycastle.asn1.DERBitString)
/*     */     {
/*  23 */       return (org.bouncycastle.asn1.DERBitString)paramObject;
/*     */     }
/*  25 */     if (paramObject instanceof DLBitString)
/*     */     {
/*  27 */       return new org.bouncycastle.asn1.DERBitString(((DLBitString)paramObject).data, ((DLBitString)paramObject).padBits);
/*     */     }
/*  29 */     if (paramObject instanceof byte[]) {
/*     */       
/*     */       try {
/*     */         
/*  33 */         return (org.bouncycastle.asn1.DERBitString)fromByteArray((byte[])paramObject);
/*     */       }
/*  35 */       catch (Exception exception) {
/*     */         
/*  37 */         throw new IllegalArgumentException("encoding error in getInstance: " + exception.toString());
/*     */       } 
/*     */     }
/*     */     
/*  41 */     throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
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
/*     */   public static org.bouncycastle.asn1.DERBitString getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
/*  58 */     ASN1Primitive aSN1Primitive = paramASN1TaggedObject.getObject();
/*     */     
/*  60 */     if (paramBoolean || aSN1Primitive instanceof org.bouncycastle.asn1.DERBitString)
/*     */     {
/*  62 */       return getInstance(aSN1Primitive);
/*     */     }
/*     */ 
/*     */     
/*  66 */     return fromOctetString(ASN1OctetString.getInstance(aSN1Primitive).getOctets());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected DERBitString(byte paramByte, int paramInt) {
/*  72 */     super(paramByte, paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DERBitString(byte[] paramArrayOfbyte, int paramInt) {
/*  83 */     super(paramArrayOfbyte, paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DERBitString(byte[] paramArrayOfbyte) {
/*  89 */     this(paramArrayOfbyte, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DERBitString(int paramInt) {
/*  95 */     super(getBytes(paramInt), getPadBits(paramInt));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DERBitString(ASN1Encodable paramASN1Encodable) throws IOException {
/* 102 */     super(paramASN1Encodable.toASN1Primitive().getEncoded("DER"), 0);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isConstructed() {
/* 107 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   int encodedLength() {
/* 112 */     return 1 + StreamUtil.calculateBodyLength(this.data.length + 1) + this.data.length + 1;
/*     */   }
/*     */ 
/*     */   
/*     */   void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/* 117 */     int i = this.data.length;
/* 118 */     if (0 == i || 0 == this.padBits || this.data[i - 1] == (byte)(this.data[i - 1] & 255 << this.padBits)) {
/*     */ 
/*     */ 
/*     */       
/* 122 */       paramASN1OutputStream.writeEncoded(paramBoolean, 3, (byte)this.padBits, this.data);
/*     */     }
/*     */     else {
/*     */       
/* 126 */       byte b = (byte)(this.data[i - 1] & 255 << this.padBits);
/* 127 */       paramASN1OutputStream.writeEncoded(paramBoolean, 3, (byte)this.padBits, this.data, 0, i - 1, b);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1Primitive toDERObject() {
/* 133 */     return (ASN1Primitive)this;
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1Primitive toDLObject() {
/* 138 */     return (ASN1Primitive)this;
/*     */   }
/*     */ 
/*     */   
/*     */   static org.bouncycastle.asn1.DERBitString fromOctetString(byte[] paramArrayOfbyte) {
/* 143 */     if (paramArrayOfbyte.length < 1)
/*     */     {
/* 145 */       throw new IllegalArgumentException("truncated BIT STRING detected");
/*     */     }
/*     */     
/* 148 */     byte b = paramArrayOfbyte[0];
/* 149 */     byte[] arrayOfByte = new byte[paramArrayOfbyte.length - 1];
/*     */     
/* 151 */     if (arrayOfByte.length != 0)
/*     */     {
/* 153 */       System.arraycopy(paramArrayOfbyte, 1, arrayOfByte, 0, paramArrayOfbyte.length - 1);
/*     */     }
/*     */     
/* 156 */     return new org.bouncycastle.asn1.DERBitString(arrayOfByte, b);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/DERBitString.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */