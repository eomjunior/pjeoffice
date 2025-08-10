/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.rainbow;
/*    */ 
/*    */ import org.bouncycastle.pqc.crypto.rainbow.RainbowKeyParameters;
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
/*    */ public class RainbowPublicKeyParameters
/*    */   extends RainbowKeyParameters
/*    */ {
/*    */   private short[][] coeffquadratic;
/*    */   private short[][] coeffsingular;
/*    */   private short[] coeffscalar;
/*    */   
/*    */   public RainbowPublicKeyParameters(int paramInt, short[][] paramArrayOfshort1, short[][] paramArrayOfshort2, short[] paramArrayOfshort) {
/* 22 */     super(false, paramInt);
/*    */     
/* 24 */     this.coeffquadratic = paramArrayOfshort1;
/* 25 */     this.coeffsingular = paramArrayOfshort2;
/* 26 */     this.coeffscalar = paramArrayOfshort;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public short[][] getCoeffQuadratic() {
/* 35 */     return this.coeffquadratic;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public short[][] getCoeffSingular() {
/* 43 */     return this.coeffsingular;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public short[] getCoeffScalar() {
/* 51 */     return this.coeffscalar;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/rainbow/RainbowPublicKeyParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */