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
/*     */ public class DERPrintableString
/*     */   extends ASN1Primitive
/*     */   implements ASN1String
/*     */ {
/*     */   private final byte[] string;
/*     */   
/*     */   public static org.bouncycastle.asn1.DERPrintableString getInstance(Object paramObject) {
/*  50 */     if (paramObject == null || paramObject instanceof org.bouncycastle.asn1.DERPrintableString)
/*     */     {
/*  52 */       return (org.bouncycastle.asn1.DERPrintableString)paramObject;
/*     */     }
/*     */     
/*  55 */     if (paramObject instanceof byte[]) {
/*     */       
/*     */       try {
/*     */         
/*  59 */         return (org.bouncycastle.asn1.DERPrintableString)fromByteArray((byte[])paramObject);
/*     */       }
/*  61 */       catch (Exception exception) {
/*     */         
/*  63 */         throw new IllegalArgumentException("encoding error in getInstance: " + exception.toString());
/*     */       } 
/*     */     }
/*     */     
/*  67 */     throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
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
/*     */   public static org.bouncycastle.asn1.DERPrintableString getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
/*  84 */     ASN1Primitive aSN1Primitive = paramASN1TaggedObject.getObject();
/*     */     
/*  86 */     if (paramBoolean || aSN1Primitive instanceof org.bouncycastle.asn1.DERPrintableString)
/*     */     {
/*  88 */       return getInstance(aSN1Primitive);
/*     */     }
/*     */ 
/*     */     
/*  92 */     return new org.bouncycastle.asn1.DERPrintableString(ASN1OctetString.getInstance(aSN1Primitive).getOctets());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   DERPrintableString(byte[] paramArrayOfbyte) {
/* 102 */     this.string = paramArrayOfbyte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DERPrintableString(String paramString) {
/* 111 */     this(paramString, false);
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
/*     */   public DERPrintableString(String paramString, boolean paramBoolean) {
/* 126 */     if (paramBoolean && !isPrintableString(paramString))
/*     */     {
/* 128 */       throw new IllegalArgumentException("string contains illegal characters");
/*     */     }
/*     */     
/* 131 */     this.string = Strings.toByteArray(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getString() {
/* 136 */     return Strings.fromByteArray(this.string);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getOctets() {
/* 141 */     return Arrays.clone(this.string);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isConstructed() {
/* 146 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   int encodedLength() {
/* 151 */     return 1 + StreamUtil.calculateBodyLength(this.string.length) + this.string.length;
/*     */   }
/*     */ 
/*     */   
/*     */   void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/* 156 */     paramASN1OutputStream.writeEncoded(paramBoolean, 19, this.string);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 161 */     return Arrays.hashCode(this.string);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean asn1Equals(ASN1Primitive paramASN1Primitive) {
/* 167 */     if (!(paramASN1Primitive instanceof org.bouncycastle.asn1.DERPrintableString))
/*     */     {
/* 169 */       return false;
/*     */     }
/*     */     
/* 172 */     org.bouncycastle.asn1.DERPrintableString dERPrintableString = (org.bouncycastle.asn1.DERPrintableString)paramASN1Primitive;
/*     */     
/* 174 */     return Arrays.areEqual(this.string, dERPrintableString.string);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 179 */     return getString();
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
/*     */   public static boolean isPrintableString(String paramString) {
/* 191 */     for (int i = paramString.length() - 1; i >= 0; i--) {
/*     */       
/* 193 */       char c = paramString.charAt(i);
/*     */       
/* 195 */       if (c > '')
/*     */       {
/* 197 */         return false;
/*     */       }
/*     */       
/* 200 */       if ('a' > c || c > 'z')
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 205 */         if ('A' > c || c > 'Z')
/*     */         {
/*     */ 
/*     */ 
/*     */           
/* 210 */           if ('0' > c || c > '9')
/*     */           {
/*     */ 
/*     */ 
/*     */             
/* 215 */             switch (c) {
/*     */               case ' ':
/*     */               case '\'':
/*     */               case '(':
/*     */               case ')':
/*     */               case '+':
/*     */               case ',':
/*     */               case '-':
/*     */               case '.':
/*     */               case '/':
/*     */               case ':':
/*     */               case '=':
/*     */               case '?':
/*     */                 break;
/*     */ 
/*     */               
/*     */               default:
/* 232 */                 return false;
/*     */             }  }  }  } 
/*     */     } 
/* 235 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/DERPrintableString.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */