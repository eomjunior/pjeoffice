/*    */ package com.google.common.primitives;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @GwtCompatible
/*    */ final class ParseRequest
/*    */ {
/*    */   final String rawValue;
/*    */   final int radix;
/*    */   
/*    */   private ParseRequest(String rawValue, int radix) {
/* 27 */     this.rawValue = rawValue;
/* 28 */     this.radix = radix;
/*    */   } static ParseRequest fromString(String stringValue) {
/*    */     String rawValue;
/*    */     int radix;
/* 32 */     if (stringValue.length() == 0) {
/* 33 */       throw new NumberFormatException("empty string");
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 39 */     char firstChar = stringValue.charAt(0);
/* 40 */     if (stringValue.startsWith("0x") || stringValue.startsWith("0X")) {
/* 41 */       rawValue = stringValue.substring(2);
/* 42 */       radix = 16;
/* 43 */     } else if (firstChar == '#') {
/* 44 */       rawValue = stringValue.substring(1);
/* 45 */       radix = 16;
/* 46 */     } else if (firstChar == '0' && stringValue.length() > 1) {
/* 47 */       rawValue = stringValue.substring(1);
/* 48 */       radix = 8;
/*    */     } else {
/* 50 */       rawValue = stringValue;
/* 51 */       radix = 10;
/*    */     } 
/*    */     
/* 54 */     return new ParseRequest(rawValue, radix);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/primitives/ParseRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */