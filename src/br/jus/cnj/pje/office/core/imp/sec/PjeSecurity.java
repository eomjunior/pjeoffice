/*     */ package br.jus.cnj.pje.office.core.imp.sec;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.IPjeSecurityGrantor;
/*     */ import br.jus.cnj.pje.office.core.IPjeServerAccess;
/*     */ import br.jus.cnj.pje.office.task.IPayload;
/*     */ import com.github.utils4j.IConstants;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Base64;
/*     */ import com.github.utils4j.imp.Certificates;
/*     */ import com.github.utils4j.imp.Ciphers;
/*     */ import java.io.InputStream;
/*     */ import java.security.PublicKey;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.util.List;
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
/*     */ public enum PjeSecurity
/*     */   implements IPjeSecurityAgent
/*     */ {
/*  52 */   GRANTOR(PjeSecurityAgent.SAFE);
/*     */   
/*     */   PjeSecurity(IPjeSecurityAgent agent) {
/*  55 */     this.agent = agent;
/*     */   }
/*     */   private static PublicKey KEY;
/*     */   private static final Logger LOGGER;
/*     */   private volatile IPjeSecurityAgent agent;
/*     */   
/*     */   public void refresh() {
/*  62 */     this.agent.refresh();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPermitted(IPayload params, StringBuilder whyNot) {
/*  67 */     return this.agent.isPermitted(params, whyNot);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPermitted(String origin) {
/*  72 */     return this.agent.isPermitted(origin);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setDevMode(boolean devMode) {
/*  77 */     return this.agent.setDevMode(devMode);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public boolean isUnsafe() {
/*  82 */     return (this.agent == PjeSecurityAgent.UNSAFE);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<IPjeServerAccess> getServers() {
/*  87 */     return this.agent.getServers();
/*     */   }
/*     */   
/*     */   public boolean isSafe() {
/*  91 */     return !isUnsafe();
/*     */   }
/*     */   
/*     */   public boolean isDevMode() {
/*  95 */     return this.agent.isDevMode();
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public IPjeSecurityGrantor setUnsafe(boolean unsafe) {
/* 100 */     boolean devMode = isDevMode();
/* 101 */     this.agent = unsafe ? PjeSecurityAgent.UNSAFE : PjeSecurityAgent.SAFE;
/* 102 */     setDevMode(devMode);
/* 103 */     return this;
/*     */   }
/*     */   
/*     */   public static String decrypt(String base64Data) throws Exception {
/* 107 */     return new String(Ciphers.decryptWithRsa(Base64.base64Decode(Args.requireText(base64Data, "base64Data is empty")), KEY), IConstants.DEFAULT_CHARSET);
/*     */   }
/*     */   static {
/* 110 */     LOGGER = LoggerFactory.getLogger(PjeSecurity.class);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 115 */     try (InputStream is = PjeSecurity.class.getResourceAsStream("/pjeoffice.cer")) {
/* 116 */       KEY = Certificates.create(is).getPublicKey();
/* 117 */     } catch (CertificateException|java.io.IOException e) {
/* 118 */       LOGGER.error("Unabled to read public key from 'PjeOffice.cer'", e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/sec/PjeSecurity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */