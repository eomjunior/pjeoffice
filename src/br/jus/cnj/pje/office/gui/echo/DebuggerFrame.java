/*     */ package br.jus.cnj.pje.office.gui.echo;
/*     */ 
/*     */ import com.github.utils4j.gui.imp.Images;
/*     */ import com.github.utils4j.gui.imp.SimpleFrame;
/*     */ import com.github.utils4j.gui.imp.SwingTools;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Browser;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Font;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ComponentAdapter;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.stream.Stream;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import net.miginfocom.swing.MigLayout;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DebuggerFrame
/*     */   extends SimpleFrame
/*     */   implements IDebuggerEchoNotifier
/*     */ {
/*     */   private final DebuggerPanel debug;
/*     */   private final String endpoint;
/*     */   
/*     */   public DebuggerFrame(String title, String header, String endpoint) {
/*  68 */     super(title, Images.ECHO.asImage());
/*  69 */     this.debug = new DebuggerPanel(header);
/*  70 */     this.endpoint = (String)Args.requireNonNull(endpoint, "endpoint is null");
/*  71 */     setup();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onEscPressed(ActionEvent e) {}
/*     */ 
/*     */   
/*     */   private void setup() {
/*  79 */     setupLayout();
/*  80 */     setupFrame();
/*     */   }
/*     */   
/*     */   public void clear(ActionEvent e) {
/*  84 */     this.debug.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void echoS2B(String message) {
/*  89 */     this.debug.echoS2B(message);
/*     */   }
/*     */ 
/*     */   
/*     */   public void echoB2S(String message) {
/*  94 */     this.debug.echoB2S(message);
/*     */   }
/*     */   
/*     */   private void setupFrame() {
/*  98 */     pack();
/*  99 */     setPosition();
/* 100 */     setDefaultCloseOperation(2);
/* 101 */     addComponentListener(new ComponentAdapter()
/*     */         {
/*     */           public void componentResized(ComponentEvent e) {
/* 104 */             DebuggerFrame.this.debug.setDividerLocation(0.5D);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private void setupLayout() {
/* 110 */     JPanel contentPane = new JPanel();
/* 111 */     contentPane.setLayout(new BorderLayout(0, 0));
/* 112 */     contentPane.add(north(), "North");
/* 113 */     contentPane.add(this.debug, "Center");
/* 114 */     contentPane.add(south(), "South");
/* 115 */     setContentPane(contentPane);
/*     */   }
/*     */   
/*     */   private Component north() {
/* 119 */     JLabel dev = new JLabel("<html>ACESSE O ENDEREÇO <u>" + this.endpoint + "</u></html>");
/* 120 */     dev.setCursor(Cursor.getPredefinedCursor(12));
/* 121 */     dev.setForeground(Color.BLUE);
/* 122 */     dev.setFont(new Font("Tahoma", 0, 12));
/* 123 */     dev.setHorizontalAlignment(0);
/* 124 */     dev.addMouseListener(new MouseAdapter() {
/*     */           public void mouseClicked(MouseEvent e) {
/* 126 */             Browser.navigateTo(DebuggerFrame.this.endpoint);
/*     */           }
/*     */         });
/* 129 */     return dev;
/*     */   }
/*     */   
/*     */   private Component south() {
/* 133 */     JButton horizontal = new JButton("Horizontal");
/* 134 */     horizontal.addActionListener(e -> this.debug.toHorizontal());
/* 135 */     JButton vertical = new JButton("Vertical");
/* 136 */     vertical.addActionListener(e -> this.debug.toVertical());
/* 137 */     JButton clear = new JButton("Limpar");
/* 138 */     clear.addActionListener(this::clear);
/* 139 */     JPanel southPane = new JPanel();
/* 140 */     southPane.setLayout((LayoutManager)new MigLayout("fillx", "push[][][]", "[][][]"));
/* 141 */     southPane.add(vertical);
/* 142 */     southPane.add(horizontal);
/* 143 */     southPane.add(clear);
/* 144 */     return southPane;
/*     */   }
/*     */   
/*     */   private void setPosition() {
/* 148 */     GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
/*     */     try {
/* 150 */       Stream<GraphicsDevice> screens = Stream.of(ge.getScreenDevices());
/* 151 */       GraphicsDevice mainScreen = ge.getDefaultScreenDevice();
/*     */ 
/*     */ 
/*     */       
/* 155 */       GraphicsDevice secondaryScreen = screens.filter(g -> !g.equals(mainScreen)).findFirst().orElse(mainScreen);
/*     */       
/* 157 */       GraphicsConfiguration config = (GraphicsConfiguration)Stream.<GraphicsConfiguration>of(secondaryScreen.getConfigurations()).findFirst().orElseThrow(() -> new Exception("There's no configuration for secondary monitor"));
/* 158 */       Rectangle monitorBounds = config.getBounds();
/*     */ 
/*     */       
/* 161 */       if (mainScreen.equals(secondaryScreen)) {
/* 162 */         int px = 5 * monitorBounds.width / 8;
/* 163 */         int py = monitorBounds.height / 4;
/*     */         
/* 165 */         int xwidth = 3 * monitorBounds.width / 8;
/* 166 */         int yheight = 3 * monitorBounds.height / 4;
/*     */         
/* 168 */         setBounds(px, py, xwidth, yheight - 32);
/* 169 */         setAlwaysOnTop(true);
/*     */       } else {
/* 171 */         int xwidth = monitorBounds.x;
/* 172 */         int yheight = monitorBounds.y;
/* 173 */         setLocation(xwidth, yheight);
/* 174 */         setExtendedState(6);
/*     */       } 
/* 176 */     } catch (Exception e) {
/* 177 */       toCenter();
/*     */       return;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 183 */     SwingTools.invokeLater(() -> (new DebuggerFrame("Depuração", "Requisição %s", "http://127.0.0.1:8800/pjeOffice/api")).setVisible(true));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/gui/echo/DebuggerFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */