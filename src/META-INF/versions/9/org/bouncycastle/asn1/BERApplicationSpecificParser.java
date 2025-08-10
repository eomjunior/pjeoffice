/*    */ package META-INF.versions.9.org.bouncycastle.asn1;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.bouncycastle.asn1.ASN1ApplicationSpecificParser;
/*    */ import org.bouncycastle.asn1.ASN1Encodable;
/*    */ import org.bouncycastle.asn1.ASN1ParsingException;
/*    */ import org.bouncycastle.asn1.ASN1Primitive;
/*    */ import org.bouncycastle.asn1.ASN1StreamParser;
/*    */ import org.bouncycastle.asn1.BERApplicationSpecific;
/*    */ 
/*    */ public class BERApplicationSpecificParser implements ASN1ApplicationSpecificParser {
/*    */   private final int tag;
/*    */   private final ASN1StreamParser parser;
/*    */   
/*    */   BERApplicationSpecificParser(int paramInt, ASN1StreamParser paramASN1StreamParser) {
/* 16 */     this.tag = paramInt;
/* 17 */     this.parser = paramASN1StreamParser;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ASN1Encodable readObject() throws IOException {
/* 28 */     return this.parser.readObject();
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
/* 40 */     return (ASN1Primitive)new BERApplicationSpecific(this.tag, this.parser.readVector());
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
/* 52 */       return getLoadedObject();
/*    */     }
/* 54 */     catch (IOException iOException) {
/*    */       
/* 56 */       throw new ASN1ParsingException(iOException.getMessage(), iOException);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/BERApplicationSpecificParser.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */