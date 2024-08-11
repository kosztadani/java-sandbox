package dev.kosztadani.sandbox.tcp.client;

import dev.kosztadani.sandbox.tcp.common.protocol.MathRequest;
import dev.kosztadani.sandbox.tcp.common.protocol.MathResponse;

import java.util.concurrent.CompletableFuture;

/**
 * Client to execute operations through a math server.
 */
public sealed interface MathClient permits MathClientChannel {

    /**
     * Submits a request to the math server.
     *
     * @param request the request.
     * @return a {@link CompletableFuture} which will be completed when the server answers the request.
     */
    CompletableFuture<MathResponse> submit(MathRequest request);

    /**
     * Submits a request to the math server and waits for the response.
     *
     * @param request the request.
     * @return the response.
     */
    default MathResponse request(final MathRequest request) {
        CompletableFuture<MathResponse> future = submit(request);
        MathResponse response = future.join();
        return response;
    }
}
