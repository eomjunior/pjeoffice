/*     */ package org.apache.tools.ant.types.selectors;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.apache.tools.ant.types.Comparison;
/*     */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*     */ import org.apache.tools.ant.types.Parameter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SizeSelector
/*     */   extends BaseExtendSelector
/*     */ {
/*     */   private static final int KILO = 1000;
/*     */   private static final int KIBI = 1024;
/*     */   private static final int KIBI_POS = 4;
/*     */   private static final int MEGA = 1000000;
/*     */   private static final int MEGA_POS = 9;
/*     */   private static final int MEBI = 1048576;
/*     */   private static final int MEBI_POS = 13;
/*     */   private static final long GIGA = 1000000000L;
/*     */   private static final int GIGA_POS = 18;
/*     */   private static final long GIBI = 1073741824L;
/*     */   private static final int GIBI_POS = 22;
/*     */   private static final long TERA = 1000000000000L;
/*     */   private static final int TERA_POS = 27;
/*     */   private static final long TEBI = 1099511627776L;
/*     */   private static final int TEBI_POS = 31;
/*     */   private static final int END_POS = 36;
/*     */   public static final String SIZE_KEY = "value";
/*     */   public static final String UNITS_KEY = "units";
/*     */   public static final String WHEN_KEY = "when";
/*  59 */   private long size = -1L;
/*  60 */   private long multiplier = 1L;
/*  61 */   private long sizelimit = -1L;
/*  62 */   private Comparison when = Comparison.EQUAL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  71 */     return String.format("{sizeselector value: %d compare: %s}", new Object[] {
/*  72 */           Long.valueOf(this.sizelimit), this.when.getValue()
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(long size) {
/*  83 */     this.size = size;
/*  84 */     if (this.multiplier != 0L && size > -1L) {
/*  85 */       this.sizelimit = size * this.multiplier;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUnits(ByteUnits units) {
/* 116 */     int i = units.getIndex();
/* 117 */     this.multiplier = 0L;
/* 118 */     if (i > -1 && i < 4) {
/* 119 */       this.multiplier = 1000L;
/* 120 */     } else if (i < 9) {
/* 121 */       this.multiplier = 1024L;
/* 122 */     } else if (i < 13) {
/* 123 */       this.multiplier = 1000000L;
/* 124 */     } else if (i < 18) {
/* 125 */       this.multiplier = 1048576L;
/* 126 */     } else if (i < 22) {
/* 127 */       this.multiplier = 1000000000L;
/* 128 */     } else if (i < 27) {
/* 129 */       this.multiplier = 1073741824L;
/* 130 */     } else if (i < 31) {
/* 131 */       this.multiplier = 1000000000000L;
/* 132 */     } else if (i < 36) {
/* 133 */       this.multiplier = 1099511627776L;
/*     */     } 
/* 135 */     if (this.multiplier > 0L && this.size > -1L) {
/* 136 */       this.sizelimit = this.size * this.multiplier;
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
/*     */   public void setWhen(SizeComparisons when) {
/* 148 */     this.when = when;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameters(Parameter... parameters) {
/* 159 */     super.setParameters(parameters);
/* 160 */     if (parameters != null) {
/* 161 */       for (Parameter parameter : parameters) {
/* 162 */         String paramname = parameter.getName();
/* 163 */         if ("value".equalsIgnoreCase(paramname)) {
/*     */           try {
/* 165 */             setValue(Long.parseLong(parameter.getValue()));
/* 166 */           } catch (NumberFormatException nfe) {
/* 167 */             setError("Invalid size setting " + parameter
/* 168 */                 .getValue());
/*     */           } 
/* 170 */         } else if ("units".equalsIgnoreCase(paramname)) {
/* 171 */           ByteUnits units = new ByteUnits();
/* 172 */           units.setValue(parameter.getValue());
/* 173 */           setUnits(units);
/* 174 */         } else if ("when".equalsIgnoreCase(paramname)) {
/* 175 */           SizeComparisons scmp = new SizeComparisons();
/* 176 */           scmp.setValue(parameter.getValue());
/* 177 */           setWhen(scmp);
/*     */         } else {
/* 179 */           setError("Invalid parameter " + paramname);
/*     */         } 
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
/*     */   public void verifySettings() {
/* 196 */     if (this.size < 0L) {
/* 197 */       setError("The value attribute is required, and must be positive");
/* 198 */     } else if (this.multiplier < 1L) {
/* 199 */       setError("Invalid Units supplied, must be K,Ki,M,Mi,G,Gi,T,or Ti");
/* 200 */     } else if (this.sizelimit < 0L) {
/* 201 */       setError("Internal error: Code is not setting sizelimit correctly");
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
/*     */   
/*     */   public boolean isSelected(File basedir, String filename, File file) {
/* 218 */     validate();
/*     */ 
/*     */     
/* 221 */     if (file.isDirectory()) {
/* 222 */       return true;
/*     */     }
/* 224 */     long diff = file.length() - this.sizelimit;
/* 225 */     return this.when.evaluate((diff == 0L) ? 0 : (int)(diff / Math.abs(diff)));
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ByteUnits
/*     */     extends EnumeratedAttribute
/*     */   {
/*     */     public String[] getValues() {
/* 252 */       return new String[] { "K", "k", "kilo", "KILO", "Ki", "KI", "ki", "kibi", "KIBI", "M", "m", "mega", "MEGA", "Mi", "MI", "mi", "mebi", "MEBI", "G", "g", "giga", "GIGA", "Gi", "GI", "gi", "gibi", "GIBI", "T", "t", "tera", "TERA", "Ti", "TI", "ti", "tebi", "TEBI" };
/*     */     }
/*     */   }
/*     */   
/*     */   public static class SizeComparisons extends Comparison {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/SizeSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */