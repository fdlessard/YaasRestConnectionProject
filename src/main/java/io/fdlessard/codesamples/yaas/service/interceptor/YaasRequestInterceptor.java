package io.fdlessard.codesamples.yaas.service.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;

import static org.apache.commons.lang3.math.NumberUtils.toInt;

/**
 * Created by fdlessard on 16-10-26.
 */
public class YaasRequestInterceptor  implements ClientHttpRequestInterceptor {

    public static final String HEADER_REQUEST_ID = "hybris-request-id";
    public static final String HEADER_HOP = "hybris-hop";
    public static final String CHECKING_IF_IS_PRESENT = "Checking if {} is present : {}";
    public static final String ADDING_UPDATING_WITH = "Adding/Updating {} with {}";

    private static final Logger LOGGER = LoggerFactory.getLogger(YaasRequestInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {

        HttpHeaders headers = request.getHeaders();
        String hybrisRequestId = headers.getFirst(HEADER_REQUEST_ID);
        LOGGER.debug(CHECKING_IF_IS_PRESENT, HEADER_REQUEST_ID, hybrisRequestId);
        if(StringUtils.isEmpty(hybrisRequestId)) {
            headers.set(HEADER_REQUEST_ID , StringUtils.EMPTY);
            LOGGER.debug(ADDING_UPDATING_WITH, HEADER_REQUEST_ID, StringUtils.EMPTY);
        }

        String hybrisHop = headers.getFirst(HEADER_HOP);
        LOGGER.debug(CHECKING_IF_IS_PRESENT, HEADER_HOP, hybrisHop);
        String newHybrisHop = "0";
        if (NumberUtils.isDigits(hybrisHop)) {
            int hop = NumberUtils.toInt(hybrisHop);
            hop++;
            newHybrisHop = Integer.toString(hop);
        }
        headers.set(HEADER_HOP, newHybrisHop);
        LOGGER.debug(ADDING_UPDATING_WITH, HEADER_HOP, newHybrisHop);

        return clientHttpRequestExecution.execute(request, body);
    }
}