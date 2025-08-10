/*    */ package org.apache.tools.ant.types.selectors.modifiedselector;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileReader;
/*    */ import java.io.Reader;
/*    */ import org.apache.tools.ant.util.FileUtils;
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
/*    */ public class HashvalueAlgorithm
/*    */   implements Algorithm
/*    */ {
/*    */   public boolean isValid() {
/* 43 */     return true;
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
/*    */   public String getValue(File file) {
/* 56 */     if (!file.canRead())
/* 57 */       return null; 
/*    */     
/* 59 */     try { Reader r = new FileReader(file); 
/* 60 */       try { int hash = FileUtils.readFully(r).hashCode();
/* 61 */         String str = Integer.toString(hash);
/* 62 */         r.close(); return str; } catch (Throwable throwable) { try { r.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Exception e)
/* 63 */     { return null; }
/*    */   
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 73 */     return "HashvalueAlgorithm";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/modifiedselector/HashvalueAlgorithm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */