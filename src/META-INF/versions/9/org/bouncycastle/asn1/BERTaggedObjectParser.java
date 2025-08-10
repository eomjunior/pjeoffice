/*    */ package META-INF.versions.9.org.bouncycastle.asn1;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.bouncycastle.asn1.ASN1Encodable;
/*    */ import org.bouncycastle.asn1.ASN1ParsingException;
/*    */ import org.bouncycastle.asn1.ASN1Primitive;
/*    */ import org.bouncycastle.asn1.ASN1StreamParser;
/*    */ import org.bouncycastle.asn1.ASN1TaggedObjectParser;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BERTaggedObjectParser
/*    */   implements ASN1TaggedObjectParser
/*    */ {
/*    */   private boolean _constructed;
/*    */   private int _tagNumber;
/*    */   private ASN1StreamParser _parser;
/*    */   
/*    */   BERTaggedObjectParser(boolean paramBoolean, int paramInt, ASN1StreamParser paramASN1StreamParser) {
/* 20 */     this._constructed = paramBoolean;
/* 21 */     this._tagNumber = paramInt;
/* 22 */     this._parser = paramASN1StreamParser;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isConstructed() {
/* 32 */     return this._constructed;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getTagNo() {
/* 42 */     return this._tagNumber;
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
/*    */ 
/*    */   
/*    */   public ASN1Encodable getObjectParser(int paramInt, boolean paramBoolean) throws IOException {
/* 58 */     if (paramBoolean) {
/*    */       
/* 60 */       if (!this._constructed)
/*    */       {
/* 62 */         throw new IOException("Explicit tags must be constructed (see X.690 8.14.2)");
/*    */       }
/* 64 */       return this._parser.readObject();
/*    */     } 
/*    */     
/* 67 */     return this._parser.readImplicit(this._constructed, paramInt);
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
/* 79 */     return this._parser.readTaggedObject(this._constructed, this._tagNumber);
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
/* 91 */       return getLoadedObject();
/*    */     }
/* 93 */     catch (IOException iOException) {
/*    */       
/* 95 */       throw new ASN1ParsingException(iOException.getMessage());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/BERTaggedObjectParser.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */