/*    */ package META-INF.versions.9.org.bouncycastle.asn1;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.bouncycastle.asn1.ASN1Encodable;
/*    */ import org.bouncycastle.asn1.ASN1EncodableVector;
/*    */ import org.bouncycastle.asn1.ASN1OutputStream;
/*    */ import org.bouncycastle.asn1.ASN1Primitive;
/*    */ import org.bouncycastle.asn1.ASN1Set;
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
/*    */ 
/*    */ public class BERSet
/*    */   extends ASN1Set
/*    */ {
/*    */   public BERSet() {}
/*    */   
/*    */   public BERSet(ASN1Encodable paramASN1Encodable) {
/* 36 */     super(paramASN1Encodable);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BERSet(ASN1EncodableVector paramASN1EncodableVector) {
/* 45 */     super(paramASN1EncodableVector, false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BERSet(ASN1Encodable[] paramArrayOfASN1Encodable) {
/* 54 */     super(paramArrayOfASN1Encodable, false);
/*    */   }
/*    */ 
/*    */   
/*    */   BERSet(boolean paramBoolean, ASN1Encodable[] paramArrayOfASN1Encodable) {
/* 59 */     super(paramBoolean, paramArrayOfASN1Encodable);
/*    */   }
/*    */ 
/*    */   
/*    */   int encodedLength() throws IOException {
/* 64 */     int i = this.elements.length;
/* 65 */     int j = 0;
/*    */     
/* 67 */     for (byte b = 0; b < i; b++) {
/*    */       
/* 69 */       ASN1Primitive aSN1Primitive = this.elements[b].toASN1Primitive();
/* 70 */       j += aSN1Primitive.encodedLength();
/*    */     } 
/*    */     
/* 73 */     return 2 + j + 2;
/*    */   }
/*    */ 
/*    */   
/*    */   void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/* 78 */     paramASN1OutputStream.writeEncodedIndef(paramBoolean, 49, this.elements);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/BERSet.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */