/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.IntStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DeweyDecimal
/*     */   implements Comparable<DeweyDecimal>
/*     */ {
/*     */   private final int[] components;
/*     */   
/*     */   public DeweyDecimal(int[] components) {
/*  44 */     this.components = new int[components.length];
/*  45 */     System.arraycopy(components, 0, this.components, 0, components.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DeweyDecimal(String string) throws NumberFormatException {
/*  56 */     StringTokenizer tokenizer = new StringTokenizer(string, ".", true);
/*  57 */     int size = tokenizer.countTokens();
/*     */     
/*  59 */     this.components = new int[(size + 1) / 2];
/*     */     
/*  61 */     for (int i = 0; i < this.components.length; i++) {
/*  62 */       String component = tokenizer.nextToken();
/*  63 */       if (component.isEmpty()) {
/*  64 */         throw new NumberFormatException("Empty component in string");
/*     */       }
/*     */       
/*  67 */       this.components[i] = Integer.parseInt(component);
/*     */ 
/*     */       
/*  70 */       if (tokenizer.hasMoreTokens()) {
/*  71 */         tokenizer.nextToken();
/*     */ 
/*     */         
/*  74 */         if (!tokenizer.hasMoreTokens()) {
/*  75 */           throw new NumberFormatException("DeweyDecimal ended in a '.'");
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSize() {
/*  87 */     return this.components.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int get(int index) {
/*  97 */     return this.components[index];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEqual(DeweyDecimal other) {
/* 108 */     int max = Math.max(other.components.length, this.components.length);
/*     */     
/* 110 */     for (int i = 0; i < max; i++) {
/* 111 */       int component1 = (i < this.components.length) ? this.components[i] : 0;
/* 112 */       int component2 = (i < other.components.length) ? other.components[i] : 0;
/*     */       
/* 114 */       if (component2 != component1) {
/* 115 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 119 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLessThan(DeweyDecimal other) {
/* 130 */     return !isGreaterThanOrEqual(other);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLessThanOrEqual(DeweyDecimal other) {
/* 141 */     return !isGreaterThan(other);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isGreaterThan(DeweyDecimal other) {
/* 152 */     int max = Math.max(other.components.length, this.components.length);
/*     */     
/* 154 */     for (int i = 0; i < max; i++) {
/* 155 */       int component1 = (i < this.components.length) ? this.components[i] : 0;
/* 156 */       int component2 = (i < other.components.length) ? other.components[i] : 0;
/*     */       
/* 158 */       if (component2 > component1) {
/* 159 */         return false;
/*     */       }
/* 161 */       if (component2 < component1) {
/* 162 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 166 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isGreaterThanOrEqual(DeweyDecimal other) {
/* 177 */     int max = Math.max(other.components.length, this.components.length);
/*     */     
/* 179 */     for (int i = 0; i < max; i++) {
/* 180 */       int component1 = (i < this.components.length) ? this.components[i] : 0;
/* 181 */       int component2 = (i < other.components.length) ? other.components[i] : 0;
/*     */       
/* 183 */       if (component2 > component1) {
/* 184 */         return false;
/*     */       }
/* 186 */       if (component2 < component1) {
/* 187 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 191 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 200 */     return IntStream.of(this.components).<CharSequence>mapToObj(Integer::toString)
/* 201 */       .collect(Collectors.joining("."));
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
/*     */   public int compareTo(DeweyDecimal other) {
/* 213 */     int max = Math.max(other.components.length, this.components.length);
/* 214 */     for (int i = 0; i < max; i++) {
/* 215 */       int component1 = (i < this.components.length) ? this.components[i] : 0;
/* 216 */       int component2 = (i < other.components.length) ? other.components[i] : 0;
/* 217 */       if (component1 != component2) {
/* 218 */         return component1 - component2;
/*     */       }
/*     */     } 
/* 221 */     return 0;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 225 */     return toString().hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 229 */     return (o instanceof DeweyDecimal && isEqual((DeweyDecimal)o));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/DeweyDecimal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */