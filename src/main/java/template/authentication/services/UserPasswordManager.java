package template.authentication.services;

import template.authentication.models.InternalUserPassword;

public interface UserPasswordManager {
    InternalUserPassword findByUserEmail(String email);

    InternalUserPassword create(InternalUserPassword userPasswordModel);

    InternalUserPassword update(Integer id,
                                InternalUserPassword userPasswordModel)
        throws Exception;

    InternalUserPassword updatePartial(Integer id,
                                       InternalUserPassword userPasswordModel);

    void delete(Integer id);
}