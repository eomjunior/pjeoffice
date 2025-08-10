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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DERGeneralString
/*     */   extends ASN1Primitive
/*     */   implements ASN1String
/*     */ {
/*     */   private final byte[] string;
/*     */   
/*     */   public static org.bouncycastle.asn1.DERGeneralString getInstance(Object paramObject) {
/*  31 */     if (paramObject == null || paramObject instanceof org.bouncycastle.asn1.DERGeneralString)
/*     */     {
/*  33 */       return (org.bouncycastle.asn1.DERGeneralString)paramObject;
/*     */     }
/*     */     
/*  36 */     if (paramObject instanceof byte[]) {
/*     */       
/*     */       try {
/*     */         
/*  40 */         return (org.bouncycastle.asn1.DERGeneralString)fromByteArray((byte[])paramObject);
/*     */       }
/*  42 */       catch (Exception exception) {
/*     */         
/*  44 */         throw new IllegalArgumentException("encoding error in getInstance: " + exception.toString());
/*     */       } 
/*     */     }
/*     */     
/*  48 */     throw new IllegalArgumentException("illegal object in getInstance: " + paramObject
/*  49 */         .getClass().getName());
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
/*     */   public static org.bouncycastle.asn1.DERGeneralString getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
/*  66 */     ASN1Primitive aSN1Primitive = paramASN1TaggedObject.getObject();
/*     */     
/*  68 */     if (paramBoolean || aSN1Primitive instanceof org.bouncycastle.asn1.DERGeneralString)
/*     */     {
/*  70 */       return getInstance(aSN1Primitive);
/*     */     }
/*     */ 
/*     */     
/*  74 */     return new org.bouncycastle.asn1.DERGeneralString(ASN1OctetString.getInstance(aSN1Primitive).getOctets());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   DERGeneralString(byte[] paramArrayOfbyte) {
/*  80 */     this.string = paramArrayOfbyte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DERGeneralString(String paramString) {
/*  90 */     this.string = Strings.toByteArray(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getString() {
/* 100 */     return Strings.fromByteArray(this.string);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 105 */     return getString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getOctets() {
/* 115 */     return Arrays.clone(this.string);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isConstructed() {
/* 120 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   int encodedLength() {
/* 125 */     return 1 + StreamUtil.calculateBodyLength(this.string.length) + this.string.length;
/*     */   }
/*     */ 
/*     */   
/*     */   void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/* 130 */     paramASN1OutputStream.writeEncoded(paramBoolean, 27, this.string);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 135 */     return Arrays.hashCode(this.string);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean asn1Equals(ASN1Primitive paramASN1Primitive) {
/* 140 */     if (!(paramASN1Primitive instanceof org.bouncycastle.asn1.DERGeneralString))
/*     */     {
/* 142 */       return false;
/*     */     }
/* 144 */     org.bouncycastle.asn1.DERGeneralString dERGeneralString = (org.bouncycastle.asn1.DERGeneralString)paramASN1Primitive;
/*     */     
/* 146 */     return Arrays.areEqual(this.string, dERGeneralString.string);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/DERGeneralString.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */