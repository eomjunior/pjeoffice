/*     */ package com.itextpdf.xmp.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PushbackReader;
/*     */ import java.io.Reader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FixASCIIControlsReader
/*     */   extends PushbackReader
/*     */ {
/*     */   private static final int STATE_START = 0;
/*     */   private static final int STATE_AMP = 1;
/*     */   private static final int STATE_HASH = 2;
/*     */   private static final int STATE_HEX = 3;
/*     */   private static final int STATE_DIG1 = 4;
/*     */   private static final int STATE_ERROR = 5;
/*     */   private static final int BUFFER_SIZE = 8;
/*  58 */   private int state = 0;
/*     */   
/*  60 */   private int control = 0;
/*     */   
/*  62 */   private int digits = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FixASCIIControlsReader(Reader in) {
/*  71 */     super(in, 8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(char[] cbuf, int off, int len) throws IOException {
/*  80 */     int readAhead = 0;
/*  81 */     int read = 0;
/*  82 */     int pos = off;
/*  83 */     char[] readAheadBuffer = new char[8];
/*     */     
/*  85 */     boolean available = true;
/*  86 */     while (available && read < len) {
/*     */       
/*  88 */       available = (super.read(readAheadBuffer, readAhead, 1) == 1);
/*  89 */       if (available) {
/*     */         
/*  91 */         char c = processChar(readAheadBuffer[readAhead]);
/*  92 */         if (this.state == 0) {
/*     */ 
/*     */           
/*  95 */           if (Utils.isControlChar(c))
/*     */           {
/*  97 */             c = ' ';
/*     */           }
/*  99 */           cbuf[pos++] = c;
/* 100 */           readAhead = 0;
/* 101 */           read++; continue;
/*     */         } 
/* 103 */         if (this.state == 5) {
/*     */           
/* 105 */           unread(readAheadBuffer, 0, readAhead + 1);
/* 106 */           readAhead = 0;
/*     */           
/*     */           continue;
/*     */         } 
/* 110 */         readAhead++;
/*     */         continue;
/*     */       } 
/* 113 */       if (readAhead > 0) {
/*     */ 
/*     */         
/* 116 */         unread(readAheadBuffer, 0, readAhead);
/* 117 */         this.state = 5;
/* 118 */         readAhead = 0;
/* 119 */         available = true;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 124 */     return (read > 0 || available) ? read : -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private char processChar(char ch) {
/* 135 */     switch (this.state) {
/*     */       
/*     */       case 0:
/* 138 */         if (ch == '&')
/*     */         {
/* 140 */           this.state = 1;
/*     */         }
/* 142 */         return ch;
/*     */       
/*     */       case 1:
/* 145 */         if (ch == '#') {
/*     */           
/* 147 */           this.state = 2;
/*     */         }
/*     */         else {
/*     */           
/* 151 */           this.state = 5;
/*     */         } 
/* 153 */         return ch;
/*     */       
/*     */       case 2:
/* 156 */         if (ch == 'x') {
/*     */           
/* 158 */           this.control = 0;
/* 159 */           this.digits = 0;
/* 160 */           this.state = 3;
/*     */         }
/* 162 */         else if ('0' <= ch && ch <= '9') {
/*     */           
/* 164 */           this.control = Character.digit(ch, 10);
/* 165 */           this.digits = 1;
/* 166 */           this.state = 4;
/*     */         }
/*     */         else {
/*     */           
/* 170 */           this.state = 5;
/*     */         } 
/* 172 */         return ch;
/*     */       
/*     */       case 4:
/* 175 */         if ('0' <= ch && ch <= '9') {
/*     */           
/* 177 */           this.control = this.control * 10 + Character.digit(ch, 10);
/* 178 */           this.digits++;
/* 179 */           if (this.digits <= 5) {
/*     */             
/* 181 */             this.state = 4;
/*     */           }
/*     */           else {
/*     */             
/* 185 */             this.state = 5;
/*     */           } 
/*     */         } else {
/* 188 */           if (ch == ';' && Utils.isControlChar((char)this.control)) {
/*     */             
/* 190 */             this.state = 0;
/* 191 */             return (char)this.control;
/*     */           } 
/*     */ 
/*     */           
/* 195 */           this.state = 5;
/*     */         } 
/* 197 */         return ch;
/*     */       
/*     */       case 3:
/* 200 */         if (('0' <= ch && ch <= '9') || ('a' <= ch && ch <= 'f') || ('A' <= ch && ch <= 'F')) {
/*     */ 
/*     */ 
/*     */           
/* 204 */           this.control = this.control * 16 + Character.digit(ch, 16);
/* 205 */           this.digits++;
/* 206 */           if (this.digits <= 4) {
/*     */             
/* 208 */             this.state = 3;
/*     */           }
/*     */           else {
/*     */             
/* 212 */             this.state = 5;
/*     */           } 
/*     */         } else {
/* 215 */           if (ch == ';' && Utils.isControlChar((char)this.control)) {
/*     */             
/* 217 */             this.state = 0;
/* 218 */             return (char)this.control;
/*     */           } 
/*     */ 
/*     */           
/* 222 */           this.state = 5;
/*     */         } 
/* 224 */         return ch;
/*     */       
/*     */       case 5:
/* 227 */         this.state = 0;
/* 228 */         return ch;
/*     */     } 
/*     */ 
/*     */     
/* 232 */     return ch;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/xmp/impl/FixASCIIControlsReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */