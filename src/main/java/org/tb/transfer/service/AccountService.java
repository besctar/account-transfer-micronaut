package org.tb.transfer.service;

import org.tb.transfer.domain.DebitAccountEntity;
import org.tb.transfer.domain.exception.NotFoundException;
import org.tb.transfer.domain.exception.ValidationException;
import org.tb.transfer.repo.AccountRepo;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.math.BigDecimal;

@Singleton
@Transactional
public class AccountService {
    @Inject
    private AccountRepo repo;

    public void transfer(Long sourceAccountId, Long targetAccountId, BigDecimal amount) throws ValidationException, NotFoundException {
        checkNotNull(sourceAccountId, "Source account must not be null");
        checkNotNull(targetAccountId, "Target account must not be null");
        checkNotNull(amount, "Amount account must not be null");
        checkIsTrue(!sourceAccountId.equals(targetAccountId), "Source and target accounts must not be the same.");

        DebitAccountEntity sourceAccount, targetAccount;

        /*** Keeping the order of locking the accounts by number in order to avoid deadlocks */
        if (sourceAccountId < targetAccountId) {
            sourceAccount = findByIdForUpdate(sourceAccountId);
            targetAccount = findByIdForUpdate(targetAccountId);
        } else {
            targetAccount = findByIdForUpdate(targetAccountId);
            sourceAccount = findByIdForUpdate(sourceAccountId);
        }

        /*** further validations */
        checkNotNull(sourceAccount, "Source account not found. id=" + sourceAccountId);
        checkNotNull(targetAccount, "Target account not found. id=" + targetAccount);
        checkIsTrue(BigDecimal.ZERO.compareTo(amount) < 0, "Incorrect amount. You may transfer only positive non zero value.");
        checkIsTrue(sourceAccount.getBalance().compareTo(amount) >= 0, "Insufficient funds at the source account.");

        BigDecimal newSourceBalance = sourceAccount.getBalance().subtract(amount);
        BigDecimal newTargetBalance = targetAccount.getBalance().add(amount);
        sourceAccount.setBalance(newSourceBalance);
        targetAccount.setBalance(newTargetBalance);
        repo.update(sourceAccount);
        repo.update(targetAccount);
    }

    public DebitAccountEntity findById(Long id) {
        return repo.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Account not found with id=" + id);
        });
    }

    public boolean updateAccountBalance(Long id, BigDecimal balance) {
        return repo.findById(id)
                .map(e -> e.setBalance(balance))
                .map(repo::update)
                .isPresent();
    }

    public DebitAccountEntity openAccount() {
        DebitAccountEntity e = new DebitAccountEntity()
                .setBalance(BigDecimal.ZERO);
        return repo.save(e);
    }

    public DebitAccountEntity findByIdForUpdate(Long id) {
        return repo.findByIdForUpdate(id).orElseThrow(() -> {
            throw new NotFoundException("Account not found with id=" + id);
        });
    }

    private static void checkNotNull(Object obj, String msg) {
        checkIsTrue(obj != null, msg);
    }

    private static void checkIsTrue(boolean condition, String msg) {
        if (!condition) {
            throw new ValidationException(msg);
        }
    }


}
