/*     */ package com.itextpdf.awt.geom;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Rectangle
/*     */   extends Rectangle2D
/*     */   implements Shape, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -4345857070255674764L;
/*     */   public double x;
/*     */   public double y;
/*     */   public double width;
/*     */   public double height;
/*     */   
/*     */   public Rectangle() {
/*  38 */     setBounds(0, 0, 0, 0);
/*     */   }
/*     */   
/*     */   public Rectangle(Point p) {
/*  42 */     setBounds(p.x, p.y, 0.0D, 0.0D);
/*     */   }
/*     */   
/*     */   public Rectangle(Point p, Dimension d) {
/*  46 */     setBounds(p.x, p.y, d.width, d.height);
/*     */   }
/*     */   
/*     */   public Rectangle(double x, double y, double width, double height) {
/*  50 */     setBounds(x, y, width, height);
/*     */   }
/*     */   
/*     */   public Rectangle(int width, int height) {
/*  54 */     setBounds(0, 0, width, height);
/*     */   }
/*     */   
/*     */   public Rectangle(Rectangle r) {
/*  58 */     setBounds(r.x, r.y, r.width, r.height);
/*     */   }
/*     */   
/*     */   public Rectangle(com.itextpdf.text.Rectangle r) {
/*  62 */     r.normalize();
/*  63 */     setBounds(r.getLeft(), r.getBottom(), r.getWidth(), r.getHeight());
/*     */   }
/*     */   
/*     */   public Rectangle(Dimension d) {
/*  67 */     setBounds(0.0D, 0.0D, d.width, d.height);
/*     */   }
/*     */ 
/*     */   
/*     */   public double getX() {
/*  72 */     return this.x;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getY() {
/*  77 */     return this.y;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getHeight() {
/*  82 */     return this.height;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getWidth() {
/*  87 */     return this.width;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  92 */     return (this.width <= 0.0D || this.height <= 0.0D);
/*     */   }
/*     */   
/*     */   public Dimension getSize() {
/*  96 */     return new Dimension(this.width, this.height);
/*     */   }
/*     */   
/*     */   public void setSize(int mx, int my) {
/* 100 */     setSize(mx, my);
/*     */   }
/*     */   public void setSize(double width, double height) {
/* 103 */     this.width = width;
/* 104 */     this.height = height;
/*     */   }
/*     */   
/*     */   public void setSize(Dimension d) {
/* 108 */     setSize(d.width, d.height);
/*     */   }
/*     */   
/*     */   public Point getLocation() {
/* 112 */     return new Point(this.x, this.y);
/*     */   }
/*     */   
/*     */   public void setLocation(int mx, int my) {
/* 116 */     setLocation(mx, my);
/*     */   }
/*     */   public void setLocation(double x, double y) {
/* 119 */     this.x = x;
/* 120 */     this.y = y;
/*     */   }
/*     */   
/*     */   public void setLocation(Point p) {
/* 124 */     setLocation(p.x, p.y);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRect(double x, double y, double width, double height) {
/* 129 */     int x1 = (int)Math.floor(x);
/* 130 */     int y1 = (int)Math.floor(y);
/* 131 */     int x2 = (int)Math.ceil(x + width);
/* 132 */     int y2 = (int)Math.ceil(y + height);
/* 133 */     setBounds(x1, y1, x2 - x1, y2 - y1);
/*     */   }
/*     */ 
/*     */   
/*     */   public Rectangle getBounds() {
/* 138 */     return new Rectangle(this.x, this.y, this.width, this.height);
/*     */   }
/*     */ 
/*     */   
/*     */   public Rectangle2D getBounds2D() {
/* 143 */     return getBounds();
/*     */   }
/*     */   
/*     */   public void setBounds(int x, int y, int width, int height) {
/* 147 */     setBounds(x, y, width, height);
/*     */   }
/*     */   public void setBounds(double x, double y, double width, double height) {
/* 150 */     this.x = x;
/* 151 */     this.y = y;
/* 152 */     this.height = height;
/* 153 */     this.width = width;
/*     */   }
/*     */   
/*     */   public void setBounds(Rectangle r) {
/* 157 */     setBounds(r.x, r.y, r.width, r.height);
/*     */   }
/*     */   
/*     */   public void grow(int mx, int my) {
/* 161 */     translate(mx, my);
/*     */   }
/*     */   public void grow(double dx, double dy) {
/* 164 */     this.x -= dx;
/* 165 */     this.y -= dy;
/* 166 */     this.width += dx + dx;
/* 167 */     this.height += dy + dy;
/*     */   }
/*     */   
/*     */   public void translate(int mx, int my) {
/* 171 */     translate(mx, my);
/*     */   }
/*     */   public void translate(double mx, double my) {
/* 174 */     this.x += mx;
/* 175 */     this.y += my;
/*     */   }
/*     */   
/*     */   public void add(int px, int py) {
/* 179 */     add(px, py);
/*     */   }
/*     */   public void add(double px, double py) {
/* 182 */     double x1 = Math.min(this.x, px);
/* 183 */     double x2 = Math.max(this.x + this.width, px);
/* 184 */     double y1 = Math.min(this.y, py);
/* 185 */     double y2 = Math.max(this.y + this.height, py);
/* 186 */     setBounds(x1, y1, x2 - x1, y2 - y1);
/*     */   }
/*     */   
/*     */   public void add(Point p) {
/* 190 */     add(p.x, p.y);
/*     */   }
/*     */   
/*     */   public void add(Rectangle r) {
/* 194 */     double x1 = Math.min(this.x, r.x);
/* 195 */     double x2 = Math.max(this.x + this.width, r.x + r.width);
/* 196 */     double y1 = Math.min(this.y, r.y);
/* 197 */     double y2 = Math.max(this.y + this.height, r.y + r.height);
/* 198 */     setBounds(x1, y1, x2 - x1, y2 - y1);
/*     */   }
/*     */   
/*     */   public boolean contains(int px, int py) {
/* 202 */     return contains(px, py);
/*     */   }
/*     */   public boolean contains(double px, double py) {
/* 205 */     if (isEmpty()) {
/* 206 */       return false;
/*     */     }
/* 208 */     if (px < this.x || py < this.y) {
/* 209 */       return false;
/*     */     }
/* 211 */     px -= this.x;
/* 212 */     py -= this.y;
/* 213 */     return (px < this.width && py < this.height);
/*     */   }
/*     */   
/*     */   public boolean contains(Point p) {
/* 217 */     return contains(p.x, p.y);
/*     */   }
/*     */   
/*     */   public boolean contains(int rx, int ry, int rw, int rh) {
/* 221 */     return (contains(rx, ry) && contains(rx + rw - 1, ry + rh - 1));
/*     */   }
/*     */   
/*     */   public boolean contains(double rx, double ry, double rw, double rh) {
/* 225 */     return (contains(rx, ry) && contains(rx + rw - 0.01D, ry + rh - 0.01D));
/*     */   }
/*     */   
/*     */   public boolean contains(Rectangle r) {
/* 229 */     return contains(r.x, r.y, r.width, r.height);
/*     */   }
/*     */ 
/*     */   
/*     */   public Rectangle2D createIntersection(Rectangle2D r) {
/* 234 */     if (r instanceof Rectangle) {
/* 235 */       return intersection((Rectangle)r);
/*     */     }
/* 237 */     Rectangle2D dst = new Rectangle2D.Double();
/* 238 */     Rectangle2D.intersect(this, r, dst);
/* 239 */     return dst;
/*     */   }
/*     */   
/*     */   public Rectangle intersection(Rectangle r) {
/* 243 */     double x1 = Math.max(this.x, r.x);
/* 244 */     double y1 = Math.max(this.y, r.y);
/* 245 */     double x2 = Math.min(this.x + this.width, r.x + r.width);
/* 246 */     double y2 = Math.min(this.y + this.height, r.y + r.height);
/* 247 */     return new Rectangle(x1, y1, x2 - x1, y2 - y1);
/*     */   }
/*     */   
/*     */   public boolean intersects(Rectangle r) {
/* 251 */     return !intersection(r).isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public int outcode(double px, double py) {
/* 256 */     int code = 0;
/*     */     
/* 258 */     if (this.width <= 0.0D) {
/* 259 */       code |= 0x5;
/*     */     }
/* 261 */     else if (px < this.x) {
/* 262 */       code |= 0x1;
/*     */     }
/* 264 */     else if (px > this.x + this.width) {
/* 265 */       code |= 0x4;
/*     */     } 
/*     */     
/* 268 */     if (this.height <= 0.0D) {
/* 269 */       code |= 0xA;
/*     */     }
/* 271 */     else if (py < this.y) {
/* 272 */       code |= 0x2;
/*     */     }
/* 274 */     else if (py > this.y + this.height) {
/* 275 */       code |= 0x8;
/*     */     } 
/*     */     
/* 278 */     return code;
/*     */   }
/*     */ 
/*     */   
/*     */   public Rectangle2D createUnion(Rectangle2D r) {
/* 283 */     if (r instanceof Rectangle) {
/* 284 */       return union((Rectangle)r);
/*     */     }
/* 286 */     Rectangle2D dst = new Rectangle2D.Double();
/* 287 */     Rectangle2D.union(this, r, dst);
/* 288 */     return dst;
/*     */   }
/*     */   
/*     */   public Rectangle union(Rectangle r) {
/* 292 */     Rectangle dst = new Rectangle(this);
/* 293 */     dst.add(r);
/* 294 */     return dst;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 299 */     if (obj == this) {
/* 300 */       return true;
/*     */     }
/* 302 */     if (obj instanceof Rectangle) {
/* 303 */       Rectangle r = (Rectangle)obj;
/* 304 */       return (r.x == this.x && r.y == this.y && r.width == this.width && r.height == this.height);
/*     */     } 
/* 306 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 313 */     return getClass().getName() + "[x=" + this.x + ",y=" + this.y + ",width=" + this.width + ",height=" + this.height + "]";
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/awt/geom/Rectangle.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */