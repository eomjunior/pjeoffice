/*     */ package org.apache.tools.ant.taskdefs.cvslib;
/*     */ 
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.TimeZone;
/*     */ import org.apache.tools.ant.taskdefs.AbstractCvsTask;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ChangeLogParser
/*     */ {
/*     */   private static final int GET_FILE = 1;
/*     */   private static final int GET_DATE = 2;
/*     */   private static final int GET_COMMENT = 3;
/*     */   private static final int GET_REVISION = 4;
/*     */   private static final int GET_PREVIOUS_REV = 5;
/*  47 */   private final SimpleDateFormat inputDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   private final SimpleDateFormat cvs1129InputDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.US);
/*     */   
/*     */   private String file;
/*     */   
/*     */   private String date;
/*     */   
/*     */   private String author;
/*     */   
/*     */   private String comment;
/*     */   private String revision;
/*     */   private String previousRevision;
/*  63 */   private int status = 1;
/*     */ 
/*     */   
/*  66 */   private final Map<String, CVSEntry> entries = new Hashtable<>();
/*     */   
/*     */   private final boolean remote;
/*     */   private final String[] moduleNames;
/*     */   private final int[] moduleNameLengths;
/*     */   
/*     */   public ChangeLogParser() {
/*  73 */     this(false, "", Collections.emptyList());
/*     */   }
/*     */   
/*     */   public ChangeLogParser(boolean remote, String packageName, List<AbstractCvsTask.Module> modules) {
/*  77 */     this.remote = remote;
/*     */     
/*  79 */     List<String> names = new ArrayList<>();
/*  80 */     if (packageName != null) {
/*  81 */       StringTokenizer tok = new StringTokenizer(packageName);
/*  82 */       while (tok.hasMoreTokens()) {
/*  83 */         names.add(tok.nextToken());
/*     */       }
/*     */     } 
/*  86 */     Objects.requireNonNull(names); modules.stream().map(AbstractCvsTask.Module::getName).forEach(names::add);
/*     */     
/*  88 */     this.moduleNames = names.<String>toArray(new String[0]);
/*  89 */     this.moduleNameLengths = new int[this.moduleNames.length];
/*  90 */     for (int i = 0; i < this.moduleNames.length; i++) {
/*  91 */       this.moduleNameLengths[i] = this.moduleNames[i].length();
/*     */     }
/*     */     
/*  94 */     TimeZone utc = TimeZone.getTimeZone("UTC");
/*  95 */     this.inputDate.setTimeZone(utc);
/*  96 */     this.cvs1129InputDate.setTimeZone(utc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CVSEntry[] getEntrySetAsArray() {
/* 105 */     return (CVSEntry[])this.entries.values().toArray((Object[])new CVSEntry[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stdout(String line) {
/* 114 */     switch (this.status) {
/*     */ 
/*     */       
/*     */       case 1:
/* 118 */         reset();
/* 119 */         processFile(line);
/*     */         break;
/*     */       case 4:
/* 122 */         processRevision(line);
/*     */         break;
/*     */       
/*     */       case 2:
/* 126 */         processDate(line);
/*     */         break;
/*     */       
/*     */       case 3:
/* 130 */         processComment(line);
/*     */         break;
/*     */       
/*     */       case 5:
/* 134 */         processGetPreviousRevision(line);
/*     */         break;
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
/*     */   private void processComment(String line) {
/* 149 */     if ("============================================================================="
/* 150 */       .equals(line)) {
/*     */ 
/*     */       
/* 153 */       int end = this.comment.length() - System.lineSeparator().length();
/* 154 */       this.comment = this.comment.substring(0, end);
/* 155 */       saveEntry();
/* 156 */       this.status = 1;
/* 157 */     } else if ("----------------------------".equals(line)) {
/* 158 */       int end = this.comment.length() - System.lineSeparator().length();
/* 159 */       this.comment = this.comment.substring(0, end);
/* 160 */       this.status = 5;
/*     */     } else {
/* 162 */       this.comment += line + System.lineSeparator();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processFile(String line) {
/* 172 */     if (!this.remote && line.startsWith("Working file:")) {
/*     */       
/* 174 */       this.file = line.substring(14);
/*     */       
/* 176 */       this.status = 4;
/* 177 */     } else if (this.remote && line.startsWith("RCS file:")) {
/*     */ 
/*     */       
/* 180 */       int startOfFileName = 0;
/* 181 */       for (int i = 0; i < this.moduleNames.length; i++) {
/* 182 */         int index = line.indexOf(this.moduleNames[i]);
/* 183 */         if (index >= 0) {
/* 184 */           startOfFileName = index + this.moduleNameLengths[i] + 1;
/*     */           break;
/*     */         } 
/*     */       } 
/* 188 */       int endOfFileName = line.indexOf(",v");
/* 189 */       if (endOfFileName == -1) {
/* 190 */         this.file = line.substring(startOfFileName);
/*     */       } else {
/* 192 */         this.file = line.substring(startOfFileName, endOfFileName);
/*     */       } 
/* 194 */       this.status = 4;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processRevision(String line) {
/* 204 */     if (line.startsWith("revision")) {
/*     */       
/* 206 */       this.revision = line.substring(9);
/*     */       
/* 208 */       this.status = 2;
/* 209 */     } else if (line.startsWith("======")) {
/*     */ 
/*     */       
/* 212 */       this.status = 1;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processDate(String line) {
/* 222 */     if (line.startsWith("date:")) {
/*     */ 
/*     */ 
/*     */       
/* 226 */       int endOfDateIndex = line.indexOf(';');
/* 227 */       this.date = line.substring("date: ".length(), endOfDateIndex);
/*     */       
/* 229 */       int startOfAuthorIndex = line.indexOf("author: ", endOfDateIndex + 1);
/* 230 */       int endOfAuthorIndex = line.indexOf(';', startOfAuthorIndex + 1);
/* 231 */       this.author = line.substring("author: ".length() + startOfAuthorIndex, endOfAuthorIndex);
/*     */       
/* 233 */       this.status = 3;
/*     */ 
/*     */ 
/*     */       
/* 237 */       this.comment = "";
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processGetPreviousRevision(String line) {
/* 247 */     if (!line.startsWith("revision ")) {
/* 248 */       throw new IllegalStateException("Unexpected line from CVS: " + line);
/*     */     }
/*     */     
/* 251 */     this.previousRevision = line.substring("revision ".length());
/*     */     
/* 253 */     saveEntry();
/*     */     
/* 255 */     this.revision = this.previousRevision;
/* 256 */     this.status = 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void saveEntry() {
/* 263 */     ((CVSEntry)this.entries.computeIfAbsent(this.date + this.author + this.comment, k -> new CVSEntry(parseDate(this.date), this.author, this.comment)))
/* 264 */       .addFile(this.file, this.revision, this.previousRevision);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Date parseDate(String date) {
/*     */     try {
/* 275 */       return this.inputDate.parse(date);
/* 276 */     } catch (ParseException e) {
/*     */       try {
/* 278 */         return this.cvs1129InputDate.parse(date);
/* 279 */       } catch (ParseException e2) {
/* 280 */         throw new IllegalStateException("Invalid date format: " + date);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 289 */     this.file = null;
/* 290 */     this.date = null;
/* 291 */     this.author = null;
/* 292 */     this.comment = null;
/* 293 */     this.revision = null;
/* 294 */     this.previousRevision = null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/cvslib/ChangeLogParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */