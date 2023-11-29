package template.hypermedia.advice;

import template.database.models.MetaQuery;
import template.database.models.QueryResult;
import template.global.exceptions.UnknownServerException;
import template.global.models.QueryResponse;
import template.global.utilities.ParameterProcessor;
import template.hypermedia.models.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Order(3)
@ControllerAdvice
public class HypermediaAdvice implements ResponseBodyAdvice<Object> {

    private final ParameterProcessor parameterProcessor;

    public HypermediaAdvice(ParameterProcessor parameterProcessor) {
        this.parameterProcessor = parameterProcessor;
    }

    public static HypermediaSelf buildSelfHypermedia(
        ServerHttpRequest request) {
        String url = URLDecoder.decode(request.getURI().toString(),
            StandardCharsets.UTF_8);

        HypermediaLink selfLink = new HypermediaLink();
        selfLink.setHref(url);
        selfLink.setType(request.getMethod().toString());

        HypermediaSelf hypermediaSelf = new HypermediaSelf();
        hypermediaSelf.setSelf(selfLink);

        return hypermediaSelf;
    }

    public static HypermediaSelf buildSelfHypermedia(
        HttpServletRequest request) {
        HypermediaLink selfLink = new HypermediaLink();
        selfLink.setHref(request.getRequestURL().toString());
        selfLink.setType(request.getMethod());

        HypermediaSelf hypermediaSelf = new HypermediaSelf();
        hypermediaSelf.setSelf(selfLink);

        return hypermediaSelf;
    }

    public static void buildHypermediaState(
        HypermediaResponse<?, HypermediaSelf> hypermediaResponse) {
        HypermediaState<?> possibleState;
        try {
            possibleState =
                (HypermediaState<?>) hypermediaResponse.getData().get(0);
        } catch (Exception ignored) {
            // If this doesn't cast into HypermediaState then we don't
            // need to worry about handling HypermediaState
            return;
        }

        hypermediaResponse.getLinks().setActions(possibleState.getActions());

        if (possibleState.getData() instanceof QueryResult) {
            hypermediaResponse.setData(
                ((QueryResult) possibleState.getData()).getData());

            return;
        }

        hypermediaResponse.setData((List<Object>) possibleState.getData());
    }

    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        HypermediaSelf hypermediaSelf = buildSelfHypermedia(request);

        HypermediaResponse<?, HypermediaSelf> hypermediaResponse =
            new HypermediaResponse<Object, HypermediaSelf>(
                (QueryResponse<?>) body,
                ((QueryResponse<?>) body).getMeta(),
                hypermediaSelf);

        buildHypermediaState(hypermediaResponse);

        if (!(hypermediaResponse.getMeta() instanceof MetaQuery)) {
            return hypermediaResponse;
        }

        MetaQuery metaQuery =
            (MetaQuery) ((QueryResponse<?>) body).getMeta();

        // Return only next link
        if (metaQuery.getNext() != null && metaQuery.getPrevious() == null) {
            try {
                HypermediaNext hypermediaNext = buildNextHypermedia(request,
                    (MetaQuery) hypermediaResponse.getMeta(),
                    hypermediaSelf.getSelf());

                return new HypermediaResponse<Object, HypermediaSelf>(
                    (QueryResponse<?>) body,
                    ((QueryResponse<?>) body).getMeta(),
                    hypermediaNext);
            } catch (Exception exception) {
                throw new UnknownServerException(
                    "Error building hypermedia next link.");
            }
        }

        // Return both next and previous links
        if (metaQuery.getNext() != null && metaQuery.getPrevious() != null) {
            try {
                HypermediaNext hypermediaNext = buildNextHypermedia(request,
                    (MetaQuery) hypermediaResponse.getMeta(),
                    hypermediaSelf.getSelf());

                HypermediaNextPrevious hypermediaPrevious =
                    buildPreviousHypermedia(request, hypermediaNext,
                        (MetaQuery) ((QueryResponse<?>) body).getMeta());

                return new HypermediaResponse<Object, HypermediaSelf>(
                    (QueryResponse<?>) body,
                    ((QueryResponse<?>) body).getMeta(),
                    hypermediaPrevious);
            } catch (Exception exception) {
                throw new UnknownServerException(
                    "Error building hypermedia next and previous link.");
            }
        }

        // Return only previous link
        if (metaQuery.getNext() == null && metaQuery.getPrevious() != null) {
            try {
                HypermediaSelfPrevious hypermediaPrevious =
                    buildSelfPreviousHypermedia(request, hypermediaSelf,
                        (MetaQuery) ((QueryResponse<?>) body).getMeta());

                return new HypermediaResponse<Object, HypermediaSelf>(
                    (QueryResponse<?>) body,
                    ((QueryResponse<?>) body).getMeta(),
                    hypermediaPrevious);
            } catch (Exception exception) {
                throw new UnknownServerException(
                    "Error building hypermedia next link.");
            }
        }

        // Return only self link
        return new HypermediaResponse<Object, HypermediaSelf>(
            (QueryResponse<?>) body,
            ((QueryResponse<?>) body).getMeta(),
            hypermediaSelf);
    }

    private HypermediaNext buildNextHypermedia(ServerHttpRequest request,
                                               MetaQuery metaQuery,
                                               HypermediaLink selfLink) {
        HypermediaNext hypermediaNext = new HypermediaNext();
        hypermediaNext.setSelf(selfLink);

        String href = parameterProcessor.buildCursorPaginationUrl(
            request.getURI().toString(), metaQuery.getCursor(),
            metaQuery.getNext().toString());

        HypermediaLink hypermediaLink = new HypermediaLink();
        hypermediaLink.setHref(href);
        hypermediaLink.setType(request.getMethod().toString());
        hypermediaNext.setNext(hypermediaLink);

        return hypermediaNext;
    }

    /**
     * Previous can't work without an existing next link
     * Will set the first element in data array as the previous page cursor
     */
    private HypermediaNextPrevious buildPreviousHypermedia(
        ServerHttpRequest request, HypermediaNext nextLink,
        MetaQuery metaQuery) {
        if (metaQuery.getPrevious() == null) {
            return null;
        }

        HypermediaNextPrevious hypermediaPrevious =
            new HypermediaNextPrevious();
        hypermediaPrevious.setSelf(nextLink.getSelf());
        hypermediaPrevious.setNext(nextLink.getNext());

        String href = parameterProcessor.buildCursorPaginationUrl(
            request.getURI().toString(), metaQuery.getCursor(),
            metaQuery.getPrevious().toString());

        String url = href.split("\\?")[0];
        String parameters = href.split("\\?")[1];
        parameters = Arrays.stream(parameters.split("\\&"))
            .filter(parameter -> !URLDecoder.decode(parameter,
                StandardCharsets.UTF_8).contains("limit="))
            .collect(Collectors.joining("&"));

        // Add limit for situation when we have reached the first page after using previous,
        // and we may have less than normal limit left
        // Previous cursor setter method will reset the query limit for this situation
        parameters += "&limit=" + metaQuery.getLimit();
        href = url + "?" + parameters;

        HypermediaLink hypermediaLink = new HypermediaLink();
        hypermediaLink.setHref(href);
        hypermediaLink.setType(request.getMethod().toString());
        hypermediaPrevious.setPrevious(hypermediaLink);

        return hypermediaPrevious;
    }

    private HypermediaSelfPrevious buildSelfPreviousHypermedia(
        ServerHttpRequest request, HypermediaSelf selfLink,
        MetaQuery metaQuery) {
        HypermediaSelfPrevious hypermediaPrevious =
            new HypermediaSelfPrevious();
        hypermediaPrevious.setSelf(selfLink.getSelf());

        String href = parameterProcessor.buildCursorPaginationUrl(
            request.getURI().toString(), metaQuery.getCursor(),
            metaQuery.getPrevious().toString());

        HypermediaLink hypermediaLink = new HypermediaLink();
        hypermediaLink.setHref(href);
        hypermediaLink.setType(request.getMethod().toString());
        hypermediaPrevious.setPrevious(hypermediaLink);

        return hypermediaPrevious;
    }
}
