/*     */ package org.apache.log4j.net;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Map;
/*     */ import org.apache.log4j.helpers.LogLog;
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
/*     */ public class ZeroConfSupport
/*     */ {
/*  29 */   private static Object jmDNS = initializeJMDNS();
/*     */   
/*     */   Object serviceInfo;
/*     */   
/*     */   private static Class jmDNSClass;
/*     */   
/*     */   private static Class serviceInfoClass;
/*     */   
/*     */   public ZeroConfSupport(String zone, int port, String name, Map properties) {
/*  38 */     boolean isVersion3 = false;
/*     */     
/*     */     try {
/*  41 */       jmDNSClass.getMethod("create", null);
/*  42 */       isVersion3 = true;
/*  43 */     } catch (NoSuchMethodException noSuchMethodException) {}
/*     */ 
/*     */ 
/*     */     
/*  47 */     if (isVersion3) {
/*  48 */       LogLog.debug("using JmDNS version 3 to construct serviceInfo instance");
/*  49 */       this.serviceInfo = buildServiceInfoVersion3(zone, port, name, properties);
/*     */     } else {
/*  51 */       LogLog.debug("using JmDNS version 1.0 to construct serviceInfo instance");
/*  52 */       this.serviceInfo = buildServiceInfoVersion1(zone, port, name, properties);
/*     */     } 
/*     */   }
/*     */   
/*     */   public ZeroConfSupport(String zone, int port, String name) {
/*  57 */     this(zone, port, name, new HashMap<Object, Object>());
/*     */   }
/*     */   
/*     */   private static Object createJmDNSVersion1() {
/*     */     try {
/*  62 */       return jmDNSClass.newInstance();
/*  63 */     } catch (InstantiationException e) {
/*  64 */       LogLog.warn("Unable to instantiate JMDNS", e);
/*  65 */     } catch (IllegalAccessException e) {
/*  66 */       LogLog.warn("Unable to instantiate JMDNS", e);
/*     */     } 
/*  68 */     return null;
/*     */   }
/*     */   
/*     */   private static Object createJmDNSVersion3() {
/*     */     try {
/*  73 */       Method jmDNSCreateMethod = jmDNSClass.getMethod("create", null);
/*  74 */       return jmDNSCreateMethod.invoke(null, null);
/*  75 */     } catch (IllegalAccessException e) {
/*  76 */       LogLog.warn("Unable to instantiate jmdns class", e);
/*  77 */     } catch (NoSuchMethodException e) {
/*  78 */       LogLog.warn("Unable to access constructor", e);
/*  79 */     } catch (InvocationTargetException e) {
/*  80 */       LogLog.warn("Unable to call constructor", e);
/*     */     } 
/*  82 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private Object buildServiceInfoVersion1(String zone, int port, String name, Map<?, ?> properties) {
/*  87 */     Hashtable<Object, Object> hashtableProperties = new Hashtable<Object, Object>(properties);
/*     */     try {
/*  89 */       Class[] args = new Class[6];
/*  90 */       args[0] = String.class;
/*  91 */       args[1] = String.class;
/*  92 */       args[2] = int.class;
/*  93 */       args[3] = int.class;
/*  94 */       args[4] = int.class;
/*  95 */       args[5] = Hashtable.class;
/*  96 */       Constructor constructor = serviceInfoClass.getConstructor(args);
/*  97 */       Object[] values = new Object[6];
/*  98 */       values[0] = zone;
/*  99 */       values[1] = name;
/* 100 */       values[2] = new Integer(port);
/* 101 */       values[3] = new Integer(0);
/* 102 */       values[4] = new Integer(0);
/* 103 */       values[5] = hashtableProperties;
/* 104 */       Object result = constructor.newInstance(values);
/* 105 */       LogLog.debug("created serviceinfo: " + result);
/* 106 */       return result;
/* 107 */     } catch (IllegalAccessException e) {
/* 108 */       LogLog.warn("Unable to construct ServiceInfo instance", e);
/* 109 */     } catch (NoSuchMethodException e) {
/* 110 */       LogLog.warn("Unable to get ServiceInfo constructor", e);
/* 111 */     } catch (InstantiationException e) {
/* 112 */       LogLog.warn("Unable to construct ServiceInfo instance", e);
/* 113 */     } catch (InvocationTargetException e) {
/* 114 */       LogLog.warn("Unable to construct ServiceInfo instance", e);
/*     */     } 
/* 116 */     return null;
/*     */   }
/*     */   
/*     */   private Object buildServiceInfoVersion3(String zone, int port, String name, Map properties) {
/*     */     try {
/* 121 */       Class[] args = new Class[6];
/* 122 */       args[0] = String.class;
/* 123 */       args[1] = String.class;
/* 124 */       args[2] = int.class;
/* 125 */       args[3] = int.class;
/* 126 */       args[4] = int.class;
/* 127 */       args[5] = Map.class;
/* 128 */       Method serviceInfoCreateMethod = serviceInfoClass.getMethod("create", args);
/* 129 */       Object[] values = new Object[6];
/* 130 */       values[0] = zone;
/* 131 */       values[1] = name;
/* 132 */       values[2] = new Integer(port);
/* 133 */       values[3] = new Integer(0);
/* 134 */       values[4] = new Integer(0);
/* 135 */       values[5] = properties;
/* 136 */       Object result = serviceInfoCreateMethod.invoke(null, values);
/* 137 */       LogLog.debug("created serviceinfo: " + result);
/* 138 */       return result;
/* 139 */     } catch (IllegalAccessException e) {
/* 140 */       LogLog.warn("Unable to invoke create method", e);
/* 141 */     } catch (NoSuchMethodException e) {
/* 142 */       LogLog.warn("Unable to find create method", e);
/* 143 */     } catch (InvocationTargetException e) {
/* 144 */       LogLog.warn("Unable to invoke create method", e);
/*     */     } 
/* 146 */     return null;
/*     */   }
/*     */   
/*     */   public void advertise() {
/*     */     try {
/* 151 */       Method method = jmDNSClass.getMethod("registerService", new Class[] { serviceInfoClass });
/* 152 */       method.invoke(jmDNS, new Object[] { this.serviceInfo });
/* 153 */       LogLog.debug("registered serviceInfo: " + this.serviceInfo);
/* 154 */     } catch (IllegalAccessException e) {
/* 155 */       LogLog.warn("Unable to invoke registerService method", e);
/* 156 */     } catch (NoSuchMethodException e) {
/* 157 */       LogLog.warn("No registerService method", e);
/* 158 */     } catch (InvocationTargetException e) {
/* 159 */       LogLog.warn("Unable to invoke registerService method", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void unadvertise() {
/*     */     try {
/* 165 */       Method method = jmDNSClass.getMethod("unregisterService", new Class[] { serviceInfoClass });
/* 166 */       method.invoke(jmDNS, new Object[] { this.serviceInfo });
/* 167 */       LogLog.debug("unregistered serviceInfo: " + this.serviceInfo);
/* 168 */     } catch (IllegalAccessException e) {
/* 169 */       LogLog.warn("Unable to invoke unregisterService method", e);
/* 170 */     } catch (NoSuchMethodException e) {
/* 171 */       LogLog.warn("No unregisterService method", e);
/* 172 */     } catch (InvocationTargetException e) {
/* 173 */       LogLog.warn("Unable to invoke unregisterService method", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Object initializeJMDNS() {
/*     */     try {
/* 179 */       jmDNSClass = Class.forName("javax.jmdns.JmDNS");
/* 180 */       serviceInfoClass = Class.forName("javax.jmdns.ServiceInfo");
/* 181 */     } catch (ClassNotFoundException e) {
/* 182 */       LogLog.warn("JmDNS or serviceInfo class not found", e);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 187 */     boolean isVersion3 = false;
/*     */     
/*     */     try {
/* 190 */       jmDNSClass.getMethod("create", null);
/* 191 */       isVersion3 = true;
/* 192 */     } catch (NoSuchMethodException noSuchMethodException) {}
/*     */ 
/*     */ 
/*     */     
/* 196 */     if (isVersion3) {
/* 197 */       return createJmDNSVersion3();
/*     */     }
/* 199 */     return createJmDNSVersion1();
/*     */   }
/*     */ 
/*     */   
/*     */   public static Object getJMDNSInstance() {
/* 204 */     return jmDNS;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/net/ZeroConfSupport.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */