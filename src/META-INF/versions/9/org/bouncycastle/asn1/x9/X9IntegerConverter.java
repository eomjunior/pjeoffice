/*    */ package META-INF.versions.9.org.bouncycastle.asn1.x9;
/*    */ 
/*    */ import java.math.BigInteger;
/*    */ import org.bouncycastle.math.ec.ECCurve;
/*    */ import org.bouncycastle.math.ec.ECFieldElement;
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
/*    */ public class X9IntegerConverter
/*    */ {
/*    */   public int getByteLength(ECCurve paramECCurve) {
/* 23 */     return (paramECCurve.getFieldSize() + 7) / 8;
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
/*    */   public int getByteLength(ECFieldElement paramECFieldElement) {
/* 35 */     return (paramECFieldElement.getFieldSize() + 7) / 8;
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
/*    */   public byte[] integerToBytes(BigInteger paramBigInteger, int paramInt) {
/* 49 */     byte[] arrayOfByte = paramBigInteger.toByteArray();
/*    */     
/* 51 */     if (paramInt < arrayOfByte.length) {
/*    */       
/* 53 */       byte[] arrayOfByte1 = new byte[paramInt];
/*    */       
/* 55 */       System.arraycopy(arrayOfByte, arrayOfByte.length - arrayOfByte1.length, arrayOfByte1, 0, arrayOfByte1.length);
/*    */       
/* 57 */       return arrayOfByte1;
/*    */     } 
/* 59 */     if (paramInt > arrayOfByte.length) {
/*    */       
/* 61 */       byte[] arrayOfByte1 = new byte[paramInt];
/*    */       
/* 63 */       System.arraycopy(arrayOfByte, 0, arrayOfByte1, arrayOfByte1.length - arrayOfByte.length, arrayOfByte.length);
/*    */       
/* 65 */       return arrayOfByte1;
/*    */     } 
/*    */     
/* 68 */     return arrayOfByte;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/x9/X9IntegerConverter.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */