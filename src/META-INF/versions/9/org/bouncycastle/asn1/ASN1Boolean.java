/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.bouncycastle.asn1.ASN1OctetString;
/*     */ import org.bouncycastle.asn1.ASN1OutputStream;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1TaggedObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ASN1Boolean
/*     */   extends ASN1Primitive
/*     */ {
/*     */   private static final byte FALSE_VALUE = 0;
/*     */   private static final byte TRUE_VALUE = -1;
/*  22 */   public static final org.bouncycastle.asn1.ASN1Boolean FALSE = new org.bouncycastle.asn1.ASN1Boolean((byte)0);
/*  23 */   public static final org.bouncycastle.asn1.ASN1Boolean TRUE = new org.bouncycastle.asn1.ASN1Boolean((byte)-1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final byte value;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static org.bouncycastle.asn1.ASN1Boolean getInstance(Object paramObject) {
/*  37 */     if (paramObject == null || paramObject instanceof org.bouncycastle.asn1.ASN1Boolean)
/*     */     {
/*  39 */       return (org.bouncycastle.asn1.ASN1Boolean)paramObject;
/*     */     }
/*     */     
/*  42 */     if (paramObject instanceof byte[]) {
/*     */       
/*  44 */       byte[] arrayOfByte = (byte[])paramObject;
/*     */       
/*     */       try {
/*  47 */         return (org.bouncycastle.asn1.ASN1Boolean)fromByteArray(arrayOfByte);
/*     */       }
/*  49 */       catch (IOException iOException) {
/*     */         
/*  51 */         throw new IllegalArgumentException("failed to construct boolean from byte[]: " + iOException.getMessage());
/*     */       } 
/*     */     } 
/*     */     
/*  55 */     throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static org.bouncycastle.asn1.ASN1Boolean getInstance(boolean paramBoolean) {
/*  65 */     return paramBoolean ? TRUE : FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static org.bouncycastle.asn1.ASN1Boolean getInstance(int paramInt) {
/*  75 */     return (paramInt != 0) ? TRUE : FALSE;
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
/*     */   public static org.bouncycastle.asn1.ASN1Boolean getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
/*  90 */     ASN1Primitive aSN1Primitive = paramASN1TaggedObject.getObject();
/*     */     
/*  92 */     if (paramBoolean || aSN1Primitive instanceof org.bouncycastle.asn1.ASN1Boolean)
/*     */     {
/*  94 */       return getInstance(aSN1Primitive);
/*     */     }
/*     */ 
/*     */     
/*  98 */     return fromOctetString(ASN1OctetString.getInstance(aSN1Primitive).getOctets());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ASN1Boolean(byte paramByte) {
/* 104 */     this.value = paramByte;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTrue() {
/* 109 */     return (this.value != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isConstructed() {
/* 114 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   int encodedLength() {
/* 119 */     return 3;
/*     */   }
/*     */ 
/*     */   
/*     */   void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/* 124 */     paramASN1OutputStream.writeEncoded(paramBoolean, 1, this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean asn1Equals(ASN1Primitive paramASN1Primitive) {
/* 129 */     if (!(paramASN1Primitive instanceof org.bouncycastle.asn1.ASN1Boolean))
/*     */     {
/* 131 */       return false;
/*     */     }
/*     */     
/* 134 */     org.bouncycastle.asn1.ASN1Boolean aSN1Boolean = (org.bouncycastle.asn1.ASN1Boolean)paramASN1Primitive;
/*     */     
/* 136 */     return (isTrue() == aSN1Boolean.isTrue());
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 141 */     return isTrue() ? 1 : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1Primitive toDERObject() {
/* 146 */     return isTrue() ? TRUE : FALSE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 151 */     return isTrue() ? "TRUE" : "FALSE";
/*     */   }
/*     */ 
/*     */   
/*     */   static org.bouncycastle.asn1.ASN1Boolean fromOctetString(byte[] paramArrayOfbyte) {
/* 156 */     if (paramArrayOfbyte.length != 1)
/*     */     {
/* 158 */       throw new IllegalArgumentException("BOOLEAN value should have 1 byte in it");
/*     */     }
/*     */     
/* 161 */     byte b = paramArrayOfbyte[0];
/* 162 */     switch (b) {
/*     */       case 0:
/* 164 */         return FALSE;
/* 165 */       case -1: return TRUE;
/* 166 */     }  return new org.bouncycastle.asn1.ASN1Boolean(b);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/ASN1Boolean.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */