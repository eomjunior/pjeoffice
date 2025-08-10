/*    */ package META-INF.versions.9.org.bouncycastle.asn1;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.bouncycastle.asn1.ASN1Null;
/*    */ import org.bouncycastle.asn1.ASN1OutputStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DERNull
/*    */   extends ASN1Null
/*    */ {
/* 13 */   public static final org.bouncycastle.asn1.DERNull INSTANCE = new org.bouncycastle.asn1.DERNull();
/*    */   
/* 15 */   private static final byte[] zeroBytes = new byte[0];
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   boolean isConstructed() {
/* 23 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   int encodedLength() {
/* 28 */     return 2;
/*    */   }
/*    */ 
/*    */   
/*    */   void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/* 33 */     paramASN1OutputStream.writeEncoded(paramBoolean, 5, zeroBytes);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/DERNull.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */