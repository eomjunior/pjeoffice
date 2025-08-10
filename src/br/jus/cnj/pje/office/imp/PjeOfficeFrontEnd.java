/*     */ package br.jus.cnj.pje.office.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.IBootable;
/*     */ import br.jus.cnj.pje.office.IPjeFrontEnd;
/*     */ import br.jus.cnj.pje.office.core.IPjeOffice;
/*     */ import br.jus.cnj.pje.office.gui.desktop.PjeOfficeDesktop;
/*     */ import br.jus.cnj.pje.office.gui.desktop.PjeOfficeSystray;
/*     */ import com.github.signer4j.IStatusMonitor;
/*     */ import com.github.signer4j.imp.Repository;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Environment;
/*     */ import com.github.utils4j.imp.Jvms;
/*     */ import com.github.utils4j.imp.Threads;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import java.awt.PopupMenu;
/*     */ import java.awt.SystemTray;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ enum PjeOfficeFrontEnd
/*     */   implements IPjeFrontEnd
/*     */ {
/*  54 */   SYSTRAY("Modo bandeja")
/*     */   {
/*     */     private PjeOfficeSystray systray;
/*     */     
/*     */     public IPjeFrontEnd fallback() {
/*  59 */       return DESKTOP;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void setOnline(boolean online) {
/*  64 */       if (this.systray != null) {
/*  65 */         this.systray.setOnline(online);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     protected void doInstall(PopupMenu menu) throws Exception {
/*  71 */       this.systray = new PjeOfficeSystray((IBootable)this.office, menu);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void doDispose() {
/*  76 */       if (this.systray != null) {
/*  77 */         this.systray.dispose();
/*  78 */         this.systray = null;
/*     */       } 
/*  80 */       PjeOfficeFrontEnd.LOGGER.debug("Systray disposed!");
/*     */     }
/*     */ 
/*     */     
/*     */     protected void done() {
/*  85 */       if (this.systray != null) {
/*  86 */         this.systray.hello();
/*     */       
/*     */       }
/*     */     }
/*     */   },
/*  91 */   DESKTOP("Modo desktop")
/*     */   {
/*     */     private PjeOfficeDesktop desktop;
/*     */     
/*     */     public IPjeFrontEnd fallback() {
/*  96 */       return SYSTRAY;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void setOnline(boolean online) {
/* 101 */       if (this.desktop != null) {
/* 102 */         this.desktop.setOnline(online);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     protected void doInstall(PopupMenu menu) {
/* 108 */       this.desktop = new PjeOfficeDesktop((IBootable)this.office, menu);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void doDispose() {
/* 113 */       if (this.desktop != null) {
/* 114 */         this.desktop.close();
/*     */       }
/* 116 */       this.desktop = null;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void done() {
/* 121 */       if (this.desktop != null)
/* 122 */         this.desktop.showToFront(); 
/*     */     } }; private static final Logger LOGGER; private final String title;
/*     */   private Disposable ticketTokenCreated;
/*     */   
/*     */   static {
/* 127 */     LOGGER = LoggerFactory.getLogger(PjeOfficeFrontEnd.class);
/*     */   } private Disposable ticketTokenOnline; private Disposable ticketRepositoryChanged; protected IPjeOffice office;
/*     */   public static PjeOfficeFrontEnd getBest() {
/* 130 */     boolean systray = supportsSystray();
/* 131 */     LOGGER.info("Suporte a systray: " + systray);
/* 132 */     boolean forceDesktop = Environment.valueFrom("pjeoffice_desktop").isPresent();
/* 133 */     LOGGER.info("Forçar uso desktop: " + forceDesktop);
/* 134 */     return (systray && !forceDesktop) ? SYSTRAY : DESKTOP;
/*     */   }
/*     */   
/*     */   public static boolean supportsSystray() {
/* 138 */     return (SystemTray.isSupported() && !Jvms.isUnix());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PjeOfficeFrontEnd(String title) {
/* 148 */     this.title = title;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getTitle() {
/* 153 */     return this.title;
/*     */   }
/*     */ 
/*     */   
/*     */   public IPjeFrontEnd install(IPjeOffice office, PopupMenu menu) throws Exception {
/* 158 */     Args.requireNonNull(office, "office is null");
/* 159 */     Args.requireNonNull(menu, "menu is null");
/* 160 */     uninstall();
/* 161 */     this.office = office;
/* 162 */     doInstall(menu);
/* 163 */     this.ticketTokenCreated = office.newToken().subscribe(this::onTokenCreated);
/* 164 */     this.ticketRepositoryChanged = office.newRepository().subscribe(this::onRepositoryChanged);
/* 165 */     return this;
/*     */   }
/*     */   
/*     */   private void onTokenCreated(IStatusMonitor monitor) {
/* 169 */     resetTicketOnline();
/* 170 */     this.ticketTokenOnline = monitor.getStatus().subscribe(this::setOnline);
/*     */   }
/*     */   
/*     */   private void onRepositoryChanged(Repository repository) {
/* 174 */     resetTicketOnline();
/* 175 */     resetTicketTokenCreated();
/* 176 */     this.ticketTokenCreated = this.office.newToken().subscribe(this::onTokenCreated);
/*     */   }
/*     */ 
/*     */   
/*     */   public IPjeFrontEnd show(long startTime) {
/* 181 */     Threads.sleep(startTime + 2800L - System.currentTimeMillis());
/* 182 */     done();
/* 183 */     return this;
/*     */   }
/*     */   
/*     */   private void resetTicketTokenCreated() {
/* 187 */     if (this.ticketTokenCreated != null) {
/* 188 */       this.ticketTokenCreated.dispose();
/* 189 */       this.ticketTokenCreated = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void resetTicketOnline() {
/* 194 */     if (this.ticketTokenOnline != null) {
/* 195 */       this.ticketTokenOnline.dispose();
/* 196 */       this.ticketTokenOnline = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void uninstall() {
/* 202 */     if (this.ticketTokenCreated != null) {
/* 203 */       this.ticketTokenCreated.dispose();
/* 204 */       this.ticketTokenCreated = null;
/*     */     } 
/* 206 */     if (this.ticketTokenOnline != null) {
/* 207 */       this.ticketTokenOnline.dispose();
/* 208 */       this.ticketTokenOnline = null;
/*     */     } 
/* 210 */     if (this.ticketRepositoryChanged != null) {
/* 211 */       this.ticketRepositoryChanged.dispose();
/* 212 */       this.ticketRepositoryChanged = null;
/*     */     } 
/* 214 */     if (Threads.isShutdownHook()) {
/* 215 */       this.office = null;
/* 216 */       LOGGER.info("Dispose escaped (thread em shutdownhook)");
/*     */       return;
/*     */     } 
/* 219 */     doDispose();
/* 220 */     this.office = null;
/* 221 */     LOGGER.info("Frontend liberado");
/*     */   }
/*     */   
/*     */   protected abstract void done();
/*     */   
/*     */   protected abstract void doDispose();
/*     */   
/*     */   protected abstract void setOnline(boolean paramBoolean);
/*     */   
/*     */   protected abstract void doInstall(PopupMenu paramPopupMenu) throws Exception;
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/imp/PjeOfficeFrontEnd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */