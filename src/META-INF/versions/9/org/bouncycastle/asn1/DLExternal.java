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
/*    */ import org.bouncycastle.asn1.DLTaggedObject;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DLExternal
/*    */   extends ASN1External
/*    */ {
/*    */   public DLExternal(ASN1EncodableVector paramASN1EncodableVector) {
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
/*    */   public DLExternal(ASN1ObjectIdentifier paramASN1ObjectIdentifier, ASN1Integer paramASN1Integer, ASN1Primitive paramASN1Primitive, DERTaggedObject paramDERTaggedObject) {
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
/*    */   public DLExternal(ASN1ObjectIdentifier paramASN1ObjectIdentifier, ASN1Integer paramASN1Integer, ASN1Primitive paramASN1Primitive1, int paramInt, ASN1Primitive paramASN1Primitive2) {
/* 53 */     super(paramASN1ObjectIdentifier, paramASN1Integer, paramASN1Primitive1, paramInt, paramASN1Primitive2);
/*    */   }
/*    */ 
/*    */   
/*    */   ASN1Primitive toDLObject() {
/* 58 */     return (ASN1Primitive)this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   int encodedLength() throws IOException {
/* 64 */     return (getEncoded()).length;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/* 72 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/* 73 */     if (this.directReference != null)
/*    */     {
/* 75 */       byteArrayOutputStream.write(this.directReference.getEncoded("DL"));
/*    */     }
/* 77 */     if (this.indirectReference != null)
/*    */     {
/* 79 */       byteArrayOutputStream.write(this.indirectReference.getEncoded("DL"));
/*    */     }
/* 81 */     if (this.dataValueDescriptor != null)
/*    */     {
/* 83 */       byteArrayOutputStream.write(this.dataValueDescriptor.getEncoded("DL"));
/*    */     }
/* 85 */     DLTaggedObject dLTaggedObject = new DLTaggedObject(true, this.encoding, (ASN1Encodable)this.externalContent);
/* 86 */     byteArrayOutputStream.write(dLTaggedObject.getEncoded("DL"));
/*    */     
/* 88 */     paramASN1OutputStream.writeEncoded(paramBoolean, 32, 8, byteArrayOutputStream.toByteArray());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/DLExternal.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */