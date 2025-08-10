package net.miginfocom.examples;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import net.miginfocom.swing.MigLayout;

public class JavaOneShrink {
  private static JComponent createPanel(String... paramVarArgs) {
    JPanel jPanel = new JPanel((LayoutManager)new MigLayout("nogrid"));
    jPanel.add(createTextArea(paramVarArgs[0].replace(", ", "\n    ")), paramVarArgs[0] + ", w 200");
    jPanel.add(createTextArea(paramVarArgs[1].replace(", ", "\n    ")), paramVarArgs[1] + ", w 200");
    jPanel.add(createTextArea(paramVarArgs[2].replace(", ", "\n    ")), paramVarArgs[2] + ", w 200");
    jPanel.add(createTextArea(paramVarArgs[3].replace(", ", "\n    ")), paramVarArgs[3] + ", w 200");
    JSplitPane jSplitPane = new JSplitPane(1, true, jPanel, new JPanel());
    jSplitPane.setOpaque(true);
    jSplitPane.setBorder((Border)null);
    return jSplitPane;
  }
  
  private static JComponent createTextArea(String paramString) {
    JTextArea jTextArea = new JTextArea("\n\n    " + paramString, 6, 20);
    jTextArea.setBorder(new LineBorder(new Color(200, 200, 200)));
    jTextArea.setFont(new Font("Helvetica", 1, 20));
    jTextArea.setMinimumSize(new Dimension(20, 20));
    jTextArea.setFocusable(false);
    return jTextArea;
  }
  
  public static void main(String[] paramArrayOfString) {
    SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            try {
              UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception exception) {
              exception.printStackTrace();
            } 
            JFrame jFrame = new JFrame("JavaOne Shrink Demo");
            Container container = jFrame.getContentPane();
            container.setLayout((LayoutManager)new MigLayout("wrap 1"));
            container.add(JavaOneShrink.createPanel(new String[] { "", "", "", "" }));
            container.add(JavaOneShrink.createPanel(new String[] { "shrinkprio 1", "shrinkprio 1", "shrinkprio 2", "shrinkprio 3" }));
            container.add(JavaOneShrink.createPanel(new String[] { "shrink 25", "shrink 50", "shrink 75", "shrink 100" }));
            container.add(JavaOneShrink.createPanel(new String[] { "shrinkprio 1, shrink 50", "shrinkprio 1, shrink 100", "shrinkprio 2, shrink 50", "shrinkprio 2, shrink 100" }));
            jFrame.pack();
            jFrame.setDefaultCloseOperation(3);
            jFrame.setLocationRelativeTo(null);
            jFrame.setVisible(true);
          }
        });
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/miginfocom/examples/JavaOneShrink.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */