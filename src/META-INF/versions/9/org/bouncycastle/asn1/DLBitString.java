/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.bouncycastle.asn1.ASN1BitString;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1OctetString;
/*     */ import org.bouncycastle.asn1.ASN1OutputStream;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1TaggedObject;
/*     */ import org.bouncycastle.asn1.StreamUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DLBitString
/*     */   extends ASN1BitString
/*     */ {
/*     */   public static ASN1BitString getInstance(Object paramObject) {
/*  21 */     if (paramObject == null || paramObject instanceof org.bouncycastle.asn1.DLBitString)
/*     */     {
/*  23 */       return (org.bouncycastle.asn1.DLBitString)paramObject;
/*     */     }
/*  25 */     if (paramObject instanceof org.bouncycastle.asn1.DERBitString)
/*     */     {
/*  27 */       return (ASN1BitString)paramObject;
/*     */     }
/*  29 */     if (paramObject instanceof byte[]) {
/*     */       
/*     */       try {
/*     */         
/*  33 */         return (ASN1BitString)fromByteArray((byte[])paramObject);
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
/*     */   public static ASN1BitString getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
/*  58 */     ASN1Primitive aSN1Primitive = paramASN1TaggedObject.getObject();
/*     */     
/*  60 */     if (paramBoolean || aSN1Primitive instanceof org.bouncycastle.asn1.DLBitString)
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
/*     */   protected DLBitString(byte paramByte, int paramInt) {
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
/*     */   public DLBitString(byte[] paramArrayOfbyte, int paramInt) {
/*  83 */     super(paramArrayOfbyte, paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DLBitString(byte[] paramArrayOfbyte) {
/*  89 */     this(paramArrayOfbyte, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DLBitString(int paramInt) {
/*  95 */     super(getBytes(paramInt), getPadBits(paramInt));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DLBitString(ASN1Encodable paramASN1Encodable) throws IOException {
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
/* 117 */     paramASN1OutputStream.writeEncoded(paramBoolean, 3, (byte)this.padBits, this.data);
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1Primitive toDLObject() {
/* 122 */     return (ASN1Primitive)this;
/*     */   }
/*     */ 
/*     */   
/*     */   static org.bouncycastle.asn1.DLBitString fromOctetString(byte[] paramArrayOfbyte) {
/* 127 */     if (paramArrayOfbyte.length < 1)
/*     */     {
/* 129 */       throw new IllegalArgumentException("truncated BIT STRING detected");
/*     */     }
/*     */     
/* 132 */     byte b = paramArrayOfbyte[0];
/* 133 */     byte[] arrayOfByte = new byte[paramArrayOfbyte.length - 1];
/*     */     
/* 135 */     if (arrayOfByte.length != 0)
/*     */     {
/* 137 */       System.arraycopy(paramArrayOfbyte, 1, arrayOfByte, 0, paramArrayOfbyte.length - 1);
/*     */     }
/*     */     
/* 140 */     return new org.bouncycastle.asn1.DLBitString(arrayOfByte, b);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/DLBitString.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */