/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Vector;
/*     */ import java.util.stream.Collector;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class StringUtils
/*     */ {
/*     */   private static final long KILOBYTE = 1024L;
/*     */   private static final long MEGABYTE = 1048576L;
/*     */   private static final long GIGABYTE = 1073741824L;
/*     */   private static final long TERABYTE = 1099511627776L;
/*     */   private static final long PETABYTE = 1125899906842624L;
/*     */   @Deprecated
/*  49 */   public static final String LINE_SEP = System.lineSeparator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vector<String> lineSplit(String data) {
/*  58 */     return split(data, 10);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vector<String> split(String data, int ch) {
/*  69 */     Vector<String> elems = new Vector<>();
/*  70 */     int pos = -1;
/*  71 */     int i = 0;
/*  72 */     while ((pos = data.indexOf(ch, i)) != -1) {
/*  73 */       String elem = data.substring(i, pos);
/*  74 */       elems.addElement(elem);
/*  75 */       i = pos + 1;
/*     */     } 
/*  77 */     elems.addElement(data.substring(i));
/*  78 */     return elems;
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
/*     */   @Deprecated
/*     */   public static String replace(String data, String from, String to) {
/*  91 */     return data.replace(from, to);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getStackTrace(Throwable t) {
/* 100 */     StringWriter sw = new StringWriter();
/* 101 */     PrintWriter pw = new PrintWriter(sw, true);
/* 102 */     t.printStackTrace(pw);
/* 103 */     pw.flush();
/* 104 */     pw.close();
/* 105 */     return sw.toString();
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
/*     */   public static boolean endsWith(StringBuffer buffer, String suffix) {
/* 123 */     if (suffix.length() > buffer.length()) {
/* 124 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 133 */     int endIndex = suffix.length() - 1;
/* 134 */     int bufferIndex = buffer.length() - 1;
/* 135 */     while (endIndex >= 0) {
/* 136 */       if (buffer.charAt(bufferIndex) != suffix.charAt(endIndex)) {
/* 137 */         return false;
/*     */       }
/* 139 */       bufferIndex--;
/* 140 */       endIndex--;
/*     */     } 
/* 142 */     return true;
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
/*     */   public static String resolveBackSlash(String input) {
/* 157 */     StringBuilder b = new StringBuilder();
/* 158 */     boolean backSlashSeen = false;
/* 159 */     for (char c : input.toCharArray()) {
/* 160 */       if (!backSlashSeen) {
/* 161 */         if (c == '\\') {
/* 162 */           backSlashSeen = true;
/*     */         } else {
/* 164 */           b.append(c);
/*     */         } 
/*     */       } else {
/* 167 */         switch (c) {
/*     */           case '\\':
/* 169 */             b.append('\\');
/*     */             break;
/*     */           case 'n':
/* 172 */             b.append('\n');
/*     */             break;
/*     */           case 'r':
/* 175 */             b.append('\r');
/*     */             break;
/*     */           case 't':
/* 178 */             b.append('\t');
/*     */             break;
/*     */           case 'f':
/* 181 */             b.append('\f');
/*     */             break;
/*     */           case 's':
/* 184 */             b.append(" \t\n\r\f");
/*     */             break;
/*     */           default:
/* 187 */             b.append(c); break;
/*     */         } 
/* 189 */         backSlashSeen = false;
/*     */       } 
/*     */     } 
/* 192 */     return b.toString();
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
/*     */   public static long parseHumanSizes(String humanSize) throws Exception {
/* 204 */     long factor = 1L;
/* 205 */     char s = humanSize.charAt(0);
/* 206 */     switch (s) {
/*     */       case '+':
/* 208 */         humanSize = humanSize.substring(1);
/*     */         break;
/*     */       case '-':
/* 211 */         factor = -1L;
/* 212 */         humanSize = humanSize.substring(1);
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 218 */     char c = humanSize.charAt(humanSize.length() - 1);
/* 219 */     if (!Character.isDigit(c)) {
/* 220 */       int trim = 1;
/* 221 */       switch (c) {
/*     */         case 'K':
/* 223 */           factor *= 1024L;
/*     */           break;
/*     */         case 'M':
/* 226 */           factor *= 1048576L;
/*     */           break;
/*     */         case 'G':
/* 229 */           factor *= 1073741824L;
/*     */           break;
/*     */         case 'T':
/* 232 */           factor *= 1099511627776L;
/*     */           break;
/*     */         case 'P':
/* 235 */           factor *= 1125899906842624L;
/*     */           break;
/*     */         default:
/* 238 */           trim = 0; break;
/*     */       } 
/* 240 */       humanSize = humanSize.substring(0, humanSize.length() - trim);
/*     */     } 
/*     */     try {
/* 243 */       return factor * Long.parseLong(humanSize);
/* 244 */     } catch (NumberFormatException e) {
/* 245 */       throw new BuildException("Failed to parse \"" + humanSize + "\"", e);
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
/*     */   public static String removeSuffix(String string, String suffix) {
/* 257 */     if (string.endsWith(suffix)) {
/* 258 */       return string.substring(0, string.length() - suffix.length());
/*     */     }
/* 260 */     return string;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String removePrefix(String string, String prefix) {
/* 271 */     if (string.startsWith(prefix)) {
/* 272 */       return string.substring(prefix.length());
/*     */     }
/* 274 */     return string;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String join(Collection<?> collection, CharSequence separator) {
/* 285 */     if (collection == null) {
/* 286 */       return "";
/*     */     }
/* 288 */     return collection.stream().map(String::valueOf)
/* 289 */       .collect(joining(separator));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String join(Object[] array, CharSequence separator) {
/* 300 */     if (array == null) {
/* 301 */       return "";
/*     */     }
/* 303 */     return join(Arrays.asList(array), separator);
/*     */   }
/*     */   
/*     */   private static Collector<CharSequence, ?, String> joining(CharSequence separator) {
/* 307 */     return (separator == null) ? Collectors.joining() : Collectors.joining(separator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String trimToNull(String inputString) {
/* 317 */     if (inputString == null) {
/* 318 */       return null;
/*     */     }
/* 320 */     String tmpString = inputString.trim();
/* 321 */     return tmpString.isEmpty() ? null : tmpString;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/StringUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */