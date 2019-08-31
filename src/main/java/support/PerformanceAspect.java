package support;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
public class PerformanceAspect {
    @Around("performancePointcut()")
    public static Object doPerformance(final ProceedingJoinPoint pjp) throws Throwable {
        final long warnLimitMilliseconds = 500;
        final StopWatch stopWatch = new StopWatch("메서드 성능");

        stopWatch.start();
        final Object result = pjp.proceed();
        stopWatch.stop();
        final double totalTimeSeconds = stopWatch.getTotalTimeSeconds();

        final Class<?> targetClass = pjp.getTarget().getClass();
        final Logger logger = LoggerFactory.getLogger(targetClass);
        final String logText = String.format("%s - %s", pjp.getSignature(), stopWatch.prettyPrint());
        if (totalTimeSeconds >= warnLimitMilliseconds) {
            logger.warn(logText);
        } else {
            logger.debug(logText);
        }

        return result;
    }

    @Around("within(myblog..*)")
    public static Object doLogMethodCalling(final ProceedingJoinPoint pjp) throws Throwable {

        final Logger logger = LoggerFactory.getLogger(pjp.getTarget().getClass());
        final Object result =  pjp.proceed();

        final String methodName = pjp.getSignature().getName();
        final Object[] methodArgs = pjp.getArgs();

        logger.debug("Method Information: \r\n----------------------------------------------------------\r\n\tMethodName: {}\r\n\tMethodArgs: {}\r\n\tMethodReturnValue: {}\r\n----------------------------------------------------------", methodName, methodArgs, result);

        return result;
    }

    @Pointcut("within(myblog..*)")
    public void performancePointcut() {

    }
}
