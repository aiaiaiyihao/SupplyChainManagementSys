package org.yihao.productserver.Config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

@Component
@Slf4j
public class Secrets extends Properties {

    private static final String PATH = "s3secrets.txt";  // remove leading slash

    @PostConstruct
    public void init() {
        log.info("prepare to load the secrets from classpath: {}", PATH);
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(PATH)) {
            if (is == null) {
                throw new FileNotFoundException("Secrets file not found in classpath");
            }
            load(is);
        } catch (IOException e) {
            log.error("read secrets file error : {}", e.getMessage());
        }
    }

}
