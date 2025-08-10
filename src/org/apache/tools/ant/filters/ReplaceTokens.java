/*     */ package org.apache.tools.ant.filters;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import org.apache.tools.ant.types.Parameter;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.resources.FileResource;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ReplaceTokens
/*     */   extends BaseParamFilterReader
/*     */   implements ChainableReader
/*     */ {
/*     */   private static final String DEFAULT_BEGIN_TOKEN = "@";
/*     */   private static final String DEFAULT_END_TOKEN = "@";
/*  64 */   private Hashtable<String, String> hash = new Hashtable<>();
/*     */ 
/*     */   
/*  67 */   private final TreeMap<String, String> resolvedTokens = new TreeMap<>();
/*     */   
/*     */   private boolean resolvedTokensBuilt = false;
/*  70 */   private String readBuffer = "";
/*     */ 
/*     */   
/*  73 */   private String replaceData = null;
/*     */ 
/*     */   
/*  76 */   private int replaceIndex = -1;
/*     */ 
/*     */   
/*  79 */   private String beginToken = "@";
/*     */ 
/*     */   
/*  82 */   private String endToken = "@";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReplaceTokens() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReplaceTokens(Reader in) {
/*  99 */     super(in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/* 113 */     if (!getInitialized()) {
/* 114 */       initialize();
/* 115 */       setInitialized(true);
/*     */     } 
/*     */     
/* 118 */     if (!this.resolvedTokensBuilt) {
/*     */       
/* 120 */       for (Map.Entry<String, String> entry : this.hash.entrySet()) {
/* 121 */         this.resolvedTokens.put(this.beginToken + (String)entry.getKey() + this.endToken, entry.getValue());
/*     */       }
/* 123 */       this.resolvedTokensBuilt = true;
/*     */     } 
/*     */ 
/*     */     
/* 127 */     if (this.replaceData != null) {
/* 128 */       if (this.replaceIndex < this.replaceData.length()) {
/* 129 */         return this.replaceData.charAt(this.replaceIndex++);
/*     */       }
/* 131 */       this.replaceData = null;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 136 */     if (this.readBuffer.isEmpty()) {
/* 137 */       int next = this.in.read();
/* 138 */       if (next == -1) {
/* 139 */         return next;
/*     */       }
/* 141 */       this.readBuffer += (char)next;
/*     */     } 
/*     */ 
/*     */     
/*     */     while (true) {
/* 146 */       SortedMap<String, String> possibleTokens = this.resolvedTokens.tailMap(this.readBuffer);
/* 147 */       if (possibleTokens.isEmpty() || !((String)possibleTokens.firstKey()).startsWith(this.readBuffer))
/* 148 */         return getFirstCharacterFromReadBuffer(); 
/* 149 */       if (this.readBuffer.equals(possibleTokens.firstKey())) {
/*     */         
/* 151 */         this.replaceData = this.resolvedTokens.get(this.readBuffer);
/* 152 */         this.replaceIndex = 0;
/* 153 */         this.readBuffer = "";
/*     */         
/* 155 */         return read();
/*     */       } 
/* 157 */       int next = this.in.read();
/* 158 */       if (next != -1) {
/* 159 */         this.readBuffer += (char)next; continue;
/*     */       }  break;
/* 161 */     }  return getFirstCharacterFromReadBuffer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getFirstCharacterFromReadBuffer() {
/* 171 */     if (this.readBuffer.isEmpty()) {
/* 172 */       return -1;
/*     */     }
/*     */     
/* 175 */     int chr = this.readBuffer.charAt(0);
/* 176 */     this.readBuffer = this.readBuffer.substring(1);
/* 177 */     return chr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeginToken(String beginToken) {
/* 186 */     this.beginToken = beginToken;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getBeginToken() {
/* 195 */     return this.beginToken;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEndToken(String endToken) {
/* 204 */     this.endToken = endToken;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getEndToken() {
/* 213 */     return this.endToken;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPropertiesResource(Resource r) {
/* 224 */     makeTokensFromProperties(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredToken(Token token) {
/* 234 */     this.hash.put(token.getKey(), token.getValue());
/* 235 */     this.resolvedTokensBuilt = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Properties getProperties(Resource resource) {
/* 244 */     InputStream in = null;
/* 245 */     Properties props = new Properties();
/*     */     try {
/* 247 */       in = resource.getInputStream();
/* 248 */       props.load(in);
/* 249 */     } catch (IOException ioe) {
/* 250 */       if (getProject() != null) {
/* 251 */         getProject().log("getProperties failed, " + ioe.getMessage(), 0);
/*     */       } else {
/* 253 */         ioe.printStackTrace();
/*     */       } 
/*     */     } finally {
/* 256 */       FileUtils.close(in);
/*     */     } 
/*     */     
/* 259 */     return props;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setTokens(Hashtable<String, String> hash) {
/* 269 */     this.hash = hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Hashtable<String, String> getTokens() {
/* 279 */     return this.hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Reader chain(Reader rdr) {
/* 293 */     ReplaceTokens newFilter = new ReplaceTokens(rdr);
/* 294 */     newFilter.setBeginToken(getBeginToken());
/* 295 */     newFilter.setEndToken(getEndToken());
/* 296 */     newFilter.setTokens(getTokens());
/* 297 */     newFilter.setInitialized(true);
/* 298 */     return newFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initialize() {
/* 305 */     Parameter[] params = getParameters();
/* 306 */     if (params != null) {
/* 307 */       for (Parameter param : params) {
/* 308 */         if (param != null) {
/* 309 */           String type = param.getType();
/* 310 */           if ("tokenchar".equals(type)) {
/* 311 */             String name = param.getName();
/* 312 */             if ("begintoken".equals(name)) {
/* 313 */               this.beginToken = param.getValue();
/* 314 */             } else if ("endtoken".equals(name)) {
/* 315 */               this.endToken = param.getValue();
/*     */             } 
/* 317 */           } else if ("token".equals(type)) {
/* 318 */             String name = param.getName();
/* 319 */             String value = param.getValue();
/* 320 */             this.hash.put(name, value);
/* 321 */           } else if ("propertiesfile".equals(type)) {
/* 322 */             makeTokensFromProperties((Resource)new FileResource(new File(param
/* 323 */                     .getValue())));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void makeTokensFromProperties(Resource r) {
/* 331 */     Properties props = getProperties(r);
/* 332 */     props.stringPropertyNames().forEach(key -> this.hash.put(key, props.getProperty(key)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Token
/*     */   {
/*     */     private String key;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private String value;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final void setKey(String key) {
/* 352 */       this.key = key;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final void setValue(String value) {
/* 361 */       this.value = value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final String getKey() {
/* 370 */       return this.key;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final String getValue() {
/* 379 */       return this.value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/filters/ReplaceTokens.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */