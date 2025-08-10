package net.miginfocom.examples;

import java.awt.LayoutManager;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import net.miginfocom.swing.MigLayout;

public class Example01 {
  private static JPanel createPanel() {
    JPanel jPanel = new JPanel((LayoutManager)new MigLayout());
    jPanel.add(new JLabel("First Name"));
    jPanel.add(new JTextField(10));
    jPanel.add(new JLabel("Surname"), "gap unrelated");
    jPanel.add(new JTextField(10), "wrap");
    jPanel.add(new JLabel("Address"));
    jPanel.add(new JTextField(), "span, grow");
    return jPanel;
  }
  
  public static void main(String[] paramArrayOfString) {
    SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            try {
              UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception exception) {
              exception.printStackTrace();
            } 
            JFrame jFrame = new JFrame("Example 01");
            jFrame.getContentPane().add(Example01.createPanel());
            jFrame.pack();
            jFrame.setDefaultCloseOperation(3);
            jFrame.setVisible(true);
          }
        });
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/miginfocom/examples/Example01.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */