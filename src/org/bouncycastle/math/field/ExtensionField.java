package org.bouncycastle.math.field;

public interface ExtensionField extends FiniteField {
  FiniteField getSubfield();
  
  int getDegree();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/math/field/ExtensionField.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */