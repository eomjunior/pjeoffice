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
/*     */ public class DERVisibleString
/*     */   extends ASN1Primitive
/*     */   implements ASN1String
/*     */ {
/*     */   private final byte[] string;
/*     */   
/*     */   public static org.bouncycastle.asn1.DERVisibleString getInstance(Object paramObject) {
/*  30 */     if (paramObject == null || paramObject instanceof org.bouncycastle.asn1.DERVisibleString)
/*     */     {
/*  32 */       return (org.bouncycastle.asn1.DERVisibleString)paramObject;
/*     */     }
/*     */     
/*  35 */     if (paramObject instanceof byte[]) {
/*     */       
/*     */       try {
/*     */         
/*  39 */         return (org.bouncycastle.asn1.DERVisibleString)fromByteArray((byte[])paramObject);
/*     */       }
/*  41 */       catch (Exception exception) {
/*     */         
/*  43 */         throw new IllegalArgumentException("encoding error in getInstance: " + exception.toString());
/*     */       } 
/*     */     }
/*     */     
/*  47 */     throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
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
/*     */   public static org.bouncycastle.asn1.DERVisibleString getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
/*  64 */     ASN1Primitive aSN1Primitive = paramASN1TaggedObject.getObject();
/*     */     
/*  66 */     if (paramBoolean || aSN1Primitive instanceof org.bouncycastle.asn1.DERVisibleString)
/*     */     {
/*  68 */       return getInstance(aSN1Primitive);
/*     */     }
/*     */ 
/*     */     
/*  72 */     return new org.bouncycastle.asn1.DERVisibleString(ASN1OctetString.getInstance(aSN1Primitive).getOctets());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   DERVisibleString(byte[] paramArrayOfbyte) {
/*  82 */     this.string = paramArrayOfbyte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DERVisibleString(String paramString) {
/*  93 */     this.string = Strings.toByteArray(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getString() {
/*  98 */     return Strings.fromByteArray(this.string);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 103 */     return getString();
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getOctets() {
/* 108 */     return Arrays.clone(this.string);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isConstructed() {
/* 113 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   int encodedLength() {
/* 118 */     return 1 + StreamUtil.calculateBodyLength(this.string.length) + this.string.length;
/*     */   }
/*     */ 
/*     */   
/*     */   void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/* 123 */     paramASN1OutputStream.writeEncoded(paramBoolean, 26, this.string);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean asn1Equals(ASN1Primitive paramASN1Primitive) {
/* 129 */     if (!(paramASN1Primitive instanceof org.bouncycastle.asn1.DERVisibleString))
/*     */     {
/* 131 */       return false;
/*     */     }
/*     */     
/* 134 */     return Arrays.areEqual(this.string, ((org.bouncycastle.asn1.DERVisibleString)paramASN1Primitive).string);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 139 */     return Arrays.hashCode(this.string);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/DERVisibleString.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */