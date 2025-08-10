/*     */ package br.jus.cnj.pje.office.gui.echo;
/*     */ 
/*     */ import com.github.utils4j.echo.IEcho;
/*     */ import com.github.utils4j.gui.imp.SwingTools;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JSplitPane;
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
/*     */ public class DebuggerPanel
/*     */   extends JPanel
/*     */   implements IEcho, IDebuggerEchoNotifier
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final IEcho b2s;
/*     */   private final IEcho s2b;
/*     */   private JSplitPane center;
/*     */   
/*     */   public DebuggerPanel(String header) {
/*  50 */     this.b2s = (IEcho)new Browser2SignerPanel(header);
/*  51 */     this.s2b = (IEcho)new Signer2BackendPanel();
/*  52 */     setup();
/*     */   }
/*     */   
/*     */   private void setup() {
/*  56 */     setupLayout();
/*     */   }
/*     */   
/*     */   private void setupLayout() {
/*  60 */     setLayout(new BorderLayout(0, 0));
/*  61 */     add(north(), "North");
/*  62 */     toHorizontal();
/*  63 */     add(west(), "West");
/*  64 */     add(east(), "East");
/*  65 */     add(south(), "South");
/*     */   }
/*     */   
/*     */   private Component center(int splitMode) {
/*  69 */     this
/*     */ 
/*     */       
/*  72 */       .center = new JSplitPane(splitMode, this.b2s.asPanel(), this.s2b.asPanel());
/*     */     
/*  74 */     this.center.setOneTouchExpandable(true);
/*  75 */     return this.center;
/*     */   }
/*     */   
/*     */   private Component east() {
/*  79 */     return new JPanel();
/*     */   }
/*     */   
/*     */   private Component west() {
/*  83 */     return new JPanel();
/*     */   }
/*     */   
/*     */   private Component south() {
/*  87 */     return new JPanel();
/*     */   }
/*     */   
/*     */   private Component north() {
/*  91 */     return new JPanel();
/*     */   }
/*     */   
/*     */   public final void toVertical() {
/*  95 */     reSplit(() -> add(center(1), "Center"));
/*     */   }
/*     */   
/*     */   public final void toHorizontal() {
/*  99 */     reSplit(() -> add(center(0), "Center"));
/*     */   }
/*     */   
/*     */   private void removeCenter() {
/* 103 */     if (this.center != null) {
/* 104 */       remove(this.center);
/*     */     }
/*     */   }
/*     */   
/*     */   private void reSplit(Runnable code) {
/* 109 */     removeCenter();
/* 110 */     code.run();
/* 111 */     revalidate();
/* 112 */     updateUI();
/*     */   }
/*     */ 
/*     */   
/*     */   public void paint(Graphics g) {
/* 117 */     setDividerLocation(0.5D);
/* 118 */     super.paint(g);
/*     */   }
/*     */   
/*     */   public final void setDividerLocation(double d) {
/* 122 */     if (this.center.getDividerLocation() != 1) {
/* 123 */       this.center.setDividerLocation(d);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 129 */     this.b2s.clear();
/* 130 */     this.s2b.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void echoS2B(String message) {
/* 135 */     this.s2b.addRequest(message);
/*     */   }
/*     */ 
/*     */   
/*     */   public void echoB2S(String message) {
/* 140 */     this.b2s.addRequest(message);
/*     */   }
/*     */ 
/*     */   
/*     */   public JPanel asPanel() {
/* 145 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addRequest(String request) {}
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/* 154 */     SwingTools.invokeLater(() -> {
/*     */           JFrame f = new JFrame();
/*     */           f.setContentPane(new DebuggerPanel("requisição %s"));
/*     */           f.pack();
/*     */           f.setVisible(true);
/*     */         });
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/gui/echo/DebuggerPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */