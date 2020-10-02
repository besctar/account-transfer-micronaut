package org.tb.transfer.repo;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import org.tb.transfer.domain.DebitAccountEntity;

@Repository
public interface AccountRepo extends CrudRepository<DebitAccountEntity, Long> {

    // Micronaut pessimistic lock feature - 'forUpdate' suffix
    DebitAccountEntity findByIdForUpdate(Long id);
}