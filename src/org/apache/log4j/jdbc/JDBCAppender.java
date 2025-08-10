/*     */ package org.apache.log4j.jdbc;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.ArrayList;
/*     */ import org.apache.log4j.Appender;
/*     */ import org.apache.log4j.AppenderSkeleton;
/*     */ import org.apache.log4j.Layout;
/*     */ import org.apache.log4j.PatternLayout;
/*     */ import org.apache.log4j.helpers.LogLog;
/*     */ import org.apache.log4j.spi.LoggingEvent;
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
/*     */ public class JDBCAppender
/*     */   extends AppenderSkeleton
/*     */   implements Appender
/*     */ {
/*  95 */   private static final IllegalArgumentException ILLEGAL_PATTERN_FOR_SECURE_EXEC = new IllegalArgumentException("Only org.apache.log4j.PatternLayout is supported for now due to CVE-2022-23305");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   protected String databaseURL = "jdbc:odbc:myDB";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   protected String databaseUser = "me";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   protected String databasePassword = "mypassword";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 120 */   protected Connection connection = null;
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
/* 131 */   protected String sqlStatement = "";
/*     */ 
/*     */   
/*     */   private JdbcPatternParser preparedStatementParser;
/*     */ 
/*     */   
/* 137 */   protected int bufferSize = 1;
/*     */ 
/*     */   
/*     */   protected ArrayList<LoggingEvent> buffer;
/*     */ 
/*     */   
/*     */   private boolean locationInfo = false;
/*     */ 
/*     */   
/*     */   private boolean isActive = false;
/*     */ 
/*     */   
/*     */   public JDBCAppender() {
/* 150 */     this.buffer = new ArrayList<LoggingEvent>(this.bufferSize);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void activateOptions() {
/* 156 */     if (getSql() == null || getSql().trim().length() == 0) {
/* 157 */       LogLog.error("JDBCAppender.sql parameter is mandatory. Skipping all futher processing");
/* 158 */       this.isActive = false;
/*     */       
/*     */       return;
/*     */     } 
/* 162 */     LogLog.debug("JDBCAppender constructing internal pattern parser");
/* 163 */     this.preparedStatementParser = new JdbcPatternParser(getSql());
/* 164 */     this.isActive = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getLocationInfo() {
/* 174 */     return this.locationInfo;
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
/*     */   public void setLocationInfo(boolean flag) {
/* 193 */     this.locationInfo = flag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(LoggingEvent event) {
/* 200 */     if (!this.isActive) {
/*     */       return;
/*     */     }
/* 203 */     event.getNDC();
/* 204 */     event.getThreadName();
/*     */     
/* 206 */     event.getMDCCopy();
/* 207 */     if (this.locationInfo) {
/* 208 */       event.getLocationInformation();
/*     */     }
/* 210 */     event.getRenderedMessage();
/* 211 */     event.getThrowableStrRep();
/* 212 */     this.buffer.add(event);
/*     */     
/* 214 */     if (this.buffer.size() >= this.bufferSize) {
/* 215 */       flushBuffer();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getLogStatement(LoggingEvent event) {
/* 225 */     return "";
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
/*     */   protected void execute(String sql) throws SQLException {
/* 237 */     Connection con = null;
/* 238 */     Statement stmt = null;
/*     */     
/*     */     try {
/* 241 */       con = getConnection();
/*     */       
/* 243 */       stmt = con.createStatement();
/* 244 */       stmt.executeUpdate(sql);
/*     */     } finally {
/* 246 */       if (stmt != null) {
/* 247 */         stmt.close();
/*     */       }
/* 249 */       closeConnection(con);
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
/*     */   protected void closeConnection(Connection con) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Connection getConnection() throws SQLException {
/* 272 */     if (!DriverManager.getDrivers().hasMoreElements()) {
/* 273 */       setDriver("sun.jdbc.odbc.JdbcOdbcDriver");
/*     */     }
/* 275 */     if (this.connection == null) {
/* 276 */       this.connection = DriverManager.getConnection(this.databaseURL, this.databaseUser, this.databasePassword);
/*     */     }
/*     */     
/* 279 */     return this.connection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 287 */     flushBuffer();
/*     */     
/*     */     try {
/* 290 */       if (this.connection != null && !this.connection.isClosed())
/* 291 */         this.connection.close(); 
/* 292 */     } catch (SQLException e) {
/* 293 */       this.errorHandler.error("Error closing connection", e, 0);
/*     */     } 
/* 295 */     this.closed = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flushBuffer() {
/* 306 */     if (this.buffer.isEmpty()) {
/*     */       return;
/*     */     }
/* 309 */     flushBufferSecure();
/*     */   }
/*     */ 
/*     */   
/*     */   private void flushBufferSecure() {
/* 314 */     ArrayList<LoggingEvent> removes = new ArrayList<LoggingEvent>(this.buffer);
/* 315 */     this.buffer.removeAll(removes);
/*     */     
/* 317 */     if (this.layout.getClass() != PatternLayout.class) {
/* 318 */       this.errorHandler.error("Failed to convert pattern " + this.layout + " to SQL", ILLEGAL_PATTERN_FOR_SECURE_EXEC, 5);
/*     */       
/*     */       return;
/*     */     } 
/* 322 */     Connection con = null;
/* 323 */     boolean useBatch = (removes.size() > 1);
/*     */     try {
/* 325 */       con = getConnection();
/* 326 */       PreparedStatement ps = con.prepareStatement(this.preparedStatementParser.getParameterizedSql());
/* 327 */       safelyInsertIntoDB(removes, useBatch, ps);
/* 328 */     } catch (SQLException e) {
/* 329 */       this.errorHandler.error("Failed to append messages sql", e, 2);
/*     */     } finally {
/* 331 */       if (con != null) {
/* 332 */         closeConnection(con);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void safelyInsertIntoDB(ArrayList<LoggingEvent> removes, boolean useBatch, PreparedStatement ps) throws SQLException {
/*     */     try {
/* 341 */       for (LoggingEvent logEvent : removes) {
/*     */         try {
/* 343 */           this.preparedStatementParser.setParameters(ps, logEvent);
/* 344 */           if (useBatch) {
/* 345 */             ps.addBatch();
/*     */           }
/* 347 */         } catch (SQLException e) {
/* 348 */           this.errorHandler.error("Failed to append parameters", e, 2);
/*     */         } 
/*     */       } 
/* 351 */       if (useBatch) {
/* 352 */         ps.executeBatch();
/*     */       } else {
/* 354 */         ps.execute();
/*     */       } 
/*     */     } finally {
/*     */       try {
/* 358 */         ps.close();
/* 359 */       } catch (SQLException sQLException) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void finalize() {
/* 366 */     close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean requiresLayout() {
/* 373 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSql(String s) {
/* 380 */     this.sqlStatement = s;
/* 381 */     if (getLayout() == null) {
/* 382 */       setLayout((Layout)new PatternLayout(s));
/*     */     } else {
/* 384 */       ((PatternLayout)getLayout()).setConversionPattern(s);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSql() {
/* 392 */     return this.sqlStatement;
/*     */   }
/*     */   
/*     */   public void setUser(String user) {
/* 396 */     this.databaseUser = user;
/*     */   }
/*     */   
/*     */   public void setURL(String url) {
/* 400 */     this.databaseURL = url;
/*     */   }
/*     */   
/*     */   public void setPassword(String password) {
/* 404 */     this.databasePassword = password;
/*     */   }
/*     */   
/*     */   public void setBufferSize(int newBufferSize) {
/* 408 */     this.bufferSize = newBufferSize;
/* 409 */     this.buffer.ensureCapacity(this.bufferSize);
/*     */   }
/*     */   
/*     */   public String getUser() {
/* 413 */     return this.databaseUser;
/*     */   }
/*     */   
/*     */   public String getURL() {
/* 417 */     return this.databaseURL;
/*     */   }
/*     */   
/*     */   public String getPassword() {
/* 421 */     return this.databasePassword;
/*     */   }
/*     */   
/*     */   public int getBufferSize() {
/* 425 */     return this.bufferSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDriver(String driverClass) {
/*     */     try {
/* 434 */       Class.forName(driverClass);
/* 435 */     } catch (Exception e) {
/* 436 */       this.errorHandler.error("Failed to load driver", e, 0);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/jdbc/JDBCAppender.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */