package template.internal.services;

import template.global.services.ListManager;
import template.global.services.Manager;
import template.global.services.QueryManager;
import template.internal.models.InternalUser;

import java.util.Optional;

public interface InternalUserManager extends
    QueryManager<InternalUser, Integer>,
    ListManager<InternalUser>,
    Manager<InternalUser, Integer> {
    Optional<InternalUser> getByEmail(String email);
}
