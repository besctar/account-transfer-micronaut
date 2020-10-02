package org.tb.transfer.rest;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import org.tb.transfer.rest.dto.TransferRequest;
import org.tb.transfer.domain.DebitAccountEntity;
import io.micronaut.http.MediaType;
import org.tb.transfer.service.AccountService;

import javax.inject.Inject;

@Controller("/")
public class AccountController {
    @Inject
    private AccountService service;

    @Get("/account/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public DebitAccountEntity accountBalance(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @Post("/account/operation/transfer")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse transfer(TransferRequest req) {
        service.transfer(req.getSourceAccountId(), req.getTargetAccountId(), req.getAmount());
        return HttpResponse.ok("Transfer successfully done");
    }
}
