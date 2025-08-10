/*      */ package com.google.common.net;
/*      */ 
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.J2ktIncompatible;
/*      */ import com.google.common.base.CharMatcher;
/*      */ import com.google.common.base.MoreObjects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.hash.Hashing;
/*      */ import com.google.common.io.ByteStreams;
/*      */ import com.google.common.primitives.Ints;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import java.math.BigInteger;
/*      */ import java.net.Inet4Address;
/*      */ import java.net.Inet6Address;
/*      */ import java.net.InetAddress;
/*      */ import java.net.UnknownHostException;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.util.Arrays;
/*      */ import java.util.Locale;
/*      */ import java.util.Objects;
/*      */ import javax.annotation.CheckForNull;
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
/*      */ @ElementTypesAreNonnullByDefault
/*      */ @J2ktIncompatible
/*      */ @GwtIncompatible
/*      */ public final class InetAddresses
/*      */ {
/*      */   private static final int IPV4_PART_COUNT = 4;
/*      */   private static final int IPV6_PART_COUNT = 8;
/*      */   private static final char IPV4_DELIMITER = '.';
/*      */   private static final char IPV6_DELIMITER = ':';
/*  108 */   private static final CharMatcher IPV4_DELIMITER_MATCHER = CharMatcher.is('.');
/*  109 */   private static final CharMatcher IPV6_DELIMITER_MATCHER = CharMatcher.is(':');
/*  110 */   private static final Inet4Address LOOPBACK4 = (Inet4Address)forString("127.0.0.1");
/*  111 */   private static final Inet4Address ANY4 = (Inet4Address)forString("0.0.0.0");
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
/*      */   private static Inet4Address getInet4Address(byte[] bytes) {
/*  123 */     Preconditions.checkArgument((bytes.length == 4), "Byte array has invalid length for an IPv4 address: %s != 4.", bytes.length);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  129 */     return (Inet4Address)bytesToInetAddress(bytes);
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
/*      */   @CanIgnoreReturnValue
/*      */   public static InetAddress forString(String ipString) {
/*  151 */     byte[] addr = ipStringToBytes(ipString);
/*      */ 
/*      */     
/*  154 */     if (addr == null) {
/*  155 */       throw formatIllegalArgumentException("'%s' is not an IP string literal.", new Object[] { ipString });
/*      */     }
/*      */     
/*  158 */     return bytesToInetAddress(addr);
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
/*      */   public static boolean isInetAddress(String ipString) {
/*  174 */     return (ipStringToBytes(ipString) != null);
/*      */   }
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   private static byte[] ipStringToBytes(String ipStringParam) {
/*  180 */     String ipString = ipStringParam;
/*      */     
/*  182 */     boolean hasColon = false;
/*  183 */     boolean hasDot = false;
/*  184 */     int percentIndex = -1;
/*  185 */     for (int i = 0; i < ipString.length(); i++) {
/*  186 */       char c = ipString.charAt(i);
/*  187 */       if (c == '.')
/*  188 */       { hasDot = true; }
/*  189 */       else if (c == ':')
/*  190 */       { if (hasDot) {
/*  191 */           return null;
/*      */         }
/*  193 */         hasColon = true; }
/*  194 */       else { if (c == '%') {
/*  195 */           percentIndex = i; break;
/*      */         } 
/*  197 */         if (Character.digit(c, 16) == -1) {
/*  198 */           return null;
/*      */         } }
/*      */     
/*      */     } 
/*      */     
/*  203 */     if (hasColon) {
/*  204 */       if (hasDot) {
/*  205 */         ipString = convertDottedQuadToHex(ipString);
/*  206 */         if (ipString == null) {
/*  207 */           return null;
/*      */         }
/*      */       } 
/*  210 */       if (percentIndex != -1) {
/*  211 */         ipString = ipString.substring(0, percentIndex);
/*      */       }
/*  213 */       return textToNumericFormatV6(ipString);
/*  214 */     }  if (hasDot) {
/*  215 */       if (percentIndex != -1) {
/*  216 */         return null;
/*      */       }
/*  218 */       return textToNumericFormatV4(ipString);
/*      */     } 
/*  220 */     return null;
/*      */   }
/*      */   
/*      */   @CheckForNull
/*      */   private static byte[] textToNumericFormatV4(String ipString) {
/*  225 */     if (IPV4_DELIMITER_MATCHER.countIn(ipString) + 1 != 4) {
/*  226 */       return null;
/*      */     }
/*      */     
/*  229 */     byte[] bytes = new byte[4];
/*  230 */     int start = 0;
/*      */ 
/*      */     
/*  233 */     for (int i = 0; i < 4; i++) {
/*  234 */       int end = ipString.indexOf('.', start);
/*  235 */       if (end == -1) {
/*  236 */         end = ipString.length();
/*      */       }
/*      */       try {
/*  239 */         bytes[i] = parseOctet(ipString, start, end);
/*  240 */       } catch (NumberFormatException ex) {
/*  241 */         return null;
/*      */       } 
/*  243 */       start = end + 1;
/*      */     } 
/*      */     
/*  246 */     return bytes;
/*      */   }
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   private static byte[] textToNumericFormatV6(String ipString) {
/*  252 */     int delimiterCount = IPV6_DELIMITER_MATCHER.countIn(ipString);
/*  253 */     if (delimiterCount < 2 || delimiterCount > 8) {
/*  254 */       return null;
/*      */     }
/*  256 */     int partsSkipped = 8 - delimiterCount + 1;
/*  257 */     boolean hasSkip = false;
/*      */ 
/*      */     
/*  260 */     for (int i = 0; i < ipString.length() - 1; i++) {
/*  261 */       if (ipString.charAt(i) == ':' && ipString.charAt(i + 1) == ':') {
/*  262 */         if (hasSkip) {
/*  263 */           return null;
/*      */         }
/*  265 */         hasSkip = true;
/*  266 */         partsSkipped++;
/*  267 */         if (i == 0) {
/*  268 */           partsSkipped++;
/*      */         }
/*  270 */         if (i == ipString.length() - 2) {
/*  271 */           partsSkipped++;
/*      */         }
/*      */       } 
/*      */     } 
/*  275 */     if (ipString.charAt(0) == ':' && ipString.charAt(1) != ':') {
/*  276 */       return null;
/*      */     }
/*  278 */     if (ipString.charAt(ipString.length() - 1) == ':' && ipString
/*  279 */       .charAt(ipString.length() - 2) != ':') {
/*  280 */       return null;
/*      */     }
/*  282 */     if (hasSkip && partsSkipped <= 0) {
/*  283 */       return null;
/*      */     }
/*  285 */     if (!hasSkip && delimiterCount + 1 != 8) {
/*  286 */       return null;
/*      */     }
/*      */     
/*  289 */     ByteBuffer rawBytes = ByteBuffer.allocate(16);
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  294 */       int start = 0;
/*  295 */       if (ipString.charAt(0) == ':') {
/*  296 */         start = 1;
/*      */       }
/*  298 */       while (start < ipString.length()) {
/*  299 */         int end = ipString.indexOf(':', start);
/*  300 */         if (end == -1) {
/*  301 */           end = ipString.length();
/*      */         }
/*  303 */         if (ipString.charAt(start) == ':') {
/*      */           
/*  305 */           for (int j = 0; j < partsSkipped; j++) {
/*  306 */             rawBytes.putShort((short)0);
/*      */           }
/*      */         } else {
/*      */           
/*  310 */           rawBytes.putShort(parseHextet(ipString, start, end));
/*      */         } 
/*  312 */         start = end + 1;
/*      */       } 
/*  314 */     } catch (NumberFormatException ex) {
/*  315 */       return null;
/*      */     } 
/*  317 */     return rawBytes.array();
/*      */   }
/*      */   
/*      */   @CheckForNull
/*      */   private static String convertDottedQuadToHex(String ipString) {
/*  322 */     int lastColon = ipString.lastIndexOf(':');
/*  323 */     String initialPart = ipString.substring(0, lastColon + 1);
/*  324 */     String dottedQuad = ipString.substring(lastColon + 1);
/*  325 */     byte[] quad = textToNumericFormatV4(dottedQuad);
/*  326 */     if (quad == null) {
/*  327 */       return null;
/*      */     }
/*  329 */     String penultimate = Integer.toHexString((quad[0] & 0xFF) << 8 | quad[1] & 0xFF);
/*  330 */     String ultimate = Integer.toHexString((quad[2] & 0xFF) << 8 | quad[3] & 0xFF);
/*  331 */     return initialPart + penultimate + ":" + ultimate;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte parseOctet(String ipString, int start, int end) {
/*  337 */     int length = end - start;
/*  338 */     if (length <= 0 || length > 3) {
/*  339 */       throw new NumberFormatException();
/*      */     }
/*      */ 
/*      */     
/*  343 */     if (length > 1 && ipString.charAt(start) == '0') {
/*  344 */       throw new NumberFormatException();
/*      */     }
/*  346 */     int octet = 0;
/*  347 */     for (int i = start; i < end; i++) {
/*  348 */       octet *= 10;
/*  349 */       int digit = Character.digit(ipString.charAt(i), 10);
/*  350 */       if (digit < 0) {
/*  351 */         throw new NumberFormatException();
/*      */       }
/*  353 */       octet += digit;
/*      */     } 
/*  355 */     if (octet > 255) {
/*  356 */       throw new NumberFormatException();
/*      */     }
/*  358 */     return (byte)octet;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static short parseHextet(String ipString, int start, int end) {
/*  364 */     int length = end - start;
/*  365 */     if (length <= 0 || length > 4) {
/*  366 */       throw new NumberFormatException();
/*      */     }
/*  368 */     int hextet = 0;
/*  369 */     for (int i = start; i < end; i++) {
/*  370 */       hextet <<= 4;
/*  371 */       hextet |= Character.digit(ipString.charAt(i), 16);
/*      */     } 
/*  373 */     return (short)hextet;
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
/*      */   private static InetAddress bytesToInetAddress(byte[] addr) {
/*      */     try {
/*  388 */       return InetAddress.getByAddress(addr);
/*  389 */     } catch (UnknownHostException e) {
/*  390 */       throw new AssertionError(e);
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
/*      */   
/*      */   public static String toAddrString(InetAddress ip) {
/*  410 */     Preconditions.checkNotNull(ip);
/*  411 */     if (ip instanceof Inet4Address)
/*      */     {
/*      */       
/*  414 */       return Objects.<String>requireNonNull(ip.getHostAddress());
/*      */     }
/*  416 */     Preconditions.checkArgument(ip instanceof Inet6Address);
/*  417 */     byte[] bytes = ip.getAddress();
/*  418 */     int[] hextets = new int[8];
/*  419 */     for (int i = 0; i < hextets.length; i++) {
/*  420 */       hextets[i] = Ints.fromBytes((byte)0, (byte)0, bytes[2 * i], bytes[2 * i + 1]);
/*      */     }
/*  422 */     compressLongestRunOfZeroes(hextets);
/*  423 */     return hextetsToIPv6String(hextets);
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
/*      */   private static void compressLongestRunOfZeroes(int[] hextets) {
/*  435 */     int bestRunStart = -1;
/*  436 */     int bestRunLength = -1;
/*  437 */     int runStart = -1;
/*  438 */     for (int i = 0; i < hextets.length + 1; i++) {
/*  439 */       if (i < hextets.length && hextets[i] == 0) {
/*  440 */         if (runStart < 0) {
/*  441 */           runStart = i;
/*      */         }
/*  443 */       } else if (runStart >= 0) {
/*  444 */         int runLength = i - runStart;
/*  445 */         if (runLength > bestRunLength) {
/*  446 */           bestRunStart = runStart;
/*  447 */           bestRunLength = runLength;
/*      */         } 
/*  449 */         runStart = -1;
/*      */       } 
/*      */     } 
/*  452 */     if (bestRunLength >= 2) {
/*  453 */       Arrays.fill(hextets, bestRunStart, bestRunStart + bestRunLength, -1);
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
/*      */   private static String hextetsToIPv6String(int[] hextets) {
/*  470 */     StringBuilder buf = new StringBuilder(39);
/*  471 */     boolean lastWasNumber = false;
/*  472 */     for (int i = 0; i < hextets.length; i++) {
/*  473 */       boolean thisIsNumber = (hextets[i] >= 0);
/*  474 */       if (thisIsNumber) {
/*  475 */         if (lastWasNumber) {
/*  476 */           buf.append(':');
/*      */         }
/*  478 */         buf.append(Integer.toHexString(hextets[i]));
/*      */       }
/*  480 */       else if (i == 0 || lastWasNumber) {
/*  481 */         buf.append("::");
/*      */       } 
/*      */       
/*  484 */       lastWasNumber = thisIsNumber;
/*      */     } 
/*  486 */     return buf.toString();
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
/*      */   public static String toUriString(InetAddress ip) {
/*  509 */     if (ip instanceof Inet6Address) {
/*  510 */       return "[" + toAddrString(ip) + "]";
/*      */     }
/*  512 */     return toAddrString(ip);
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
/*      */   public static InetAddress forUriString(String hostAddr) {
/*  535 */     InetAddress addr = forUriStringNoThrow(hostAddr);
/*  536 */     if (addr == null) {
/*  537 */       throw formatIllegalArgumentException("Not a valid URI IP literal: '%s'", new Object[] { hostAddr });
/*      */     }
/*      */     
/*  540 */     return addr;
/*      */   } @CheckForNull
/*      */   private static InetAddress forUriStringNoThrow(String hostAddr) {
/*      */     String ipString;
/*      */     int expectBytes;
/*  545 */     Preconditions.checkNotNull(hostAddr);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  550 */     if (hostAddr.startsWith("[") && hostAddr.endsWith("]")) {
/*  551 */       ipString = hostAddr.substring(1, hostAddr.length() - 1);
/*  552 */       expectBytes = 16;
/*      */     } else {
/*  554 */       ipString = hostAddr;
/*  555 */       expectBytes = 4;
/*      */     } 
/*      */ 
/*      */     
/*  559 */     byte[] addr = ipStringToBytes(ipString);
/*  560 */     if (addr == null || addr.length != expectBytes) {
/*  561 */       return null;
/*      */     }
/*      */     
/*  564 */     return bytesToInetAddress(addr);
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
/*      */   public static boolean isUriInetAddress(String ipString) {
/*  580 */     return (forUriStringNoThrow(ipString) != null);
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
/*      */   public static boolean isCompatIPv4Address(Inet6Address ip) {
/*  602 */     if (!ip.isIPv4CompatibleAddress()) {
/*  603 */       return false;
/*      */     }
/*      */     
/*  606 */     byte[] bytes = ip.getAddress();
/*  607 */     if (bytes[12] == 0 && bytes[13] == 0 && bytes[14] == 0 && (bytes[15] == 0 || bytes[15] == 1))
/*      */     {
/*      */ 
/*      */       
/*  611 */       return false;
/*      */     }
/*      */     
/*  614 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Inet4Address getCompatIPv4Address(Inet6Address ip) {
/*  625 */     Preconditions.checkArgument(
/*  626 */         isCompatIPv4Address(ip), "Address '%s' is not IPv4-compatible.", toAddrString(ip));
/*      */     
/*  628 */     return getInet4Address(Arrays.copyOfRange(ip.getAddress(), 12, 16));
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
/*      */   public static boolean is6to4Address(Inet6Address ip) {
/*  644 */     byte[] bytes = ip.getAddress();
/*  645 */     return (bytes[0] == 32 && bytes[1] == 2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Inet4Address get6to4IPv4Address(Inet6Address ip) {
/*  656 */     Preconditions.checkArgument(is6to4Address(ip), "Address '%s' is not a 6to4 address.", toAddrString(ip));
/*      */     
/*  658 */     return getInet4Address(Arrays.copyOfRange(ip.getAddress(), 2, 6));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final class TeredoInfo
/*      */   {
/*      */     private final Inet4Address server;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final Inet4Address client;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final int port;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final int flags;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public TeredoInfo(@CheckForNull Inet4Address server, @CheckForNull Inet4Address client, int port, int flags) {
/*  692 */       Preconditions.checkArgument((port >= 0 && port <= 65535), "port '%s' is out of range (0 <= port <= 0xffff)", port);
/*      */       
/*  694 */       Preconditions.checkArgument((flags >= 0 && flags <= 65535), "flags '%s' is out of range (0 <= flags <= 0xffff)", flags);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  699 */       this.server = (Inet4Address)MoreObjects.firstNonNull(server, InetAddresses.ANY4);
/*  700 */       this.client = (Inet4Address)MoreObjects.firstNonNull(client, InetAddresses.ANY4);
/*  701 */       this.port = port;
/*  702 */       this.flags = flags;
/*      */     }
/*      */     
/*      */     public Inet4Address getServer() {
/*  706 */       return this.server;
/*      */     }
/*      */     
/*      */     public Inet4Address getClient() {
/*  710 */       return this.client;
/*      */     }
/*      */     
/*      */     public int getPort() {
/*  714 */       return this.port;
/*      */     }
/*      */     
/*      */     public int getFlags() {
/*  718 */       return this.flags;
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
/*      */   public static boolean isTeredoAddress(Inet6Address ip) {
/*  731 */     byte[] bytes = ip.getAddress();
/*  732 */     return (bytes[0] == 32 && bytes[1] == 1 && bytes[2] == 0 && bytes[3] == 0);
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
/*      */   public static TeredoInfo getTeredoInfo(Inet6Address ip) {
/*  746 */     Preconditions.checkArgument(isTeredoAddress(ip), "Address '%s' is not a Teredo address.", toAddrString(ip));
/*      */     
/*  748 */     byte[] bytes = ip.getAddress();
/*  749 */     Inet4Address server = getInet4Address(Arrays.copyOfRange(bytes, 4, 8));
/*      */     
/*  751 */     int flags = ByteStreams.newDataInput(bytes, 8).readShort() & 0xFFFF;
/*      */ 
/*      */     
/*  754 */     int port = (ByteStreams.newDataInput(bytes, 10).readShort() ^ 0xFFFFFFFF) & 0xFFFF;
/*      */     
/*  756 */     byte[] clientBytes = Arrays.copyOfRange(bytes, 12, 16);
/*  757 */     for (int i = 0; i < clientBytes.length; i++)
/*      */     {
/*  759 */       clientBytes[i] = (byte)(clientBytes[i] ^ 0xFFFFFFFF);
/*      */     }
/*  761 */     Inet4Address client = getInet4Address(clientBytes);
/*      */     
/*  763 */     return new TeredoInfo(server, client, port, flags);
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
/*      */   public static boolean isIsatapAddress(Inet6Address ip) {
/*  783 */     if (isTeredoAddress(ip)) {
/*  784 */       return false;
/*      */     }
/*      */     
/*  787 */     byte[] bytes = ip.getAddress();
/*      */     
/*  789 */     if ((bytes[8] | 0x3) != 3)
/*      */     {
/*      */ 
/*      */       
/*  793 */       return false;
/*      */     }
/*      */     
/*  796 */     return (bytes[9] == 0 && bytes[10] == 94 && bytes[11] == -2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Inet4Address getIsatapIPv4Address(Inet6Address ip) {
/*  807 */     Preconditions.checkArgument(isIsatapAddress(ip), "Address '%s' is not an ISATAP address.", toAddrString(ip));
/*      */     
/*  809 */     return getInet4Address(Arrays.copyOfRange(ip.getAddress(), 12, 16));
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
/*      */   public static boolean hasEmbeddedIPv4ClientAddress(Inet6Address ip) {
/*  825 */     return (isCompatIPv4Address(ip) || is6to4Address(ip) || isTeredoAddress(ip));
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
/*      */   public static Inet4Address getEmbeddedIPv4ClientAddress(Inet6Address ip) {
/*  841 */     if (isCompatIPv4Address(ip)) {
/*  842 */       return getCompatIPv4Address(ip);
/*      */     }
/*      */     
/*  845 */     if (is6to4Address(ip)) {
/*  846 */       return get6to4IPv4Address(ip);
/*      */     }
/*      */     
/*  849 */     if (isTeredoAddress(ip)) {
/*  850 */       return getTeredoInfo(ip).getClient();
/*      */     }
/*      */     
/*  853 */     throw formatIllegalArgumentException("'%s' has no embedded IPv4 address.", new Object[] { toAddrString(ip) });
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
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isMappedIPv4Address(String ipString) {
/*  879 */     byte[] bytes = ipStringToBytes(ipString);
/*  880 */     if (bytes != null && bytes.length == 16) {
/*  881 */       int i; for (i = 0; i < 10; i++) {
/*  882 */         if (bytes[i] != 0) {
/*  883 */           return false;
/*      */         }
/*      */       } 
/*  886 */       for (i = 10; i < 12; i++) {
/*  887 */         if (bytes[i] != -1) {
/*  888 */           return false;
/*      */         }
/*      */       } 
/*  891 */       return true;
/*      */     } 
/*  893 */     return false;
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Inet4Address getCoercedIPv4Address(InetAddress ip) {
/*  920 */     if (ip instanceof Inet4Address) {
/*  921 */       return (Inet4Address)ip;
/*      */     }
/*      */ 
/*      */     
/*  925 */     byte[] bytes = ip.getAddress();
/*  926 */     boolean leadingBytesOfZero = true;
/*  927 */     for (int i = 0; i < 15; i++) {
/*  928 */       if (bytes[i] != 0) {
/*  929 */         leadingBytesOfZero = false;
/*      */         break;
/*      */       } 
/*      */     } 
/*  933 */     if (leadingBytesOfZero && bytes[15] == 1)
/*  934 */       return LOOPBACK4; 
/*  935 */     if (leadingBytesOfZero && bytes[15] == 0) {
/*  936 */       return ANY4;
/*      */     }
/*      */     
/*  939 */     Inet6Address ip6 = (Inet6Address)ip;
/*  940 */     long addressAsLong = 0L;
/*  941 */     if (hasEmbeddedIPv4ClientAddress(ip6)) {
/*  942 */       addressAsLong = getEmbeddedIPv4ClientAddress(ip6).hashCode();
/*      */     } else {
/*      */       
/*  945 */       addressAsLong = ByteBuffer.wrap(ip6.getAddress(), 0, 8).getLong();
/*      */     } 
/*      */ 
/*      */     
/*  949 */     int coercedHash = Hashing.murmur3_32_fixed().hashLong(addressAsLong).asInt();
/*      */ 
/*      */     
/*  952 */     coercedHash |= 0xE0000000;
/*      */ 
/*      */ 
/*      */     
/*  956 */     if (coercedHash == -1) {
/*  957 */       coercedHash = -2;
/*      */     }
/*      */     
/*  960 */     return getInet4Address(Ints.toByteArray(coercedHash));
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
/*      */   public static int coerceToInteger(InetAddress ip) {
/*  982 */     return ByteStreams.newDataInput(getCoercedIPv4Address(ip).getAddress()).readInt();
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
/*      */   public static BigInteger toBigInteger(InetAddress address) {
/*  995 */     return new BigInteger(1, address.getAddress());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Inet4Address fromInteger(int address) {
/* 1005 */     return getInet4Address(Ints.toByteArray(address));
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
/*      */   public static Inet4Address fromIPv4BigInteger(BigInteger address) {
/* 1017 */     return (Inet4Address)fromBigInteger(address, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Inet6Address fromIPv6BigInteger(BigInteger address) {
/* 1028 */     return (Inet6Address)fromBigInteger(address, true);
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
/*      */   private static InetAddress fromBigInteger(BigInteger address, boolean isIpv6) {
/* 1042 */     Preconditions.checkArgument((address.signum() >= 0), "BigInteger must be greater than or equal to 0");
/*      */     
/* 1044 */     int numBytes = isIpv6 ? 16 : 4;
/*      */     
/* 1046 */     byte[] addressBytes = address.toByteArray();
/* 1047 */     byte[] targetCopyArray = new byte[numBytes];
/*      */     
/* 1049 */     int srcPos = Math.max(0, addressBytes.length - numBytes);
/* 1050 */     int copyLength = addressBytes.length - srcPos;
/* 1051 */     int destPos = numBytes - copyLength;
/*      */ 
/*      */     
/* 1054 */     for (int i = 0; i < srcPos; i++) {
/* 1055 */       if (addressBytes[i] != 0) {
/* 1056 */         throw formatIllegalArgumentException("BigInteger cannot be converted to InetAddress because it has more than %d bytes: %s", new Object[] {
/*      */ 
/*      */               
/* 1059 */               Integer.valueOf(numBytes), address
/*      */             });
/*      */       }
/*      */     } 
/*      */     
/* 1064 */     System.arraycopy(addressBytes, srcPos, targetCopyArray, destPos, copyLength);
/*      */     
/*      */     try {
/* 1067 */       return InetAddress.getByAddress(targetCopyArray);
/* 1068 */     } catch (UnknownHostException impossible) {
/* 1069 */       throw new AssertionError(impossible);
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
/*      */   public static InetAddress fromLittleEndianByteArray(byte[] addr) throws UnknownHostException {
/* 1084 */     byte[] reversed = new byte[addr.length];
/* 1085 */     for (int i = 0; i < addr.length; i++) {
/* 1086 */       reversed[i] = addr[addr.length - i - 1];
/*      */     }
/* 1088 */     return InetAddress.getByAddress(reversed);
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
/*      */   public static InetAddress decrement(InetAddress address) {
/* 1101 */     byte[] addr = address.getAddress();
/* 1102 */     int i = addr.length - 1;
/* 1103 */     while (i >= 0 && addr[i] == 0) {
/* 1104 */       addr[i] = -1;
/* 1105 */       i--;
/*      */     } 
/*      */     
/* 1108 */     Preconditions.checkArgument((i >= 0), "Decrementing %s would wrap.", address);
/*      */     
/* 1110 */     addr[i] = (byte)(addr[i] - 1);
/* 1111 */     return bytesToInetAddress(addr);
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
/*      */   public static InetAddress increment(InetAddress address) {
/* 1124 */     byte[] addr = address.getAddress();
/* 1125 */     int i = addr.length - 1;
/* 1126 */     while (i >= 0 && addr[i] == -1) {
/* 1127 */       addr[i] = 0;
/* 1128 */       i--;
/*      */     } 
/*      */     
/* 1131 */     Preconditions.checkArgument((i >= 0), "Incrementing %s would wrap.", address);
/*      */     
/* 1133 */     addr[i] = (byte)(addr[i] + 1);
/* 1134 */     return bytesToInetAddress(addr);
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
/*      */   public static boolean isMaximum(InetAddress address) {
/* 1146 */     byte[] addr = address.getAddress();
/* 1147 */     for (byte b : addr) {
/* 1148 */       if (b != -1) {
/* 1149 */         return false;
/*      */       }
/*      */     } 
/* 1152 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private static IllegalArgumentException formatIllegalArgumentException(String format, Object... args) {
/* 1157 */     return new IllegalArgumentException(String.format(Locale.ROOT, format, args));
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/net/InetAddresses.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */