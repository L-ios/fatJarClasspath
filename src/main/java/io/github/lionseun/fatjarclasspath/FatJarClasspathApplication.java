package io.github.lionseun.fatjarclasspath;

import io.github.lionseun.fatjarclasspath.itfc.PathScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FatJarClasspathApplication {

    @Autowired
    private PathScanner pathScanner;

    public static void main(String[] args) {
        SpringApplication.run(FatJarClasspathApplication.class, args);
    }

}
