/*     */ package com.itextpdf.xmp.impl;
/*     */ 
/*     */ import com.itextpdf.xmp.XMPDateTime;
/*     */ import com.itextpdf.xmp.XMPException;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.DecimalFormatSymbols;
/*     */ import java.util.Locale;
/*     */ import java.util.SimpleTimeZone;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ISO8601Converter
/*     */ {
/*     */   public static XMPDateTime parse(String iso8601String) throws XMPException {
/*  99 */     return parse(iso8601String, new XMPDateTimeImpl());
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
/*     */   public static XMPDateTime parse(String iso8601String, XMPDateTime binValue) throws XMPException {
/* 111 */     if (iso8601String == null)
/*     */     {
/* 113 */       throw new XMPException("Parameter must not be null", 4);
/*     */     }
/* 115 */     if (iso8601String.length() == 0)
/*     */     {
/* 117 */       return binValue;
/*     */     }
/*     */     
/* 120 */     ParseState input = new ParseState(iso8601String);
/*     */ 
/*     */     
/* 123 */     if (input.ch(0) == '-')
/*     */     {
/* 125 */       input.skip();
/*     */     }
/*     */ 
/*     */     
/* 129 */     int value = input.gatherInt("Invalid year in date string", 9999);
/* 130 */     if (input.hasNext() && input.ch() != '-')
/*     */     {
/* 132 */       throw new XMPException("Invalid date string, after year", 5);
/*     */     }
/*     */     
/* 135 */     if (input.ch(0) == '-')
/*     */     {
/* 137 */       value = -value;
/*     */     }
/* 139 */     binValue.setYear(value);
/* 140 */     if (!input.hasNext())
/*     */     {
/* 142 */       return binValue;
/*     */     }
/* 144 */     input.skip();
/*     */ 
/*     */ 
/*     */     
/* 148 */     value = input.gatherInt("Invalid month in date string", 12);
/* 149 */     if (input.hasNext() && input.ch() != '-')
/*     */     {
/* 151 */       throw new XMPException("Invalid date string, after month", 5);
/*     */     }
/* 153 */     binValue.setMonth(value);
/* 154 */     if (!input.hasNext())
/*     */     {
/* 156 */       return binValue;
/*     */     }
/* 158 */     input.skip();
/*     */ 
/*     */ 
/*     */     
/* 162 */     value = input.gatherInt("Invalid day in date string", 31);
/* 163 */     if (input.hasNext() && input.ch() != 'T')
/*     */     {
/* 165 */       throw new XMPException("Invalid date string, after day", 5);
/*     */     }
/* 167 */     binValue.setDay(value);
/* 168 */     if (!input.hasNext())
/*     */     {
/* 170 */       return binValue;
/*     */     }
/* 172 */     input.skip();
/*     */ 
/*     */     
/* 175 */     value = input.gatherInt("Invalid hour in date string", 23);
/* 176 */     binValue.setHour(value);
/* 177 */     if (!input.hasNext())
/*     */     {
/* 179 */       return binValue;
/*     */     }
/*     */ 
/*     */     
/* 183 */     if (input.ch() == ':') {
/*     */       
/* 185 */       input.skip();
/* 186 */       value = input.gatherInt("Invalid minute in date string", 59);
/* 187 */       if (input.hasNext() && input
/* 188 */         .ch() != ':' && input.ch() != 'Z' && input.ch() != '+' && input.ch() != '-')
/*     */       {
/* 190 */         throw new XMPException("Invalid date string, after minute", 5);
/*     */       }
/* 192 */       binValue.setMinute(value);
/*     */     } 
/*     */     
/* 195 */     if (!input.hasNext())
/*     */     {
/* 197 */       return binValue;
/*     */     }
/* 199 */     if (input.hasNext() && input.ch() == ':') {
/*     */       
/* 201 */       input.skip();
/* 202 */       value = input.gatherInt("Invalid whole seconds in date string", 59);
/* 203 */       if (input.hasNext() && input.ch() != '.' && input.ch() != 'Z' && input
/* 204 */         .ch() != '+' && input.ch() != '-')
/*     */       {
/* 206 */         throw new XMPException("Invalid date string, after whole seconds", 5);
/*     */       }
/*     */       
/* 209 */       binValue.setSecond(value);
/* 210 */       if (input.ch() == '.')
/*     */       {
/* 212 */         input.skip();
/* 213 */         int digits = input.pos();
/* 214 */         value = input.gatherInt("Invalid fractional seconds in date string", 999999999);
/* 215 */         if (input.hasNext() && input
/* 216 */           .ch() != 'Z' && input.ch() != '+' && input.ch() != '-')
/*     */         {
/* 218 */           throw new XMPException("Invalid date string, after fractional second", 5);
/*     */         }
/*     */         
/* 221 */         digits = input.pos() - digits;
/* 222 */         for (; digits > 9; digits--)
/*     */         {
/* 224 */           value /= 10;
/*     */         }
/* 226 */         for (; digits < 9; digits++)
/*     */         {
/* 228 */           value *= 10;
/*     */         }
/* 230 */         binValue.setNanoSecond(value);
/*     */       }
/*     */     
/* 233 */     } else if (input.ch() != 'Z' && input.ch() != '+' && input.ch() != '-') {
/*     */       
/* 235 */       throw new XMPException("Invalid date string, after time", 5);
/*     */     } 
/*     */ 
/*     */     
/* 239 */     int tzSign = 0;
/* 240 */     int tzHour = 0;
/* 241 */     int tzMinute = 0;
/*     */     
/* 243 */     if (!input.hasNext())
/*     */     {
/*     */       
/* 246 */       return binValue;
/*     */     }
/* 248 */     if (input.ch() == 'Z') {
/*     */       
/* 250 */       input.skip();
/*     */     }
/* 252 */     else if (input.hasNext()) {
/*     */       
/* 254 */       if (input.ch() == '+') {
/*     */         
/* 256 */         tzSign = 1;
/*     */       }
/* 258 */       else if (input.ch() == '-') {
/*     */         
/* 260 */         tzSign = -1;
/*     */       }
/*     */       else {
/*     */         
/* 264 */         throw new XMPException("Time zone must begin with 'Z', '+', or '-'", 5);
/*     */       } 
/*     */ 
/*     */       
/* 268 */       input.skip();
/*     */       
/* 270 */       tzHour = input.gatherInt("Invalid time zone hour in date string", 23);
/* 271 */       if (input.hasNext())
/*     */       {
/* 273 */         if (input.ch() == ':') {
/*     */           
/* 275 */           input.skip();
/*     */ 
/*     */           
/* 278 */           tzMinute = input.gatherInt("Invalid time zone minute in date string", 59);
/*     */         }
/*     */         else {
/*     */           
/* 282 */           throw new XMPException("Invalid date string, after time zone hour", 5);
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 289 */     int offset = (tzHour * 3600 * 1000 + tzMinute * 60 * 1000) * tzSign;
/* 290 */     binValue.setTimeZone(new SimpleTimeZone(offset, ""));
/*     */     
/* 292 */     if (input.hasNext())
/*     */     {
/* 294 */       throw new XMPException("Invalid date string, extra chars at end", 5);
/*     */     }
/*     */ 
/*     */     
/* 298 */     return binValue;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String render(XMPDateTime dateTime) {
/* 336 */     StringBuffer buffer = new StringBuffer();
/*     */     
/* 338 */     if (dateTime.hasDate()) {
/*     */ 
/*     */       
/* 341 */       DecimalFormat df = new DecimalFormat("0000", new DecimalFormatSymbols(Locale.ENGLISH));
/* 342 */       buffer.append(df.format(dateTime.getYear()));
/* 343 */       if (dateTime.getMonth() == 0)
/*     */       {
/* 345 */         return buffer.toString();
/*     */       }
/*     */ 
/*     */       
/* 349 */       df.applyPattern("'-'00");
/* 350 */       buffer.append(df.format(dateTime.getMonth()));
/* 351 */       if (dateTime.getDay() == 0)
/*     */       {
/* 353 */         return buffer.toString();
/*     */       }
/*     */ 
/*     */       
/* 357 */       buffer.append(df.format(dateTime.getDay()));
/*     */ 
/*     */       
/* 360 */       if (dateTime.hasTime()) {
/*     */ 
/*     */         
/* 363 */         buffer.append('T');
/* 364 */         df.applyPattern("00");
/* 365 */         buffer.append(df.format(dateTime.getHour()));
/* 366 */         buffer.append(':');
/* 367 */         buffer.append(df.format(dateTime.getMinute()));
/*     */ 
/*     */         
/* 370 */         if (dateTime.getSecond() != 0 || dateTime.getNanoSecond() != 0) {
/*     */           
/* 372 */           double seconds = dateTime.getSecond() + dateTime.getNanoSecond() / 1.0E9D;
/*     */           
/* 374 */           df.applyPattern(":00.#########");
/* 375 */           buffer.append(df.format(seconds));
/*     */         } 
/*     */ 
/*     */         
/* 379 */         if (dateTime.hasTimeZone()) {
/*     */ 
/*     */           
/* 382 */           long timeInMillis = dateTime.getCalendar().getTimeInMillis();
/* 383 */           int offset = dateTime.getTimeZone().getOffset(timeInMillis);
/* 384 */           if (offset == 0) {
/*     */ 
/*     */             
/* 387 */             buffer.append('Z');
/*     */           }
/*     */           else {
/*     */             
/* 391 */             int thours = offset / 3600000;
/* 392 */             int tminutes = Math.abs(offset % 3600000 / 60000);
/* 393 */             df.applyPattern("+00;-00");
/* 394 */             buffer.append(df.format(thours));
/* 395 */             df.applyPattern(":00");
/* 396 */             buffer.append(df.format(tminutes));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 401 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/xmp/impl/ISO8601Converter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */