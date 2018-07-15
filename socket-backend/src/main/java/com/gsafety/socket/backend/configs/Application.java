package com.gsafety.socket.backend.configs;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import com.gsafety.socket.common.XseedSettings;
import com.gsafety.socket.backend.aspectj.AppWideExceptionHandle;
import com.gsafety.socket.common.util.HelpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.net.InetAddress;
import java.util.Arrays;

/**
 * Created by Administrator on 2017/3/2.
 * socket 不需要数据库 默认排除其注入
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableConfigurationProperties({XseedSettings.class})
@ComponentScan({"com.gsafety.socket.*"})
public class Application {

    private Logger logger = LoggerFactory.getLogger(AppWideExceptionHandle.class);

    @Value("${socket.port}")
    private int port;

    @Value("${socket.host}")
    private  String  host;

    @Value("${socket.isSelectSwarm}")
    private boolean isSelectSwarm;

    /**
     * Socket io server socket io server.
     *
     * @return the socket io server
     * 1、服务器配置主机名 和监听的端口
     * 2、根据配置创建服务器对象
     */
    @Bean
    public SocketIOServer socketIOServer(){
        Configuration config = new Configuration();
        InetAddress inetAddress= HelpUtil.getLocalHostLANAddress();
        config.setHostname(isSelectSwarm?inetAddress.getHostAddress():host);
        config.setPort(port);
        return new SocketIOServer(config);
    }

    /**
     * Spring annotation scanner spring annotation scanner.
     *
     * @param socketServer the socket server
     * @return the spring annotation scanner
     */
    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
        return new SpringAnnotationScanner(socketServer);
    }

    /**
     * mian
     *
     * @param args string[]
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args);
    }

    /**
     * commandLineRunner
     *
     * @param ctx ApplicationContext
     * @return command line runner
     */
    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            logger.info("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                logger.info(beanName);
            }

        };
    }
}
