/*     */ package com.itextpdf.text.pdf.parser;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LocationTextExtractionStrategy
/*     */   implements TextExtractionStrategy
/*     */ {
/*     */   static boolean DUMP_STATE = false;
/*  76 */   private final List<TextChunk> locationalResult = new ArrayList<TextChunk>();
/*     */ 
/*     */ 
/*     */   
/*     */   private final TextChunkLocationStrategy tclStrat;
/*     */ 
/*     */ 
/*     */   
/*     */   public LocationTextExtractionStrategy() {
/*  85 */     this(new TextChunkLocationStrategy() {
/*     */           public LocationTextExtractionStrategy.TextChunkLocation createLocation(TextRenderInfo renderInfo, LineSegment baseline) {
/*  87 */             return new LocationTextExtractionStrategy.TextChunkLocationDefaultImp(baseline.getStartPoint(), baseline.getEndPoint(), renderInfo.getSingleSpaceWidth());
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LocationTextExtractionStrategy(TextChunkLocationStrategy strat) {
/*  99 */     this.tclStrat = strat;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void beginTextBlock() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void endTextBlock() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean startsWithSpace(String str) {
/* 119 */     if (str.length() == 0) return false; 
/* 120 */     return (str.charAt(0) == ' ');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean endsWithSpace(String str) {
/* 128 */     if (str.length() == 0) return false; 
/* 129 */     return (str.charAt(str.length() - 1) == ' ');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<TextChunk> filterTextChunks(List<TextChunk> textChunks, TextChunkFilter filter) {
/* 140 */     if (filter == null) {
/* 141 */       return textChunks;
/*     */     }
/* 143 */     List<TextChunk> filtered = new ArrayList<TextChunk>();
/* 144 */     for (TextChunk textChunk : textChunks) {
/* 145 */       if (filter.accept(textChunk))
/* 146 */         filtered.add(textChunk); 
/*     */     } 
/* 148 */     return filtered;
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
/*     */   protected boolean isChunkAtWordBoundary(TextChunk chunk, TextChunk previousChunk) {
/* 162 */     return chunk.getLocation().isAtWordBoundary(previousChunk.getLocation());
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
/*     */   public String getResultantText(TextChunkFilter chunkFilter) {
/* 174 */     if (DUMP_STATE) dumpState();
/*     */     
/* 176 */     List<TextChunk> filteredTextChunks = filterTextChunks(this.locationalResult, chunkFilter);
/* 177 */     Collections.sort(filteredTextChunks);
/*     */     
/* 179 */     StringBuilder sb = new StringBuilder();
/* 180 */     TextChunk lastChunk = null;
/* 181 */     for (TextChunk chunk : filteredTextChunks) {
/*     */       
/* 183 */       if (lastChunk == null) {
/* 184 */         sb.append(chunk.text);
/*     */       }
/* 186 */       else if (chunk.sameLine(lastChunk)) {
/*     */         
/* 188 */         if (isChunkAtWordBoundary(chunk, lastChunk) && !startsWithSpace(chunk.text) && !endsWithSpace(lastChunk.text)) {
/* 189 */           sb.append(' ');
/*     */         }
/* 191 */         sb.append(chunk.text);
/*     */       } else {
/* 193 */         sb.append('\n');
/* 194 */         sb.append(chunk.text);
/*     */       } 
/*     */       
/* 197 */       lastChunk = chunk;
/*     */     } 
/*     */     
/* 200 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getResultantText() {
/* 209 */     return getResultantText(null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void dumpState() {
/* 215 */     for (TextChunk location : this.locationalResult) {
/* 216 */       location.printDiagnostics();
/*     */       
/* 218 */       System.out.println();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void renderText(TextRenderInfo renderInfo) {
/* 228 */     LineSegment segment = renderInfo.getBaseline();
/* 229 */     if (renderInfo.getRise() != 0.0F) {
/* 230 */       Matrix riseOffsetTransform = new Matrix(0.0F, -renderInfo.getRise());
/* 231 */       segment = segment.transformBy(riseOffsetTransform);
/*     */     } 
/* 233 */     TextChunk tc = new TextChunk(renderInfo.getText(), this.tclStrat.createLocation(renderInfo, segment));
/* 234 */     this.locationalResult.add(tc);
/*     */   }
/*     */ 
/*     */   
/*     */   public static interface TextChunkLocationStrategy
/*     */   {
/*     */     LocationTextExtractionStrategy.TextChunkLocation createLocation(TextRenderInfo param1TextRenderInfo, LineSegment param1LineSegment);
/*     */   }
/*     */ 
/*     */   
/*     */   public static interface TextChunkLocation
/*     */     extends Comparable<TextChunkLocation>
/*     */   {
/*     */     float distParallelEnd();
/*     */ 
/*     */     
/*     */     float distParallelStart();
/*     */ 
/*     */     
/*     */     int distPerpendicular();
/*     */ 
/*     */     
/*     */     float getCharSpaceWidth();
/*     */ 
/*     */     
/*     */     Vector getEndLocation();
/*     */     
/*     */     Vector getStartLocation();
/*     */     
/*     */     int orientationMagnitude();
/*     */     
/*     */     boolean sameLine(TextChunkLocation param1TextChunkLocation);
/*     */     
/*     */     float distanceFromEndOf(TextChunkLocation param1TextChunkLocation);
/*     */     
/*     */     boolean isAtWordBoundary(TextChunkLocation param1TextChunkLocation);
/*     */   }
/*     */   
/*     */   public static class TextChunkLocationDefaultImp
/*     */     implements TextChunkLocation
/*     */   {
/*     */     private final Vector startLocation;
/*     */     private final Vector endLocation;
/*     */     private final Vector orientationVector;
/*     */     private final int orientationMagnitude;
/*     */     private final int distPerpendicular;
/*     */     private final float distParallelStart;
/*     */     private final float distParallelEnd;
/*     */     private final float charSpaceWidth;
/*     */     
/*     */     public TextChunkLocationDefaultImp(Vector startLocation, Vector endLocation, float charSpaceWidth) {
/* 285 */       this.startLocation = startLocation;
/* 286 */       this.endLocation = endLocation;
/* 287 */       this.charSpaceWidth = charSpaceWidth;
/*     */       
/* 289 */       Vector oVector = endLocation.subtract(startLocation);
/* 290 */       if (oVector.length() == 0.0F) {
/* 291 */         oVector = new Vector(1.0F, 0.0F, 0.0F);
/*     */       }
/* 293 */       this.orientationVector = oVector.normalize();
/* 294 */       this.orientationMagnitude = (int)(Math.atan2(this.orientationVector.get(1), this.orientationVector.get(0)) * 1000.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 299 */       Vector origin = new Vector(0.0F, 0.0F, 1.0F);
/* 300 */       this.distPerpendicular = (int)startLocation.subtract(origin).cross(this.orientationVector).get(2);
/*     */       
/* 302 */       this.distParallelStart = this.orientationVector.dot(startLocation);
/* 303 */       this.distParallelEnd = this.orientationVector.dot(endLocation);
/*     */     }
/*     */     
/*     */     public int orientationMagnitude() {
/* 307 */       return this.orientationMagnitude;
/* 308 */     } public int distPerpendicular() { return this.distPerpendicular; }
/* 309 */     public float distParallelStart() { return this.distParallelStart; } public float distParallelEnd() {
/* 310 */       return this.distParallelEnd;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Vector getStartLocation() {
/* 317 */       return this.startLocation;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Vector getEndLocation() {
/* 324 */       return this.endLocation;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public float getCharSpaceWidth() {
/* 331 */       return this.charSpaceWidth;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean sameLine(LocationTextExtractionStrategy.TextChunkLocation as) {
/* 340 */       return (orientationMagnitude() == as.orientationMagnitude() && distPerpendicular() == as.distPerpendicular());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public float distanceFromEndOf(LocationTextExtractionStrategy.TextChunkLocation other) {
/* 352 */       float distance = distParallelStart() - other.distParallelEnd();
/* 353 */       return distance;
/*     */     }
/*     */     
/*     */     public boolean isAtWordBoundary(LocationTextExtractionStrategy.TextChunkLocation previous) {
/* 357 */       float dist = distanceFromEndOf(previous);
/*     */       
/* 359 */       if (dist < 0.0F) {
/* 360 */         dist = previous.distanceFromEndOf(this);
/*     */ 
/*     */         
/* 363 */         if (dist < 0.0F) {
/* 364 */           return false;
/*     */         }
/*     */       } 
/* 367 */       return (dist > getCharSpaceWidth() / 2.0F);
/*     */     }
/*     */     
/*     */     public int compareTo(LocationTextExtractionStrategy.TextChunkLocation other) {
/* 371 */       if (this == other) return 0;
/*     */ 
/*     */       
/* 374 */       int rslt = LocationTextExtractionStrategy.compareInts(orientationMagnitude(), other.orientationMagnitude());
/* 375 */       if (rslt != 0) return rslt;
/*     */       
/* 377 */       rslt = LocationTextExtractionStrategy.compareInts(distPerpendicular(), other.distPerpendicular());
/* 378 */       if (rslt != 0) return rslt;
/*     */       
/* 380 */       return Float.compare(distParallelStart(), other.distParallelStart());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class TextChunk
/*     */     implements Comparable<TextChunk>
/*     */   {
/*     */     private final String text;
/*     */     private final LocationTextExtractionStrategy.TextChunkLocation location;
/*     */     
/*     */     public TextChunk(String string, Vector startLocation, Vector endLocation, float charSpaceWidth) {
/* 392 */       this(string, new LocationTextExtractionStrategy.TextChunkLocationDefaultImp(startLocation, endLocation, charSpaceWidth));
/*     */     }
/*     */     
/*     */     public TextChunk(String string, LocationTextExtractionStrategy.TextChunkLocation loc) {
/* 396 */       this.text = string;
/* 397 */       this.location = loc;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getText() {
/* 404 */       return this.text;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public LocationTextExtractionStrategy.TextChunkLocation getLocation() {
/* 411 */       return this.location;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Vector getStartLocation() {
/* 418 */       return this.location.getStartLocation();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Vector getEndLocation() {
/* 424 */       return this.location.getEndLocation();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public float getCharSpaceWidth() {
/* 431 */       return this.location.getCharSpaceWidth();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public float distanceFromEndOf(TextChunk other) {
/* 443 */       return this.location.distanceFromEndOf(other.location);
/*     */     }
/*     */     
/*     */     private void printDiagnostics() {
/* 447 */       System.out.println("Text (@" + this.location.getStartLocation() + " -> " + this.location.getEndLocation() + "): " + this.text);
/* 448 */       System.out.println("orientationMagnitude: " + this.location.orientationMagnitude());
/* 449 */       System.out.println("distPerpendicular: " + this.location.distPerpendicular());
/* 450 */       System.out.println("distParallel: " + this.location.distParallelStart());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int compareTo(TextChunk rhs) {
/* 459 */       return this.location.compareTo(rhs.location);
/*     */     }
/*     */     
/*     */     private boolean sameLine(TextChunk lastChunk) {
/* 463 */       return getLocation().sameLine(lastChunk.getLocation());
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
/*     */   private static int compareInts(int int1, int int2) {
/* 475 */     return (int1 == int2) ? 0 : ((int1 < int2) ? -1 : 1);
/*     */   }
/*     */   
/*     */   public void renderImage(ImageRenderInfo renderInfo) {}
/*     */   
/*     */   public static interface TextChunkFilter {
/*     */     boolean accept(LocationTextExtractionStrategy.TextChunk param1TextChunk);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/LocationTextExtractionStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */