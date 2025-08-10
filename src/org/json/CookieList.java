/*    */ package org.json;
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
/*    */ public class CookieList
/*    */ {
/*    */   public static JSONObject toJSONObject(String string) throws JSONException {
/* 28 */     JSONObject jo = new JSONObject();
/* 29 */     JSONTokener x = new JSONTokener(string);
/* 30 */     while (x.more()) {
/* 31 */       String name = Cookie.unescape(x.nextTo('='));
/* 32 */       x.next('=');
/* 33 */       jo.put(name, Cookie.unescape(x.nextTo(';')));
/* 34 */       x.next();
/*    */     } 
/* 36 */     return jo;
/*    */   }
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
/*    */   public static String toString(JSONObject jo) throws JSONException {
/* 49 */     boolean b = false;
/* 50 */     StringBuilder sb = new StringBuilder();
/*    */     
/* 52 */     for (String key : jo.keySet()) {
/* 53 */       Object value = jo.opt(key);
/* 54 */       if (!JSONObject.NULL.equals(value)) {
/* 55 */         if (b) {
/* 56 */           sb.append(';');
/*    */         }
/* 58 */         sb.append(Cookie.escape(key));
/* 59 */         sb.append("=");
/* 60 */         sb.append(Cookie.escape(value.toString()));
/* 61 */         b = true;
/*    */       } 
/*    */     } 
/* 64 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/json/CookieList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */