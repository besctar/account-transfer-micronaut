package org.tb.transfer.rest;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import org.tb.transfer.domain.DebitAccountEntity;
import org.tb.transfer.domain.exception.NotFoundException;
import org.tb.transfer.rest.dto.TransferRequest;
import org.tb.transfer.service.AccountService;

import javax.inject.Inject;

@Controller
public class AccountController implements Rest {
    @Inject
    private AccountService service;

    @Get(HEALTH)
    public HttpResponse<String> health() {
        return HttpResponse.ok("OK").contentType(MediaType.TEXT_HTML);
    }

    @Get(ACCOUNT_)
    public DebitAccountEntity accountBalance(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @Post(ACCOUNT)
    public HttpResponse<DebitAccountEntity> openAccount() {
        DebitAccountEntity account = service.openAccount();
        return HttpResponse.created(account);
    }

    @Put(ACCOUNT_)
    public HttpResponse<String> updateAccountBalance(@PathVariable("id") Long id, DebitAccountEntity account) {
        if (service.updateAccountBalance(id, account.getBalance())) {
            return HttpResponse.ok("Account balance successfully updated.").contentType(MediaType.TEXT_HTML);
        } else {
            throw new NotFoundException("Account does not exists id=" + account.getId());
        }
    }

    @Post(TRANSFER)
    public HttpResponse<String> transfer(TransferRequest req) {
        service.transfer(req.getSourceAccountId(), req.getTargetAccountId(), req.getAmount());
        return HttpResponse.ok("Transfer successfully done").contentType(MediaType.TEXT_HTML);
    }
}
