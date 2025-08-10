/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfTransition
/*     */ {
/*     */   public static final int SPLITVOUT = 1;
/*     */   public static final int SPLITHOUT = 2;
/*     */   public static final int SPLITVIN = 3;
/*     */   public static final int SPLITHIN = 4;
/*     */   public static final int BLINDV = 5;
/*     */   public static final int BLINDH = 6;
/*     */   public static final int INBOX = 7;
/*     */   public static final int OUTBOX = 8;
/*     */   public static final int LRWIPE = 9;
/*     */   public static final int RLWIPE = 10;
/*     */   public static final int BTWIPE = 11;
/*     */   public static final int TBWIPE = 12;
/*     */   public static final int DISSOLVE = 13;
/*     */   public static final int LRGLITTER = 14;
/*     */   public static final int TBGLITTER = 15;
/*     */   public static final int DGLITTER = 16;
/*     */   protected int duration;
/*     */   protected int type;
/*     */   
/*     */   public PdfTransition() {
/* 126 */     this(6);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfTransition(int type) {
/* 135 */     this(type, 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfTransition(int type, int duration) {
/* 145 */     this.duration = duration;
/* 146 */     this.type = type;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDuration() {
/* 151 */     return this.duration;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getType() {
/* 156 */     return this.type;
/*     */   }
/*     */   
/*     */   public PdfDictionary getTransitionDictionary() {
/* 160 */     PdfDictionary trans = new PdfDictionary(PdfName.TRANS);
/* 161 */     switch (this.type) {
/*     */       case 1:
/* 163 */         trans.put(PdfName.S, PdfName.SPLIT);
/* 164 */         trans.put(PdfName.D, new PdfNumber(this.duration));
/* 165 */         trans.put(PdfName.DM, PdfName.V);
/* 166 */         trans.put(PdfName.M, PdfName.O);
/*     */         break;
/*     */       case 2:
/* 169 */         trans.put(PdfName.S, PdfName.SPLIT);
/* 170 */         trans.put(PdfName.D, new PdfNumber(this.duration));
/* 171 */         trans.put(PdfName.DM, PdfName.H);
/* 172 */         trans.put(PdfName.M, PdfName.O);
/*     */         break;
/*     */       case 3:
/* 175 */         trans.put(PdfName.S, PdfName.SPLIT);
/* 176 */         trans.put(PdfName.D, new PdfNumber(this.duration));
/* 177 */         trans.put(PdfName.DM, PdfName.V);
/* 178 */         trans.put(PdfName.M, PdfName.I);
/*     */         break;
/*     */       case 4:
/* 181 */         trans.put(PdfName.S, PdfName.SPLIT);
/* 182 */         trans.put(PdfName.D, new PdfNumber(this.duration));
/* 183 */         trans.put(PdfName.DM, PdfName.H);
/* 184 */         trans.put(PdfName.M, PdfName.I);
/*     */         break;
/*     */       case 5:
/* 187 */         trans.put(PdfName.S, PdfName.BLINDS);
/* 188 */         trans.put(PdfName.D, new PdfNumber(this.duration));
/* 189 */         trans.put(PdfName.DM, PdfName.V);
/*     */         break;
/*     */       case 6:
/* 192 */         trans.put(PdfName.S, PdfName.BLINDS);
/* 193 */         trans.put(PdfName.D, new PdfNumber(this.duration));
/* 194 */         trans.put(PdfName.DM, PdfName.H);
/*     */         break;
/*     */       case 7:
/* 197 */         trans.put(PdfName.S, PdfName.BOX);
/* 198 */         trans.put(PdfName.D, new PdfNumber(this.duration));
/* 199 */         trans.put(PdfName.M, PdfName.I);
/*     */         break;
/*     */       case 8:
/* 202 */         trans.put(PdfName.S, PdfName.BOX);
/* 203 */         trans.put(PdfName.D, new PdfNumber(this.duration));
/* 204 */         trans.put(PdfName.M, PdfName.O);
/*     */         break;
/*     */       case 9:
/* 207 */         trans.put(PdfName.S, PdfName.WIPE);
/* 208 */         trans.put(PdfName.D, new PdfNumber(this.duration));
/* 209 */         trans.put(PdfName.DI, new PdfNumber(0));
/*     */         break;
/*     */       case 10:
/* 212 */         trans.put(PdfName.S, PdfName.WIPE);
/* 213 */         trans.put(PdfName.D, new PdfNumber(this.duration));
/* 214 */         trans.put(PdfName.DI, new PdfNumber(180));
/*     */         break;
/*     */       case 11:
/* 217 */         trans.put(PdfName.S, PdfName.WIPE);
/* 218 */         trans.put(PdfName.D, new PdfNumber(this.duration));
/* 219 */         trans.put(PdfName.DI, new PdfNumber(90));
/*     */         break;
/*     */       case 12:
/* 222 */         trans.put(PdfName.S, PdfName.WIPE);
/* 223 */         trans.put(PdfName.D, new PdfNumber(this.duration));
/* 224 */         trans.put(PdfName.DI, new PdfNumber(270));
/*     */         break;
/*     */       case 13:
/* 227 */         trans.put(PdfName.S, PdfName.DISSOLVE);
/* 228 */         trans.put(PdfName.D, new PdfNumber(this.duration));
/*     */         break;
/*     */       case 14:
/* 231 */         trans.put(PdfName.S, PdfName.GLITTER);
/* 232 */         trans.put(PdfName.D, new PdfNumber(this.duration));
/* 233 */         trans.put(PdfName.DI, new PdfNumber(0));
/*     */         break;
/*     */       case 15:
/* 236 */         trans.put(PdfName.S, PdfName.GLITTER);
/* 237 */         trans.put(PdfName.D, new PdfNumber(this.duration));
/* 238 */         trans.put(PdfName.DI, new PdfNumber(270));
/*     */         break;
/*     */       case 16:
/* 241 */         trans.put(PdfName.S, PdfName.GLITTER);
/* 242 */         trans.put(PdfName.D, new PdfNumber(this.duration));
/* 243 */         trans.put(PdfName.DI, new PdfNumber(315));
/*     */         break;
/*     */     } 
/* 246 */     return trans;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfTransition.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */