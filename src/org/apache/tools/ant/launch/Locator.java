/*     */ package org.apache.tools.ant.launch;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.text.CharacterIterator;
/*     */ import java.text.StringCharacterIterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Locator
/*     */ {
/*     */   private static final int NIBBLE = 4;
/*     */   private static final int NIBBLE_MASK = 15;
/*     */   private static final int ASCII_SIZE = 128;
/*     */   private static final int BYTE_SIZE = 256;
/*     */   private static final int WORD = 16;
/*     */   private static final int SPACE = 32;
/*     */   private static final int DEL = 127;
/*  66 */   private static boolean[] gNeedEscaping = new boolean[128];
/*     */   
/*  68 */   private static char[] gAfterEscaping1 = new char[128];
/*     */   
/*  70 */   private static char[] gAfterEscaping2 = new char[128];
/*  71 */   private static char[] gHexChs = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
/*     */ 
/*     */   
/*     */   public static final String ERROR_NOT_FILE_URI = "Can only handle valid file: URIs, not ";
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  79 */     for (int i = 0; i < 32; i++) {
/*  80 */       gNeedEscaping[i] = true;
/*  81 */       gAfterEscaping1[i] = gHexChs[i >> 4];
/*  82 */       gAfterEscaping2[i] = gHexChs[i & 0xF];
/*     */     } 
/*  84 */     gNeedEscaping[127] = true;
/*  85 */     gAfterEscaping1[127] = '7';
/*  86 */     gAfterEscaping2[127] = 'F';
/*  87 */     char[] escChs = { ' ', '<', '>', '#', '%', '"', '{', '}', '|', '\\', '^', '~', '[', ']', '`' };
/*     */     
/*  89 */     for (char ch : escChs) {
/*  90 */       gNeedEscaping[ch] = true;
/*  91 */       gAfterEscaping1[ch] = gHexChs[ch >> 4];
/*  92 */       gAfterEscaping2[ch] = gHexChs[ch & 0xF];
/*     */     } 
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
/*     */   public static File getClassSource(Class<?> c) {
/* 106 */     String classResource = c.getName().replace('.', '/') + ".class";
/* 107 */     return getResourceSource(c.getClassLoader(), classResource);
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
/*     */   public static File getResourceSource(ClassLoader c, String resource) {
/*     */     URL url;
/* 122 */     if (c == null) {
/* 123 */       c = Locator.class.getClassLoader();
/*     */     }
/*     */     
/* 126 */     if (c == null) {
/* 127 */       url = ClassLoader.getSystemResource(resource);
/*     */     } else {
/* 129 */       url = c.getResource(resource);
/*     */     } 
/* 131 */     if (url != null) {
/* 132 */       String u = url.toString();
/*     */       try {
/* 134 */         if (u.startsWith("jar:file:")) {
/* 135 */           return new File(fromJarURI(u));
/*     */         }
/* 137 */         if (u.startsWith("file:")) {
/* 138 */           int tail = u.indexOf(resource);
/* 139 */           String dirName = u.substring(0, tail);
/* 140 */           return new File(fromURI(dirName));
/*     */         } 
/* 142 */       } catch (IllegalArgumentException illegalArgumentException) {}
/*     */     } 
/*     */ 
/*     */     
/* 146 */     return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String fromURI(String uri) {
/* 167 */     URL url = null;
/*     */     try {
/* 169 */       url = new URL(uri);
/* 170 */     } catch (MalformedURLException malformedURLException) {}
/*     */ 
/*     */     
/* 173 */     if (url == null || !"file".equals(url.getProtocol())) {
/* 174 */       throw new IllegalArgumentException("Can only handle valid file: URIs, not " + uri);
/*     */     }
/* 176 */     StringBuilder buf = new StringBuilder(url.getHost());
/* 177 */     if (buf.length() > 0) {
/* 178 */       buf.insert(0, File.separatorChar).insert(0, File.separatorChar);
/*     */     }
/* 180 */     String file = url.getFile();
/* 181 */     int queryPos = file.indexOf('?');
/* 182 */     buf.append((queryPos < 0) ? file : file.substring(0, queryPos));
/*     */     
/* 184 */     uri = buf.toString().replace('/', File.separatorChar);
/*     */     
/* 186 */     if (File.pathSeparatorChar == ';' && uri.startsWith("\\") && uri.length() > 2 && 
/* 187 */       Character.isLetter(uri.charAt(1)) && uri.lastIndexOf(':') > -1) {
/* 188 */       uri = uri.substring(1);
/*     */     }
/* 190 */     String path = null;
/*     */     try {
/* 192 */       path = decodeUri(uri);
/*     */ 
/*     */       
/* 195 */       String cwd = System.getProperty("user.dir");
/* 196 */       int posi = cwd.indexOf(':');
/* 197 */       boolean pathStartsWithFileSeparator = path.startsWith(File.separator);
/* 198 */       boolean pathStartsWithUNC = path.startsWith("" + File.separator + File.separator);
/* 199 */       if (posi > 0 && pathStartsWithFileSeparator && !pathStartsWithUNC) {
/* 200 */         path = cwd.substring(0, posi + 1) + path;
/*     */       }
/* 202 */     } catch (UnsupportedEncodingException exc) {
/*     */ 
/*     */       
/* 205 */       throw new IllegalStateException("Could not convert URI " + uri + " to path: " + exc
/*     */           
/* 207 */           .getMessage());
/*     */     } 
/* 209 */     return path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String fromJarURI(String uri) {
/* 220 */     int pling = uri.indexOf("!/");
/* 221 */     String jarName = uri.substring("jar:".length(), pling);
/* 222 */     return fromURI(jarName);
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
/*     */   public static String decodeUri(String uri) throws UnsupportedEncodingException {
/* 234 */     if (!uri.contains("%")) {
/* 235 */       return uri;
/*     */     }
/* 237 */     ByteArrayOutputStream sb = new ByteArrayOutputStream(uri.length());
/* 238 */     CharacterIterator iter = new StringCharacterIterator(uri);
/* 239 */     for (char c = iter.first(); c != Character.MAX_VALUE; 
/* 240 */       c = iter.next()) {
/* 241 */       if (c == '%') {
/* 242 */         char c1 = iter.next();
/* 243 */         if (c1 != Character.MAX_VALUE) {
/* 244 */           int i1 = Character.digit(c1, 16);
/* 245 */           char c2 = iter.next();
/* 246 */           if (c2 != Character.MAX_VALUE) {
/* 247 */             int i2 = Character.digit(c2, 16);
/* 248 */             sb.write((char)((i1 << 4) + i2));
/*     */           } 
/*     */         } 
/* 251 */       } else if (c >= '\000' && c < '') {
/* 252 */         sb.write(c);
/*     */       } else {
/* 254 */         byte[] bytes = String.valueOf(c).getBytes(StandardCharsets.UTF_8);
/* 255 */         sb.write(bytes, 0, bytes.length);
/*     */       } 
/*     */     } 
/* 258 */     return sb.toString(StandardCharsets.UTF_8.name());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodeURI(String path) {
/* 269 */     int i = 0;
/* 270 */     int len = path.length();
/* 271 */     int ch = 0;
/* 272 */     StringBuilder sb = null;
/* 273 */     for (; i < len; i++) {
/* 274 */       ch = path.charAt(i);
/*     */       
/* 276 */       if (ch >= 128) {
/*     */         break;
/*     */       }
/* 279 */       if (gNeedEscaping[ch]) {
/* 280 */         if (sb == null) {
/* 281 */           sb = new StringBuilder(path.substring(0, i));
/*     */         }
/* 283 */         sb.append('%');
/* 284 */         sb.append(gAfterEscaping1[ch]);
/* 285 */         sb.append(gAfterEscaping2[ch]);
/*     */       }
/* 287 */       else if (sb != null) {
/* 288 */         sb.append((char)ch);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 293 */     if (i < len) {
/* 294 */       if (sb == null) {
/* 295 */         sb = new StringBuilder(path.substring(0, i));
/*     */       }
/*     */       
/* 298 */       byte[] bytes = path.substring(i).getBytes(StandardCharsets.UTF_8);
/* 299 */       len = bytes.length;
/*     */ 
/*     */       
/* 302 */       for (i = 0; i < len; i++) {
/* 303 */         byte b = bytes[i];
/*     */         
/* 305 */         if (b < 0) {
/* 306 */           ch = b + 256;
/* 307 */           sb.append('%');
/* 308 */           sb.append(gHexChs[ch >> 4]);
/* 309 */           sb.append(gHexChs[ch & 0xF]);
/* 310 */         } else if (gNeedEscaping[b]) {
/* 311 */           sb.append('%');
/* 312 */           sb.append(gAfterEscaping1[b]);
/* 313 */           sb.append(gAfterEscaping2[b]);
/*     */         } else {
/* 315 */           sb.append((char)b);
/*     */         } 
/*     */       } 
/*     */     } 
/* 319 */     return (sb == null) ? path : sb.toString();
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
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static URL fileToURL(File file) throws MalformedURLException {
/* 338 */     return new URL(file.toURI().toASCIIString());
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
/*     */   public static File getToolsJar() {
/* 351 */     boolean toolsJarAvailable = false;
/*     */     
/*     */     try {
/* 354 */       Class.forName("com.sun.tools.javac.Main");
/* 355 */       toolsJarAvailable = true;
/* 356 */     } catch (Exception e) {
/*     */       try {
/* 358 */         Class.forName("sun.tools.javac.Main");
/* 359 */         toolsJarAvailable = true;
/* 360 */       } catch (Exception exception) {}
/*     */     } 
/*     */ 
/*     */     
/* 364 */     if (toolsJarAvailable) {
/* 365 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 369 */     String libToolsJar = File.separator + "lib" + File.separator + "tools.jar";
/*     */     
/* 371 */     String javaHome = System.getProperty("java.home");
/* 372 */     File toolsJar = new File(javaHome + libToolsJar);
/* 373 */     if (toolsJar.exists())
/*     */     {
/* 375 */       return toolsJar;
/*     */     }
/* 377 */     if (javaHome.toLowerCase(Locale.ENGLISH).endsWith(File.separator + "jre")) {
/* 378 */       javaHome = javaHome.substring(0, javaHome
/* 379 */           .length() - "/jre".length());
/* 380 */       toolsJar = new File(javaHome + libToolsJar);
/*     */     } 
/* 382 */     if (!toolsJar.exists()) {
/* 383 */       return null;
/*     */     }
/* 385 */     return toolsJar;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static URL[] getLocationURLs(File location) throws MalformedURLException {
/* 403 */     return getLocationURLs(location, new String[] { ".jar" });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URL[] getLocationURLs(File location, String... extensions) throws MalformedURLException {
/* 423 */     URL[] urls = new URL[0];
/*     */     
/* 425 */     if (!location.exists()) {
/* 426 */       return urls;
/*     */     }
/* 428 */     if (!location.isDirectory()) {
/* 429 */       urls = new URL[1];
/* 430 */       String path = location.getPath();
/* 431 */       String littlePath = path.toLowerCase(Locale.ENGLISH);
/* 432 */       for (String extension : extensions) {
/* 433 */         if (littlePath.endsWith(extension)) {
/* 434 */           urls[0] = fileToURL(location);
/*     */           break;
/*     */         } 
/*     */       } 
/* 438 */       return urls;
/*     */     } 
/* 440 */     File[] matches = location.listFiles((dir, name) -> {
/*     */           String littleName = name.toLowerCase(Locale.ENGLISH); Objects.requireNonNull(littleName);
/*     */           return Stream.<String>of(extensions).anyMatch(littleName::endsWith);
/*     */         });
/* 444 */     urls = new URL[matches.length];
/* 445 */     for (int i = 0; i < matches.length; i++) {
/* 446 */       urls[i] = fileToURL(matches[i]);
/*     */     }
/* 448 */     return urls;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/launch/Locator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */