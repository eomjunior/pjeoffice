/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.mceliece;
/*    */ 
/*    */ import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2KeyParameters;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class McElieceCCA2PublicKeyParameters
/*    */   extends McElieceCCA2KeyParameters
/*    */ {
/*    */   private int n;
/*    */   private int t;
/*    */   private GF2Matrix matrixG;
/*    */   
/*    */   public McElieceCCA2PublicKeyParameters(int paramInt1, int paramInt2, GF2Matrix paramGF2Matrix, String paramString) {
/* 31 */     super(false, paramString);
/*    */     
/* 33 */     this.n = paramInt1;
/* 34 */     this.t = paramInt2;
/* 35 */     this.matrixG = new GF2Matrix(paramGF2Matrix);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getN() {
/* 43 */     return this.n;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getT() {
/* 51 */     return this.t;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public GF2Matrix getG() {
/* 59 */     return this.matrixG;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getK() {
/* 67 */     return this.matrixG.getNumRows();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/mceliece/McElieceCCA2PublicKeyParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */