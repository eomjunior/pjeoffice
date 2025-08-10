package net.miginfocom.examples;

import java.awt.Color;
import java.awt.Component;
import java.awt.LayoutManager;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import net.miginfocom.swing.MigLayout;

public class BugTestApp {
  private static JPanel createPanel() {
    JPanel jPanel = new JPanel();
    jPanel.setBackground(new Color(200, 255, 200));
    jPanel.setLayout((LayoutManager)new MigLayout("debug"));
    JLabel jLabel = new JLabel("Qwerty");
    jLabel.setFont(jLabel.getFont().deriveFont(30.0F));
    jPanel.add(jLabel, "pos 0%+5 0%+5 50%-5  50%-5");
    jPanel.add(new JTextField("Qwerty"));
    JFrame jFrame = new JFrame();
    jFrame.setDefaultCloseOperation(3);
    jFrame.setLayout((LayoutManager)new MigLayout());
    jFrame.add(jPanel, "w 400, h 100");
    jFrame.setLocationRelativeTo((Component)null);
    jFrame.pack();
    jFrame.setVisible(true);
    return null;
  }
  
  private static JPanel createPanel2() {
    JFrame jFrame = new JFrame();
    jFrame.setLayout((LayoutManager)new MigLayout("debug, fillx", "", ""));
    jFrame.add(new JTextField(), "span 2, grow, wrap");
    jFrame.add(new JTextField(10));
    jFrame.add(new JLabel("End"));
    jFrame.setSize(600, 400);
    jFrame.setDefaultCloseOperation(3);
    jFrame.setVisible(true);
    return null;
  }
  
  public static void main(String[] paramArrayOfString) {
    SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            try {
              UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception exception) {
              exception.printStackTrace();
            } 
            BugTestApp.createPanel();
          }
        });
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/miginfocom/examples/BugTestApp.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */