package com.inyro.api.domain.common.port;

public interface AuthVerificationPort {
    void saveVerification(String sno, String value, long ttlSeconds);

    boolean isVerificationExists(String sno);

    void deleteVerification(String sno);
}
