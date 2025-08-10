/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfContentParser
/*     */ {
/*     */   public static final int COMMAND_TYPE = 200;
/*     */   private PRTokeniser tokeniser;
/*     */   
/*     */   public PdfContentParser(PRTokeniser tokeniser) {
/*  71 */     this.tokeniser = tokeniser;
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
/*     */   public ArrayList<PdfObject> parse(ArrayList<PdfObject> ls) throws IOException {
/*  84 */     if (ls == null) {
/*  85 */       ls = new ArrayList<PdfObject>();
/*     */     } else {
/*  87 */       ls.clear();
/*  88 */     }  PdfObject ob = null;
/*  89 */     while ((ob = readPRObject()) != null) {
/*  90 */       ls.add(ob);
/*  91 */       if (ob.type() == 200)
/*     */         break; 
/*     */     } 
/*  94 */     return ls;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PRTokeniser getTokeniser() {
/* 102 */     return this.tokeniser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTokeniser(PRTokeniser tokeniser) {
/* 110 */     this.tokeniser = tokeniser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfDictionary readDictionary() throws IOException {
/* 119 */     PdfDictionary dic = new PdfDictionary();
/*     */     while (true) {
/* 121 */       if (!nextValidToken())
/* 122 */         throw new IOException(MessageLocalization.getComposedMessage("unexpected.end.of.file", new Object[0])); 
/* 123 */       if (this.tokeniser.getTokenType() == PRTokeniser.TokenType.END_DIC)
/*     */         break; 
/* 125 */       if (this.tokeniser.getTokenType() == PRTokeniser.TokenType.OTHER && "def".equals(this.tokeniser.getStringValue()))
/*     */         continue; 
/* 127 */       if (this.tokeniser.getTokenType() != PRTokeniser.TokenType.NAME)
/* 128 */         throw new IOException(MessageLocalization.getComposedMessage("dictionary.key.1.is.not.a.name", new Object[] { this.tokeniser.getStringValue() })); 
/* 129 */       PdfName name = new PdfName(this.tokeniser.getStringValue(), false);
/* 130 */       PdfObject obj = readPRObject();
/* 131 */       int type = obj.type();
/* 132 */       if (-type == PRTokeniser.TokenType.END_DIC.ordinal())
/* 133 */         throw new IOException(MessageLocalization.getComposedMessage("unexpected.gt.gt", new Object[0])); 
/* 134 */       if (-type == PRTokeniser.TokenType.END_ARRAY.ordinal())
/* 135 */         throw new IOException(MessageLocalization.getComposedMessage("unexpected.close.bracket", new Object[0])); 
/* 136 */       dic.put(name, obj);
/*     */     } 
/* 138 */     return dic;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfArray readArray() throws IOException {
/* 147 */     PdfArray array = new PdfArray();
/*     */     while (true) {
/* 149 */       PdfObject obj = readPRObject();
/* 150 */       int type = obj.type();
/* 151 */       if (-type == PRTokeniser.TokenType.END_ARRAY.ordinal())
/*     */         break; 
/* 153 */       if (-type == PRTokeniser.TokenType.END_DIC.ordinal())
/* 154 */         throw new IOException(MessageLocalization.getComposedMessage("unexpected.gt.gt", new Object[0])); 
/* 155 */       array.add(obj);
/*     */     } 
/* 157 */     return array;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfObject readPRObject() throws IOException {
/*     */     PdfDictionary dic;
/*     */     PdfString str;
/* 166 */     if (!nextValidToken())
/* 167 */       return null; 
/* 168 */     PRTokeniser.TokenType type = this.tokeniser.getTokenType();
/* 169 */     switch (type) {
/*     */       case START_DIC:
/* 171 */         dic = readDictionary();
/* 172 */         return dic;
/*     */       
/*     */       case START_ARRAY:
/* 175 */         return readArray();
/*     */       case STRING:
/* 177 */         str = (new PdfString(this.tokeniser.getStringValue(), null)).setHexWriting(this.tokeniser.isHexString());
/* 178 */         return str;
/*     */       case NAME:
/* 180 */         return new PdfName(this.tokeniser.getStringValue(), false);
/*     */       case NUMBER:
/* 182 */         return new PdfNumber(this.tokeniser.getStringValue());
/*     */       case OTHER:
/* 184 */         return new PdfLiteral(200, this.tokeniser.getStringValue());
/*     */     } 
/* 186 */     return new PdfLiteral(-type.ordinal(), this.tokeniser.getStringValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean nextValidToken() throws IOException {
/* 196 */     while (this.tokeniser.nextToken()) {
/* 197 */       if (this.tokeniser.getTokenType() == PRTokeniser.TokenType.COMMENT)
/*     */         continue; 
/* 199 */       return true;
/*     */     } 
/* 201 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfContentParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */