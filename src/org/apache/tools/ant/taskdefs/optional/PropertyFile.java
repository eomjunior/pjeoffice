/*     */ package org.apache.tools.ant.taskdefs.optional;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.text.DateFormat;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ import org.apache.tools.ant.util.LayoutPreservingProperties;
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
/*     */ public class PropertyFile
/*     */   extends Task
/*     */ {
/*     */   private String comment;
/*     */   private Properties properties;
/*     */   private File propertyfile;
/*     */   private boolean useJDKProperties;
/* 149 */   private Vector<Entry> entries = new Vector<>();
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
/*     */   public void execute() throws BuildException {
/* 167 */     checkParameters();
/* 168 */     readFile();
/* 169 */     executeOperation();
/* 170 */     writeFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entry createEntry() {
/* 178 */     Entry e = new Entry();
/* 179 */     this.entries.addElement(e);
/* 180 */     return e;
/*     */   }
/*     */   
/*     */   private void executeOperation() throws BuildException {
/* 184 */     this.entries.forEach(e -> e.executeOn(this.properties));
/*     */   }
/*     */   
/*     */   private void readFile() throws BuildException {
/* 188 */     if (this.useJDKProperties) {
/*     */ 
/*     */       
/* 191 */       this.properties = new Properties();
/*     */     } else {
/* 193 */       this.properties = (Properties)new LayoutPreservingProperties();
/*     */     } 
/*     */     try {
/* 196 */       if (this.propertyfile.exists())
/* 197 */       { log("Updating property file: " + this.propertyfile
/* 198 */             .getAbsolutePath());
/* 199 */         InputStream fis = Files.newInputStream(this.propertyfile.toPath(), new java.nio.file.OpenOption[0]); 
/* 200 */         try { BufferedInputStream bis = new BufferedInputStream(fis); 
/* 201 */           try { this.properties.load(bis);
/* 202 */             bis.close(); } catch (Throwable throwable) { try { bis.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  if (fis != null) fis.close();  } catch (Throwable throwable) { if (fis != null)
/*     */             try { fis.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  }
/* 204 */       else { log("Creating new property file: " + this.propertyfile
/* 205 */             .getAbsolutePath());
/*     */         
/* 207 */         OutputStream out = Files.newOutputStream(this.propertyfile.toPath(), new java.nio.file.OpenOption[0]); 
/* 208 */         try { out.flush();
/* 209 */           if (out != null) out.close();  } catch (Throwable throwable) { if (out != null)
/*     */             try { out.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } 
/* 211 */     } catch (IOException ioe) {
/* 212 */       throw new BuildException(ioe.toString());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void checkParameters() throws BuildException {
/* 217 */     if (!checkParam(this.propertyfile)) {
/* 218 */       throw new BuildException("file token must not be null.", 
/* 219 */           getLocation());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFile(File file) {
/* 228 */     this.propertyfile = file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setComment(String hdr) {
/* 236 */     this.comment = hdr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJDKProperties(boolean val) {
/* 245 */     this.useJDKProperties = val;
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeFile() throws BuildException {
/* 250 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*     */     try {
/* 252 */       this.properties.store(baos, this.comment);
/* 253 */     } catch (IOException x) {
/* 254 */       throw new BuildException(x, getLocation());
/*     */     }  try {
/*     */       
/* 257 */       try { OutputStream os = Files.newOutputStream(this.propertyfile.toPath(), new java.nio.file.OpenOption[0]); 
/* 258 */         try { os.write(baos.toByteArray());
/* 259 */           if (os != null) os.close();  } catch (Throwable throwable) { if (os != null) try { os.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException x)
/* 260 */       { FileUtils.getFileUtils().tryHardToDelete(this.propertyfile);
/* 261 */         throw x; }
/*     */     
/* 263 */     } catch (IOException x) {
/* 264 */       throw new BuildException(x, getLocation());
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean checkParam(File param) {
/* 269 */     return (param != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Entry
/*     */   {
/*     */     private static final int DEFAULT_INT_VALUE = 0;
/*     */     
/*     */     private static final String DEFAULT_DATE_VALUE = "now";
/*     */     
/*     */     private static final String DEFAULT_STRING_VALUE = "";
/*     */     
/* 281 */     private String key = null;
/* 282 */     private int type = 2;
/* 283 */     private int operation = 2;
/* 284 */     private String value = null;
/* 285 */     private String defaultValue = null;
/* 286 */     private String newValue = null;
/* 287 */     private String pattern = null;
/* 288 */     private int field = 5;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setKey(String value) {
/* 295 */       this.key = value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setValue(String value) {
/* 303 */       this.value = value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setOperation(Operation value) {
/* 313 */       this.operation = Operation.toOperation(value.getValue());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setType(Type value) {
/* 321 */       this.type = Type.toType(value.getValue());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setDefault(String value) {
/* 331 */       this.defaultValue = value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setPattern(String value) {
/* 340 */       this.pattern = value;
/*     */     }
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
/*     */     public void setUnit(PropertyFile.Unit unit) {
/* 361 */       this.field = unit.getCalendarField();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void executeOn(Properties props) throws BuildException {
/* 370 */       checkParameters();
/*     */       
/* 372 */       if (this.operation == 3) {
/* 373 */         props.remove(this.key);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 378 */       String oldValue = (String)props.get(this.key);
/*     */       try {
/* 380 */         if (this.type == 0) {
/* 381 */           executeInteger(oldValue);
/* 382 */         } else if (this.type == 1) {
/* 383 */           executeDate(oldValue);
/* 384 */         } else if (this.type == 2) {
/* 385 */           executeString(oldValue);
/*     */         } else {
/* 387 */           throw new BuildException("Unknown operation type: %d", new Object[] { Integer.valueOf(this.type) });
/*     */         } 
/* 389 */       } catch (NullPointerException npe) {
/*     */ 
/*     */         
/* 392 */         npe.printStackTrace();
/*     */       } 
/*     */       
/* 395 */       if (this.newValue == null) {
/* 396 */         this.newValue = "";
/*     */       }
/*     */ 
/*     */       
/* 400 */       props.put(this.key, this.newValue);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void executeDate(String oldValue) throws BuildException {
/* 411 */       Calendar currentValue = Calendar.getInstance();
/*     */       
/* 413 */       if (this.pattern == null) {
/* 414 */         this.pattern = "yyyy/MM/dd HH:mm";
/*     */       }
/* 416 */       DateFormat fmt = new SimpleDateFormat(this.pattern);
/*     */       
/* 418 */       String currentStringValue = getCurrentValue(oldValue);
/* 419 */       if (currentStringValue == null) {
/* 420 */         currentStringValue = "now";
/*     */       }
/*     */       
/* 423 */       if ("now".equals(currentStringValue)) {
/* 424 */         currentValue.setTime(new Date());
/*     */       } else {
/*     */         try {
/* 427 */           currentValue.setTime(fmt.parse(currentStringValue));
/* 428 */         } catch (ParseException parseException) {}
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 433 */       if (this.operation != 2) {
/* 434 */         int offset = 0;
/*     */         try {
/* 436 */           offset = Integer.parseInt(this.value);
/* 437 */           if (this.operation == 1) {
/* 438 */             offset = -1 * offset;
/*     */           }
/* 440 */         } catch (NumberFormatException e) {
/* 441 */           throw new BuildException("Value not an integer on " + this.key);
/*     */         } 
/* 443 */         currentValue.add(this.field, offset);
/*     */       } 
/*     */       
/* 446 */       this.newValue = fmt.format(currentValue.getTime());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void executeInteger(String oldValue) throws BuildException {
/* 457 */       int currentValue = 0;
/* 458 */       int newV = 0;
/*     */ 
/*     */       
/* 461 */       DecimalFormat fmt = (this.pattern != null) ? new DecimalFormat(this.pattern) : new DecimalFormat();
/*     */       try {
/* 463 */         String curval = getCurrentValue(oldValue);
/* 464 */         if (curval != null) {
/* 465 */           currentValue = fmt.parse(curval).intValue();
/*     */         } else {
/* 467 */           currentValue = 0;
/*     */         } 
/* 469 */       } catch (NumberFormatException|ParseException numberFormatException) {}
/*     */ 
/*     */ 
/*     */       
/* 473 */       if (this.operation == 2) {
/* 474 */         newV = currentValue;
/*     */       } else {
/* 476 */         int operationValue = 1;
/* 477 */         if (this.value != null) {
/*     */           try {
/* 479 */             operationValue = fmt.parse(this.value).intValue();
/* 480 */           } catch (NumberFormatException|ParseException numberFormatException) {}
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 485 */         if (this.operation == 0) {
/* 486 */           newV = currentValue + operationValue;
/* 487 */         } else if (this.operation == 1) {
/* 488 */           newV = currentValue - operationValue;
/*     */         } 
/*     */       } 
/*     */       
/* 492 */       this.newValue = fmt.format(newV);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void executeString(String oldValue) throws BuildException {
/* 503 */       String newV = "";
/*     */       
/* 505 */       String currentValue = getCurrentValue(oldValue);
/*     */       
/* 507 */       if (currentValue == null) {
/* 508 */         currentValue = "";
/*     */       }
/*     */       
/* 511 */       if (this.operation == 2) {
/* 512 */         newV = currentValue;
/* 513 */       } else if (this.operation == 0) {
/* 514 */         newV = currentValue + this.value;
/*     */       } 
/* 516 */       this.newValue = newV;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void checkParameters() throws BuildException {
/* 525 */       if (this.type == 2 && this.operation == 1)
/*     */       {
/* 527 */         throw new BuildException("- is not supported for string properties (key:" + this.key + ")");
/*     */       }
/*     */       
/* 530 */       if (this.value == null && this.defaultValue == null && this.operation != 3) {
/* 531 */         throw new BuildException("\"value\" and/or \"default\" attribute must be specified (key: %s)", new Object[] { this.key });
/*     */       }
/*     */ 
/*     */       
/* 535 */       if (this.key == null) {
/* 536 */         throw new BuildException("key is mandatory");
/*     */       }
/* 538 */       if (this.type == 2 && this.pattern != null) {
/* 539 */         throw new BuildException("pattern is not supported for string properties (key: %s)", new Object[] { this.key });
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private String getCurrentValue(String oldValue) {
/* 546 */       String ret = null;
/* 547 */       if (this.operation == 2) {
/*     */ 
/*     */         
/* 550 */         if (this.value != null && this.defaultValue == null) {
/* 551 */           ret = this.value;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 556 */         if (this.value == null && this.defaultValue != null && oldValue != null) {
/* 557 */           ret = oldValue;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 562 */         if (this.value == null && this.defaultValue != null && oldValue == null) {
/* 563 */           ret = this.defaultValue;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 569 */         if (this.value != null && this.defaultValue != null && oldValue != null) {
/* 570 */           ret = this.value;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 576 */         if (this.value != null && this.defaultValue != null && oldValue == null) {
/* 577 */           ret = this.defaultValue;
/*     */         }
/*     */       } else {
/* 580 */         ret = (oldValue == null) ? this.defaultValue : oldValue;
/*     */       } 
/*     */       
/* 583 */       return ret;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public static class Operation
/*     */       extends EnumeratedAttribute
/*     */     {
/*     */       public static final int INCREMENT_OPER = 0;
/*     */ 
/*     */       
/*     */       public static final int DECREMENT_OPER = 1;
/*     */ 
/*     */       
/*     */       public static final int EQUALS_OPER = 2;
/*     */ 
/*     */       
/*     */       public static final int DELETE_OPER = 3;
/*     */ 
/*     */       
/*     */       public String[] getValues() {
/* 604 */         return new String[] { "+", "-", "=", "del" };
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public static int toOperation(String oper) {
/* 613 */         if ("+".equals(oper)) {
/* 614 */           return 0;
/*     */         }
/* 616 */         if ("-".equals(oper)) {
/* 617 */           return 1;
/*     */         }
/* 619 */         if ("del".equals(oper)) {
/* 620 */           return 3;
/*     */         }
/* 622 */         return 2;
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public static class Type
/*     */       extends EnumeratedAttribute
/*     */     {
/*     */       public static final int INTEGER_TYPE = 0;
/*     */ 
/*     */       
/*     */       public static final int DATE_TYPE = 1;
/*     */ 
/*     */       
/*     */       public static final int STRING_TYPE = 2;
/*     */ 
/*     */ 
/*     */       
/*     */       public String[] getValues() {
/* 642 */         return new String[] { "int", "date", "string" };
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public static int toType(String type) {
/* 651 */         if ("int".equals(type)) {
/* 652 */           return 0;
/*     */         }
/* 654 */         if ("date".equals(type)) {
/* 655 */           return 1;
/*     */         }
/* 657 */         return 2;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Unit
/*     */     extends EnumeratedAttribute
/*     */   {
/*     */     private static final String MILLISECOND = "millisecond";
/*     */     
/*     */     private static final String SECOND = "second";
/*     */     
/*     */     private static final String MINUTE = "minute";
/*     */     
/*     */     private static final String HOUR = "hour";
/*     */     
/*     */     private static final String DAY = "day";
/*     */     private static final String WEEK = "week";
/*     */     private static final String MONTH = "month";
/*     */     private static final String YEAR = "year";
/* 678 */     private static final String[] UNITS = new String[] { "millisecond", "second", "minute", "hour", "day", "week", "month", "year" };
/*     */ 
/*     */     
/* 681 */     private Map<String, Integer> calendarFields = new HashMap<>();
/*     */ 
/*     */     
/*     */     public Unit() {
/* 685 */       this.calendarFields.put("millisecond", 
/* 686 */           Integer.valueOf(14));
/* 687 */       this.calendarFields.put("second", Integer.valueOf(13));
/* 688 */       this.calendarFields.put("minute", Integer.valueOf(12));
/* 689 */       this.calendarFields.put("hour", Integer.valueOf(11));
/* 690 */       this.calendarFields.put("day", Integer.valueOf(5));
/* 691 */       this.calendarFields.put("week", Integer.valueOf(3));
/* 692 */       this.calendarFields.put("month", Integer.valueOf(2));
/* 693 */       this.calendarFields.put("year", Integer.valueOf(1));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getCalendarField() {
/* 701 */       return ((Integer)this.calendarFields.get(getValue().toLowerCase())).intValue();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getValues() {
/* 707 */       return UNITS;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/PropertyFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */