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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DERNumericString
/*     */   extends ASN1Primitive
/*     */   implements ASN1String
/*     */ {
/*     */   private final byte[] string;
/*     */   
/*     */   public static org.bouncycastle.asn1.DERNumericString getInstance(Object paramObject) {
/*  34 */     if (paramObject == null || paramObject instanceof org.bouncycastle.asn1.DERNumericString)
/*     */     {
/*  36 */       return (org.bouncycastle.asn1.DERNumericString)paramObject;
/*     */     }
/*     */     
/*  39 */     if (paramObject instanceof byte[]) {
/*     */       
/*     */       try {
/*     */         
/*  43 */         return (org.bouncycastle.asn1.DERNumericString)fromByteArray((byte[])paramObject);
/*     */       }
/*  45 */       catch (Exception exception) {
/*     */         
/*  47 */         throw new IllegalArgumentException("encoding error in getInstance: " + exception.toString());
/*     */       } 
/*     */     }
/*     */     
/*  51 */     throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
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
/*     */   public static org.bouncycastle.asn1.DERNumericString getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
/*  68 */     ASN1Primitive aSN1Primitive = paramASN1TaggedObject.getObject();
/*     */     
/*  70 */     if (paramBoolean || aSN1Primitive instanceof org.bouncycastle.asn1.DERNumericString)
/*     */     {
/*  72 */       return getInstance(aSN1Primitive);
/*     */     }
/*     */ 
/*     */     
/*  76 */     return new org.bouncycastle.asn1.DERNumericString(ASN1OctetString.getInstance(aSN1Primitive).getOctets());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   DERNumericString(byte[] paramArrayOfbyte) {
/*  86 */     this.string = paramArrayOfbyte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DERNumericString(String paramString) {
/*  95 */     this(paramString, false);
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
/*     */   public DERNumericString(String paramString, boolean paramBoolean) {
/* 110 */     if (paramBoolean && !isNumericString(paramString))
/*     */     {
/* 112 */       throw new IllegalArgumentException("string contains illegal characters");
/*     */     }
/*     */     
/* 115 */     this.string = Strings.toByteArray(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getString() {
/* 120 */     return Strings.fromByteArray(this.string);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 125 */     return getString();
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getOctets() {
/* 130 */     return Arrays.clone(this.string);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isConstructed() {
/* 135 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   int encodedLength() {
/* 140 */     return 1 + StreamUtil.calculateBodyLength(this.string.length) + this.string.length;
/*     */   }
/*     */ 
/*     */   
/*     */   void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/* 145 */     paramASN1OutputStream.writeEncoded(paramBoolean, 18, this.string);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 150 */     return Arrays.hashCode(this.string);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean asn1Equals(ASN1Primitive paramASN1Primitive) {
/* 156 */     if (!(paramASN1Primitive instanceof org.bouncycastle.asn1.DERNumericString))
/*     */     {
/* 158 */       return false;
/*     */     }
/*     */     
/* 161 */     org.bouncycastle.asn1.DERNumericString dERNumericString = (org.bouncycastle.asn1.DERNumericString)paramASN1Primitive;
/*     */     
/* 163 */     return Arrays.areEqual(this.string, dERNumericString.string);
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
/*     */   public static boolean isNumericString(String paramString) {
/* 175 */     for (int i = paramString.length() - 1; i >= 0; ) {
/*     */       
/* 177 */       char c = paramString.charAt(i);
/*     */       
/* 179 */       if (c > '')
/*     */       {
/* 181 */         return false;
/*     */       }
/*     */       
/* 184 */       if (('0' <= c && c <= '9') || c == ' ') {
/*     */         i--;
/*     */         
/*     */         continue;
/*     */       } 
/* 189 */       return false;
/*     */     } 
/*     */     
/* 192 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/DERNumericString.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */