package org.tb.transfer;

import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import javax.inject.Inject;

@MicronautTest
public class AppTest {

    @Inject
    EmbeddedApplication application;

    @Test
    void testItWorks() {
        Assertions.assertTrue(application.isRunning());
    }

    // TODO: implement further integration tests

    // Test: Successful transfer
    // Test: Invalid account
    // Test: Accounts must not be the same
    // Test: Invalid amount
    // Test: Insufficient funds
}
