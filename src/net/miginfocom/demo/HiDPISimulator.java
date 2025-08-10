package net.miginfocom.demo;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.FontUIResource;
import net.miginfocom.layout.PlatformDefaults;
import net.miginfocom.swing.MigLayout;
import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.fonts.SubstanceFontUtilities;
import org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel;

public class HiDPISimulator {
  static final String SYSTEM_LAF_NAME = "System";
  
  static final String SUBSTANCE_LAF_NAME = "Substance";
  
  static final String OCEAN_LAF_NAME = "Ocean";
  
  static final String NUMBUS_LAF_NAME = "Nimbus (Soon..)";
  
  static JFrame APP_GUI_FRAME;
  
  static HiDPIDemoPanel HiDPIDEMO_PANEL;
  
  static JPanel SIM_PANEL;
  
  static JPanel MIRROR_PANEL;
  
  static JScrollPane MAIN_SCROLL;
  
  static JTextArea TEXT_AREA;
  
  static boolean SCALE_LAF = false;
  
  static boolean SCALE_FONTS = true;
  
  static boolean SCALE_LAYOUT = true;
  
  static boolean PAINT_GHOSTED = false;
  
  static BufferedImage GUI_BUF = null;
  
  static BufferedImage ORIG_GUI_BUF = null;
  
  static int CUR_DPI = PlatformDefaults.getDefaultDPI();
  
  static HashMap<String, Font> ORIG_DEFAULTS;
  
  private static JPanel createScaleMirror() {
    return new JPanel((LayoutManager)new MigLayout()) {
        protected void paintComponent(Graphics param1Graphics) {
          super.paintComponent(param1Graphics);
          if (HiDPISimulator.GUI_BUF != null) {
            Graphics2D graphics2D = (Graphics2D)param1Graphics.create();
            double d = getToolkit().getScreenResolution();
            AffineTransform affineTransform = graphics2D.getTransform();
            graphics2D.scale(d / HiDPISimulator.CUR_DPI, d / HiDPISimulator.CUR_DPI);
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            graphics2D.drawImage(HiDPISimulator.GUI_BUF, 0, 0, (ImageObserver)null);
            graphics2D.setTransform(affineTransform);
            if (HiDPISimulator.ORIG_GUI_BUF != null && HiDPISimulator.PAINT_GHOSTED) {
              graphics2D.setComposite(AlphaComposite.getInstance(3, 0.2F));
              graphics2D.drawImage(HiDPISimulator.ORIG_GUI_BUF, 0, 0, (ImageObserver)null);
            } 
            graphics2D.dispose();
          } 
        }
        
        public Dimension getPreferredSize() {
          return (HiDPISimulator.ORIG_GUI_BUF != null) ? new Dimension(HiDPISimulator.ORIG_GUI_BUF.getWidth(), HiDPISimulator.ORIG_GUI_BUF.getHeight()) : new Dimension(100, 100);
        }
        
        public Dimension getMinimumSize() {
          return getPreferredSize();
        }
      };
  }
  
  private static JPanel createSimulator() {
    final JRadioButton scaleCompsFonts = new JRadioButton("UIManager Font Substitution", true);
    final JRadioButton scaleCompsLaf = new JRadioButton("Native Look&Feel Scaling", false);
    JRadioButton jRadioButton3 = new JRadioButton("No Scaling", false);
    final JRadioButton scaleLayoutMig = new JRadioButton("Native MigLayout Gap Scaling", true);
    JRadioButton jRadioButton5 = new JRadioButton("No Gap Scaling", false);
    final JComboBox<String> lafCombo = new JComboBox<String>(new String[] { "System", "Substance", "Ocean", "Nimbus (Soon..)" });
    ButtonGroup buttonGroup1 = new ButtonGroup();
    ButtonGroup buttonGroup2 = new ButtonGroup();
    final JCheckBox ghostCheck = new JCheckBox("Overlay \"Optimal\" HiDPI Result");
    jRadioButton2.setEnabled(false);
    buttonGroup1.add(jRadioButton1);
    buttonGroup1.add(jRadioButton2);
    buttonGroup1.add(jRadioButton3);
    buttonGroup2.add(jRadioButton4);
    buttonGroup2.add(jRadioButton5);
    Vector<String> vector = new Vector();
    float f;
    for (f = 0.5F; f < 2.01F; f += 0.1F)
      vector.add(Math.round(PlatformDefaults.getDefaultDPI() * f) + " DPI (" + Math.round(f * 100.0F + 0.499F) + "%)"); 
    final JComboBox<String> dpiCombo = new JComboBox<String>(vector);
    jComboBox2.setSelectedIndex(5);
    JPanel jPanel = new JPanel((LayoutManager)new MigLayout("alignx center, insets 10px, flowy", "[]", "[]3px[]0px[]"));
    JLabel jLabel1 = new JLabel("Look & Feel:");
    JLabel jLabel2 = new JLabel("Simulated DPI:");
    JLabel jLabel3 = new JLabel("Component/Text Scaling:");
    JLabel jLabel4 = new JLabel("LayoutManager Scaling:");
    JLabel jLabel5 = new JLabel("Visual Aids:");
    jPanel.add(jLabel1, "");
    jPanel.add(jComboBox1, "wrap");
    jPanel.add(jLabel2, "");
    jPanel.add(jComboBox2, "wrap");
    jPanel.add(jLabel3, "");
    jPanel.add(jRadioButton1, "");
    jPanel.add(jRadioButton2, "");
    jPanel.add(jRadioButton3, "wrap");
    jPanel.add(jLabel4, "");
    jPanel.add(jRadioButton4, "");
    jPanel.add(jRadioButton5, "wrap");
    jPanel.add(jLabel5, "");
    jPanel.add(jCheckBox, "");
    lockFont(new Component[] { 
          jComboBox2, jRadioButton1, jRadioButton2, jRadioButton4, jRadioButton3, jComboBox1, jCheckBox, jPanel, jLabel1, jLabel2, 
          jRadioButton5, jLabel3, jLabel4, jLabel5 });
    jComboBox1.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent param1ActionEvent) {
            HiDPISimulator.GUI_BUF = null;
            try {
              Object object = lafCombo.getSelectedItem();
              dpiCombo.setSelectedIndex(5);
              if (object.equals("System")) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
              } else if (object.equals("Substance")) {
                UIManager.setLookAndFeel((LookAndFeel)new SubstanceBusinessBlackSteelLookAndFeel());
              } else if (object.equals("Ocean")) {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
              } else {
                JOptionPane.showMessageDialog(HiDPISimulator.APP_GUI_FRAME, "Nimbus will be included as soon as it is ready!");
              } 
              if (HiDPISimulator.ORIG_DEFAULTS != null)
                for (String str : HiDPISimulator.ORIG_DEFAULTS.keySet())
                  UIManager.put(str, null);  
              HiDPISimulator.ORIG_DEFAULTS = null;
              if (UIManager.getLookAndFeel().getName().toLowerCase().contains("windows")) {
                UIManager.put("TextArea.font", UIManager.getFont("TextField.font"));
              } else {
                UIManager.put("TextArea.font", null);
              } 
              SwingUtilities.updateComponentTreeUI(HiDPISimulator.APP_GUI_FRAME);
              HiDPISimulator.MAIN_SCROLL.setBorder((Border)null);
              if (object.equals("System")) {
                if (scaleCompsLaf.isSelected())
                  scaleCompsFonts.setSelected(true); 
                scaleCompsLaf.setEnabled(false);
              } else if (object.equals("Substance")) {
                scaleCompsLaf.setEnabled(true);
              } else if (object.equals("Ocean")) {
                if (scaleCompsLaf.isSelected())
                  scaleCompsFonts.setSelected(true); 
                scaleCompsLaf.setEnabled(false);
              } 
              HiDPISimulator.setDPI(HiDPISimulator.CUR_DPI);
            } catch (Exception exception) {
              exception.printStackTrace();
            } 
          }
        });
    jCheckBox.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent param1ActionEvent) {
            HiDPISimulator.GUI_BUF = null;
            HiDPISimulator.PAINT_GHOSTED = ghostCheck.isSelected();
            HiDPISimulator.APP_GUI_FRAME.repaint();
          }
        });
    jRadioButton4.addItemListener(new ItemListener() {
          public void itemStateChanged(ItemEvent param1ItemEvent) {
            HiDPISimulator.GUI_BUF = null;
            HiDPISimulator.SCALE_LAYOUT = scaleLayoutMig.isSelected();
            HiDPISimulator.setDPI(HiDPISimulator.CUR_DPI);
          }
        });
    ItemListener itemListener = new ItemListener() {
        public void itemStateChanged(ItemEvent param1ItemEvent) {
          if (param1ItemEvent.getStateChange() == 1) {
            HiDPISimulator.GUI_BUF = null;
            HiDPISimulator.SCALE_LAF = scaleCompsLaf.isSelected();
            HiDPISimulator.SCALE_FONTS = scaleCompsFonts.isSelected();
            HiDPISimulator.setDPI(HiDPISimulator.CUR_DPI);
          } 
        }
      };
    jRadioButton2.addItemListener(itemListener);
    jRadioButton1.addItemListener(itemListener);
    jRadioButton3.addItemListener(itemListener);
    jComboBox2.addItemListener(new ItemListener() {
          public void itemStateChanged(ItemEvent param1ItemEvent) {
            if (param1ItemEvent.getStateChange() == 1) {
              HiDPISimulator.GUI_BUF = null;
              HiDPISimulator.CUR_DPI = Integer.parseInt(dpiCombo.getSelectedItem().toString().substring(0, 3).trim());
              HiDPISimulator.setDPI(HiDPISimulator.CUR_DPI);
            } 
          }
        });
    return jPanel;
  }
  
  private static void lockFont(Component... paramVarArgs) {
    for (Component component : paramVarArgs) {
      Font font = component.getFont();
      component.setFont(font.deriveFont(font.getSize()));
    } 
  }
  
  private static void revalidateGUI() {
    APP_GUI_FRAME.getContentPane().invalidate();
    APP_GUI_FRAME.repaint();
  }
  
  private static synchronized void setDPI(int paramInt) {
    float f1 = paramInt / Toolkit.getDefaultToolkit().getScreenResolution();
    TEXT_AREA.setSize(0, 0);
    PlatformDefaults.setHorizontalScaleFactor(Float.valueOf(0.1F));
    PlatformDefaults.setHorizontalScaleFactor(SCALE_LAYOUT ? Float.valueOf(f1) : null);
    PlatformDefaults.setVerticalScaleFactor(SCALE_LAYOUT ? Float.valueOf(f1) : null);
    float f2 = SCALE_FONTS ? (paramInt / Toolkit.getDefaultToolkit().getScreenResolution()) : 1.0F;
    if (ORIG_DEFAULTS == null) {
      ORIG_DEFAULTS = new HashMap<String, Font>();
      HashSet hashSet = new HashSet(UIManager.getLookAndFeelDefaults().keySet());
      Iterator<E> iterator = hashSet.iterator();
      while (iterator.hasNext()) {
        String str = iterator.next().toString();
        Object object = UIManager.get(str);
        if (object instanceof Font)
          ORIG_DEFAULTS.put(str, (Font)object); 
      } 
    } 
    Set<Map.Entry<String, Font>> set = ORIG_DEFAULTS.entrySet();
    for (Map.Entry<String, Font> entry : set) {
      Font font = (Font)entry.getValue();
      if (!SCALE_LAF) {
        UIManager.put(entry.getKey(), new FontUIResource(font.deriveFont(font.getSize() * f2)));
        continue;
      } 
      UIManager.put(entry.getKey(), null);
    } 
    if (SCALE_LAF) {
      scaleSubstanceLAF(f1);
    } else if (UIManager.getLookAndFeel().getName().toLowerCase().contains("substance")) {
      scaleSubstanceLAF(1.0F);
    } 
    SwingUtilities.updateComponentTreeUI(HiDPIDEMO_PANEL);
    revalidateGUI();
  }
  
  private static void scaleSubstanceLAF(float paramFloat) {
    SubstanceLookAndFeel.setFontPolicy(SubstanceFontUtilities.getScaledFontPolicy(paramFloat));
    try {
      UIManager.setLookAndFeel((LookAndFeel)new SubstanceBusinessBlackSteelLookAndFeel());
    } catch (Exception exception) {}
    SwingUtilities.updateComponentTreeUI(APP_GUI_FRAME);
    MAIN_SCROLL.setBorder((Border)null);
  }
  
  public static void main(String[] paramArrayOfString) {
    try {
      System.setProperty("apple.laf.useScreenMenuBar", "true");
      System.setProperty("com.apple.mrj.application.apple.menu.about.name", "HiDPI Simulator");
    } catch (Exception exception) {}
    PlatformDefaults.setDefaultHorizontalUnit(1);
    PlatformDefaults.setDefaultVerticalUnit(2);
    SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            try {
              UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception exception) {
              exception.printStackTrace();
            } 
            if (UIManager.getLookAndFeel().getName().toLowerCase().contains("windows"))
              UIManager.put("TextArea.font", UIManager.getFont("TextField.font")); 
            HiDPISimulator.APP_GUI_FRAME = new JFrame("Resolution Independence Simulator");
            JPanel jPanel1 = new JPanel((LayoutManager)new MigLayout("fill, insets 0px, nocache"));
            JPanel jPanel2 = new JPanel((LayoutManager)new MigLayout("fill, insets 0px, nocache")) {
                public void paintComponent(Graphics param2Graphics) {
                  Graphics2D graphics2D = (Graphics2D)param2Graphics.create();
                  graphics2D.setPaint(new GradientPaint(0.0F, 0.0F, new Color(20, 20, 30), 0.0F, getHeight(), new Color(90, 90, 110), false));
                  graphics2D.fillRect(0, 0, getWidth(), getHeight());
                  graphics2D.setFont(graphics2D.getFont().deriveFont(1, 13.0F));
                  graphics2D.setPaint(Color.WHITE);
                  graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                  graphics2D.drawString("Left panel shows the scaled version. Right side shows how this would look on a HiDPI screen. It should look the same as the original panel!", 10, 19);
                  graphics2D.dispose();
                }
              };
            HiDPISimulator.HiDPIDEMO_PANEL = new HiDPIDemoPanel();
            HiDPISimulator.SIM_PANEL = HiDPISimulator.createSimulator();
            HiDPISimulator.MIRROR_PANEL = HiDPISimulator.createScaleMirror();
            HiDPISimulator.MAIN_SCROLL = new JScrollPane(jPanel2);
            HiDPISimulator.MAIN_SCROLL.setBorder((Border)null);
            jPanel2.add(HiDPISimulator.HiDPIDEMO_PANEL, "align center center, split, span, width pref!");
            jPanel2.add(HiDPISimulator.MIRROR_PANEL, "id mirror, gap 20px!, width pref!");
            jPanel1.add(HiDPISimulator.SIM_PANEL, "dock south");
            jPanel1.add(HiDPISimulator.MAIN_SCROLL, "dock center");
            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            HiDPISimulator.APP_GUI_FRAME.setContentPane(jPanel1);
            HiDPISimulator.APP_GUI_FRAME.setSize(Math.min(1240, dimension.width), Math.min(950, dimension.height - 30));
            HiDPISimulator.APP_GUI_FRAME.setDefaultCloseOperation(3);
            HiDPISimulator.APP_GUI_FRAME.setLocationRelativeTo(null);
            HiDPISimulator.APP_GUI_FRAME.setVisible(true);
          }
        });
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/miginfocom/demo/HiDPISimulator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */