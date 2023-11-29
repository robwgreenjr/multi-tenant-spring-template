package template.authorization.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import template.authorization.helpers.AuthorizationBouncer;
import template.global.dtos.ErrorDto;
import template.global.models.MetaData;
import template.global.models.QueryResponse;
import template.hypermedia.advice.HypermediaAdvice;
import template.hypermedia.models.HypermediaResponse;
import template.hypermedia.models.HypermediaSelf;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {
    private final ObjectMapper mapper;
    private final AuthorizationBouncer roleBouncer;

    public AuthorizationFilter(ObjectMapper mapper,
                               AuthorizationBouncer roleBouncer) {
        this.mapper = mapper;
        this.roleBouncer = roleBouncer;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
        throws IOException, ServletException {
        try {
            roleBouncer.authorize(request);

            filterChain.doFilter(request, response);
        } catch (ResponseStatusException exception) {
            response.setStatus(exception.getStatusCode().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            QueryResponse<MetaData> queryResponse =
                new QueryResponse<>(new ErrorDto(
                    HttpStatus.valueOf(
                        exception.getStatusCode().value()),
                    exception.getReason()),
                    new MetaData());
            HypermediaSelf hypermediaSelf =
                HypermediaAdvice.buildSelfHypermedia(request);

            HypermediaResponse<?, HypermediaSelf> hypermediaResponse =
                new HypermediaResponse<Object, HypermediaSelf>(
                    queryResponse,
                    ((QueryResponse<?>) queryResponse).getMeta(),
                    hypermediaSelf);

            HypermediaAdvice.buildHypermediaState(hypermediaResponse);

            mapper.writeValue(response.getWriter(), hypermediaResponse);
        }
    }
}