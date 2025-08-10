/*     */ package org.apache.tools.ant.types.selectors;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.text.DateFormat;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Locale;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.Parameter;
/*     */ import org.apache.tools.ant.types.TimeComparison;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DateSelector
/*     */   extends BaseExtendSelector
/*     */ {
/*     */   public static final String MILLIS_KEY = "millis";
/*     */   public static final String DATETIME_KEY = "datetime";
/*     */   public static final String CHECKDIRS_KEY = "checkdirs";
/*     */   public static final String GRANULARITY_KEY = "granularity";
/*     */   public static final String WHEN_KEY = "when";
/*     */   public static final String PATTERN_KEY = "pattern";
/*  53 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */   
/*  55 */   private long millis = -1L;
/*  56 */   private String dateTime = null;
/*     */   private boolean includeDirs = false;
/*  58 */   private long granularity = FILE_UTILS.getFileTimestampGranularity();
/*     */   private String pattern;
/*  60 */   private TimeComparison when = TimeComparison.EQUAL;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  66 */     StringBuilder buf = new StringBuilder("{dateselector date: ");
/*  67 */     buf.append(this.dateTime);
/*  68 */     buf.append(" compare: ").append(this.when.getValue());
/*  69 */     buf.append(" granularity: ").append(this.granularity);
/*  70 */     if (this.pattern != null) {
/*  71 */       buf.append(" pattern: ").append(this.pattern);
/*     */     }
/*  73 */     buf.append("}");
/*  74 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMillis(long millis) {
/*  84 */     this.millis = millis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getMillis() {
/*  92 */     if (this.dateTime != null) {
/*  93 */       validate();
/*     */     }
/*  95 */     return this.millis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDatetime(String dateTime) {
/* 105 */     this.dateTime = dateTime;
/* 106 */     this.millis = -1L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCheckdirs(boolean includeDirs) {
/* 115 */     this.includeDirs = includeDirs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGranularity(int granularity) {
/* 124 */     this.granularity = granularity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWhen(TimeComparisons tcmp) {
/* 134 */     setWhen(tcmp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWhen(TimeComparison t) {
/* 142 */     this.when = t;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPattern(String pattern) {
/* 151 */     this.pattern = pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameters(Parameter... parameters) {
/* 161 */     super.setParameters(parameters);
/* 162 */     if (parameters != null) {
/* 163 */       for (Parameter parameter : parameters) {
/* 164 */         String paramname = parameter.getName();
/* 165 */         if ("millis".equalsIgnoreCase(paramname)) {
/*     */           try {
/* 167 */             setMillis(Long.parseLong(parameter.getValue()));
/* 168 */           } catch (NumberFormatException nfe) {
/* 169 */             setError("Invalid millisecond setting " + parameter
/* 170 */                 .getValue());
/*     */           } 
/* 172 */         } else if ("datetime".equalsIgnoreCase(paramname)) {
/* 173 */           setDatetime(parameter.getValue());
/* 174 */         } else if ("checkdirs".equalsIgnoreCase(paramname)) {
/* 175 */           setCheckdirs(Project.toBoolean(parameter.getValue()));
/* 176 */         } else if ("granularity".equalsIgnoreCase(paramname)) {
/*     */           try {
/* 178 */             setGranularity(Integer.parseInt(parameter.getValue()));
/* 179 */           } catch (NumberFormatException nfe) {
/* 180 */             setError("Invalid granularity setting " + parameter
/* 181 */                 .getValue());
/*     */           } 
/* 183 */         } else if ("when".equalsIgnoreCase(paramname)) {
/* 184 */           setWhen(new TimeComparison(parameter.getValue()));
/* 185 */         } else if ("pattern".equalsIgnoreCase(paramname)) {
/* 186 */           setPattern(parameter.getValue());
/*     */         } else {
/* 188 */           setError("Invalid parameter " + paramname);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void verifySettings() {
/* 199 */     if (this.dateTime == null && this.millis < 0L) {
/* 200 */       setError("You must provide a datetime or the number of milliseconds.");
/*     */     }
/* 202 */     else if (this.millis < 0L && this.dateTime != null) {
/* 203 */       String p = (this.pattern == null) ? "MM/dd/yyyy hh:mm a" : this.pattern;
/*     */ 
/*     */       
/* 206 */       DateFormat df = (this.pattern == null) ? new SimpleDateFormat(p, Locale.US) : new SimpleDateFormat(p);
/*     */       
/*     */       try {
/* 209 */         setMillis(df.parse(this.dateTime).getTime());
/* 210 */         if (this.millis < 0L) {
/* 211 */           setError("Date of " + this.dateTime + " results in negative milliseconds value relative to epoch (January 1, 1970, 00:00:00 GMT).");
/*     */         
/*     */         }
/*     */       }
/* 215 */       catch (ParseException pe) {
/* 216 */         setError("Date of " + this.dateTime + " Cannot be parsed correctly. It should be in '" + p + "' format.", pe);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSelected(File basedir, String filename, File file) {
/* 233 */     validate();
/* 234 */     return ((file.isDirectory() && !this.includeDirs) || this.when
/* 235 */       .evaluate(file.lastModified(), this.millis, this.granularity));
/*     */   }
/*     */   
/*     */   public static class TimeComparisons extends TimeComparison {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/DateSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */