package com.closer.xt.web.config;


import com.closer.xt.web.handler.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*");//
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //add代表需要拦截的，exclude代表不需要拦截的
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/topic/*")
                .addPathPatterns("/subject/*")
                .addPathPatterns("/course/*")
                .addPathPatterns("/order/*")
                .addPathPatterns("/user/*")
                .addPathPatterns("/i/*")
                .excludePathPatterns("/course/courseList")
                .excludePathPatterns("/subject/listSubjectNew")
                .excludePathPatterns("/course/subjectInfo")
                .excludePathPatterns("/order/notify")
                .excludePathPatterns("/case/*")
                .excludePathPatterns("/wechat/*")
                .excludePathPatterns("/login/wxLoginCallBack")
                .excludePathPatterns("/i/u/*");
//                .excludePathPatterns("/course/courseList");;
    }

    //    /**
//     * 跨域过滤器
//     *
//     * @return
//     */
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        this.addAllowedOrigins(corsConfiguration);
//        corsConfiguration.addAllowedHeader("*");
//        corsConfiguration.addAllowedMethod("*");
//        corsConfiguration.addAllowedOrigin("*");
//        source.registerCorsConfiguration("/**", corsConfiguration);
//        return new CorsFilter(source);
//    }
//
//    private void addAllowedOrigins(CorsConfiguration corsConfiguration) {
////        for (String origin : originsVal) {
//            corsConfiguration.addAllowedOrigin("*");
////        }
//    }
}
