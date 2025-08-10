/*     */ package org.apache.tools.ant.filters;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import org.apache.tools.ant.util.UnicodeUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EscapeUnicode
/*     */   extends BaseParamFilterReader
/*     */   implements ChainableReader
/*     */ {
/*     */   private StringBuffer unicodeBuf;
/*     */   
/*     */   public EscapeUnicode() {
/*  54 */     this.unicodeBuf = new StringBuffer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EscapeUnicode(Reader in) {
/*  64 */     super(in);
/*  65 */     this.unicodeBuf = new StringBuffer();
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
/*     */   public final int read() throws IOException {
/*  79 */     if (!getInitialized()) {
/*  80 */       initialize();
/*  81 */       setInitialized(true);
/*     */     } 
/*     */     
/*  84 */     int ch = -1;
/*  85 */     if (this.unicodeBuf.length() > 0) {
/*  86 */       ch = this.unicodeBuf.charAt(0);
/*  87 */       this.unicodeBuf.deleteCharAt(0);
/*     */     } else {
/*  89 */       ch = this.in.read();
/*  90 */       if (ch != -1) {
/*  91 */         char achar = (char)ch;
/*  92 */         if (achar >= '') {
/*  93 */           this.unicodeBuf = UnicodeUtil.EscapeUnicode(achar);
/*  94 */           ch = 92;
/*     */         } 
/*     */       } 
/*     */     } 
/*  98 */     return ch;
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
/*     */   public final Reader chain(Reader rdr) {
/* 112 */     EscapeUnicode newFilter = new EscapeUnicode(rdr);
/* 113 */     newFilter.setInitialized(true);
/* 114 */     return newFilter;
/*     */   }
/*     */   
/*     */   private void initialize() {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/filters/EscapeUnicode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */