package io.github.lionseun.fatjarclasspath.config;

import io.github.lionseun.fatjarclasspath.itfc.PathScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.*;

@Configuration
public class TestConfig {
    Logger log = LoggerFactory.getLogger(TestConfig.class);

    @Bean
    public PathScanner pathScanner() {
        return new PathScanner() {

            @PostConstruct
            public void init() {
                String content = this.getPath("classpath:test.properties");
                log.info("----------------------------");
                log.info(content);
                log.info("----------------------------");
            }
            public InputStream getConfigAsStream(String filePath) {
                InputStream inStream = null;
                try {
                    if (filePath.startsWith("classpath:")) {
                        String resourcePath = filePath.substring("classpath:".length());
                        inStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
                    } else {
                        inStream = getFileAsStream(filePath);
                    }

                    if (inStream == null) {
                        log.error("load config file error, file : " + filePath);
                        return null;
                    }

                    return inStream;
                } catch (Exception ex) {
                    log.error("load config file error, file : " + filePath, ex);
                    return null;
                }
            }

            private InputStream getFileAsStream(String filePath) throws FileNotFoundException {
                InputStream inStream = null;
                File file = new File(filePath);
                if (file.exists()) {
                    inStream = new FileInputStream(file);
                } else {
                    inStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
                }
                return inStream;
            }

            @Override
            public String getPath(String url) {

                try {
                    InputStream is = getConfigAsStream(url);
                    BufferedReader reader = null;
                    if (is != null) {
                        StringBuffer sb = new StringBuffer();
                        try {
                            InputStreamReader inputStreamReader = new InputStreamReader(is, "UTF-8");
                            reader = new BufferedReader(inputStreamReader);
                            String tempString = null;
                            while ((tempString = reader.readLine()) != null) {
                                tempString = tempString.trim();
                                if (!tempString.startsWith("#") && !tempString.startsWith("//")) {
                                    sb.append(tempString);
                                }
                            }
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (reader != null) {
                                try {
                                    reader.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        return sb.toString();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }
        };
    }
}
