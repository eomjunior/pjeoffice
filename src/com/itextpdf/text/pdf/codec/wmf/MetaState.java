/*     */ package com.itextpdf.text.pdf.codec.wmf;
/*     */ 
/*     */ import com.itextpdf.text.BaseColor;
/*     */ import com.itextpdf.text.pdf.PdfContentByte;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Stack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MetaState
/*     */ {
/*     */   public static final int TA_NOUPDATECP = 0;
/*     */   public static final int TA_UPDATECP = 1;
/*     */   public static final int TA_LEFT = 0;
/*     */   public static final int TA_RIGHT = 2;
/*     */   public static final int TA_CENTER = 6;
/*     */   public static final int TA_TOP = 0;
/*     */   public static final int TA_BOTTOM = 8;
/*     */   public static final int TA_BASELINE = 24;
/*     */   public static final int TRANSPARENT = 1;
/*     */   public static final int OPAQUE = 2;
/*     */   public static final int ALTERNATE = 1;
/*     */   public static final int WINDING = 2;
/*     */   public Stack<MetaState> savedStates;
/*     */   public ArrayList<MetaObject> MetaObjects;
/*     */   public Point currentPoint;
/*     */   public MetaPen currentPen;
/*     */   public MetaBrush currentBrush;
/*     */   public MetaFont currentFont;
/*  75 */   public BaseColor currentBackgroundColor = BaseColor.WHITE;
/*  76 */   public BaseColor currentTextColor = BaseColor.BLACK;
/*  77 */   public int backgroundMode = 2;
/*  78 */   public int polyFillMode = 1;
/*  79 */   public int lineJoin = 1;
/*     */   
/*     */   public int textAlign;
/*     */   
/*     */   public int offsetWx;
/*     */   public int offsetWy;
/*     */   public int extentWx;
/*     */   public int extentWy;
/*     */   public float scalingX;
/*     */   public float scalingY;
/*     */   
/*     */   public MetaState() {
/*  91 */     this.savedStates = new Stack<MetaState>();
/*  92 */     this.MetaObjects = new ArrayList<MetaObject>();
/*  93 */     this.currentPoint = new Point(0, 0);
/*  94 */     this.currentPen = new MetaPen();
/*  95 */     this.currentBrush = new MetaBrush();
/*  96 */     this.currentFont = new MetaFont();
/*     */   }
/*     */   
/*     */   public MetaState(MetaState state) {
/* 100 */     setMetaState(state);
/*     */   }
/*     */   
/*     */   public void setMetaState(MetaState state) {
/* 104 */     this.savedStates = state.savedStates;
/* 105 */     this.MetaObjects = state.MetaObjects;
/* 106 */     this.currentPoint = state.currentPoint;
/* 107 */     this.currentPen = state.currentPen;
/* 108 */     this.currentBrush = state.currentBrush;
/* 109 */     this.currentFont = state.currentFont;
/* 110 */     this.currentBackgroundColor = state.currentBackgroundColor;
/* 111 */     this.currentTextColor = state.currentTextColor;
/* 112 */     this.backgroundMode = state.backgroundMode;
/* 113 */     this.polyFillMode = state.polyFillMode;
/* 114 */     this.textAlign = state.textAlign;
/* 115 */     this.lineJoin = state.lineJoin;
/* 116 */     this.offsetWx = state.offsetWx;
/* 117 */     this.offsetWy = state.offsetWy;
/* 118 */     this.extentWx = state.extentWx;
/* 119 */     this.extentWy = state.extentWy;
/* 120 */     this.scalingX = state.scalingX;
/* 121 */     this.scalingY = state.scalingY;
/*     */   }
/*     */   
/*     */   public void addMetaObject(MetaObject object) {
/* 125 */     for (int k = 0; k < this.MetaObjects.size(); k++) {
/* 126 */       if (this.MetaObjects.get(k) == null) {
/* 127 */         this.MetaObjects.set(k, object);
/*     */         return;
/*     */       } 
/*     */     } 
/* 131 */     this.MetaObjects.add(object);
/*     */   }
/*     */   public void selectMetaObject(int index, PdfContentByte cb) {
/*     */     int style;
/* 135 */     MetaObject obj = this.MetaObjects.get(index);
/* 136 */     if (obj == null) {
/*     */       return;
/*     */     }
/* 139 */     switch (obj.getType()) {
/*     */       case 2:
/* 141 */         this.currentBrush = (MetaBrush)obj;
/* 142 */         style = this.currentBrush.getStyle();
/* 143 */         if (style == 0) {
/* 144 */           BaseColor color = this.currentBrush.getColor();
/* 145 */           cb.setColorFill(color); break;
/*     */         } 
/* 147 */         if (style == 2) {
/* 148 */           BaseColor color = this.currentBackgroundColor;
/* 149 */           cb.setColorFill(color);
/*     */         } 
/*     */         break;
/*     */       
/*     */       case 1:
/* 154 */         this.currentPen = (MetaPen)obj;
/* 155 */         style = this.currentPen.getStyle();
/* 156 */         if (style != 5) {
/* 157 */           BaseColor color = this.currentPen.getColor();
/* 158 */           cb.setColorStroke(color);
/* 159 */           cb.setLineWidth(Math.abs(this.currentPen.getPenWidth() * this.scalingX / this.extentWx));
/* 160 */           switch (style) {
/*     */             case 1:
/* 162 */               cb.setLineDash(18.0F, 6.0F, 0.0F);
/*     */               break;
/*     */             case 3:
/* 165 */               cb.setLiteral("[9 6 3 6]0 d\n");
/*     */               break;
/*     */             case 4:
/* 168 */               cb.setLiteral("[9 3 3 3 3 3]0 d\n");
/*     */               break;
/*     */             case 2:
/* 171 */               cb.setLineDash(3.0F, 0.0F);
/*     */               break;
/*     */           } 
/* 174 */           cb.setLineDash(0.0F);
/*     */         } 
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 3:
/* 182 */         this.currentFont = (MetaFont)obj;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void deleteMetaObject(int index) {
/* 189 */     this.MetaObjects.set(index, null);
/*     */   }
/*     */   
/*     */   public void saveState(PdfContentByte cb) {
/* 193 */     cb.saveState();
/* 194 */     MetaState state = new MetaState(this);
/* 195 */     this.savedStates.push(state);
/*     */   }
/*     */   
/*     */   public void restoreState(int index, PdfContentByte cb) {
/*     */     int pops;
/* 200 */     if (index < 0) {
/* 201 */       pops = Math.min(-index, this.savedStates.size());
/*     */     } else {
/* 203 */       pops = Math.max(this.savedStates.size() - index, 0);
/* 204 */     }  if (pops == 0)
/*     */       return; 
/* 206 */     MetaState state = null;
/* 207 */     while (pops-- != 0) {
/* 208 */       cb.restoreState();
/* 209 */       state = this.savedStates.pop();
/*     */     } 
/* 211 */     setMetaState(state);
/*     */   }
/*     */   
/*     */   public void cleanup(PdfContentByte cb) {
/* 215 */     int k = this.savedStates.size();
/* 216 */     while (k-- > 0)
/* 217 */       cb.restoreState(); 
/*     */   }
/*     */   
/*     */   public float transformX(int x) {
/* 221 */     return (x - this.offsetWx) * this.scalingX / this.extentWx;
/*     */   }
/*     */   
/*     */   public float transformY(int y) {
/* 225 */     return (1.0F - (y - this.offsetWy) / this.extentWy) * this.scalingY;
/*     */   }
/*     */   
/*     */   public void setScalingX(float scalingX) {
/* 229 */     this.scalingX = scalingX;
/*     */   }
/*     */   
/*     */   public void setScalingY(float scalingY) {
/* 233 */     this.scalingY = scalingY;
/*     */   }
/*     */   
/*     */   public void setOffsetWx(int offsetWx) {
/* 237 */     this.offsetWx = offsetWx;
/*     */   }
/*     */   
/*     */   public void setOffsetWy(int offsetWy) {
/* 241 */     this.offsetWy = offsetWy;
/*     */   }
/*     */   
/*     */   public void setExtentWx(int extentWx) {
/* 245 */     this.extentWx = extentWx;
/*     */   }
/*     */   
/*     */   public void setExtentWy(int extentWy) {
/* 249 */     this.extentWy = extentWy;
/*     */   }
/*     */   
/*     */   public float transformAngle(float angle) {
/* 253 */     float ta = (this.scalingY < 0.0F) ? -angle : angle;
/* 254 */     return (float)((this.scalingX < 0.0F) ? (Math.PI - ta) : ta);
/*     */   }
/*     */   
/*     */   public void setCurrentPoint(Point p) {
/* 258 */     this.currentPoint = p;
/*     */   }
/*     */   
/*     */   public Point getCurrentPoint() {
/* 262 */     return this.currentPoint;
/*     */   }
/*     */   
/*     */   public MetaBrush getCurrentBrush() {
/* 266 */     return this.currentBrush;
/*     */   }
/*     */   
/*     */   public MetaPen getCurrentPen() {
/* 270 */     return this.currentPen;
/*     */   }
/*     */   
/*     */   public MetaFont getCurrentFont() {
/* 274 */     return this.currentFont;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseColor getCurrentBackgroundColor() {
/* 281 */     return this.currentBackgroundColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCurrentBackgroundColor(BaseColor currentBackgroundColor) {
/* 288 */     this.currentBackgroundColor = currentBackgroundColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseColor getCurrentTextColor() {
/* 295 */     return this.currentTextColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCurrentTextColor(BaseColor currentTextColor) {
/* 302 */     this.currentTextColor = currentTextColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBackgroundMode() {
/* 309 */     return this.backgroundMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBackgroundMode(int backgroundMode) {
/* 316 */     this.backgroundMode = backgroundMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTextAlign() {
/* 323 */     return this.textAlign;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTextAlign(int textAlign) {
/* 330 */     this.textAlign = textAlign;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPolyFillMode() {
/* 337 */     return this.polyFillMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPolyFillMode(int polyFillMode) {
/* 344 */     this.polyFillMode = polyFillMode;
/*     */   }
/*     */   
/*     */   public void setLineJoinRectangle(PdfContentByte cb) {
/* 348 */     if (this.lineJoin != 0) {
/* 349 */       this.lineJoin = 0;
/* 350 */       cb.setLineJoin(0);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setLineJoinPolygon(PdfContentByte cb) {
/* 355 */     if (this.lineJoin == 0) {
/* 356 */       this.lineJoin = 1;
/* 357 */       cb.setLineJoin(1);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean getLineNeutral() {
/* 362 */     return (this.lineJoin == 0);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/codec/wmf/MetaState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */