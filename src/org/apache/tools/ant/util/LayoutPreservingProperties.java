/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PushbackReader;
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.text.ParseException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LayoutPreservingProperties
/*     */   extends Properties
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  86 */   private String eol = System.lineSeparator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   private List<LogicalLine> logicalLines = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   private Map<String, Integer> keyedPairLines = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean removeComments;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LayoutPreservingProperties() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LayoutPreservingProperties(Properties defaults) {
/* 118 */     super(defaults);
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
/*     */   public boolean isRemoveComments() {
/* 130 */     return this.removeComments;
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
/*     */   public void setRemoveComments(boolean val) {
/* 143 */     this.removeComments = val;
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(InputStream inStream) throws IOException {
/* 148 */     String s = readLines(inStream);
/* 149 */     byte[] ba = s.getBytes(StandardCharsets.ISO_8859_1);
/* 150 */     ByteArrayInputStream bais = new ByteArrayInputStream(ba);
/* 151 */     super.load(bais);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object put(Object key, Object value) throws NullPointerException {
/* 156 */     Object obj = super.put(key, value);
/*     */     
/* 158 */     innerSetProperty(key.toString(), value.toString());
/* 159 */     return obj;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object setProperty(String key, String value) throws NullPointerException {
/* 165 */     Object obj = super.setProperty(key, value);
/*     */     
/* 167 */     innerSetProperty(key, value);
/* 168 */     return obj;
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
/*     */   private void innerSetProperty(String key, String value) {
/* 180 */     value = escapeValue(value);
/*     */     
/* 182 */     if (this.keyedPairLines.containsKey(key)) {
/* 183 */       Integer i = this.keyedPairLines.get(key);
/* 184 */       Pair p = (Pair)this.logicalLines.get(i.intValue());
/* 185 */       p.setValue(value);
/*     */     } else {
/* 187 */       key = escapeName(key);
/* 188 */       Pair p = new Pair(key, value);
/* 189 */       p.setNew(true);
/* 190 */       this.keyedPairLines.put(key, Integer.valueOf(this.logicalLines.size()));
/* 191 */       this.logicalLines.add(p);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 197 */     super.clear();
/* 198 */     this.keyedPairLines.clear();
/* 199 */     this.logicalLines.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object remove(Object key) {
/* 204 */     Object obj = super.remove(key);
/* 205 */     Integer i = this.keyedPairLines.remove(key);
/* 206 */     if (null != i) {
/* 207 */       if (this.removeComments) {
/* 208 */         removeCommentsEndingAt(i.intValue());
/*     */       }
/* 210 */       this.logicalLines.set(i.intValue(), null);
/*     */     } 
/* 212 */     return obj;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() {
/* 218 */     LayoutPreservingProperties dolly = (LayoutPreservingProperties)super.clone();
/* 219 */     dolly.keyedPairLines = new HashMap<>(this.keyedPairLines);
/* 220 */     dolly.logicalLines = new ArrayList<>(this.logicalLines);
/* 221 */     int size = dolly.logicalLines.size();
/* 222 */     for (int j = 0; j < size; j++) {
/* 223 */       LogicalLine line = dolly.logicalLines.get(j);
/* 224 */       if (line instanceof Pair) {
/* 225 */         Pair p = (Pair)line;
/* 226 */         dolly.logicalLines.set(j, (Pair)p.clone());
/*     */       } 
/*     */     } 
/*     */     
/* 230 */     return dolly;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void listLines(PrintStream out) {
/* 239 */     out.println("-- logical lines --");
/* 240 */     for (LogicalLine line : this.logicalLines) {
/* 241 */       if (line instanceof Blank) {
/* 242 */         out.println("blank:   \"" + line + "\""); continue;
/* 243 */       }  if (line instanceof Comment) {
/* 244 */         out.println("comment: \"" + line + "\""); continue;
/* 245 */       }  if (line instanceof Pair) {
/* 246 */         out.println("pair:    \"" + line + "\"");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveAs(File dest) throws IOException {
/* 257 */     OutputStream fos = Files.newOutputStream(dest.toPath(), new java.nio.file.OpenOption[0]);
/* 258 */     store(fos, null);
/* 259 */     fos.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public void store(OutputStream out, String header) throws IOException {
/* 264 */     OutputStreamWriter osw = new OutputStreamWriter(out, StandardCharsets.ISO_8859_1);
/*     */     
/* 266 */     int skipLines = 0;
/* 267 */     int totalLines = this.logicalLines.size();
/*     */     
/* 269 */     if (header != null) {
/* 270 */       osw.write("#" + header + this.eol);
/* 271 */       if (totalLines > 0 && this.logicalLines
/* 272 */         .get(0) instanceof Comment && header
/* 273 */         .equals(((LogicalLine)this.logicalLines.get(0)).toString().substring(1))) {
/* 274 */         skipLines = 1;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 281 */     if (totalLines > skipLines && this.logicalLines
/* 282 */       .get(skipLines) instanceof Comment) {
/*     */       try {
/* 284 */         DateUtils.parseDateFromHeader(((LogicalLine)this.logicalLines
/* 285 */             .get(skipLines))
/* 286 */             .toString().substring(1));
/* 287 */         skipLines++;
/* 288 */       } catch (ParseException parseException) {}
/*     */     }
/*     */ 
/*     */     
/* 292 */     osw.write("#" + DateUtils.getDateForHeader() + this.eol);
/*     */     
/* 294 */     boolean writtenSep = false;
/* 295 */     for (LogicalLine line : this.logicalLines.subList(skipLines, totalLines)) {
/* 296 */       if (line instanceof Pair) {
/* 297 */         if (((Pair)line).isNew() && 
/* 298 */           !writtenSep) {
/* 299 */           osw.write(this.eol);
/* 300 */           writtenSep = true;
/*     */         } 
/*     */         
/* 303 */         osw.write(line.toString() + this.eol); continue;
/* 304 */       }  if (line != null) {
/* 305 */         osw.write(line.toString() + this.eol);
/*     */       }
/*     */     } 
/* 308 */     osw.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String readLines(InputStream is) throws IOException {
/* 319 */     InputStreamReader isr = new InputStreamReader(is, StandardCharsets.ISO_8859_1);
/* 320 */     PushbackReader pbr = new PushbackReader(isr, 1);
/*     */     
/* 322 */     if (!this.logicalLines.isEmpty())
/*     */     {
/* 324 */       this.logicalLines.add(new Blank());
/*     */     }
/*     */     
/* 327 */     String s = readFirstLine(pbr);
/* 328 */     BufferedReader br = new BufferedReader(pbr);
/*     */     
/* 330 */     boolean continuation = false;
/* 331 */     boolean comment = false;
/* 332 */     StringBuilder fileBuffer = new StringBuilder();
/* 333 */     StringBuilder logicalLineBuffer = new StringBuilder();
/* 334 */     while (s != null) {
/* 335 */       fileBuffer.append(s).append(this.eol);
/*     */       
/* 337 */       if (continuation) {
/*     */         
/* 339 */         s = "\n" + s;
/*     */       } else {
/*     */         
/* 342 */         comment = s.matches("^[ \t\f]*[#!].*");
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 347 */       if (!comment) {
/* 348 */         continuation = requiresContinuation(s);
/*     */       }
/*     */       
/* 351 */       logicalLineBuffer.append(s);
/*     */       
/* 353 */       if (!continuation) {
/*     */         LogicalLine line;
/* 355 */         if (comment) {
/* 356 */           line = new Comment(logicalLineBuffer.toString());
/* 357 */         } else if (logicalLineBuffer.toString().trim().isEmpty()) {
/* 358 */           line = new Blank();
/*     */         } else {
/* 360 */           line = new Pair(logicalLineBuffer.toString());
/* 361 */           String key = unescape(((Pair)line).getName());
/* 362 */           if (this.keyedPairLines.containsKey(key))
/*     */           {
/*     */             
/* 365 */             remove(key);
/*     */           }
/* 367 */           this.keyedPairLines.put(key, Integer.valueOf(this.logicalLines.size()));
/*     */         } 
/* 369 */         this.logicalLines.add(line);
/* 370 */         logicalLineBuffer.setLength(0);
/*     */       } 
/* 372 */       s = br.readLine();
/*     */     } 
/* 374 */     return fileBuffer.toString();
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
/*     */   private String readFirstLine(PushbackReader r) throws IOException {
/* 389 */     StringBuilder sb = new StringBuilder(80);
/* 390 */     int ch = r.read();
/* 391 */     boolean hasCR = false;
/*     */ 
/*     */     
/* 394 */     this.eol = System.lineSeparator();
/*     */     
/* 396 */     while (ch >= 0) {
/* 397 */       if (hasCR && ch != 10) {
/*     */         
/* 399 */         r.unread(ch);
/*     */         
/*     */         break;
/*     */       } 
/* 403 */       if (ch == 13)
/* 404 */       { this.eol = "\r";
/* 405 */         hasCR = true; }
/* 406 */       else { if (ch == 10) {
/* 407 */           this.eol = hasCR ? "\r\n" : "\n";
/*     */           break;
/*     */         } 
/* 410 */         sb.append((char)ch); }
/*     */       
/* 412 */       ch = r.read();
/*     */     } 
/* 414 */     return sb.toString();
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
/*     */   private boolean requiresContinuation(String s) {
/* 426 */     char[] ca = s.toCharArray();
/* 427 */     int i = ca.length - 1;
/* 428 */     while (i > 0 && ca[i] == '\\') {
/* 429 */       i--;
/*     */     }
/*     */     
/* 432 */     int tb = ca.length - i - 1;
/* 433 */     return (tb % 2 == 1);
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
/*     */   private String unescape(String s) {
/* 456 */     char[] ch = new char[s.length() + 1];
/* 457 */     s.getChars(0, s.length(), ch, 0);
/* 458 */     ch[s.length()] = '\n';
/* 459 */     StringBuilder buffy = new StringBuilder(s.length());
/* 460 */     for (int i = 0; i < ch.length; i++) {
/* 461 */       char c = ch[i];
/* 462 */       if (c == '\n') {
/*     */         break;
/*     */       }
/*     */       
/* 466 */       if (c == '\\') {
/*     */         
/* 468 */         c = ch[++i];
/* 469 */         if (c == 'n') {
/* 470 */           buffy.append('\n');
/* 471 */         } else if (c == 'r') {
/* 472 */           buffy.append('\r');
/* 473 */         } else if (c == 'f') {
/* 474 */           buffy.append('\f');
/* 475 */         } else if (c == 't') {
/* 476 */           buffy.append('\t');
/* 477 */         } else if (c == 'u') {
/*     */           
/* 479 */           c = unescapeUnicode(ch, i + 1);
/* 480 */           i += 4;
/* 481 */           buffy.append(c);
/*     */         } else {
/* 483 */           buffy.append(c);
/*     */         } 
/*     */       } else {
/* 486 */         buffy.append(c);
/*     */       } 
/*     */     } 
/* 489 */     return buffy.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private char unescapeUnicode(char[] ch, int i) {
/* 499 */     String s = new String(ch, i, 4);
/* 500 */     return (char)Integer.parseInt(s, 16);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String escapeValue(String s) {
/* 511 */     return escape(s, false);
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
/*     */   private String escapeName(String s) {
/* 524 */     return escape(s, true);
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
/*     */   private String escape(String s, boolean escapeAllSpaces) {
/* 538 */     if (s == null) {
/* 539 */       return null;
/*     */     }
/*     */     
/* 542 */     char[] ch = new char[s.length()];
/* 543 */     s.getChars(0, s.length(), ch, 0);
/* 544 */     String forEscaping = "\t\f\r\n\\:=#!";
/* 545 */     String escaped = "tfrn\\:=#!";
/* 546 */     StringBuilder buffy = new StringBuilder(s.length());
/* 547 */     boolean leadingSpace = true;
/* 548 */     for (char c : ch) {
/* 549 */       if (c == ' ') {
/* 550 */         if (escapeAllSpaces || leadingSpace) {
/* 551 */           buffy.append("\\");
/*     */         }
/*     */       } else {
/* 554 */         leadingSpace = false;
/*     */       } 
/* 556 */       int p = "\t\f\r\n\\:=#!".indexOf(c);
/* 557 */       if (p != -1) {
/* 558 */         buffy.append("\\").append("tfrn\\:=#!", p, p + 1);
/* 559 */       } else if (c < ' ' || c > '~') {
/* 560 */         buffy.append(escapeUnicode(c));
/*     */       } else {
/* 562 */         buffy.append(c);
/*     */       } 
/*     */     } 
/* 565 */     return buffy.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String escapeUnicode(char ch) {
/* 575 */     return "\\" + UnicodeUtil.EscapeUnicode(ch);
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
/*     */   private void removeCommentsEndingAt(int pos) {
/* 592 */     int end = pos - 1;
/*     */ 
/*     */     
/* 595 */     for (pos = end; pos > 0 && 
/* 596 */       this.logicalLines.get(pos) instanceof Blank; pos--);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 603 */     if (!(this.logicalLines.get(pos) instanceof Comment)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 608 */     for (; pos >= 0 && 
/* 609 */       this.logicalLines.get(pos) instanceof Comment; pos--);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 615 */     for (; ++pos <= end; pos++) {
/* 616 */       this.logicalLines.set(pos, null);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static abstract class LogicalLine
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     private String text;
/*     */     
/*     */     public LogicalLine(String text) {
/* 629 */       this.text = text;
/*     */     }
/*     */     
/*     */     public void setText(String text) {
/* 633 */       this.text = text;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 638 */       return this.text;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class Blank
/*     */     extends LogicalLine
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     public Blank() {
/* 649 */       super("");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class Comment
/*     */     extends LogicalLine
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     public Comment(String text) {
/* 660 */       super(text);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class Pair
/*     */     extends LogicalLine
/*     */     implements Cloneable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     private String name;
/*     */     
/*     */     private String value;
/*     */     private boolean added;
/*     */     
/*     */     public Pair(String text) {
/* 677 */       super(text);
/* 678 */       parsePair(text);
/*     */     }
/*     */     
/*     */     public Pair(String name, String value) {
/* 682 */       this(name + "=" + value);
/*     */     }
/*     */     
/*     */     public String getName() {
/* 686 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getValue() {
/* 691 */       return this.value;
/*     */     }
/*     */     
/*     */     public void setValue(String value) {
/* 695 */       this.value = value;
/* 696 */       setText(this.name + "=" + value);
/*     */     }
/*     */     
/*     */     public boolean isNew() {
/* 700 */       return this.added;
/*     */     }
/*     */     
/*     */     public void setNew(boolean val) {
/* 704 */       this.added = val;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object clone() {
/* 709 */       Object dolly = null;
/*     */       try {
/* 711 */         dolly = super.clone();
/* 712 */       } catch (CloneNotSupportedException e) {
/*     */         
/* 714 */         e.printStackTrace();
/*     */       } 
/* 716 */       return dolly;
/*     */     }
/*     */ 
/*     */     
/*     */     private void parsePair(String text) {
/* 721 */       int pos = findFirstSeparator(text);
/* 722 */       if (pos == -1) {
/*     */         
/* 724 */         this.name = text;
/* 725 */         setValue((String)null);
/*     */       } else {
/* 727 */         this.name = text.substring(0, pos);
/* 728 */         setValue(text.substring(pos + 1));
/*     */       } 
/*     */       
/* 731 */       this.name = stripStart(this.name, " \t\f");
/*     */     }
/*     */     
/*     */     private String stripStart(String s, String chars) {
/* 735 */       if (s == null) {
/* 736 */         return null;
/*     */       }
/*     */       
/* 739 */       int i = 0;
/* 740 */       for (; i < s.length() && 
/* 741 */         chars.indexOf(s.charAt(i)) != -1; i++);
/*     */ 
/*     */ 
/*     */       
/* 745 */       if (i == s.length()) {
/* 746 */         return "";
/*     */       }
/* 748 */       return s.substring(i);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int findFirstSeparator(String s) {
/* 755 */       s = s.replaceAll("\\\\\\\\", "__");
/*     */ 
/*     */ 
/*     */       
/* 759 */       s = s.replaceAll("\\\\=", "__");
/* 760 */       s = s.replaceAll("\\\\:", "__");
/* 761 */       s = s.replaceAll("\\\\ ", "__");
/* 762 */       s = s.replaceAll("\\\\t", "__");
/*     */ 
/*     */       
/* 765 */       return indexOfAny(s, " :=\t");
/*     */     }
/*     */     
/*     */     private int indexOfAny(String s, String chars) {
/* 769 */       if (s == null || chars == null) {
/* 770 */         return -1;
/*     */       }
/*     */       
/* 773 */       int p = s.length() + 1;
/* 774 */       for (int i = 0; i < chars.length(); i++) {
/* 775 */         int x = s.indexOf(chars.charAt(i));
/* 776 */         if (x != -1 && x < p) {
/* 777 */           p = x;
/*     */         }
/*     */       } 
/* 780 */       if (p == s.length() + 1) {
/* 781 */         return -1;
/*     */       }
/* 783 */       return p;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/LayoutPreservingProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */