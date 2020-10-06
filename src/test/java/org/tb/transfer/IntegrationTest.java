package org.tb.transfer;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.tb.transfer.domain.DebitAccountEntity;
import org.tb.transfer.rest.Rest;

import javax.inject.Inject;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class IntegrationTest {

    @Inject
    @Client("/")
    private HttpClient http;

    @Test
    public void testRestHealth() {
        HttpStatus healthStatus = http.toBlocking().exchange(HttpRequest.GET(Rest.HEALTH)).status();
        assertEquals(HttpStatus.OK, healthStatus);
    }

    @Test
    public void testAccountNotFound() {
        long accountId = 123456789L;
        try {
            HttpResponse<?> resp = http.toBlocking().exchange(HttpRequest.GET(Rest.ACCOUNT + "/" + accountId));
        } catch (HttpClientResponseException ex) {
            assertErrorResponse(ex, HttpStatus.NOT_FOUND, "Account not found with id=" + accountId);
        }
    }

    @Test
    public void testCreateAccountAndSetBalanceScenario() {
        long accountId = createAccount();

        setAccountBalance(accountId, 1000.0);

        assertAccountBalance(accountId, 1000.0);
    }

    @Test
    public void testTransferInvalidSourceAccountScenario() {
        long accountId1 = 123456789L;
        long accountId2 = 987654321L;
        try {
            transfer(accountId1, accountId2, 500.0);
        } catch (HttpClientResponseException ex) {
            assertErrorResponse(ex, HttpStatus.NOT_FOUND, "Account not found with id=" + accountId1);
        }
    }

    @Test
    public void testTransferInvalidTargetAccountScenario() {
        long accountId1 = createAccount();
        long accountId2 = 123456789L;
        try {
            transfer(accountId1, accountId2, 500.0);
        } catch (HttpClientResponseException ex) {
            assertErrorResponse(ex, HttpStatus.NOT_FOUND, "Account not found with id=" + accountId2);
        }
    }

    @Test
    public void testTransferAccountsMustNotBeTheSameScenario() {
        long accountId1 = createAccount();
        try {
            transfer(accountId1, accountId1, 500.0);
        } catch (HttpClientResponseException ex) {
            assertErrorResponse(ex, HttpStatus.BAD_REQUEST, "Source and target accounts must not be the same.");
        }
    }

    @Test
    public void testTransferInvalidAmountScenario() {
        long accountId1 = createAccount();
        long accountId2 = createAccount();
        try {
            transfer(accountId1, accountId2, -500.0);
        } catch (HttpClientResponseException ex) {
            assertErrorResponse(ex, HttpStatus.BAD_REQUEST, "Incorrect amount. You may transfer only positive non zero value.");
        }
    }

    @Test
    public void testTransferInsufficientFundsScenario() {
        long accountId1 = createAccount();
        long accountId2 = createAccount();
        try {
            transfer(accountId1, accountId2, 1000.0);
        } catch (HttpClientResponseException ex) {
            assertErrorResponse(ex, HttpStatus.BAD_REQUEST, "Insufficient funds at the source account.");
        }
    }

    @Test
    public void testTransferSuccessfulScenario() {
        long accountId1 = createAccount();
        long accountId2 = createAccount();

        setAccountBalance(accountId1, 3000.0);

        transfer(accountId1, accountId2, 1000.0);

        assertAccountBalance(accountId1, 2000.0);
        assertAccountBalance(accountId2, 1000.0);
    }

    private Long createAccount() {
        HttpResponse<DebitAccountEntity> resp = http.toBlocking().exchange(HttpRequest.POST(Rest.ACCOUNT, ""), DebitAccountEntity.class);
        assertEquals(HttpStatus.CREATED, resp.status());
        return resp.body().getId();
    }

    private void setAccountBalance(Long createdId, double targetBalance) {
        HttpResponse<?> resp = http.toBlocking()
                .exchange(HttpRequest.PUT(Rest.ACCOUNT + "/" + createdId, new DebitAccountEntity().setBalance(BigDecimal.valueOf(targetBalance))));
        assertEquals(HttpStatus.OK, resp.status());
    }

    private void transfer(Long sourceAccountId, Long targetAccountId, double amount) {
        HttpResponse<?> resp2 = http.toBlocking()
                .exchange(HttpRequest.POST(Rest.TRANSFER,
                        "{  \"sourceAccountId\": " + sourceAccountId +
                                ", \"targetAccountId\": " + targetAccountId +
                                ", \"amount\": " + amount + " }"));
    }

    private void assertAccountBalance(Long createdAccountId, double expectedBalance) {
        HttpResponse<DebitAccountEntity> resp3 = http.toBlocking().exchange(HttpRequest.GET(Rest.ACCOUNT + "/" + createdAccountId), DebitAccountEntity.class);
        assertEquals(0, BigDecimal.valueOf(expectedBalance).compareTo(resp3.body().getBalance()));
    }

    private void assertErrorResponse(HttpClientResponseException ex, HttpStatus expectedStatus, String expectedMsg) {
        assertEquals(expectedStatus, ex.getStatus());
        assertEquals(expectedMsg, ex.getMessage());
    }
}
