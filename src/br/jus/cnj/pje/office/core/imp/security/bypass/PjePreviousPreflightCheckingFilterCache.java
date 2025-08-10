/*     */ package br.jus.cnj.pje.office.core.imp.security.bypass;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.imp.PjeHttpExchangeRequest;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeWebGlobal;
/*     */ import br.jus.cnj.pje.office.core.imp.sec.PjeSecurity;
/*     */ import br.jus.cnj.pje.office.core.imp.security.PjePreviousPreflightCheckingFilter;
/*     */ import br.jus.cnj.pje.office.task.IPayload;
/*     */ import br.jus.cnj.pje.office.task.imp.PayloadRequestReader;
/*     */ import br.jus.cnj.pje.office.task.imp.PjeTaskReader;
/*     */ import com.github.utils4j.IRequestRejectNotifier;
/*     */ import com.github.utils4j.imp.JsonTextReader;
/*     */ import com.github.utils4j.imp.Randomizer;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import com.sun.net.httpserver.HttpExchange;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.hc.client5.http.classic.methods.HttpGet;
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
/*     */ @Deprecated
/*     */ public class PjePreviousPreflightCheckingFilterCache
/*     */   extends PjePreviousPreflightCheckingFilter
/*     */ {
/*  36 */   private static final Logger LOGGER = LoggerFactory.getLogger(PjePreviousPreflightCheckingFilterCache.class);
/*     */ 
/*     */   
/*     */   private final LoadingCache<String, Boolean> isBackendUpdatedCache;
/*     */   
/*  41 */   private final JsonTextReader payloadReader = new JsonTextReader(PayloadRequestReader.payloadClass());
/*     */   
/*     */   public PjePreviousPreflightCheckingFilterCache(IRequestRejectNotifier notifier) {
/*  44 */     super(notifier);
/*  45 */     this.isBackendUpdatedCache = setupRequestCache();
/*     */   }
/*     */   
/*     */   private LoadingCache<String, Boolean> setupRequestCache() {
/*  49 */     return CacheBuilder.newBuilder()
/*     */       
/*  51 */       .expireAfterWrite(10L, TimeUnit.MINUTES)
/*  52 */       .build(new CacheLoader<String, Boolean>()
/*     */         {
/*     */           public Boolean load(String backendAddress) {
/*  55 */             return PjePreviousPreflightCheckingFilterCache.this.isBackendUpdated(backendAddress);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private static HttpGet prepareGET(String request) {
/*  61 */     HttpGet r = new HttpGet(request);
/*     */     
/*  63 */     r.setHeader("Accept-Encoding", "gzip,deflate");
/*  64 */     r.setHeader("Cache-Control", "no-cache");
/*  65 */     return r;
/*     */   }
/*     */   
/*     */   private Boolean isBackendUpdated(String backendAddress) {
/*  69 */     if (!backendAddress.toLowerCase().startsWith("https:")) {
/*  70 */       LOGGER.warn("O payload NÃO faz uso do protocolo HTTPS (servidor: '{}'). Backend considerado desatualizado", backendAddress);
/*     */       
/*  72 */       return Boolean.FALSE;
/*     */     } 
/*     */     
/*  75 */     String request = backendAddress + "pjeofficepro?nocache=" + Randomizer.nocache();
/*     */     
/*     */     try {
/*  78 */       String response = PjeWebGlobal.HTTPS_CODEC.get(() -> prepareGET(request));
/*  79 */       LOGGER.info("Requisição '{}' retornando '{}'", request, response);
/*     */       
/*  81 */       String message = "Backend '{}' considerado ";
/*     */       boolean updated;
/*  83 */       message = message + ((updated = Strings.isTrue(response)) ? "atualizado" : "desatualizado");
/*     */       
/*  85 */       LOGGER.info(message, backendAddress);
/*  86 */       return Boolean.valueOf(updated);
/*     */     }
/*  88 */     catch (Exception e) {
/*  89 */       LOGGER.warn("Falha na requisição '" + request + "'. Backend '" + backendAddress + "' considerado desatualizado", e);
/*  90 */       return Boolean.FALSE;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean acceptGetMethod(HttpExchange nativeRequest, StringBuilder whyNot) {
/*     */     IPayload payload;
/*  97 */     if (PjeSecurity.GRANTOR.isSafe()) {
/*  98 */       return super.acceptGetMethod(nativeRequest, whyNot);
/*     */     }
/* 100 */     Optional<String> r = (new PjeHttpExchangeRequest(nativeRequest)).getParameterR();
/* 101 */     if (!r.isPresent()) {
/* 102 */       whyNot.append("O parâmetro 'r' da requisição está AUSENTE.");
/* 103 */       return false;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 108 */       payload = (IPayload)this.payloadReader.read(r.get());
/* 109 */     } catch (Exception e) {
/* 110 */       whyNot.append("Não foi possível ler o payload json do parâmetro 'r'. Causa: " + e.getMessage());
/* 111 */       return false;
/*     */     } 
/*     */     
/* 114 */     Optional<String> servidor = payload.getServidor();
/* 115 */     if (!servidor.isPresent()) {
/* 116 */       whyNot.append("O atributo 'servidor' do payload está AUSENTE.");
/* 117 */       return false;
/*     */     } 
/*     */     
/* 120 */     Optional<String> taskId = payload.getTarefaId();
/* 121 */     if (!taskId.isPresent()) {
/* 122 */       whyNot.append("O atributo 'tarefaId' do payload está AUSENTE");
/* 123 */       return false;
/*     */     } 
/*     */     
/* 126 */     String tid = taskId.get();
/*     */     
/* 128 */     String backendAddress = Strings.replace(servidor.get(), '\\', '/');
/*     */     
/* 130 */     if (!backendAddress.endsWith("/")) {
/* 131 */       backendAddress = backendAddress + "/";
/*     */     }
/*     */ 
/*     */     
/* 135 */     boolean invalidateCache = (PjeTaskReader.SSO_AUTENTICADOR.getId().equals(tid) || PjeTaskReader.CNJ_AUTENTICADOR.getId().equals(tid));
/* 136 */     if (invalidateCache) {
/* 137 */       this.isBackendUpdatedCache.invalidate(backendAddress);
/*     */     }
/*     */     
/*     */     try {
/* 141 */       return !((Boolean)this.isBackendUpdatedCache.get(backendAddress)).booleanValue();
/* 142 */     } catch (Exception e) {
/* 143 */       LOGGER.error("Falha durante a leitura do cache", e);
/* 144 */       return true;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/security/bypass/PjePreviousPreflightCheckingFilterCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */