package kz.baltabayev.audits.service;


public interface SecurityServiceLogging<EntityClass> {

    EntityClass register(String username, String password);

    String authorization(String login, String password);
}
