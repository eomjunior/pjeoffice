/*     */ package org.apache.log4j.helpers;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URL;
/*     */ import java.util.Properties;
/*     */ import org.apache.log4j.Level;
/*     */ import org.apache.log4j.PropertyConfigurator;
/*     */ import org.apache.log4j.spi.Configurator;
/*     */ import org.apache.log4j.spi.LoggerRepository;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OptionConverter
/*     */ {
/*  43 */   static String DELIM_START = "${";
/*  44 */   static char DELIM_STOP = '}';
/*  45 */   static int DELIM_START_LEN = 2;
/*  46 */   static int DELIM_STOP_LEN = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] concatanateArrays(String[] l, String[] r) {
/*  53 */     int len = l.length + r.length;
/*  54 */     String[] a = new String[len];
/*     */     
/*  56 */     System.arraycopy(l, 0, a, 0, l.length);
/*  57 */     System.arraycopy(r, 0, a, l.length, r.length);
/*     */     
/*  59 */     return a;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String convertSpecialChars(String s) {
/*  64 */     int len = s.length();
/*  65 */     StringBuilder sbuf = new StringBuilder(len);
/*     */     
/*  67 */     int i = 0;
/*  68 */     while (i < len) {
/*  69 */       char c = s.charAt(i++);
/*  70 */       if (c == '\\') {
/*  71 */         c = s.charAt(i++);
/*  72 */         if (c == 'n') {
/*  73 */           c = '\n';
/*  74 */         } else if (c == 'r') {
/*  75 */           c = '\r';
/*  76 */         } else if (c == 't') {
/*  77 */           c = '\t';
/*  78 */         } else if (c == 'f') {
/*  79 */           c = '\f';
/*  80 */         } else if (c == '\b') {
/*  81 */           c = '\b';
/*  82 */         } else if (c == '"') {
/*  83 */           c = '"';
/*  84 */         } else if (c == '\'') {
/*  85 */           c = '\'';
/*  86 */         } else if (c == '\\') {
/*  87 */           c = '\\';
/*     */         } 
/*  89 */       }  sbuf.append(c);
/*     */     } 
/*  91 */     return sbuf.toString();
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
/*     */   public static String getSystemProperty(String key, String def) {
/*     */     try {
/* 107 */       return System.getProperty(key, def);
/* 108 */     } catch (Throwable e) {
/* 109 */       LogLog.debug("Was not allowed to read system property \"" + key + "\".");
/* 110 */       return def;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object instantiateByKey(Properties props, String key, Class superClass, Object defaultValue) {
/* 117 */     String className = findAndSubst(key, props);
/* 118 */     if (className == null) {
/* 119 */       LogLog.error("Could not find value for key " + key);
/* 120 */       return defaultValue;
/*     */     } 
/*     */     
/* 123 */     return instantiateByClassName(className.trim(), superClass, defaultValue);
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
/*     */   public static boolean toBoolean(String value, boolean dEfault) {
/* 135 */     if (value == null)
/* 136 */       return dEfault; 
/* 137 */     String trimmedVal = value.trim();
/* 138 */     if ("true".equalsIgnoreCase(trimmedVal))
/* 139 */       return true; 
/* 140 */     if ("false".equalsIgnoreCase(trimmedVal))
/* 141 */       return false; 
/* 142 */     return dEfault;
/*     */   }
/*     */   
/*     */   public static int toInt(String value, int dEfault) {
/* 146 */     if (value != null) {
/* 147 */       String s = value.trim();
/*     */       try {
/* 149 */         return Integer.valueOf(s).intValue();
/* 150 */       } catch (NumberFormatException e) {
/* 151 */         LogLog.error("[" + s + "] is not in proper int form.");
/* 152 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/* 155 */     return dEfault;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Level toLevel(String value, Level defaultValue) {
/* 182 */     if (value == null) {
/* 183 */       return defaultValue;
/*     */     }
/* 185 */     value = value.trim();
/*     */     
/* 187 */     int hashIndex = value.indexOf('#');
/* 188 */     if (hashIndex == -1) {
/* 189 */       if ("NULL".equalsIgnoreCase(value)) {
/* 190 */         return null;
/*     */       }
/*     */       
/* 193 */       return Level.toLevel(value, defaultValue);
/*     */     } 
/*     */ 
/*     */     
/* 197 */     Level result = defaultValue;
/*     */     
/* 199 */     String clazz = value.substring(hashIndex + 1);
/* 200 */     String levelName = value.substring(0, hashIndex);
/*     */ 
/*     */     
/* 203 */     if ("NULL".equalsIgnoreCase(levelName)) {
/* 204 */       return null;
/*     */     }
/*     */     
/* 207 */     LogLog.debug("toLevel:class=[" + clazz + "]:pri=[" + levelName + "]");
/*     */     
/*     */     try {
/* 210 */       Class customLevel = Loader.loadClass(clazz);
/*     */ 
/*     */ 
/*     */       
/* 214 */       Class[] paramTypes = { String.class, Level.class };
/* 215 */       Method toLevelMethod = customLevel.getMethod("toLevel", paramTypes);
/*     */ 
/*     */       
/* 218 */       Object[] params = { levelName, defaultValue };
/* 219 */       Object o = toLevelMethod.invoke(null, params);
/*     */       
/* 221 */       result = (Level)o;
/* 222 */     } catch (ClassNotFoundException e) {
/* 223 */       LogLog.warn("custom level class [" + clazz + "] not found.");
/* 224 */     } catch (NoSuchMethodException e) {
/* 225 */       LogLog.warn("custom level class [" + clazz + "] does not have a class function toLevel(String, Level)", e);
/*     */     }
/* 227 */     catch (InvocationTargetException e) {
/* 228 */       if (e.getTargetException() instanceof InterruptedException || e
/* 229 */         .getTargetException() instanceof java.io.InterruptedIOException) {
/* 230 */         Thread.currentThread().interrupt();
/*     */       }
/* 232 */       LogLog.warn("custom level class [" + clazz + "] could not be instantiated", e);
/* 233 */     } catch (ClassCastException e) {
/* 234 */       LogLog.warn("class [" + clazz + "] is not a subclass of org.apache.log4j.Level", e);
/* 235 */     } catch (IllegalAccessException e) {
/* 236 */       LogLog.warn("class [" + clazz + "] cannot be instantiated due to access restrictions", e);
/* 237 */     } catch (RuntimeException e) {
/* 238 */       LogLog.warn("class [" + clazz + "], level [" + levelName + "] conversion failed.", e);
/*     */     } 
/* 240 */     return result;
/*     */   }
/*     */   
/*     */   public static long toFileSize(String value, long dEfault) {
/* 244 */     if (value == null) {
/* 245 */       return dEfault;
/*     */     }
/* 247 */     String s = value.trim().toUpperCase();
/* 248 */     long multiplier = 1L;
/*     */     
/*     */     int index;
/* 251 */     if ((index = s.indexOf("KB")) != -1) {
/* 252 */       multiplier = 1024L;
/* 253 */       s = s.substring(0, index);
/* 254 */     } else if ((index = s.indexOf("MB")) != -1) {
/* 255 */       multiplier = 1048576L;
/* 256 */       s = s.substring(0, index);
/* 257 */     } else if ((index = s.indexOf("GB")) != -1) {
/* 258 */       multiplier = 1073741824L;
/* 259 */       s = s.substring(0, index);
/*     */     } 
/* 261 */     if (s != null) {
/*     */       try {
/* 263 */         return Long.valueOf(s).longValue() * multiplier;
/* 264 */       } catch (NumberFormatException e) {
/* 265 */         LogLog.error("[" + s + "] is not in proper int form.");
/* 266 */         LogLog.error("[" + value + "] not in expected format.", e);
/*     */       } 
/*     */     }
/* 269 */     return dEfault;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String findAndSubst(String key, Properties props) {
/* 278 */     String value = props.getProperty(key);
/* 279 */     if (value == null) {
/* 280 */       return null;
/*     */     }
/*     */     try {
/* 283 */       return substVars(value, props);
/* 284 */     } catch (IllegalArgumentException e) {
/* 285 */       LogLog.error("Bad option value [" + value + "].", e);
/* 286 */       return value;
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
/*     */ 
/*     */   
/*     */   public static Object instantiateByClassName(String className, Class superClass, Object defaultValue) {
/* 302 */     if (className != null) {
/*     */       try {
/* 304 */         Class<?> classObj = Loader.loadClass(className);
/* 305 */         if (!superClass.isAssignableFrom(classObj)) {
/* 306 */           LogLog.error("A \"" + className + "\" object is not assignable to a \"" + superClass.getName() + "\" variable.");
/*     */           
/* 308 */           LogLog.error("The class \"" + superClass.getName() + "\" was loaded by ");
/* 309 */           LogLog.error("[" + superClass.getClassLoader() + "] whereas object of type ");
/* 310 */           LogLog.error("\"" + classObj.getName() + "\" was loaded by [" + classObj.getClassLoader() + "].");
/* 311 */           return defaultValue;
/*     */         } 
/* 313 */         return classObj.newInstance();
/* 314 */       } catch (ClassNotFoundException e) {
/* 315 */         LogLog.error("Could not instantiate class [" + className + "].", e);
/* 316 */       } catch (IllegalAccessException e) {
/* 317 */         LogLog.error("Could not instantiate class [" + className + "].", e);
/* 318 */       } catch (InstantiationException e) {
/* 319 */         LogLog.error("Could not instantiate class [" + className + "].", e);
/* 320 */       } catch (RuntimeException e) {
/* 321 */         LogLog.error("Could not instantiate class [" + className + "].", e);
/*     */       } 
/*     */     }
/* 324 */     return defaultValue;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String substVars(String val, Properties props) throws IllegalArgumentException {
/* 374 */     StringBuilder sbuf = new StringBuilder();
/*     */     
/* 376 */     int i = 0;
/*     */ 
/*     */     
/*     */     while (true) {
/* 380 */       int j = val.indexOf(DELIM_START, i);
/* 381 */       if (j == -1) {
/*     */         
/* 383 */         if (i == 0) {
/* 384 */           return val;
/*     */         }
/* 386 */         sbuf.append(val.substring(i, val.length()));
/* 387 */         return sbuf.toString();
/*     */       } 
/*     */       
/* 390 */       sbuf.append(val.substring(i, j));
/* 391 */       int k = val.indexOf(DELIM_STOP, j);
/* 392 */       if (k == -1) {
/* 393 */         throw new IllegalArgumentException('"' + val + "\" has no closing brace. Opening brace at position " + j + '.');
/*     */       }
/*     */       
/* 396 */       j += DELIM_START_LEN;
/* 397 */       String key = val.substring(j, k);
/*     */       
/* 399 */       String replacement = getSystemProperty(key, null);
/*     */       
/* 401 */       if (replacement == null && props != null) {
/* 402 */         replacement = props.getProperty(key);
/*     */       }
/*     */       
/* 405 */       if (replacement != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 411 */         String recursiveReplacement = substVars(replacement, props);
/* 412 */         sbuf.append(recursiveReplacement);
/*     */       } 
/* 414 */       i = k + DELIM_STOP_LEN;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void selectAndConfigure(InputStream inputStream, String clazz, LoggerRepository hierarchy) {
/*     */     PropertyConfigurator propertyConfigurator;
/* 443 */     Configurator configurator = null;
/*     */     
/* 445 */     if (clazz != null) {
/* 446 */       LogLog.debug("Preferred configurator class: " + clazz);
/* 447 */       configurator = (Configurator)instantiateByClassName(clazz, Configurator.class, null);
/* 448 */       if (configurator == null) {
/* 449 */         LogLog.error("Could not instantiate configurator [" + clazz + "].");
/*     */         return;
/*     */       } 
/*     */     } else {
/* 453 */       propertyConfigurator = new PropertyConfigurator();
/*     */     } 
/*     */     
/* 456 */     propertyConfigurator.doConfigure(inputStream, hierarchy);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void selectAndConfigure(URL url, String clazz, LoggerRepository hierarchy) {
/*     */     PropertyConfigurator propertyConfigurator;
/* 486 */     Configurator configurator = null;
/* 487 */     String filename = url.getFile();
/*     */     
/* 489 */     if (clazz == null && filename != null && filename.endsWith(".xml")) {
/* 490 */       clazz = "org.apache.log4j.xml.DOMConfigurator";
/*     */     }
/*     */     
/* 493 */     if (clazz != null) {
/* 494 */       LogLog.debug("Preferred configurator class: " + clazz);
/* 495 */       configurator = (Configurator)instantiateByClassName(clazz, Configurator.class, null);
/* 496 */       if (configurator == null) {
/* 497 */         LogLog.error("Could not instantiate configurator [" + clazz + "].");
/*     */         return;
/*     */       } 
/*     */     } else {
/* 501 */       propertyConfigurator = new PropertyConfigurator();
/*     */     } 
/*     */     
/* 504 */     propertyConfigurator.doConfigure(url, hierarchy);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/helpers/OptionConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */