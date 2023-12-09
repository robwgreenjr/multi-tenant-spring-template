package template.tenants.services;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import template.tenants.entities.TenantEmailConfirmationEntity;
import template.tenants.events.publishers.TenantEmailConfirmationEventPublisher;
import template.tenants.mappers.TenantEmailConfirmationMapper;
import template.tenants.models.TenantEmailConfirmation;
import template.tenants.repositories.TenantEmailConfirmationRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class TenantEmailConfirmationManagerImpl
    implements TenantEmailConfirmationManager {
    private final TenantEmailConfirmationMapper tenantEmailConfirmationMapper;
    private final TenantEmailConfirmationRepository
        tenantEmailConfirmationRepository;
    private final TenantEmailConfirmationEventPublisher
        tenantEmailConfirmationEventPublisher;

    public TenantEmailConfirmationManagerImpl(
        TenantEmailConfirmationMapper tenantEmailConfirmationMapper,
        TenantEmailConfirmationRepository tenantEmailConfirmationRepository,
        TenantEmailConfirmationEventPublisher tenantEmailConfirmationEventPublisher) {
        this.tenantEmailConfirmationMapper = tenantEmailConfirmationMapper;
        this.tenantEmailConfirmationRepository =
            tenantEmailConfirmationRepository;
        this.tenantEmailConfirmationEventPublisher =
            tenantEmailConfirmationEventPublisher;
    }

    @Override
    public TenantEmailConfirmation create(
        TenantEmailConfirmation tenantEmailConfirmation) {
        TenantEmailConfirmationEntity tenantEmailConfirmationEntity =
            tenantEmailConfirmationMapper.toEntity(
                tenantEmailConfirmation);

        tenantEmailConfirmationRepository.save(tenantEmailConfirmationEntity);

        TenantEmailConfirmation newTenantEmailConfirmation =
            tenantEmailConfirmationMapper.entityToObject(
                tenantEmailConfirmationEntity);

        tenantEmailConfirmationEventPublisher.publishTenantEmailConfirmationCreatedEvent(
            newTenantEmailConfirmation);

        return newTenantEmailConfirmation;
    }

    @Override
    public void delete(TenantEmailConfirmation tenantEmailConfirmation) {
        tenantEmailConfirmationRepository.delete(
            tenantEmailConfirmationMapper.toEntity(tenantEmailConfirmation));
    }

    @Override
    public Optional<TenantEmailConfirmation> getByToken(UUID token) {
        Optional<TenantEmailConfirmationEntity> tenantEmailConfirmationEntity =
            tenantEmailConfirmationRepository.getByToken(token);

        if (tenantEmailConfirmationEntity.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Token not found.");
        }

        return Optional.of(tenantEmailConfirmationMapper.entityToObject(
            tenantEmailConfirmationEntity.get()));
    }
}
