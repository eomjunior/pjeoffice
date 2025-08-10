package org.bouncycastle.pqc.crypto.qtesla;

final class IntSlicer {
  private final int[] values;
  
  private int base;
  
  IntSlicer(int[] paramArrayOfint, int paramInt) {
    this.values = paramArrayOfint;
    this.base = paramInt;
  }
  
  final int at(int paramInt) {
    return this.values[this.base + paramInt];
  }
  
  final int at(int paramInt1, int paramInt2) {
    this.values[this.base + paramInt1] = paramInt2;
    return paramInt2;
  }
  
  final int at(int paramInt, long paramLong) {
    this.values[this.base + paramInt] = (int)paramLong;
    return (int)paramLong;
  }
  
  final IntSlicer from(int paramInt) {
    return new IntSlicer(this.values, this.base + paramInt);
  }
  
  final void incBase(int paramInt) {
    this.base += paramInt;
  }
  
  final IntSlicer copy() {
    return new IntSlicer(this.values, this.base);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/crypto/qtesla/IntSlicer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */