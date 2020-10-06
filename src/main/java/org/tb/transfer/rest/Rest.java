package org.tb.transfer.rest;

public interface Rest {
    String HEALTH = "/health";
    String ACCOUNT = "/account";
    String ACCOUNT_ = ACCOUNT + "/{id}";
    String TRANSFER = ACCOUNT + "/operation/transfer";
}
