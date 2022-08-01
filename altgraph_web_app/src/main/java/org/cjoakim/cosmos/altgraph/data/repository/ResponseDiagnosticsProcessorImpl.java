package org.cjoakim.cosmos.altgraph.data.repository;

import com.azure.spring.data.cosmos.core.ResponseDiagnostics;
import com.azure.spring.data.cosmos.core.ResponseDiagnosticsProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

@Slf4j
public class ResponseDiagnosticsProcessorImpl implements ResponseDiagnosticsProcessor {

    public static ResponseDiagnostics lastResponseDiagnostics;

    public ResponseDiagnosticsProcessorImpl() {
        super();
        log.warn("constructor");
    }

    @Override
    public void processResponseDiagnostics(@Nullable ResponseDiagnostics responseDiagnostics) {

        lastResponseDiagnostics = responseDiagnostics;  // capture the last diagnostics, for single-use demo purposes
        if (responseDiagnostics != null) {
            log.debug("ResponseDiagnostics: " + responseDiagnostics);
        }
    }

    public static double getLastRequestCharge() {

        if (lastResponseDiagnostics != null) {
            try {
                return lastResponseDiagnostics.getCosmosResponseStatistics().getRequestCharge();
            }
            catch (Exception e) {
                // ignore for now
            }
        }
        return -1.0; // value indicating lack of success (less than zero)
    }

}
