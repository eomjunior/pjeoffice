/*    */ package com.google.common.reflect;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.lang.reflect.Type;
/*    */ import java.lang.reflect.TypeVariable;
/*    */ import javax.annotation.CheckForNull;
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
/*    */ public abstract class TypeParameter<T>
/*    */   extends TypeCapture<T>
/*    */ {
/*    */   final TypeVariable<?> typeVariable;
/*    */   
/*    */   protected TypeParameter() {
/* 52 */     Type type = capture();
/* 53 */     Preconditions.checkArgument(type instanceof TypeVariable, "%s should be a type variable.", type);
/* 54 */     this.typeVariable = (TypeVariable)type;
/*    */   }
/*    */ 
/*    */   
/*    */   public final int hashCode() {
/* 59 */     return this.typeVariable.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean equals(@CheckForNull Object o) {
/* 64 */     if (o instanceof TypeParameter) {
/* 65 */       TypeParameter<?> that = (TypeParameter)o;
/* 66 */       return this.typeVariable.equals(that.typeVariable);
/*    */     } 
/* 68 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 73 */     return this.typeVariable.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/reflect/TypeParameter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */