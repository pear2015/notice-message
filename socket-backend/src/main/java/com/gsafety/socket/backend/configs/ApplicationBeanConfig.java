package com.gsafety.socket.backend.configs;

import com.gsafety.springboot.common.utils.HttpClientUtil;
import com.gsafety.springboot.common.utils.HttpClientUtilImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by xiaodiming on 2017/3/7.
 */
@Configuration
public class ApplicationBeanConfig {
    /**
     * appInitBean
     *
     * @return
     */
    @Bean
    public AppInit appInitBean() {
        return new AppInit();
    }

    /**
     * 配置httpclient帮助类bean到容器
     *
     * @return the http client util
     */
    @Bean
    public HttpClientUtil configHttpClientUtil() {
        return new HttpClientUtilImpl();
    }
}
