/*    */ package META-INF.versions.9.org.bouncycastle.asn1;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.bouncycastle.asn1.ASN1OctetStringParser;
/*    */ import org.bouncycastle.asn1.ASN1ParsingException;
/*    */ import org.bouncycastle.asn1.ASN1Primitive;
/*    */ import org.bouncycastle.asn1.DEROctetString;
/*    */ import org.bouncycastle.asn1.DefiniteLengthInputStream;
/*    */ 
/*    */ public class DEROctetStringParser
/*    */   implements ASN1OctetStringParser
/*    */ {
/*    */   private DefiniteLengthInputStream stream;
/*    */   
/*    */   DEROctetStringParser(DefiniteLengthInputStream paramDefiniteLengthInputStream) {
/* 17 */     this.stream = paramDefiniteLengthInputStream;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public InputStream getOctetStream() {
/* 27 */     return (InputStream)this.stream;
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
/*    */   public ASN1Primitive getLoadedObject() throws IOException {
/* 39 */     return (ASN1Primitive)new DEROctetString(this.stream.toByteArray());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ASN1Primitive toASN1Primitive() {
/*    */     try {
/* 51 */       return getLoadedObject();
/*    */     }
/* 53 */     catch (IOException iOException) {
/*    */       
/* 55 */       throw new ASN1ParsingException("IOException converting stream to byte array: " + iOException.getMessage(), iOException);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/DEROctetStringParser.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */