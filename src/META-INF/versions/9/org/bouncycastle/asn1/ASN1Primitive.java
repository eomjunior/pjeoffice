/*    */ package META-INF.versions.9.org.bouncycastle.asn1;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import org.bouncycastle.asn1.ASN1Encodable;
/*    */ import org.bouncycastle.asn1.ASN1InputStream;
/*    */ import org.bouncycastle.asn1.ASN1Object;
/*    */ import org.bouncycastle.asn1.ASN1OutputStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ASN1Primitive
/*    */   extends ASN1Object
/*    */ {
/*    */   public void encodeTo(OutputStream paramOutputStream) throws IOException {
/* 18 */     ASN1OutputStream.create(paramOutputStream).writeObject(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void encodeTo(OutputStream paramOutputStream, String paramString) throws IOException {
/* 23 */     ASN1OutputStream.create(paramOutputStream, paramString).writeObject(this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static org.bouncycastle.asn1.ASN1Primitive fromByteArray(byte[] paramArrayOfbyte) throws IOException {
/* 36 */     ASN1InputStream aSN1InputStream = new ASN1InputStream(paramArrayOfbyte);
/*    */ 
/*    */     
/*    */     try {
/* 40 */       org.bouncycastle.asn1.ASN1Primitive aSN1Primitive = aSN1InputStream.readObject();
/*    */       
/* 42 */       if (aSN1InputStream.available() != 0)
/*    */       {
/* 44 */         throw new IOException("Extra data detected in stream");
/*    */       }
/*    */       
/* 47 */       return aSN1Primitive;
/*    */     }
/* 49 */     catch (ClassCastException classCastException) {
/*    */       
/* 51 */       throw new IOException("cannot recognise object in stream");
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean equals(Object paramObject) {
/* 57 */     if (this == paramObject)
/*    */     {
/* 59 */       return true;
/*    */     }
/*    */     
/* 62 */     return (paramObject instanceof ASN1Encodable && asn1Equals(((ASN1Encodable)paramObject).toASN1Primitive()));
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean equals(ASN1Encodable paramASN1Encodable) {
/* 67 */     return (this == paramASN1Encodable || (null != paramASN1Encodable && asn1Equals(paramASN1Encodable.toASN1Primitive())));
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean equals(org.bouncycastle.asn1.ASN1Primitive paramASN1Primitive) {
/* 72 */     return (this == paramASN1Primitive || asn1Equals(paramASN1Primitive));
/*    */   }
/*    */ 
/*    */   
/*    */   public final org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
/* 77 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   org.bouncycastle.asn1.ASN1Primitive toDERObject() {
/* 87 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   org.bouncycastle.asn1.ASN1Primitive toDLObject() {
/* 97 */     return this;
/*    */   }
/*    */   
/*    */   public abstract int hashCode();
/*    */   
/*    */   abstract boolean isConstructed();
/*    */   
/*    */   abstract int encodedLength() throws IOException;
/*    */   
/*    */   abstract void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException;
/*    */   
/*    */   abstract boolean asn1Equals(org.bouncycastle.asn1.ASN1Primitive paramASN1Primitive);
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/ASN1Primitive.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */