package com.gsafety.socket.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by zhengyali on 2018/3/15.
 */
public class HelpUtil {
    private static Logger logger = LoggerFactory.getLogger(HelpUtil.class);

    /**
     * 方法构造
     */
    private HelpUtil() {
    }
    /**
     * 获取本机IP
     *
     * @return
     * @throws Exception
     */
    public static InetAddress getLocalHostLANAddress() {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    candidateAddress=getCurrentAddr((InetAddress) inetAddrs.nextElement(),candidateAddress);
                }
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            return candidateAddress != null?candidateAddress:InetAddress.getLocalHost();
        } catch (Exception e) {
            logger.error("Exception: getLocalHostLANAddress error.{}", e);
        }
        return null;
    }

    /**
     * 获取当前的地址
     * @param inetAddr
     * @param candidateAddress
     * @return
     */
    private static InetAddress  getCurrentAddr(InetAddress inetAddr,InetAddress candidateAddress){
        if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
            // 如果是site-local地址，就是它了
            return inetAddr.isSiteLocalAddress()?inetAddr:(candidateAddress!=null?candidateAddress:inetAddr);
        }else{
            return candidateAddress;
        }

    }
    /**
     * 获取当前连接服务地址
     *
     * @param port
     * @param contextPath
     * @return
     */
    public static String getServerUrl(String port, String contextPath) {
        try {
            InetAddress inetAddress = getLocalHostLANAddress();
            if (inetAddress == null) {
                return null;
            }
            return "http://" + inetAddress.getHostAddress() + ":" + port + contextPath;
        } catch (Exception e) {
            logger.error("Exception: getServerUrl error.{}", e);
        }
        return null;
    }
}
