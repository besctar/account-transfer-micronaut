package org.tb.transfer.rest.handler;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import org.tb.transfer.domain.exception.NotFoundException;

import javax.inject.Singleton;


@Produces
@Singleton
@Requires(classes = {NotFoundException.class, NotFoundExHandler.class})
public class NotFoundExHandler implements ExceptionHandler<NotFoundException, HttpResponse<?>> {

    @Override
    public HttpResponse<?> handle(HttpRequest request, NotFoundException ex) {
        return HttpResponse.notFound(ex.getMessage()).contentType(MediaType.TEXT_HTML);
    }
}
