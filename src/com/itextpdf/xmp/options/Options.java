/*     */ package com.itextpdf.xmp.options;
/*     */ 
/*     */ import com.itextpdf.xmp.XMPException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Options
/*     */ {
/*  48 */   private int options = 0;
/*     */   
/*  50 */   private Map optionNames = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Options() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Options(int options) throws XMPException {
/*  70 */     assertOptionsValid(options);
/*  71 */     setOptions(options);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/*  80 */     this.options = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExactly(int optionBits) {
/*  90 */     return (getOptions() == optionBits);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsAllOptions(int optionBits) {
/* 100 */     return ((getOptions() & optionBits) == optionBits);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsOneOf(int optionBits) {
/* 110 */     return ((getOptions() & optionBits) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean getOption(int optionBit) {
/* 120 */     return ((this.options & optionBit) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOption(int optionBits, boolean value) {
/* 130 */     this.options = value ? (this.options | optionBits) : (this.options & (optionBits ^ 0xFFFFFFFF));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOptions() {
/* 140 */     return this.options;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOptions(int options) throws XMPException {
/* 150 */     assertOptionsValid(options);
/* 151 */     this.options = options;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 160 */     return (getOptions() == ((Options)obj).getOptions());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 169 */     return getOptions();
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
/*     */   public String getOptionsString() {
/* 181 */     if (this.options != 0) {
/*     */       
/* 183 */       StringBuffer sb = new StringBuffer();
/* 184 */       int theBits = this.options;
/* 185 */       while (theBits != 0) {
/*     */         
/* 187 */         int oneLessBit = theBits & theBits - 1;
/* 188 */         int singleBit = theBits ^ oneLessBit;
/* 189 */         String bitName = getOptionName(singleBit);
/* 190 */         sb.append(bitName);
/* 191 */         if (oneLessBit != 0)
/*     */         {
/* 193 */           sb.append(" | ");
/*     */         }
/* 195 */         theBits = oneLessBit;
/*     */       } 
/* 197 */       return sb.toString();
/*     */     } 
/*     */ 
/*     */     
/* 201 */     return "<none>";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 211 */     return "0x" + Integer.toHexString(this.options);
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
/*     */   protected abstract int getValidOptions();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract String defineOptionName(int paramInt);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void assertConsistency(int options) throws XMPException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void assertOptionsValid(int options) throws XMPException {
/* 257 */     int invalidOptions = options & (getValidOptions() ^ 0xFFFFFFFF);
/* 258 */     if (invalidOptions == 0) {
/*     */       
/* 260 */       assertConsistency(options);
/*     */     }
/*     */     else {
/*     */       
/* 264 */       throw new XMPException("The option bit(s) 0x" + Integer.toHexString(invalidOptions) + " are invalid!", 103);
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
/*     */   private String getOptionName(int option) {
/* 279 */     Map<Integer, String> optionsNames = procureOptionNames();
/*     */     
/* 281 */     Integer key = new Integer(option);
/* 282 */     String result = (String)optionsNames.get(key);
/* 283 */     if (result == null) {
/*     */       
/* 285 */       result = defineOptionName(option);
/* 286 */       if (result != null) {
/*     */         
/* 288 */         optionsNames.put(key, result);
/*     */       }
/*     */       else {
/*     */         
/* 292 */         result = "<option name not defined>";
/*     */       } 
/*     */     } 
/*     */     
/* 296 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map procureOptionNames() {
/* 305 */     if (this.optionNames == null)
/*     */     {
/* 307 */       this.optionNames = new HashMap<Object, Object>();
/*     */     }
/* 309 */     return this.optionNames;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/xmp/options/Options.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */