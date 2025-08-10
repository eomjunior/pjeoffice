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
/*     */ public class DERIA5String
/*     */   extends ASN1Primitive
/*     */   implements ASN1String
/*     */ {
/*     */   private final byte[] string;
/*     */   
/*     */   public static org.bouncycastle.asn1.DERIA5String getInstance(Object paramObject) {
/*  30 */     if (paramObject == null || paramObject instanceof org.bouncycastle.asn1.DERIA5String)
/*     */     {
/*  32 */       return (org.bouncycastle.asn1.DERIA5String)paramObject;
/*     */     }
/*     */     
/*  35 */     if (paramObject instanceof byte[]) {
/*     */       
/*     */       try {
/*     */         
/*  39 */         return (org.bouncycastle.asn1.DERIA5String)fromByteArray((byte[])paramObject);
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
/*     */   public static org.bouncycastle.asn1.DERIA5String getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
/*  64 */     ASN1Primitive aSN1Primitive = paramASN1TaggedObject.getObject();
/*     */     
/*  66 */     if (paramBoolean || aSN1Primitive instanceof org.bouncycastle.asn1.DERIA5String)
/*     */     {
/*  68 */       return getInstance(aSN1Primitive);
/*     */     }
/*     */ 
/*     */     
/*  72 */     return new org.bouncycastle.asn1.DERIA5String(ASN1OctetString.getInstance(aSN1Primitive).getOctets());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   DERIA5String(byte[] paramArrayOfbyte) {
/*  83 */     this.string = paramArrayOfbyte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DERIA5String(String paramString) {
/*  93 */     this(paramString, false);
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
/*     */   public DERIA5String(String paramString, boolean paramBoolean) {
/* 108 */     if (paramString == null)
/*     */     {
/* 110 */       throw new NullPointerException("'string' cannot be null");
/*     */     }
/* 112 */     if (paramBoolean && !isIA5String(paramString))
/*     */     {
/* 114 */       throw new IllegalArgumentException("'string' contains illegal characters");
/*     */     }
/*     */     
/* 117 */     this.string = Strings.toByteArray(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getString() {
/* 122 */     return Strings.fromByteArray(this.string);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 127 */     return getString();
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getOctets() {
/* 132 */     return Arrays.clone(this.string);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isConstructed() {
/* 137 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   int encodedLength() {
/* 142 */     return 1 + StreamUtil.calculateBodyLength(this.string.length) + this.string.length;
/*     */   }
/*     */ 
/*     */   
/*     */   void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/* 147 */     paramASN1OutputStream.writeEncoded(paramBoolean, 22, this.string);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 152 */     return Arrays.hashCode(this.string);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean asn1Equals(ASN1Primitive paramASN1Primitive) {
/* 158 */     if (!(paramASN1Primitive instanceof org.bouncycastle.asn1.DERIA5String))
/*     */     {
/* 160 */       return false;
/*     */     }
/*     */     
/* 163 */     org.bouncycastle.asn1.DERIA5String dERIA5String = (org.bouncycastle.asn1.DERIA5String)paramASN1Primitive;
/*     */     
/* 165 */     return Arrays.areEqual(this.string, dERIA5String.string);
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
/*     */   public static boolean isIA5String(String paramString) {
/* 178 */     for (int i = paramString.length() - 1; i >= 0; i--) {
/*     */       
/* 180 */       char c = paramString.charAt(i);
/*     */       
/* 182 */       if (c > '')
/*     */       {
/* 184 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 188 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/DERIA5String.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */