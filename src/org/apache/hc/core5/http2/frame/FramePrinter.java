/*     */ package org.apache.hc.core5.http2.frame;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Objects;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.http2.H2Error;
/*     */ import org.apache.hc.core5.http2.config.H2Param;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Internal
/*     */ public final class FramePrinter
/*     */ {
/*     */   public void printFrameInfo(RawFrame frame, Appendable appendable) throws IOException {
/*  44 */     appendable.append("stream ").append(Integer.toString(frame.getStreamId())).append(" frame: ");
/*  45 */     FrameType type = FrameType.valueOf(frame.getType());
/*  46 */     appendable.append(Objects.toString(type))
/*  47 */       .append(" (0x").append(Integer.toHexString(frame.getType())).append("); flags: ");
/*  48 */     int flags = frame.getFlags();
/*  49 */     if (flags > 0)
/*  50 */       switch (type) {
/*     */         case SETTINGS:
/*     */         case PING:
/*  53 */           if ((flags & FrameFlag.ACK.value) > 0) {
/*  54 */             appendable.append(FrameFlag.ACK.name()).append(" ");
/*     */           }
/*     */           break;
/*     */         case DATA:
/*  58 */           if ((flags & FrameFlag.END_STREAM.value) > 0) {
/*  59 */             appendable.append(FrameFlag.END_STREAM.name()).append(" ");
/*     */           }
/*  61 */           if ((flags & FrameFlag.PADDED.value) > 0) {
/*  62 */             appendable.append(FrameFlag.PADDED.name()).append(" ");
/*     */           }
/*     */           break;
/*     */         case HEADERS:
/*  66 */           if ((flags & FrameFlag.END_STREAM.value) > 0) {
/*  67 */             appendable.append(FrameFlag.END_STREAM.name()).append(" ");
/*     */           }
/*  69 */           if ((flags & FrameFlag.END_HEADERS.value) > 0) {
/*  70 */             appendable.append(FrameFlag.END_HEADERS.name()).append(" ");
/*     */           }
/*  72 */           if ((flags & FrameFlag.PADDED.value) > 0) {
/*  73 */             appendable.append(FrameFlag.PADDED.name()).append(" ");
/*     */           }
/*  75 */           if ((flags & FrameFlag.PRIORITY.value) > 0) {
/*  76 */             appendable.append(FrameFlag.PRIORITY.name()).append(" ");
/*     */           }
/*     */           break;
/*     */         case PUSH_PROMISE:
/*  80 */           if ((flags & FrameFlag.END_HEADERS.value) > 0) {
/*  81 */             appendable.append(FrameFlag.END_HEADERS.name()).append(" ");
/*     */           }
/*  83 */           if ((flags & FrameFlag.PADDED.value) > 0) {
/*  84 */             appendable.append(FrameFlag.PADDED.name()).append(" ");
/*     */           }
/*     */           break;
/*     */         case CONTINUATION:
/*  88 */           if ((flags & FrameFlag.END_HEADERS.value) > 0) {
/*  89 */             appendable.append(FrameFlag.END_HEADERS.name()).append(" ");
/*     */           }
/*     */           break;
/*     */       }  
/*  93 */     appendable.append("(0x").append(Integer.toHexString(flags)).append("); length: ").append(Integer.toString(frame.getLength()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void printPayload(RawFrame frame, Appendable appendable) throws IOException {
/*  98 */     FrameType type = FrameType.valueOf(frame.getType());
/*  99 */     ByteBuffer buf = frame.getPayloadContent();
/* 100 */     if (buf != null) {
/*     */       
/* 102 */       switch (type) {
/*     */         case SETTINGS:
/* 104 */           if (buf.remaining() % 6 == 0) {
/* 105 */             while (buf.hasRemaining()) {
/* 106 */               int code = buf.getShort();
/* 107 */               H2Param param = H2Param.valueOf(code);
/* 108 */               int value = buf.getInt();
/* 109 */               if (param != null) {
/* 110 */                 appendable.append(param.name());
/*     */               } else {
/* 112 */                 appendable.append("0x").append(Integer.toHexString(code));
/*     */               } 
/* 114 */               appendable.append(": ").append(Integer.toString(value)).append("\r\n");
/*     */             } 
/*     */           } else {
/* 117 */             appendable.append("Invalid\r\n");
/*     */           } 
/*     */           return;
/*     */         case RST_STREAM:
/* 121 */           if (buf.remaining() == 4) {
/* 122 */             appendable.append("Code ");
/* 123 */             int code = buf.getInt();
/* 124 */             H2Error error = H2Error.getByCode(code);
/* 125 */             if (error != null) {
/* 126 */               appendable.append(error.name());
/*     */             } else {
/* 128 */               appendable.append("0x").append(Integer.toHexString(code));
/*     */             } 
/* 130 */             appendable.append("\r\n");
/*     */           } else {
/* 132 */             appendable.append("Invalid\r\n");
/*     */           } 
/*     */           return;
/*     */         case GOAWAY:
/* 136 */           if (buf.remaining() >= 8) {
/* 137 */             int lastStream = buf.getInt();
/* 138 */             appendable.append("Last stream ").append(Integer.toString(lastStream)).append("\r\n");
/* 139 */             appendable.append("Code ");
/* 140 */             int code2 = buf.getInt();
/* 141 */             H2Error error2 = H2Error.getByCode(code2);
/* 142 */             if (error2 != null) {
/* 143 */               appendable.append(error2.name());
/*     */             } else {
/* 145 */               appendable.append("0x").append(Integer.toHexString(code2));
/*     */             } 
/* 147 */             appendable.append("\r\n");
/* 148 */             byte[] tmp = new byte[buf.remaining()];
/* 149 */             buf.get(tmp);
/* 150 */             appendable.append(new String(tmp, StandardCharsets.US_ASCII));
/* 151 */             appendable.append("\r\n");
/*     */           } else {
/* 153 */             appendable.append("Invalid\r\n");
/*     */           } 
/*     */           return;
/*     */         case WINDOW_UPDATE:
/* 157 */           if (buf.remaining() == 4) {
/* 158 */             int increment = buf.getInt();
/* 159 */             appendable.append("Increment ").append(Integer.toString(increment)).append("\r\n");
/*     */           } else {
/* 161 */             appendable.append("Invalid\r\n");
/*     */           } 
/*     */           return;
/*     */         case PUSH_PROMISE:
/* 165 */           if (buf.remaining() > 4) {
/* 166 */             int streamId = buf.getInt();
/* 167 */             appendable.append("Promised stream ").append(Integer.toString(streamId)).append("\r\n");
/* 168 */             printData(buf, appendable);
/*     */           } else {
/* 170 */             appendable.append("Invalid\r\n");
/*     */           } 
/*     */           return;
/*     */       } 
/* 174 */       printData(frame.getPayload(), appendable);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void printData(ByteBuffer data, Appendable appendable) throws IOException {
/* 180 */     ByteBuffer buf = data.duplicate();
/* 181 */     byte[] line = new byte[16];
/* 182 */     while (buf.hasRemaining()) {
/* 183 */       int chunk = Math.min(buf.remaining(), line.length);
/* 184 */       buf.get(line, 0, chunk);
/*     */       int i;
/* 186 */       for (i = 0; i < chunk; i++) {
/* 187 */         char ch = (char)line[i];
/* 188 */         if (ch > ' ' && ch <= '') {
/* 189 */           appendable.append(ch);
/* 190 */         } else if (Character.isWhitespace(ch)) {
/* 191 */           appendable.append(' ');
/*     */         } else {
/* 193 */           appendable.append('.');
/*     */         } 
/*     */       } 
/* 196 */       for (i = chunk; i < 17; i++) {
/* 197 */         appendable.append(' ');
/*     */       }
/*     */       
/* 200 */       for (i = 0; i < chunk; i++) {
/* 201 */         appendable.append(' ');
/* 202 */         int b = line[i] & 0xFF;
/* 203 */         String s = Integer.toHexString(b);
/* 204 */         if (s.length() == 1) {
/* 205 */           appendable.append("0");
/*     */         }
/* 207 */         appendable.append(s);
/*     */       } 
/* 209 */       appendable.append("\r\n");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/frame/FramePrinter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */