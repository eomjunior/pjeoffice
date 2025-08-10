/*    */ package org.apache.tools.ant.types.resources;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.lang.reflect.Field;
/*    */ import java.nio.charset.StandardCharsets;
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
/*    */ public class JavaConstantResource
/*    */   extends AbstractClasspathResource
/*    */ {
/*    */   protected InputStream openInputStream(ClassLoader cl) throws IOException {
/* 42 */     String constant = getName();
/* 43 */     if (constant == null) {
/* 44 */       throw new IOException("Attribute 'name' must be set.");
/*    */     }
/* 46 */     int index = constant.lastIndexOf('.');
/* 47 */     if (index < 0) {
/* 48 */       throw new IOException("No class name in " + constant);
/*    */     }
/* 50 */     String classname = constant.substring(0, index);
/* 51 */     String fieldname = constant.substring(index + 1);
/*    */ 
/*    */ 
/*    */     
/*    */     try {
/* 56 */       Class<?> clazz = (cl != null) ? Class.forName(classname, true, cl) : Class.forName(classname);
/* 57 */       Field field = clazz.getField(fieldname);
/* 58 */       String value = field.get(null).toString();
/* 59 */       return new ByteArrayInputStream(value.getBytes(StandardCharsets.UTF_8));
/* 60 */     } catch (ClassNotFoundException e) {
/* 61 */       throw new IOException("Class not found:" + classname);
/* 62 */     } catch (NoSuchFieldException e) {
/* 63 */       throw new IOException("Field not found:" + fieldname + " in " + classname);
/*    */     }
/* 65 */     catch (IllegalAccessException e) {
/* 66 */       throw new IOException("Illegal access to :" + fieldname + " in " + classname);
/* 67 */     } catch (NullPointerException npe) {
/* 68 */       throw new IOException("Not a static field: " + fieldname + " in " + classname);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/JavaConstantResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */