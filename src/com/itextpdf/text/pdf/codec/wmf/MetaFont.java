/*     */ package com.itextpdf.text.pdf.codec.wmf;
/*     */ 
/*     */ import com.itextpdf.text.Document;
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.Font;
/*     */ import com.itextpdf.text.FontFactory;
/*     */ import com.itextpdf.text.pdf.BaseFont;
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MetaFont
/*     */   extends MetaObject
/*     */ {
/*  55 */   static final String[] fontNames = new String[] { "Courier", "Courier-Bold", "Courier-Oblique", "Courier-BoldOblique", "Helvetica", "Helvetica-Bold", "Helvetica-Oblique", "Helvetica-BoldOblique", "Times-Roman", "Times-Bold", "Times-Italic", "Times-BoldItalic", "Symbol", "ZapfDingbats" };
/*     */   
/*     */   static final int MARKER_BOLD = 1;
/*     */   
/*     */   static final int MARKER_ITALIC = 2;
/*     */   
/*     */   static final int MARKER_COURIER = 0;
/*     */   
/*     */   static final int MARKER_HELVETICA = 4;
/*     */   
/*     */   static final int MARKER_TIMES = 8;
/*     */   
/*     */   static final int MARKER_SYMBOL = 12;
/*     */   
/*     */   static final int DEFAULT_PITCH = 0;
/*     */   static final int FIXED_PITCH = 1;
/*     */   static final int VARIABLE_PITCH = 2;
/*     */   static final int FF_DONTCARE = 0;
/*     */   static final int FF_ROMAN = 1;
/*     */   static final int FF_SWISS = 2;
/*     */   static final int FF_MODERN = 3;
/*     */   static final int FF_SCRIPT = 4;
/*     */   static final int FF_DECORATIVE = 5;
/*     */   static final int BOLDTHRESHOLD = 600;
/*     */   static final int nameSize = 32;
/*     */   static final int ETO_OPAQUE = 2;
/*     */   static final int ETO_CLIPPED = 4;
/*     */   int height;
/*     */   float angle;
/*     */   int bold;
/*     */   int italic;
/*     */   boolean underline;
/*     */   boolean strikeout;
/*     */   int charset;
/*     */   int pitchAndFamily;
/*  90 */   String faceName = "arial";
/*  91 */   BaseFont font = null;
/*     */   
/*     */   public MetaFont() {
/*  94 */     this.type = 3;
/*     */   }
/*     */   
/*     */   public void init(InputMeta in) throws IOException {
/*  98 */     this.height = Math.abs(in.readShort());
/*  99 */     in.skip(2);
/* 100 */     this.angle = (float)(in.readShort() / 1800.0D * Math.PI);
/* 101 */     in.skip(2);
/* 102 */     this.bold = (in.readShort() >= 600) ? 1 : 0;
/* 103 */     this.italic = (in.readByte() != 0) ? 2 : 0;
/* 104 */     this.underline = (in.readByte() != 0);
/* 105 */     this.strikeout = (in.readByte() != 0);
/* 106 */     this.charset = in.readByte();
/* 107 */     in.skip(3);
/* 108 */     this.pitchAndFamily = in.readByte();
/* 109 */     byte[] name = new byte[32];
/*     */     int k;
/* 111 */     for (k = 0; k < 32; k++) {
/* 112 */       int c = in.readByte();
/* 113 */       if (c == 0) {
/*     */         break;
/*     */       }
/* 116 */       name[k] = (byte)c;
/*     */     } 
/*     */     try {
/* 119 */       this.faceName = new String(name, 0, k, "Cp1252");
/*     */     }
/* 121 */     catch (UnsupportedEncodingException e) {
/* 122 */       this.faceName = new String(name, 0, k);
/*     */     } 
/* 124 */     this.faceName = this.faceName.toLowerCase();
/*     */   }
/*     */   public BaseFont getFont() {
/*     */     String fontName;
/* 128 */     if (this.font != null)
/* 129 */       return this.font; 
/* 130 */     Font ff2 = FontFactory.getFont(this.faceName, "Cp1252", true, 10.0F, ((this.italic != 0) ? 2 : 0) | ((this.bold != 0) ? 1 : 0));
/* 131 */     this.font = ff2.getBaseFont();
/* 132 */     if (this.font != null) {
/* 133 */       return this.font;
/*     */     }
/* 135 */     if (this.faceName.indexOf("courier") != -1 || this.faceName.indexOf("terminal") != -1 || this.faceName
/* 136 */       .indexOf("fixedsys") != -1) {
/* 137 */       fontName = fontNames[0 + this.italic + this.bold];
/*     */     }
/* 139 */     else if (this.faceName.indexOf("ms sans serif") != -1 || this.faceName.indexOf("arial") != -1 || this.faceName
/* 140 */       .indexOf("system") != -1) {
/* 141 */       fontName = fontNames[4 + this.italic + this.bold];
/*     */     }
/* 143 */     else if (this.faceName.indexOf("arial black") != -1) {
/* 144 */       fontName = fontNames[4 + this.italic + 1];
/*     */     }
/* 146 */     else if (this.faceName.indexOf("times") != -1 || this.faceName.indexOf("ms serif") != -1 || this.faceName
/* 147 */       .indexOf("roman") != -1) {
/* 148 */       fontName = fontNames[8 + this.italic + this.bold];
/*     */     }
/* 150 */     else if (this.faceName.indexOf("symbol") != -1) {
/* 151 */       fontName = fontNames[12];
/*     */     } else {
/*     */       
/* 154 */       int pitch = this.pitchAndFamily & 0x3;
/* 155 */       int family = this.pitchAndFamily >> 4 & 0x7;
/* 156 */       switch (family) {
/*     */         case 3:
/* 158 */           fontName = fontNames[0 + this.italic + this.bold];
/*     */           break;
/*     */         case 1:
/* 161 */           fontName = fontNames[8 + this.italic + this.bold];
/*     */           break;
/*     */         case 2:
/*     */         case 4:
/*     */         case 5:
/* 166 */           fontName = fontNames[4 + this.italic + this.bold];
/*     */           break;
/*     */         
/*     */         default:
/* 170 */           switch (pitch) {
/*     */             case 1:
/* 172 */               fontName = fontNames[0 + this.italic + this.bold];
/*     */               break;
/*     */           } 
/* 175 */           fontName = fontNames[4 + this.italic + this.bold];
/*     */           break;
/*     */       } 
/*     */ 
/*     */     
/*     */     } 
/*     */     try {
/* 182 */       this.font = BaseFont.createFont(fontName, "Cp1252", false);
/*     */     }
/* 184 */     catch (Exception e) {
/* 185 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */     
/* 188 */     return this.font;
/*     */   }
/*     */   
/*     */   public float getAngle() {
/* 192 */     return this.angle;
/*     */   }
/*     */   
/*     */   public boolean isUnderline() {
/* 196 */     return this.underline;
/*     */   }
/*     */   
/*     */   public boolean isStrikeout() {
/* 200 */     return this.strikeout;
/*     */   }
/*     */   
/*     */   public float getFontSize(MetaState state) {
/* 204 */     return Math.abs(state.transformY(this.height) - state.transformY(0)) * Document.wmfFontCorrection;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/codec/wmf/MetaFont.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */