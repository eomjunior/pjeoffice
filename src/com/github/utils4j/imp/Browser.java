/*     */ package com.github.utils4j.imp;
/*     */ 
/*     */ import com.github.utils4j.gui.imp.MessageAlert;
/*     */ import java.awt.Desktop;
/*     */ import java.awt.Image;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.datatransfer.StringSelection;
/*     */ import java.net.URI;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum Browser
/*     */ {
/*  16 */   CHROME("Chrome", "Avançado", "Ir para 127.0.0.1 (não seguro)"),
/*     */   
/*  18 */   FIREFOX("Firefox", "Avançado", "Aceitar o risco e continuar"),
/*     */   
/*  20 */   EDGE("MSEdge", "Avançado", "Continue até 127.0.0.1 (não seguro)"),
/*     */   
/*  22 */   OPERA("Opera", "Help me understand", "Prosseguir para 127.0.0.1 (inseguro)"),
/*     */   
/*  24 */   UNKNOWN("", "", "")
/*     */   {
/*     */     public final String exceptionExplain() {
/*  27 */       return Stream.<Browser>of(Browser.VALUES).filter(p -> (p != this)).map(Browser::exceptionExplain).reduce((a, b) -> a + "\n  " + b).get();
/*     */     } };
/*     */   private static final Browser[] VALUES; private final String id;
/*     */   static {
/*  31 */     VALUES = values();
/*     */   }
/*     */   private final String advanced; private final String link;
/*     */   
/*     */   Browser(String id, String advanced, String link) {
/*  36 */     this.id = id;
/*  37 */     this.link = link;
/*  38 */     this.advanced = advanced;
/*     */   }
/*     */   
/*     */   public String exceptionExplain() {
/*  42 */     return name() + ": '" + this.advanced + "' -> '" + this.link + "'";
/*     */   }
/*     */   
/*     */   public void open(String url) {
/*  46 */     open(url, (Image)null);
/*     */   }
/*     */   
/*     */   public static void navigateTo(String endpoint) {
/*  50 */     navigateTo(endpoint, true);
/*     */   }
/*     */   
/*     */   public static void navigateTo(String endpoint, boolean alertHttps) {
/*  54 */     whoIsDefault().open(endpoint, alertHttps);
/*     */   }
/*     */   
/*     */   public static void navigateTo(String endpoint, Image icon) {
/*  58 */     whoIsDefault().open(endpoint, icon);
/*     */   }
/*     */   
/*     */   public static Browser whoIsDefault() {
/*  62 */     return Jvms.isWindows() ? defaultOnWindows() : UNKNOWN;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Browser defaultOnWindows() {
/*     */     try {
/*  71 */       String output = Streams.readOutStream(Runtime.getRuntime().exec("REG QUERY HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\Shell\\Associations\\UrlAssociations\\http\\UserChoice").getInputStream()).get(2L, TimeUnit.SECONDS);
/*  72 */       return Stream.<Browser>of(VALUES).filter(b -> output.contains(b.id)).findFirst().orElse(UNKNOWN);
/*  73 */     } catch (Exception e) {
/*  74 */       return UNKNOWN;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void open(String url, boolean alertHttps) {
/*  79 */     open(url, (Image)null, alertHttps);
/*     */   }
/*     */   
/*     */   public void open(String url, Image icon) {
/*  83 */     open(url, icon, true);
/*     */   }
/*     */   
/*     */   public void open(String url, Image icon, boolean alertHttps) {
/*  87 */     url = Args.requireText(url, "url is null").trim();
/*  88 */     Browser self = this;
/*     */     try {
/*  90 */       Desktop.getDesktop().browse(new URI(url));
/*  91 */     } catch (Exception e) {
/*  92 */       self = UNKNOWN;
/*  93 */       String ctrl = Jvms.isMac() ? "Command" : "Ctrl";
/*  94 */       Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(url), null);
/*  95 */       MessageAlert.showInfo("No seu navegador acesse o endereço '" + url + "'.\nEste endereço já foi copiado para o seu clipboard '" + ctrl + "+C', aguardando apenas o '" + ctrl + "+V' na barra de endereços.", icon);
/*     */ 
/*     */     
/*     */     }
/*     */     finally {
/*     */ 
/*     */ 
/*     */       
/* 103 */       if (alertHttps && url.toLowerCase().startsWith("https:"))
/* 104 */         MessageAlert.showInfo("Caso se depare com uma página com erro no certificado, confirme a exceção de segurança\nclicando em " + ((self == UNKNOWN) ? "\n  " : "") + self
/*     */ 
/*     */             
/* 107 */             .exceptionExplain(), icon); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Browser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */