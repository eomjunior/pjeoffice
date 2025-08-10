package com.google.common.hash;

@ElementTypesAreNonnullByDefault
interface LongAddable {
  void increment();
  
  void add(long paramLong);
  
  long sum();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/hash/LongAddable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */