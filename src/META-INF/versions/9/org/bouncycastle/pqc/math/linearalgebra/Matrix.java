/*    */ package META-INF.versions.9.org.bouncycastle.pqc.math.linearalgebra;
/*    */ 
/*    */ import org.bouncycastle.pqc.math.linearalgebra.Permutation;
/*    */ import org.bouncycastle.pqc.math.linearalgebra.Vector;
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
/*    */ public abstract class Matrix
/*    */ {
/*    */   protected int numRows;
/*    */   protected int numColumns;
/*    */   public static final char MATRIX_TYPE_ZERO = 'Z';
/*    */   public static final char MATRIX_TYPE_UNIT = 'I';
/*    */   public static final char MATRIX_TYPE_RANDOM_LT = 'L';
/*    */   public static final char MATRIX_TYPE_RANDOM_UT = 'U';
/*    */   public static final char MATRIX_TYPE_RANDOM_REGULAR = 'R';
/*    */   
/*    */   public int getNumRows() {
/* 58 */     return this.numRows;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getNumColumns() {
/* 66 */     return this.numColumns;
/*    */   }
/*    */   
/*    */   public abstract byte[] getEncoded();
/*    */   
/*    */   public abstract org.bouncycastle.pqc.math.linearalgebra.Matrix computeInverse();
/*    */   
/*    */   public abstract boolean isZero();
/*    */   
/*    */   public abstract org.bouncycastle.pqc.math.linearalgebra.Matrix rightMultiply(org.bouncycastle.pqc.math.linearalgebra.Matrix paramMatrix);
/*    */   
/*    */   public abstract org.bouncycastle.pqc.math.linearalgebra.Matrix rightMultiply(Permutation paramPermutation);
/*    */   
/*    */   public abstract Vector leftMultiply(Vector paramVector);
/*    */   
/*    */   public abstract Vector rightMultiply(Vector paramVector);
/*    */   
/*    */   public abstract String toString();
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/math/linearalgebra/Matrix.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */