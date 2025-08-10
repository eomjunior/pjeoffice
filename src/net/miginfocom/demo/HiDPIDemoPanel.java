package net.miginfocom.demo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import javax.swing.AbstractListModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import net.miginfocom.layout.PlatformDefaults;
import net.miginfocom.swing.MigLayout;

class HiDPIDemoPanel extends JPanel {
  public HiDPIDemoPanel() {
    super((LayoutManager)new MigLayout());
    JLabel jLabel1 = new JLabel("A Small Label:");
    JTextField jTextField1 = new JTextField(10);
    JButton jButton1 = new JButton("Cancel");
    JButton jButton2 = new JButton("OK");
    JButton jButton3 = new JButton("Help");
    JList jList = new JList();
    JLabel jLabel2 = new JLabel("Label:");
    JTextField jTextField2 = new JTextField(10);
    JLabel jLabel3 = new JLabel("This is another section");
    JSeparator jSeparator = new JSeparator();
    JTextArea jTextArea = new JTextArea("Some general text that takes place, doesn't offend anyone and fills some pixels.", 3, 30);
    JLabel jLabel4 = new JLabel("Some Text Area");
    JLabel jLabel5 = new JLabel("Some List:");
    JComboBox jComboBox = new JComboBox();
    JCheckBox jCheckBox = new JCheckBox("Orange");
    JScrollBar jScrollBar1 = new JScrollBar(1);
    JScrollBar jScrollBar2 = new JScrollBar(0, 30, 40, 0, 100);
    JRadioButton jRadioButton = new JRadioButton("Apple");
    JProgressBar jProgressBar = new JProgressBar();
    jProgressBar.setValue(50);
    JSpinner jSpinner = new JSpinner(new SpinnerNumberModel(50, 0, 100, 1));
    JTree jTree = new JTree();
    jTree.setOpaque(false);
    jTree.setEnabled(false);
    jList.setModel(new AbstractListModel() {
          String[] strings = new String[] { "Donald Duck", "Mickey Mouse", "Pluto", "Cartman" };
          
          public int getSize() {
            return this.strings.length;
          }
          
          public Object getElementAt(int param1Int) {
            return this.strings[param1Int];
          }
        });
    jList.setVisibleRowCount(4);
    jList.setBorder(new LineBorder(Color.GRAY));
    jTextArea.setLineWrap(true);
    jTextArea.setWrapStyleWord(true);
    jTextArea.setBorder(new LineBorder(Color.GRAY));
    jComboBox.setModel(new DefaultComboBoxModel<String>(new String[] { "Text in ComboBox" }));
    jCheckBox.setMargin(new Insets(0, 0, 0, 0));
    add(jLabel1, "split, span");
    add(jTextField1, "");
    add(jLabel2, "gap unrelated");
    add(jTextField2, "wrap");
    add(jLabel3, "split, span");
    add(jSeparator, "growx, span, gap 2, wrap unrelated");
    add(jLabel4, "wrap 2");
    add(jTextArea, "span, wmin 150, wrap unrelated");
    add(jLabel5, "wrap 2");
    add(jList, "split, span");
    add(jScrollBar1, "growy");
    add(jProgressBar, "width 80!");
    add(jTree, "wrap unrelated");
    add(jScrollBar2, "split, span, growx");
    add(jSpinner, "wrap unrelated");
    add(jComboBox, "span, split");
    add(jRadioButton, "");
    add(jCheckBox, "wrap unrelated");
    add(jButton3, "split, span, tag help2");
    add(jButton2, "tag ok");
    add(jButton1, "tag cancel");
    HiDPISimulator.TEXT_AREA = jTextArea;
  }
  
  public void paint(Graphics paramGraphics) {
    if (HiDPISimulator.GUI_BUF == null) {
      BufferedImage bufferedImage = new BufferedImage(getWidth(), getHeight(), 2);
      Graphics2D graphics2D = bufferedImage.createGraphics();
      super.paint(graphics2D);
      graphics2D.dispose();
      paramGraphics.drawImage(bufferedImage, 0, 0, null);
      HiDPISimulator.GUI_BUF = bufferedImage;
      if (HiDPISimulator.CUR_DPI == PlatformDefaults.getDefaultDPI())
        HiDPISimulator.ORIG_GUI_BUF = bufferedImage; 
      SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              HiDPISimulator.MIRROR_PANEL.revalidate();
              HiDPISimulator.MIRROR_PANEL.repaint();
            }
          });
    } else {
      super.paint(paramGraphics);
    } 
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/miginfocom/demo/HiDPIDemoPanel.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */