package com.challenge.transfer.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;

@Component
public class RestHelper {

    private final RestTemplate restTemplate;

    @Autowired
    public RestHelper(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String createUrl(String urlBase, String path) {
        final String url = urlBase + path;
        return url;
    }

    public <T> T executeServiceCall(String urlCall, HttpEntity<?> requestEntity,
                                                             Class<T> responseType, HttpMethod method) {
        try {
            final ResponseEntity<T> response = this.restTemplate.exchange(urlCall, method, requestEntity, responseType);
            //TODO: Construct a custom response in case response is not 200?
            return response.getBody();
        } catch(HttpStatusCodeException e) {
            return (T) ResponseEntity.status(e.getRawStatusCode())
                    .headers(e.getResponseHeaders())
                    .body(e.getResponseBodyAsString());
        } catch (ResourceAccessException e) {
            return (T) ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Service unavailable due to a network issue.");
        } catch (RestClientException e) {
            return (T) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error occurred.");
        }
    }
}
