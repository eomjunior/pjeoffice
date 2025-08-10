/*    */ package META-INF.versions.9.org.bouncycastle.asn1;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Enumeration;
/*    */ import java.util.NoSuchElementException;
/*    */ import org.bouncycastle.asn1.ASN1InputStream;
/*    */ import org.bouncycastle.asn1.ASN1ParsingException;
/*    */ 
/*    */ class LazyConstructionEnumeration
/*    */   implements Enumeration {
/*    */   private ASN1InputStream aIn;
/*    */   private Object nextObj;
/*    */   
/*    */   public LazyConstructionEnumeration(byte[] paramArrayOfbyte) {
/* 15 */     this.aIn = new ASN1InputStream(paramArrayOfbyte, true);
/* 16 */     this.nextObj = readObject();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasMoreElements() {
/* 21 */     return (this.nextObj != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object nextElement() {
/* 26 */     if (this.nextObj != null) {
/*    */       
/* 28 */       Object object = this.nextObj;
/* 29 */       this.nextObj = readObject();
/* 30 */       return object;
/*    */     } 
/* 32 */     throw new NoSuchElementException();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private Object readObject() {
/*    */     try {
/* 39 */       return this.aIn.readObject();
/*    */     }
/* 41 */     catch (IOException iOException) {
/*    */       
/* 43 */       throw new ASN1ParsingException("malformed DER construction: " + iOException, iOException);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/LazyConstructionEnumeration.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */