package kz.baltabayev.audits.aspects;

import kz.baltabayev.audits.domain.Audit;
import kz.baltabayev.audits.repository.AuditRepository;
import kz.baltabayev.audits.service.AuditServiceLogging;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

import static kz.baltabayev.audits.domain.types.ActionType.*;
import static kz.baltabayev.audits.domain.types.AuditType.FAIL;
import static kz.baltabayev.audits.domain.types.AuditType.SUCCESS;

@Aspect
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditRepository auditRepository;

    private static final Logger logger = LoggerFactory.getLogger(AuditAspect.class);

    @Pointcut("execution(public * kz.baltabayev.audits.service.SecurityServiceLogging.register(..))")
    public void registrationPointcut() {
    }

    @Pointcut("execution(public * kz.baltabayev.audits.service.SecurityServiceLogging.authorization(..))")
    public void authenticationPointcut() {
    }

    @Pointcut("execution(public * kz.baltabayev.audits.service.PlayerServiceLogging.getPlayerBalance(..))")
    public void getBalancePointcut() {
    }

    @Pointcut("execution(public * kz.baltabayev.audits.service.TransactionServiceLogging.credit(..))")
    public void creditPointcut() {
    }

    @Pointcut("execution(public * kz.baltabayev.audits.service.TransactionServiceLogging.debit(..))")
    public void debitPointcut() {
    }

    @Pointcut("execution(public * kz.baltabayev.audits.service.TransactionServiceLogging.getPlayerHistory(..))")
    public void viewTransactionHistoryPointcut() {
    }

    @AfterReturning(pointcut = "registrationPointcut()", returning = "result")
    public void afterSuccessfulRegistration(JoinPoint joinPoint, ResponseEntity<?> result) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            Object firstArgument = args[0];
            Class<?> argumentClass = firstArgument.getClass();
            try {
                Field usernameField = argumentClass.getDeclaredField("username");
                usernameField.setAccessible(true);
                String username = (String) usernameField.get(firstArgument);

                Audit audit = Audit.builder()
                        .auditType(SUCCESS)
                        .actionType(REGISTRATION)
                        .playerFullName(username)
                        .build();
                auditRepository.save(audit);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                logger.error(String.valueOf(e));
            }
        }
    }

    @AfterThrowing(pointcut = "registrationPointcut()", throwing = "exception")
    public void afterFailedRegistration(JoinPoint joinPoint, Throwable exception) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            Object firstArgument = args[0];
            Class<?> argumentClass = firstArgument.getClass();
            try {
                Field usernameField = argumentClass.getDeclaredField("username");
                usernameField.setAccessible(true);
                String username = (String) usernameField.get(firstArgument);

                Audit audit = Audit.builder()
                        .auditType(FAIL)
                        .actionType(REGISTRATION)
                        .playerFullName(username)
                        .build();
                auditRepository.save(audit);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                logger.error(String.valueOf(e));
            }
        }
    }

    @AfterReturning(pointcut = "authenticationPointcut()", returning = "result")
    public void afterSuccessfulAuthentication(JoinPoint joinPoint, ResponseEntity<?> result) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            Object firstArgument = args[0];
            Class<?> argumentClass = firstArgument.getClass();
            try {
                Field usernameField = argumentClass.getDeclaredField("username");
                usernameField.setAccessible(true);
                String username = (String) usernameField.get(firstArgument);

                Audit audit = Audit.builder()
                        .auditType(SUCCESS)
                        .actionType(AUTHORIZATION)
                        .playerFullName(username)
                        .build();
                auditRepository.save(audit);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                logger.error(String.valueOf(e));
            }
        }
    }

    @AfterThrowing(pointcut = "authenticationPointcut()", throwing = "exception")
    public void afterFailedAuthentication(JoinPoint joinPoint, Throwable exception) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            Object firstArgument = args[0];
            Class<?> argumentClass = firstArgument.getClass();
            try {
                Field usernameField = argumentClass.getDeclaredField("username");
                usernameField.setAccessible(true);
                String username = (String) usernameField.get(firstArgument);

                Audit audit = Audit.builder()
                        .auditType(FAIL)
                        .actionType(AUTHORIZATION)
                        .playerFullName(username)
                        .build();
                auditRepository.save(audit);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                logger.error(String.valueOf(e));
            }
        }
    }

    @AfterReturning(pointcut = "getBalancePointcut()", returning = "result")
    public void afterSuccessfulGetBalance(JoinPoint joinPoint, ResponseEntity<?> result) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            Object firstArgument = args[0];
            Class<?> argumentClass = firstArgument.getClass();
            try {
                Field usernameField = argumentClass.getDeclaredField("username");
                usernameField.setAccessible(true);
                String username = (String) usernameField.get(firstArgument);

                Audit audit = Audit.builder()
                        .auditType(SUCCESS)
                        .actionType(BALANCE_INQUIRY)
                        .playerFullName(username)
                        .build();
                auditRepository.save(audit);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                logger.error(String.valueOf(e));
            }
        }
    }

    @AfterThrowing(pointcut = "getBalancePointcut()", throwing = "exception")
    public void afterFailedGetBalance(JoinPoint joinPoint, Throwable exception) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            Object firstArgument = args[0];
            Class<?> argumentClass = firstArgument.getClass();
            try {
                Field usernameField = argumentClass.getDeclaredField("username");
                usernameField.setAccessible(true);
                String username = (String) usernameField.get(firstArgument);

                Audit audit = Audit.builder()
                        .auditType(FAIL)
                        .actionType(BALANCE_INQUIRY)
                        .playerFullName(username)
                        .build();
                auditRepository.save(audit);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                logger.error(String.valueOf(e));
            }
        }
    }

    @AfterReturning(pointcut = "creditPointcut()", returning = "result")
    public void afterSuccessfulCredit(JoinPoint joinPoint, ResponseEntity<?> result) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            Object firstArgument = args[0];
            Class<?> argumentClass = firstArgument.getClass();
            try {
                Field usernameField = argumentClass.getDeclaredField("username");
                usernameField.setAccessible(true);
                String username = (String) usernameField.get(firstArgument);

                Audit audit = Audit.builder()
                        .auditType(SUCCESS)
                        .actionType(CREDIT_TRANSACTION)
                        .playerFullName(username)
                        .build();
                auditRepository.save(audit);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                logger.error(String.valueOf(e));
            }
        }
    }

    @AfterThrowing(pointcut = "creditPointcut()", throwing = "exception")
    public void afterFailedCredit(JoinPoint joinPoint, Throwable exception) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            Object firstArgument = args[0];
            Class<?> argumentClass = firstArgument.getClass();
            try {
                Field usernameField = argumentClass.getDeclaredField("username");
                usernameField.setAccessible(true);
                String username = (String) usernameField.get(firstArgument);

                Audit audit = Audit.builder()
                        .auditType(FAIL)
                        .actionType(CREDIT_TRANSACTION)
                        .playerFullName(username)
                        .build();
                auditRepository.save(audit);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                logger.error(String.valueOf(e));
            }
        }
    }

    @AfterReturning(pointcut = "debitPointcut()", returning = "result")
    public void afterSuccessfulDebit(JoinPoint joinPoint, ResponseEntity<?> result) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            Object firstArgument = args[0];
            Class<?> argumentClass = firstArgument.getClass();
            try {
                Field usernameField = argumentClass.getDeclaredField("username");
                usernameField.setAccessible(true);
                String username = (String) usernameField.get(firstArgument);

                Audit audit = Audit.builder()
                        .auditType(SUCCESS)
                        .actionType(DEBIT_TRANSACTION)
                        .playerFullName(username)
                        .build();
                auditRepository.save(audit);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                logger.error(String.valueOf(e));
            }
        }
    }

    @AfterThrowing(pointcut = "debitPointcut()", throwing = "exception")
    public void afterFailedDebit(JoinPoint joinPoint, Throwable exception) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            Object firstArgument = args[0];
            Class<?> argumentClass = firstArgument.getClass();
            try {
                Field usernameField = argumentClass.getDeclaredField("username");
                usernameField.setAccessible(true);
                String username = (String) usernameField.get(firstArgument);

                Audit audit = Audit.builder()
                        .auditType(FAIL)
                        .actionType(DEBIT_TRANSACTION)
                        .playerFullName(username)
                        .build();
                auditRepository.save(audit);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                logger.error(String.valueOf(e));
            }
        }
    }

    @AfterReturning(pointcut = "viewTransactionHistoryPointcut()", returning = "result")
    public void afterSuccessfulViewTransactionHistory(JoinPoint joinPoint, ResponseEntity<?> result) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            Object firstArgument = args[0];
            Class<?> argumentClass = firstArgument.getClass();
            try {
                Field usernameField = argumentClass.getDeclaredField("username");
                usernameField.setAccessible(true);
                String username = (String) usernameField.get(firstArgument);

                Audit audit = Audit.builder()
                        .auditType(SUCCESS)
                        .actionType(VIEW_TRANSACTION_HISTORY)
                        .playerFullName(username)
                        .build();
                auditRepository.save(audit);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                logger.error(String.valueOf(e));
            }
        }
    }

    @AfterThrowing(pointcut = "viewTransactionHistoryPointcut()", throwing = "exception")
    public void afterFailedViewTransactionHistory(JoinPoint joinPoint, Throwable exception) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            Object firstArgument = args[0];
            Class<?> argumentClass = firstArgument.getClass();
            try {
                Field usernameField = argumentClass.getDeclaredField("username");
                usernameField.setAccessible(true);
                String username = (String) usernameField.get(firstArgument);

                Audit audit = Audit.builder()
                        .auditType(FAIL)
                        .actionType(VIEW_TRANSACTION_HISTORY)
                        .playerFullName(username)
                        .build();
                auditRepository.save(audit);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                logger.error(String.valueOf(e));
            }
        }
    }
}
