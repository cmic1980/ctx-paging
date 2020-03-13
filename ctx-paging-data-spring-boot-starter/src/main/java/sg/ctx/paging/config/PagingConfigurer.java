package sg.ctx.paging.config;

import com.github.pagehelper.PageInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sg.ctx.paging.interceptor.PagingWebInterceptor;

/**
 * @author yu.miao
 */
@Configuration
public class PagingConfigurer implements WebMvcConfigurer {

    @Bean
    public PageInterceptor pageInterceptor() {
        PageInterceptor interceptor = new PageInterceptor();
        return interceptor;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        var pagingWebInterceptor = new PagingWebInterceptor();
        registry.addInterceptor(pagingWebInterceptor).addPathPatterns("/**");
    }
}
