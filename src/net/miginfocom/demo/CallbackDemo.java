package net.miginfocom.demo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.IdentityHashMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import net.miginfocom.layout.BoundSize;
import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.LayoutCallback;
import net.miginfocom.layout.UnitValue;
import net.miginfocom.swing.MigLayout;

public class CallbackDemo extends JFrame implements ActionListener, MouseMotionListener, MouseListener {
  private final Timer repaintTimer = new Timer(20, new ActionListener() {
        public void actionPerformed(ActionEvent param1ActionEvent) {
          ((JPanel)CallbackDemo.this.getContentPane()).revalidate();
        }
      });
  
  private final IdentityHashMap<Object, Long> pressMap = new IdentityHashMap<Object, Long>();
  
  private Point mousePos = null;
  
  private static Font[] FONTS = new Font[120];
  
  public CallbackDemo() {
    super("MiG Layout Callback Demo");
    MigLayout migLayout = new MigLayout("align center bottom, insets 30");
    final JPanel panel = new JPanel((LayoutManager)migLayout) {
        protected void paintComponent(Graphics param1Graphics) {
          ((Graphics2D)param1Graphics).setPaint(new GradientPaint(0.0F, (getHeight() / 2), Color.WHITE, 0.0F, getHeight(), new Color(240, 238, 235)));
          param1Graphics.fillRect(0, 0, getWidth(), getHeight());
        }
      };
    setContentPane(jPanel);
    migLayout.addLayoutCallback(new LayoutCallback() {
          public BoundSize[] getSize(ComponentWrapper param1ComponentWrapper) {
            if (param1ComponentWrapper.getComponent() instanceof JButton) {
              Component component = (Component)param1ComponentWrapper.getComponent();
              Point point = (CallbackDemo.this.mousePos != null) ? SwingUtilities.convertPoint(panel, CallbackDemo.this.mousePos, component) : new Point(-1000, -1000);
              float f = (float)Math.sqrt(Math.pow(Math.abs(point.x - component.getWidth() / 2.0F), 2.0D) + Math.pow(Math.abs(point.y - component.getHeight() / 2.0F), 2.0D));
              f = Math.max(2.0F - f / 200.0F, 1.0F);
              return new BoundSize[] { new BoundSize(new UnitValue(70.0F * f), ""), new BoundSize(new UnitValue(70.0F * f), "") };
            } 
            return null;
          }
          
          public void correctBounds(ComponentWrapper param1ComponentWrapper) {
            Long long_ = (Long)CallbackDemo.this.pressMap.get(param1ComponentWrapper.getComponent());
            if (long_ != null) {
              long l = System.nanoTime() - long_.longValue();
              double d = 100.0D - l / 1.0E8D;
              int i = (int)Math.round(Math.abs(Math.sin(l / 3.0E8D) * d));
              param1ComponentWrapper.setBounds(param1ComponentWrapper.getX(), param1ComponentWrapper.getY() - i, param1ComponentWrapper.getWidth(), param1ComponentWrapper.getHeight());
              if (d < 0.5D) {
                CallbackDemo.this.pressMap.remove(param1ComponentWrapper.getComponent());
                if (CallbackDemo.this.pressMap.size() == 0)
                  CallbackDemo.this.repaintTimer.stop(); 
              } 
            } 
          }
        });
    for (byte b = 0; b < 10; b++)
      jPanel.add(createButton(b), "aligny 0.8al"); 
    JLabel jLabel = new JLabel("Can't you just feel the urge to press one of those Swing JButtons?");
    jLabel.setFont(new Font("verdana", 0, 24));
    jLabel.setForeground(new Color(150, 150, 150));
    jPanel.add(jLabel, "pos 0.5al 0.2al");
    jPanel.addMouseMotionListener(this);
    jPanel.addMouseListener(this);
  }
  
  private JButton createButton(int paramInt) {
    JButton jButton = new JButton(String.valueOf("MIG LAYOUT".charAt(paramInt))) {
        public Font getFont() {
          if (CallbackDemo.FONTS[0] == null)
            for (byte b = 0; b < CallbackDemo.FONTS.length; b++)
              CallbackDemo.FONTS[b] = new Font("tahoma", 0, b);  
          return CallbackDemo.FONTS[getWidth() >> 1];
        }
      };
    jButton.setForeground(new Color(100, 100, 100));
    jButton.setFocusPainted(false);
    jButton.addMouseMotionListener(this);
    jButton.addActionListener(this);
    jButton.setMargin(new Insets(0, 0, 0, 0));
    return jButton;
  }
  
  public void mouseDragged(MouseEvent paramMouseEvent) {}
  
  public void mouseMoved(MouseEvent paramMouseEvent) {
    if (paramMouseEvent.getSource() instanceof JButton) {
      this.mousePos = SwingUtilities.convertPoint((Component)paramMouseEvent.getSource(), paramMouseEvent.getPoint(), getContentPane());
    } else {
      this.mousePos = paramMouseEvent.getPoint();
    } 
    ((JPanel)getContentPane()).revalidate();
  }
  
  public void mousePressed(MouseEvent paramMouseEvent) {}
  
  public void mouseReleased(MouseEvent paramMouseEvent) {}
  
  public void mouseClicked(MouseEvent paramMouseEvent) {}
  
  public void mouseEntered(MouseEvent paramMouseEvent) {}
  
  public void mouseExited(MouseEvent paramMouseEvent) {
    this.mousePos = null;
    ((JPanel)getContentPane()).revalidate();
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    this.pressMap.put(paramActionEvent.getSource(), Long.valueOf(System.nanoTime()));
    this.repaintTimer.start();
  }
  
  public static void main(String[] paramArrayOfString) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception exception) {}
    CallbackDemo callbackDemo = new CallbackDemo();
    callbackDemo.setDefaultCloseOperation(3);
    callbackDemo.setSize(970, 500);
    callbackDemo.setLocationRelativeTo(null);
    callbackDemo.setVisible(true);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/miginfocom/demo/CallbackDemo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */