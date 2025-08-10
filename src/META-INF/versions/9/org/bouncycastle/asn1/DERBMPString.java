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
/*     */ public class DERBMPString
/*     */   extends ASN1Primitive
/*     */   implements ASN1String
/*     */ {
/*     */   private final char[] string;
/*     */   
/*     */   public static org.bouncycastle.asn1.DERBMPString getInstance(Object paramObject) {
/*  31 */     if (paramObject == null || paramObject instanceof org.bouncycastle.asn1.DERBMPString)
/*     */     {
/*  33 */       return (org.bouncycastle.asn1.DERBMPString)paramObject;
/*     */     }
/*     */     
/*  36 */     if (paramObject instanceof byte[]) {
/*     */       
/*     */       try {
/*     */         
/*  40 */         return (org.bouncycastle.asn1.DERBMPString)fromByteArray((byte[])paramObject);
/*     */       }
/*  42 */       catch (Exception exception) {
/*     */         
/*  44 */         throw new IllegalArgumentException("encoding error in getInstance: " + exception.toString());
/*     */       } 
/*     */     }
/*     */     
/*  48 */     throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
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
/*     */   public static org.bouncycastle.asn1.DERBMPString getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
/*  65 */     ASN1Primitive aSN1Primitive = paramASN1TaggedObject.getObject();
/*     */     
/*  67 */     if (paramBoolean || aSN1Primitive instanceof org.bouncycastle.asn1.DERBMPString)
/*     */     {
/*  69 */       return getInstance(aSN1Primitive);
/*     */     }
/*     */ 
/*     */     
/*  73 */     return new org.bouncycastle.asn1.DERBMPString(ASN1OctetString.getInstance(aSN1Primitive).getOctets());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   DERBMPString(byte[] paramArrayOfbyte) {
/*  84 */     if (paramArrayOfbyte == null)
/*     */     {
/*  86 */       throw new NullPointerException("'string' cannot be null");
/*     */     }
/*     */     
/*  89 */     int i = paramArrayOfbyte.length;
/*  90 */     if (0 != (i & 0x1))
/*     */     {
/*  92 */       throw new IllegalArgumentException("malformed BMPString encoding encountered");
/*     */     }
/*     */     
/*  95 */     int j = i / 2;
/*  96 */     char[] arrayOfChar = new char[j];
/*     */     
/*  98 */     for (int k = 0; k != j; k++)
/*     */     {
/* 100 */       arrayOfChar[k] = (char)(paramArrayOfbyte[2 * k] << 8 | paramArrayOfbyte[2 * k + 1] & 0xFF);
/*     */     }
/*     */     
/* 103 */     this.string = arrayOfChar;
/*     */   }
/*     */ 
/*     */   
/*     */   DERBMPString(char[] paramArrayOfchar) {
/* 108 */     if (paramArrayOfchar == null)
/*     */     {
/* 110 */       throw new NullPointerException("'string' cannot be null");
/*     */     }
/*     */     
/* 113 */     this.string = paramArrayOfchar;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DERBMPString(String paramString) {
/* 123 */     if (paramString == null)
/*     */     {
/* 125 */       throw new NullPointerException("'string' cannot be null");
/*     */     }
/*     */     
/* 128 */     this.string = paramString.toCharArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getString() {
/* 133 */     return new String(this.string);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 138 */     return getString();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 143 */     return Arrays.hashCode(this.string);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean asn1Equals(ASN1Primitive paramASN1Primitive) {
/* 149 */     if (!(paramASN1Primitive instanceof org.bouncycastle.asn1.DERBMPString))
/*     */     {
/* 151 */       return false;
/*     */     }
/*     */     
/* 154 */     org.bouncycastle.asn1.DERBMPString dERBMPString = (org.bouncycastle.asn1.DERBMPString)paramASN1Primitive;
/*     */     
/* 156 */     return Arrays.areEqual(this.string, dERBMPString.string);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isConstructed() {
/* 161 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   int encodedLength() {
/* 166 */     return 1 + StreamUtil.calculateBodyLength(this.string.length * 2) + this.string.length * 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/* 173 */     int i = this.string.length;
/* 174 */     if (paramBoolean)
/*     */     {
/* 176 */       paramASN1OutputStream.write(30);
/*     */     }
/* 178 */     paramASN1OutputStream.writeLength(i * 2);
/*     */     
/* 180 */     byte[] arrayOfByte = new byte[8];
/*     */     
/* 182 */     byte b = 0; int j = i & 0xFFFFFFFC;
/* 183 */     while (b < j) {
/*     */       
/* 185 */       char c1 = this.string[b], c2 = this.string[b + 1], c3 = this.string[b + 2], c4 = this.string[b + 3];
/* 186 */       b += 4;
/*     */       
/* 188 */       arrayOfByte[0] = (byte)(c1 >> 8);
/* 189 */       arrayOfByte[1] = (byte)c1;
/* 190 */       arrayOfByte[2] = (byte)(c2 >> 8);
/* 191 */       arrayOfByte[3] = (byte)c2;
/* 192 */       arrayOfByte[4] = (byte)(c3 >> 8);
/* 193 */       arrayOfByte[5] = (byte)c3;
/* 194 */       arrayOfByte[6] = (byte)(c4 >> 8);
/* 195 */       arrayOfByte[7] = (byte)c4;
/*     */       
/* 197 */       paramASN1OutputStream.write(arrayOfByte, 0, 8);
/*     */     } 
/* 199 */     if (b < i) {
/*     */       
/* 201 */       byte b1 = 0;
/*     */       
/*     */       do {
/* 204 */         char c = this.string[b];
/* 205 */         b++;
/*     */         
/* 207 */         arrayOfByte[b1++] = (byte)(c >> 8);
/* 208 */         arrayOfByte[b1++] = (byte)c;
/*     */       }
/* 210 */       while (b < i);
/*     */       
/* 212 */       paramASN1OutputStream.write(arrayOfByte, 0, b1);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/DERBMPString.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */