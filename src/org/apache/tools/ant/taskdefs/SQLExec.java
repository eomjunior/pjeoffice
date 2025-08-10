/*      */ package org.apache.tools.ant.taskdefs;
/*      */ 
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Reader;
/*      */ import java.io.StringReader;
/*      */ import java.nio.charset.Charset;
/*      */ import java.sql.Blob;
/*      */ import java.sql.Connection;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.ResultSetMetaData;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLWarning;
/*      */ import java.sql.Statement;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ import org.apache.tools.ant.BuildException;
/*      */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*      */ import org.apache.tools.ant.types.FileSet;
/*      */ import org.apache.tools.ant.types.Resource;
/*      */ import org.apache.tools.ant.types.ResourceCollection;
/*      */ import org.apache.tools.ant.types.resources.Appendable;
/*      */ import org.apache.tools.ant.types.resources.FileProvider;
/*      */ import org.apache.tools.ant.types.resources.FileResource;
/*      */ import org.apache.tools.ant.types.resources.Union;
/*      */ import org.apache.tools.ant.util.FileUtils;
/*      */ import org.apache.tools.ant.util.KeepAliveOutputStream;
/*      */ import org.apache.tools.ant.util.StringUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class SQLExec
/*      */   extends JDBCTask
/*      */ {
/*      */   public static class DelimiterType
/*      */     extends EnumeratedAttribute
/*      */   {
/*      */     public static final String NORMAL = "normal";
/*      */     public static final String ROW = "row";
/*      */     
/*      */     public String[] getValues() {
/*   94 */       return new String[] { "normal", "row" };
/*      */     }
/*      */   }
/*      */   
/*   98 */   private int goodSql = 0;
/*      */   
/*  100 */   private int totalSql = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  105 */   private Connection conn = null;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Union resources;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  115 */   private Statement statement = null;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  120 */   private File srcFile = null;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  125 */   private String sqlCommand = "";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  130 */   private List<Transaction> transactions = new Vector<>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  135 */   private String delimiter = ";";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  141 */   private String delimiterType = "normal";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean print = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean showheaders = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean showtrailers = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  161 */   private Resource output = null;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  166 */   private String outputEncoding = null;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  171 */   private String onError = "abort";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  176 */   private String encoding = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean append = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean keepformat = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean escapeProcessing = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean expandProperties = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean rawBlobs;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean strictDelimiterMatching = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean showWarnings = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  228 */   private String csvColumnSep = ",";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  248 */   private String csvQuoteChar = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean treatWarningsAsErrors = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  260 */   private String errorProperty = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  266 */   private String warningProperty = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  273 */   private String rowCountProperty = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean forceCsvQuoteChar = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSrc(File srcFile) {
/*  286 */     this.srcFile = srcFile;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExpandProperties(boolean expandProperties) {
/*  296 */     this.expandProperties = expandProperties;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getExpandProperties() {
/*  306 */     return this.expandProperties;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addText(String sql) {
/*  318 */     this.sqlCommand += sql;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addFileset(FileSet set) {
/*  327 */     add((ResourceCollection)set);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void add(ResourceCollection rc) {
/*  337 */     if (rc == null) {
/*  338 */       throw new BuildException("Cannot add null ResourceCollection");
/*      */     }
/*  340 */     synchronized (this) {
/*  341 */       if (this.resources == null) {
/*  342 */         this.resources = new Union();
/*      */       }
/*      */     } 
/*  345 */     this.resources.add(rc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Transaction createTransaction() {
/*  353 */     Transaction t = new Transaction();
/*  354 */     this.transactions.add(t);
/*  355 */     return t;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEncoding(String encoding) {
/*  364 */     this.encoding = encoding;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDelimiter(String delimiter) {
/*  376 */     this.delimiter = delimiter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDelimiterType(DelimiterType delimiterType) {
/*  389 */     this.delimiterType = delimiterType.getValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPrint(boolean print) {
/*  398 */     this.print = print;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setShowheaders(boolean showheaders) {
/*  407 */     this.showheaders = showheaders;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setShowtrailers(boolean showtrailers) {
/*  417 */     this.showtrailers = showtrailers;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOutput(File output) {
/*  426 */     setOutput((Resource)new FileResource(getProject(), output));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOutput(Resource output) {
/*  436 */     this.output = output;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOutputEncoding(String outputEncoding) {
/*  447 */     this.outputEncoding = outputEncoding;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAppend(boolean append) {
/*  458 */     this.append = append;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOnerror(OnError action) {
/*  468 */     this.onError = action.getValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setKeepformat(boolean keepformat) {
/*  478 */     this.keepformat = keepformat;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEscapeProcessing(boolean enable) {
/*  487 */     this.escapeProcessing = enable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRawBlobs(boolean rawBlobs) {
/*  496 */     this.rawBlobs = rawBlobs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setStrictDelimiterMatching(boolean b) {
/*  508 */     this.strictDelimiterMatching = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setShowWarnings(boolean b) {
/*  518 */     this.showWarnings = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTreatWarningsAsErrors(boolean b) {
/*  528 */     this.treatWarningsAsErrors = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCsvColumnSeparator(String s) {
/*  540 */     this.csvColumnSep = s;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCsvQuoteCharacter(String s) {
/*  563 */     if (s != null && s.length() > 1) {
/*  564 */       throw new BuildException("The quote character must be a single character.");
/*      */     }
/*      */     
/*  567 */     this.csvQuoteChar = s;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setErrorProperty(String errorProperty) {
/*  578 */     this.errorProperty = errorProperty;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setWarningProperty(String warningProperty) {
/*  589 */     this.warningProperty = warningProperty;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRowCountProperty(String rowCountProperty) {
/*  599 */     this.rowCountProperty = rowCountProperty;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setForceCsvQuoteChar(boolean forceCsvQuoteChar) {
/*  607 */     this.forceCsvQuoteChar = forceCsvQuoteChar;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void execute() throws BuildException {
/*  616 */     List<Transaction> savedTransaction = new Vector<>(this.transactions);
/*  617 */     String savedSqlCommand = this.sqlCommand;
/*      */     
/*  619 */     this.sqlCommand = this.sqlCommand.trim();
/*      */     
/*      */     try {
/*  622 */       if (this.srcFile == null && this.sqlCommand.isEmpty() && this.resources == null && 
/*  623 */         this.transactions.isEmpty()) {
/*  624 */         throw new BuildException("Source file or resource collection, transactions or sql statement must be set!", 
/*      */             
/*  626 */             getLocation());
/*      */       }
/*      */ 
/*      */       
/*  630 */       if (this.srcFile != null && !this.srcFile.isFile()) {
/*  631 */         throw new BuildException("Source file " + this.srcFile + " is not a file!", 
/*  632 */             getLocation());
/*      */       }
/*      */       
/*  635 */       if (this.resources != null)
/*      */       {
/*  637 */         for (Resource r : this.resources) {
/*      */           
/*  639 */           Transaction transaction = createTransaction();
/*  640 */           transaction.setSrcResource(r);
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/*  645 */       Transaction t = createTransaction();
/*  646 */       t.setSrc(this.srcFile);
/*  647 */       t.addText(this.sqlCommand);
/*      */       
/*  649 */       if (getConnection() == null) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/*      */       try {
/*  655 */         PrintStream out = KeepAliveOutputStream.wrapSystemOut();
/*      */         try {
/*  657 */           if (this.output != null) {
/*  658 */             log("Opening PrintStream to output Resource " + this.output, 3);
/*  659 */             OutputStream os = null;
/*      */             
/*  661 */             FileProvider fp = (FileProvider)this.output.as(FileProvider.class);
/*  662 */             if (fp != null) {
/*  663 */               os = FileUtils.newOutputStream(fp.getFile().toPath(), this.append);
/*      */             } else {
/*  665 */               if (this.append) {
/*      */                 
/*  667 */                 Appendable a = (Appendable)this.output.as(Appendable.class);
/*  668 */                 if (a != null) {
/*  669 */                   os = a.getAppendOutputStream();
/*      */                 }
/*      */               } 
/*  672 */               if (os == null) {
/*  673 */                 os = this.output.getOutputStream();
/*  674 */                 if (this.append) {
/*  675 */                   log("Ignoring append=true for non-appendable resource " + this.output, 1);
/*      */                 }
/*      */               } 
/*      */             } 
/*      */ 
/*      */             
/*  681 */             if (this.outputEncoding != null) {
/*  682 */               out = new PrintStream(new BufferedOutputStream(os), false, this.outputEncoding);
/*      */             } else {
/*      */               
/*  685 */               out = new PrintStream(new BufferedOutputStream(os));
/*      */             } 
/*      */           } 
/*      */ 
/*      */           
/*  690 */           for (Transaction txn : this.transactions) {
/*  691 */             txn.runTransaction(out);
/*  692 */             if (!isAutocommit()) {
/*  693 */               log("Committing transaction", 3);
/*  694 */               getConnection().commit();
/*      */             } 
/*      */           } 
/*      */         } finally {
/*  698 */           FileUtils.close(out);
/*      */         } 
/*  700 */       } catch (IOException|SQLException e) {
/*  701 */         closeQuietly();
/*  702 */         setErrorProperty();
/*  703 */         if ("abort".equals(this.onError)) {
/*  704 */           throw new BuildException(e, getLocation());
/*      */         }
/*      */       } finally {
/*      */         try {
/*  708 */           FileUtils.close(getStatement());
/*  709 */         } catch (SQLException sQLException) {}
/*      */ 
/*      */         
/*  712 */         FileUtils.close(getConnection());
/*      */       } 
/*      */       
/*  715 */       log(this.goodSql + " of " + this.totalSql + " SQL statements executed successfully");
/*      */     } finally {
/*  717 */       this.transactions = savedTransaction;
/*  718 */       this.sqlCommand = savedSqlCommand;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void runStatements(Reader reader, PrintStream out) throws SQLException, IOException {
/*  731 */     StringBuffer sql = new StringBuffer();
/*      */     
/*  733 */     BufferedReader in = new BufferedReader(reader);
/*      */     
/*      */     String line;
/*  736 */     while ((line = in.readLine()) != null) {
/*  737 */       if (!this.keepformat) {
/*  738 */         line = line.trim();
/*      */       }
/*  740 */       if (this.expandProperties) {
/*  741 */         line = getProject().replaceProperties(line);
/*      */       }
/*  743 */       if (!this.keepformat) {
/*  744 */         if (line.startsWith("//")) {
/*      */           continue;
/*      */         }
/*  747 */         if (line.startsWith("--")) {
/*      */           continue;
/*      */         }
/*  750 */         StringTokenizer st = new StringTokenizer(line);
/*  751 */         if (st.hasMoreTokens()) {
/*  752 */           String token = st.nextToken();
/*  753 */           if ("REM".equalsIgnoreCase(token)) {
/*      */             continue;
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/*  759 */       sql.append(this.keepformat ? "\n" : " ").append(line);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  764 */       if (!this.keepformat && line.contains("--")) {
/*  765 */         sql.append("\n");
/*      */       }
/*  767 */       int lastDelimPos = lastDelimiterPosition(sql, line);
/*  768 */       if (lastDelimPos > -1) {
/*  769 */         execSQL(sql.substring(0, lastDelimPos), out);
/*  770 */         sql.replace(0, sql.length(), "");
/*      */       } 
/*      */     } 
/*      */     
/*  774 */     if (sql.length() > 0) {
/*  775 */       execSQL(sql.toString(), out);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void execSQL(String sql, PrintStream out) throws SQLException {
/*  787 */     if (sql.trim().isEmpty()) {
/*      */       return;
/*      */     }
/*      */     
/*  791 */     ResultSet resultSet = null;
/*      */     try {
/*  793 */       this.totalSql++;
/*  794 */       log("SQL: " + sql, 3);
/*      */ 
/*      */       
/*  797 */       int updateCount = 0, updateCountTotal = 0;
/*      */       
/*  799 */       boolean ret = getStatement().execute(sql);
/*  800 */       updateCount = getStatement().getUpdateCount();
/*      */       do {
/*  802 */         if (updateCount != -1) {
/*  803 */           updateCountTotal += updateCount;
/*      */         }
/*  805 */         if (ret) {
/*  806 */           resultSet = getStatement().getResultSet();
/*  807 */           printWarnings(resultSet.getWarnings(), false);
/*  808 */           resultSet.clearWarnings();
/*  809 */           if (this.print) {
/*  810 */             printResults(resultSet, out);
/*      */           }
/*      */         } 
/*  813 */         ret = getStatement().getMoreResults();
/*  814 */         updateCount = getStatement().getUpdateCount();
/*  815 */       } while (ret || updateCount != -1);
/*      */       
/*  817 */       printWarnings(getStatement().getWarnings(), false);
/*  818 */       getStatement().clearWarnings();
/*      */       
/*  820 */       log(updateCountTotal + " rows affected", 3);
/*  821 */       if (updateCountTotal != -1) {
/*  822 */         setRowCountProperty(updateCountTotal);
/*      */       }
/*      */       
/*  825 */       if (this.print && this.showtrailers) {
/*  826 */         out.println(updateCountTotal + " rows affected");
/*      */       }
/*  828 */       SQLWarning warning = getConnection().getWarnings();
/*  829 */       printWarnings(warning, true);
/*  830 */       getConnection().clearWarnings();
/*  831 */       this.goodSql++;
/*  832 */     } catch (SQLException e) {
/*  833 */       log("Failed to execute: " + sql, 0);
/*  834 */       setErrorProperty();
/*  835 */       if (!"abort".equals(this.onError)) {
/*  836 */         log(e.toString(), 0);
/*      */       }
/*  838 */       if (!"continue".equals(this.onError)) {
/*  839 */         throw e;
/*      */       }
/*      */     } finally {
/*  842 */       FileUtils.close(resultSet);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected void printResults(PrintStream out) throws SQLException {
/*  856 */     ResultSet rs = getStatement().getResultSet(); try {
/*  857 */       printResults(rs, out);
/*  858 */       if (rs != null) rs.close(); 
/*      */     } catch (Throwable throwable) {
/*      */       if (rs != null)
/*      */         try {
/*      */           rs.close();
/*      */         } catch (Throwable throwable1) {
/*      */           throwable.addSuppressed(throwable1);
/*      */         }  
/*      */       throw throwable;
/*      */     } 
/*      */   } protected void printResults(ResultSet rs, PrintStream out) throws SQLException {
/*  869 */     if (rs != null) {
/*  870 */       log("Processing new result set.", 3);
/*  871 */       ResultSetMetaData md = rs.getMetaData();
/*  872 */       int columnCount = md.getColumnCount();
/*  873 */       if (columnCount > 0) {
/*  874 */         if (this.showheaders) {
/*  875 */           out.print(maybeQuote(md.getColumnName(1)));
/*  876 */           for (int col = 2; col <= columnCount; col++) {
/*  877 */             out.print(this.csvColumnSep);
/*  878 */             out.print(maybeQuote(md.getColumnName(col)));
/*      */           } 
/*  880 */           out.println();
/*      */         } 
/*  882 */         while (rs.next()) {
/*  883 */           printValue(rs, 1, out);
/*  884 */           for (int col = 2; col <= columnCount; col++) {
/*  885 */             out.print(this.csvColumnSep);
/*  886 */             printValue(rs, col, out);
/*      */           } 
/*  888 */           out.println();
/*  889 */           printWarnings(rs.getWarnings(), false);
/*      */         } 
/*      */       } 
/*      */     } 
/*  893 */     out.println();
/*      */   }
/*      */ 
/*      */   
/*      */   private void printValue(ResultSet rs, int col, PrintStream out) throws SQLException {
/*  898 */     if (this.rawBlobs && rs.getMetaData().getColumnType(col) == 2004) {
/*  899 */       Blob blob = rs.getBlob(col);
/*  900 */       if (blob != null) {
/*  901 */         (new StreamPumper(rs.getBlob(col).getBinaryStream(), out)).run();
/*      */       }
/*      */     } else {
/*  904 */       out.print(maybeQuote(rs.getString(col)));
/*      */     } 
/*      */   }
/*      */   
/*      */   private String maybeQuote(String s) {
/*  909 */     if (this.csvQuoteChar == null || s == null || (!this.forceCsvQuoteChar && 
/*  910 */       !s.contains(this.csvColumnSep) && !s.contains(this.csvQuoteChar))) {
/*  911 */       return s;
/*      */     }
/*  913 */     StringBuilder sb = new StringBuilder(this.csvQuoteChar);
/*  914 */     char q = this.csvQuoteChar.charAt(0);
/*  915 */     for (char c : s.toCharArray()) {
/*  916 */       if (c == q) {
/*  917 */         sb.append(q);
/*      */       }
/*  919 */       sb.append(c);
/*      */     } 
/*  921 */     return sb.append(this.csvQuoteChar).toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void closeQuietly() {
/*  930 */     if (!isAutocommit() && getConnection() != null && "abort".equals(this.onError)) {
/*      */       try {
/*  932 */         getConnection().rollback();
/*  933 */       } catch (SQLException sQLException) {}
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Connection getConnection() {
/*  952 */     if (this.conn == null) {
/*  953 */       this.conn = super.getConnection();
/*  954 */       if (!isValidRdbms(this.conn)) {
/*  955 */         this.conn = null;
/*      */       }
/*      */     } 
/*  958 */     return this.conn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Statement getStatement() throws SQLException {
/*  974 */     if (this.statement == null) {
/*  975 */       this.statement = getConnection().createStatement();
/*  976 */       this.statement.setEscapeProcessing(this.escapeProcessing);
/*      */     } 
/*  978 */     return this.statement;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class OnError
/*      */     extends EnumeratedAttribute
/*      */   {
/*      */     public String[] getValues() {
/*  989 */       return new String[] { "continue", "stop", "abort" };
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public class Transaction
/*      */   {
/* 1000 */     private Resource tSrcResource = null;
/* 1001 */     private String tSqlCommand = "";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setSrc(File src) {
/* 1010 */       if (src != null) {
/* 1011 */         setSrcResource((Resource)new FileResource(src));
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setSrcResource(Resource src) {
/* 1021 */       if (this.tSrcResource != null) {
/* 1022 */         throw new BuildException("only one resource per transaction");
/*      */       }
/* 1024 */       this.tSrcResource = src;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void addText(String sql) {
/* 1032 */       if (sql != null) {
/* 1033 */         this.tSqlCommand += sql;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void addConfigured(ResourceCollection a) {
/* 1043 */       if (a.size() != 1) {
/* 1044 */         throw new BuildException("only single argument resource collections are supported.");
/*      */       }
/*      */       
/* 1047 */       setSrcResource(a.iterator().next());
/*      */     }
/*      */ 
/*      */     
/*      */     private void runTransaction(PrintStream out) throws IOException, SQLException {
/* 1052 */       if (!this.tSqlCommand.isEmpty()) {
/* 1053 */         SQLExec.this.log("Executing commands", 2);
/* 1054 */         SQLExec.this.runStatements(new StringReader(this.tSqlCommand), out);
/*      */       } 
/*      */       
/* 1057 */       if (this.tSrcResource != null) {
/* 1058 */         SQLExec.this.log("Executing resource: " + this.tSrcResource.toString(), 2);
/*      */ 
/*      */         
/* 1061 */         Charset charset = (SQLExec.this.encoding == null) ? Charset.defaultCharset() : Charset.forName(SQLExec.this.encoding);
/*      */         
/* 1063 */         Reader reader = new InputStreamReader(this.tSrcResource.getInputStream(), charset); 
/* 1064 */         try { SQLExec.this.runStatements(reader, out);
/* 1065 */           reader.close(); }
/*      */         catch (Throwable throwable) { try { reader.close(); }
/*      */           catch (Throwable throwable1)
/*      */           { throwable.addSuppressed(throwable1); }
/*      */            throw throwable; }
/*      */       
/* 1071 */       }  } } public int lastDelimiterPosition(StringBuffer buf, String currentLine) { if (this.strictDelimiterMatching) {
/* 1072 */       if ((this.delimiterType.equals("normal") && 
/* 1073 */         StringUtils.endsWith(buf, this.delimiter)) || (this.delimiterType
/* 1074 */         .equals("row") && currentLine
/* 1075 */         .equals(this.delimiter))) {
/* 1076 */         return buf.length() - this.delimiter.length();
/*      */       }
/*      */       
/* 1079 */       return -1;
/*      */     } 
/* 1081 */     String d = this.delimiter.trim().toLowerCase(Locale.ENGLISH);
/* 1082 */     if ("normal".equals(this.delimiterType)) {
/*      */ 
/*      */       
/* 1085 */       int endIndex = this.delimiter.length() - 1;
/* 1086 */       int bufferIndex = buf.length() - 1;
/* 1087 */       while (bufferIndex >= 0 && Character.isWhitespace(buf.charAt(bufferIndex))) {
/* 1088 */         bufferIndex--;
/*      */       }
/* 1090 */       if (bufferIndex < endIndex) {
/* 1091 */         return -1;
/*      */       }
/* 1093 */       while (endIndex >= 0) {
/* 1094 */         if (buf.substring(bufferIndex, bufferIndex + 1).toLowerCase(Locale.ENGLISH)
/* 1095 */           .charAt(0) != d.charAt(endIndex)) {
/* 1096 */           return -1;
/*      */         }
/* 1098 */         bufferIndex--;
/* 1099 */         endIndex--;
/*      */       } 
/* 1101 */       return bufferIndex + 1;
/*      */     } 
/* 1103 */     return currentLine.trim().toLowerCase(Locale.ENGLISH).equals(d) ? (
/* 1104 */       buf.length() - currentLine.length()) : -1; }
/*      */ 
/*      */ 
/*      */   
/*      */   private void printWarnings(SQLWarning warning, boolean force) throws SQLException {
/* 1109 */     SQLWarning initialWarning = warning;
/* 1110 */     if (this.showWarnings || force) {
/* 1111 */       while (warning != null) {
/* 1112 */         log(warning + " sql warning", 
/* 1113 */             this.showWarnings ? 1 : 3);
/* 1114 */         warning = warning.getNextWarning();
/*      */       } 
/*      */     }
/* 1117 */     if (initialWarning != null) {
/* 1118 */       setWarningProperty();
/*      */     }
/* 1120 */     if (this.treatWarningsAsErrors && initialWarning != null) {
/* 1121 */       throw initialWarning;
/*      */     }
/*      */   }
/*      */   
/*      */   protected final void setErrorProperty() {
/* 1126 */     setProperty(this.errorProperty, "true");
/*      */   }
/*      */   
/*      */   protected final void setWarningProperty() {
/* 1130 */     setProperty(this.warningProperty, "true");
/*      */   }
/*      */   
/*      */   protected final void setRowCountProperty(int rowCount) {
/* 1134 */     setProperty(this.rowCountProperty, Integer.toString(rowCount));
/*      */   }
/*      */   
/*      */   private void setProperty(String name, String value) {
/* 1138 */     if (name != null)
/* 1139 */       getProject().setNewProperty(name, value); 
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/SQLExec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */