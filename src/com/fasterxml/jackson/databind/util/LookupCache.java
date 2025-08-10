package com.fasterxml.jackson.databind.util;

public interface LookupCache<K, V> {
  int size();
  
  V get(Object paramObject);
  
  V put(K paramK, V paramV);
  
  V putIfAbsent(K paramK, V paramV);
  
  void clear();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/databind/util/LookupCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */