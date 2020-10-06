package org.tb.transfer.repo;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import org.tb.transfer.domain.DebitAccountEntity;

import java.util.Optional;

@JdbcRepository(dialect = Dialect.H2)
public interface AccountRepo extends CrudRepository<DebitAccountEntity, Long> {
    /**
     * Important note: micronaut data jdbc interprets all 'ForUpdate' suffix methods as
     * Pessimistic Lock on the data requested. Lock is 'turned off' by the further transaction close.
     */
    Optional<DebitAccountEntity> findByIdForUpdate(Long id);
}