/*     */ package br.jus.cnj.pje.office.core.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.IPjeConfigPersister;
/*     */ import br.jus.cnj.pje.office.core.IPjeServerAccess;
/*     */ import br.jus.cnj.pje.office.shell.ShellExtension;
/*     */ import com.github.signer4j.IAuthStrategy;
/*     */ import com.github.signer4j.IConfig;
/*     */ import com.github.signer4j.imp.ConfigPersister;
/*     */ import com.github.signer4j.imp.SignerConfig;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Properties;
/*     */ import java.util.function.Consumer;
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
/*     */ class PjeConfigPersister
/*     */   extends ConfigPersister
/*     */   implements IPjeConfigPersister
/*     */ {
/*     */   private static final String SERVER_LIST = "list.server";
/*     */   private static final String AUTH_STRATEGY = "auth.strategy";
/*     */   
/*     */   private static class PjeConfig
/*     */     extends SignerConfig
/*     */   {
/*     */     PjeConfig() {
/*  52 */       super(ShellExtension.HOME_CONFIG_FILE.toFile());
/*     */     }
/*     */   }
/*     */   
/*     */   PjeConfigPersister() {
/*  57 */     super((IConfig)new PjeConfig());
/*     */   }
/*     */ 
/*     */   
/*     */   public void save(IPjeServerAccess... access) {
/*  62 */     put(p -> Strings.trim(p.getProperty("list.server", "")), "list.server", (Object[])access);
/*     */   }
/*     */ 
/*     */   
/*     */   public void save(IAuthStrategy strategy) {
/*  67 */     put(p -> "", "auth.strategy", (Object[])Strings.toArray(new String[] { strategy.name() }));
/*     */   }
/*     */ 
/*     */   
/*     */   public void overwrite(IPjeServerAccess... access) {
/*  72 */     put(p -> "", "list.server", (Object[])access);
/*     */   }
/*     */ 
/*     */   
/*     */   public void delete(IPjeServerAccess access) {
/*  77 */     remove(access, "list.server");
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<String> authStrategy() {
/*  82 */     Properties properties = new Properties();
/*  83 */     if (!open(properties))
/*  84 */       return Optional.empty(); 
/*  85 */     return Optional.ofNullable(properties.getProperty("auth.strategy"));
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadServerAccess(Consumer<IPjeServerAccess> add) {
/*  90 */     Properties properties = new Properties();
/*  91 */     if (!open(properties))
/*     */       return; 
/*  93 */     List<String> serverList = Strings.split(properties.getProperty("list.server", ""), '|');
/*  94 */     for (String server : serverList) {
/*  95 */       List<String> members = Strings.split(server, ';');
/*  96 */       if (members.size() != 4) {
/*  97 */         LOGGER.warn("Arquivo de configuração em formato inválido: {}", members);
/*     */         continue;
/*     */       } 
/* 100 */       add.accept(PjeServerAccess.fromString(members));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeConfigPersister.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */