/*     */ package br.jus.cnj.pje.office.gui.desktop;
/*     */ 
/*     */ import br.jus.cnj.pje.office.IBootable;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeConfig;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeVersion;
/*     */ import br.jus.cnj.pje.office.gui.PjeImages;
/*     */ import com.github.utils4j.gui.imp.SimpleFrame;
/*     */ import com.github.utils4j.gui.imp.SwingTools;
/*     */ import com.github.utils4j.imp.Jvms;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.PopupMenu;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.border.EtchedBorder;
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
/*     */ 
/*     */ public class PjeOfficeDesktop
/*     */   extends SimpleFrame
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private JPanel contentPane;
/*     */   private JButton screenButton;
/*     */   
/*     */   public PjeOfficeDesktop(IBootable office, PopupMenu popup) {
/*  62 */     super("PJeOffice Pro - " + PjeVersion.CURRENT + office.model(), Jvms.isUnix() ? PjeImages.PJE_ICON_PJE_FEATHER.asImage() : PjeConfig.getIcon());
/*  63 */     setupFrame(office, setupLayout(popup));
/*     */   }
/*     */   
/*     */   public void setOnline(boolean online) {
/*  67 */     SwingTools.invokeLater(() -> this.screenButton.setIcon(online ? PjeImages.PJE_ICON_LIGHT_ONLINE.asIcon() : PjeImages.PJE_ICON_LIGHT.asIcon()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setupFrame(final IBootable boot, ImageIcon viewport) {
/*  74 */     addWindowListener(new WindowAdapter() {
/*     */           public void windowClosing(WindowEvent e) {
/*  76 */             boot.stateChanging(true);
/*     */           }
/*     */ 
/*     */           
/*     */           public void windowIconified(WindowEvent e) {
/*  81 */             boot.stateChanging(false);
/*     */           }
/*     */         });
/*  84 */     setResizable(false);
/*  85 */     setDefaultCloseOperation(0);
/*  86 */     setBounds(100, 100, viewport.getIconWidth() + 2, viewport.getIconHeight());
/*  87 */     toCenter();
/*     */   }
/*     */ 
/*     */   
/*     */   private ImageIcon setupLayout(final PopupMenu popup) {
/*  92 */     this.screenButton = new JButton("");
/*  93 */     this.screenButton.add(popup); ImageIcon viewport;
/*  94 */     this.screenButton.setIcon(viewport = PjeImages.PJE_ICON_LIGHT.asIcon());
/*  95 */     this.screenButton.addMouseListener(new MouseAdapter() {
/*     */           public void mousePressed(MouseEvent e) {
/*  97 */             popup.show(e.getComponent(), e.getX(), e.getY());
/*     */           }
/*     */         });
/* 100 */     this.contentPane = new JPanel();
/* 101 */     this.contentPane.setBorder(new EtchedBorder(1, null, null));
/* 102 */     this.contentPane.setLayout(new BorderLayout(0, 0));
/* 103 */     this.contentPane.add(this.screenButton, "Center");
/* 104 */     setContentPane(this.contentPane);
/* 105 */     return viewport;
/*     */   }
/*     */   
/*     */   protected void onEscPressed(ActionEvent e) {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/gui/desktop/PjeOfficeDesktop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */