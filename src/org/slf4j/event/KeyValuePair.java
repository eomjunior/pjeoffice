/*    */ package org.slf4j.event;
/*    */ 
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class KeyValuePair
/*    */ {
/*    */   public final String key;
/*    */   public final Object value;
/*    */   
/*    */   public KeyValuePair(String key, Object value) {
/* 11 */     this.key = key;
/* 12 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 17 */     return String.valueOf(this.key) + "=\"" + String.valueOf(this.value) + "\"";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 22 */     if (this == o) return true; 
/* 23 */     if (o == null || getClass() != o.getClass()) return false; 
/* 24 */     KeyValuePair that = (KeyValuePair)o;
/* 25 */     return (Objects.equals(this.key, that.key) && Objects.equals(this.value, that.value));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 30 */     return Objects.hash(new Object[] { this.key, this.value });
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/slf4j/event/KeyValuePair.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */