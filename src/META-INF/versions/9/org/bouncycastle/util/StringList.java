package META-INF.versions.9.org.bouncycastle.util;

import org.bouncycastle.util.Iterable;

public interface StringList extends Iterable<String> {
  boolean add(String paramString);
  
  String get(int paramInt);
  
  int size();
  
  String[] toStringArray();
  
  String[] toStringArray(int paramInt1, int paramInt2);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/util/StringList.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */