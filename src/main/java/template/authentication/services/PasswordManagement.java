package template.authentication.services;

import template.authentication.models.InternalUserPassword;

public interface PasswordManagement {
    void change(InternalUserPassword userPasswordModel) throws Exception;

    void changeFORCE(InternalUserPassword userPasswordModel) throws Exception;

    void forgot(InternalUserPassword userPasswordModel);

    void reset(InternalUserPassword userPasswordModel) throws Exception;
}