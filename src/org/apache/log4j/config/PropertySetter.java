/*     */ package org.apache.log4j.config;
/*     */ 
/*     */ import java.beans.BeanInfo;
/*     */ import java.beans.IntrospectionException;
/*     */ import java.beans.Introspector;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Properties;
/*     */ import org.apache.log4j.Level;
/*     */ import org.apache.log4j.Priority;
/*     */ import org.apache.log4j.helpers.LogLog;
/*     */ import org.apache.log4j.helpers.OptionConverter;
/*     */ import org.apache.log4j.spi.ErrorHandler;
/*     */ import org.apache.log4j.spi.OptionHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertySetter
/*     */ {
/*     */   protected Object obj;
/*     */   protected PropertyDescriptor[] props;
/*     */   
/*     */   public PropertySetter(Object obj) {
/*  74 */     this.obj = obj;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void introspect() {
/*     */     try {
/*  83 */       BeanInfo bi = Introspector.getBeanInfo(this.obj.getClass());
/*  84 */       this.props = bi.getPropertyDescriptors();
/*  85 */     } catch (IntrospectionException ex) {
/*  86 */       LogLog.error("Failed to introspect " + this.obj + ": " + ex.getMessage());
/*  87 */       this.props = new PropertyDescriptor[0];
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setProperties(Object obj, Properties properties, String prefix) {
/* 100 */     (new PropertySetter(obj)).setProperties(properties, prefix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperties(Properties properties, String prefix) {
/* 110 */     int len = prefix.length();
/*     */     
/* 112 */     for (Enumeration<?> e = properties.propertyNames(); e.hasMoreElements(); ) {
/* 113 */       String key = (String)e.nextElement();
/*     */ 
/*     */       
/* 116 */       if (key.startsWith(prefix)) {
/*     */ 
/*     */         
/* 119 */         if (key.indexOf('.', len + 1) > 0) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 125 */         String value = OptionConverter.findAndSubst(key, properties);
/* 126 */         key = key.substring(len);
/* 127 */         if (("layout".equals(key) || "errorhandler".equals(key)) && this.obj instanceof org.apache.log4j.Appender) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 134 */         PropertyDescriptor prop = getPropertyDescriptor(Introspector.decapitalize(key));
/* 135 */         if (prop != null && OptionHandler.class.isAssignableFrom(prop.getPropertyType()) && prop
/* 136 */           .getWriteMethod() != null) {
/* 137 */           OptionHandler opt = (OptionHandler)OptionConverter.instantiateByKey(properties, prefix + key, prop
/* 138 */               .getPropertyType(), null);
/* 139 */           PropertySetter setter = new PropertySetter(opt);
/* 140 */           setter.setProperties(properties, prefix + key + ".");
/*     */           try {
/* 142 */             prop.getWriteMethod().invoke(this.obj, new Object[] { opt });
/* 143 */           } catch (IllegalAccessException ex) {
/* 144 */             LogLog.warn("Failed to set property [" + key + "] to value \"" + value + "\". ", ex);
/* 145 */           } catch (InvocationTargetException ex) {
/* 146 */             if (ex.getTargetException() instanceof InterruptedException || ex
/* 147 */               .getTargetException() instanceof java.io.InterruptedIOException) {
/* 148 */               Thread.currentThread().interrupt();
/*     */             }
/* 150 */             LogLog.warn("Failed to set property [" + key + "] to value \"" + value + "\". ", ex);
/* 151 */           } catch (RuntimeException ex) {
/* 152 */             LogLog.warn("Failed to set property [" + key + "] to value \"" + value + "\". ", ex);
/*     */           } 
/*     */           
/*     */           continue;
/*     */         } 
/* 157 */         setProperty(key, value);
/*     */       } 
/*     */     } 
/* 160 */     activate();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String name, String value) {
/* 180 */     if (value == null) {
/*     */       return;
/*     */     }
/* 183 */     name = Introspector.decapitalize(name);
/* 184 */     PropertyDescriptor prop = getPropertyDescriptor(name);
/*     */ 
/*     */ 
/*     */     
/* 188 */     if (prop == null) {
/* 189 */       LogLog.warn("No such property [" + name + "] in " + this.obj.getClass().getName() + ".");
/*     */     } else {
/*     */       try {
/* 192 */         setProperty(prop, name, value);
/* 193 */       } catch (PropertySetterException ex) {
/* 194 */         LogLog.warn("Failed to set property [" + name + "] to value \"" + value + "\". ", ex.rootCause);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(PropertyDescriptor prop, String name, String value) throws PropertySetterException {
/*     */     Object arg;
/* 208 */     Method setter = prop.getWriteMethod();
/* 209 */     if (setter == null) {
/* 210 */       throw new PropertySetterException("No setter for property [" + name + "].");
/*     */     }
/* 212 */     Class[] paramTypes = setter.getParameterTypes();
/* 213 */     if (paramTypes.length != 1) {
/* 214 */       throw new PropertySetterException("#params for setter != 1");
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 219 */       arg = convertArg(value, paramTypes[0]);
/* 220 */     } catch (Throwable t) {
/* 221 */       throw new PropertySetterException("Conversion to type [" + paramTypes[0] + "] failed. Reason: " + t);
/*     */     } 
/* 223 */     if (arg == null) {
/* 224 */       throw new PropertySetterException("Conversion to type [" + paramTypes[0] + "] failed.");
/*     */     }
/* 226 */     LogLog.debug("Setting property [" + name + "] to [" + arg + "].");
/*     */     try {
/* 228 */       setter.invoke(this.obj, new Object[] { arg });
/* 229 */     } catch (IllegalAccessException ex) {
/* 230 */       throw new PropertySetterException(ex);
/* 231 */     } catch (InvocationTargetException ex) {
/* 232 */       if (ex.getTargetException() instanceof InterruptedException || ex
/* 233 */         .getTargetException() instanceof java.io.InterruptedIOException) {
/* 234 */         Thread.currentThread().interrupt();
/*     */       }
/* 236 */       throw new PropertySetterException(ex);
/* 237 */     } catch (RuntimeException ex) {
/* 238 */       throw new PropertySetterException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object convertArg(String val, Class<?> type) {
/* 246 */     if (val == null) {
/* 247 */       return null;
/*     */     }
/* 249 */     String v = val.trim();
/* 250 */     if (String.class.isAssignableFrom(type))
/* 251 */       return val; 
/* 252 */     if (int.class.isAssignableFrom(type))
/* 253 */       return new Integer(v); 
/* 254 */     if (long.class.isAssignableFrom(type))
/* 255 */       return new Long(v); 
/* 256 */     if (boolean.class.isAssignableFrom(type)) {
/* 257 */       if ("true".equalsIgnoreCase(v))
/* 258 */         return Boolean.TRUE; 
/* 259 */       if ("false".equalsIgnoreCase(v))
/* 260 */         return Boolean.FALSE; 
/*     */     } else {
/* 262 */       if (Priority.class.isAssignableFrom(type))
/* 263 */         return OptionConverter.toLevel(v, Level.DEBUG); 
/* 264 */       if (ErrorHandler.class.isAssignableFrom(type))
/* 265 */         return OptionConverter.instantiateByClassName(v, ErrorHandler.class, null); 
/*     */     } 
/* 267 */     return null;
/*     */   }
/*     */   
/*     */   protected PropertyDescriptor getPropertyDescriptor(String name) {
/* 271 */     if (this.props == null) {
/* 272 */       introspect();
/*     */     }
/* 274 */     for (int i = 0; i < this.props.length; i++) {
/* 275 */       if (name.equals(this.props[i].getName())) {
/* 276 */         return this.props[i];
/*     */       }
/*     */     } 
/* 279 */     return null;
/*     */   }
/*     */   
/*     */   public void activate() {
/* 283 */     if (this.obj instanceof OptionHandler)
/* 284 */       ((OptionHandler)this.obj).activateOptions(); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/config/PropertySetter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */