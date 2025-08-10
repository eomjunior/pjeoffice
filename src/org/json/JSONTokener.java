/*     */ package org.json;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.nio.charset.Charset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JSONTokener
/*     */ {
/*     */   private long character;
/*     */   private boolean eof;
/*     */   private long index;
/*     */   private long line;
/*     */   private char previous;
/*     */   private final Reader reader;
/*     */   private boolean usePrevious;
/*     */   private long characterPreviousLine;
/*     */   
/*     */   public JSONTokener(Reader reader) {
/*  42 */     this.reader = reader.markSupported() ? reader : new BufferedReader(reader);
/*     */ 
/*     */     
/*  45 */     this.eof = false;
/*  46 */     this.usePrevious = false;
/*  47 */     this.previous = Character.MIN_VALUE;
/*  48 */     this.index = 0L;
/*  49 */     this.character = 1L;
/*  50 */     this.characterPreviousLine = 0L;
/*  51 */     this.line = 1L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONTokener(InputStream inputStream) {
/*  60 */     this(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONTokener(String s) {
/*  70 */     this(new StringReader(s));
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
/*     */   public void back() throws JSONException {
/*  82 */     if (this.usePrevious || this.index <= 0L) {
/*  83 */       throw new JSONException("Stepping back two steps is not supported");
/*     */     }
/*  85 */     decrementIndexes();
/*  86 */     this.usePrevious = true;
/*  87 */     this.eof = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void decrementIndexes() {
/*  94 */     this.index--;
/*  95 */     if (this.previous == '\r' || this.previous == '\n') {
/*  96 */       this.line--;
/*  97 */       this.character = this.characterPreviousLine;
/*  98 */     } else if (this.character > 0L) {
/*  99 */       this.character--;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int dehexchar(char c) {
/* 110 */     if (c >= '0' && c <= '9') {
/* 111 */       return c - 48;
/*     */     }
/* 113 */     if (c >= 'A' && c <= 'F') {
/* 114 */       return c - 55;
/*     */     }
/* 116 */     if (c >= 'a' && c <= 'f') {
/* 117 */       return c - 87;
/*     */     }
/* 119 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean end() {
/* 128 */     return (this.eof && !this.usePrevious);
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
/*     */   public boolean more() throws JSONException {
/* 140 */     if (this.usePrevious) {
/* 141 */       return true;
/*     */     }
/*     */     try {
/* 144 */       this.reader.mark(1);
/* 145 */     } catch (IOException e) {
/* 146 */       throw new JSONException("Unable to preserve stream position", e);
/*     */     } 
/*     */     
/*     */     try {
/* 150 */       if (this.reader.read() <= 0) {
/* 151 */         this.eof = true;
/* 152 */         return false;
/*     */       } 
/* 154 */       this.reader.reset();
/* 155 */     } catch (IOException e) {
/* 156 */       throw new JSONException("Unable to read the next character from the stream", e);
/*     */     } 
/* 158 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char next() throws JSONException {
/*     */     int c;
/* 170 */     if (this.usePrevious) {
/* 171 */       this.usePrevious = false;
/* 172 */       c = this.previous;
/*     */     } else {
/*     */       try {
/* 175 */         c = this.reader.read();
/* 176 */       } catch (IOException exception) {
/* 177 */         throw new JSONException(exception);
/*     */       } 
/*     */     } 
/* 180 */     if (c <= 0) {
/* 181 */       this.eof = true;
/* 182 */       return Character.MIN_VALUE;
/*     */     } 
/* 184 */     incrementIndexes(c);
/* 185 */     this.previous = (char)c;
/* 186 */     return this.previous;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected char getPrevious() {
/* 193 */     return this.previous;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void incrementIndexes(int c) {
/* 201 */     if (c > 0) {
/* 202 */       this.index++;
/* 203 */       if (c == 13) {
/* 204 */         this.line++;
/* 205 */         this.characterPreviousLine = this.character;
/* 206 */         this.character = 0L;
/* 207 */       } else if (c == 10) {
/* 208 */         if (this.previous != '\r') {
/* 209 */           this.line++;
/* 210 */           this.characterPreviousLine = this.character;
/*     */         } 
/* 212 */         this.character = 0L;
/*     */       } else {
/* 214 */         this.character++;
/*     */       } 
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
/*     */   public char next(char c) throws JSONException {
/* 227 */     char n = next();
/* 228 */     if (n != c) {
/* 229 */       if (n > '\000') {
/* 230 */         throw syntaxError("Expected '" + c + "' and instead saw '" + n + "'");
/*     */       }
/*     */       
/* 233 */       throw syntaxError("Expected '" + c + "' and instead saw ''");
/*     */     } 
/* 235 */     return n;
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
/*     */   public String next(int n) throws JSONException {
/* 249 */     if (n == 0) {
/* 250 */       return "";
/*     */     }
/*     */     
/* 253 */     char[] chars = new char[n];
/* 254 */     int pos = 0;
/*     */     
/* 256 */     while (pos < n) {
/* 257 */       chars[pos] = next();
/* 258 */       if (end()) {
/* 259 */         throw syntaxError("Substring bounds error");
/*     */       }
/* 261 */       pos++;
/*     */     } 
/* 263 */     return new String(chars);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char nextClean() throws JSONException {
/*     */     char c;
/*     */     do {
/* 274 */       c = next();
/* 275 */     } while (c != '\000' && c <= ' ');
/* 276 */     return c;
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
/*     */   public String nextString(char quote) throws JSONException {
/* 295 */     StringBuilder sb = new StringBuilder();
/*     */     while (true) {
/* 297 */       char c = next();
/* 298 */       switch (c) {
/*     */         case '\000':
/*     */         case '\n':
/*     */         case '\r':
/* 302 */           throw syntaxError("Unterminated string");
/*     */         case '\\':
/* 304 */           c = next();
/* 305 */           switch (c) {
/*     */             case 'b':
/* 307 */               sb.append('\b');
/*     */               continue;
/*     */             case 't':
/* 310 */               sb.append('\t');
/*     */               continue;
/*     */             case 'n':
/* 313 */               sb.append('\n');
/*     */               continue;
/*     */             case 'f':
/* 316 */               sb.append('\f');
/*     */               continue;
/*     */             case 'r':
/* 319 */               sb.append('\r');
/*     */               continue;
/*     */             case 'u':
/*     */               try {
/* 323 */                 sb.append((char)Integer.parseInt(next(4), 16));
/* 324 */               } catch (NumberFormatException e) {
/* 325 */                 throw syntaxError("Illegal escape.", e);
/*     */               } 
/*     */               continue;
/*     */             case '"':
/*     */             case '\'':
/*     */             case '/':
/*     */             case '\\':
/* 332 */               sb.append(c);
/*     */               continue;
/*     */           } 
/* 335 */           throw syntaxError("Illegal escape.");
/*     */       } 
/*     */ 
/*     */       
/* 339 */       if (c == quote) {
/* 340 */         return sb.toString();
/*     */       }
/* 342 */       sb.append(c);
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
/*     */   public String nextTo(char delimiter) throws JSONException {
/* 357 */     StringBuilder sb = new StringBuilder();
/*     */     while (true) {
/* 359 */       char c = next();
/* 360 */       if (c == delimiter || c == '\000' || c == '\n' || c == '\r') {
/* 361 */         if (c != '\000') {
/* 362 */           back();
/*     */         }
/* 364 */         return sb.toString().trim();
/*     */       } 
/* 366 */       sb.append(c);
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
/*     */   public String nextTo(String delimiters) throws JSONException {
/* 381 */     StringBuilder sb = new StringBuilder();
/*     */     while (true) {
/* 383 */       char c = next();
/* 384 */       if (delimiters.indexOf(c) >= 0 || c == '\000' || c == '\n' || c == '\r') {
/*     */         
/* 386 */         if (c != '\000') {
/* 387 */           back();
/*     */         }
/* 389 */         return sb.toString().trim();
/*     */       } 
/* 391 */       sb.append(c);
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
/*     */   public Object nextValue() throws JSONException {
/* 404 */     char c = nextClean();
/* 405 */     switch (c) {
/*     */       case '{':
/* 407 */         back();
/*     */         try {
/* 409 */           return new JSONObject(this);
/* 410 */         } catch (StackOverflowError e) {
/* 411 */           throw new JSONException("JSON Array or Object depth too large to process.", e);
/*     */         } 
/*     */       case '[':
/* 414 */         back();
/*     */         try {
/* 416 */           return new JSONArray(this);
/* 417 */         } catch (StackOverflowError e) {
/* 418 */           throw new JSONException("JSON Array or Object depth too large to process.", e);
/*     */         } 
/*     */     } 
/* 421 */     return nextSimpleValue(c);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Object nextSimpleValue(char c) {
/* 427 */     switch (c) {
/*     */       case '"':
/*     */       case '\'':
/* 430 */         return nextString(c);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 442 */     StringBuilder sb = new StringBuilder();
/* 443 */     while (c >= ' ' && ",:]}/\\\"[{;=#".indexOf(c) < 0) {
/* 444 */       sb.append(c);
/* 445 */       c = next();
/*     */     } 
/* 447 */     if (!this.eof) {
/* 448 */       back();
/*     */     }
/*     */     
/* 451 */     String string = sb.toString().trim();
/* 452 */     if ("".equals(string)) {
/* 453 */       throw syntaxError("Missing value");
/*     */     }
/* 455 */     return JSONObject.stringToValue(string);
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
/*     */   public char skipTo(char to) throws JSONException {
/*     */     try {
/* 471 */       long startIndex = this.index;
/* 472 */       long startCharacter = this.character;
/* 473 */       long startLine = this.line;
/* 474 */       this.reader.mark(1000000);
/*     */       while (true) {
/* 476 */         char c = next();
/* 477 */         if (c == '\000') {
/*     */ 
/*     */ 
/*     */           
/* 481 */           this.reader.reset();
/* 482 */           this.index = startIndex;
/* 483 */           this.character = startCharacter;
/* 484 */           this.line = startLine;
/* 485 */           return Character.MIN_VALUE;
/*     */         } 
/* 487 */         if (c == to) {
/* 488 */           this.reader.mark(1);
/*     */ 
/*     */ 
/*     */           
/* 492 */           back();
/* 493 */           return c;
/*     */         } 
/*     */       } 
/*     */     } catch (IOException exception) {
/*     */       throw new JSONException(exception);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public JSONException syntaxError(String message) {
/* 503 */     return new JSONException(message + toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONException syntaxError(String message, Throwable causedBy) {
/* 514 */     return new JSONException(message + toString(), causedBy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 524 */     return " at " + this.index + " [character " + this.character + " line " + this.line + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 529 */     if (this.reader != null)
/* 530 */       this.reader.close(); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/json/JSONTokener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */