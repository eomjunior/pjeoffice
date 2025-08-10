/*     */ package org.apache.hc.core5.http.ssl;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.http.ParseException;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.util.Tokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum TLS
/*     */ {
/*  44 */   V_1_0("TLSv1", new ProtocolVersion("TLS", 1, 0)),
/*  45 */   V_1_1("TLSv1.1", new ProtocolVersion("TLS", 1, 1)),
/*  46 */   V_1_2("TLSv1.2", new ProtocolVersion("TLS", 1, 2)),
/*  47 */   V_1_3("TLSv1.3", new ProtocolVersion("TLS", 1, 3));
/*     */   
/*     */   public final String id;
/*     */   public final ProtocolVersion version;
/*     */   
/*     */   TLS(String id, ProtocolVersion version) {
/*  53 */     this.id = id;
/*  54 */     this.version = version;
/*     */   }
/*     */   
/*     */   public boolean isSame(ProtocolVersion protocolVersion) {
/*  58 */     return this.version.equals(protocolVersion);
/*     */   }
/*     */   
/*     */   public boolean isComparable(ProtocolVersion protocolVersion) {
/*  62 */     return this.version.isComparable(protocolVersion);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/*  72 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProtocolVersion getVersion() {
/*  82 */     return this.version;
/*     */   }
/*     */   
/*     */   public boolean greaterEquals(ProtocolVersion protocolVersion) {
/*  86 */     return this.version.greaterEquals(protocolVersion);
/*     */   }
/*     */   
/*     */   public boolean lessEquals(ProtocolVersion protocolVersion) {
/*  90 */     return this.version.lessEquals(protocolVersion);
/*     */   }
/*     */   
/*     */   public static ProtocolVersion parse(String s) throws ParseException {
/*  94 */     if (s == null) {
/*  95 */       return null;
/*     */     }
/*  97 */     Tokenizer.Cursor cursor = new Tokenizer.Cursor(0, s.length());
/*  98 */     return TlsVersionParser.INSTANCE.parse(s, cursor, null);
/*     */   }
/*     */   
/*     */   public static String[] excludeWeak(String... protocols) {
/* 102 */     if (protocols == null) {
/* 103 */       return null;
/*     */     }
/* 105 */     List<String> enabledProtocols = new ArrayList<>();
/* 106 */     for (String protocol : protocols) {
/* 107 */       if (isSecure(protocol)) {
/* 108 */         enabledProtocols.add(protocol);
/*     */       }
/*     */     } 
/* 111 */     if (enabledProtocols.isEmpty()) {
/* 112 */       enabledProtocols.add(V_1_2.id);
/*     */     }
/* 114 */     return enabledProtocols.<String>toArray(new String[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isSecure(String protocol) {
/* 125 */     return (!protocol.startsWith("SSL") && !protocol.equals(V_1_0.id) && !protocol.equals(V_1_1.id));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/ssl/TLS.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */