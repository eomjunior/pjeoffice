/*     */ package com.itextpdf.awt.geom.misc;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RenderingHints
/*     */   implements Map<Object, Object>, Cloneable
/*     */ {
/*  37 */   public static final Key KEY_ALPHA_INTERPOLATION = new KeyImpl(1);
/*  38 */   public static final Object VALUE_ALPHA_INTERPOLATION_DEFAULT = new KeyValue(KEY_ALPHA_INTERPOLATION);
/*  39 */   public static final Object VALUE_ALPHA_INTERPOLATION_SPEED = new KeyValue(KEY_ALPHA_INTERPOLATION);
/*  40 */   public static final Object VALUE_ALPHA_INTERPOLATION_QUALITY = new KeyValue(KEY_ALPHA_INTERPOLATION);
/*     */   
/*  42 */   public static final Key KEY_ANTIALIASING = new KeyImpl(2);
/*  43 */   public static final Object VALUE_ANTIALIAS_DEFAULT = new KeyValue(KEY_ANTIALIASING);
/*  44 */   public static final Object VALUE_ANTIALIAS_ON = new KeyValue(KEY_ANTIALIASING);
/*  45 */   public static final Object VALUE_ANTIALIAS_OFF = new KeyValue(KEY_ANTIALIASING);
/*     */   
/*  47 */   public static final Key KEY_COLOR_RENDERING = new KeyImpl(3);
/*  48 */   public static final Object VALUE_COLOR_RENDER_DEFAULT = new KeyValue(KEY_COLOR_RENDERING);
/*  49 */   public static final Object VALUE_COLOR_RENDER_SPEED = new KeyValue(KEY_COLOR_RENDERING);
/*  50 */   public static final Object VALUE_COLOR_RENDER_QUALITY = new KeyValue(KEY_COLOR_RENDERING);
/*     */   
/*  52 */   public static final Key KEY_DITHERING = new KeyImpl(4);
/*  53 */   public static final Object VALUE_DITHER_DEFAULT = new KeyValue(KEY_DITHERING);
/*  54 */   public static final Object VALUE_DITHER_DISABLE = new KeyValue(KEY_DITHERING);
/*  55 */   public static final Object VALUE_DITHER_ENABLE = new KeyValue(KEY_DITHERING);
/*     */   
/*  57 */   public static final Key KEY_FRACTIONALMETRICS = new KeyImpl(5);
/*  58 */   public static final Object VALUE_FRACTIONALMETRICS_DEFAULT = new KeyValue(KEY_FRACTIONALMETRICS);
/*  59 */   public static final Object VALUE_FRACTIONALMETRICS_ON = new KeyValue(KEY_FRACTIONALMETRICS);
/*  60 */   public static final Object VALUE_FRACTIONALMETRICS_OFF = new KeyValue(KEY_FRACTIONALMETRICS);
/*     */   
/*  62 */   public static final Key KEY_INTERPOLATION = new KeyImpl(6);
/*  63 */   public static final Object VALUE_INTERPOLATION_BICUBIC = new KeyValue(KEY_INTERPOLATION);
/*  64 */   public static final Object VALUE_INTERPOLATION_BILINEAR = new KeyValue(KEY_INTERPOLATION);
/*  65 */   public static final Object VALUE_INTERPOLATION_NEAREST_NEIGHBOR = new KeyValue(KEY_INTERPOLATION);
/*     */   
/*  67 */   public static final Key KEY_RENDERING = new KeyImpl(7);
/*  68 */   public static final Object VALUE_RENDER_DEFAULT = new KeyValue(KEY_RENDERING);
/*  69 */   public static final Object VALUE_RENDER_SPEED = new KeyValue(KEY_RENDERING);
/*  70 */   public static final Object VALUE_RENDER_QUALITY = new KeyValue(KEY_RENDERING);
/*     */   
/*  72 */   public static final Key KEY_STROKE_CONTROL = new KeyImpl(8);
/*  73 */   public static final Object VALUE_STROKE_DEFAULT = new KeyValue(KEY_STROKE_CONTROL);
/*  74 */   public static final Object VALUE_STROKE_NORMALIZE = new KeyValue(KEY_STROKE_CONTROL);
/*  75 */   public static final Object VALUE_STROKE_PURE = new KeyValue(KEY_STROKE_CONTROL);
/*     */   
/*  77 */   public static final Key KEY_TEXT_ANTIALIASING = new KeyImpl(9);
/*  78 */   public static final Object VALUE_TEXT_ANTIALIAS_DEFAULT = new KeyValue(KEY_TEXT_ANTIALIASING);
/*  79 */   public static final Object VALUE_TEXT_ANTIALIAS_ON = new KeyValue(KEY_TEXT_ANTIALIASING);
/*  80 */   public static final Object VALUE_TEXT_ANTIALIAS_OFF = new KeyValue(KEY_TEXT_ANTIALIASING);
/*     */   
/*  82 */   private HashMap<Object, Object> map = new HashMap<Object, Object>();
/*     */ 
/*     */   
/*     */   public RenderingHints(Map<Key, ?> map) {
/*  86 */     if (map != null) {
/*  87 */       putAll(map);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public RenderingHints(Key key, Object value) {
/*  93 */     put(key, value);
/*     */   }
/*     */   
/*     */   public void add(RenderingHints hints) {
/*  97 */     this.map.putAll(hints.map);
/*     */   }
/*     */   
/*     */   public Object put(Object key, Object value) {
/* 101 */     if (!((Key)key).isCompatibleValue(value)) {
/* 102 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/* 105 */     return this.map.put(key, value);
/*     */   }
/*     */   
/*     */   public Object remove(Object key) {
/* 109 */     return this.map.remove(key);
/*     */   }
/*     */   
/*     */   public Object get(Object key) {
/* 113 */     return this.map.get(key);
/*     */   }
/*     */   
/*     */   public Set<Object> keySet() {
/* 117 */     return this.map.keySet();
/*     */   }
/*     */   
/*     */   public Set<Map.Entry<Object, Object>> entrySet() {
/* 121 */     return this.map.entrySet();
/*     */   }
/*     */   
/*     */   public void putAll(Map<?, ?> m) {
/* 125 */     if (m instanceof RenderingHints) {
/* 126 */       this.map.putAll(((RenderingHints)m).map);
/*     */     } else {
/* 128 */       Set<?> entries = m.entrySet();
/*     */       
/* 130 */       if (entries != null) {
/* 131 */         Iterator<?> it = entries.iterator();
/* 132 */         while (it.hasNext()) {
/* 133 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)it.next();
/* 134 */           Key key = (Key)entry.getKey();
/* 135 */           Object val = entry.getValue();
/* 136 */           put(key, val);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Collection<Object> values() {
/* 143 */     return this.map.values();
/*     */   }
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 147 */     return this.map.containsValue(value);
/*     */   }
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 151 */     if (key == null) {
/* 152 */       throw new NullPointerException();
/*     */     }
/*     */     
/* 155 */     return this.map.containsKey(key);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 159 */     return this.map.isEmpty();
/*     */   }
/*     */   
/*     */   public void clear() {
/* 163 */     this.map.clear();
/*     */   }
/*     */   
/*     */   public int size() {
/* 167 */     return this.map.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 172 */     if (!(o instanceof Map)) {
/* 173 */       return false;
/*     */     }
/*     */     
/* 176 */     Map<?, ?> m = (Map<?, ?>)o;
/* 177 */     Set<?> keys = keySet();
/* 178 */     if (!keys.equals(m.keySet())) {
/* 179 */       return false;
/*     */     }
/*     */     
/* 182 */     Iterator<?> it = keys.iterator();
/* 183 */     while (it.hasNext()) {
/* 184 */       Key key = (Key)it.next();
/* 185 */       Object v1 = get(key);
/* 186 */       Object v2 = m.get(key);
/* 187 */       if ((v1 == null) ? (v2 == null) : v1.equals(v2))
/* 188 */         continue;  return false;
/*     */     } 
/*     */     
/* 191 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 196 */     return this.map.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() {
/* 202 */     RenderingHints clone = new RenderingHints(null);
/* 203 */     clone.map = (HashMap<Object, Object>)this.map.clone();
/* 204 */     return clone;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 209 */     return "RenderingHints[" + this.map.toString() + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public static abstract class Key
/*     */   {
/*     */     private final int key;
/*     */ 
/*     */     
/*     */     protected Key(int key) {
/* 219 */       this.key = key;
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean equals(Object o) {
/* 224 */       return (this == o);
/*     */     }
/*     */ 
/*     */     
/*     */     public final int hashCode() {
/* 229 */       return System.identityHashCode(this);
/*     */     }
/*     */     
/*     */     protected final int intKey() {
/* 233 */       return this.key;
/*     */     }
/*     */ 
/*     */     
/*     */     public abstract boolean isCompatibleValue(Object param1Object);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class KeyImpl
/*     */     extends Key
/*     */   {
/*     */     protected KeyImpl(int key) {
/* 245 */       super(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isCompatibleValue(Object val) {
/* 250 */       if (!(val instanceof RenderingHints.KeyValue)) {
/* 251 */         return false;
/*     */       }
/*     */       
/* 254 */       return (((RenderingHints.KeyValue)val).key == this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class KeyValue
/*     */   {
/*     */     private final RenderingHints.Key key;
/*     */ 
/*     */     
/*     */     protected KeyValue(RenderingHints.Key key) {
/* 265 */       this.key = key;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/awt/geom/misc/RenderingHints.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */