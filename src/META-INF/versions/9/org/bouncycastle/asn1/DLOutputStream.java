/*    */ package META-INF.versions.9.org.bouncycastle.asn1;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import org.bouncycastle.asn1.ASN1OutputStream;
/*    */ import org.bouncycastle.asn1.ASN1Primitive;
/*    */ 
/*    */ 
/*    */ 
/*    */ class DLOutputStream
/*    */   extends ASN1OutputStream
/*    */ {
/*    */   DLOutputStream(OutputStream paramOutputStream) {
/* 14 */     super(paramOutputStream);
/*    */   }
/*    */ 
/*    */   
/*    */   void writePrimitive(ASN1Primitive paramASN1Primitive, boolean paramBoolean) throws IOException {
/* 19 */     paramASN1Primitive.toDLObject().encode(this, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   ASN1OutputStream getDLSubStream() {
/* 24 */     return this;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/DLOutputStream.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */