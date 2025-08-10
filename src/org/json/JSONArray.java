/*      */ package org.json;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.StringWriter;
/*      */ import java.io.Writer;
/*      */ import java.lang.reflect.Array;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class JSONArray
/*      */   implements Iterable<Object>
/*      */ {
/*      */   private final ArrayList<Object> myArrayList;
/*      */   
/*      */   public JSONArray() {
/*   75 */     this.myArrayList = new ArrayList();
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
/*      */   public JSONArray(JSONTokener x) throws JSONException {
/*   87 */     this();
/*   88 */     if (x.nextClean() != '[') {
/*   89 */       throw x.syntaxError("A JSONArray text must start with '['");
/*      */     }
/*      */     
/*   92 */     char nextChar = x.nextClean();
/*   93 */     if (nextChar == '\000')
/*      */     {
/*   95 */       throw x.syntaxError("Expected a ',' or ']'");
/*      */     }
/*   97 */     if (nextChar != ']') {
/*   98 */       x.back();
/*      */       while (true) {
/*  100 */         if (x.nextClean() == ',') {
/*  101 */           x.back();
/*  102 */           this.myArrayList.add(JSONObject.NULL);
/*      */         } else {
/*  104 */           x.back();
/*  105 */           this.myArrayList.add(x.nextValue());
/*      */         } 
/*  107 */         switch (x.nextClean()) {
/*      */           
/*      */           case '\000':
/*  110 */             throw x.syntaxError("Expected a ',' or ']'");
/*      */           case ',':
/*  112 */             nextChar = x.nextClean();
/*  113 */             if (nextChar == '\000')
/*      */             {
/*  115 */               throw x.syntaxError("Expected a ',' or ']'");
/*      */             }
/*  117 */             if (nextChar == ']') {
/*      */               return;
/*      */             }
/*  120 */             x.back(); continue;
/*      */           case ']':
/*      */             return;
/*      */         }  break;
/*      */       } 
/*  125 */       throw x.syntaxError("Expected a ',' or ']'");
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
/*      */   public JSONArray(String source) throws JSONException {
/*  142 */     this(new JSONTokener(source));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONArray(Collection<?> collection) {
/*  152 */     if (collection == null) {
/*  153 */       this.myArrayList = new ArrayList();
/*      */     } else {
/*  155 */       this.myArrayList = new ArrayList(collection.size());
/*  156 */       addAll(collection, true);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONArray(Iterable<?> iter) {
/*  167 */     this();
/*  168 */     if (iter == null) {
/*      */       return;
/*      */     }
/*  171 */     addAll(iter, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONArray(JSONArray array) {
/*  181 */     if (array == null) {
/*  182 */       this.myArrayList = new ArrayList();
/*      */     }
/*      */     else {
/*      */       
/*  186 */       this.myArrayList = new ArrayList(array.myArrayList);
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
/*      */   public JSONArray(Object array) throws JSONException {
/*  203 */     this();
/*  204 */     if (!array.getClass().isArray()) {
/*  205 */       throw new JSONException("JSONArray initial value should be a string or collection or array.");
/*      */     }
/*      */     
/*  208 */     addAll(array, true);
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
/*      */   public JSONArray(int initialCapacity) throws JSONException {
/*  220 */     if (initialCapacity < 0) {
/*  221 */       throw new JSONException("JSONArray initial capacity cannot be negative.");
/*      */     }
/*      */     
/*  224 */     this.myArrayList = new ArrayList(initialCapacity);
/*      */   }
/*      */ 
/*      */   
/*      */   public Iterator<Object> iterator() {
/*  229 */     return this.myArrayList.iterator();
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
/*      */   public Object get(int index) throws JSONException {
/*  242 */     Object object = opt(index);
/*  243 */     if (object == null) {
/*  244 */       throw new JSONException("JSONArray[" + index + "] not found.");
/*      */     }
/*  246 */     return object;
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
/*      */   public boolean getBoolean(int index) throws JSONException {
/*  261 */     Object object = get(index);
/*  262 */     if (object.equals(Boolean.FALSE) || (object instanceof String && ((String)object)
/*      */       
/*  264 */       .equalsIgnoreCase("false")))
/*  265 */       return false; 
/*  266 */     if (object.equals(Boolean.TRUE) || (object instanceof String && ((String)object)
/*      */       
/*  268 */       .equalsIgnoreCase("true"))) {
/*  269 */       return true;
/*      */     }
/*  271 */     throw wrongValueFormatException(index, "boolean", object, null);
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
/*      */   public double getDouble(int index) throws JSONException {
/*  285 */     Object object = get(index);
/*  286 */     if (object instanceof Number) {
/*  287 */       return ((Number)object).doubleValue();
/*      */     }
/*      */     try {
/*  290 */       return Double.parseDouble(object.toString());
/*  291 */     } catch (Exception e) {
/*  292 */       throw wrongValueFormatException(index, "double", object, e);
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
/*      */   public float getFloat(int index) throws JSONException {
/*  307 */     Object object = get(index);
/*  308 */     if (object instanceof Number) {
/*  309 */       return ((Number)object).floatValue();
/*      */     }
/*      */     try {
/*  312 */       return Float.parseFloat(object.toString());
/*  313 */     } catch (Exception e) {
/*  314 */       throw wrongValueFormatException(index, "float", object, e);
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
/*      */   public Number getNumber(int index) throws JSONException {
/*  329 */     Object object = get(index);
/*      */     try {
/*  331 */       if (object instanceof Number) {
/*  332 */         return (Number)object;
/*      */       }
/*  334 */       return JSONObject.stringToNumber(object.toString());
/*  335 */     } catch (Exception e) {
/*  336 */       throw wrongValueFormatException(index, "number", object, e);
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
/*      */   public <E extends Enum<E>> E getEnum(Class<E> clazz, int index) throws JSONException {
/*  355 */     E val = optEnum(clazz, index);
/*  356 */     if (val == null)
/*      */     {
/*      */ 
/*      */       
/*  360 */       throw wrongValueFormatException(index, "enum of type " + 
/*  361 */           JSONObject.quote(clazz.getSimpleName()), opt(index), null);
/*      */     }
/*  363 */     return val;
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
/*      */   public BigDecimal getBigDecimal(int index) throws JSONException {
/*  380 */     Object object = get(index);
/*  381 */     BigDecimal val = JSONObject.objectToBigDecimal(object, null);
/*  382 */     if (val == null) {
/*  383 */       throw wrongValueFormatException(index, "BigDecimal", object, null);
/*      */     }
/*  385 */     return val;
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
/*      */   public BigInteger getBigInteger(int index) throws JSONException {
/*  399 */     Object object = get(index);
/*  400 */     BigInteger val = JSONObject.objectToBigInteger(object, null);
/*  401 */     if (val == null) {
/*  402 */       throw wrongValueFormatException(index, "BigInteger", object, null);
/*      */     }
/*  404 */     return val;
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
/*      */   public int getInt(int index) throws JSONException {
/*  417 */     Object object = get(index);
/*  418 */     if (object instanceof Number) {
/*  419 */       return ((Number)object).intValue();
/*      */     }
/*      */     try {
/*  422 */       return Integer.parseInt(object.toString());
/*  423 */     } catch (Exception e) {
/*  424 */       throw wrongValueFormatException(index, "int", object, e);
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
/*      */   public JSONArray getJSONArray(int index) throws JSONException {
/*  439 */     Object object = get(index);
/*  440 */     if (object instanceof JSONArray) {
/*  441 */       return (JSONArray)object;
/*      */     }
/*  443 */     throw wrongValueFormatException(index, "JSONArray", object, null);
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
/*      */   public JSONObject getJSONObject(int index) throws JSONException {
/*  457 */     Object object = get(index);
/*  458 */     if (object instanceof JSONObject) {
/*  459 */       return (JSONObject)object;
/*      */     }
/*  461 */     throw wrongValueFormatException(index, "JSONObject", object, null);
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
/*      */   public long getLong(int index) throws JSONException {
/*  475 */     Object object = get(index);
/*  476 */     if (object instanceof Number) {
/*  477 */       return ((Number)object).longValue();
/*      */     }
/*      */     try {
/*  480 */       return Long.parseLong(object.toString());
/*  481 */     } catch (Exception e) {
/*  482 */       throw wrongValueFormatException(index, "long", object, e);
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
/*      */   public String getString(int index) throws JSONException {
/*  496 */     Object object = get(index);
/*  497 */     if (object instanceof String) {
/*  498 */       return (String)object;
/*      */     }
/*  500 */     throw wrongValueFormatException(index, "String", object, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNull(int index) {
/*  511 */     return JSONObject.NULL.equals(opt(index));
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
/*      */   public String join(String separator) throws JSONException {
/*  526 */     int len = length();
/*  527 */     if (len == 0) {
/*  528 */       return "";
/*      */     }
/*      */ 
/*      */     
/*  532 */     StringBuilder sb = new StringBuilder(JSONObject.valueToString(this.myArrayList.get(0)));
/*      */     
/*  534 */     for (int i = 1; i < len; i++) {
/*  535 */       sb.append(separator)
/*  536 */         .append(JSONObject.valueToString(this.myArrayList.get(i)));
/*      */     }
/*  538 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int length() {
/*  547 */     return this.myArrayList.size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clear() {
/*  555 */     this.myArrayList.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object opt(int index) {
/*  566 */     return (index < 0 || index >= length()) ? null : this.myArrayList
/*  567 */       .get(index);
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
/*      */   public boolean optBoolean(int index) {
/*  580 */     return optBoolean(index, false);
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
/*      */   public boolean optBoolean(int index, boolean defaultValue) {
/*      */     try {
/*  596 */       return getBoolean(index);
/*  597 */     } catch (Exception e) {
/*  598 */       return defaultValue;
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
/*      */   public Boolean optBooleanObject(int index) {
/*  612 */     return optBooleanObject(index, Boolean.valueOf(false));
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
/*      */   public Boolean optBooleanObject(int index, Boolean defaultValue) {
/*      */     try {
/*  628 */       return Boolean.valueOf(getBoolean(index));
/*  629 */     } catch (Exception e) {
/*  630 */       return defaultValue;
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
/*      */   public double optDouble(int index) {
/*  644 */     return optDouble(index, Double.NaN);
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
/*      */   public double optDouble(int index, double defaultValue) {
/*  659 */     Number val = optNumber(index, null);
/*  660 */     if (val == null) {
/*  661 */       return defaultValue;
/*      */     }
/*  663 */     double doubleValue = val.doubleValue();
/*      */ 
/*      */ 
/*      */     
/*  667 */     return doubleValue;
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
/*      */   public Double optDoubleObject(int index) {
/*  680 */     return optDoubleObject(index, Double.valueOf(Double.NaN));
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
/*      */   public Double optDoubleObject(int index, Double defaultValue) {
/*  695 */     Number val = optNumber(index, null);
/*  696 */     if (val == null) {
/*  697 */       return defaultValue;
/*      */     }
/*  699 */     Double doubleValue = Double.valueOf(val.doubleValue());
/*      */ 
/*      */ 
/*      */     
/*  703 */     return doubleValue;
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
/*      */   public float optFloat(int index) {
/*  716 */     return optFloat(index, Float.NaN);
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
/*      */   public float optFloat(int index, float defaultValue) {
/*  731 */     Number val = optNumber(index, null);
/*  732 */     if (val == null) {
/*  733 */       return defaultValue;
/*      */     }
/*  735 */     float floatValue = val.floatValue();
/*      */ 
/*      */ 
/*      */     
/*  739 */     return floatValue;
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
/*      */   public Float optFloatObject(int index) {
/*  752 */     return optFloatObject(index, Float.valueOf(Float.NaN));
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
/*      */   public Float optFloatObject(int index, Float defaultValue) {
/*  767 */     Number val = optNumber(index, null);
/*  768 */     if (val == null) {
/*  769 */       return defaultValue;
/*      */     }
/*  771 */     Float floatValue = Float.valueOf(val.floatValue());
/*      */ 
/*      */ 
/*      */     
/*  775 */     return floatValue;
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
/*      */   public int optInt(int index) {
/*  788 */     return optInt(index, 0);
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
/*      */   public int optInt(int index, int defaultValue) {
/*  803 */     Number val = optNumber(index, null);
/*  804 */     if (val == null) {
/*  805 */       return defaultValue;
/*      */     }
/*  807 */     return val.intValue();
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
/*      */   public Integer optIntegerObject(int index) {
/*  820 */     return optIntegerObject(index, Integer.valueOf(0));
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
/*      */   public Integer optIntegerObject(int index, Integer defaultValue) {
/*  835 */     Number val = optNumber(index, null);
/*  836 */     if (val == null) {
/*  837 */       return defaultValue;
/*      */     }
/*  839 */     return Integer.valueOf(val.intValue());
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
/*      */   public <E extends Enum<E>> E optEnum(Class<E> clazz, int index) {
/*  854 */     return optEnum(clazz, index, null);
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
/*      */   public <E extends Enum<E>> E optEnum(Class<E> clazz, int index, E defaultValue) {
/*      */     try {
/*  873 */       Object val = opt(index);
/*  874 */       if (JSONObject.NULL.equals(val)) {
/*  875 */         return defaultValue;
/*      */       }
/*  877 */       if (clazz.isAssignableFrom(val.getClass()))
/*      */       {
/*      */         
/*  880 */         return (E)val;
/*      */       }
/*      */       
/*  883 */       return Enum.valueOf(clazz, val.toString());
/*  884 */     } catch (IllegalArgumentException e) {
/*  885 */       return defaultValue;
/*  886 */     } catch (NullPointerException e) {
/*  887 */       return defaultValue;
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
/*      */   public BigInteger optBigInteger(int index, BigInteger defaultValue) {
/*  903 */     Object val = opt(index);
/*  904 */     return JSONObject.objectToBigInteger(val, defaultValue);
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
/*      */   public BigDecimal optBigDecimal(int index, BigDecimal defaultValue) {
/*  922 */     Object val = opt(index);
/*  923 */     return JSONObject.objectToBigDecimal(val, defaultValue);
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
/*      */   public JSONArray optJSONArray(int index) {
/*  935 */     return optJSONArray(index, null);
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
/*      */   public JSONArray optJSONArray(int index, JSONArray defaultValue) {
/*  949 */     Object object = opt(index);
/*  950 */     return (object instanceof JSONArray) ? (JSONArray)object : defaultValue;
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
/*      */   public JSONObject optJSONObject(int index) {
/*  962 */     return optJSONObject(index, null);
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
/*      */   public JSONObject optJSONObject(int index, JSONObject defaultValue) {
/*  976 */     Object object = opt(index);
/*  977 */     return (object instanceof JSONObject) ? (JSONObject)object : defaultValue;
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
/*      */   public long optLong(int index) {
/*  990 */     return optLong(index, 0L);
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
/*      */   public long optLong(int index, long defaultValue) {
/* 1005 */     Number val = optNumber(index, null);
/* 1006 */     if (val == null) {
/* 1007 */       return defaultValue;
/*      */     }
/* 1009 */     return val.longValue();
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
/*      */   public Long optLongObject(int index) {
/* 1022 */     return optLongObject(index, Long.valueOf(0L));
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
/*      */   public Long optLongObject(int index, Long defaultValue) {
/* 1037 */     Number val = optNumber(index, null);
/* 1038 */     if (val == null) {
/* 1039 */       return defaultValue;
/*      */     }
/* 1041 */     return Long.valueOf(val.longValue());
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
/*      */   public Number optNumber(int index) {
/* 1055 */     return optNumber(index, null);
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
/*      */   public Number optNumber(int index, Number defaultValue) {
/* 1071 */     Object val = opt(index);
/* 1072 */     if (JSONObject.NULL.equals(val)) {
/* 1073 */       return defaultValue;
/*      */     }
/* 1075 */     if (val instanceof Number) {
/* 1076 */       return (Number)val;
/*      */     }
/*      */     
/* 1079 */     if (val instanceof String) {
/*      */       try {
/* 1081 */         return JSONObject.stringToNumber((String)val);
/* 1082 */       } catch (Exception e) {
/* 1083 */         return defaultValue;
/*      */       } 
/*      */     }
/* 1086 */     return defaultValue;
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
/*      */   public String optString(int index) {
/* 1099 */     return optString(index, "");
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
/*      */   public String optString(int index, String defaultValue) {
/* 1113 */     Object object = opt(index);
/* 1114 */     return JSONObject.NULL.equals(object) ? defaultValue : object
/* 1115 */       .toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONArray put(boolean value) {
/* 1126 */     return put(value ? Boolean.TRUE : Boolean.FALSE);
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
/*      */   public JSONArray put(Collection<?> value) {
/* 1140 */     return put(new JSONArray(value));
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
/*      */   public JSONArray put(double value) throws JSONException {
/* 1153 */     return put(Double.valueOf(value));
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
/*      */   public JSONArray put(float value) throws JSONException {
/* 1166 */     return put(Float.valueOf(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONArray put(int value) {
/* 1177 */     return put(Integer.valueOf(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONArray put(long value) {
/* 1188 */     return put(Long.valueOf(value));
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
/*      */   public JSONArray put(Map<?, ?> value) {
/* 1204 */     return put(new JSONObject(value));
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
/*      */   public JSONArray put(Object value) {
/* 1219 */     JSONObject.testValidity(value);
/* 1220 */     this.myArrayList.add(value);
/* 1221 */     return this;
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
/*      */   public JSONArray put(int index, boolean value) throws JSONException {
/* 1238 */     return put(index, value ? Boolean.TRUE : Boolean.FALSE);
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
/*      */   public JSONArray put(int index, Collection<?> value) throws JSONException {
/* 1254 */     return put(index, new JSONArray(value));
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
/*      */   public JSONArray put(int index, double value) throws JSONException {
/* 1271 */     return put(index, Double.valueOf(value));
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
/*      */   public JSONArray put(int index, float value) throws JSONException {
/* 1288 */     return put(index, Float.valueOf(value));
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
/*      */   public JSONArray put(int index, int value) throws JSONException {
/* 1305 */     return put(index, Integer.valueOf(value));
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
/*      */   public JSONArray put(int index, long value) throws JSONException {
/* 1322 */     return put(index, Long.valueOf(value));
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
/*      */   public JSONArray put(int index, Map<?, ?> value) throws JSONException {
/* 1341 */     put(index, new JSONObject(value));
/* 1342 */     return this;
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
/*      */   public JSONArray put(int index, Object value) throws JSONException {
/* 1362 */     if (index < 0) {
/* 1363 */       throw new JSONException("JSONArray[" + index + "] not found.");
/*      */     }
/* 1365 */     if (index < length()) {
/* 1366 */       JSONObject.testValidity(value);
/* 1367 */       this.myArrayList.set(index, value);
/* 1368 */       return this;
/*      */     } 
/* 1370 */     if (index == length())
/*      */     {
/* 1372 */       return put(value);
/*      */     }
/*      */ 
/*      */     
/* 1376 */     this.myArrayList.ensureCapacity(index + 1);
/* 1377 */     while (index != length())
/*      */     {
/* 1379 */       this.myArrayList.add(JSONObject.NULL);
/*      */     }
/* 1381 */     return put(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONArray putAll(Collection<?> collection) {
/* 1392 */     addAll(collection, false);
/* 1393 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONArray putAll(Iterable<?> iter) {
/* 1404 */     addAll(iter, false);
/* 1405 */     return this;
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
/*      */   public JSONArray putAll(JSONArray array) {
/* 1418 */     this.myArrayList.addAll(array.myArrayList);
/* 1419 */     return this;
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
/*      */   public JSONArray putAll(Object array) throws JSONException {
/* 1436 */     addAll(array, false);
/* 1437 */     return this;
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
/* 1460 */     return query(new JSONPointer(jsonPointer));
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
/*      */   public Object query(JSONPointer jsonPointer) {
/* 1483 */     return jsonPointer.queryFrom(this);
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
/* 1495 */     return optQuery(new JSONPointer(jsonPointer));
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
/* 1508 */       return jsonPointer.queryFrom(this);
/* 1509 */     } catch (JSONPointerException e) {
/* 1510 */       return null;
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
/*      */   public Object remove(int index) {
/* 1523 */     return (index >= 0 && index < length()) ? this.myArrayList
/* 1524 */       .remove(index) : null;
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
/* 1536 */     if (!(other instanceof JSONArray)) {
/* 1537 */       return false;
/*      */     }
/* 1539 */     int len = length();
/* 1540 */     if (len != ((JSONArray)other).length()) {
/* 1541 */       return false;
/*      */     }
/* 1543 */     for (int i = 0; i < len; i++) {
/* 1544 */       Object valueThis = this.myArrayList.get(i);
/* 1545 */       Object valueOther = ((JSONArray)other).myArrayList.get(i);
/* 1546 */       if (valueThis != valueOther) {
/*      */ 
/*      */         
/* 1549 */         if (valueThis == null) {
/* 1550 */           return false;
/*      */         }
/* 1552 */         if (valueThis instanceof JSONObject) {
/* 1553 */           if (!((JSONObject)valueThis).similar(valueOther)) {
/* 1554 */             return false;
/*      */           }
/* 1556 */         } else if (valueThis instanceof JSONArray) {
/* 1557 */           if (!((JSONArray)valueThis).similar(valueOther)) {
/* 1558 */             return false;
/*      */           }
/* 1560 */         } else if (valueThis instanceof Number && valueOther instanceof Number) {
/* 1561 */           if (!JSONObject.isNumberSimilar((Number)valueThis, (Number)valueOther)) {
/* 1562 */             return false;
/*      */           }
/* 1564 */         } else if (valueThis instanceof JSONString && valueOther instanceof JSONString) {
/* 1565 */           if (!((JSONString)valueThis).toJSONString().equals(((JSONString)valueOther).toJSONString())) {
/* 1566 */             return false;
/*      */           }
/* 1568 */         } else if (!valueThis.equals(valueOther)) {
/* 1569 */           return false;
/*      */         } 
/*      */       } 
/* 1572 */     }  return true;
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
/*      */   public JSONObject toJSONObject(JSONArray names) throws JSONException {
/* 1588 */     if (names == null || names.isEmpty() || isEmpty()) {
/* 1589 */       return null;
/*      */     }
/* 1591 */     JSONObject jo = new JSONObject(names.length());
/* 1592 */     for (int i = 0; i < names.length(); i++) {
/* 1593 */       jo.put(names.getString(i), opt(i));
/*      */     }
/* 1595 */     return jo;
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
/*      */   public String toString() {
/*      */     try {
/* 1613 */       return toString(0);
/* 1614 */     } catch (Exception e) {
/* 1615 */       return null;
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
/*      */   public String toString(int indentFactor) throws JSONException {
/* 1648 */     StringWriter sw = new StringWriter();
/* 1649 */     return write(sw, indentFactor, 0).toString();
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
/* 1663 */     return write(writer, 0, 0);
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
/*      */   public Writer write(Writer writer, int indentFactor, int indent) throws JSONException {
/*      */     try {
/* 1698 */       boolean needsComma = false;
/* 1699 */       int length = length();
/* 1700 */       writer.write(91);
/*      */       
/* 1702 */       if (length == 1) {
/*      */         try {
/* 1704 */           JSONObject.writeValue(writer, this.myArrayList.get(0), indentFactor, indent);
/*      */         }
/* 1706 */         catch (Exception e) {
/* 1707 */           throw new JSONException("Unable to write JSONArray value at index: 0", e);
/*      */         } 
/* 1709 */       } else if (length != 0) {
/* 1710 */         int newIndent = indent + indentFactor;
/*      */         
/* 1712 */         for (int i = 0; i < length; i++) {
/* 1713 */           if (needsComma) {
/* 1714 */             writer.write(44);
/*      */           }
/* 1716 */           if (indentFactor > 0) {
/* 1717 */             writer.write(10);
/*      */           }
/* 1719 */           JSONObject.indent(writer, newIndent);
/*      */           try {
/* 1721 */             JSONObject.writeValue(writer, this.myArrayList.get(i), indentFactor, newIndent);
/*      */           }
/* 1723 */           catch (Exception e) {
/* 1724 */             throw new JSONException("Unable to write JSONArray value at index: " + i, e);
/*      */           } 
/* 1726 */           needsComma = true;
/*      */         } 
/* 1728 */         if (indentFactor > 0) {
/* 1729 */           writer.write(10);
/*      */         }
/* 1731 */         JSONObject.indent(writer, indent);
/*      */       } 
/* 1733 */       writer.write(93);
/* 1734 */       return writer;
/* 1735 */     } catch (IOException e) {
/* 1736 */       throw new JSONException(e);
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
/*      */   public List<Object> toList() {
/* 1750 */     List<Object> results = new ArrayList(this.myArrayList.size());
/* 1751 */     for (Object element : this.myArrayList) {
/* 1752 */       if (element == null || JSONObject.NULL.equals(element)) {
/* 1753 */         results.add(null); continue;
/* 1754 */       }  if (element instanceof JSONArray) {
/* 1755 */         results.add(((JSONArray)element).toList()); continue;
/* 1756 */       }  if (element instanceof JSONObject) {
/* 1757 */         results.add(((JSONObject)element).toMap()); continue;
/*      */       } 
/* 1759 */       results.add(element);
/*      */     } 
/*      */     
/* 1762 */     return results;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEmpty() {
/* 1771 */     return this.myArrayList.isEmpty();
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
/*      */   private void addAll(Collection<?> collection, boolean wrap) {
/* 1785 */     this.myArrayList.ensureCapacity(this.myArrayList.size() + collection.size());
/* 1786 */     if (wrap) {
/* 1787 */       for (Object o : collection) {
/* 1788 */         put(JSONObject.wrap(o));
/*      */       }
/*      */     } else {
/* 1791 */       for (Object o : collection) {
/* 1792 */         put(o);
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
/*      */   private void addAll(Iterable<?> iter, boolean wrap) {
/* 1807 */     if (wrap) {
/* 1808 */       for (Object o : iter) {
/* 1809 */         put(JSONObject.wrap(o));
/*      */       }
/*      */     } else {
/* 1812 */       for (Object o : iter) {
/* 1813 */         put(o);
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
/*      */   private void addAll(Object array, boolean wrap) throws JSONException {
/* 1835 */     if (array.getClass().isArray()) {
/* 1836 */       int length = Array.getLength(array);
/* 1837 */       this.myArrayList.ensureCapacity(this.myArrayList.size() + length);
/* 1838 */       if (wrap) {
/* 1839 */         for (int i = 0; i < length; i++) {
/* 1840 */           put(JSONObject.wrap(Array.get(array, i)));
/*      */         }
/*      */       } else {
/* 1843 */         for (int i = 0; i < length; i++) {
/* 1844 */           put(Array.get(array, i));
/*      */         }
/*      */       } 
/* 1847 */     } else if (array instanceof JSONArray) {
/*      */ 
/*      */ 
/*      */       
/* 1851 */       this.myArrayList.addAll(((JSONArray)array).myArrayList);
/* 1852 */     } else if (array instanceof Collection) {
/* 1853 */       addAll((Collection)array, wrap);
/* 1854 */     } else if (array instanceof Iterable) {
/* 1855 */       addAll((Iterable)array, wrap);
/*      */     } else {
/* 1857 */       throw new JSONException("JSONArray initial value should be a string or collection or array.");
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
/*      */   private static JSONException wrongValueFormatException(int idx, String valueType, Object value, Throwable cause) {
/* 1874 */     if (value == null) {
/* 1875 */       return new JSONException("JSONArray[" + idx + "] is not a " + valueType + " (null).", cause);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1880 */     if (value instanceof Map || value instanceof Iterable || value instanceof JSONObject) {
/* 1881 */       return new JSONException("JSONArray[" + idx + "] is not a " + valueType + " (" + value
/* 1882 */           .getClass() + ").", cause);
/*      */     }
/*      */     
/* 1885 */     return new JSONException("JSONArray[" + idx + "] is not a " + valueType + " (" + value
/* 1886 */         .getClass() + " : " + value + ").", cause);
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/json/JSONArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */