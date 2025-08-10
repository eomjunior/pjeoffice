/*    */ package META-INF.versions.9.org.bouncycastle.pqc.jcajce.spec;
/*    */ 
/*    */ import java.security.spec.KeySpec;
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
/*    */ public class RainbowPublicKeySpec
/*    */   implements KeySpec
/*    */ {
/*    */   private short[][] coeffquadratic;
/*    */   private short[][] coeffsingular;
/*    */   private short[] coeffscalar;
/*    */   private int docLength;
/*    */   
/*    */   public RainbowPublicKeySpec(int paramInt, short[][] paramArrayOfshort1, short[][] paramArrayOfshort2, short[] paramArrayOfshort) {
/* 31 */     this.docLength = paramInt;
/* 32 */     this.coeffquadratic = paramArrayOfshort1;
/* 33 */     this.coeffsingular = paramArrayOfshort2;
/* 34 */     this.coeffscalar = paramArrayOfshort;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getDocLength() {
/* 42 */     return this.docLength;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public short[][] getCoeffQuadratic() {
/* 50 */     return this.coeffquadratic;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public short[][] getCoeffSingular() {
/* 58 */     return this.coeffsingular;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public short[] getCoeffScalar() {
/* 66 */     return this.coeffscalar;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/jcajce/spec/RainbowPublicKeySpec.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */