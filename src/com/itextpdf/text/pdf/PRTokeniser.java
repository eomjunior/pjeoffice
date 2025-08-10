/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.exceptions.InvalidPdfException;
/*     */ import com.itextpdf.text.io.RandomAccessSourceFactory;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PRTokeniser
/*     */ {
/*  57 */   private final StringBuilder outBuf = new StringBuilder();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum TokenType
/*     */   {
/*  64 */     NUMBER,
/*  65 */     STRING,
/*  66 */     NAME,
/*  67 */     COMMENT,
/*  68 */     START_ARRAY,
/*  69 */     END_ARRAY,
/*  70 */     START_DIC,
/*  71 */     END_DIC,
/*  72 */     REF,
/*  73 */     OTHER,
/*  74 */     ENDOFFILE;
/*     */   }
/*     */   
/*  77 */   public static final boolean[] delims = new boolean[] { 
/*     */       true, true, false, false, false, false, false, false, false, false, 
/*     */       true, true, false, true, true, false, false, false, false, false, 
/*     */       false, false, false, false, false, false, false, false, false, false, 
/*     */       false, false, false, true, false, false, false, false, true, false, 
/*     */       false, true, true, false, false, false, false, false, true, false, 
/*     */       false, false, false, false, false, false, false, false, false, false, 
/*     */       false, true, false, true, false, false, false, false, false, false, 
/*     */       false, false, false, false, false, false, false, false, false, false, 
/*     */       false, false, false, false, false, false, false, false, false, false, 
/*     */       false, false, true, false, true, false, false, false, false, false, 
/*     */       false, false, false, false, false, false, false, false, false, false, 
/*     */       false, false, false, false, false, false, false, false, false, false, 
/*     */       false, false, false, false, false, false, false, false, false, false, 
/*     */       false, false, false, false, false, false, false, false, false, false, 
/*     */       false, false, false, false, false, false, false, false, false, false, 
/*     */       false, false, false, false, false, false, false, false, false, false, 
/*     */       false, false, false, false, false, false, false, false, false, false, 
/*     */       false, false, false, false, false, false, false, false, false, false, 
/*     */       false, false, false, false, false, false, false, false, false, false, 
/*     */       false, false, false, false, false, false, false, false, false, false, 
/*     */       false, false, false, false, false, false, false, false, false, false, 
/*     */       false, false, false, false, false, false, false, false, false, false, 
/*     */       false, false, false, false, false, false, false, false, false, false, 
/*     */       false, false, false, false, false, false, false, false, false, false, 
/*     */       false, false, false, false, false, false, false, false, false, false, 
/*     */       false, false, false, false, false, false, false };
/*     */ 
/*     */   
/*     */   static final String EMPTY = "";
/*     */ 
/*     */   
/*     */   private final RandomAccessFileOrArray file;
/*     */   
/*     */   protected TokenType type;
/*     */   
/*     */   protected String stringValue;
/*     */   
/*     */   protected int reference;
/*     */   
/*     */   protected int generation;
/*     */   
/*     */   protected boolean hexString;
/*     */ 
/*     */   
/*     */   public PRTokeniser(RandomAccessFileOrArray file) {
/* 123 */     this.file = file;
/*     */   }
/*     */   
/*     */   public void seek(long pos) throws IOException {
/* 127 */     this.file.seek(pos);
/*     */   }
/*     */   
/*     */   public long getFilePointer() throws IOException {
/* 131 */     return this.file.getFilePointer();
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 135 */     this.file.close();
/*     */   }
/*     */   
/*     */   public long length() throws IOException {
/* 139 */     return this.file.length();
/*     */   }
/*     */   
/*     */   public int read() throws IOException {
/* 143 */     return this.file.read();
/*     */   }
/*     */   
/*     */   public RandomAccessFileOrArray getSafeFile() {
/* 147 */     return new RandomAccessFileOrArray(this.file);
/*     */   }
/*     */ 
/*     */   
/*     */   public RandomAccessFileOrArray getFile() {
/* 152 */     return this.file;
/*     */   }
/*     */   
/*     */   public String readString(int size) throws IOException {
/* 156 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 158 */     while (size-- > 0) {
/* 159 */       int ch = read();
/* 160 */       if (ch == -1)
/*     */         break; 
/* 162 */       buf.append((char)ch);
/*     */     } 
/* 164 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean isWhitespace(int ch) {
/* 175 */     return isWhitespace(ch, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean isWhitespace(int ch, boolean isWhitespace) {
/* 186 */     return ((isWhitespace && ch == 0) || ch == 9 || ch == 10 || ch == 12 || ch == 13 || ch == 32);
/*     */   }
/*     */   
/*     */   public static final boolean isDelimiter(int ch) {
/* 190 */     return (ch == 40 || ch == 41 || ch == 60 || ch == 62 || ch == 91 || ch == 93 || ch == 47 || ch == 37);
/*     */   }
/*     */   
/*     */   public static final boolean isDelimiterWhitespace(int ch) {
/* 194 */     return delims[ch + 1];
/*     */   }
/*     */   
/*     */   public TokenType getTokenType() {
/* 198 */     return this.type;
/*     */   }
/*     */   
/*     */   public String getStringValue() {
/* 202 */     return this.stringValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getReference() {
/* 211 */     return this.reference;
/*     */   }
/*     */   
/*     */   public int getGeneration() {
/* 215 */     return this.generation;
/*     */   }
/*     */   
/*     */   public void backOnePosition(int ch) {
/* 219 */     if (ch != -1)
/* 220 */       this.file.pushBack((byte)ch); 
/*     */   }
/*     */   
/*     */   public void throwError(String error) throws IOException {
/* 224 */     throw new InvalidPdfException(MessageLocalization.getComposedMessage("1.at.file.pointer.2", new Object[] { error, String.valueOf(this.file.getFilePointer()) }));
/*     */   }
/*     */   
/*     */   public int getHeaderOffset() throws IOException {
/* 228 */     String str = readString(1024);
/* 229 */     int idx = str.indexOf("%PDF-");
/* 230 */     if (idx < 0) {
/* 231 */       idx = str.indexOf("%FDF-");
/* 232 */       if (idx < 0) {
/* 233 */         throw new InvalidPdfException(MessageLocalization.getComposedMessage("pdf.header.not.found", new Object[0]));
/*     */       }
/*     */     } 
/* 236 */     return idx;
/*     */   }
/*     */   
/*     */   public char checkPdfHeader() throws IOException {
/* 240 */     this.file.seek(0L);
/* 241 */     String str = readString(1024);
/* 242 */     int idx = str.indexOf("%PDF-");
/* 243 */     if (idx != 0)
/* 244 */       throw new InvalidPdfException(MessageLocalization.getComposedMessage("pdf.header.not.found", new Object[0])); 
/* 245 */     return str.charAt(7);
/*     */   }
/*     */   
/*     */   public void checkFdfHeader() throws IOException {
/* 249 */     this.file.seek(0L);
/* 250 */     String str = readString(1024);
/* 251 */     int idx = str.indexOf("%FDF-");
/* 252 */     if (idx != 0)
/* 253 */       throw new InvalidPdfException(MessageLocalization.getComposedMessage("fdf.header.not.found", new Object[0])); 
/*     */   }
/*     */   
/*     */   public long getStartxref() throws IOException {
/* 257 */     int arrLength = 1024;
/* 258 */     long fileLength = this.file.length();
/* 259 */     long pos = fileLength - arrLength;
/* 260 */     if (pos < 1L) pos = 1L; 
/* 261 */     while (pos > 0L) {
/* 262 */       this.file.seek(pos);
/* 263 */       String str = readString(arrLength);
/* 264 */       int idx = str.lastIndexOf("startxref");
/* 265 */       if (idx >= 0) return pos + idx; 
/* 266 */       pos = pos - arrLength + 9L;
/*     */     } 
/* 268 */     throw new InvalidPdfException(MessageLocalization.getComposedMessage("pdf.startxref.not.found", new Object[0]));
/*     */   }
/*     */   
/*     */   public static int getHex(int v) {
/* 272 */     if (v >= 48 && v <= 57)
/* 273 */       return v - 48; 
/* 274 */     if (v >= 65 && v <= 70)
/* 275 */       return v - 65 + 10; 
/* 276 */     if (v >= 97 && v <= 102)
/* 277 */       return v - 97 + 10; 
/* 278 */     return -1;
/*     */   }
/*     */   
/*     */   public void nextValidToken() throws IOException {
/* 282 */     int level = 0;
/* 283 */     String n1 = null;
/* 284 */     String n2 = null;
/* 285 */     long ptr = 0L;
/* 286 */     while (nextToken()) {
/* 287 */       if (this.type == TokenType.COMMENT)
/*     */         continue; 
/* 289 */       switch (level) {
/*     */         
/*     */         case 0:
/* 292 */           if (this.type != TokenType.NUMBER)
/*     */             return; 
/* 294 */           ptr = this.file.getFilePointer();
/* 295 */           n1 = this.stringValue;
/* 296 */           level++;
/*     */           continue;
/*     */ 
/*     */         
/*     */         case 1:
/* 301 */           if (this.type != TokenType.NUMBER) {
/* 302 */             this.file.seek(ptr);
/* 303 */             this.type = TokenType.NUMBER;
/* 304 */             this.stringValue = n1;
/*     */             return;
/*     */           } 
/* 307 */           n2 = this.stringValue;
/* 308 */           level++;
/*     */           continue;
/*     */       } 
/*     */ 
/*     */       
/* 313 */       if (this.type != TokenType.OTHER || !this.stringValue.equals("R")) {
/* 314 */         this.file.seek(ptr);
/* 315 */         this.type = TokenType.NUMBER;
/* 316 */         this.stringValue = n1;
/*     */         return;
/*     */       } 
/* 319 */       this.type = TokenType.REF;
/*     */       try {
/* 321 */         this.reference = Integer.parseInt(n1);
/* 322 */         this.generation = Integer.parseInt(n2);
/* 323 */       } catch (NumberFormatException ex) {
/* 324 */         this.reference = -1;
/* 325 */         this.generation = 0;
/*     */       } 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 332 */     if (level == 1) {
/* 333 */       this.type = TokenType.NUMBER;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean nextToken() throws IOException {
/*     */     // Byte code:
/*     */     //   0: iconst_0
/*     */     //   1: istore_1
/*     */     //   2: aload_0
/*     */     //   3: getfield file : Lcom/itextpdf/text/pdf/RandomAccessFileOrArray;
/*     */     //   6: invokevirtual read : ()I
/*     */     //   9: istore_1
/*     */     //   10: iload_1
/*     */     //   11: iconst_m1
/*     */     //   12: if_icmpeq -> 22
/*     */     //   15: iload_1
/*     */     //   16: invokestatic isWhitespace : (I)Z
/*     */     //   19: ifne -> 2
/*     */     //   22: iload_1
/*     */     //   23: iconst_m1
/*     */     //   24: if_icmpne -> 36
/*     */     //   27: aload_0
/*     */     //   28: getstatic com/itextpdf/text/pdf/PRTokeniser$TokenType.ENDOFFILE : Lcom/itextpdf/text/pdf/PRTokeniser$TokenType;
/*     */     //   31: putfield type : Lcom/itextpdf/text/pdf/PRTokeniser$TokenType;
/*     */     //   34: iconst_0
/*     */     //   35: ireturn
/*     */     //   36: aload_0
/*     */     //   37: getfield outBuf : Ljava/lang/StringBuilder;
/*     */     //   40: iconst_0
/*     */     //   41: invokevirtual setLength : (I)V
/*     */     //   44: aload_0
/*     */     //   45: ldc ''
/*     */     //   47: putfield stringValue : Ljava/lang/String;
/*     */     //   50: iload_1
/*     */     //   51: lookupswitch default -> 931, 37 -> 457, 40 -> 492, 47 -> 136, 60 -> 260, 62 -> 223, 91 -> 116, 93 -> 126
/*     */     //   116: aload_0
/*     */     //   117: getstatic com/itextpdf/text/pdf/PRTokeniser$TokenType.START_ARRAY : Lcom/itextpdf/text/pdf/PRTokeniser$TokenType;
/*     */     //   120: putfield type : Lcom/itextpdf/text/pdf/PRTokeniser$TokenType;
/*     */     //   123: goto -> 1161
/*     */     //   126: aload_0
/*     */     //   127: getstatic com/itextpdf/text/pdf/PRTokeniser$TokenType.END_ARRAY : Lcom/itextpdf/text/pdf/PRTokeniser$TokenType;
/*     */     //   130: putfield type : Lcom/itextpdf/text/pdf/PRTokeniser$TokenType;
/*     */     //   133: goto -> 1161
/*     */     //   136: aload_0
/*     */     //   137: getfield outBuf : Ljava/lang/StringBuilder;
/*     */     //   140: iconst_0
/*     */     //   141: invokevirtual setLength : (I)V
/*     */     //   144: aload_0
/*     */     //   145: getstatic com/itextpdf/text/pdf/PRTokeniser$TokenType.NAME : Lcom/itextpdf/text/pdf/PRTokeniser$TokenType;
/*     */     //   148: putfield type : Lcom/itextpdf/text/pdf/PRTokeniser$TokenType;
/*     */     //   151: aload_0
/*     */     //   152: getfield file : Lcom/itextpdf/text/pdf/RandomAccessFileOrArray;
/*     */     //   155: invokevirtual read : ()I
/*     */     //   158: istore_1
/*     */     //   159: getstatic com/itextpdf/text/pdf/PRTokeniser.delims : [Z
/*     */     //   162: iload_1
/*     */     //   163: iconst_1
/*     */     //   164: iadd
/*     */     //   165: baload
/*     */     //   166: ifeq -> 172
/*     */     //   169: goto -> 215
/*     */     //   172: iload_1
/*     */     //   173: bipush #35
/*     */     //   175: if_icmpne -> 202
/*     */     //   178: aload_0
/*     */     //   179: getfield file : Lcom/itextpdf/text/pdf/RandomAccessFileOrArray;
/*     */     //   182: invokevirtual read : ()I
/*     */     //   185: invokestatic getHex : (I)I
/*     */     //   188: iconst_4
/*     */     //   189: ishl
/*     */     //   190: aload_0
/*     */     //   191: getfield file : Lcom/itextpdf/text/pdf/RandomAccessFileOrArray;
/*     */     //   194: invokevirtual read : ()I
/*     */     //   197: invokestatic getHex : (I)I
/*     */     //   200: iadd
/*     */     //   201: istore_1
/*     */     //   202: aload_0
/*     */     //   203: getfield outBuf : Ljava/lang/StringBuilder;
/*     */     //   206: iload_1
/*     */     //   207: i2c
/*     */     //   208: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   211: pop
/*     */     //   212: goto -> 151
/*     */     //   215: aload_0
/*     */     //   216: iload_1
/*     */     //   217: invokevirtual backOnePosition : (I)V
/*     */     //   220: goto -> 1161
/*     */     //   223: aload_0
/*     */     //   224: getfield file : Lcom/itextpdf/text/pdf/RandomAccessFileOrArray;
/*     */     //   227: invokevirtual read : ()I
/*     */     //   230: istore_1
/*     */     //   231: iload_1
/*     */     //   232: bipush #62
/*     */     //   234: if_icmpeq -> 250
/*     */     //   237: aload_0
/*     */     //   238: ldc 'greaterthan.not.expected'
/*     */     //   240: iconst_0
/*     */     //   241: anewarray java/lang/Object
/*     */     //   244: invokestatic getComposedMessage : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   247: invokevirtual throwError : (Ljava/lang/String;)V
/*     */     //   250: aload_0
/*     */     //   251: getstatic com/itextpdf/text/pdf/PRTokeniser$TokenType.END_DIC : Lcom/itextpdf/text/pdf/PRTokeniser$TokenType;
/*     */     //   254: putfield type : Lcom/itextpdf/text/pdf/PRTokeniser$TokenType;
/*     */     //   257: goto -> 1161
/*     */     //   260: aload_0
/*     */     //   261: getfield file : Lcom/itextpdf/text/pdf/RandomAccessFileOrArray;
/*     */     //   264: invokevirtual read : ()I
/*     */     //   267: istore_2
/*     */     //   268: iload_2
/*     */     //   269: bipush #60
/*     */     //   271: if_icmpne -> 284
/*     */     //   274: aload_0
/*     */     //   275: getstatic com/itextpdf/text/pdf/PRTokeniser$TokenType.START_DIC : Lcom/itextpdf/text/pdf/PRTokeniser$TokenType;
/*     */     //   278: putfield type : Lcom/itextpdf/text/pdf/PRTokeniser$TokenType;
/*     */     //   281: goto -> 1161
/*     */     //   284: aload_0
/*     */     //   285: getfield outBuf : Ljava/lang/StringBuilder;
/*     */     //   288: iconst_0
/*     */     //   289: invokevirtual setLength : (I)V
/*     */     //   292: aload_0
/*     */     //   293: getstatic com/itextpdf/text/pdf/PRTokeniser$TokenType.STRING : Lcom/itextpdf/text/pdf/PRTokeniser$TokenType;
/*     */     //   296: putfield type : Lcom/itextpdf/text/pdf/PRTokeniser$TokenType;
/*     */     //   299: aload_0
/*     */     //   300: iconst_1
/*     */     //   301: putfield hexString : Z
/*     */     //   304: iconst_0
/*     */     //   305: istore_3
/*     */     //   306: iload_2
/*     */     //   307: invokestatic isWhitespace : (I)Z
/*     */     //   310: ifeq -> 324
/*     */     //   313: aload_0
/*     */     //   314: getfield file : Lcom/itextpdf/text/pdf/RandomAccessFileOrArray;
/*     */     //   317: invokevirtual read : ()I
/*     */     //   320: istore_2
/*     */     //   321: goto -> 306
/*     */     //   324: iload_2
/*     */     //   325: bipush #62
/*     */     //   327: if_icmpne -> 333
/*     */     //   330: goto -> 433
/*     */     //   333: iload_2
/*     */     //   334: invokestatic getHex : (I)I
/*     */     //   337: istore_2
/*     */     //   338: iload_2
/*     */     //   339: ifge -> 345
/*     */     //   342: goto -> 433
/*     */     //   345: aload_0
/*     */     //   346: getfield file : Lcom/itextpdf/text/pdf/RandomAccessFileOrArray;
/*     */     //   349: invokevirtual read : ()I
/*     */     //   352: istore_3
/*     */     //   353: iload_3
/*     */     //   354: invokestatic isWhitespace : (I)Z
/*     */     //   357: ifeq -> 371
/*     */     //   360: aload_0
/*     */     //   361: getfield file : Lcom/itextpdf/text/pdf/RandomAccessFileOrArray;
/*     */     //   364: invokevirtual read : ()I
/*     */     //   367: istore_3
/*     */     //   368: goto -> 353
/*     */     //   371: iload_3
/*     */     //   372: bipush #62
/*     */     //   374: if_icmpne -> 394
/*     */     //   377: iload_2
/*     */     //   378: iconst_4
/*     */     //   379: ishl
/*     */     //   380: istore_1
/*     */     //   381: aload_0
/*     */     //   382: getfield outBuf : Ljava/lang/StringBuilder;
/*     */     //   385: iload_1
/*     */     //   386: i2c
/*     */     //   387: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   390: pop
/*     */     //   391: goto -> 433
/*     */     //   394: iload_3
/*     */     //   395: invokestatic getHex : (I)I
/*     */     //   398: istore_3
/*     */     //   399: iload_3
/*     */     //   400: ifge -> 406
/*     */     //   403: goto -> 433
/*     */     //   406: iload_2
/*     */     //   407: iconst_4
/*     */     //   408: ishl
/*     */     //   409: iload_3
/*     */     //   410: iadd
/*     */     //   411: istore_1
/*     */     //   412: aload_0
/*     */     //   413: getfield outBuf : Ljava/lang/StringBuilder;
/*     */     //   416: iload_1
/*     */     //   417: i2c
/*     */     //   418: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   421: pop
/*     */     //   422: aload_0
/*     */     //   423: getfield file : Lcom/itextpdf/text/pdf/RandomAccessFileOrArray;
/*     */     //   426: invokevirtual read : ()I
/*     */     //   429: istore_2
/*     */     //   430: goto -> 306
/*     */     //   433: iload_2
/*     */     //   434: iflt -> 441
/*     */     //   437: iload_3
/*     */     //   438: ifge -> 1161
/*     */     //   441: aload_0
/*     */     //   442: ldc 'error.reading.string'
/*     */     //   444: iconst_0
/*     */     //   445: anewarray java/lang/Object
/*     */     //   448: invokestatic getComposedMessage : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   451: invokevirtual throwError : (Ljava/lang/String;)V
/*     */     //   454: goto -> 1161
/*     */     //   457: aload_0
/*     */     //   458: getstatic com/itextpdf/text/pdf/PRTokeniser$TokenType.COMMENT : Lcom/itextpdf/text/pdf/PRTokeniser$TokenType;
/*     */     //   461: putfield type : Lcom/itextpdf/text/pdf/PRTokeniser$TokenType;
/*     */     //   464: aload_0
/*     */     //   465: getfield file : Lcom/itextpdf/text/pdf/RandomAccessFileOrArray;
/*     */     //   468: invokevirtual read : ()I
/*     */     //   471: istore_1
/*     */     //   472: iload_1
/*     */     //   473: iconst_m1
/*     */     //   474: if_icmpeq -> 1161
/*     */     //   477: iload_1
/*     */     //   478: bipush #13
/*     */     //   480: if_icmpeq -> 1161
/*     */     //   483: iload_1
/*     */     //   484: bipush #10
/*     */     //   486: if_icmpne -> 464
/*     */     //   489: goto -> 1161
/*     */     //   492: aload_0
/*     */     //   493: getfield outBuf : Ljava/lang/StringBuilder;
/*     */     //   496: iconst_0
/*     */     //   497: invokevirtual setLength : (I)V
/*     */     //   500: aload_0
/*     */     //   501: getstatic com/itextpdf/text/pdf/PRTokeniser$TokenType.STRING : Lcom/itextpdf/text/pdf/PRTokeniser$TokenType;
/*     */     //   504: putfield type : Lcom/itextpdf/text/pdf/PRTokeniser$TokenType;
/*     */     //   507: aload_0
/*     */     //   508: iconst_0
/*     */     //   509: putfield hexString : Z
/*     */     //   512: iconst_0
/*     */     //   513: istore_2
/*     */     //   514: aload_0
/*     */     //   515: getfield file : Lcom/itextpdf/text/pdf/RandomAccessFileOrArray;
/*     */     //   518: invokevirtual read : ()I
/*     */     //   521: istore_1
/*     */     //   522: iload_1
/*     */     //   523: iconst_m1
/*     */     //   524: if_icmpne -> 530
/*     */     //   527: goto -> 910
/*     */     //   530: iload_1
/*     */     //   531: bipush #40
/*     */     //   533: if_icmpne -> 542
/*     */     //   536: iinc #2, 1
/*     */     //   539: goto -> 889
/*     */     //   542: iload_1
/*     */     //   543: bipush #41
/*     */     //   545: if_icmpne -> 554
/*     */     //   548: iinc #2, -1
/*     */     //   551: goto -> 889
/*     */     //   554: iload_1
/*     */     //   555: bipush #92
/*     */     //   557: if_icmpne -> 854
/*     */     //   560: iconst_0
/*     */     //   561: istore_3
/*     */     //   562: aload_0
/*     */     //   563: getfield file : Lcom/itextpdf/text/pdf/RandomAccessFileOrArray;
/*     */     //   566: invokevirtual read : ()I
/*     */     //   569: istore_1
/*     */     //   570: iload_1
/*     */     //   571: lookupswitch default -> 722, 10 -> 717, 13 -> 693, 40 -> 690, 41 -> 690, 92 -> 690, 98 -> 678, 102 -> 684, 110 -> 660, 114 -> 666, 116 -> 672
/*     */     //   660: bipush #10
/*     */     //   662: istore_1
/*     */     //   663: goto -> 837
/*     */     //   666: bipush #13
/*     */     //   668: istore_1
/*     */     //   669: goto -> 837
/*     */     //   672: bipush #9
/*     */     //   674: istore_1
/*     */     //   675: goto -> 837
/*     */     //   678: bipush #8
/*     */     //   680: istore_1
/*     */     //   681: goto -> 837
/*     */     //   684: bipush #12
/*     */     //   686: istore_1
/*     */     //   687: goto -> 837
/*     */     //   690: goto -> 837
/*     */     //   693: iconst_1
/*     */     //   694: istore_3
/*     */     //   695: aload_0
/*     */     //   696: getfield file : Lcom/itextpdf/text/pdf/RandomAccessFileOrArray;
/*     */     //   699: invokevirtual read : ()I
/*     */     //   702: istore_1
/*     */     //   703: iload_1
/*     */     //   704: bipush #10
/*     */     //   706: if_icmpeq -> 837
/*     */     //   709: aload_0
/*     */     //   710: iload_1
/*     */     //   711: invokevirtual backOnePosition : (I)V
/*     */     //   714: goto -> 837
/*     */     //   717: iconst_1
/*     */     //   718: istore_3
/*     */     //   719: goto -> 837
/*     */     //   722: iload_1
/*     */     //   723: bipush #48
/*     */     //   725: if_icmplt -> 837
/*     */     //   728: iload_1
/*     */     //   729: bipush #55
/*     */     //   731: if_icmple -> 737
/*     */     //   734: goto -> 837
/*     */     //   737: iload_1
/*     */     //   738: bipush #48
/*     */     //   740: isub
/*     */     //   741: istore #4
/*     */     //   743: aload_0
/*     */     //   744: getfield file : Lcom/itextpdf/text/pdf/RandomAccessFileOrArray;
/*     */     //   747: invokevirtual read : ()I
/*     */     //   750: istore_1
/*     */     //   751: iload_1
/*     */     //   752: bipush #48
/*     */     //   754: if_icmplt -> 763
/*     */     //   757: iload_1
/*     */     //   758: bipush #55
/*     */     //   760: if_icmple -> 774
/*     */     //   763: aload_0
/*     */     //   764: iload_1
/*     */     //   765: invokevirtual backOnePosition : (I)V
/*     */     //   768: iload #4
/*     */     //   770: istore_1
/*     */     //   771: goto -> 837
/*     */     //   774: iload #4
/*     */     //   776: iconst_3
/*     */     //   777: ishl
/*     */     //   778: iload_1
/*     */     //   779: iadd
/*     */     //   780: bipush #48
/*     */     //   782: isub
/*     */     //   783: istore #4
/*     */     //   785: aload_0
/*     */     //   786: getfield file : Lcom/itextpdf/text/pdf/RandomAccessFileOrArray;
/*     */     //   789: invokevirtual read : ()I
/*     */     //   792: istore_1
/*     */     //   793: iload_1
/*     */     //   794: bipush #48
/*     */     //   796: if_icmplt -> 805
/*     */     //   799: iload_1
/*     */     //   800: bipush #55
/*     */     //   802: if_icmple -> 816
/*     */     //   805: aload_0
/*     */     //   806: iload_1
/*     */     //   807: invokevirtual backOnePosition : (I)V
/*     */     //   810: iload #4
/*     */     //   812: istore_1
/*     */     //   813: goto -> 837
/*     */     //   816: iload #4
/*     */     //   818: iconst_3
/*     */     //   819: ishl
/*     */     //   820: iload_1
/*     */     //   821: iadd
/*     */     //   822: bipush #48
/*     */     //   824: isub
/*     */     //   825: istore #4
/*     */     //   827: iload #4
/*     */     //   829: sipush #255
/*     */     //   832: iand
/*     */     //   833: istore_1
/*     */     //   834: goto -> 837
/*     */     //   837: iload_3
/*     */     //   838: ifeq -> 844
/*     */     //   841: goto -> 514
/*     */     //   844: iload_1
/*     */     //   845: ifge -> 851
/*     */     //   848: goto -> 910
/*     */     //   851: goto -> 889
/*     */     //   854: iload_1
/*     */     //   855: bipush #13
/*     */     //   857: if_icmpne -> 889
/*     */     //   860: aload_0
/*     */     //   861: getfield file : Lcom/itextpdf/text/pdf/RandomAccessFileOrArray;
/*     */     //   864: invokevirtual read : ()I
/*     */     //   867: istore_1
/*     */     //   868: iload_1
/*     */     //   869: ifge -> 875
/*     */     //   872: goto -> 910
/*     */     //   875: iload_1
/*     */     //   876: bipush #10
/*     */     //   878: if_icmpeq -> 889
/*     */     //   881: aload_0
/*     */     //   882: iload_1
/*     */     //   883: invokevirtual backOnePosition : (I)V
/*     */     //   886: bipush #10
/*     */     //   888: istore_1
/*     */     //   889: iload_2
/*     */     //   890: iconst_m1
/*     */     //   891: if_icmpne -> 897
/*     */     //   894: goto -> 910
/*     */     //   897: aload_0
/*     */     //   898: getfield outBuf : Ljava/lang/StringBuilder;
/*     */     //   901: iload_1
/*     */     //   902: i2c
/*     */     //   903: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   906: pop
/*     */     //   907: goto -> 514
/*     */     //   910: iload_1
/*     */     //   911: iconst_m1
/*     */     //   912: if_icmpne -> 1161
/*     */     //   915: aload_0
/*     */     //   916: ldc 'error.reading.string'
/*     */     //   918: iconst_0
/*     */     //   919: anewarray java/lang/Object
/*     */     //   922: invokestatic getComposedMessage : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   925: invokevirtual throwError : (Ljava/lang/String;)V
/*     */     //   928: goto -> 1161
/*     */     //   931: aload_0
/*     */     //   932: getfield outBuf : Ljava/lang/StringBuilder;
/*     */     //   935: iconst_0
/*     */     //   936: invokevirtual setLength : (I)V
/*     */     //   939: iload_1
/*     */     //   940: bipush #45
/*     */     //   942: if_icmpeq -> 969
/*     */     //   945: iload_1
/*     */     //   946: bipush #43
/*     */     //   948: if_icmpeq -> 969
/*     */     //   951: iload_1
/*     */     //   952: bipush #46
/*     */     //   954: if_icmpeq -> 969
/*     */     //   957: iload_1
/*     */     //   958: bipush #48
/*     */     //   960: if_icmplt -> 1116
/*     */     //   963: iload_1
/*     */     //   964: bipush #57
/*     */     //   966: if_icmpgt -> 1116
/*     */     //   969: aload_0
/*     */     //   970: getstatic com/itextpdf/text/pdf/PRTokeniser$TokenType.NUMBER : Lcom/itextpdf/text/pdf/PRTokeniser$TokenType;
/*     */     //   973: putfield type : Lcom/itextpdf/text/pdf/PRTokeniser$TokenType;
/*     */     //   976: iconst_0
/*     */     //   977: istore_2
/*     */     //   978: iconst_0
/*     */     //   979: istore_3
/*     */     //   980: iload_1
/*     */     //   981: bipush #45
/*     */     //   983: if_icmpne -> 1016
/*     */     //   986: iinc #3, 1
/*     */     //   989: aload_0
/*     */     //   990: getfield file : Lcom/itextpdf/text/pdf/RandomAccessFileOrArray;
/*     */     //   993: invokevirtual read : ()I
/*     */     //   996: istore_1
/*     */     //   997: iload_1
/*     */     //   998: bipush #45
/*     */     //   1000: if_icmpeq -> 986
/*     */     //   1003: aload_0
/*     */     //   1004: getfield outBuf : Ljava/lang/StringBuilder;
/*     */     //   1007: bipush #45
/*     */     //   1009: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   1012: pop
/*     */     //   1013: goto -> 1034
/*     */     //   1016: aload_0
/*     */     //   1017: getfield outBuf : Ljava/lang/StringBuilder;
/*     */     //   1020: iload_1
/*     */     //   1021: i2c
/*     */     //   1022: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   1025: pop
/*     */     //   1026: aload_0
/*     */     //   1027: getfield file : Lcom/itextpdf/text/pdf/RandomAccessFileOrArray;
/*     */     //   1030: invokevirtual read : ()I
/*     */     //   1033: istore_1
/*     */     //   1034: iload_1
/*     */     //   1035: iconst_m1
/*     */     //   1036: if_icmpeq -> 1086
/*     */     //   1039: iload_1
/*     */     //   1040: bipush #48
/*     */     //   1042: if_icmplt -> 1051
/*     */     //   1045: iload_1
/*     */     //   1046: bipush #57
/*     */     //   1048: if_icmple -> 1057
/*     */     //   1051: iload_1
/*     */     //   1052: bipush #46
/*     */     //   1054: if_icmpne -> 1086
/*     */     //   1057: iload_1
/*     */     //   1058: bipush #46
/*     */     //   1060: if_icmpne -> 1065
/*     */     //   1063: iconst_1
/*     */     //   1064: istore_2
/*     */     //   1065: aload_0
/*     */     //   1066: getfield outBuf : Ljava/lang/StringBuilder;
/*     */     //   1069: iload_1
/*     */     //   1070: i2c
/*     */     //   1071: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   1074: pop
/*     */     //   1075: aload_0
/*     */     //   1076: getfield file : Lcom/itextpdf/text/pdf/RandomAccessFileOrArray;
/*     */     //   1079: invokevirtual read : ()I
/*     */     //   1082: istore_1
/*     */     //   1083: goto -> 1034
/*     */     //   1086: iload_3
/*     */     //   1087: iconst_1
/*     */     //   1088: if_icmple -> 1113
/*     */     //   1091: iload_2
/*     */     //   1092: ifne -> 1113
/*     */     //   1095: aload_0
/*     */     //   1096: getfield outBuf : Ljava/lang/StringBuilder;
/*     */     //   1099: iconst_0
/*     */     //   1100: invokevirtual setLength : (I)V
/*     */     //   1103: aload_0
/*     */     //   1104: getfield outBuf : Ljava/lang/StringBuilder;
/*     */     //   1107: bipush #48
/*     */     //   1109: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   1112: pop
/*     */     //   1113: goto -> 1151
/*     */     //   1116: aload_0
/*     */     //   1117: getstatic com/itextpdf/text/pdf/PRTokeniser$TokenType.OTHER : Lcom/itextpdf/text/pdf/PRTokeniser$TokenType;
/*     */     //   1120: putfield type : Lcom/itextpdf/text/pdf/PRTokeniser$TokenType;
/*     */     //   1123: aload_0
/*     */     //   1124: getfield outBuf : Ljava/lang/StringBuilder;
/*     */     //   1127: iload_1
/*     */     //   1128: i2c
/*     */     //   1129: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   1132: pop
/*     */     //   1133: aload_0
/*     */     //   1134: getfield file : Lcom/itextpdf/text/pdf/RandomAccessFileOrArray;
/*     */     //   1137: invokevirtual read : ()I
/*     */     //   1140: istore_1
/*     */     //   1141: getstatic com/itextpdf/text/pdf/PRTokeniser.delims : [Z
/*     */     //   1144: iload_1
/*     */     //   1145: iconst_1
/*     */     //   1146: iadd
/*     */     //   1147: baload
/*     */     //   1148: ifeq -> 1123
/*     */     //   1151: iload_1
/*     */     //   1152: iconst_m1
/*     */     //   1153: if_icmpeq -> 1161
/*     */     //   1156: aload_0
/*     */     //   1157: iload_1
/*     */     //   1158: invokevirtual backOnePosition : (I)V
/*     */     //   1161: aload_0
/*     */     //   1162: getfield outBuf : Ljava/lang/StringBuilder;
/*     */     //   1165: ifnull -> 1179
/*     */     //   1168: aload_0
/*     */     //   1169: aload_0
/*     */     //   1170: getfield outBuf : Ljava/lang/StringBuilder;
/*     */     //   1173: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   1176: putfield stringValue : Ljava/lang/String;
/*     */     //   1179: iconst_1
/*     */     //   1180: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #341	-> 0
/*     */     //   #343	-> 2
/*     */     //   #344	-> 10
/*     */     //   #345	-> 22
/*     */     //   #346	-> 27
/*     */     //   #347	-> 34
/*     */     //   #353	-> 36
/*     */     //   #354	-> 44
/*     */     //   #356	-> 50
/*     */     //   #358	-> 116
/*     */     //   #359	-> 123
/*     */     //   #361	-> 126
/*     */     //   #362	-> 133
/*     */     //   #365	-> 136
/*     */     //   #366	-> 144
/*     */     //   #368	-> 151
/*     */     //   #369	-> 159
/*     */     //   #370	-> 169
/*     */     //   #371	-> 172
/*     */     //   #372	-> 178
/*     */     //   #374	-> 202
/*     */     //   #376	-> 215
/*     */     //   #377	-> 220
/*     */     //   #380	-> 223
/*     */     //   #381	-> 231
/*     */     //   #382	-> 237
/*     */     //   #383	-> 250
/*     */     //   #384	-> 257
/*     */     //   #387	-> 260
/*     */     //   #388	-> 268
/*     */     //   #389	-> 274
/*     */     //   #390	-> 281
/*     */     //   #392	-> 284
/*     */     //   #393	-> 292
/*     */     //   #394	-> 299
/*     */     //   #395	-> 304
/*     */     //   #397	-> 306
/*     */     //   #398	-> 313
/*     */     //   #399	-> 324
/*     */     //   #400	-> 330
/*     */     //   #401	-> 333
/*     */     //   #402	-> 338
/*     */     //   #403	-> 342
/*     */     //   #404	-> 345
/*     */     //   #405	-> 353
/*     */     //   #406	-> 360
/*     */     //   #407	-> 371
/*     */     //   #408	-> 377
/*     */     //   #409	-> 381
/*     */     //   #410	-> 391
/*     */     //   #412	-> 394
/*     */     //   #413	-> 399
/*     */     //   #414	-> 403
/*     */     //   #415	-> 406
/*     */     //   #416	-> 412
/*     */     //   #417	-> 422
/*     */     //   #419	-> 433
/*     */     //   #420	-> 441
/*     */     //   #424	-> 457
/*     */     //   #426	-> 464
/*     */     //   #427	-> 472
/*     */     //   #428	-> 489
/*     */     //   #431	-> 492
/*     */     //   #432	-> 500
/*     */     //   #433	-> 507
/*     */     //   #434	-> 512
/*     */     //   #436	-> 514
/*     */     //   #437	-> 522
/*     */     //   #438	-> 527
/*     */     //   #439	-> 530
/*     */     //   #440	-> 536
/*     */     //   #442	-> 542
/*     */     //   #443	-> 548
/*     */     //   #445	-> 554
/*     */     //   #446	-> 560
/*     */     //   #447	-> 562
/*     */     //   #448	-> 570
/*     */     //   #450	-> 660
/*     */     //   #451	-> 663
/*     */     //   #453	-> 666
/*     */     //   #454	-> 669
/*     */     //   #456	-> 672
/*     */     //   #457	-> 675
/*     */     //   #459	-> 678
/*     */     //   #460	-> 681
/*     */     //   #462	-> 684
/*     */     //   #463	-> 687
/*     */     //   #467	-> 690
/*     */     //   #469	-> 693
/*     */     //   #470	-> 695
/*     */     //   #471	-> 703
/*     */     //   #472	-> 709
/*     */     //   #475	-> 717
/*     */     //   #476	-> 719
/*     */     //   #479	-> 722
/*     */     //   #480	-> 734
/*     */     //   #482	-> 737
/*     */     //   #483	-> 743
/*     */     //   #484	-> 751
/*     */     //   #485	-> 763
/*     */     //   #486	-> 768
/*     */     //   #487	-> 771
/*     */     //   #489	-> 774
/*     */     //   #490	-> 785
/*     */     //   #491	-> 793
/*     */     //   #492	-> 805
/*     */     //   #493	-> 810
/*     */     //   #494	-> 813
/*     */     //   #496	-> 816
/*     */     //   #497	-> 827
/*     */     //   #498	-> 834
/*     */     //   #501	-> 837
/*     */     //   #502	-> 841
/*     */     //   #503	-> 844
/*     */     //   #504	-> 848
/*     */     //   #505	-> 851
/*     */     //   #506	-> 854
/*     */     //   #507	-> 860
/*     */     //   #508	-> 868
/*     */     //   #509	-> 872
/*     */     //   #510	-> 875
/*     */     //   #511	-> 881
/*     */     //   #512	-> 886
/*     */     //   #515	-> 889
/*     */     //   #516	-> 894
/*     */     //   #517	-> 897
/*     */     //   #519	-> 910
/*     */     //   #520	-> 915
/*     */     //   #525	-> 931
/*     */     //   #526	-> 939
/*     */     //   #527	-> 969
/*     */     //   #528	-> 976
/*     */     //   #529	-> 978
/*     */     //   #530	-> 980
/*     */     //   #533	-> 986
/*     */     //   #534	-> 989
/*     */     //   #535	-> 997
/*     */     //   #536	-> 1003
/*     */     //   #539	-> 1016
/*     */     //   #542	-> 1026
/*     */     //   #544	-> 1034
/*     */     //   #545	-> 1057
/*     */     //   #546	-> 1063
/*     */     //   #547	-> 1065
/*     */     //   #548	-> 1075
/*     */     //   #550	-> 1086
/*     */     //   #553	-> 1095
/*     */     //   #554	-> 1103
/*     */     //   #556	-> 1113
/*     */     //   #558	-> 1116
/*     */     //   #560	-> 1123
/*     */     //   #561	-> 1133
/*     */     //   #562	-> 1141
/*     */     //   #564	-> 1151
/*     */     //   #565	-> 1156
/*     */     //   #569	-> 1161
/*     */     //   #570	-> 1168
/*     */     //   #571	-> 1179
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   268	189	2	v1	I
/*     */     //   306	151	3	v2	I
/*     */     //   743	94	4	octal	I
/*     */     //   562	289	3	lineBreak	Z
/*     */     //   514	417	2	nesting	I
/*     */     //   978	135	2	isReal	Z
/*     */     //   980	133	3	numberOfMinuses	I
/*     */     //   0	1181	0	this	Lcom/itextpdf/text/pdf/PRTokeniser;
/*     */     //   2	1179	1	ch	I
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long longValue() {
/* 575 */     return Long.parseLong(this.stringValue);
/*     */   }
/*     */   
/*     */   public int intValue() {
/* 579 */     return Integer.parseInt(this.stringValue);
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
/*     */   public boolean readLineSegment(byte[] input) throws IOException {
/* 594 */     return readLineSegment(input, true);
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
/*     */   public boolean readLineSegment(byte[] input, boolean isNullWhitespace) throws IOException {
/* 610 */     int c = -1;
/* 611 */     boolean eol = false;
/* 612 */     int ptr = 0;
/* 613 */     int len = input.length;
/*     */ 
/*     */ 
/*     */     
/* 617 */     if (ptr < len) {
/* 618 */       while (isWhitespace(c = read(), isNullWhitespace));
/*     */     }
/* 620 */     while (!eol && ptr < len) {
/* 621 */       long cur; switch (c) {
/*     */         case -1:
/*     */         case 10:
/* 624 */           eol = true;
/*     */           break;
/*     */         case 13:
/* 627 */           eol = true;
/* 628 */           cur = getFilePointer();
/* 629 */           if (read() != 10) {
/* 630 */             seek(cur);
/*     */           }
/*     */           break;
/*     */         default:
/* 634 */           input[ptr++] = (byte)c;
/*     */           break;
/*     */       } 
/*     */ 
/*     */       
/* 639 */       if (eol || len <= ptr) {
/*     */         break;
/*     */       }
/* 642 */       c = read();
/*     */     } 
/*     */     
/* 645 */     if (ptr >= len) {
/* 646 */       eol = false;
/* 647 */       while (!eol) {
/* 648 */         long cur; switch (c = read()) {
/*     */           case -1:
/*     */           case 10:
/* 651 */             eol = true;
/*     */           
/*     */           case 13:
/* 654 */             eol = true;
/* 655 */             cur = getFilePointer();
/* 656 */             if (read() != 10) {
/* 657 */               seek(cur);
/*     */             }
/*     */         } 
/*     */ 
/*     */       
/*     */       } 
/*     */     } 
/* 664 */     if (c == -1 && ptr == 0) {
/* 665 */       return false;
/*     */     }
/* 667 */     if (ptr + 2 <= len) {
/* 668 */       input[ptr++] = 32;
/* 669 */       input[ptr] = 88;
/*     */     } 
/* 671 */     return true;
/*     */   }
/*     */   
/*     */   public static long[] checkObjectStart(byte[] line) {
/*     */     try {
/* 676 */       PRTokeniser tk = new PRTokeniser(new RandomAccessFileOrArray((new RandomAccessSourceFactory()).createSource(line)));
/* 677 */       int num = 0;
/* 678 */       int gen = 0;
/* 679 */       if (!tk.nextToken() || tk.getTokenType() != TokenType.NUMBER)
/* 680 */         return null; 
/* 681 */       num = tk.intValue();
/* 682 */       if (!tk.nextToken() || tk.getTokenType() != TokenType.NUMBER)
/* 683 */         return null; 
/* 684 */       gen = tk.intValue();
/* 685 */       if (!tk.nextToken())
/* 686 */         return null; 
/* 687 */       if (!tk.getStringValue().equals("obj"))
/* 688 */         return null; 
/* 689 */       return new long[] { num, gen };
/*     */     }
/* 691 */     catch (Exception exception) {
/*     */ 
/*     */       
/* 694 */       return null;
/*     */     } 
/*     */   }
/*     */   public boolean isHexString() {
/* 698 */     return this.hexString;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PRTokeniser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */