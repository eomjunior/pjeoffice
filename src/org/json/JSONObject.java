/*      */ package org.json;
/*      */ 
/*      */ import java.io.Closeable;
/*      */ import java.io.IOException;
/*      */ import java.io.StringWriter;
/*      */ import java.io.Writer;
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.IdentityHashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.Set;
/*      */ import java.util.regex.Pattern;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class JSONObject
/*      */ {
/*      */   private static final class Null
/*      */   {
/*      */     private Null() {}
/*      */     
/*      */     protected final Object clone() {
/*  100 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object object) {
/*  114 */       return (object == null || object == this);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  123 */       return 0;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  133 */       return "null";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  141 */   static final Pattern NUMBER_PATTERN = Pattern.compile("-?(?:0|[1-9]\\d*)(?:\\.\\d+)?(?:[eE][+-]?\\d+)?");
/*      */ 
/*      */   
/*      */   private final Map<String, Object> map;
/*      */ 
/*      */ 
/*      */   
/*      */   public Class<? extends Map> getMapType() {
/*  149 */     return (Class)this.map.getClass();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  158 */   public static final Object NULL = new Null();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject() {
/*  170 */     this.map = new HashMap<>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject(JSONObject jo, String... names) {
/*  184 */     this(names.length);
/*  185 */     for (int i = 0; i < names.length; i++) {
/*      */       try {
/*  187 */         putOnce(names[i], jo.opt(names[i]));
/*  188 */       } catch (Exception exception) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject(JSONTokener x) throws JSONException {
/*  203 */     this();
/*      */ 
/*      */ 
/*      */     
/*  207 */     if (x.nextClean() != '{') {
/*  208 */       throw x.syntaxError("A JSONObject text must begin with '{'");
/*      */     }
/*      */     while (true) {
/*  211 */       char c = x.nextClean();
/*  212 */       switch (c) {
/*      */         case '\000':
/*  214 */           throw x.syntaxError("A JSONObject text must end with '}'");
/*      */         case '}':
/*      */           return;
/*      */       } 
/*  218 */       String key = x.nextSimpleValue(c).toString();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  223 */       c = x.nextClean();
/*  224 */       if (c != ':') {
/*  225 */         throw x.syntaxError("Expected a ':' after a key");
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  230 */       if (key != null) {
/*      */         
/*  232 */         if (opt(key) != null)
/*      */         {
/*  234 */           throw x.syntaxError("Duplicate key \"" + key + "\"");
/*      */         }
/*      */         
/*  237 */         Object value = x.nextValue();
/*  238 */         if (value != null) {
/*  239 */           put(key, value);
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  245 */       switch (x.nextClean()) {
/*      */         case ',':
/*      */         case ';':
/*  248 */           if (x.nextClean() == '}') {
/*      */             return;
/*      */           }
/*  251 */           if (x.end()) {
/*  252 */             throw x.syntaxError("A JSONObject text must end with '}'");
/*      */           }
/*  254 */           x.back(); continue;
/*      */         case '}':
/*      */           return;
/*      */       }  break;
/*      */     } 
/*  259 */     throw x.syntaxError("Expected a ',' or '}'");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject(Map<?, ?> m) {
/*  276 */     if (m == null) {
/*  277 */       this.map = new HashMap<>();
/*      */     } else {
/*  279 */       this.map = new HashMap<>(m.size());
/*  280 */       for (Map.Entry<?, ?> e : m.entrySet()) {
/*  281 */         if (e.getKey() == null) {
/*  282 */           throw new NullPointerException("Null key.");
/*      */         }
/*  284 */         Object value = e.getValue();
/*  285 */         if (value != null) {
/*  286 */           testValidity(value);
/*  287 */           this.map.put(String.valueOf(e.getKey()), wrap(value));
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject(Object bean) {
/*  354 */     this();
/*  355 */     populateMap(bean);
/*      */   }
/*      */   
/*      */   private JSONObject(Object bean, Set<Object> objectsRecord) {
/*  359 */     this();
/*  360 */     populateMap(bean, objectsRecord);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject(Object object, String... names) {
/*  378 */     this(names.length);
/*  379 */     Class<?> c = object.getClass();
/*  380 */     for (int i = 0; i < names.length; i++) {
/*  381 */       String name = names[i];
/*      */       try {
/*  383 */         putOpt(name, c.getField(name).get(object));
/*  384 */       } catch (Exception exception) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject(String source) throws JSONException {
/*  402 */     this(new JSONTokener(source));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject(String baseName, Locale locale) throws JSONException {
/*  416 */     this();
/*  417 */     ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale, 
/*  418 */         Thread.currentThread().getContextClassLoader());
/*      */ 
/*      */ 
/*      */     
/*  422 */     Enumeration<String> keys = bundle.getKeys();
/*  423 */     while (keys.hasMoreElements()) {
/*  424 */       Object key = keys.nextElement();
/*  425 */       if (key != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  431 */         String[] path = ((String)key).split("\\.");
/*  432 */         int last = path.length - 1;
/*  433 */         JSONObject target = this;
/*  434 */         for (int i = 0; i < last; i++) {
/*  435 */           String segment = path[i];
/*  436 */           JSONObject nextTarget = target.optJSONObject(segment);
/*  437 */           if (nextTarget == null) {
/*  438 */             nextTarget = new JSONObject();
/*  439 */             target.put(segment, nextTarget);
/*      */           } 
/*  441 */           target = nextTarget;
/*      */         } 
/*  443 */         target.put(path[last], bundle.getString((String)key));
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JSONObject(int initialCapacity) {
/*  456 */     this.map = new HashMap<>(initialCapacity);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject accumulate(String key, Object value) throws JSONException {
/*  481 */     testValidity(value);
/*  482 */     Object object = opt(key);
/*  483 */     if (object == null) {
/*  484 */       put(key, (value instanceof JSONArray) ? (new JSONArray())
/*  485 */           .put(value) : value);
/*      */     }
/*  487 */     else if (object instanceof JSONArray) {
/*  488 */       ((JSONArray)object).put(value);
/*      */     } else {
/*  490 */       put(key, (new JSONArray()).put(object).put(value));
/*      */     } 
/*  492 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject append(String key, Object value) throws JSONException {
/*  513 */     testValidity(value);
/*  514 */     Object object = opt(key);
/*  515 */     if (object == null) {
/*  516 */       put(key, (new JSONArray()).put(value));
/*  517 */     } else if (object instanceof JSONArray) {
/*  518 */       put(key, ((JSONArray)object).put(value));
/*      */     } else {
/*  520 */       throw wrongValueFormatException(key, "JSONArray", null, null);
/*      */     } 
/*  522 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String doubleToString(double d) {
/*  534 */     if (Double.isInfinite(d) || Double.isNaN(d)) {
/*  535 */       return "null";
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  540 */     String string = Double.toString(d);
/*  541 */     if (string.indexOf('.') > 0 && string.indexOf('e') < 0 && string
/*  542 */       .indexOf('E') < 0) {
/*  543 */       while (string.endsWith("0")) {
/*  544 */         string = string.substring(0, string.length() - 1);
/*      */       }
/*  546 */       if (string.endsWith(".")) {
/*  547 */         string = string.substring(0, string.length() - 1);
/*      */       }
/*      */     } 
/*  550 */     return string;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object get(String key) throws JSONException {
/*  563 */     if (key == null) {
/*  564 */       throw new JSONException("Null key.");
/*      */     }
/*  566 */     Object object = opt(key);
/*  567 */     if (object == null) {
/*  568 */       throw new JSONException("JSONObject[" + quote(key) + "] not found.");
/*      */     }
/*  570 */     return object;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> E getEnum(Class<E> clazz, String key) throws JSONException {
/*  588 */     E val = optEnum(clazz, key);
/*  589 */     if (val == null)
/*      */     {
/*      */ 
/*      */       
/*  593 */       throw wrongValueFormatException(key, "enum of type " + quote(clazz.getSimpleName()), opt(key), null);
/*      */     }
/*  595 */     return val;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getBoolean(String key) throws JSONException {
/*  609 */     Object object = get(key);
/*  610 */     if (object.equals(Boolean.FALSE) || (object instanceof String && ((String)object)
/*      */       
/*  612 */       .equalsIgnoreCase("false")))
/*  613 */       return false; 
/*  614 */     if (object.equals(Boolean.TRUE) || (object instanceof String && ((String)object)
/*      */       
/*  616 */       .equalsIgnoreCase("true"))) {
/*  617 */       return true;
/*      */     }
/*  619 */     throw wrongValueFormatException(key, "Boolean", object, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BigInteger getBigInteger(String key) throws JSONException {
/*  633 */     Object object = get(key);
/*  634 */     BigInteger ret = objectToBigInteger(object, null);
/*  635 */     if (ret != null) {
/*  636 */       return ret;
/*      */     }
/*  638 */     throw wrongValueFormatException(key, "BigInteger", object, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BigDecimal getBigDecimal(String key) throws JSONException {
/*  655 */     Object object = get(key);
/*  656 */     BigDecimal ret = objectToBigDecimal(object, null);
/*  657 */     if (ret != null) {
/*  658 */       return ret;
/*      */     }
/*  660 */     throw wrongValueFormatException(key, "BigDecimal", object, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getDouble(String key) throws JSONException {
/*  674 */     Object object = get(key);
/*  675 */     if (object instanceof Number) {
/*  676 */       return ((Number)object).doubleValue();
/*      */     }
/*      */     try {
/*  679 */       return Double.parseDouble(object.toString());
/*  680 */     } catch (Exception e) {
/*  681 */       throw wrongValueFormatException(key, "double", object, e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getFloat(String key) throws JSONException {
/*  696 */     Object object = get(key);
/*  697 */     if (object instanceof Number) {
/*  698 */       return ((Number)object).floatValue();
/*      */     }
/*      */     try {
/*  701 */       return Float.parseFloat(object.toString());
/*  702 */     } catch (Exception e) {
/*  703 */       throw wrongValueFormatException(key, "float", object, e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Number getNumber(String key) throws JSONException {
/*  718 */     Object object = get(key);
/*      */     try {
/*  720 */       if (object instanceof Number) {
/*  721 */         return (Number)object;
/*      */       }
/*  723 */       return stringToNumber(object.toString());
/*  724 */     } catch (Exception e) {
/*  725 */       throw wrongValueFormatException(key, "number", object, e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getInt(String key) throws JSONException {
/*  740 */     Object object = get(key);
/*  741 */     if (object instanceof Number) {
/*  742 */       return ((Number)object).intValue();
/*      */     }
/*      */     try {
/*  745 */       return Integer.parseInt(object.toString());
/*  746 */     } catch (Exception e) {
/*  747 */       throw wrongValueFormatException(key, "int", object, e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONArray getJSONArray(String key) throws JSONException {
/*  761 */     Object object = get(key);
/*  762 */     if (object instanceof JSONArray) {
/*  763 */       return (JSONArray)object;
/*      */     }
/*  765 */     throw wrongValueFormatException(key, "JSONArray", object, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject getJSONObject(String key) throws JSONException {
/*  778 */     Object object = get(key);
/*  779 */     if (object instanceof JSONObject) {
/*  780 */       return (JSONObject)object;
/*      */     }
/*  782 */     throw wrongValueFormatException(key, "JSONObject", object, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLong(String key) throws JSONException {
/*  796 */     Object object = get(key);
/*  797 */     if (object instanceof Number) {
/*  798 */       return ((Number)object).longValue();
/*      */     }
/*      */     try {
/*  801 */       return Long.parseLong(object.toString());
/*  802 */     } catch (Exception e) {
/*  803 */       throw wrongValueFormatException(key, "long", object, e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] getNames(JSONObject jo) {
/*  815 */     if (jo.isEmpty()) {
/*  816 */       return null;
/*      */     }
/*  818 */     return jo.keySet().<String>toArray(new String[jo.length()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] getNames(Object object) {
/*  829 */     if (object == null) {
/*  830 */       return null;
/*      */     }
/*  832 */     Class<?> klass = object.getClass();
/*  833 */     Field[] fields = klass.getFields();
/*  834 */     int length = fields.length;
/*  835 */     if (length == 0) {
/*  836 */       return null;
/*      */     }
/*  838 */     String[] names = new String[length];
/*  839 */     for (int i = 0; i < length; i++) {
/*  840 */       names[i] = fields[i].getName();
/*      */     }
/*  842 */     return names;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getString(String key) throws JSONException {
/*  855 */     Object object = get(key);
/*  856 */     if (object instanceof String) {
/*  857 */       return (String)object;
/*      */     }
/*  859 */     throw wrongValueFormatException(key, "string", object, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean has(String key) {
/*  870 */     return this.map.containsKey(key);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject increment(String key) throws JSONException {
/*  889 */     Object value = opt(key);
/*  890 */     if (value == null) {
/*  891 */       put(key, 1);
/*  892 */     } else if (value instanceof Integer) {
/*  893 */       put(key, ((Integer)value).intValue() + 1);
/*  894 */     } else if (value instanceof Long) {
/*  895 */       put(key, ((Long)value).longValue() + 1L);
/*  896 */     } else if (value instanceof BigInteger) {
/*  897 */       put(key, ((BigInteger)value).add(BigInteger.ONE));
/*  898 */     } else if (value instanceof Float) {
/*  899 */       put(key, ((Float)value).floatValue() + 1.0F);
/*  900 */     } else if (value instanceof Double) {
/*  901 */       put(key, ((Double)value).doubleValue() + 1.0D);
/*  902 */     } else if (value instanceof BigDecimal) {
/*  903 */       put(key, ((BigDecimal)value).add(BigDecimal.ONE));
/*      */     } else {
/*  905 */       throw new JSONException("Unable to increment [" + quote(key) + "].");
/*      */     } 
/*  907 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNull(String key) {
/*  920 */     return NULL.equals(opt(key));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterator<String> keys() {
/*  932 */     return keySet().iterator();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<String> keySet() {
/*  944 */     return this.map.keySet();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Set<Map.Entry<String, Object>> entrySet() {
/*  960 */     return this.map.entrySet();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int length() {
/*  969 */     return this.map.size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clear() {
/*  977 */     this.map.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEmpty() {
/*  986 */     return this.map.isEmpty();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONArray names() {
/*  997 */     if (this.map.isEmpty()) {
/*  998 */       return null;
/*      */     }
/* 1000 */     return new JSONArray(this.map.keySet());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String numberToString(Number number) throws JSONException {
/* 1013 */     if (number == null) {
/* 1014 */       throw new JSONException("Null pointer");
/*      */     }
/* 1016 */     testValidity(number);
/*      */ 
/*      */ 
/*      */     
/* 1020 */     String string = number.toString();
/* 1021 */     if (string.indexOf('.') > 0 && string.indexOf('e') < 0 && string
/* 1022 */       .indexOf('E') < 0) {
/* 1023 */       while (string.endsWith("0")) {
/* 1024 */         string = string.substring(0, string.length() - 1);
/*      */       }
/* 1026 */       if (string.endsWith(".")) {
/* 1027 */         string = string.substring(0, string.length() - 1);
/*      */       }
/*      */     } 
/* 1030 */     return string;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object opt(String key) {
/* 1041 */     return (key == null) ? null : this.map.get(key);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> E optEnum(Class<E> clazz, String key) {
/* 1056 */     return optEnum(clazz, key, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> E optEnum(Class<E> clazz, String key, E defaultValue) {
/*      */     try {
/* 1075 */       Object val = opt(key);
/* 1076 */       if (NULL.equals(val)) {
/* 1077 */         return defaultValue;
/*      */       }
/* 1079 */       if (clazz.isAssignableFrom(val.getClass()))
/*      */       {
/*      */         
/* 1082 */         return (E)val;
/*      */       }
/*      */       
/* 1085 */       return Enum.valueOf(clazz, val.toString());
/* 1086 */     } catch (IllegalArgumentException e) {
/* 1087 */       return defaultValue;
/* 1088 */     } catch (NullPointerException e) {
/* 1089 */       return defaultValue;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean optBoolean(String key) {
/* 1102 */     return optBoolean(key, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean optBoolean(String key, boolean defaultValue) {
/* 1117 */     Object val = opt(key);
/* 1118 */     if (NULL.equals(val)) {
/* 1119 */       return defaultValue;
/*      */     }
/* 1121 */     if (val instanceof Boolean) {
/* 1122 */       return ((Boolean)val).booleanValue();
/*      */     }
/*      */     
/*      */     try {
/* 1126 */       return getBoolean(key);
/* 1127 */     } catch (Exception e) {
/* 1128 */       return defaultValue;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Boolean optBooleanObject(String key) {
/* 1141 */     return optBooleanObject(key, Boolean.valueOf(false));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Boolean optBooleanObject(String key, Boolean defaultValue) {
/* 1156 */     Object val = opt(key);
/* 1157 */     if (NULL.equals(val)) {
/* 1158 */       return defaultValue;
/*      */     }
/* 1160 */     if (val instanceof Boolean) {
/* 1161 */       return Boolean.valueOf(((Boolean)val).booleanValue());
/*      */     }
/*      */     
/*      */     try {
/* 1165 */       return Boolean.valueOf(getBoolean(key));
/* 1166 */     } catch (Exception e) {
/* 1167 */       return defaultValue;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BigDecimal optBigDecimal(String key, BigDecimal defaultValue) {
/* 1186 */     Object val = opt(key);
/* 1187 */     return objectToBigDecimal(val, defaultValue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static BigDecimal objectToBigDecimal(Object val, BigDecimal defaultValue) {
/* 1197 */     return objectToBigDecimal(val, defaultValue, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static BigDecimal objectToBigDecimal(Object val, BigDecimal defaultValue, boolean exact) {
/* 1209 */     if (NULL.equals(val)) {
/* 1210 */       return defaultValue;
/*      */     }
/* 1212 */     if (val instanceof BigDecimal) {
/* 1213 */       return (BigDecimal)val;
/*      */     }
/* 1215 */     if (val instanceof BigInteger) {
/* 1216 */       return new BigDecimal((BigInteger)val);
/*      */     }
/* 1218 */     if (val instanceof Double || val instanceof Float) {
/* 1219 */       if (!numberIsFinite((Number)val)) {
/* 1220 */         return defaultValue;
/*      */       }
/* 1222 */       if (exact) {
/* 1223 */         return new BigDecimal(((Number)val).doubleValue());
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1228 */       return new BigDecimal(val.toString());
/*      */     } 
/* 1230 */     if (val instanceof Long || val instanceof Integer || val instanceof Short || val instanceof Byte)
/*      */     {
/* 1232 */       return new BigDecimal(((Number)val).longValue());
/*      */     }
/*      */     
/*      */     try {
/* 1236 */       return new BigDecimal(val.toString());
/* 1237 */     } catch (Exception e) {
/* 1238 */       return defaultValue;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BigInteger optBigInteger(String key, BigInteger defaultValue) {
/* 1254 */     Object val = opt(key);
/* 1255 */     return objectToBigInteger(val, defaultValue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static BigInteger objectToBigInteger(Object val, BigInteger defaultValue) {
/* 1265 */     if (NULL.equals(val)) {
/* 1266 */       return defaultValue;
/*      */     }
/* 1268 */     if (val instanceof BigInteger) {
/* 1269 */       return (BigInteger)val;
/*      */     }
/* 1271 */     if (val instanceof BigDecimal) {
/* 1272 */       return ((BigDecimal)val).toBigInteger();
/*      */     }
/* 1274 */     if (val instanceof Double || val instanceof Float) {
/* 1275 */       if (!numberIsFinite((Number)val)) {
/* 1276 */         return defaultValue;
/*      */       }
/* 1278 */       return (new BigDecimal(((Number)val).doubleValue())).toBigInteger();
/*      */     } 
/* 1280 */     if (val instanceof Long || val instanceof Integer || val instanceof Short || val instanceof Byte)
/*      */     {
/* 1282 */       return BigInteger.valueOf(((Number)val).longValue());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1291 */       String valStr = val.toString();
/* 1292 */       if (isDecimalNotation(valStr)) {
/* 1293 */         return (new BigDecimal(valStr)).toBigInteger();
/*      */       }
/* 1295 */       return new BigInteger(valStr);
/* 1296 */     } catch (Exception e) {
/* 1297 */       return defaultValue;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double optDouble(String key) {
/* 1311 */     return optDouble(key, Double.NaN);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double optDouble(String key, double defaultValue) {
/* 1326 */     Number val = optNumber(key);
/* 1327 */     if (val == null) {
/* 1328 */       return defaultValue;
/*      */     }
/* 1330 */     return val.doubleValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Double optDoubleObject(String key) {
/* 1343 */     return optDoubleObject(key, Double.valueOf(Double.NaN));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Double optDoubleObject(String key, Double defaultValue) {
/* 1358 */     Number val = optNumber(key);
/* 1359 */     if (val == null) {
/* 1360 */       return defaultValue;
/*      */     }
/* 1362 */     return Double.valueOf(val.doubleValue());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float optFloat(String key) {
/* 1375 */     return optFloat(key, Float.NaN);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float optFloat(String key, float defaultValue) {
/* 1390 */     Number val = optNumber(key);
/* 1391 */     if (val == null) {
/* 1392 */       return defaultValue;
/*      */     }
/* 1394 */     float floatValue = val.floatValue();
/*      */ 
/*      */ 
/*      */     
/* 1398 */     return floatValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Float optFloatObject(String key) {
/* 1411 */     return optFloatObject(key, Float.valueOf(Float.NaN));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Float optFloatObject(String key, Float defaultValue) {
/* 1426 */     Number val = optNumber(key);
/* 1427 */     if (val == null) {
/* 1428 */       return defaultValue;
/*      */     }
/* 1430 */     Float floatValue = Float.valueOf(val.floatValue());
/*      */ 
/*      */ 
/*      */     
/* 1434 */     return floatValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int optInt(String key) {
/* 1447 */     return optInt(key, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int optInt(String key, int defaultValue) {
/* 1462 */     Number val = optNumber(key, null);
/* 1463 */     if (val == null) {
/* 1464 */       return defaultValue;
/*      */     }
/* 1466 */     return val.intValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Integer optIntegerObject(String key) {
/* 1479 */     return optIntegerObject(key, Integer.valueOf(0));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Integer optIntegerObject(String key, Integer defaultValue) {
/* 1494 */     Number val = optNumber(key, null);
/* 1495 */     if (val == null) {
/* 1496 */       return defaultValue;
/*      */     }
/* 1498 */     return Integer.valueOf(val.intValue());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONArray optJSONArray(String key) {
/* 1510 */     return optJSONArray(key, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONArray optJSONArray(String key, JSONArray defaultValue) {
/* 1524 */     Object object = opt(key);
/* 1525 */     return (object instanceof JSONArray) ? (JSONArray)object : defaultValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject optJSONObject(String key) {
/* 1536 */     return optJSONObject(key, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject optJSONObject(String key, JSONObject defaultValue) {
/* 1549 */     Object object = opt(key);
/* 1550 */     return (object instanceof JSONObject) ? (JSONObject)object : defaultValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long optLong(String key) {
/* 1563 */     return optLong(key, 0L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long optLong(String key, long defaultValue) {
/* 1578 */     Number val = optNumber(key, null);
/* 1579 */     if (val == null) {
/* 1580 */       return defaultValue;
/*      */     }
/*      */     
/* 1583 */     return val.longValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Long optLongObject(String key) {
/* 1596 */     return optLongObject(key, Long.valueOf(0L));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Long optLongObject(String key, Long defaultValue) {
/* 1611 */     Number val = optNumber(key, null);
/* 1612 */     if (val == null) {
/* 1613 */       return defaultValue;
/*      */     }
/*      */     
/* 1616 */     return Long.valueOf(val.longValue());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Number optNumber(String key) {
/* 1630 */     return optNumber(key, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Number optNumber(String key, Number defaultValue) {
/* 1646 */     Object val = opt(key);
/* 1647 */     if (NULL.equals(val)) {
/* 1648 */       return defaultValue;
/*      */     }
/* 1650 */     if (val instanceof Number) {
/* 1651 */       return (Number)val;
/*      */     }
/*      */     
/*      */     try {
/* 1655 */       return stringToNumber(val.toString());
/* 1656 */     } catch (Exception e) {
/* 1657 */       return defaultValue;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String optString(String key) {
/* 1671 */     return optString(key, "");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String optString(String key, String defaultValue) {
/* 1685 */     Object object = opt(key);
/* 1686 */     return NULL.equals(object) ? defaultValue : object.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void populateMap(Object bean) {
/* 1701 */     populateMap(bean, Collections.newSetFromMap(new IdentityHashMap<>()));
/*      */   }
/*      */   
/*      */   private void populateMap(Object bean, Set<Object> objectsRecord) {
/* 1705 */     Class<?> klass = bean.getClass();
/*      */ 
/*      */ 
/*      */     
/* 1709 */     boolean includeSuperClass = (klass.getClassLoader() != null);
/*      */     
/* 1711 */     Method[] methods = includeSuperClass ? klass.getMethods() : klass.getDeclaredMethods();
/* 1712 */     for (Method method : methods) {
/* 1713 */       int modifiers = method.getModifiers();
/* 1714 */       if (Modifier.isPublic(modifiers) && 
/* 1715 */         !Modifier.isStatic(modifiers) && (method
/* 1716 */         .getParameterTypes()).length == 0 && 
/* 1717 */         !method.isBridge() && method
/* 1718 */         .getReturnType() != void.class && 
/* 1719 */         isValidMethodName(method.getName())) {
/* 1720 */         String key = getKeyNameFromMethod(method);
/* 1721 */         if (key != null && !key.isEmpty()) {
/*      */           
/* 1723 */           try { Object result = method.invoke(bean, new Object[0]);
/* 1724 */             if (result != null)
/*      */             {
/*      */ 
/*      */               
/* 1728 */               if (objectsRecord.contains(result)) {
/* 1729 */                 throw recursivelyDefinedObjectException(key);
/*      */               }
/*      */               
/* 1732 */               objectsRecord.add(result);
/*      */               
/* 1734 */               testValidity(result);
/* 1735 */               this.map.put(key, wrap(result, objectsRecord));
/*      */               
/* 1737 */               objectsRecord.remove(result);
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1742 */               if (result instanceof Closeable) {
/*      */                 try {
/* 1744 */                   ((Closeable)result).close();
/* 1745 */                 } catch (IOException iOException) {}
/*      */               }
/*      */             }
/*      */              }
/* 1749 */           catch (IllegalAccessException illegalAccessException) {  }
/* 1750 */           catch (IllegalArgumentException illegalArgumentException) {  }
/* 1751 */           catch (InvocationTargetException invocationTargetException) {}
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean isValidMethodName(String name) {
/* 1759 */     return (!"getClass".equals(name) && !"getDeclaringClass".equals(name));
/*      */   }
/*      */   private static String getKeyNameFromMethod(Method method) {
/*      */     String key;
/* 1763 */     int ignoreDepth = getAnnotationDepth(method, (Class)JSONPropertyIgnore.class);
/* 1764 */     if (ignoreDepth > 0) {
/* 1765 */       int forcedNameDepth = getAnnotationDepth(method, (Class)JSONPropertyName.class);
/* 1766 */       if (forcedNameDepth < 0 || ignoreDepth <= forcedNameDepth)
/*      */       {
/*      */         
/* 1769 */         return null;
/*      */       }
/*      */     } 
/* 1772 */     JSONPropertyName annotation = getAnnotation(method, JSONPropertyName.class);
/* 1773 */     if (annotation != null && annotation.value() != null && !annotation.value().isEmpty()) {
/* 1774 */       return annotation.value();
/*      */     }
/*      */     
/* 1777 */     String name = method.getName();
/* 1778 */     if (name.startsWith("get") && name.length() > 3) {
/* 1779 */       key = name.substring(3);
/* 1780 */     } else if (name.startsWith("is") && name.length() > 2) {
/* 1781 */       key = name.substring(2);
/*      */     } else {
/* 1783 */       return null;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1788 */     if (key.length() == 0 || Character.isLowerCase(key.charAt(0))) {
/* 1789 */       return null;
/*      */     }
/* 1791 */     if (key.length() == 1) {
/* 1792 */       key = key.toLowerCase(Locale.ROOT);
/* 1793 */     } else if (!Character.isUpperCase(key.charAt(1))) {
/* 1794 */       key = key.substring(0, 1).toLowerCase(Locale.ROOT) + key.substring(1);
/*      */     } 
/* 1796 */     return key;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <A extends Annotation> A getAnnotation(Method m, Class<A> annotationClass) {
/* 1815 */     if (m == null || annotationClass == null) {
/* 1816 */       return null;
/*      */     }
/*      */     
/* 1819 */     if (m.isAnnotationPresent(annotationClass)) {
/* 1820 */       return m.getAnnotation(annotationClass);
/*      */     }
/*      */ 
/*      */     
/* 1824 */     Class<?> c = m.getDeclaringClass();
/* 1825 */     if (c.getSuperclass() == null) {
/* 1826 */       return null;
/*      */     }
/*      */ 
/*      */     
/* 1830 */     for (Class<?> i : c.getInterfaces()) {
/*      */       try {
/* 1832 */         Method im = i.getMethod(m.getName(), m.getParameterTypes());
/* 1833 */         return getAnnotation(im, annotationClass);
/* 1834 */       } catch (SecurityException ex) {
/*      */       
/* 1836 */       } catch (NoSuchMethodException ex) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1842 */       return getAnnotation(c
/* 1843 */           .getSuperclass().getMethod(m.getName(), m.getParameterTypes()), annotationClass);
/*      */     }
/* 1845 */     catch (SecurityException ex) {
/* 1846 */       return null;
/* 1847 */     } catch (NoSuchMethodException ex) {
/* 1848 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int getAnnotationDepth(Method m, Class<? extends Annotation> annotationClass) {
/* 1865 */     if (m == null || annotationClass == null) {
/* 1866 */       return -1;
/*      */     }
/*      */     
/* 1869 */     if (m.isAnnotationPresent(annotationClass)) {
/* 1870 */       return 1;
/*      */     }
/*      */ 
/*      */     
/* 1874 */     Class<?> c = m.getDeclaringClass();
/* 1875 */     if (c.getSuperclass() == null) {
/* 1876 */       return -1;
/*      */     }
/*      */ 
/*      */     
/* 1880 */     for (Class<?> i : c.getInterfaces()) {
/*      */       try {
/* 1882 */         Method im = i.getMethod(m.getName(), m.getParameterTypes());
/* 1883 */         int d = getAnnotationDepth(im, annotationClass);
/* 1884 */         if (d > 0)
/*      */         {
/* 1886 */           return d + 1;
/*      */         }
/* 1888 */       } catch (SecurityException ex) {
/*      */       
/* 1890 */       } catch (NoSuchMethodException ex) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1896 */       int d = getAnnotationDepth(c
/* 1897 */           .getSuperclass().getMethod(m.getName(), m.getParameterTypes()), annotationClass);
/*      */       
/* 1899 */       if (d > 0)
/*      */       {
/* 1901 */         return d + 1;
/*      */       }
/* 1903 */       return -1;
/* 1904 */     } catch (SecurityException ex) {
/* 1905 */       return -1;
/* 1906 */     } catch (NoSuchMethodException ex) {
/* 1907 */       return -1;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject put(String key, boolean value) throws JSONException {
/* 1925 */     return put(key, value ? Boolean.TRUE : Boolean.FALSE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject put(String key, Collection<?> value) throws JSONException {
/* 1943 */     return put(key, new JSONArray(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject put(String key, double value) throws JSONException {
/* 1960 */     return put(key, Double.valueOf(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject put(String key, float value) throws JSONException {
/* 1977 */     return put(key, Float.valueOf(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject put(String key, int value) throws JSONException {
/* 1994 */     return put(key, Integer.valueOf(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject put(String key, long value) throws JSONException {
/* 2011 */     return put(key, Long.valueOf(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject put(String key, Map<?, ?> value) throws JSONException {
/* 2029 */     return put(key, new JSONObject(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject put(String key, Object value) throws JSONException {
/* 2049 */     if (key == null) {
/* 2050 */       throw new NullPointerException("Null key.");
/*      */     }
/* 2052 */     if (value != null) {
/* 2053 */       testValidity(value);
/* 2054 */       this.map.put(key, value);
/*      */     } else {
/* 2056 */       remove(key);
/*      */     } 
/* 2058 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject putOnce(String key, Object value) throws JSONException {
/* 2075 */     if (key != null && value != null) {
/* 2076 */       if (opt(key) != null) {
/* 2077 */         throw new JSONException("Duplicate key \"" + key + "\"");
/*      */       }
/* 2079 */       return put(key, value);
/*      */     } 
/* 2081 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject putOpt(String key, Object value) throws JSONException {
/* 2099 */     if (key != null && value != null) {
/* 2100 */       return put(key, value);
/*      */     }
/* 2102 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object query(String jsonPointer) {
/* 2125 */     return query(new JSONPointer(jsonPointer));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object query(JSONPointer jsonPointer) {
/* 2147 */     return jsonPointer.queryFrom(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object optQuery(String jsonPointer) {
/* 2159 */     return optQuery(new JSONPointer(jsonPointer));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object optQuery(JSONPointer jsonPointer) {
/*      */     try {
/* 2172 */       return jsonPointer.queryFrom(this);
/* 2173 */     } catch (JSONPointerException e) {
/* 2174 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String quote(String string) {
/* 2191 */     StringWriter sw = new StringWriter();
/*      */     try {
/* 2193 */       return quote(string, sw).toString();
/* 2194 */     } catch (IOException ignored) {
/*      */       
/* 2196 */       return "";
/*      */     } 
/*      */   }
/*      */   
/*      */   public static Writer quote(String string, Writer w) throws IOException {
/* 2201 */     if (string == null || string.isEmpty()) {
/* 2202 */       w.write("\"\"");
/* 2203 */       return w;
/*      */     } 
/*      */ 
/*      */     
/* 2207 */     char c = Character.MIN_VALUE;
/*      */ 
/*      */     
/* 2210 */     int len = string.length();
/*      */     
/* 2212 */     w.write(34);
/* 2213 */     for (int i = 0; i < len; i++) {
/* 2214 */       char b = c;
/* 2215 */       c = string.charAt(i);
/* 2216 */       switch (c) {
/*      */         case '"':
/*      */         case '\\':
/* 2219 */           w.write(92);
/* 2220 */           w.write(c);
/*      */           break;
/*      */         case '/':
/* 2223 */           if (b == '<') {
/* 2224 */             w.write(92);
/*      */           }
/* 2226 */           w.write(c);
/*      */           break;
/*      */         case '\b':
/* 2229 */           w.write("\\b");
/*      */           break;
/*      */         case '\t':
/* 2232 */           w.write("\\t");
/*      */           break;
/*      */         case '\n':
/* 2235 */           w.write("\\n");
/*      */           break;
/*      */         case '\f':
/* 2238 */           w.write("\\f");
/*      */           break;
/*      */         case '\r':
/* 2241 */           w.write("\\r");
/*      */           break;
/*      */         default:
/* 2244 */           if (c < ' ' || (c >= '' && c < ' ') || (c >= ' ' && c < '℀')) {
/*      */             
/* 2246 */             w.write("\\u");
/* 2247 */             String hhhh = Integer.toHexString(c);
/* 2248 */             w.write("0000", 0, 4 - hhhh.length());
/* 2249 */             w.write(hhhh); break;
/*      */           } 
/* 2251 */           w.write(c);
/*      */           break;
/*      */       } 
/*      */     } 
/* 2255 */     w.write(34);
/* 2256 */     return w;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object remove(String key) {
/* 2268 */     return this.map.remove(key);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean similar(Object other) {
/*      */     try {
/* 2281 */       if (!(other instanceof JSONObject)) {
/* 2282 */         return false;
/*      */       }
/* 2284 */       if (!keySet().equals(((JSONObject)other).keySet())) {
/* 2285 */         return false;
/*      */       }
/* 2287 */       for (Map.Entry<String, ?> entry : entrySet()) {
/* 2288 */         String name = entry.getKey();
/* 2289 */         Object valueThis = entry.getValue();
/* 2290 */         Object valueOther = ((JSONObject)other).get(name);
/* 2291 */         if (valueThis == valueOther) {
/*      */           continue;
/*      */         }
/* 2294 */         if (valueThis == null) {
/* 2295 */           return false;
/*      */         }
/* 2297 */         if (valueThis instanceof JSONObject) {
/* 2298 */           if (!((JSONObject)valueThis).similar(valueOther))
/* 2299 */             return false;  continue;
/*      */         } 
/* 2301 */         if (valueThis instanceof JSONArray) {
/* 2302 */           if (!((JSONArray)valueThis).similar(valueOther))
/* 2303 */             return false;  continue;
/*      */         } 
/* 2305 */         if (valueThis instanceof Number && valueOther instanceof Number) {
/* 2306 */           if (!isNumberSimilar((Number)valueThis, (Number)valueOther))
/* 2307 */             return false;  continue;
/*      */         } 
/* 2309 */         if (valueThis instanceof JSONString && valueOther instanceof JSONString) {
/* 2310 */           if (!((JSONString)valueThis).toJSONString().equals(((JSONString)valueOther).toJSONString()))
/* 2311 */             return false;  continue;
/*      */         } 
/* 2313 */         if (!valueThis.equals(valueOther)) {
/* 2314 */           return false;
/*      */         }
/*      */       } 
/* 2317 */       return true;
/* 2318 */     } catch (Throwable exception) {
/* 2319 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean isNumberSimilar(Number l, Number r) {
/* 2339 */     if (!numberIsFinite(l) || !numberIsFinite(r))
/*      */     {
/* 2341 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 2346 */     if (l.getClass().equals(r.getClass()) && l instanceof Comparable) {
/*      */       
/* 2348 */       int compareTo = ((Comparable<Number>)l).compareTo(r);
/* 2349 */       return (compareTo == 0);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2355 */     BigDecimal lBigDecimal = objectToBigDecimal(l, null, false);
/* 2356 */     BigDecimal rBigDecimal = objectToBigDecimal(r, null, false);
/* 2357 */     if (lBigDecimal == null || rBigDecimal == null) {
/* 2358 */       return false;
/*      */     }
/* 2360 */     return (lBigDecimal.compareTo(rBigDecimal) == 0);
/*      */   }
/*      */   
/*      */   private static boolean numberIsFinite(Number n) {
/* 2364 */     if (n instanceof Double && (((Double)n).isInfinite() || ((Double)n).isNaN()))
/* 2365 */       return false; 
/* 2366 */     if (n instanceof Float && (((Float)n).isInfinite() || ((Float)n).isNaN())) {
/* 2367 */       return false;
/*      */     }
/* 2369 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static boolean isDecimalNotation(String val) {
/* 2379 */     return (val.indexOf('.') > -1 || val.indexOf('e') > -1 || val
/* 2380 */       .indexOf('E') > -1 || "-0".equals(val));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static Number stringToNumber(String input) throws NumberFormatException {
/* 2394 */     String val = input;
/* 2395 */     if (val.startsWith(".")) {
/* 2396 */       val = "0" + val;
/*      */     }
/* 2398 */     if (val.startsWith("-.")) {
/* 2399 */       val = "-0." + val.substring(2);
/*      */     }
/* 2401 */     char initial = val.charAt(0);
/* 2402 */     if ((initial >= '0' && initial <= '9') || initial == '-') {
/*      */       
/* 2404 */       if (isDecimalNotation(val)) {
/*      */         
/*      */         try {
/*      */ 
/*      */           
/* 2409 */           BigDecimal bd = new BigDecimal(val);
/* 2410 */           if (initial == '-' && BigDecimal.ZERO.compareTo(bd) == 0) {
/* 2411 */             return Double.valueOf(-0.0D);
/*      */           }
/* 2413 */           return bd;
/* 2414 */         } catch (NumberFormatException retryAsDouble) {
/*      */           
/*      */           try {
/* 2417 */             Double d = Double.valueOf(val);
/* 2418 */             if (d.isNaN() || d.isInfinite()) {
/* 2419 */               throw new NumberFormatException("val [" + input + "] is not a valid number.");
/*      */             }
/* 2421 */             return d;
/* 2422 */           } catch (NumberFormatException ignore) {
/* 2423 */             throw new NumberFormatException("val [" + input + "] is not a valid number.");
/*      */           } 
/*      */         } 
/*      */       }
/* 2427 */       val = removeLeadingZerosOfNumber(input);
/* 2428 */       initial = val.charAt(0);
/* 2429 */       if (initial == '0' && val.length() > 1) {
/* 2430 */         char at1 = val.charAt(1);
/* 2431 */         if (at1 >= '0' && at1 <= '9') {
/* 2432 */           throw new NumberFormatException("val [" + input + "] is not a valid number.");
/*      */         }
/* 2434 */       } else if (initial == '-' && val.length() > 2) {
/* 2435 */         char at1 = val.charAt(1);
/* 2436 */         char at2 = val.charAt(2);
/* 2437 */         if (at1 == '0' && at2 >= '0' && at2 <= '9') {
/* 2438 */           throw new NumberFormatException("val [" + input + "] is not a valid number.");
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2449 */       BigInteger bi = new BigInteger(val);
/* 2450 */       if (bi.bitLength() <= 31) {
/* 2451 */         return Integer.valueOf(bi.intValue());
/*      */       }
/* 2453 */       if (bi.bitLength() <= 63) {
/* 2454 */         return Long.valueOf(bi.longValue());
/*      */       }
/* 2456 */       return bi;
/*      */     } 
/* 2458 */     throw new NumberFormatException("val [" + input + "] is not a valid number.");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object stringToValue(String string) {
/* 2474 */     if ("".equals(string)) {
/* 2475 */       return string;
/*      */     }
/*      */ 
/*      */     
/* 2479 */     if ("true".equalsIgnoreCase(string)) {
/* 2480 */       return Boolean.TRUE;
/*      */     }
/* 2482 */     if ("false".equalsIgnoreCase(string)) {
/* 2483 */       return Boolean.FALSE;
/*      */     }
/* 2485 */     if ("null".equalsIgnoreCase(string)) {
/* 2486 */       return NULL;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2494 */     if (potentialNumber(string)) {
/*      */       try {
/* 2496 */         return stringToNumber(string);
/* 2497 */       } catch (Exception exception) {}
/*      */     }
/*      */     
/* 2500 */     return string;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean potentialNumber(String value) {
/* 2505 */     if (value == null || value.isEmpty()) {
/* 2506 */       return false;
/*      */     }
/* 2508 */     return potentialPositiveNumberStartingAtIndex(value, (value.charAt(0) == '-') ? 1 : 0);
/*      */   }
/*      */   
/*      */   private static boolean potentialPositiveNumberStartingAtIndex(String value, int index) {
/* 2512 */     if (index >= value.length()) {
/* 2513 */       return false;
/*      */     }
/* 2515 */     return digitAtIndex(value, (value.charAt(index) == '.') ? (index + 1) : index);
/*      */   }
/*      */   
/*      */   private static boolean digitAtIndex(String value, int index) {
/* 2519 */     if (index >= value.length()) {
/* 2520 */       return false;
/*      */     }
/* 2522 */     return (value.charAt(index) >= '0' && value.charAt(index) <= '9');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void testValidity(Object o) throws JSONException {
/* 2534 */     if (o instanceof Number && !numberIsFinite((Number)o)) {
/* 2535 */       throw new JSONException("JSON does not allow non-finite numbers.");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONArray toJSONArray(JSONArray names) throws JSONException {
/* 2551 */     if (names == null || names.isEmpty()) {
/* 2552 */       return null;
/*      */     }
/* 2554 */     JSONArray ja = new JSONArray();
/* 2555 */     for (int i = 0; i < names.length(); i++) {
/* 2556 */       ja.put(opt(names.getString(i)));
/*      */     }
/* 2558 */     return ja;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*      */     try {
/* 2577 */       return toString(0);
/* 2578 */     } catch (Exception e) {
/* 2579 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString(int indentFactor) throws JSONException {
/* 2611 */     StringWriter w = new StringWriter();
/* 2612 */     return write(w, indentFactor, 0).toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String valueToString(Object value) throws JSONException {
/* 2644 */     return JSONWriter.valueToString(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object wrap(Object object) {
/* 2660 */     return wrap(object, null);
/*      */   }
/*      */   
/*      */   private static Object wrap(Object object, Set<Object> objectsRecord) {
/*      */     try {
/* 2665 */       if (NULL.equals(object)) {
/* 2666 */         return NULL;
/*      */       }
/* 2668 */       if (object instanceof JSONObject || object instanceof JSONArray || NULL
/* 2669 */         .equals(object) || object instanceof JSONString || object instanceof Byte || object instanceof Character || object instanceof Short || object instanceof Integer || object instanceof Long || object instanceof Boolean || object instanceof Float || object instanceof Double || object instanceof String || object instanceof BigInteger || object instanceof BigDecimal || object instanceof Enum)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2676 */         return object;
/*      */       }
/*      */       
/* 2679 */       if (object instanceof Collection) {
/* 2680 */         Collection<?> coll = (Collection)object;
/* 2681 */         return new JSONArray(coll);
/*      */       } 
/* 2683 */       if (object.getClass().isArray()) {
/* 2684 */         return new JSONArray(object);
/*      */       }
/* 2686 */       if (object instanceof Map) {
/* 2687 */         Map<?, ?> map = (Map<?, ?>)object;
/* 2688 */         return new JSONObject(map);
/*      */       } 
/* 2690 */       Package objectPackage = object.getClass().getPackage();
/*      */       
/* 2692 */       String objectPackageName = (objectPackage != null) ? objectPackage.getName() : "";
/* 2693 */       if (objectPackageName.startsWith("java.") || objectPackageName
/* 2694 */         .startsWith("javax.") || object
/* 2695 */         .getClass().getClassLoader() == null) {
/* 2696 */         return object.toString();
/*      */       }
/* 2698 */       if (objectsRecord != null) {
/* 2699 */         return new JSONObject(object, objectsRecord);
/*      */       }
/* 2701 */       return new JSONObject(object);
/*      */     }
/* 2703 */     catch (JSONException exception) {
/* 2704 */       throw exception;
/* 2705 */     } catch (Exception exception) {
/* 2706 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Writer write(Writer writer) throws JSONException {
/* 2721 */     return write(writer, 0, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final Writer writeValue(Writer writer, Object value, int indentFactor, int indent) throws JSONException, IOException {
/* 2727 */     if (value == null || value.equals(null)) {
/* 2728 */       writer.write("null");
/* 2729 */     } else if (value instanceof JSONString) {
/*      */       Object o;
/*      */       try {
/* 2732 */         o = ((JSONString)value).toJSONString();
/* 2733 */       } catch (Exception e) {
/* 2734 */         throw new JSONException(e);
/*      */       } 
/* 2736 */       writer.write((o != null) ? o.toString() : quote(value.toString()));
/* 2737 */     } else if (value instanceof Number) {
/*      */       
/* 2739 */       String numberAsString = numberToString((Number)value);
/* 2740 */       if (NUMBER_PATTERN.matcher(numberAsString).matches()) {
/* 2741 */         writer.write(numberAsString);
/*      */       }
/*      */       else {
/*      */         
/* 2745 */         quote(numberAsString, writer);
/*      */       } 
/* 2747 */     } else if (value instanceof Boolean) {
/* 2748 */       writer.write(value.toString());
/* 2749 */     } else if (value instanceof Enum) {
/* 2750 */       writer.write(quote(((Enum)value).name()));
/* 2751 */     } else if (value instanceof JSONObject) {
/* 2752 */       ((JSONObject)value).write(writer, indentFactor, indent);
/* 2753 */     } else if (value instanceof JSONArray) {
/* 2754 */       ((JSONArray)value).write(writer, indentFactor, indent);
/* 2755 */     } else if (value instanceof Map) {
/* 2756 */       Map<?, ?> map = (Map<?, ?>)value;
/* 2757 */       (new JSONObject(map)).write(writer, indentFactor, indent);
/* 2758 */     } else if (value instanceof Collection) {
/* 2759 */       Collection<?> coll = (Collection)value;
/* 2760 */       (new JSONArray(coll)).write(writer, indentFactor, indent);
/* 2761 */     } else if (value.getClass().isArray()) {
/* 2762 */       (new JSONArray(value)).write(writer, indentFactor, indent);
/*      */     } else {
/* 2764 */       quote(value.toString(), writer);
/*      */     } 
/* 2766 */     return writer;
/*      */   }
/*      */   
/*      */   static final void indent(Writer writer, int indent) throws IOException {
/* 2770 */     for (int i = 0; i < indent; i++) {
/* 2771 */       writer.write(32);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Writer write(Writer writer, int indentFactor, int indent) throws JSONException {
/*      */     try {
/* 2806 */       boolean needsComma = false;
/* 2807 */       int length = length();
/* 2808 */       writer.write(123);
/*      */       
/* 2810 */       if (length == 1) {
/* 2811 */         Map.Entry<String, ?> entry = entrySet().iterator().next();
/* 2812 */         String key = entry.getKey();
/* 2813 */         writer.write(quote(key));
/* 2814 */         writer.write(58);
/* 2815 */         if (indentFactor > 0) {
/* 2816 */           writer.write(32);
/*      */         }
/*      */         try {
/* 2819 */           writeValue(writer, entry.getValue(), indentFactor, indent);
/* 2820 */         } catch (Exception e) {
/* 2821 */           throw new JSONException("Unable to write JSONObject value for key: " + key, e);
/*      */         } 
/* 2823 */       } else if (length != 0) {
/* 2824 */         int newIndent = indent + indentFactor;
/* 2825 */         for (Map.Entry<String, ?> entry : entrySet()) {
/* 2826 */           if (needsComma) {
/* 2827 */             writer.write(44);
/*      */           }
/* 2829 */           if (indentFactor > 0) {
/* 2830 */             writer.write(10);
/*      */           }
/* 2832 */           indent(writer, newIndent);
/* 2833 */           String key = entry.getKey();
/* 2834 */           writer.write(quote(key));
/* 2835 */           writer.write(58);
/* 2836 */           if (indentFactor > 0) {
/* 2837 */             writer.write(32);
/*      */           }
/*      */           try {
/* 2840 */             writeValue(writer, entry.getValue(), indentFactor, newIndent);
/* 2841 */           } catch (Exception e) {
/* 2842 */             throw new JSONException("Unable to write JSONObject value for key: " + key, e);
/*      */           } 
/* 2844 */           needsComma = true;
/*      */         } 
/* 2846 */         if (indentFactor > 0) {
/* 2847 */           writer.write(10);
/*      */         }
/* 2849 */         indent(writer, indent);
/*      */       } 
/* 2851 */       writer.write(125);
/* 2852 */       return writer;
/* 2853 */     } catch (IOException exception) {
/* 2854 */       throw new JSONException(exception);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, Object> toMap() {
/* 2868 */     Map<String, Object> results = new HashMap<>();
/* 2869 */     for (Map.Entry<String, Object> entry : entrySet()) {
/*      */       Object value;
/* 2871 */       if (entry.getValue() == null || NULL.equals(entry.getValue())) {
/* 2872 */         value = null;
/* 2873 */       } else if (entry.getValue() instanceof JSONObject) {
/* 2874 */         value = ((JSONObject)entry.getValue()).toMap();
/* 2875 */       } else if (entry.getValue() instanceof JSONArray) {
/* 2876 */         value = ((JSONArray)entry.getValue()).toList();
/*      */       } else {
/* 2878 */         value = entry.getValue();
/*      */       } 
/* 2880 */       results.put(entry.getKey(), value);
/*      */     } 
/* 2882 */     return results;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static JSONException wrongValueFormatException(String key, String valueType, Object value, Throwable cause) {
/* 2897 */     if (value == null)
/*      */     {
/* 2899 */       return new JSONException("JSONObject[" + 
/* 2900 */           quote(key) + "] is not a " + valueType + " (null).", cause);
/*      */     }
/*      */ 
/*      */     
/* 2904 */     if (value instanceof Map || value instanceof Iterable || value instanceof JSONObject) {
/* 2905 */       return new JSONException("JSONObject[" + 
/* 2906 */           quote(key) + "] is not a " + valueType + " (" + value.getClass() + ").", cause);
/*      */     }
/*      */     
/* 2909 */     return new JSONException("JSONObject[" + 
/* 2910 */         quote(key) + "] is not a " + valueType + " (" + value.getClass() + " : " + value + ").", cause);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static JSONException recursivelyDefinedObjectException(String key) {
/* 2920 */     return new JSONException("JavaBean object contains recursively defined member variable of key " + 
/* 2921 */         quote(key));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String removeLeadingZerosOfNumber(String value) {
/* 2931 */     if (value.equals("-")) return value; 
/* 2932 */     boolean negativeFirstChar = (value.charAt(0) == '-');
/* 2933 */     int counter = negativeFirstChar ? 1 : 0;
/* 2934 */     while (counter < value.length()) {
/* 2935 */       if (value.charAt(counter) != '0') {
/* 2936 */         if (negativeFirstChar) return "-".concat(value.substring(counter)); 
/* 2937 */         return value.substring(counter);
/*      */       } 
/* 2939 */       counter++;
/*      */     } 
/* 2941 */     if (negativeFirstChar) return "-0"; 
/* 2942 */     return "0";
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/json/JSONObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */