/*     */ package org.apache.hc.core5.net;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class InetAddressUtils
/*     */ {
/*     */   public static final byte IPV4 = 1;
/*     */   public static final byte IPV6 = 4;
/*     */   private static final String IPV4_BASIC_PATTERN_STRING = "(([1-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){1}(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){2}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])";
/*  67 */   private static final Pattern IPV4_PATTERN = Pattern.compile("^(([1-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){1}(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){2}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
/*     */ 
/*     */   
/*  70 */   private static final Pattern IPV4_MAPPED_IPV6_PATTERN = Pattern.compile("^::[fF]{4}:(([1-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){1}(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){2}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
/*     */ 
/*     */   
/*  73 */   private static final Pattern IPV6_STD_PATTERN = Pattern.compile("^[0-9a-fA-F]{1,4}(:[0-9a-fA-F]{1,4}){7}$");
/*     */ 
/*     */ 
/*     */   
/*  77 */   private static final Pattern IPV6_HEX_COMPRESSED_PATTERN = Pattern.compile("^(([0-9A-Fa-f]{1,4}(:[0-9A-Fa-f]{1,4}){0,5})?)::(([0-9A-Fa-f]{1,4}(:[0-9A-Fa-f]{1,4}){0,5})?)$");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final char COLON_CHAR = ':';
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MAX_COLON_COUNT = 7;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isIPv4Address(String input) {
/*  97 */     return IPV4_PATTERN.matcher(input).matches();
/*     */   }
/*     */   
/*     */   public static boolean isIPv4MappedIPv64Address(String input) {
/* 101 */     return IPV4_MAPPED_IPV6_PATTERN.matcher(input).matches();
/*     */   }
/*     */   
/*     */   static boolean hasValidIPv6ColonCount(String input) {
/* 105 */     int colonCount = 0;
/* 106 */     for (int i = 0; i < input.length(); i++) {
/* 107 */       if (input.charAt(i) == ':') {
/* 108 */         colonCount++;
/*     */       }
/*     */     } 
/*     */     
/* 112 */     return (colonCount >= 2 && colonCount <= 7);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isIPv6StdAddress(String input) {
/* 122 */     return (hasValidIPv6ColonCount(input) && IPV6_STD_PATTERN.matcher(input).matches());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isIPv6HexCompressedAddress(String input) {
/* 132 */     return (hasValidIPv6ColonCount(input) && IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isIPv6Address(String input) {
/* 142 */     return (isIPv6StdAddress(input) || isIPv6HexCompressedAddress(input));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isIPv6URLBracketedAddress(String input) {
/* 153 */     return (input.startsWith("[") && input.endsWith("]") && isIPv6Address(input.substring(1, input.length() - 1)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void formatAddress(StringBuilder buffer, SocketAddress socketAddress) {
/* 164 */     Args.notNull(buffer, "buffer");
/* 165 */     if (socketAddress instanceof InetSocketAddress) {
/* 166 */       InetSocketAddress socketaddr = (InetSocketAddress)socketAddress;
/* 167 */       InetAddress inetaddr = socketaddr.getAddress();
/* 168 */       if (inetaddr != null) {
/* 169 */         buffer.append(inetaddr.getHostAddress()).append(':').append(socketaddr.getPort());
/*     */       } else {
/* 171 */         buffer.append(socketAddress);
/*     */       } 
/*     */     } else {
/* 174 */       buffer.append(socketAddress);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getCanonicalLocalHostName() {
/*     */     try {
/* 185 */       InetAddress localHost = InetAddress.getLocalHost();
/* 186 */       return localHost.getCanonicalHostName();
/* 187 */     } catch (UnknownHostException ex) {
/* 188 */       return "localhost";
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/net/InetAddressUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */