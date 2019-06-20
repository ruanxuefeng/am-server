package com.am.server.common.util;

import javax.servlet.http.HttpServletRequest;

/**
 *  IP工具类
 * @author 阮雪峰
 * @date 2018/8/1 15:16
 */
object IpUtils {
    private const val UNKNOWN = "unknown"
    @JvmStatic fun getIpAddress(request: HttpServletRequest): String {
        var ip = request.getHeader("x-forwarded-for")
        if (ip == null || ip.isEmpty()|| UNKNOWN.toLowerCase() == ip) {
            ip = request.getHeader("Proxy-Client-IP")
        }
        if (ip == null || ip.isEmpty()|| UNKNOWN.toLowerCase() == ip) {
            ip = request.getHeader("WL-Proxy-Client-IP")
        }
        if (ip == null || ip.isEmpty()|| UNKNOWN.toLowerCase() == ip) {
            ip = request.getHeader("HTTP_CLIENT_IP")
        }
        if (ip == null || ip.isEmpty()|| UNKNOWN.toLowerCase() == ip) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR")
        }
        if (ip == null || ip.isEmpty()|| UNKNOWN.toLowerCase() == ip) {
            ip = request.remoteAddr
        }
        return ip
    }

}
