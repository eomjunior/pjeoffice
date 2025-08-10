/*    */ package META-INF.versions.9.org.bouncycastle.asn1;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.bouncycastle.asn1.ASN1Encodable;
/*    */ import org.bouncycastle.asn1.ASN1OctetString;
/*    */ import org.bouncycastle.asn1.ASN1OutputStream;
/*    */ import org.bouncycastle.asn1.ASN1Primitive;
/*    */ import org.bouncycastle.asn1.StreamUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DEROctetString
/*    */   extends ASN1OctetString
/*    */ {
/*    */   public DEROctetString(byte[] paramArrayOfbyte) {
/* 19 */     super(paramArrayOfbyte);
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
/*    */   public DEROctetString(ASN1Encodable paramASN1Encodable) throws IOException {
/* 31 */     super(paramASN1Encodable.toASN1Primitive().getEncoded("DER"));
/*    */   }
/*    */ 
/*    */   
/*    */   boolean isConstructed() {
/* 36 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   int encodedLength() {
/* 41 */     return 1 + StreamUtil.calculateBodyLength(this.string.length) + this.string.length;
/*    */   }
/*    */ 
/*    */   
/*    */   void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/* 46 */     paramASN1OutputStream.writeEncoded(paramBoolean, 4, this.string);
/*    */   }
/*    */ 
/*    */   
/*    */   ASN1Primitive toDERObject() {
/* 51 */     return (ASN1Primitive)this;
/*    */   }
/*    */ 
/*    */   
/*    */   ASN1Primitive toDLObject() {
/* 56 */     return (ASN1Primitive)this;
/*    */   }
/*    */ 
/*    */   
/*    */   static void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
/* 61 */     paramASN1OutputStream.writeEncoded(paramBoolean, 4, paramArrayOfbyte, paramInt1, paramInt2);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/DEROctetString.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */