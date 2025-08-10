/*    */ package org.apache.tools.ant.filters;
/*    */ 
/*    */ import java.io.Reader;
/*    */ import org.apache.tools.ant.types.Parameter;
/*    */ import org.apache.tools.ant.types.Parameterizable;
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
/*    */ public abstract class BaseParamFilterReader
/*    */   extends BaseFilterReader
/*    */   implements Parameterizable
/*    */ {
/*    */   private Parameter[] parameters;
/*    */   
/*    */   public BaseParamFilterReader() {}
/*    */   
/*    */   public BaseParamFilterReader(Reader in) {
/* 51 */     super(in);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final void setParameters(Parameter... parameters) {
/* 62 */     this.parameters = parameters;
/* 63 */     setInitialized(false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final Parameter[] getParameters() {
/* 72 */     return this.parameters;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/filters/BaseParamFilterReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */