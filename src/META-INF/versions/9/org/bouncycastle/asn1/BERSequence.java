/*    */ package META-INF.versions.9.org.bouncycastle.asn1;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.bouncycastle.asn1.ASN1Encodable;
/*    */ import org.bouncycastle.asn1.ASN1EncodableVector;
/*    */ import org.bouncycastle.asn1.ASN1OutputStream;
/*    */ import org.bouncycastle.asn1.ASN1Primitive;
/*    */ import org.bouncycastle.asn1.ASN1Sequence;
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
/*    */ public class BERSequence
/*    */   extends ASN1Sequence
/*    */ {
/*    */   public BERSequence() {}
/*    */   
/*    */   public BERSequence(ASN1Encodable paramASN1Encodable) {
/* 28 */     super(paramASN1Encodable);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BERSequence(ASN1EncodableVector paramASN1EncodableVector) {
/* 36 */     super(paramASN1EncodableVector);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BERSequence(ASN1Encodable[] paramArrayOfASN1Encodable) {
/* 44 */     super(paramArrayOfASN1Encodable);
/*    */   }
/*    */ 
/*    */   
/*    */   int encodedLength() throws IOException {
/* 49 */     int i = this.elements.length;
/* 50 */     int j = 0;
/*    */     
/* 52 */     for (byte b = 0; b < i; b++) {
/*    */       
/* 54 */       ASN1Primitive aSN1Primitive = this.elements[b].toASN1Primitive();
/* 55 */       j += aSN1Primitive.encodedLength();
/*    */     } 
/*    */     
/* 58 */     return 2 + j + 2;
/*    */   }
/*    */ 
/*    */   
/*    */   void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/* 63 */     paramASN1OutputStream.writeEncodedIndef(paramBoolean, 48, this.elements);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/BERSequence.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */