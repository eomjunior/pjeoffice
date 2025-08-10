/*    */ package com.fasterxml.jackson.databind.introspect;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.reflect.AnnotatedElement;
/*    */ import java.lang.reflect.Modifier;
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
/*    */ public abstract class Annotated
/*    */ {
/*    */   public abstract <A extends Annotation> A getAnnotation(Class<A> paramClass);
/*    */   
/*    */   public abstract boolean hasAnnotation(Class<?> paramClass);
/*    */   
/*    */   public abstract boolean hasOneOf(Class<? extends Annotation>[] paramArrayOfClass);
/*    */   
/*    */   public abstract AnnotatedElement getAnnotated();
/*    */   
/*    */   protected abstract int getModifiers();
/*    */   
/*    */   public boolean isPublic() {
/* 36 */     return Modifier.isPublic(getModifiers());
/*    */   }
/*    */   
/*    */   public abstract String getName();
/*    */   
/*    */   public abstract JavaType getType();
/*    */   
/*    */   public abstract Class<?> getRawType();
/*    */   
/*    */   @Deprecated
/*    */   public abstract Iterable<Annotation> annotations();
/*    */   
/*    */   public abstract boolean equals(Object paramObject);
/*    */   
/*    */   public abstract int hashCode();
/*    */   
/*    */   public abstract String toString();
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/databind/introspect/Annotated.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */