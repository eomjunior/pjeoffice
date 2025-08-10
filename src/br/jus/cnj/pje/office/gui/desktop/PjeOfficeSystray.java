/*     */ package br.jus.cnj.pje.office.gui.desktop;
/*     */ 
/*     */ import br.jus.cnj.pje.office.IBootable;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeVersion;
/*     */ import br.jus.cnj.pje.office.gui.PjeImages;
/*     */ import com.github.utils4j.IDisposable;
/*     */ import com.github.utils4j.gui.imp.SwingTools;
/*     */ import java.awt.AWTException;
/*     */ import java.awt.Frame;
/*     */ import java.awt.PopupMenu;
/*     */ import java.awt.SystemTray;
/*     */ import java.awt.TrayIcon;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
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
/*     */ public final class PjeOfficeSystray
/*     */   implements IDisposable
/*     */ {
/*     */   private Frame frame;
/*     */   private SystemTray tray;
/*     */   private TrayIcon trayIcon;
/*     */   private IBootable office;
/*     */   
/*     */   public PjeOfficeSystray(IBootable boot, PopupMenu menu) throws AWTException {
/*  56 */     setFrameState();
/*  57 */     setupLayout(boot, menu);
/*     */   }
/*     */   
/*     */   private void setFrameState() {
/*  61 */     this.frame = new Frame();
/*  62 */     this.frame.setType(Window.Type.UTILITY);
/*  63 */     this.frame.setUndecorated(true);
/*  64 */     this.frame.setResizable(false);
/*     */   }
/*     */   
/*     */   private void setupLayout(IBootable boot, final PopupMenu menu) throws AWTException {
/*  68 */     this.office = boot;
/*  69 */     this.trayIcon = new TrayIcon(PjeImages.PJE_ICON_TRAY.asImage());
/*  70 */     this.trayIcon.setPopupMenu(menu);
/*  71 */     this.trayIcon.addMouseListener(new MouseAdapter() {
/*     */           public void mouseReleased(MouseEvent e) {
/*  73 */             if (e.getButton() != 3) {
/*  74 */               PjeOfficeSystray.this.frame.add(menu);
/*  75 */               menu.show(PjeOfficeSystray.this.frame, e.getX(), e.getY());
/*  76 */               PjeOfficeSystray.this.frame.removeAll();
/*     */             } 
/*     */           }
/*     */         });
/*  80 */     this.tray = SystemTray.getSystemTray();
/*  81 */     this.tray.add(this.trayIcon);
/*     */   }
/*     */   
/*     */   public final void setOnline(boolean online) {
/*  85 */     if (this.trayIcon != null) {
/*  86 */       SwingTools.invokeLater(() -> this.trayIcon.setImage(online ? PjeImages.PJE_ICON_TRAY_ONLINE.asImage() : PjeImages.PJE_ICON_TRAY.asImage()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void dispose() {
/*  95 */     if (this.tray != null) {
/*  96 */       this.tray.remove(this.trayIcon);
/*  97 */       this.tray = null;
/*     */     } 
/*  99 */     if (this.trayIcon != null) {
/* 100 */       this.trayIcon.setPopupMenu(null);
/* 101 */       this.trayIcon = null;
/*     */     } 
/* 103 */     if (this.frame != null) {
/* 104 */       this.frame.removeAll();
/* 105 */       this.frame.dispose();
/* 106 */       this.frame = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void hello() {
/* 111 */     if (this.frame != null) {
/* 112 */       this.frame.setVisible(true);
/*     */     }
/* 114 */     if (this.trayIcon != null) {
/* 115 */       String professional = "PJeOffice Pro";
/* 116 */       String version = "Versão " + PjeVersion.CURRENT + this.office.model();
/* 117 */       this.trayIcon.setToolTip(String.format("%s - %s \nAssinador Digital.", new Object[] { professional, version }));
/* 118 */       this.trayIcon.displayMessage(professional, version, TrayIcon.MessageType.NONE);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/gui/desktop/PjeOfficeSystray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */