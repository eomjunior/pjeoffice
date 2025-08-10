package org.bouncycastle.tsp;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.util.Arrays;

public class DataGroup {
  private List<byte[]> dataObjects;
  
  private byte[] groupHash;
  
  private TreeSet<byte[]> hashes;
  
  public DataGroup(List<byte[]> paramList) {
    this.dataObjects = paramList;
  }
  
  public DataGroup(byte[] paramArrayOfbyte) {
    this.dataObjects = (List)new ArrayList<byte>();
    this.dataObjects.add(paramArrayOfbyte);
  }
  
  public TreeSet<byte[]> getHashes(DigestCalculator paramDigestCalculator) {
    return getHashes(paramDigestCalculator, null);
  }
  
  private TreeSet<byte[]> getHashes(DigestCalculator paramDigestCalculator, byte[] paramArrayOfbyte) {
    if (this.hashes == null) {
      this.hashes = (TreeSet)new TreeSet<byte>(new ByteArrayComparator());
      for (byte b = 0; b != this.dataObjects.size(); b++) {
        byte[] arrayOfByte = this.dataObjects.get(b);
        if (paramArrayOfbyte != null) {
          this.hashes.add(calcDigest(paramDigestCalculator, Arrays.concatenate(calcDigest(paramDigestCalculator, arrayOfByte), paramArrayOfbyte)));
        } else {
          this.hashes.add(calcDigest(paramDigestCalculator, arrayOfByte));
        } 
      } 
    } 
    return this.hashes;
  }
  
  public byte[] getHash(DigestCalculator paramDigestCalculator) {
    if (this.groupHash == null) {
      TreeSet<byte[]> treeSet = getHashes(paramDigestCalculator);
      if (treeSet.size() > 1) {
        byte[] arrayOfByte = new byte[0];
        Iterator<byte> iterator = treeSet.iterator();
        while (iterator.hasNext())
          arrayOfByte = Arrays.concatenate(arrayOfByte, (byte[])iterator.next()); 
        this.groupHash = calcDigest(paramDigestCalculator, arrayOfByte);
      } else {
        this.groupHash = treeSet.first();
      } 
    } 
    return this.groupHash;
  }
  
  static byte[] calcDigest(DigestCalculator paramDigestCalculator, byte[] paramArrayOfbyte) {
    try {
      OutputStream outputStream = paramDigestCalculator.getOutputStream();
      outputStream.write(paramArrayOfbyte);
      outputStream.close();
      return paramDigestCalculator.getDigest();
    } catch (IOException iOException) {
      throw new IllegalStateException("digest calculator failure: " + iOException.getMessage());
    } 
  }
  
  private class ByteArrayComparator implements Comparator {
    private ByteArrayComparator() {}
    
    public int compare(Object param1Object1, Object param1Object2) {
      byte[] arrayOfByte1 = (byte[])param1Object1;
      byte[] arrayOfByte2 = (byte[])param1Object2;
      int i = (arrayOfByte1.length < arrayOfByte2.length) ? arrayOfByte1.length : arrayOfByte2.length;
      for (int j = 0; j != i; j++) {
        int k = arrayOfByte1[j] & 0xFF;
        int m = arrayOfByte2[j] & 0xFF;
        if (k != m)
          return k - m; 
      } 
      return arrayOfByte1.length - arrayOfByte2.length;
    }
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/tsp/DataGroup.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */