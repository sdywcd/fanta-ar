package plus.meo.fanta.fantaserivce;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sdywcd on 2020/3/13.
 * Des:
 */
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/", "classpath:/resources/",
            "classpath:/static/", "classpath:/public/"};

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
        registry.addResourceHandler("/templates/**")
                .addResourceLocations("classpath:/templates/");
        super.addResourceHandlers(registry);
    }


    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        /**
         * 1.先定义一个convert转换消息的对象
         * 2.添加fastjson的配置信息，比如：是否要格式化返回的json数据
         * 3.在convert中添加配置信息
         * 4.将convert添加到converters当中
         */
        // 1.先定义一个convert转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        // 2.添加fastjson的配置信息，比如：是否要格式化返回的json数据
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty
        );
        // 处理中文乱码问题(不然出现中文乱码)
        List<MediaType> fastMediaTypes = new ArrayList<MediaType>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);
        // 3.在convert中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);
        return new HttpMessageConverters(fastConverter);
    }

//    @Bean
//    RestTemplate restTemplate(ClientHttpRequestFactory simpleClientHttpRequestFactory, @Qualifier("loggingRequestInterecptor") LoggingRequestInterecptor loggingRequestInterecptor){
//        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(simpleClientHttpRequestFactory));
//        restTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());
//        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
//        interceptors.add(loggingRequestInterecptor);
//        restTemplate.setInterceptors(interceptors);
//        return restTemplate;
//    }

    @Bean
    RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());
        return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory(){
        SimpleClientHttpRequestFactory factory=new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(15000);
        factory.setReadTimeout(5000);
        return factory;
    }


//    @Override
//    protected void addInterceptors(InterceptorRegistry registry) {
//       // registry.addInterceptor(new ApiHanderInterceptor()).addPathPatterns("/api/dingd/**");
//       // registry.addInterceptor(new CocoApiHanderInterceptor()).addPathPatterns("/api/coco/**");
//        super.addInterceptors(registry);
//    }
}
