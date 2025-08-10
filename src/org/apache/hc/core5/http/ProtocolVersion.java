/*     */ package org.apache.hc.core5.http;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*     */ public class ProtocolVersion
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 8950662842175091068L;
/*     */   private final String protocol;
/*     */   private final int major;
/*     */   private final int minor;
/*     */   
/*     */   public ProtocolVersion(String protocol, int major, int minor) {
/*  71 */     this.protocol = (String)Args.notNull(protocol, "Protocol name");
/*  72 */     this.major = Args.notNegative(major, "Protocol minor version");
/*  73 */     this.minor = Args.notNegative(minor, "Protocol minor version");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getProtocol() {
/*  82 */     return this.protocol;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getMajor() {
/*  91 */     return this.major;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getMinor() {
/* 100 */     return this.minor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/* 111 */     return this.protocol.hashCode() ^ this.major * 100000 ^ this.minor;
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
/*     */   public final boolean equals(int major, int minor) {
/* 123 */     return (this.major == major && this.minor == minor);
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
/*     */   public final boolean equals(Object obj) {
/* 141 */     if (this == obj) {
/* 142 */       return true;
/*     */     }
/* 144 */     if (!(obj instanceof ProtocolVersion)) {
/* 145 */       return false;
/*     */     }
/* 147 */     ProtocolVersion that = (ProtocolVersion)obj;
/*     */     
/* 149 */     return (this.protocol.equals(that.protocol) && this.major == that.major && this.minor == that.minor);
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
/*     */   public String format() {
/* 161 */     StringBuilder buffer = new StringBuilder();
/* 162 */     buffer.append(this.protocol);
/* 163 */     buffer.append('/');
/* 164 */     buffer.append(this.major);
/* 165 */     buffer.append('.');
/* 166 */     buffer.append(this.minor);
/* 167 */     return buffer.toString();
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
/*     */   public boolean isComparable(ProtocolVersion that) {
/* 181 */     return (that != null && this.protocol.equals(that.protocol));
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
/*     */   public int compareToVersion(ProtocolVersion that) {
/* 202 */     Args.notNull(that, "Protocol version");
/* 203 */     Args.check(this.protocol.equals(that.protocol), "Versions for different protocols cannot be compared: %s %s", new Object[] { this, that });
/*     */     
/* 205 */     int delta = getMajor() - that.getMajor();
/* 206 */     if (delta == 0) {
/* 207 */       delta = getMinor() - that.getMinor();
/*     */     }
/* 209 */     return delta;
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
/*     */   public final boolean greaterEquals(ProtocolVersion version) {
/* 224 */     return (isComparable(version) && compareToVersion(version) >= 0);
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
/*     */   public final boolean lessEquals(ProtocolVersion version) {
/* 239 */     return (isComparable(version) && compareToVersion(version) <= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 250 */     return format();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/ProtocolVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */