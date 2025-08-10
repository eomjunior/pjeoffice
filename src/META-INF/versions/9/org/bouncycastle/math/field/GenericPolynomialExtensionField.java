/*    */ package META-INF.versions.9.org.bouncycastle.math.field;
/*    */ 
/*    */ import java.math.BigInteger;
/*    */ import org.bouncycastle.math.field.FiniteField;
/*    */ import org.bouncycastle.math.field.Polynomial;
/*    */ import org.bouncycastle.math.field.PolynomialExtensionField;
/*    */ import org.bouncycastle.util.Integers;
/*    */ 
/*    */ class GenericPolynomialExtensionField implements PolynomialExtensionField {
/*    */   protected final FiniteField subfield;
/*    */   protected final Polynomial minimalPolynomial;
/*    */   
/*    */   GenericPolynomialExtensionField(FiniteField paramFiniteField, Polynomial paramPolynomial) {
/* 14 */     this.subfield = paramFiniteField;
/* 15 */     this.minimalPolynomial = paramPolynomial;
/*    */   }
/*    */ 
/*    */   
/*    */   public BigInteger getCharacteristic() {
/* 20 */     return this.subfield.getCharacteristic();
/*    */   }
/*    */ 
/*    */   
/*    */   public int getDimension() {
/* 25 */     return this.subfield.getDimension() * this.minimalPolynomial.getDegree();
/*    */   }
/*    */ 
/*    */   
/*    */   public FiniteField getSubfield() {
/* 30 */     return this.subfield;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getDegree() {
/* 35 */     return this.minimalPolynomial.getDegree();
/*    */   }
/*    */ 
/*    */   
/*    */   public Polynomial getMinimalPolynomial() {
/* 40 */     return this.minimalPolynomial;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 45 */     if (this == paramObject)
/*    */     {
/* 47 */       return true;
/*    */     }
/* 49 */     if (!(paramObject instanceof org.bouncycastle.math.field.GenericPolynomialExtensionField))
/*    */     {
/* 51 */       return false;
/*    */     }
/* 53 */     org.bouncycastle.math.field.GenericPolynomialExtensionField genericPolynomialExtensionField = (org.bouncycastle.math.field.GenericPolynomialExtensionField)paramObject;
/* 54 */     return (this.subfield.equals(genericPolynomialExtensionField.subfield) && this.minimalPolynomial.equals(genericPolynomialExtensionField.minimalPolynomial));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 59 */     return this.subfield.hashCode() ^ 
/* 60 */       Integers.rotateLeft(this.minimalPolynomial.hashCode(), 16);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/field/GenericPolynomialExtensionField.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */