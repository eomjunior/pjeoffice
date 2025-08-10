/*    */ package META-INF.versions.9.org.bouncycastle.asn1;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.bouncycastle.asn1.ASN1OutputStream;
/*    */ import org.bouncycastle.asn1.ASN1Primitive;
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
/*    */ public abstract class ASN1Null
/*    */   extends ASN1Primitive
/*    */ {
/*    */   public static org.bouncycastle.asn1.ASN1Null getInstance(Object paramObject) {
/* 36 */     if (paramObject instanceof org.bouncycastle.asn1.ASN1Null)
/*    */     {
/* 38 */       return (org.bouncycastle.asn1.ASN1Null)paramObject;
/*    */     }
/*    */     
/* 41 */     if (paramObject != null) {
/*    */       
/*    */       try {
/*    */         
/* 45 */         return getInstance(ASN1Primitive.fromByteArray((byte[])paramObject));
/*    */       }
/* 47 */       catch (IOException iOException) {
/*    */         
/* 49 */         throw new IllegalArgumentException("failed to construct NULL from byte[]: " + iOException.getMessage());
/*    */       }
/* 51 */       catch (ClassCastException classCastException) {
/*    */         
/* 53 */         throw new IllegalArgumentException("unknown object in getInstance(): " + paramObject.getClass().getName());
/*    */       } 
/*    */     }
/*    */     
/* 57 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 62 */     return -1;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   boolean asn1Equals(ASN1Primitive paramASN1Primitive) {
/* 68 */     if (!(paramASN1Primitive instanceof org.bouncycastle.asn1.ASN1Null))
/*    */     {
/* 70 */       return false;
/*    */     }
/*    */     
/* 73 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   abstract void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException;
/*    */   
/*    */   public String toString() {
/* 80 */     return "NULL";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/ASN1Null.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */