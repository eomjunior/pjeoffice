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
/*     */ public class DERVideotexString
/*     */   extends ASN1Primitive
/*     */   implements ASN1String
/*     */ {
/*     */   private final byte[] string;
/*     */   
/*     */   public static org.bouncycastle.asn1.DERVideotexString getInstance(Object paramObject) {
/*  24 */     if (paramObject == null || paramObject instanceof org.bouncycastle.asn1.DERVideotexString)
/*     */     {
/*  26 */       return (org.bouncycastle.asn1.DERVideotexString)paramObject;
/*     */     }
/*     */     
/*  29 */     if (paramObject instanceof byte[]) {
/*     */       
/*     */       try {
/*     */         
/*  33 */         return (org.bouncycastle.asn1.DERVideotexString)fromByteArray((byte[])paramObject);
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
/*     */   public static org.bouncycastle.asn1.DERVideotexString getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
/*  58 */     ASN1Primitive aSN1Primitive = paramASN1TaggedObject.getObject();
/*     */     
/*  60 */     if (paramBoolean || aSN1Primitive instanceof org.bouncycastle.asn1.DERVideotexString)
/*     */     {
/*  62 */       return getInstance(aSN1Primitive);
/*     */     }
/*     */ 
/*     */     
/*  66 */     return new org.bouncycastle.asn1.DERVideotexString(ASN1OctetString.getInstance(aSN1Primitive).getOctets());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DERVideotexString(byte[] paramArrayOfbyte) {
/*  77 */     this.string = Arrays.clone(paramArrayOfbyte);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getOctets() {
/*  82 */     return Arrays.clone(this.string);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isConstructed() {
/*  87 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   int encodedLength() {
/*  92 */     return 1 + StreamUtil.calculateBodyLength(this.string.length) + this.string.length;
/*     */   }
/*     */ 
/*     */   
/*     */   void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/*  97 */     paramASN1OutputStream.writeEncoded(paramBoolean, 21, this.string);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 102 */     return Arrays.hashCode(this.string);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean asn1Equals(ASN1Primitive paramASN1Primitive) {
/* 108 */     if (!(paramASN1Primitive instanceof org.bouncycastle.asn1.DERVideotexString))
/*     */     {
/* 110 */       return false;
/*     */     }
/*     */     
/* 113 */     org.bouncycastle.asn1.DERVideotexString dERVideotexString = (org.bouncycastle.asn1.DERVideotexString)paramASN1Primitive;
/*     */     
/* 115 */     return Arrays.areEqual(this.string, dERVideotexString.string);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getString() {
/* 120 */     return Strings.fromByteArray(this.string);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/DERVideotexString.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */