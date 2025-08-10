/*    */ package META-INF.versions.9.org.bouncycastle.asn1;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.bouncycastle.asn1.ASN1Encodable;
/*    */ import org.bouncycastle.asn1.ASN1Exception;
/*    */ import org.bouncycastle.asn1.ASN1ParsingException;
/*    */ import org.bouncycastle.asn1.ASN1Primitive;
/*    */ import org.bouncycastle.asn1.ASN1StreamParser;
/*    */ import org.bouncycastle.asn1.DLExternal;
/*    */ import org.bouncycastle.asn1.InMemoryRepresentable;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DERExternalParser
/*    */   implements ASN1Encodable, InMemoryRepresentable
/*    */ {
/*    */   private ASN1StreamParser _parser;
/*    */   
/*    */   public DERExternalParser(ASN1StreamParser paramASN1StreamParser) {
/* 20 */     this._parser = paramASN1StreamParser;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ASN1Encodable readObject() throws IOException {
/* 26 */     return this._parser.readObject();
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
/*    */   public ASN1Primitive getLoadedObject() throws IOException {
/*    */     try {
/* 40 */       return (ASN1Primitive)new DLExternal(this._parser.readVector());
/*    */     }
/* 42 */     catch (IllegalArgumentException illegalArgumentException) {
/*    */       
/* 44 */       throw new ASN1Exception(illegalArgumentException.getMessage(), illegalArgumentException);
/*    */     } 
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
/* 57 */       return getLoadedObject();
/*    */     }
/* 59 */     catch (IOException iOException) {
/*    */       
/* 61 */       throw new ASN1ParsingException("unable to get DER object", iOException);
/*    */     }
/* 63 */     catch (IllegalArgumentException illegalArgumentException) {
/*    */       
/* 65 */       throw new ASN1ParsingException("unable to get DER object", illegalArgumentException);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/DERExternalParser.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */