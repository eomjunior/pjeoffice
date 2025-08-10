/*    */ package META-INF.versions.9.org.bouncycastle.asn1;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.bouncycastle.asn1.ASN1Encodable;
/*    */ import org.bouncycastle.asn1.ASN1OutputStream;
/*    */ import org.bouncycastle.asn1.ASN1Primitive;
/*    */ import org.bouncycastle.asn1.ASN1TaggedObject;
/*    */ import org.bouncycastle.asn1.StreamUtil;
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
/*    */ public class DERTaggedObject
/*    */   extends ASN1TaggedObject
/*    */ {
/*    */   public DERTaggedObject(boolean paramBoolean, int paramInt, ASN1Encodable paramASN1Encodable) {
/* 23 */     super(paramBoolean, paramInt, paramASN1Encodable);
/*    */   }
/*    */ 
/*    */   
/*    */   public DERTaggedObject(int paramInt, ASN1Encodable paramASN1Encodable) {
/* 28 */     super(true, paramInt, paramASN1Encodable);
/*    */   }
/*    */ 
/*    */   
/*    */   boolean isConstructed() {
/* 33 */     return (this.explicit || this.obj.toASN1Primitive().toDERObject().isConstructed());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   int encodedLength() throws IOException {
/* 39 */     ASN1Primitive aSN1Primitive = this.obj.toASN1Primitive().toDERObject();
/* 40 */     int i = aSN1Primitive.encodedLength();
/*    */     
/* 42 */     if (this.explicit)
/*    */     {
/* 44 */       return StreamUtil.calculateTagLength(this.tagNo) + StreamUtil.calculateBodyLength(i) + i;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 49 */     i--;
/*    */     
/* 51 */     return StreamUtil.calculateTagLength(this.tagNo) + i;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/* 57 */     ASN1Primitive aSN1Primitive = this.obj.toASN1Primitive().toDERObject();
/*    */     
/* 59 */     int i = 128;
/* 60 */     if (this.explicit || aSN1Primitive.isConstructed())
/*    */     {
/* 62 */       i |= 0x20;
/*    */     }
/*    */     
/* 65 */     paramASN1OutputStream.writeTag(paramBoolean, i, this.tagNo);
/*    */     
/* 67 */     if (this.explicit)
/*    */     {
/* 69 */       paramASN1OutputStream.writeLength(aSN1Primitive.encodedLength());
/*    */     }
/*    */     
/* 72 */     aSN1Primitive.encode((ASN1OutputStream)paramASN1OutputStream.getDERSubStream(), this.explicit);
/*    */   }
/*    */ 
/*    */   
/*    */   ASN1Primitive toDERObject() {
/* 77 */     return (ASN1Primitive)this;
/*    */   }
/*    */ 
/*    */   
/*    */   ASN1Primitive toDLObject() {
/* 82 */     return (ASN1Primitive)this;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/DERTaggedObject.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */