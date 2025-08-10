package net.miginfocom.examples;

import net.miginfocom.layout.AC;

public class TestMigLayout {
  public static void main(String[] paramArrayOfString) {
    AC aC1 = (new AC()).size(" ").gap().size(" ");
    AC aC2 = (new AC()).size(":").gap().size(":");
    AC aC3 = (new AC()).size("").gap().size("");
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/miginfocom/examples/TestMigLayout.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */