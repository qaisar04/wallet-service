package kz.baltabayev.audits.aspects;

import kz.baltabayev.audits.domain.Audit;
import kz.baltabayev.audits.domain.types.ActionType;
import kz.baltabayev.audits.domain.types.AuditType;
import kz.baltabayev.audits.service.AuditService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import static kz.baltabayev.audits.domain.types.ActionType.*;
import static kz.baltabayev.audits.domain.types.AuditType.FAIL;
import static kz.baltabayev.audits.domain.types.AuditType.SUCCESS;

@Aspect
public class AuditAspect {

    private final AuditService auditService;

    public AuditAspect(AuditService auditService) {
        this.auditService = auditService;
    }

    @Pointcut("execution(public * kz.baltabayev.audits.manager.PlayerManager.registerPlayer(..))")
    public void registrationPointcut() {
    }

    @Pointcut("execution(public * kz.baltabayev.audits.manager.PlayerManager.authenticatePlayer(..))")
    public void authenticationPointcut() {
    }

    @Pointcut("execution(public * kz.baltabayev.audits.manager.PlayerManager.getBalance(..))")
    public void getBalancePointcut() {
    }

    @Pointcut("execution(public * kz.baltabayev.audits.manager.PlayerManager.creditWithTransactionId(..))")
    public void creditWithTransactionIdPointcut() {
    }

    @Pointcut("execution(public * kz.baltabayev.audits.manager.PlayerManager.debitWithTransactionId(..))")
    public void debitWithTransactionIdPointcut() {
    }

    @Pointcut("execution(public * kz.baltabayev.audits.manager.TransactionManager.viewTransactionHistory(..))")
    public void viewTransactionHistoryPointcut() {
    }

    @AfterReturning(pointcut = "registrationPointcut()", returning = "result")
    public void afterSuccessfulRegistration(JoinPoint joinPoint, ResponseEntity<?> result) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof String) {
            String username = (String) args[0];
            Audit audit = Audit.builder().auditType(SUCCESS)
                    .actionType(REGISTRATION)
                    .playerFullName(username)
                    .build();
            auditService.save(audit);
        }
    }

    @AfterThrowing(pointcut = "registrationPointcut()", throwing = "exception")
    public void afterFailedRegistration(JoinPoint joinPoint, Throwable exception) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof String) {
            String username = (String) args[0];
            Audit audit = Audit.builder().auditType(FAIL)
                    .actionType(REGISTRATION)
                    .playerFullName(username)
                    .build();
            auditService.save(audit);
        }
    }

    @AfterReturning(pointcut = "authenticationPointcut()", returning = "result")
    public void afterSuccessfulAuthentication(JoinPoint joinPoint, ResponseEntity<?> result) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof String) {
            String username = (String) args[0];
            Audit audit = Audit.builder().auditType(SUCCESS)
                    .actionType(AUTHORIZATION)
                    .playerFullName(username)
                    .build();
            auditService.save(audit);
        }
    }

    @AfterThrowing(pointcut = "authenticationPointcut()", throwing = "exception")
    public void afterFailedAuthentication(JoinPoint joinPoint, Throwable exception) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof String) {
            String username = (String) args[0];
            Audit audit = Audit.builder().auditType(FAIL)
                    .actionType(AUTHORIZATION)
                    .playerFullName(username)
                    .build();
            auditService.save(audit);
        }
    }

    @AfterReturning(pointcut = "getBalancePointcut()", returning = "result")
    public void afterSuccessfulGetBalance(JoinPoint joinPoint, ResponseEntity<?> result) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof String) {
            String username = (String) args[0];
            Audit audit = Audit.builder().auditType(SUCCESS)
                    .actionType(BALANCE_INQUIRY)
                    .playerFullName(username)
                    .build();
            auditService.save(audit);
        }
    }

    @AfterThrowing(pointcut = "getBalancePointcut()", throwing = "exception")
    public void afterFailedGetBalance(JoinPoint joinPoint, Throwable exception) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof String) {
            String username = (String) args[0];
            Audit audit = Audit.builder().auditType(FAIL)
                    .actionType(BALANCE_INQUIRY)
                    .playerFullName(username)
                    .build();
            auditService.save(audit);
        }
    }

    @AfterReturning(pointcut = "creditWithTransactionIdPointcut()", returning = "result")
    public void afterSuccessfulCredit(JoinPoint joinPoint, ResponseEntity<?> result) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof String) {
            String username = (String) args[0];
            Audit audit = Audit.builder().auditType(SUCCESS)
                    .actionType(CREDIT_TRANSACTION)
                    .playerFullName(username)
                    .build();
            auditService.save(audit);
        }
    }

    @AfterThrowing(pointcut = "creditWithTransactionIdPointcut()", throwing = "exception")
    public void afterFailedCredit(JoinPoint joinPoint, Throwable exception) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof String) {
            String username = (String) args[0];
            Audit audit = Audit.builder().auditType(FAIL)
                    .actionType(CREDIT_TRANSACTION)
                    .playerFullName(username)
                    .build();
            auditService.save(audit);
        }
    }

    @AfterReturning(pointcut = "debitWithTransactionIdPointcut()", returning = "result")
    public void afterSuccessfulDebit(JoinPoint joinPoint, ResponseEntity<?> result) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof String) {
            String username = (String) args[0];
            Audit audit = Audit.builder().auditType(SUCCESS)
                    .actionType(DEBIT_TRANSACTION)
                    .playerFullName(username)
                    .build();
            auditService.save(audit);
        }
    }

    @AfterThrowing(pointcut = "debitWithTransactionIdPointcut()", throwing = "exception")
    public void afterFailedDebit(JoinPoint joinPoint, Throwable exception) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof String) {
            String username = (String) args[0];
            Audit audit = Audit.builder().auditType(FAIL)
                    .actionType(DEBIT_TRANSACTION)
                    .playerFullName(username)
                    .build();
            auditService.save(audit);
        }
    }

    @AfterReturning(pointcut = "viewTransactionHistoryPointcut()", returning = "result")
    public void afterSuccessfulViewTransactionHistory(JoinPoint joinPoint, ResponseEntity<?> result) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof String) {
            String username = (String) args[0];
            Audit audit = Audit.builder().auditType(SUCCESS)
                    .actionType(VIEW_TRANSACTION_HISTORY)
                    .playerFullName(username)
                    .build();
            auditService.save(audit);
        }
    }

    @AfterThrowing(pointcut = "viewTransactionHistoryPointcut()", throwing = "exception")
    public void afterFailedViewTransactionHistory(JoinPoint joinPoint, Throwable exception) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof String) {
            String username = (String) args[0];
            Audit audit = Audit.builder().auditType(FAIL)
                    .actionType(VIEW_TRANSACTION_HISTORY)
                    .playerFullName(username)
                    .build();
            auditService.save(audit);
        }
    }
}
