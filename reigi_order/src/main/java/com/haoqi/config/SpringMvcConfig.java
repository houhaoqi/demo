package com.haoqi.config;

import com.haoqi.common.JacksonObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 1.响应静态资源映射
 * 2.处理 json 只有16位的精度运算
 *  将id转为字符串传给后台
 * @author haoqi
 * @Date 2022/7/20 - 19:42
 */

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {
    //设置资源映射
    //前台请求，后台映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("静态资源映射");
        registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resources/");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    /**
     * 在前面的webMvcConfig 配置类中扩展spring mvc 的消息转换器，
     * 在此消息转换器中使用spring提供的对象转换器进行Java对象到json数据的转换；
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        System.out.println("扩展消息格式json-java-string转换器");
        //创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用Jackson将Java对象转为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器对象追加到mvc框架的转换器集合中
        //转换器是有优先级顺序的，这里我们把自己定义的消息转换器设置为第一优先级，所以会优先使用我们的转换器来进行相关数据进行转换，如果我们的转换器没有匹配到相应的数据来转换，那么就会去寻找第二个优先级的转换器，以此类推
        converters.add(0,messageConverter);
    }
}
