package com.github.ray.common;

import com.github.ray.common.service.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.github.ray"})
public class MagicByteNettyStarter implements ApplicationRunner {
    @Autowired
    NettyServer nettyServer;

    public static void main(String args[]){
        SpringApplication.run(MagicByteNettyStarter.class, args);

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        nettyServer.start();
    }
}
