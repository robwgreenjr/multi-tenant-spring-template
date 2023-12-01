package template.authentication.services;

public interface PasswordManagement<T> {
    void change(T userPassword) throws Exception;

    void changeFORCE(T userPassword) throws Exception;

    void forgot(T userPassword);

    void reset(T userPassword) throws Exception;
}