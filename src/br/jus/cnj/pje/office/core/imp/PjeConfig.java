/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeConfigPersister;
/*    */ import br.jus.cnj.pje.office.core.IPjeServerAccess;
/*    */ import br.jus.cnj.pje.office.gui.PjeImages;
/*    */ import com.github.signer4j.IAuthStrategy;
/*    */ import com.github.signer4j.IConfigPersister;
/*    */ import com.github.signer4j.imp.Config;
/*    */ import com.github.signer4j.provider.ProviderInstaller;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Consumer;
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
/*    */ public class PjeConfig
/*    */   extends Config
/*    */ {
/*    */   static {
/* 45 */     ProviderInstaller.SIGNER4J.install();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static void setup() {
/* 51 */     setup(PjeImages.PJE_ICON_TRAY.asImage(), (IConfigPersister)new PjeConfigPersister());
/*    */   }
/*    */   
/*    */   protected static IPjeConfigPersister persister() {
/* 55 */     return (IPjeConfigPersister)Config.config();
/*    */   }
/*    */   
/*    */   public static void loadServerAccess(Consumer<IPjeServerAccess> add) {
/* 59 */     persister().loadServerAccess(add);
/*    */   }
/*    */   
/*    */   public static Optional<String> authStrategy() {
/* 63 */     return persister().authStrategy();
/*    */   }
/*    */   
/*    */   public static void save(IPjeServerAccess... access) {
/* 67 */     persister().save(access);
/*    */   }
/*    */   
/*    */   public static void save(IAuthStrategy strategy) {
/* 71 */     persister().save(strategy);
/*    */   }
/*    */   
/*    */   public static void overwrite(IPjeServerAccess... access) {
/* 75 */     persister().overwrite(access);
/*    */   }
/*    */   
/*    */   public static void delete(IPjeServerAccess access) {
/* 79 */     persister().delete(access);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */