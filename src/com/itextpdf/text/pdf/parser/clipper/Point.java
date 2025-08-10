/*     */ package com.itextpdf.text.pdf.parser.clipper;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.util.Comparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Point<T extends Number & Comparable<T>>
/*     */ {
/*     */   public static class DoublePoint
/*     */     extends Point<Double>
/*     */   {
/*     */     public DoublePoint() {
/*  83 */       this(0.0D, 0.0D);
/*     */     }
/*     */     
/*     */     public DoublePoint(double x, double y) {
/*  87 */       this(x, y, 0.0D);
/*     */     }
/*     */     
/*     */     public DoublePoint(double x, double y, double z) {
/*  91 */       super(Double.valueOf(x), Double.valueOf(y), Double.valueOf(z));
/*     */     }
/*     */     
/*     */     public DoublePoint(DoublePoint other) {
/*  95 */       super(other);
/*     */     }
/*     */     
/*     */     public double getX() {
/*  99 */       return this.x.doubleValue();
/*     */     }
/*     */     
/*     */     public double getY() {
/* 103 */       return this.y.doubleValue();
/*     */     }
/*     */     
/*     */     public double getZ() {
/* 107 */       return this.z.doubleValue();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class LongPoint extends Point<Long> {
/*     */     public static double getDeltaX(LongPoint pt1, LongPoint pt2) {
/* 113 */       if (pt1.getY() == pt2.getY()) {
/* 114 */         return -3.4E38D;
/*     */       }
/*     */       
/* 117 */       return (pt2.getX() - pt1.getX()) / (pt2.getY() - pt1.getY());
/*     */     }
/*     */ 
/*     */     
/*     */     public LongPoint() {
/* 122 */       this(0L, 0L);
/*     */     }
/*     */     
/*     */     public LongPoint(long x, long y) {
/* 126 */       this(x, y, 0L);
/*     */     }
/*     */     
/*     */     public LongPoint(double x, double y) {
/* 130 */       this((long)x, (long)y);
/*     */     }
/*     */     
/*     */     public LongPoint(long x, long y, long z) {
/* 134 */       super(Long.valueOf(x), Long.valueOf(y), Long.valueOf(z));
/*     */     }
/*     */     
/*     */     public LongPoint(LongPoint other) {
/* 138 */       super(other);
/*     */     }
/*     */     
/*     */     public long getX() {
/* 142 */       return this.x.longValue();
/*     */     }
/*     */     
/*     */     public long getY() {
/* 146 */       return this.y.longValue();
/*     */     }
/*     */     
/*     */     public long getZ() {
/* 150 */       return this.z.longValue();
/*     */     } }
/*     */   
/*     */   private static class NumberComparator<T extends Number & Comparable<T>> implements Comparator<T> {
/*     */     private NumberComparator() {}
/*     */     
/*     */     public int compare(T a, T b) throws ClassCastException {
/* 157 */       return ((Comparable<T>)a).compareTo(b);
/*     */     }
/*     */   }
/*     */   
/*     */   static boolean arePointsClose(Point<? extends Number> pt1, Point<? extends Number> pt2, double distSqrd) {
/* 162 */     double dx = pt1.x.doubleValue() - pt2.x.doubleValue();
/* 163 */     double dy = pt1.y.doubleValue() - pt2.y.doubleValue();
/* 164 */     return (dx * dx + dy * dy <= distSqrd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static double distanceFromLineSqrd(Point<? extends Number> pt, Point<? extends Number> ln1, Point<? extends Number> ln2) {
/* 174 */     double A = ln1.y.doubleValue() - ln2.y.doubleValue();
/* 175 */     double B = ln2.x.doubleValue() - ln1.x.doubleValue();
/* 176 */     double C = A * ln1.x.doubleValue() + B * ln1.y.doubleValue();
/* 177 */     C = A * pt.x.doubleValue() + B * pt.y.doubleValue() - C;
/* 178 */     return C * C / (A * A + B * B);
/*     */   }
/*     */   
/*     */   static DoublePoint getUnitNormal(LongPoint pt1, LongPoint pt2) {
/* 182 */     double dx = (pt2.x.longValue() - pt1.x.longValue());
/* 183 */     double dy = (pt2.y.longValue() - pt1.y.longValue());
/* 184 */     if (dx == 0.0D && dy == 0.0D) {
/* 185 */       return new DoublePoint();
/*     */     }
/*     */     
/* 188 */     double f = 1.0D / Math.sqrt(dx * dx + dy * dy);
/* 189 */     dx *= f;
/* 190 */     dy *= f;
/*     */     
/* 192 */     return new DoublePoint(dy, -dx);
/*     */   }
/*     */   
/*     */   protected static boolean isPt2BetweenPt1AndPt3(LongPoint pt1, LongPoint pt2, LongPoint pt3) {
/* 196 */     if (pt1.equals(pt3) || pt1.equals(pt2) || pt3.equals(pt2)) {
/* 197 */       return false;
/*     */     }
/* 199 */     if (pt1.x != pt3.x) {
/* 200 */       return (((pt2.x.longValue() > pt1.x.longValue()) ? true : false) == ((pt2.x.longValue() < pt3.x.longValue()) ? true : false));
/*     */     }
/*     */     
/* 203 */     return (((pt2.y.longValue() > pt1.y.longValue()) ? true : false) == ((pt2.y.longValue() < pt3.y.longValue()) ? true : false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected static boolean slopesEqual(LongPoint pt1, LongPoint pt2, LongPoint pt3, boolean useFullRange) {
/* 208 */     if (useFullRange) {
/* 209 */       return BigInteger.valueOf(pt1.getY() - pt2.getY()).multiply(BigInteger.valueOf(pt2.getX() - pt3.getX())).equals(
/* 210 */           BigInteger.valueOf(pt1.getX() - pt2.getX()).multiply(BigInteger.valueOf(pt2.getY() - pt3.getY())));
/*     */     }
/* 212 */     return ((pt1.getY() - pt2.getY()) * (pt2.getX() - pt3.getX()) - (pt1.getX() - pt2.getX()) * (pt2.getY() - pt3.getY()) == 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   protected static boolean slopesEqual(LongPoint pt1, LongPoint pt2, LongPoint pt3, LongPoint pt4, boolean useFullRange) {
/* 217 */     if (useFullRange) {
/* 218 */       return BigInteger.valueOf(pt1.getY() - pt2.getY()).multiply(BigInteger.valueOf(pt3.getX() - pt4.getX())).equals(
/* 219 */           BigInteger.valueOf(pt1.getX() - pt2.getX()).multiply(BigInteger.valueOf(pt3.getY() - pt4.getY())));
/*     */     }
/* 221 */     return ((pt1.getY() - pt2.getY()) * (pt3.getX() - pt4.getX()) - (pt1.getX() - pt2.getX()) * (pt3.getY() - pt4.getY()) == 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean slopesNearCollinear(LongPoint pt1, LongPoint pt2, LongPoint pt3, double distSqrd) {
/* 229 */     if (Math.abs(pt1.x.longValue() - pt2.x.longValue()) > Math.abs(pt1.y.longValue() - pt2.y.longValue())) {
/* 230 */       if (((pt1.x.longValue() > pt2.x.longValue()) ? true : false) == ((pt1.x.longValue() < pt3.x.longValue()) ? true : false)) {
/* 231 */         return (distanceFromLineSqrd(pt1, pt2, pt3) < distSqrd);
/*     */       }
/* 233 */       if (((pt2.x.longValue() > pt1.x.longValue()) ? true : false) == ((pt2.x.longValue() < pt3.x.longValue()) ? true : false)) {
/* 234 */         return (distanceFromLineSqrd(pt2, pt1, pt3) < distSqrd);
/*     */       }
/*     */       
/* 237 */       return (distanceFromLineSqrd(pt3, pt1, pt2) < distSqrd);
/*     */     } 
/*     */ 
/*     */     
/* 241 */     if (((pt1.y.longValue() > pt2.y.longValue()) ? true : false) == ((pt1.y.longValue() < pt3.y.longValue()) ? true : false)) {
/* 242 */       return (distanceFromLineSqrd(pt1, pt2, pt3) < distSqrd);
/*     */     }
/* 244 */     if (((pt2.y.longValue() > pt1.y.longValue()) ? true : false) == ((pt2.y.longValue() < pt3.y.longValue()) ? true : false)) {
/* 245 */       return (distanceFromLineSqrd(pt2, pt1, pt3) < distSqrd);
/*     */     }
/*     */     
/* 248 */     return (distanceFromLineSqrd(pt3, pt1, pt2) < distSqrd);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 253 */   private static final NumberComparator NUMBER_COMPARATOR = new NumberComparator<Number>();
/*     */   
/*     */   protected T x;
/*     */   
/*     */   protected T y;
/*     */   
/*     */   protected T z;
/*     */   
/*     */   protected Point(Point<T> pt) {
/* 262 */     this(pt.x, pt.y, pt.z);
/*     */   }
/*     */   
/*     */   protected Point(T x, T y, T z) {
/* 266 */     this.x = x;
/* 267 */     this.y = y;
/* 268 */     this.z = z;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 273 */     if (obj == null) {
/* 274 */       return false;
/*     */     }
/* 276 */     if (obj instanceof Point) {
/* 277 */       Point<?> a = (Point)obj;
/* 278 */       return (NUMBER_COMPARATOR.compare(this.x, a.x) == 0 && NUMBER_COMPARATOR.compare(this.y, a.y) == 0);
/*     */     } 
/*     */     
/* 281 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(Point<T> other) {
/* 286 */     this.x = other.x;
/* 287 */     this.y = other.y;
/* 288 */     this.z = other.z;
/*     */   }
/*     */   
/*     */   public void setX(T x) {
/* 292 */     this.x = x;
/*     */   }
/*     */   
/*     */   public void setY(T y) {
/* 296 */     this.y = y;
/*     */   }
/*     */   
/*     */   public void setZ(T z) {
/* 300 */     this.z = z;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 305 */     return "Point [x=" + this.x + ", y=" + this.y + ", z=" + this.z + "]";
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/clipper/Point.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */