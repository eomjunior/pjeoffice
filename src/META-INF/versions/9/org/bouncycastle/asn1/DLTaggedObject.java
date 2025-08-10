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
/*    */ public class DLTaggedObject
/*    */   extends ASN1TaggedObject
/*    */ {
/*    */   public DLTaggedObject(boolean paramBoolean, int paramInt, ASN1Encodable paramASN1Encodable) {
/* 23 */     super(paramBoolean, paramInt, paramASN1Encodable);
/*    */   }
/*    */ 
/*    */   
/*    */   boolean isConstructed() {
/* 28 */     return (this.explicit || this.obj.toASN1Primitive().toDLObject().isConstructed());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   int encodedLength() throws IOException {
/* 34 */     int i = this.obj.toASN1Primitive().toDLObject().encodedLength();
/*    */     
/* 36 */     if (this.explicit)
/*    */     {
/* 38 */       return StreamUtil.calculateTagLength(this.tagNo) + StreamUtil.calculateBodyLength(i) + i;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 43 */     i--;
/*    */     
/* 45 */     return StreamUtil.calculateTagLength(this.tagNo) + i;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/* 51 */     ASN1Primitive aSN1Primitive = this.obj.toASN1Primitive().toDLObject();
/*    */     
/* 53 */     int i = 128;
/* 54 */     if (this.explicit || aSN1Primitive.isConstructed())
/*    */     {
/* 56 */       i |= 0x20;
/*    */     }
/*    */     
/* 59 */     paramASN1OutputStream.writeTag(paramBoolean, i, this.tagNo);
/*    */     
/* 61 */     if (this.explicit)
/*    */     {
/* 63 */       paramASN1OutputStream.writeLength(aSN1Primitive.encodedLength());
/*    */     }
/*    */     
/* 66 */     paramASN1OutputStream.getDLSubStream().writePrimitive(aSN1Primitive, this.explicit);
/*    */   }
/*    */ 
/*    */   
/*    */   ASN1Primitive toDLObject() {
/* 71 */     return (ASN1Primitive)this;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/DLTaggedObject.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */