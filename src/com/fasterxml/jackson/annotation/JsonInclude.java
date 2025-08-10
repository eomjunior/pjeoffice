/*     */ package com.fasterxml.jackson.annotation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.annotation.Retention;
/*     */ import java.lang.annotation.RetentionPolicy;
/*     */ import java.lang.annotation.Target;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.TYPE, ElementType.PARAMETER})
/*     */ @Retention(RetentionPolicy.RUNTIME)
/*     */ @JacksonAnnotation
/*     */ public @interface JsonInclude
/*     */ {
/*     */   Include value() default Include.ALWAYS;
/*     */   
/*     */   Include content() default Include.ALWAYS;
/*     */   
/*     */   Class<?> valueFilter() default Void.class;
/*     */   
/*     */   Class<?> contentFilter() default Void.class;
/*     */   
/*     */   public enum Include
/*     */   {
/* 109 */     ALWAYS,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 115 */     NON_NULL,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 130 */     NON_ABSENT,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 177 */     NON_EMPTY,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 200 */     NON_DEFAULT,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 218 */     CUSTOM,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 229 */     USE_DEFAULTS;
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
/*     */   public static class Value
/*     */     implements JacksonAnnotationValue<JsonInclude>, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 251 */     protected static final Value EMPTY = new Value(JsonInclude.Include.USE_DEFAULTS, JsonInclude.Include.USE_DEFAULTS, null, null);
/*     */ 
/*     */     
/*     */     protected final JsonInclude.Include _valueInclusion;
/*     */ 
/*     */     
/*     */     protected final JsonInclude.Include _contentInclusion;
/*     */ 
/*     */     
/*     */     protected final Class<?> _valueFilter;
/*     */ 
/*     */     
/*     */     protected final Class<?> _contentFilter;
/*     */ 
/*     */ 
/*     */     
/*     */     public Value(JsonInclude src) {
/* 268 */       this(src.value(), src.content(), src
/* 269 */           .valueFilter(), src.contentFilter());
/*     */     }
/*     */ 
/*     */     
/*     */     protected Value(JsonInclude.Include vi, JsonInclude.Include ci, Class<?> valueFilter, Class<?> contentFilter) {
/* 274 */       this._valueInclusion = (vi == null) ? JsonInclude.Include.USE_DEFAULTS : vi;
/* 275 */       this._contentInclusion = (ci == null) ? JsonInclude.Include.USE_DEFAULTS : ci;
/* 276 */       this._valueFilter = (valueFilter == Void.class) ? null : valueFilter;
/* 277 */       this._contentFilter = (contentFilter == Void.class) ? null : contentFilter;
/*     */     }
/*     */     
/*     */     public static Value empty() {
/* 281 */       return EMPTY;
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Value merge(Value base, Value overrides) {
/* 297 */       return (base == null) ? overrides : base
/* 298 */         .withOverrides(overrides);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Value mergeAll(Value... values) {
/* 306 */       Value result = null;
/* 307 */       for (Value curr : values) {
/* 308 */         if (curr != null) {
/* 309 */           result = (result == null) ? curr : result.withOverrides(curr);
/*     */         }
/*     */       } 
/* 312 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Object readResolve() {
/* 317 */       if (this._valueInclusion == JsonInclude.Include.USE_DEFAULTS && this._contentInclusion == JsonInclude.Include.USE_DEFAULTS && this._valueFilter == null && this._contentFilter == null)
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 322 */         return EMPTY;
/*     */       }
/* 324 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Value withOverrides(Value overrides) {
/* 334 */       if (overrides == null || overrides == EMPTY) {
/* 335 */         return this;
/*     */       }
/* 337 */       JsonInclude.Include vi = overrides._valueInclusion;
/* 338 */       JsonInclude.Include ci = overrides._contentInclusion;
/* 339 */       Class<?> vf = overrides._valueFilter;
/* 340 */       Class<?> cf = overrides._contentFilter;
/*     */       
/* 342 */       boolean viDiff = (vi != this._valueInclusion && vi != JsonInclude.Include.USE_DEFAULTS);
/* 343 */       boolean ciDiff = (ci != this._contentInclusion && ci != JsonInclude.Include.USE_DEFAULTS);
/* 344 */       boolean filterDiff = (vf != this._valueFilter || cf != this._valueFilter);
/*     */       
/* 346 */       if (viDiff) {
/* 347 */         if (ciDiff) {
/* 348 */           return new Value(vi, ci, vf, cf);
/*     */         }
/* 350 */         return new Value(vi, this._contentInclusion, vf, cf);
/* 351 */       }  if (ciDiff)
/* 352 */         return new Value(this._valueInclusion, ci, vf, cf); 
/* 353 */       if (filterDiff) {
/* 354 */         return new Value(this._valueInclusion, this._contentInclusion, vf, cf);
/*     */       }
/* 356 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Value construct(JsonInclude.Include valueIncl, JsonInclude.Include contentIncl) {
/* 363 */       if ((valueIncl == JsonInclude.Include.USE_DEFAULTS || valueIncl == null) && (contentIncl == JsonInclude.Include.USE_DEFAULTS || contentIncl == null))
/*     */       {
/* 365 */         return EMPTY;
/*     */       }
/* 367 */       return new Value(valueIncl, contentIncl, null, null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Value construct(JsonInclude.Include valueIncl, JsonInclude.Include contentIncl, Class<?> valueFilter, Class<?> contentFilter) {
/* 378 */       if (valueFilter == Void.class) {
/* 379 */         valueFilter = null;
/*     */       }
/* 381 */       if (contentFilter == Void.class) {
/* 382 */         contentFilter = null;
/*     */       }
/* 384 */       if ((valueIncl == JsonInclude.Include.USE_DEFAULTS || valueIncl == null) && (contentIncl == JsonInclude.Include.USE_DEFAULTS || contentIncl == null) && valueFilter == null && contentFilter == null)
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 389 */         return EMPTY;
/*     */       }
/* 391 */       return new Value(valueIncl, contentIncl, valueFilter, contentFilter);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Value from(JsonInclude src) {
/* 399 */       if (src == null) {
/* 400 */         return EMPTY;
/*     */       }
/* 402 */       JsonInclude.Include vi = src.value();
/* 403 */       JsonInclude.Include ci = src.content();
/*     */       
/* 405 */       if (vi == JsonInclude.Include.USE_DEFAULTS && ci == JsonInclude.Include.USE_DEFAULTS) {
/* 406 */         return EMPTY;
/*     */       }
/* 408 */       Class<?> vf = src.valueFilter();
/* 409 */       if (vf == Void.class) {
/* 410 */         vf = null;
/*     */       }
/* 412 */       Class<?> cf = src.contentFilter();
/* 413 */       if (cf == Void.class) {
/* 414 */         cf = null;
/*     */       }
/* 416 */       return new Value(vi, ci, vf, cf);
/*     */     }
/*     */     
/*     */     public Value withValueInclusion(JsonInclude.Include incl) {
/* 420 */       return (incl == this._valueInclusion) ? this : new Value(incl, this._contentInclusion, this._valueFilter, this._contentFilter);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Value withValueFilter(Class<?> filter) {
/*     */       JsonInclude.Include incl;
/* 438 */       if (filter == null || filter == Void.class) {
/* 439 */         incl = JsonInclude.Include.USE_DEFAULTS;
/* 440 */         filter = null;
/*     */       } else {
/* 442 */         incl = JsonInclude.Include.CUSTOM;
/*     */       } 
/* 444 */       return construct(incl, this._contentInclusion, filter, this._contentFilter);
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Value withContentFilter(Class<?> filter) {
/*     */       JsonInclude.Include incl;
/* 461 */       if (filter == null || filter == Void.class) {
/* 462 */         incl = JsonInclude.Include.USE_DEFAULTS;
/* 463 */         filter = null;
/*     */       } else {
/* 465 */         incl = JsonInclude.Include.CUSTOM;
/*     */       } 
/* 467 */       return construct(this._valueInclusion, incl, this._valueFilter, filter);
/*     */     }
/*     */     
/*     */     public Value withContentInclusion(JsonInclude.Include incl) {
/* 471 */       return (incl == this._contentInclusion) ? this : new Value(this._valueInclusion, incl, this._valueFilter, this._contentFilter);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Class<JsonInclude> valueFor() {
/* 477 */       return JsonInclude.class;
/*     */     }
/*     */     
/*     */     public JsonInclude.Include getValueInclusion() {
/* 481 */       return this._valueInclusion;
/*     */     }
/*     */     
/*     */     public JsonInclude.Include getContentInclusion() {
/* 485 */       return this._contentInclusion;
/*     */     }
/*     */     
/*     */     public Class<?> getValueFilter() {
/* 489 */       return this._valueFilter;
/*     */     }
/*     */     
/*     */     public Class<?> getContentFilter() {
/* 493 */       return this._contentFilter;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 498 */       StringBuilder sb = new StringBuilder(80);
/* 499 */       sb.append("JsonInclude.Value(value=")
/* 500 */         .append(this._valueInclusion)
/* 501 */         .append(",content=")
/* 502 */         .append(this._contentInclusion);
/* 503 */       if (this._valueFilter != null) {
/* 504 */         sb.append(",valueFilter=").append(this._valueFilter.getName()).append(".class");
/*     */       }
/* 506 */       if (this._contentFilter != null) {
/* 507 */         sb.append(",contentFilter=").append(this._contentFilter.getName()).append(".class");
/*     */       }
/* 509 */       return sb.append(')').toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 514 */       return (this._valueInclusion.hashCode() << 2) + this._contentInclusion
/* 515 */         .hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 520 */       if (o == this) return true; 
/* 521 */       if (o == null) return false; 
/* 522 */       if (o.getClass() != getClass()) return false; 
/* 523 */       Value other = (Value)o;
/*     */       
/* 525 */       return (other._valueInclusion == this._valueInclusion && other._contentInclusion == this._contentInclusion && other._valueFilter == this._valueFilter && other._contentFilter == this._contentFilter);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/annotation/JsonInclude.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */