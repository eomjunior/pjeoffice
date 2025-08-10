package net.miginfocom.examples;

import java.awt.LayoutManager;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import net.miginfocom.swing.MigLayout;

public class Example02 {
  private static JPanel createPanel() {
    JPanel jPanel = new JPanel((LayoutManager)new MigLayout());
    jPanel.add(createLabel("West Panel"), "dock west");
    jPanel.add(createLabel("North 1 Panel"), "dock north");
    jPanel.add(createLabel("North 2 Panel"), "dock north");
    jPanel.add(createLabel("South Panel"), "dock south");
    jPanel.add(createLabel("East Panel"), "dock east");
    jPanel.add(createLabel("Center Panel"), "grow, push");
    return jPanel;
  }
  
  private static JLabel createLabel(String paramString) {
    JLabel jLabel = new JLabel(paramString);
    jLabel.setHorizontalAlignment(0);
    jLabel.setBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(5, 10, 5, 10)));
    return jLabel;
  }
  
  public static void main(String[] paramArrayOfString) {
    SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            try {
              UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception exception) {
              exception.printStackTrace();
            } 
            JFrame jFrame = new JFrame("Example 02");
            jFrame.getContentPane().add(Example02.createPanel());
            jFrame.pack();
            jFrame.setDefaultCloseOperation(3);
            jFrame.setVisible(true);
          }
        });
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/miginfocom/examples/Example02.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */