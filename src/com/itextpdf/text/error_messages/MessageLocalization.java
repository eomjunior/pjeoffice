/*     */ package com.itextpdf.text.error_messages;
/*     */ 
/*     */ import com.itextpdf.text.io.StreamUtil;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MessageLocalization
/*     */ {
/*  63 */   private static HashMap<String, String> defaultLanguage = new HashMap<String, String>();
/*     */   
/*     */   private static HashMap<String, String> currentLanguage;
/*     */   
/*     */   private static final String BASE_PATH = "com/itextpdf/text/l10n/error/";
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/*  72 */       defaultLanguage = getLanguageMessages("en", null);
/*  73 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/*  76 */     if (defaultLanguage == null) {
/*  77 */       defaultLanguage = new HashMap<String, String>();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getMessage(String key) {
/*  86 */     return getMessage(key, true);
/*     */   }
/*     */   
/*     */   public static String getMessage(String key, boolean useDefaultLanguageIfMessageNotFound) {
/*  90 */     HashMap<String, String> cl = currentLanguage;
/*     */     
/*  92 */     if (cl != null) {
/*  93 */       String val = cl.get(key);
/*  94 */       if (val != null) {
/*  95 */         return val;
/*     */       }
/*     */     } 
/*  98 */     if (useDefaultLanguageIfMessageNotFound) {
/*  99 */       cl = defaultLanguage;
/* 100 */       String val = cl.get(key);
/* 101 */       if (val != null) {
/* 102 */         return val;
/*     */       }
/*     */     } 
/* 105 */     return "No message found for " + key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getComposedMessage(String key, int p1) {
/* 116 */     return getComposedMessage(key, new Object[] { String.valueOf(p1), null, null, null });
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
/*     */   
/*     */   public static String getComposedMessage(String key, Object... param) {
/* 131 */     String msg = getMessage(key);
/* 132 */     if (null != param) {
/* 133 */       int i = 1;
/* 134 */       for (Object o : param) {
/* 135 */         if (null != o) {
/* 136 */           msg = msg.replace("{" + i + "}", o.toString());
/*     */         }
/* 138 */         i++;
/*     */       } 
/*     */     } 
/* 141 */     return msg;
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
/*     */   public static boolean setLanguage(String language, String country) throws IOException {
/* 154 */     HashMap<String, String> lang = getLanguageMessages(language, country);
/* 155 */     if (lang == null)
/* 156 */       return false; 
/* 157 */     currentLanguage = lang;
/* 158 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setMessages(Reader r) throws IOException {
/* 167 */     currentLanguage = readLanguageStream(r);
/*     */   }
/*     */   
/*     */   private static HashMap<String, String> getLanguageMessages(String language, String country) throws IOException {
/* 171 */     if (language == null)
/* 172 */       throw new IllegalArgumentException("The language cannot be null."); 
/* 173 */     InputStream is = null;
/*     */     
/*     */     try {
/* 176 */       if (country != null) {
/* 177 */         file = language + "_" + country + ".lng";
/*     */       } else {
/* 179 */         file = language + ".lng";
/* 180 */       }  is = StreamUtil.getResourceStream("com/itextpdf/text/l10n/error/" + file, (new MessageLocalization()).getClass().getClassLoader());
/* 181 */       if (is != null)
/* 182 */         return readLanguageStream(is); 
/* 183 */       if (country == null)
/* 184 */         return null; 
/* 185 */       String file = language + ".lng";
/* 186 */       is = StreamUtil.getResourceStream("com/itextpdf/text/l10n/error/" + file, (new MessageLocalization()).getClass().getClassLoader());
/* 187 */       if (is != null) {
/* 188 */         return readLanguageStream(is);
/*     */       }
/* 190 */       return null;
/*     */     } finally {
/*     */       
/*     */       try {
/* 194 */         if (null != is) {
/* 195 */           is.close();
/*     */         }
/* 197 */       } catch (Exception exception) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static HashMap<String, String> readLanguageStream(InputStream is) throws IOException {
/* 204 */     return readLanguageStream(new InputStreamReader(is, "UTF-8"));
/*     */   }
/*     */   
/*     */   private static HashMap<String, String> readLanguageStream(Reader r) throws IOException {
/* 208 */     HashMap<String, String> lang = new HashMap<String, String>();
/* 209 */     BufferedReader br = new BufferedReader(r);
/*     */     String line;
/* 211 */     while ((line = br.readLine()) != null) {
/* 212 */       int idxeq = line.indexOf('=');
/* 213 */       if (idxeq < 0)
/*     */         continue; 
/* 215 */       String key = line.substring(0, idxeq).trim();
/* 216 */       if (key.startsWith("#"))
/*     */         continue; 
/* 218 */       lang.put(key, line.substring(idxeq + 1));
/*     */     } 
/* 220 */     return lang;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/error_messages/MessageLocalization.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */