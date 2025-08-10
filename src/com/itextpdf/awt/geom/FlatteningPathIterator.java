/*     */ package com.itextpdf.awt.geom;
/*     */ 
/*     */ import com.itextpdf.awt.geom.misc.Messages;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatteningPathIterator
/*     */   implements PathIterator
/*     */ {
/*     */   private static final int BUFFER_SIZE = 16;
/*     */   private static final int BUFFER_LIMIT = 16;
/*     */   private static final int BUFFER_CAPACITY = 16;
/*     */   int bufType;
/*     */   int bufLimit;
/*     */   int bufSize;
/*     */   int bufIndex;
/*     */   int bufSubdiv;
/*     */   double[] buf;
/*     */   boolean bufEmpty = true;
/*     */   PathIterator p;
/*     */   double flatness;
/*     */   double flatness2;
/*     */   double px;
/*     */   double py;
/* 110 */   double[] coords = new double[6];
/*     */   
/*     */   public FlatteningPathIterator(PathIterator path, double flatness) {
/* 113 */     this(path, flatness, 16);
/*     */   }
/*     */   
/*     */   public FlatteningPathIterator(PathIterator path, double flatness, int limit) {
/* 117 */     if (flatness < 0.0D)
/*     */     {
/* 119 */       throw new IllegalArgumentException(Messages.getString("awt.206"));
/*     */     }
/* 121 */     if (limit < 0)
/*     */     {
/* 123 */       throw new IllegalArgumentException(Messages.getString("awt.207"));
/*     */     }
/* 125 */     if (path == null)
/*     */     {
/* 127 */       throw new NullPointerException(Messages.getString("awt.208"));
/*     */     }
/* 129 */     this.p = path;
/* 130 */     this.flatness = flatness;
/* 131 */     this.flatness2 = flatness * flatness;
/* 132 */     this.bufLimit = limit;
/* 133 */     this.bufSize = Math.min(this.bufLimit, 16);
/* 134 */     this.buf = new double[this.bufSize];
/* 135 */     this.bufIndex = this.bufSize;
/*     */   }
/*     */   
/*     */   public double getFlatness() {
/* 139 */     return this.flatness;
/*     */   }
/*     */   
/*     */   public int getRecursionLimit() {
/* 143 */     return this.bufLimit;
/*     */   }
/*     */   
/*     */   public int getWindingRule() {
/* 147 */     return this.p.getWindingRule();
/*     */   }
/*     */   
/*     */   public boolean isDone() {
/* 151 */     return (this.bufEmpty && this.p.isDone());
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
/*     */   void evaluate() {
/* 163 */     if (this.bufEmpty) {
/* 164 */       this.bufType = this.p.currentSegment(this.coords);
/*     */     }
/*     */     
/* 167 */     switch (this.bufType) {
/*     */       case 0:
/*     */       case 1:
/* 170 */         this.px = this.coords[0];
/* 171 */         this.py = this.coords[1];
/*     */         break;
/*     */       case 2:
/* 174 */         if (this.bufEmpty) {
/* 175 */           this.bufIndex -= 6;
/* 176 */           this.buf[this.bufIndex + 0] = this.px;
/* 177 */           this.buf[this.bufIndex + 1] = this.py;
/* 178 */           System.arraycopy(this.coords, 0, this.buf, this.bufIndex + 2, 4);
/* 179 */           this.bufSubdiv = 0;
/*     */         } 
/*     */         
/* 182 */         while (this.bufSubdiv < this.bufLimit && 
/* 183 */           QuadCurve2D.getFlatnessSq(this.buf, this.bufIndex) >= this.flatness2) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 188 */           if (this.bufIndex <= 4) {
/* 189 */             double[] tmp = new double[this.bufSize + 16];
/* 190 */             System.arraycopy(this.buf, this.bufIndex, tmp, this.bufIndex + 16, this.bufSize - this.bufIndex);
/*     */ 
/*     */ 
/*     */             
/* 194 */             this.buf = tmp;
/* 195 */             this.bufSize += 16;
/* 196 */             this.bufIndex += 16;
/*     */           } 
/*     */           
/* 199 */           QuadCurve2D.subdivide(this.buf, this.bufIndex, this.buf, this.bufIndex - 4, this.buf, this.bufIndex);
/*     */           
/* 201 */           this.bufIndex -= 4;
/* 202 */           this.bufSubdiv++;
/*     */         } 
/*     */         
/* 205 */         this.bufIndex += 4;
/* 206 */         this.px = this.buf[this.bufIndex];
/* 207 */         this.py = this.buf[this.bufIndex + 1];
/*     */         
/* 209 */         this.bufEmpty = (this.bufIndex == this.bufSize - 2);
/* 210 */         if (this.bufEmpty) {
/* 211 */           this.bufIndex = this.bufSize;
/* 212 */           this.bufType = 1;
/*     */         } 
/*     */         break;
/*     */       case 3:
/* 216 */         if (this.bufEmpty) {
/* 217 */           this.bufIndex -= 8;
/* 218 */           this.buf[this.bufIndex + 0] = this.px;
/* 219 */           this.buf[this.bufIndex + 1] = this.py;
/* 220 */           System.arraycopy(this.coords, 0, this.buf, this.bufIndex + 2, 6);
/* 221 */           this.bufSubdiv = 0;
/*     */         } 
/*     */         
/* 224 */         while (this.bufSubdiv < this.bufLimit && 
/* 225 */           CubicCurve2D.getFlatnessSq(this.buf, this.bufIndex) >= this.flatness2) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 230 */           if (this.bufIndex <= 6) {
/* 231 */             double[] tmp = new double[this.bufSize + 16];
/* 232 */             System.arraycopy(this.buf, this.bufIndex, tmp, this.bufIndex + 16, this.bufSize - this.bufIndex);
/*     */ 
/*     */ 
/*     */             
/* 236 */             this.buf = tmp;
/* 237 */             this.bufSize += 16;
/* 238 */             this.bufIndex += 16;
/*     */           } 
/*     */           
/* 241 */           CubicCurve2D.subdivide(this.buf, this.bufIndex, this.buf, this.bufIndex - 6, this.buf, this.bufIndex);
/*     */           
/* 243 */           this.bufIndex -= 6;
/* 244 */           this.bufSubdiv++;
/*     */         } 
/*     */         
/* 247 */         this.bufIndex += 6;
/* 248 */         this.px = this.buf[this.bufIndex];
/* 249 */         this.py = this.buf[this.bufIndex + 1];
/*     */         
/* 251 */         this.bufEmpty = (this.bufIndex == this.bufSize - 2);
/* 252 */         if (this.bufEmpty) {
/* 253 */           this.bufIndex = this.bufSize;
/* 254 */           this.bufType = 1;
/*     */         } 
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void next() {
/* 262 */     if (this.bufEmpty) {
/* 263 */       this.p.next();
/*     */     }
/*     */   }
/*     */   
/*     */   public int currentSegment(float[] coords) {
/* 268 */     if (isDone())
/*     */     {
/* 270 */       throw new NoSuchElementException(Messages.getString("awt.4Bx"));
/*     */     }
/* 272 */     evaluate();
/* 273 */     int type = this.bufType;
/* 274 */     if (type != 4) {
/* 275 */       coords[0] = (float)this.px;
/* 276 */       coords[1] = (float)this.py;
/* 277 */       if (type != 0) {
/* 278 */         type = 1;
/*     */       }
/*     */     } 
/* 281 */     return type;
/*     */   }
/*     */   
/*     */   public int currentSegment(double[] coords) {
/* 285 */     if (isDone())
/*     */     {
/* 287 */       throw new NoSuchElementException(Messages.getString("awt.4B"));
/*     */     }
/* 289 */     evaluate();
/* 290 */     int type = this.bufType;
/* 291 */     if (type != 4) {
/* 292 */       coords[0] = this.px;
/* 293 */       coords[1] = this.py;
/* 294 */       if (type != 0) {
/* 295 */         type = 1;
/*     */       }
/*     */     } 
/* 298 */     return type;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/awt/geom/FlatteningPathIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */