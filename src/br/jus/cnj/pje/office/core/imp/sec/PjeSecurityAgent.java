/*     */ package br.jus.cnj.pje.office.core.imp.sec;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.IPjePermissionAccessor;
/*     */ import br.jus.cnj.pje.office.core.IPjeServerAccess;
/*     */ import br.jus.cnj.pje.office.core.IPjeServerAccessPersister;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeAccessTime;
/*     */ import br.jus.cnj.pje.office.core.imp.PjePermissionDeniedException;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeServerAccess;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeServerAccessPersisters;
/*     */ import br.jus.cnj.pje.office.task.IPayload;
/*     */ import com.github.utils4j.echo.imp.Networks;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.BooleanTimeout;
/*     */ import com.github.utils4j.imp.Globs;
/*     */ import com.github.utils4j.imp.Streams;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.stream.Stream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ enum PjeSecurityAgent
/*     */   implements IPjeSecurityAgent
/*     */ {
/*  65 */   SAFE,
/*     */   
/*  67 */   UNSAFE
/*     */   {
/*     */     @Deprecated
/*     */     protected boolean isHttpsRequired()
/*     */     {
/*  72 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     protected final boolean checkOrigin() {
/*  78 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public final boolean isPermitted(String origin) {
/*  84 */       return true;
/*     */     } };
/*     */   
/*     */   static {
/*  88 */     MATCHING_DOMAINS = new ArrayList<>();
/*     */     
/*  90 */     NO_DISCARDING = new BooleanTimeout("security-no_discarding");
/*     */     
/*  92 */     YES_DISCARDING = new BooleanTimeout("security-yes_discarding");
/*     */     
/*  94 */     LOGGER = LoggerFactory.getLogger(PjeSecurityAgent.class);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 331 */     Streams.resourceLines("/preflight.list", lines -> lines.map(String::trim).filter(()).map(String::toLowerCase).forEach(()));
/*     */   }
/*     */   
/*     */   private static final List<Pattern> MATCHING_DOMAINS;
/*     */   private static final BooleanTimeout NO_DISCARDING;
/*     */   private static final BooleanTimeout YES_DISCARDING;
/*     */   private static final Logger LOGGER;
/*     */   private final IPjePermissionAccessor accessor;
/*     */   private volatile IPjeServerAccessPersister persister;
/*     */   
/*     */   PjeSecurityAgent(IPjePermissionAccessor acessor, IPjeServerAccessPersister persister) {
/*     */     this.accessor = (IPjePermissionAccessor)Args.requireNonNull(acessor, "acessor is null");
/*     */     this.persister = (IPjeServerAccessPersister)Args.requireNonNull(persister, "persister is null");
/*     */   }
/*     */   
/*     */   public final boolean setDevMode(boolean devMode) {
/*     */     this.persister = devMode ? PjeServerAccessPersisters.DEVMODE.reload() : PjeServerAccessPersisters.PRODUCTION.reload();
/*     */     return isDevMode();
/*     */   }
/*     */   
/*     */   public final List<IPjeServerAccess> getServers() {
/*     */     return this.persister.getServers();
/*     */   }
/*     */   
/*     */   public final boolean isDevMode() {
/*     */     return (this.persister == PjeServerAccessPersisters.DEVMODE);
/*     */   }
/*     */   
/*     */   public final void refresh() {
/*     */     this.persister.reload();
/*     */   }
/*     */   
/*     */   protected boolean checkOrigin() {
/*     */     return true;
/*     */   }
/*     */   
/*     */   protected boolean isHttpsRequired() {
/*     */     return true;
/*     */   }
/*     */   
/*     */   public boolean isPermitted(String origin) {
/*     */     return (Strings.hasText(origin) && (isDevMode() || MATCHING_DOMAINS.stream().map(p -> p.matcher(origin)).filter(Matcher::matches).findAny().isPresent()));
/*     */   }
/*     */   
/*     */   public final boolean isPermitted(IPayload payload, StringBuilder whyNot) {
/*     */     URI serverTargetUri;
/*     */     Args.requireNonNull(whyNot, "whyNot is null");
/*     */     if (payload == null) {
/*     */       whyNot.append("payload é indefinido/null");
/*     */       return false;
/*     */     } 
/*     */     Optional<String> payloadSecurityCodeOpt = payload.getCodigoSeguranca();
/*     */     if (!payloadSecurityCodeOpt.isPresent()) {
/*     */       whyNot.append("Servidor do Pje não enviou parâmetro 'codigoSeguranca'.");
/*     */       return false;
/*     */     } 
/*     */     String payloadSecurityCode = payloadSecurityCodeOpt.get();
/*     */     Optional<String> payloadAppOpt = payload.getAplicacao();
/*     */     if (!payloadAppOpt.isPresent()) {
/*     */       whyNot.append("Servidor do Pje não enviou parâmetro 'aplicacao'.");
/*     */       return false;
/*     */     } 
/*     */     String payloadApp = payloadAppOpt.get();
/*     */     Optional<String> serverTargetOpt = payload.getServidor();
/*     */     if (!serverTargetOpt.isPresent()) {
/*     */       whyNot.append("Servidor do Pje não enviou parâmetro 'servidor'.");
/*     */       return false;
/*     */     } 
/*     */     String serverTarget = ((String)serverTargetOpt.get()).toLowerCase();
/*     */     try {
/*     */       serverTargetUri = new URI(serverTarget);
/*     */     } catch (URISyntaxException e1) {
/*     */       whyNot.append("Parâmetro 'servidor' não corresponde a uma URI válida -> " + serverTarget);
/*     */       return false;
/*     */     } 
/*     */     Optional<String> serverTargetUriSchemaOpt = Strings.optional(serverTargetUri.getScheme());
/*     */     if (!serverTargetUriSchemaOpt.isPresent()) {
/*     */       whyNot.append("Parâmetro 'servidor' não define um 'schema' válido -> " + serverTarget);
/*     */       return false;
/*     */     } 
/*     */     String serverTargetUriSchema = ((String)serverTargetUriSchemaOpt.get()).toLowerCase();
/*     */     Optional<String> serverTargetUriHostOpt = Strings.optional(serverTargetUri.getHost());
/*     */     if (!serverTargetUriHostOpt.isPresent()) {
/*     */       whyNot.append("Parâmetro 'servidor' não define um 'host' válido -> " + serverTarget);
/*     */       return false;
/*     */     } 
/*     */     String serverTargetUriHost = ((String)serverTargetUriHostOpt.get()).toLowerCase();
/*     */     if (checkIfHttpsRequired(serverTargetUriSchema, serverTargetUriHost)) {
/*     */       whyNot.append("Parâmetro 'servidor' deve operar sobre schema 'httpS' ao invés de  '" + serverTargetUriSchema + "' em -> " + serverTarget);
/*     */       return false;
/*     */     } 
/*     */     int serverTargetUriPort = computePort(serverTargetUri.getPort(), serverTargetUriSchema);
/*     */     if (payload.isFromPreflightableRequest() || checkOrigin()) {
/*     */       URI browserOriginUri;
/*     */       Optional<String> browserOriginOpt = payload.getOrigin();
/*     */       if (!browserOriginOpt.isPresent()) {
/*     */         whyNot.append("A origem da requisição é desconhecida e foi rejeitada por segurança.");
/*     */         return false;
/*     */       } 
/*     */       String browserOrigin = ((String)browserOriginOpt.get()).toLowerCase();
/*     */       try {
/*     */         browserOriginUri = new URI(browserOrigin);
/*     */       } catch (URISyntaxException e1) {
/*     */         whyNot.append("Header 'Origin' enviado não corresponde a uma URI válida -> " + browserOrigin);
/*     */         return false;
/*     */       } 
/*     */       Optional<String> browserOriginUriSchemaOpt = Strings.optional(browserOriginUri.getScheme());
/*     */       if (!browserOriginUriSchemaOpt.isPresent()) {
/*     */         whyNot.append("Header 'Origin' enviado não define um 'schema' válido -> " + browserOrigin);
/*     */         return false;
/*     */       } 
/*     */       String browserOriginUriSchema = ((String)browserOriginUriSchemaOpt.get()).toLowerCase();
/*     */       Optional<String> browserOriginUriHostOpt = Strings.optional(browserOriginUri.getHost());
/*     */       if (!browserOriginUriHostOpt.isPresent()) {
/*     */         whyNot.append("Header 'Origin' enviado não define um 'host' válido -> " + browserOrigin);
/*     */         return false;
/*     */       } 
/*     */       String browserOriginUriHost = ((String)browserOriginUriHostOpt.get()).toLowerCase();
/*     */       int browserOriginUriPort = computePort(browserOriginUri.getPort(), browserOriginUriSchema);
/*     */       String fullBrowserOriginURI = browserOriginUriSchema + "://" + browserOriginUriHost + ":" + browserOriginUriPort;
/*     */       String fullServerTargetURI = serverTargetUriSchema + "://" + serverTargetUriHost + ":" + serverTargetUriPort;
/*     */       if (!fullBrowserOriginURI.equalsIgnoreCase(fullServerTargetURI)) {
/*     */         whyNot.append("A origem da requisição é inválida e foi rejeitada por segurança.\nAtente-se aos sites/e-mails/extensões maliciosos que queira acessar!");
/*     */         return false;
/*     */       } 
/*     */     } 
/*     */     synchronized (this.persister) {
/*     */       if (NO_DISCARDING.isTrue())
/*     */         return false; 
/*     */       if (YES_DISCARDING.isTrue())
/*     */         return true; 
/*     */       PjeServerAccess pjeServerAccess = new PjeServerAccess(payloadApp, serverTarget, payloadSecurityCode);
/*     */       Optional<IPjeServerAccess> access = this.persister.hasPermission(pjeServerAccess.getId());
/*     */       if (!access.isPresent())
/*     */         try {
/*     */           this.persister.checkAccessPermission((IPjeServerAccess)pjeServerAccess);
/*     */           PjeAccessTime time = this.accessor.tryAccess((IPjeServerAccess)pjeServerAccess);
/*     */           if (PjeAccessTime.AWAYS.equals(time) || PjeAccessTime.NEVER.equals(time))
/*     */             this.persister.save(pjeServerAccess.clone(PjeAccessTime.AWAYS.equals(time))); 
/*     */           boolean atThisTime;
/*     */           if (atThisTime = PjeAccessTime.AT_THIS_TIME.equals(time))
/*     */             YES_DISCARDING.setTrue(); 
/*     */           boolean result = (atThisTime || PjeAccessTime.AWAYS.equals(time));
/*     */           if (!result)
/*     */             NO_DISCARDING.setTrue(); 
/*     */           return result;
/*     */         } catch (PjePermissionDeniedException e) {
/*     */           NO_DISCARDING.setTrue();
/*     */           whyNot.append("Acesso não autorizado ao site '" + serverTarget + "'.\nO nome da aplicação informado (" + payloadApp + ") ou este endereço não\nfaz(em) parte do código de segurança fornecido pelo CNJ.");
/*     */           LOGGER.warn(whyNot.toString(), (Throwable)e);
/*     */           return false;
/*     */         }  
/*     */       boolean ok = ((IPjeServerAccess)access.get()).isAutorized();
/*     */       if (!ok)
/*     */         whyNot.append("Acesso não autorizado ao site: '" + serverTarget + "'.\nRevise as configurações no menu 'Segurança > Sites autorizados' do PJeOffice."); 
/*     */       return ok;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean checkIfHttpsRequired(String serverTargetUriSchema, String serverTargetUriHost) {
/*     */     return (!Networks.isLoopbackEndpoint(serverTargetUriHost) && isHttpsRequired() && !"https".equalsIgnoreCase(serverTargetUriSchema));
/*     */   }
/*     */   
/*     */   private static int computePort(int defaultPort, String schema) {
/*     */     return (defaultPort >= 0) ? defaultPort : ("http".equalsIgnoreCase(schema) ? 80 : ("https".equalsIgnoreCase(schema) ? 443 : defaultPort));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/sec/PjeSecurityAgent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */