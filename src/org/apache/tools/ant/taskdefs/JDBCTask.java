/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.Driver;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Properties;
/*     */ import org.apache.tools.ant.AntClassLoader;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.Reference;
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
/*     */ public abstract class JDBCTask
/*     */   extends Task
/*     */ {
/*     */   private static final int HASH_TABLE_SIZE = 3;
/* 101 */   private static final Hashtable<String, AntClassLoader> LOADER_MAP = new Hashtable<>(3);
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean caching = true;
/*     */ 
/*     */   
/*     */   private Path classpath;
/*     */ 
/*     */   
/*     */   private AntClassLoader loader;
/*     */ 
/*     */   
/*     */   private boolean autocommit = false;
/*     */ 
/*     */   
/* 117 */   private String driver = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   private String url = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   private String userId = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 132 */   private String password = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   private String rdbms = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 142 */   private String version = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean failOnConnectionError = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 155 */   private List<Property> connectionProperties = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspath(Path classpath) {
/* 162 */     this.classpath = classpath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCaching(boolean enable) {
/* 172 */     this.caching = enable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createClasspath() {
/* 180 */     if (this.classpath == null) {
/* 181 */       this.classpath = new Path(getProject());
/*     */     }
/* 183 */     return this.classpath.createPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspathRef(Reference r) {
/* 192 */     createClasspath().setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDriver(String driver) {
/* 200 */     this.driver = driver.trim();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUrl(String url) {
/* 208 */     this.url = url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPassword(String password) {
/* 216 */     this.password = password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutocommit(boolean autocommit) {
/* 225 */     this.autocommit = autocommit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRdbms(String rdbms) {
/* 234 */     this.rdbms = rdbms;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVersion(String version) {
/* 243 */     this.version = version;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFailOnConnectionError(boolean b) {
/* 253 */     this.failOnConnectionError = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isValidRdbms(Connection conn) {
/* 262 */     if (this.rdbms == null && this.version == null) {
/* 263 */       return true;
/*     */     }
/*     */     
/*     */     try {
/* 267 */       DatabaseMetaData dmd = conn.getMetaData();
/*     */       
/* 269 */       if (this.rdbms != null) {
/* 270 */         String theVendor = dmd.getDatabaseProductName().toLowerCase();
/*     */         
/* 272 */         log("RDBMS = " + theVendor, 3);
/* 273 */         if (theVendor == null || !theVendor.contains(this.rdbms)) {
/* 274 */           log("Not the required RDBMS: " + this.rdbms, 3);
/* 275 */           return false;
/*     */         } 
/*     */       } 
/*     */       
/* 279 */       if (this.version != null) {
/* 280 */         String theVersion = dmd.getDatabaseProductVersion().toLowerCase(Locale.ENGLISH);
/*     */         
/* 282 */         log("Version = " + theVersion, 3);
/* 283 */         if (theVersion == null || (
/* 284 */           !theVersion.startsWith(this.version) && 
/* 285 */           !theVersion.contains(" " + this.version))) {
/* 286 */           log("Not the required version: \"" + this.version + "\"", 3);
/* 287 */           return false;
/*     */         } 
/*     */       } 
/* 290 */     } catch (SQLException e) {
/*     */       
/* 292 */       log("Failed to obtain required RDBMS information", 0);
/* 293 */       return false;
/*     */     } 
/*     */     
/* 296 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Hashtable<String, AntClassLoader> getLoaderMap() {
/* 304 */     return LOADER_MAP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AntClassLoader getLoader() {
/* 312 */     return this.loader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConnectionProperty(Property var) {
/* 322 */     this.connectionProperties.add(var);
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
/*     */   protected Connection getConnection() throws BuildException {
/* 337 */     if (this.userId == null) {
/* 338 */       throw new BuildException("UserId attribute must be set!", getLocation());
/*     */     }
/* 340 */     if (this.password == null) {
/* 341 */       throw new BuildException("Password attribute must be set!", getLocation());
/*     */     }
/* 343 */     if (this.url == null) {
/* 344 */       throw new BuildException("Url attribute must be set!", getLocation());
/*     */     }
/*     */     try {
/* 347 */       log("connecting to " + getUrl(), 3);
/* 348 */       Properties info = new Properties();
/* 349 */       info.put("user", getUserId());
/* 350 */       info.put("password", getPassword());
/*     */       
/* 352 */       for (Property p : this.connectionProperties) {
/* 353 */         String name = p.getName();
/* 354 */         String value = p.getValue();
/* 355 */         if (name == null || value == null) {
/* 356 */           log("Only name/value pairs are supported as connection properties.", 1);
/*     */           continue;
/*     */         } 
/* 359 */         log("Setting connection property " + name + " to " + value, 3);
/*     */         
/* 361 */         info.put(name, value);
/*     */       } 
/*     */ 
/*     */       
/* 365 */       Connection conn = getDriver().connect(getUrl(), info);
/*     */       
/* 367 */       if (conn == null)
/*     */       {
/* 369 */         throw new SQLException("No suitable Driver for " + this.url);
/*     */       }
/*     */       
/* 372 */       conn.setAutoCommit(this.autocommit);
/* 373 */       return conn;
/* 374 */     } catch (SQLException e) {
/*     */       
/* 376 */       if (this.failOnConnectionError) {
/* 377 */         throw new BuildException(e, getLocation());
/*     */       }
/* 379 */       log("Failed to connect: " + e.getMessage(), 1);
/* 380 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Driver getDriver() throws BuildException {
/*     */     Driver driverInstance;
/* 391 */     if (this.driver == null) {
/* 392 */       throw new BuildException("Driver attribute must be set!", getLocation());
/*     */     }
/*     */     
/*     */     try {
/*     */       Class<? extends Driver> dc;
/*     */       
/* 398 */       if (this.classpath != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 405 */         synchronized (LOADER_MAP) {
/* 406 */           if (this.caching) {
/* 407 */             this.loader = LOADER_MAP.get(this.driver);
/*     */           }
/* 409 */           if (this.loader == null) {
/* 410 */             log("Loading " + this.driver + " using AntClassLoader with classpath " + this.classpath, 3);
/*     */ 
/*     */             
/* 413 */             this.loader = getProject().createClassLoader(this.classpath);
/* 414 */             if (this.caching) {
/* 415 */               LOADER_MAP.put(this.driver, this.loader);
/*     */             }
/*     */           } else {
/* 418 */             log("Loading " + this.driver + " using a cached AntClassLoader.", 3);
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 423 */         dc = this.loader.loadClass(this.driver).asSubclass(Driver.class);
/*     */       } else {
/* 425 */         log("Loading " + this.driver + " using system loader.", 3);
/*     */         
/* 427 */         dc = Class.forName(this.driver).asSubclass(Driver.class);
/*     */       } 
/* 429 */       driverInstance = dc.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/* 430 */     } catch (ClassNotFoundException e) {
/* 431 */       throw new BuildException("Class Not Found: JDBC driver " + this.driver + " could not be loaded", e, 
/*     */ 
/*     */           
/* 434 */           getLocation());
/* 435 */     } catch (IllegalAccessException e) {
/* 436 */       throw new BuildException("Illegal Access: JDBC driver " + this.driver + " could not be loaded", e, 
/*     */ 
/*     */           
/* 439 */           getLocation());
/* 440 */     } catch (InstantiationException|NoSuchMethodException|java.lang.reflect.InvocationTargetException e) {
/* 441 */       throw new BuildException(e
/* 442 */           .getClass().getSimpleName() + ": JDBC driver " + this.driver + " could not be loaded", e, 
/*     */           
/* 444 */           getLocation());
/*     */     } 
/* 446 */     return driverInstance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void isCaching(boolean value) {
/* 454 */     this.caching = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getClasspath() {
/* 462 */     return this.classpath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAutocommit() {
/* 470 */     return this.autocommit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUrl() {
/* 478 */     return this.url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUserId() {
/* 486 */     return this.userId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUserid(String userId) {
/* 494 */     this.userId = userId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPassword() {
/* 502 */     return this.password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRdbms() {
/* 510 */     return this.rdbms;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getVersion() {
/* 518 */     return this.version;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/JDBCTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */