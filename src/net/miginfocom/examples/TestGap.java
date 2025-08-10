package net.miginfocom.examples;

import java.awt.LayoutManager;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import net.miginfocom.swing.MigLayout;

public class TestGap {
  public static void main(String[] paramArrayOfString) throws Exception {
    JFrame jFrame = new JFrame();
    jFrame.setLayout((LayoutManager)new MigLayout("debug"));
    JTable jTable = new JTable(10, 10);
    jFrame.add(new JScrollPane(jTable));
    jFrame.pack();
    jFrame.setDefaultCloseOperation(2);
    jFrame.setVisible(true);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/miginfocom/examples/TestGap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */