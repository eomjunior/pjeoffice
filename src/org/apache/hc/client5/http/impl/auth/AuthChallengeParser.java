/*     */ package org.apache.hc.client5.http.impl.auth;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.BitSet;
/*     */ import java.util.List;
/*     */ import org.apache.hc.client5.http.auth.AuthChallenge;
/*     */ import org.apache.hc.client5.http.auth.ChallengeType;
/*     */ import org.apache.hc.core5.http.NameValuePair;
/*     */ import org.apache.hc.core5.http.ParseException;
/*     */ import org.apache.hc.core5.http.message.BasicNameValuePair;
/*     */ import org.apache.hc.core5.http.message.ParserCursor;
/*     */ import org.apache.hc.core5.util.TextUtils;
/*     */ import org.apache.hc.core5.util.Tokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AuthChallengeParser
/*     */ {
/*  50 */   public static final AuthChallengeParser INSTANCE = new AuthChallengeParser();
/*     */   
/*  52 */   private final Tokenizer tokenParser = Tokenizer.INSTANCE;
/*     */   
/*     */   private static final char BLANK = ' ';
/*     */   
/*     */   private static final char COMMA_CHAR = ',';
/*     */   
/*     */   private static final char EQUAL_CHAR = '=';
/*     */   
/*  60 */   private static final BitSet TERMINATORS = Tokenizer.INIT_BITSET(new int[] { 32, 61, 44 });
/*  61 */   private static final BitSet DELIMITER = Tokenizer.INIT_BITSET(new int[] { 44 });
/*  62 */   private static final BitSet SPACE = Tokenizer.INIT_BITSET(new int[] { 32 });
/*     */   
/*     */   static class ChallengeInt
/*     */   {
/*     */     final String schemeName;
/*     */     final List<NameValuePair> params;
/*     */     
/*     */     ChallengeInt(String schemeName) {
/*  70 */       this.schemeName = schemeName;
/*  71 */       this.params = new ArrayList<>();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  76 */       return "ChallengeInternal{schemeName='" + this.schemeName + '\'' + ", params=" + this.params + '}';
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
/*     */   public List<AuthChallenge> parse(ChallengeType challengeType, CharSequence buffer, ParserCursor cursor) throws ParseException {
/*  94 */     this.tokenParser.skipWhiteSpace(buffer, (Tokenizer.Cursor)cursor);
/*  95 */     if (cursor.atEnd()) {
/*  96 */       throw new ParseException("Malformed auth challenge");
/*     */     }
/*  98 */     List<ChallengeInt> internalChallenges = new ArrayList<>();
/*  99 */     String schemeName = this.tokenParser.parseToken(buffer, (Tokenizer.Cursor)cursor, SPACE);
/* 100 */     if (TextUtils.isBlank(schemeName)) {
/* 101 */       throw new ParseException("Malformed auth challenge");
/*     */     }
/* 103 */     ChallengeInt current = new ChallengeInt(schemeName);
/* 104 */     while (current != null) {
/* 105 */       internalChallenges.add(current);
/* 106 */       current = parseChallenge(buffer, cursor, current);
/*     */     } 
/* 108 */     List<AuthChallenge> challenges = new ArrayList<>(internalChallenges.size());
/* 109 */     for (ChallengeInt internal : internalChallenges) {
/* 110 */       List<NameValuePair> params = internal.params;
/* 111 */       String token68 = null;
/* 112 */       if (params.size() == 1) {
/* 113 */         NameValuePair param = params.get(0);
/* 114 */         if (param.getValue() == null) {
/* 115 */           token68 = param.getName();
/* 116 */           params.clear();
/*     */         } 
/*     */       } 
/* 119 */       challenges.add(new AuthChallenge(challengeType, internal.schemeName, token68, 
/* 120 */             !params.isEmpty() ? params : null));
/*     */     } 
/* 122 */     return challenges;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ChallengeInt parseChallenge(CharSequence buffer, ParserCursor cursor, ChallengeInt currentChallenge) throws ParseException {
/*     */     String token;
/*     */     while (true) {
/* 130 */       this.tokenParser.skipWhiteSpace(buffer, (Tokenizer.Cursor)cursor);
/* 131 */       if (cursor.atEnd()) {
/* 132 */         return null;
/*     */       }
/* 134 */       token = parseToken(buffer, cursor);
/* 135 */       if (TextUtils.isBlank(token)) {
/* 136 */         throw new ParseException("Malformed auth challenge");
/*     */       }
/* 138 */       this.tokenParser.skipWhiteSpace(buffer, (Tokenizer.Cursor)cursor);
/*     */ 
/*     */       
/* 141 */       if (cursor.atEnd()) {
/*     */         
/* 143 */         currentChallenge.params.add(new BasicNameValuePair(token, null)); continue;
/*     */       } 
/* 145 */       char ch = buffer.charAt(cursor.getPos());
/* 146 */       if (ch == '=') {
/* 147 */         cursor.updatePos(cursor.getPos() + 1);
/* 148 */         String value = this.tokenParser.parseValue(buffer, (Tokenizer.Cursor)cursor, DELIMITER);
/* 149 */         this.tokenParser.skipWhiteSpace(buffer, (Tokenizer.Cursor)cursor);
/* 150 */         if (!cursor.atEnd()) {
/* 151 */           ch = buffer.charAt(cursor.getPos());
/* 152 */           if (ch == ',') {
/* 153 */             cursor.updatePos(cursor.getPos() + 1);
/*     */           }
/*     */         } 
/* 156 */         currentChallenge.params.add(new BasicNameValuePair(token, value)); continue;
/* 157 */       }  if (ch == ',') {
/* 158 */         cursor.updatePos(cursor.getPos() + 1);
/* 159 */         currentChallenge.params.add(new BasicNameValuePair(token, null)); continue;
/*     */       }  break;
/*     */     } 
/* 162 */     if (currentChallenge.params.isEmpty()) {
/* 163 */       throw new ParseException("Malformed auth challenge");
/*     */     }
/* 165 */     return new ChallengeInt(token);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String parseToken(CharSequence buf, ParserCursor cursor) {
/* 172 */     StringBuilder dst = new StringBuilder();
/* 173 */     label21: while (!cursor.atEnd()) {
/* 174 */       int pos = cursor.getPos();
/* 175 */       char current = buf.charAt(pos);
/* 176 */       if (TERMINATORS.get(current)) {
/*     */         
/* 178 */         if (current == '=') {
/*     */ 
/*     */           
/* 181 */           if (pos + 1 < cursor.getUpperBound() && buf.charAt(pos + 1) != '=') {
/*     */             break;
/*     */           }
/*     */           while (true) {
/* 185 */             dst.append(current);
/* 186 */             pos++;
/* 187 */             cursor.updatePos(pos);
/* 188 */             if (cursor.atEnd()) {
/*     */               continue label21;
/*     */             }
/* 191 */             current = buf.charAt(pos);
/* 192 */             if (current != '=')
/*     */               continue label21; 
/*     */           } 
/*     */         }  break;
/*     */       } 
/* 197 */       dst.append(current);
/* 198 */       cursor.updatePos(pos + 1);
/*     */     } 
/*     */     
/* 201 */     return dst.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/auth/AuthChallengeParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */