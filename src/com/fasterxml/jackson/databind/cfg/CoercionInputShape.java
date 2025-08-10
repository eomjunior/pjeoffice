/*    */ package com.fasterxml.jackson.databind.cfg;
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
/*    */ public enum CoercionInputShape
/*    */ {
/* 28 */   Array,
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   Object,
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   Integer,
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   Float,
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   Boolean,
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   String,
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 68 */   Binary,
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 78 */   EmptyArray,
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 86 */   EmptyObject,
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 93 */   EmptyString;
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/databind/cfg/CoercionInputShape.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */