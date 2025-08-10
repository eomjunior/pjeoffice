/*    */ package META-INF.versions.9.org.bouncycastle.asn1;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import org.bouncycastle.asn1.ASN1Encodable;
/*    */ import org.bouncycastle.asn1.ASN1OutputStream;
/*    */ import org.bouncycastle.asn1.ASN1Primitive;
/*    */ import org.bouncycastle.util.Encodable;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ASN1Object
/*    */   implements ASN1Encodable, Encodable
/*    */ {
/*    */   public void encodeTo(OutputStream paramOutputStream) throws IOException {
/* 17 */     ASN1OutputStream.create(paramOutputStream).writeObject(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void encodeTo(OutputStream paramOutputStream, String paramString) throws IOException {
/* 22 */     ASN1OutputStream.create(paramOutputStream, paramString).writeObject(this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] getEncoded() throws IOException {
/* 33 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/* 34 */     encodeTo(byteArrayOutputStream);
/* 35 */     return byteArrayOutputStream.toByteArray();
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
/*    */   public byte[] getEncoded(String paramString) throws IOException {
/* 47 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/* 48 */     encodeTo(byteArrayOutputStream, paramString);
/* 49 */     return byteArrayOutputStream.toByteArray();
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 54 */     return toASN1Primitive().hashCode();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 60 */     if (this == paramObject)
/*    */     {
/* 62 */       return true;
/*    */     }
/*    */     
/* 65 */     if (!(paramObject instanceof ASN1Encodable))
/*    */     {
/* 67 */       return false;
/*    */     }
/*    */     
/* 70 */     ASN1Encodable aSN1Encodable = (ASN1Encodable)paramObject;
/*    */     
/* 72 */     return toASN1Primitive().equals(aSN1Encodable.toASN1Primitive());
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
/*    */   protected static boolean hasEncodedTagValue(Object paramObject, int paramInt) {
/* 84 */     return (paramObject instanceof byte[] && ((byte[])paramObject)[0] == paramInt);
/*    */   }
/*    */   
/*    */   public abstract ASN1Primitive toASN1Primitive();
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/ASN1Object.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */