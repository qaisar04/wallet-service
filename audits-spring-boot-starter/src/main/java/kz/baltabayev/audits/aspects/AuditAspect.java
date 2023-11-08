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

import static kz.baltabayev.audits.domain.types.ActionType.REGISTRATION;
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

    @AfterReturning(pointcut = "registrationPointcut()", returning = "result")
    public void afterSuccessfulRegistration(JoinPoint joinPoint, ResponseEntity<?> result) {
        Object[] args = joinPoint.getArgs();
        System.out.println(args);
        Audit audit = Audit.builder().auditType(SUCCESS)
                .actionType(REGISTRATION)
                .playerFullName(args.toString())
                .build();
        auditService.save(audit);
    }

    @AfterThrowing(pointcut = "registrationPointcut()", throwing = "exception")
    public void afterFailedRegistration(JoinPoint joinPoint, Throwable exception) {
        Object[] args = joinPoint.getArgs();
        System.out.println(args);
        Audit audit = Audit.builder().auditType(FAIL)
                .actionType(REGISTRATION)
                .playerFullName(args.toString())
                .build();
        auditService.save(audit);
    }
}
