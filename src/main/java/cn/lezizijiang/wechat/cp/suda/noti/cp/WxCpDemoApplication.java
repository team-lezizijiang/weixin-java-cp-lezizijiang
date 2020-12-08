package cn.lezizijiang.wechat.cp.suda.noti.cp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@EnableScheduling
@SpringBootApplication
public class WxCpDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxCpDemoApplication.class, args);
    }
}
