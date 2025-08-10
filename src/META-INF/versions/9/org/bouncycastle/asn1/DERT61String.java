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
/*     */ public class DERT61String
/*     */   extends ASN1Primitive
/*     */   implements ASN1String
/*     */ {
/*     */   private byte[] string;
/*     */   
/*     */   public static org.bouncycastle.asn1.DERT61String getInstance(Object paramObject) {
/*  28 */     if (paramObject == null || paramObject instanceof org.bouncycastle.asn1.DERT61String)
/*     */     {
/*  30 */       return (org.bouncycastle.asn1.DERT61String)paramObject;
/*     */     }
/*     */     
/*  33 */     if (paramObject instanceof byte[]) {
/*     */       
/*     */       try {
/*     */         
/*  37 */         return (org.bouncycastle.asn1.DERT61String)fromByteArray((byte[])paramObject);
/*     */       }
/*  39 */       catch (Exception exception) {
/*     */         
/*  41 */         throw new IllegalArgumentException("encoding error in getInstance: " + exception.toString());
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
/*     */   public static org.bouncycastle.asn1.DERT61String getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
/*  62 */     ASN1Primitive aSN1Primitive = paramASN1TaggedObject.getObject();
/*     */     
/*  64 */     if (paramBoolean || aSN1Primitive instanceof org.bouncycastle.asn1.DERT61String)
/*     */     {
/*  66 */       return getInstance(aSN1Primitive);
/*     */     }
/*     */ 
/*     */     
/*  70 */     return new org.bouncycastle.asn1.DERT61String(ASN1OctetString.getInstance(aSN1Primitive).getOctets());
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
/*     */   public DERT61String(byte[] paramArrayOfbyte) {
/*  82 */     this.string = Arrays.clone(paramArrayOfbyte);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DERT61String(String paramString) {
/*  93 */     this.string = Strings.toByteArray(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getString() {
/* 102 */     return Strings.fromByteArray(this.string);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 107 */     return getString();
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isConstructed() {
/* 112 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   int encodedLength() {
/* 117 */     return 1 + StreamUtil.calculateBodyLength(this.string.length) + this.string.length;
/*     */   }
/*     */ 
/*     */   
/*     */   void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/* 122 */     paramASN1OutputStream.writeEncoded(paramBoolean, 20, this.string);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getOctets() {
/* 131 */     return Arrays.clone(this.string);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean asn1Equals(ASN1Primitive paramASN1Primitive) {
/* 137 */     if (!(paramASN1Primitive instanceof org.bouncycastle.asn1.DERT61String))
/*     */     {
/* 139 */       return false;
/*     */     }
/*     */     
/* 142 */     return Arrays.areEqual(this.string, ((org.bouncycastle.asn1.DERT61String)paramASN1Primitive).string);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 147 */     return Arrays.hashCode(this.string);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/DERT61String.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */