package org.tb.transfer.service;

import org.tb.transfer.domain.DebitAccountEntity;
import org.tb.transfer.repo.AccountRepo;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Objects;

@Singleton
public class AccountService {
    @Inject
    private AccountRepo repo;

    @Transactional
    public void transfer(Long sourceAccountId, Long targetAccountId, BigDecimal amount) {
        Objects.requireNonNull(sourceAccountId);
        Objects.requireNonNull(targetAccountId);
        Objects.requireNonNull(amount);
        if (sourceAccountId.equals(targetAccountId)) {
            throw new IllegalArgumentException("Source and target accounts must not be the same.");
        }

        DebitAccountEntity sourceAccount, targetAccount;

        // keeping the order of locking the accounts by number in order to avoid deadlocks
        if (sourceAccountId < targetAccountId) {
            sourceAccount = repo.findByIdForUpdate(sourceAccountId);
            targetAccount = repo.findByIdForUpdate(targetAccountId);
        } else {
            targetAccount = repo.findByIdForUpdate(targetAccountId);
            sourceAccount = repo.findByIdForUpdate(sourceAccountId);
        }

        // further validations
        Objects.requireNonNull(sourceAccount, "Transfer source account not found. id=" + sourceAccountId);
        Objects.requireNonNull(targetAccount, "Transfer target account not found. id=" + targetAccount);

        if (BigDecimal.ZERO.compareTo(amount) >= 0) {
            throw new IllegalArgumentException("Incorrect amount. You may transfer only positive non zero value.");
        }

        if (sourceAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Not enough money at the source account.");
        }

        BigDecimal newSourceBalance = sourceAccount.getBalance().subtract(amount);
        BigDecimal newTargetBalance = targetAccount.getBalance().add(amount);
        sourceAccount.setBalance(newSourceBalance);
        targetAccount.setBalance(newTargetBalance);
        repo.save(sourceAccount);
        repo.save(targetAccount);
    }

    public DebitAccountEntity findById(Long id) {
        return repo.findById(id).orElseThrow(() -> {
            throw new IllegalArgumentException("Account not found with id=" + id);
        });
    }
}
