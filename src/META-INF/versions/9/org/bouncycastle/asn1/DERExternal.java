/*    */ package META-INF.versions.9.org.bouncycastle.asn1;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import org.bouncycastle.asn1.ASN1Encodable;
/*    */ import org.bouncycastle.asn1.ASN1EncodableVector;
/*    */ import org.bouncycastle.asn1.ASN1External;
/*    */ import org.bouncycastle.asn1.ASN1Integer;
/*    */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*    */ import org.bouncycastle.asn1.ASN1OutputStream;
/*    */ import org.bouncycastle.asn1.ASN1Primitive;
/*    */ import org.bouncycastle.asn1.DERTaggedObject;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DERExternal
/*    */   extends ASN1External
/*    */ {
/*    */   public DERExternal(ASN1EncodableVector paramASN1EncodableVector) {
/* 26 */     super(paramASN1EncodableVector);
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
/*    */   public DERExternal(ASN1ObjectIdentifier paramASN1ObjectIdentifier, ASN1Integer paramASN1Integer, ASN1Primitive paramASN1Primitive, DERTaggedObject paramDERTaggedObject) {
/* 39 */     this(paramASN1ObjectIdentifier, paramASN1Integer, paramASN1Primitive, paramDERTaggedObject.getTagNo(), paramDERTaggedObject.toASN1Primitive());
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
/*    */   
/*    */   public DERExternal(ASN1ObjectIdentifier paramASN1ObjectIdentifier, ASN1Integer paramASN1Integer, ASN1Primitive paramASN1Primitive1, int paramInt, ASN1Primitive paramASN1Primitive2) {
/* 53 */     super(paramASN1ObjectIdentifier, paramASN1Integer, paramASN1Primitive1, paramInt, paramASN1Primitive2);
/*    */   }
/*    */ 
/*    */   
/*    */   ASN1Primitive toDERObject() {
/* 58 */     return (ASN1Primitive)this;
/*    */   }
/*    */ 
/*    */   
/*    */   ASN1Primitive toDLObject() {
/* 63 */     return (ASN1Primitive)this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   int encodedLength() throws IOException {
/* 69 */     return (getEncoded()).length;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/* 77 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/* 78 */     if (this.directReference != null)
/*    */     {
/* 80 */       byteArrayOutputStream.write(this.directReference.getEncoded("DER"));
/*    */     }
/* 82 */     if (this.indirectReference != null)
/*    */     {
/* 84 */       byteArrayOutputStream.write(this.indirectReference.getEncoded("DER"));
/*    */     }
/* 86 */     if (this.dataValueDescriptor != null)
/*    */     {
/* 88 */       byteArrayOutputStream.write(this.dataValueDescriptor.getEncoded("DER"));
/*    */     }
/* 90 */     DERTaggedObject dERTaggedObject = new DERTaggedObject(true, this.encoding, (ASN1Encodable)this.externalContent);
/* 91 */     byteArrayOutputStream.write(dERTaggedObject.getEncoded("DER"));
/*    */     
/* 93 */     paramASN1OutputStream.writeEncoded(paramBoolean, 32, 8, byteArrayOutputStream.toByteArray());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/DERExternal.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */