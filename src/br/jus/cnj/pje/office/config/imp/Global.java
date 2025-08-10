/*     */ package br.jus.cnj.pje.office.config.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.config.IHttpClientParams;
/*     */ import br.jus.cnj.pje.office.config.IUpdateParams;
/*     */ import com.github.utils4j.imp.Directory;
/*     */ import com.github.utils4j.imp.Environment;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Properties;
/*     */ import org.apache.hc.core5.util.Timeout;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum Global
/*     */   implements IUpdateParams, IHttpClientParams
/*     */ {
/*  25 */   CONFIG;
/*     */   
/*  27 */   private final Properties properties = new Properties();
/*     */   
/*  29 */   private final Logger logger = LoggerFactory.getLogger(Global.class);
/*     */   
/*     */   Global() {
/*  32 */     setup();
/*     */   }
/*     */   
/*     */   private String stringValue(String name) {
/*  36 */     return stringValue(name, Strings.empty());
/*     */   }
/*     */   
/*     */   private int intValue(String name, int defaultValue) {
/*  40 */     return Math.abs(Strings.toInt(stringValue(name), defaultValue));
/*     */   }
/*     */   
/*     */   private String stringValue(String name, String defaultValue) {
/*  44 */     return Strings.trim(this.properties.getProperty(name, defaultValue));
/*     */   }
/*     */ 
/*     */   
/*     */   public String update_url() {
/*  49 */     return stringValue("update.url", "disabled");
/*     */   }
/*     */ 
/*     */   
/*     */   public String help_url() {
/*  54 */     return stringValue("help.url", "https://www.google.com/search?q=pjeoffice-pro");
/*     */   }
/*     */ 
/*     */   
/*     */   public String update_architecture() {
/*  59 */     return stringValue("update.architecture");
/*     */   }
/*     */ 
/*     */   
/*     */   public String http_client_user_agent() {
/*  64 */     return stringValue("http.client.userAgent", "Apache-HttpClient/4.5.6 (Java/1.8.0_301)");
/*     */   }
/*     */ 
/*     */   
/*     */   public int http_client_connections_maxTotal() {
/*  69 */     return intValue("http.client.connections.maxTotal", 600);
/*     */   }
/*     */ 
/*     */   
/*     */   public int http_client_connections_maxPerRoute() {
/*  74 */     return intValue("http.client.connections.maxPerRoute", 600);
/*     */   }
/*     */ 
/*     */   
/*     */   public Timeout http_client_connectTimeout() {
/*  79 */     return Timeout.ofSeconds(intValue("http.client.connectTimeout", 15));
/*     */   }
/*     */ 
/*     */   
/*     */   public Timeout http_client_responseTimeout() {
/*  84 */     return Timeout.ofSeconds(intValue("http.client.responseTimeout", 60));
/*     */   }
/*     */ 
/*     */   
/*     */   public Timeout http_client_connectionRequestTimeout() {
/*  89 */     return Timeout.ofSeconds(intValue("http.client.connectionRequestTimeout", 66));
/*     */   }
/*     */ 
/*     */   
/*     */   public Timeout http_client_connectionKeepAliveTimeout() {
/*  94 */     return Timeout.ofSeconds(intValue("http.client.connectionKeepAliveTimeout", 5));
/*     */   }
/*     */ 
/*     */   
/*     */   public Timeout http_client_evictIdleConnectionTimeout() {
/*  99 */     return Timeout.ofSeconds(intValue("http.client.evictIdleConnectionTimeout", 5));
/*     */   }
/*     */ 
/*     */   
/*     */   public Timeout http_client_validateAfterInactivityTimeout() {
/* 104 */     return Timeout.ofSeconds(intValue("http.client.validateAfterInactivityTimeout", 2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setup() {
/*     */     try {
/* 112 */       File file = ((Path)Environment.requirePathFrom("pjeoffice_home").orElseThrow(() -> new RuntimeException("Não foi encontrada variável de ambiente pjeoffice_home"))).resolve("pjeoffice-update.properties").toFile();
/* 113 */       try (FileInputStream fis = new FileInputStream(file)) {
/* 114 */         this.properties.load(fis);
/* 115 */       } catch (IOException e) {
/* 116 */         this.logger.warn("Não foi encontrado arquivo de atualização 'pjeoffice-update.properties' em " + Directory.stringPath(file, true), e);
/* 117 */         throw e;
/*     */       } 
/* 119 */     } catch (Exception e) {
/* 120 */       this.properties.clear();
/* 121 */       this.logger.warn("Não foi possível criar instância estável de 'Global.CONFIG'. Assumidos parâmetros default!", e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/config/imp/Global.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */