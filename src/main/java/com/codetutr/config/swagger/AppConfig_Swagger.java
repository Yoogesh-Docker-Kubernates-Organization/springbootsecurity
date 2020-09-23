package com.codetutr.config.swagger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.codetutr.validationHelper.LemonConstant;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
public class AppConfig_Swagger implements WebMvcConfigurer {

	private static final String NO_DESCRIPTION = "";

	/**
	 * If you want to keep @EnableWebMvc annotation any way, you have to add the
	 * following:
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

	}

	/**
	 * 
	 * Want to exclude the @controller an inclue only @RestController annotated
	 * classes to show in a /swagger-ui.html page By the way if you still want to
	 * exclude some @RestController annotated classes then you need to
	 * use @ApiIgnore in a class level individually
	 */
	@Bean
	public Docket swaggerCustomization() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.codetutr.restAPI"))
				.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class)).paths(PathSelectors.any()) // .paths(PathSelectors.ant("/api/*"))
				.build().apiInfo(apiDetails()).globalOperationParameters(showContentTypeParamater())
				.tags(new Tag(LemonConstant.SWAGGER_AUTHENTICATION_DESCRIPTION, NO_DESCRIPTION))
				.tags(new Tag(LemonConstant.SWAGGER_USER_DESCRIPTION, NO_DESCRIPTION))
				.tags(new Tag(LemonConstant.SWAGGER_WEBSOCKET_DESCRIPTION, NO_DESCRIPTION))
				.tags(new Tag(LemonConstant.SWAGGER_COMMON_DESCRIPTION, NO_DESCRIPTION));

	}

	@Bean
	UiConfiguration uiConfig() {
		return UiConfigurationBuilder.builder().docExpansion(DocExpansion.LIST).build(); // or DocExpansion.NONE or // DocExpansion.FULL
																							

	}

	private ApiInfo apiDetails() {
		return new ApiInfo("Yoogesh Rest API",
				"<span style='color:red; font-size:15px; font-weight:bold'>A Rest API For Yoogesh</span>", "1.0",
				"Free to use",
				new springfox.documentation.service.Contact("Yoogesh Sharma", "https://www.google.com/", "y@gmail.com"),
				"Api lincence", "https://www.google.com/", Collections.emptyList());
	}

	private List<Parameter> showContentTypeParamater() {
		return Arrays.asList(new ParameterBuilder().name("Content-Type").modelRef(new ModelRef("string"))
				.parameterType("header").required(true).defaultValue("application/json").build());
	}

}
