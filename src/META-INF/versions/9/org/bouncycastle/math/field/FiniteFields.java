/*    */ package META-INF.versions.9.org.bouncycastle.math.field;
/*    */ import java.math.BigInteger;
/*    */ import org.bouncycastle.math.field.FiniteField;
/*    */ import org.bouncycastle.math.field.PrimeField;
/*    */ 
/*    */ public abstract class FiniteFields {
/*  7 */   static final FiniteField GF_2 = (FiniteField)new PrimeField(BigInteger.valueOf(2L));
/*  8 */   static final FiniteField GF_3 = (FiniteField)new PrimeField(BigInteger.valueOf(3L));
/*    */ 
/*    */   
/*    */   public static PolynomialExtensionField getBinaryExtensionField(int[] paramArrayOfint) {
/* 12 */     if (paramArrayOfint[0] != 0)
/*    */     {
/* 14 */       throw new IllegalArgumentException("Irreducible polynomials in GF(2) must have constant term");
/*    */     }
/* 16 */     for (byte b = 1; b < paramArrayOfint.length; b++) {
/*    */       
/* 18 */       if (paramArrayOfint[b] <= paramArrayOfint[b - 1])
/*    */       {
/* 20 */         throw new IllegalArgumentException("Polynomial exponents must be monotonically increasing");
/*    */       }
/*    */     } 
/*    */     
/* 24 */     return (PolynomialExtensionField)new GenericPolynomialExtensionField(GF_2, (Polynomial)new GF2Polynomial(paramArrayOfint));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static FiniteField getPrimeField(BigInteger paramBigInteger) {
/* 34 */     int i = paramBigInteger.bitLength();
/* 35 */     if (paramBigInteger.signum() <= 0 || i < 2)
/*    */     {
/* 37 */       throw new IllegalArgumentException("'characteristic' must be >= 2");
/*    */     }
/*    */     
/* 40 */     if (i < 3)
/*    */     {
/* 42 */       switch (paramBigInteger.intValue()) {
/*    */         
/*    */         case 2:
/* 45 */           return GF_2;
/*    */         case 3:
/* 47 */           return GF_3;
/*    */       } 
/*    */     
/*    */     }
/* 51 */     return (FiniteField)new PrimeField(paramBigInteger);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/field/FiniteFields.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */