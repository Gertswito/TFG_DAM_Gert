package com.gert.tfgdam.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PayPalOrderService {

    private final WebClient webClient;
    private final PayPalAuthService authService;

    public PayPalOrderService(WebClient paypalWebClient,
                              PayPalAuthService authService) {
        this.webClient = paypalWebClient;
        this.authService = authService;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> createOrder(double total) {
        String accessToken = authService.getAccessToken();

        Map<String, Object> body = Map.of(
                "intent", "CAPTURE",
                "purchase_units", List.of(
                        Map.of(
                                "amount", Map.of(
                                        "currency_code", "EUR",
                                        "value", String.format("%.2f", total)
                                )
                        )
                ),
                "application_context", Map.of(
                        "return_url", "myapp://paypal-success",
                        "cancel_url", "myapp://paypal-cancel"
                )
        );

        return webClient.post()
                .uri("/v2/checkout/orders")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> captureOrder(String orderId) {

        String accessToken = authService.getAccessToken();

        return webClient.post()
                .uri("/v2/checkout/orders/" + orderId + "/capture")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}
