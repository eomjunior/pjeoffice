/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.bouncycastle.asn1.ASN1OctetString;
/*     */ import org.bouncycastle.asn1.ASN1OutputStream;
/*     */ import org.bouncycastle.asn1.ASN1ParsingException;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1String;
/*     */ import org.bouncycastle.asn1.ASN1TaggedObject;
/*     */ import org.bouncycastle.asn1.StreamUtil;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ 
/*     */ public class DERUniversalString
/*     */   extends ASN1Primitive implements ASN1String {
/*  15 */   private static final char[] table = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final byte[] string;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static org.bouncycastle.asn1.DERUniversalString getInstance(Object paramObject) {
/*  28 */     if (paramObject == null || paramObject instanceof org.bouncycastle.asn1.DERUniversalString)
/*     */     {
/*  30 */       return (org.bouncycastle.asn1.DERUniversalString)paramObject;
/*     */     }
/*     */     
/*  33 */     if (paramObject instanceof byte[]) {
/*     */       
/*     */       try {
/*     */         
/*  37 */         return (org.bouncycastle.asn1.DERUniversalString)fromByteArray((byte[])paramObject);
/*     */       }
/*  39 */       catch (Exception exception) {
/*     */         
/*  41 */         throw new IllegalArgumentException("encoding error getInstance: " + exception.toString());
/*     */       } 
/*     */     }
/*     */     
/*  45 */     throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
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
/*     */   public static org.bouncycastle.asn1.DERUniversalString getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
/*  62 */     ASN1Primitive aSN1Primitive = paramASN1TaggedObject.getObject();
/*     */     
/*  64 */     if (paramBoolean || aSN1Primitive instanceof org.bouncycastle.asn1.DERUniversalString)
/*     */     {
/*  66 */       return getInstance(aSN1Primitive);
/*     */     }
/*     */ 
/*     */     
/*  70 */     return new org.bouncycastle.asn1.DERUniversalString(ASN1OctetString.getInstance(aSN1Primitive).getOctets());
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
/*     */   public DERUniversalString(byte[] paramArrayOfbyte) {
/*  82 */     this.string = Arrays.clone(paramArrayOfbyte);
/*     */   }
/*     */   
/*     */   public String getString() {
/*     */     byte[] arrayOfByte;
/*  87 */     StringBuffer stringBuffer = new StringBuffer("#");
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  92 */       arrayOfByte = getEncoded();
/*     */     }
/*  94 */     catch (IOException iOException) {
/*     */       
/*  96 */       throw new ASN1ParsingException("internal error encoding UniversalString");
/*     */     } 
/*     */     
/*  99 */     for (byte b = 0; b != arrayOfByte.length; b++) {
/*     */       
/* 101 */       stringBuffer.append(table[arrayOfByte[b] >>> 4 & 0xF]);
/* 102 */       stringBuffer.append(table[arrayOfByte[b] & 0xF]);
/*     */     } 
/*     */     
/* 105 */     return stringBuffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 110 */     return getString();
/*     */   }
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
/* 130 */     paramASN1OutputStream.writeEncoded(paramBoolean, 28, this.string);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean asn1Equals(ASN1Primitive paramASN1Primitive) {
/* 136 */     if (!(paramASN1Primitive instanceof org.bouncycastle.asn1.DERUniversalString))
/*     */     {
/* 138 */       return false;
/*     */     }
/*     */     
/* 141 */     return Arrays.areEqual(this.string, ((org.bouncycastle.asn1.DERUniversalString)paramASN1Primitive).string);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 146 */     return Arrays.hashCode(this.string);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/DERUniversalString.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */