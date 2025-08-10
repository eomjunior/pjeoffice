/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.mceliece;
/*    */ 
/*    */ import org.bouncycastle.pqc.crypto.mceliece.McElieceKeyParameters;
/*    */ import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;
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
/*    */ public class McEliecePublicKeyParameters
/*    */   extends McElieceKeyParameters
/*    */ {
/*    */   private int n;
/*    */   private int t;
/*    */   private GF2Matrix g;
/*    */   
/*    */   public McEliecePublicKeyParameters(int paramInt1, int paramInt2, GF2Matrix paramGF2Matrix) {
/* 27 */     super(false, null);
/* 28 */     this.n = paramInt1;
/* 29 */     this.t = paramInt2;
/* 30 */     this.g = new GF2Matrix(paramGF2Matrix);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getN() {
/* 38 */     return this.n;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getT() {
/* 46 */     return this.t;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public GF2Matrix getG() {
/* 54 */     return this.g;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getK() {
/* 62 */     return this.g.getNumRows();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/mceliece/McEliecePublicKeyParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */