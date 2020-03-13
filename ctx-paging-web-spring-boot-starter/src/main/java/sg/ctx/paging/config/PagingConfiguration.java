package sg.ctx.paging.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// import sg.ctx.paging.interceptor.PagingWebInterceptor;

/**
 * @author yu.miao
 */
@Configuration
public class PagingConfiguration implements WebMvcConfigurer {

    public static final String PAGE_NUMBER_TAG = "pageNumber";
    public static final String PAGE_SIZE_TAG = "pageSize";
    public static final String PAGE_COUNT_TAG = "pageCount";
    public static final String TOTAL_TAG = "total";
}
