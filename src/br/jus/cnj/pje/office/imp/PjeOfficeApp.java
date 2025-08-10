/*    */ package br.jus.cnj.pje.office.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeCommandFactory;
/*    */ import br.jus.cnj.pje.office.core.IPjeLifeCycleHook;
/*    */ import br.jus.cnj.pje.office.core.IPjeOffice;
/*    */ import br.jus.cnj.pje.office.core.imp.PjeConfig;
/*    */ import br.jus.cnj.pje.office.core.imp.PjeLifeCycleFactory;
/*    */ import br.jus.cnj.pje.office.core.imp.PjeOffice;
/*    */ import com.github.utils4j.gui.imp.LookAndFeelsInstaller;
/*    */ import com.github.utils4j.gui.imp.MessageAlert;
/*    */ import com.github.utils4j.imp.Environment;
/*    */ import com.github.utils4j.imp.Threads;
/*    */ import com.github.utils4j.imp.Throwables;
/*    */ import javax.swing.ToolTipManager;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class PjeOfficeApp
/*    */   implements IPjeLifeCycleHook
/*    */ {
/* 53 */   protected static final long STARTUP = System.currentTimeMillis();
/*    */   
/*    */   static {
/* 56 */     install();
/*    */   }
/*    */   
/*    */   private static void install() {
/* 60 */     PjeConfig.setup();
/* 61 */     Throwables.quietly(() -> LookAndFeelsInstaller.install(Environment.valueFrom("pjeoffice_looksandfeels").orElse("undefined")));
/* 62 */     ToolTipManager.sharedInstance().setInitialDelay(0);
/* 63 */     ToolTipManager.sharedInstance().setDismissDelay(6000);
/*    */   }
/*    */   
/* 66 */   protected static final Logger LOGGER = LoggerFactory.getLogger(PjeOfficeApp.class);
/*    */   
/*    */   protected IPjeOffice office;
/*    */   
/*    */   private Threads.ShutdownHookThread jvmHook;
/*    */   
/*    */   protected PjeOfficeApp(PjeLifeCycleFactory factory, String model) {
/* 73 */     this.office = (IPjeOffice)new PjeOffice(this, (IPjeCommandFactory)factory, model, this::systray);
/* 74 */     this.jvmHook = Threads.shutdownHookAdd(this.office::exit, "JVMShutDownHook");
/*    */   }
/*    */ 
/*    */   
/*    */   protected void systray(boolean closing) {}
/*    */   
/*    */   public void onKill() {
/* 81 */     LOGGER.info("Reciclando jvmHook");
/* 82 */     if (this.jvmHook != null) {
/* 83 */       Threads.shutdownHookRem(this.jvmHook);
/* 84 */       this.jvmHook = null;
/*    */     } 
/* 86 */     this.office = null;
/* 87 */     LOGGER.info("App closed");
/*    */   }
/*    */ 
/*    */   
/*    */   public final void onFailStart(Exception e) {
/* 92 */     MessageAlert.showInfo("Uma versão antiga do PJeOffice precisa ser fechada e/ou desinstalada do seu computador.\n" + e.getMessage(), PjeConfig.getIcon());
/* 93 */     System.exit(1);
/*    */   }
/*    */   
/*    */   protected void start() {
/* 97 */     this.office.boot();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/imp/PjeOfficeApp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */