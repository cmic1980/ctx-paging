package sg.ctx.paging.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import sg.ctx.paging.interceptor.FeignResponseInterceptor;


/**
 * @author yumiao
 */
@Configuration
@ControllerAdvice
public class FeignClientPagingInterceptor implements RequestInterceptor, ResponseBodyAdvice {

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        var request = attributes.getRequest();

        var pageNumberString = request.getHeader(PagingConfiguration.PAGE_NUMBER_TAG);
        if (StringUtils.isNotEmpty(pageNumberString)) {
            template.header(PagingConfiguration.PAGE_NUMBER_TAG, pageNumberString);
        }

        var pageSizeString = request.getHeader(PagingConfiguration.PAGE_SIZE_TAG);
        if (StringUtils.isNotEmpty(pageSizeString)) {
            template.header(PagingConfiguration.PAGE_SIZE_TAG, pageSizeString);
        }
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        var pageResult = FeignResponseInterceptor.PAGE_RESULT.get();
        if (pageResult != null) {
            response.getHeaders().add(PagingConfiguration.PAGE_NUMBER_TAG, String.valueOf(pageResult.getPageNum()));
            response.getHeaders().add(PagingConfiguration.PAGE_SIZE_TAG, String.valueOf(pageResult.getPageSize()));
            response.getHeaders().add(PagingConfiguration.PAGE_COUNT_TAG, String.valueOf(pageResult.getPages()));
            response.getHeaders().add(PagingConfiguration.TOTAL_TAG, String.valueOf(pageResult.getTotal()));
        }
        FeignResponseInterceptor.PAGE_RESULT.remove();
        return body;
    }
}
