package geulsam.archive.global.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* geulsam.archive.domain..*Controller.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();

        long startTime = System.currentTimeMillis(); // Start time

        // Proceed with the method execution
        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis(); // End time
        long duration = endTime - startTime; // Calculate duration

        // Log in the desired format: method name - start time - time taken
        logger.info("{} - Start time: {} - End time: {} - Duration: {} ms",
                methodName,
                startTime,
                endTime,
                duration
        );

        return result;
    }
}
