/*    */ package META-INF.versions.9.org.bouncycastle.asn1;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.bouncycastle.asn1.ASN1Encodable;
/*    */ import org.bouncycastle.asn1.ASN1Primitive;
/*    */ import org.bouncycastle.asn1.ASN1SequenceParser;
/*    */ import org.bouncycastle.asn1.ASN1StreamParser;
/*    */ import org.bouncycastle.asn1.BERSequence;
/*    */ 
/*    */ public class BERSequenceParser
/*    */   implements ASN1SequenceParser {
/*    */   private ASN1StreamParser _parser;
/*    */   
/*    */   BERSequenceParser(ASN1StreamParser paramASN1StreamParser) {
/* 15 */     this._parser = paramASN1StreamParser;
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
/*    */   public ASN1Encodable readObject() throws IOException {
/* 27 */     return this._parser.readObject();
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
/* 39 */     return (ASN1Primitive)new BERSequence(this._parser.readVector());
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
/* 55 */       throw new IllegalStateException(iOException.getMessage());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/BERSequenceParser.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */