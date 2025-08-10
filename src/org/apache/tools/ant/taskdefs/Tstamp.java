/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.time.Instant;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Optional;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.TimeZone;
/*     */ import java.util.Vector;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Location;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Tstamp
/*     */   extends Task
/*     */ {
/*     */   private static final String ENV_SOURCE_DATE_EPOCH = "SOURCE_DATE_EPOCH";
/*  55 */   private List<CustomFormat> customFormats = new Vector<>();
/*  56 */   private String prefix = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrefix(String prefix) {
/*  65 */     this.prefix = prefix;
/*  66 */     if (!this.prefix.endsWith(".")) {
/*  67 */       this.prefix += ".";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/*     */     try {
/*  79 */       Date d = getNow();
/*     */       
/*  81 */       String epoch = System.getenv("SOURCE_DATE_EPOCH");
/*     */       try {
/*  83 */         if (epoch != null) {
/*     */           
/*  85 */           d = new Date((Integer.parseInt(epoch) * 1000));
/*  86 */           log("Honouring environment variable SOURCE_DATE_EPOCH which has been set to " + epoch);
/*     */         } 
/*  88 */       } catch (NumberFormatException e) {
/*     */         
/*  90 */         log("Ignoring invalid value '" + epoch + "' for " + "SOURCE_DATE_EPOCH" + " environment variable", 4);
/*     */       } 
/*     */       
/*  93 */       Date date = d;
/*  94 */       this.customFormats.forEach(cts -> cts.execute(getProject(), date, getLocation()));
/*     */       
/*  96 */       SimpleDateFormat dstamp = new SimpleDateFormat("yyyyMMdd");
/*  97 */       setProperty("DSTAMP", dstamp.format(d));
/*     */       
/*  99 */       SimpleDateFormat tstamp = new SimpleDateFormat("HHmm");
/* 100 */       setProperty("TSTAMP", tstamp.format(d));
/*     */       
/* 102 */       SimpleDateFormat today = new SimpleDateFormat("MMMM d yyyy", Locale.US);
/*     */       
/* 104 */       setProperty("TODAY", today.format(d));
/*     */     }
/* 106 */     catch (Exception e) {
/* 107 */       throw new BuildException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CustomFormat createFormat() {
/* 116 */     CustomFormat cts = new CustomFormat();
/* 117 */     this.customFormats.add(cts);
/* 118 */     return cts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setProperty(String name, String value) {
/* 126 */     getProject().setNewProperty(this.prefix + name, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Date getNow() {
/* 135 */     Optional<Date> now = getNow("ant.tstamp.now.iso", s -> Date.from(Instant.parse(s)), (k, v) -> "magic property " + k + " ignored as '" + v + "' is not in valid ISO pattern");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 140 */     if (now.isPresent()) {
/* 141 */       return now.get();
/*     */     }
/*     */     
/* 144 */     now = getNow("ant.tstamp.now", s -> new Date(1000L * Long.parseLong(s)), (k, v) -> "magic property " + k + " ignored as " + v + " is not a valid number");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 149 */     return now.orElseGet(Date::new);
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
/*     */   protected Optional<Date> getNow(String propertyName, Function<String, Date> map, BiFunction<String, String, String> log) {
/* 161 */     String property = getProject().getProperty(propertyName);
/* 162 */     if (property != null && !property.isEmpty()) {
/*     */       try {
/* 164 */         return Optional.ofNullable(map.apply(property));
/* 165 */       } catch (Exception e) {
/* 166 */         log(log.apply(propertyName, property));
/*     */       } 
/*     */     }
/* 169 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public class CustomFormat
/*     */   {
/*     */     private TimeZone timeZone;
/*     */     
/*     */     private String propertyName;
/*     */     
/*     */     private String pattern;
/*     */     
/*     */     private String language;
/*     */     
/*     */     private String country;
/*     */     
/*     */     private String variant;
/*     */     
/* 188 */     private int offset = 0;
/* 189 */     private int field = 5;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setProperty(String propertyName) {
/* 196 */       this.propertyName = propertyName;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setPattern(String pattern) {
/* 206 */       this.pattern = pattern;
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
/*     */     public void setLocale(String locale) {
/* 219 */       StringTokenizer st = new StringTokenizer(locale, " \t\n\r\f,");
/*     */       try {
/* 221 */         this.language = st.nextToken();
/* 222 */         if (st.hasMoreElements()) {
/* 223 */           this.country = st.nextToken();
/* 224 */           if (st.hasMoreElements()) {
/* 225 */             this.variant = st.nextToken();
/* 226 */             if (st.hasMoreElements()) {
/* 227 */               throw new BuildException("bad locale format", Tstamp.this.getLocation());
/*     */             }
/*     */           } 
/*     */         } else {
/* 231 */           this.country = "";
/*     */         } 
/* 233 */       } catch (NoSuchElementException e) {
/* 234 */         throw new BuildException("bad locale format", e, Tstamp.this.getLocation());
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setTimezone(String id) {
/* 245 */       this.timeZone = TimeZone.getTimeZone(id);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setOffset(int offset) {
/* 253 */       this.offset = offset;
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
/*     */     @Deprecated
/*     */     public void setUnit(String unit) {
/* 268 */       Tstamp.this.log("DEPRECATED - The setUnit(String) method has been deprecated. Use setUnit(Tstamp.Unit) instead.");
/* 269 */       Tstamp.Unit u = new Tstamp.Unit();
/* 270 */       u.setValue(unit);
/* 271 */       this.field = u.getCalendarField();
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
/*     */     public void setUnit(Tstamp.Unit unit) {
/* 291 */       this.field = unit.getCalendarField();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void execute(Project project, Date date, Location location) {
/*     */       SimpleDateFormat sdf;
/* 301 */       if (this.propertyName == null) {
/* 302 */         throw new BuildException("property attribute must be provided", location);
/*     */       }
/*     */       
/* 305 */       if (this.pattern == null) {
/* 306 */         throw new BuildException("pattern attribute must be provided", location);
/*     */       }
/*     */ 
/*     */       
/* 310 */       if (this.language == null) {
/* 311 */         sdf = new SimpleDateFormat(this.pattern);
/* 312 */       } else if (this.variant == null) {
/* 313 */         sdf = new SimpleDateFormat(this.pattern, new Locale(this.language, this.country));
/*     */       } else {
/* 315 */         sdf = new SimpleDateFormat(this.pattern, new Locale(this.language, this.country, this.variant));
/*     */       } 
/* 317 */       if (this.offset != 0) {
/* 318 */         Calendar calendar = Calendar.getInstance();
/* 319 */         calendar.setTime(date);
/* 320 */         calendar.add(this.field, this.offset);
/* 321 */         date = calendar.getTime();
/*     */       } 
/* 323 */       if (this.timeZone != null) {
/* 324 */         sdf.setTimeZone(this.timeZone);
/*     */       }
/* 326 */       Tstamp.this.setProperty(this.propertyName, sdf.format(date));
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
/*     */     private static final String HOUR = "hour";
/*     */     private static final String DAY = "day";
/*     */     private static final String WEEK = "week";
/*     */     private static final String MONTH = "month";
/*     */     private static final String YEAR = "year";
/* 344 */     private static final String[] UNITS = new String[] { "millisecond", "second", "minute", "hour", "day", "week", "month", "year" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 355 */     private Map<String, Integer> calendarFields = new HashMap<>();
/*     */ 
/*     */     
/*     */     public Unit() {
/* 359 */       this.calendarFields.put("millisecond", 
/* 360 */           Integer.valueOf(14));
/* 361 */       this.calendarFields.put("second", Integer.valueOf(13));
/* 362 */       this.calendarFields.put("minute", Integer.valueOf(12));
/* 363 */       this.calendarFields.put("hour", Integer.valueOf(11));
/* 364 */       this.calendarFields.put("day", Integer.valueOf(5));
/* 365 */       this.calendarFields.put("week", Integer.valueOf(3));
/* 366 */       this.calendarFields.put("month", Integer.valueOf(2));
/* 367 */       this.calendarFields.put("year", Integer.valueOf(1));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getCalendarField() {
/* 375 */       String key = getValue().toLowerCase(Locale.ENGLISH);
/* 376 */       return ((Integer)this.calendarFields.get(key)).intValue();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getValues() {
/* 385 */       return UNITS;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Tstamp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */