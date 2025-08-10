/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import com.github.utils4j.IParam;
/*    */ import com.github.utils4j.IParams;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Params
/*    */   implements IParams
/*    */ {
/* 39 */   public static final Params EMPTY = new Params()
/*    */     {
/*    */       public Params of(String name, Object value) {
/* 42 */         throw new IllegalStateException("Unabled to create param on EMPTY instance");
/*    */       }
/*    */     };
/*    */   
/*    */   public static Params create() {
/* 47 */     return new Params();
/*    */   }
/*    */   private final Map<String, IParam> params;
/*    */   private static Params create(Map<String, IParam> map) {
/* 51 */     return new Params(new HashMap<>(map));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Params() {
/* 57 */     this(new HashMap<>());
/*    */   }
/*    */   
/*    */   private Params(HashMap<String, IParam> params) {
/* 61 */     this.params = params;
/*    */   }
/*    */   
/*    */   public Params of(String name, Optional<?> value) {
/* 65 */     this.params.put(name, ParamImp.of(name, value.orElse(null)));
/* 66 */     return this;
/*    */   }
/*    */   
/*    */   public Params of(String name, Object value) {
/* 70 */     this.params.put(name, ParamImp.of(name, value));
/* 71 */     return this;
/*    */   }
/*    */   
/*    */   public Params clone() {
/* 75 */     return create(new HashMap<>(this.params));
/*    */   }
/*    */ 
/*    */   
/*    */   public final IParam get(String key) {
/* 80 */     return Optional.<IParam>ofNullable(this.params.get(key)).orElse(ParamImp.NULL);
/*    */   }
/*    */   
/*    */   public final void clear() {
/* 84 */     this.params.clear();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Params.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */