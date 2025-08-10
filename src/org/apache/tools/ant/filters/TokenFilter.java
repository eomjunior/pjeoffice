/*     */ package org.apache.tools.ant.filters;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.apache.tools.ant.types.RegularExpression;
/*     */ import org.apache.tools.ant.types.Substitution;
/*     */ import org.apache.tools.ant.util.LineTokenizer;
/*     */ import org.apache.tools.ant.util.StringUtils;
/*     */ import org.apache.tools.ant.util.Tokenizer;
/*     */ import org.apache.tools.ant.util.regexp.Regexp;
/*     */ import org.apache.tools.ant.util.regexp.RegexpUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TokenFilter
/*     */   extends BaseFilterReader
/*     */   implements ChainableReader
/*     */ {
/*  61 */   private Vector<Filter> filters = new Vector<>();
/*     */   
/*  63 */   private Tokenizer tokenizer = null;
/*     */   
/*  65 */   private String delimOutput = null;
/*     */   
/*  67 */   private String line = null;
/*     */   
/*  69 */   private int linePos = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TokenFilter() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TokenFilter(Reader in) {
/*  87 */     super(in);
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
/*     */   public int read() throws IOException {
/* 104 */     if (this.tokenizer == null) {
/* 105 */       this.tokenizer = (Tokenizer)new LineTokenizer();
/*     */     }
/* 107 */     while (this.line == null || this.line.isEmpty()) {
/* 108 */       this.line = this.tokenizer.getToken(this.in);
/* 109 */       if (this.line == null) {
/* 110 */         return -1;
/*     */       }
/* 112 */       for (Filter filter : this.filters) {
/* 113 */         this.line = filter.filter(this.line);
/* 114 */         if (this.line == null) {
/*     */           break;
/*     */         }
/*     */       } 
/* 118 */       this.linePos = 0;
/* 119 */       if (this.line != null && !this.tokenizer.getPostToken().isEmpty()) {
/* 120 */         if (this.delimOutput != null) {
/* 121 */           this.line += this.delimOutput; continue;
/*     */         } 
/* 123 */         this.line += this.tokenizer.getPostToken();
/*     */       } 
/*     */     } 
/*     */     
/* 127 */     int ch = this.line.charAt(this.linePos);
/* 128 */     this.linePos++;
/* 129 */     if (this.linePos == this.line.length()) {
/* 130 */       this.line = null;
/*     */     }
/* 132 */     return ch;
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
/*     */   public final Reader chain(Reader reader) {
/* 145 */     TokenFilter newFilter = new TokenFilter(reader);
/* 146 */     newFilter.filters = this.filters;
/* 147 */     newFilter.tokenizer = this.tokenizer;
/* 148 */     newFilter.delimOutput = this.delimOutput;
/* 149 */     newFilter.setProject(getProject());
/* 150 */     return newFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDelimOutput(String delimOutput) {
/* 160 */     this.delimOutput = resolveBackSlash(delimOutput);
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
/*     */   public void addLineTokenizer(LineTokenizer tokenizer) {
/* 173 */     add((Tokenizer)tokenizer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addStringTokenizer(StringTokenizer tokenizer) {
/* 182 */     add((Tokenizer)tokenizer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileTokenizer(FileTokenizer tokenizer) {
/* 190 */     add((Tokenizer)tokenizer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(Tokenizer tokenizer) {
/* 199 */     if (this.tokenizer != null) {
/* 200 */       throw new BuildException("Only one tokenizer allowed");
/*     */     }
/* 202 */     this.tokenizer = tokenizer;
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
/*     */   public void addReplaceString(ReplaceString filter) {
/* 214 */     this.filters.addElement(filter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addContainsString(ContainsString filter) {
/* 222 */     this.filters.addElement(filter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addReplaceRegex(ReplaceRegex filter) {
/* 230 */     this.filters.addElement(filter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addContainsRegex(ContainsRegex filter) {
/* 238 */     this.filters.addElement(filter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTrim(Trim filter) {
/* 246 */     this.filters.addElement(filter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addIgnoreBlank(IgnoreBlank filter) {
/* 254 */     this.filters.addElement(filter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDeleteCharacters(DeleteCharacters filter) {
/* 262 */     this.filters.addElement(filter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(Filter filter) {
/* 270 */     this.filters.addElement(filter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface Filter
/*     */   {
/*     */     String filter(String param1String);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class FileTokenizer
/*     */     extends org.apache.tools.ant.util.FileTokenizer {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class StringTokenizer
/*     */     extends org.apache.tools.ant.util.StringTokenizer {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class ChainableReaderFilter
/*     */     extends ProjectComponent
/*     */     implements ChainableReader, Filter
/*     */   {
/*     */     private boolean byLine = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setByLine(boolean byLine) {
/* 319 */       this.byLine = byLine;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Reader chain(Reader reader) {
/* 329 */       TokenFilter tokenFilter = new TokenFilter(reader);
/* 330 */       if (!this.byLine) {
/* 331 */         tokenFilter.add((Tokenizer)new TokenFilter.FileTokenizer());
/*     */       }
/* 333 */       tokenFilter.add(this);
/* 334 */       return tokenFilter;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ReplaceString
/*     */     extends ChainableReaderFilter
/*     */   {
/*     */     private String from;
/*     */ 
/*     */     
/*     */     private String to;
/*     */ 
/*     */ 
/*     */     
/*     */     public void setFrom(String from) {
/* 351 */       this.from = from;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setTo(String to) {
/* 360 */       this.to = to;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String filter(String line) {
/* 370 */       if (this.from == null) {
/* 371 */         throw new BuildException("Missing from in stringreplace");
/*     */       }
/* 373 */       StringBuilder ret = new StringBuilder();
/* 374 */       int start = 0;
/* 375 */       int found = line.indexOf(this.from);
/* 376 */       while (found >= 0) {
/*     */         
/* 378 */         if (found > start) {
/* 379 */           ret.append(line, start, found);
/*     */         }
/*     */ 
/*     */         
/* 383 */         if (this.to != null) {
/* 384 */           ret.append(this.to);
/*     */         }
/*     */ 
/*     */         
/* 388 */         start = found + this.from.length();
/* 389 */         found = line.indexOf(this.from, start);
/*     */       } 
/*     */ 
/*     */       
/* 393 */       if (line.length() > start) {
/* 394 */         ret.append(line, start, line.length());
/*     */       }
/*     */       
/* 397 */       return ret.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ContainsString
/*     */     extends ProjectComponent
/*     */     implements Filter
/*     */   {
/*     */     private String contains;
/*     */ 
/*     */ 
/*     */     
/*     */     public void setContains(String contains) {
/* 413 */       this.contains = contains;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String filter(String string) {
/* 424 */       if (this.contains == null) {
/* 425 */         throw new BuildException("Missing contains in containsstring");
/*     */       }
/* 427 */       if (string.contains(this.contains)) {
/* 428 */         return string;
/*     */       }
/* 430 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class ReplaceRegex
/*     */     extends ChainableReaderFilter
/*     */   {
/*     */     private String from;
/*     */     private String to;
/*     */     private RegularExpression regularExpression;
/*     */     private Substitution substitution;
/*     */     private boolean initialized = false;
/* 443 */     private String flags = "";
/*     */ 
/*     */     
/*     */     private int options;
/*     */     
/*     */     private Regexp regexp;
/*     */ 
/*     */     
/*     */     public void setPattern(String from) {
/* 452 */       this.from = from;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setReplace(String to) {
/* 459 */       this.to = to;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setFlags(String flags) {
/* 466 */       this.flags = flags;
/*     */     }
/*     */     
/*     */     private void initialize() {
/* 470 */       if (this.initialized) {
/*     */         return;
/*     */       }
/* 473 */       this.options = TokenFilter.convertRegexOptions(this.flags);
/* 474 */       if (this.from == null) {
/* 475 */         throw new BuildException("Missing pattern in replaceregex");
/*     */       }
/* 477 */       this.regularExpression = new RegularExpression();
/* 478 */       this.regularExpression.setPattern(this.from);
/* 479 */       this.regexp = this.regularExpression.getRegexp(getProject());
/* 480 */       if (this.to == null) {
/* 481 */         this.to = "";
/*     */       }
/* 483 */       this.substitution = new Substitution();
/* 484 */       this.substitution.setExpression(this.to);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String filter(String line) {
/* 492 */       initialize();
/*     */       
/* 494 */       if (!this.regexp.matches(line, this.options)) {
/* 495 */         return line;
/*     */       }
/* 497 */       return this.regexp.substitute(line, this.substitution
/* 498 */           .getExpression(getProject()), this.options);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class ContainsRegex
/*     */     extends ChainableReaderFilter
/*     */   {
/*     */     private String from;
/*     */     private String to;
/*     */     private RegularExpression regularExpression;
/*     */     private Substitution substitution;
/*     */     private boolean initialized = false;
/* 511 */     private String flags = "";
/*     */ 
/*     */     
/*     */     private int options;
/*     */     
/*     */     private Regexp regexp;
/*     */ 
/*     */     
/*     */     public void setPattern(String from) {
/* 520 */       this.from = from;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setReplace(String to) {
/* 527 */       this.to = to;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setFlags(String flags) {
/* 534 */       this.flags = flags;
/*     */     }
/*     */     
/*     */     private void initialize() {
/* 538 */       if (this.initialized) {
/*     */         return;
/*     */       }
/* 541 */       this.options = TokenFilter.convertRegexOptions(this.flags);
/* 542 */       if (this.from == null) {
/* 543 */         throw new BuildException("Missing from in containsregex");
/*     */       }
/* 545 */       this.regularExpression = new RegularExpression();
/* 546 */       this.regularExpression.setPattern(this.from);
/* 547 */       this.regexp = this.regularExpression.getRegexp(getProject());
/* 548 */       if (this.to == null) {
/*     */         return;
/*     */       }
/* 551 */       this.substitution = new Substitution();
/* 552 */       this.substitution.setExpression(this.to);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String filter(String string) {
/* 561 */       initialize();
/* 562 */       if (!this.regexp.matches(string, this.options)) {
/* 563 */         return null;
/*     */       }
/* 565 */       if (this.substitution == null) {
/* 566 */         return string;
/*     */       }
/* 568 */       return this.regexp.substitute(string, this.substitution
/* 569 */           .getExpression(getProject()), this.options);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Trim
/*     */     extends ChainableReaderFilter
/*     */   {
/*     */     public String filter(String line) {
/* 580 */       return line.trim();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class IgnoreBlank
/*     */     extends ChainableReaderFilter
/*     */   {
/*     */     public String filter(String line) {
/* 593 */       if (line.trim().isEmpty()) {
/* 594 */         return null;
/*     */       }
/* 596 */       return line;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class DeleteCharacters
/*     */     extends ProjectComponent
/*     */     implements Filter, ChainableReader
/*     */   {
/* 607 */     private String deleteChars = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setChars(String deleteChars) {
/* 614 */       this.deleteChars = TokenFilter.resolveBackSlash(deleteChars);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String filter(String string) {
/* 623 */       StringBuilder output = new StringBuilder(string.length());
/* 624 */       for (int i = 0; i < string.length(); i++) {
/* 625 */         char ch = string.charAt(i);
/* 626 */         if (!isDeleteCharacter(ch)) {
/* 627 */           output.append(ch);
/*     */         }
/*     */       } 
/* 630 */       return output.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Reader chain(Reader reader) {
/* 641 */       return new BaseFilterReader(reader)
/*     */         {
/*     */ 
/*     */           
/*     */           public int read() throws IOException
/*     */           {
/*     */             while (true) {
/* 648 */               int c = this.in.read();
/* 649 */               if (c == -1) {
/* 650 */                 return c;
/*     */               }
/* 652 */               if (!TokenFilter.DeleteCharacters.this.isDeleteCharacter((char)c)) {
/* 653 */                 return c;
/*     */               }
/*     */             } 
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean isDeleteCharacter(char c) {
/* 667 */       for (int d = 0; d < this.deleteChars.length(); d++) {
/* 668 */         if (this.deleteChars.charAt(d) == c) {
/* 669 */           return true;
/*     */         }
/*     */       } 
/* 672 */       return false;
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
/*     */   public static String resolveBackSlash(String input) {
/* 691 */     return StringUtils.resolveBackSlash(input);
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
/*     */   public static int convertRegexOptions(String flags) {
/* 706 */     return RegexpUtil.asOptions(flags);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/filters/TokenFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */