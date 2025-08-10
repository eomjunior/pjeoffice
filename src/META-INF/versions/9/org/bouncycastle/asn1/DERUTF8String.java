/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.bouncycastle.asn1.ASN1OctetString;
/*     */ import org.bouncycastle.asn1.ASN1OutputStream;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1String;
/*     */ import org.bouncycastle.asn1.ASN1TaggedObject;
/*     */ import org.bouncycastle.asn1.StreamUtil;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ import org.bouncycastle.util.Strings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DERUTF8String
/*     */   extends ASN1Primitive
/*     */   implements ASN1String
/*     */ {
/*     */   private final byte[] string;
/*     */   
/*     */   public static org.bouncycastle.asn1.DERUTF8String getInstance(Object paramObject) {
/*  27 */     if (paramObject == null || paramObject instanceof org.bouncycastle.asn1.DERUTF8String)
/*     */     {
/*  29 */       return (org.bouncycastle.asn1.DERUTF8String)paramObject;
/*     */     }
/*     */     
/*  32 */     if (paramObject instanceof byte[]) {
/*     */       
/*     */       try {
/*     */         
/*  36 */         return (org.bouncycastle.asn1.DERUTF8String)fromByteArray((byte[])paramObject);
/*     */       }
/*  38 */       catch (Exception exception) {
/*     */         
/*  40 */         throw new IllegalArgumentException("encoding error in getInstance: " + exception.toString());
/*     */       } 
/*     */     }
/*     */     
/*  44 */     throw new IllegalArgumentException("illegal object in getInstance: " + paramObject
/*  45 */         .getClass().getName());
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
/*     */   public static org.bouncycastle.asn1.DERUTF8String getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
/*  64 */     ASN1Primitive aSN1Primitive = paramASN1TaggedObject.getObject();
/*     */     
/*  66 */     if (paramBoolean || aSN1Primitive instanceof org.bouncycastle.asn1.DERUTF8String)
/*     */     {
/*  68 */       return getInstance(aSN1Primitive);
/*     */     }
/*     */ 
/*     */     
/*  72 */     return new org.bouncycastle.asn1.DERUTF8String(ASN1OctetString.getInstance(aSN1Primitive).getOctets());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   DERUTF8String(byte[] paramArrayOfbyte) {
/*  81 */     this.string = paramArrayOfbyte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DERUTF8String(String paramString) {
/*  91 */     this.string = Strings.toUTF8ByteArray(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getString() {
/*  96 */     return Strings.fromUTF8ByteArray(this.string);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 101 */     return getString();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 106 */     return Arrays.hashCode(this.string);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean asn1Equals(ASN1Primitive paramASN1Primitive) {
/* 111 */     if (!(paramASN1Primitive instanceof org.bouncycastle.asn1.DERUTF8String))
/*     */     {
/* 113 */       return false;
/*     */     }
/*     */     
/* 116 */     org.bouncycastle.asn1.DERUTF8String dERUTF8String = (org.bouncycastle.asn1.DERUTF8String)paramASN1Primitive;
/*     */     
/* 118 */     return Arrays.areEqual(this.string, dERUTF8String.string);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isConstructed() {
/* 123 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   int encodedLength() throws IOException {
/* 129 */     return 1 + StreamUtil.calculateBodyLength(this.string.length) + this.string.length;
/*     */   }
/*     */ 
/*     */   
/*     */   void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/* 134 */     paramASN1OutputStream.writeEncoded(paramBoolean, 12, this.string);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/DERUTF8String.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */