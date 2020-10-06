package org.tb.transfer.rest.handler;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import org.tb.transfer.domain.exception.ValidationException;

import javax.inject.Singleton;


@Produces
@Singleton
@Requires(classes = {ValidationException.class, ValidationExHandler.class})
public class ValidationExHandler implements ExceptionHandler<ValidationException, HttpResponse<?>> {

    @Override
    public HttpResponse<?> handle(HttpRequest request, ValidationException ex) {
        return HttpResponse.badRequest(ex.getMessage()).contentType(MediaType.TEXT_HTML);
    }
}
