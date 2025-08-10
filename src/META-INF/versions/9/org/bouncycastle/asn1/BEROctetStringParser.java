/*    */ package META-INF.versions.9.org.bouncycastle.asn1;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.bouncycastle.asn1.ASN1OctetStringParser;
/*    */ import org.bouncycastle.asn1.ASN1ParsingException;
/*    */ import org.bouncycastle.asn1.ASN1Primitive;
/*    */ import org.bouncycastle.asn1.ASN1StreamParser;
/*    */ import org.bouncycastle.asn1.BEROctetString;
/*    */ import org.bouncycastle.asn1.ConstructedOctetStream;
/*    */ import org.bouncycastle.util.io.Streams;
/*    */ 
/*    */ public class BEROctetStringParser
/*    */   implements ASN1OctetStringParser
/*    */ {
/*    */   private ASN1StreamParser _parser;
/*    */   
/*    */   BEROctetStringParser(ASN1StreamParser paramASN1StreamParser) {
/* 19 */     this._parser = paramASN1StreamParser;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public InputStream getOctetStream() {
/* 29 */     return (InputStream)new ConstructedOctetStream(this._parser);
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
/* 41 */     return (ASN1Primitive)new BEROctetString(Streams.readAll(getOctetStream()));
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
/* 53 */       return getLoadedObject();
/*    */     }
/* 55 */     catch (IOException iOException) {
/*    */       
/* 57 */       throw new ASN1ParsingException("IOException converting stream to byte array: " + iOException.getMessage(), iOException);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/BEROctetStringParser.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */