package myblog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
@ComponentScan({"support", "myblog"})
public class MyblogApplication {
    private static final Logger logger = LoggerFactory.getLogger(MyblogApplication.class);

    public static void main(final String[] args) {
        SpringApplication.run(MyblogApplication.class, args);
        logger.info("Loaded Myblog Application");
    }
}
