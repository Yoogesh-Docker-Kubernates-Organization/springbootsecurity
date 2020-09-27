package com.codetutr.interceptor;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


/**
 * We are not overriding the postHandle method here. In myCountry application, postHandle was used to put the Authentication state bean i.e. AuthenticationStateBeanHandlerInterceptor
 * where you put all the session related variables before passing model to view
 */
@Component
public class RequestParamValidationInterceptor extends HandlerInterceptorAdapter {

	private final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

	@Override
	public final boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
			final Object handler) throws Exception {

		if (handler instanceof HandlerMethod) {
			areParametersValid((HandlerMethod) handler, request);
			return true;
		}

		return super.preHandle(request, response, handler);
	}

	private boolean areParametersValid(final HandlerMethod handlerMethod, final HttpServletRequest request)
			throws RuntimeException {
		final MethodParameter[] methodParams = handlerMethod.getMethodParameters();
		final List<MethodParameter> tobeValidatedParamList = new ArrayList<>();

		for (final MethodParameter param : methodParams) {
			if (isParameterTobeValidated(param)) {
				tobeValidatedParamList.add(param);
			}

		}

		if (!tobeValidatedParamList.isEmpty()) {
			validate(tobeValidatedParamList, request);
		}

		return true;
	}

	private void validate(final List<MethodParameter> tobeValidatedParamList, final HttpServletRequest request)
			throws RuntimeException {

		for (final MethodParameter param : tobeValidatedParamList) {
			String paramName = getparamName(request, param);
			
			if (param.getParameterAnnotation(Pattern.class) != null) {
				final Pattern pattern = param.getParameterAnnotation(Pattern.class);
				if (!isPatternValid(paramName, pattern.regexp())) {
					throw new RuntimeException(pattern.message(), null);
				}
			}

			if (param.getParameterAnnotation(Max.class) != null) {
				final Max max = param.getParameterAnnotation(Max.class);
				validateMax(max.value(), paramName, param.getParameterType(),
						max.message());
			}

			if (param.getParameterAnnotation(Min.class) != null) {
				final Min min = param.getParameterAnnotation(Min.class);
				validateMin(min.value(), paramName, param.getParameterType(),
						min.message());
			}

			if (param.getParameterAnnotation(NotNull.class) != null) {
				final NotNull notNull = param.getParameterAnnotation(NotNull.class);
				if (!StringUtils.isNotBlank(paramName)) {
					throw new RuntimeException(notNull.message(), null);
				}
			}

			if (param.getParameterAnnotation(Digits.class) != null) {
				final Digits digits = param.getParameterAnnotation(Digits.class);
				validateDigits(paramName, digits.message());
			}

		}

	}

	@SuppressWarnings("unchecked")
	private String getparamName(HttpServletRequest request, final MethodParameter param) {
		if(param.getParameterAnnotation(RequestHeader.class) != null) {
			return request.getHeader(param.getParameterName());
		}
		else if (param.getParameterAnnotation(PathVariable.class) != null){
			return ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE)).get(param.getParameterName());
		}else {
			return request.getParameter(param.getParameterName());
		}
	}

	private void validateDigits(final String value, final String code) throws RuntimeException {
		if (value == null || value.isEmpty()) {
			return;
		}

		if (!value.matches("\\d+")) {
			throw new RuntimeException(code, null);
		}
	}

	private void validateMin(final long value, final Object parameter, final Class type, final String code)
			throws RuntimeException {
		if (parameter == null || parameter.toString().isEmpty()) {
			return;
		}

		// validation for double.
		if (type == Double.class) {
			Double requestValue;
			try {
				requestValue = Double.valueOf((String) parameter);
			} catch (NumberFormatException e) {
				throw new RuntimeException("twm.category.radius.invalid",
						null);
			}
			final Double comapreValue = Double.valueOf(value + "");

			if (requestValue.compareTo(comapreValue) < 0) {
				throw new RuntimeException(code,  null);
			}
		}

		// validation for integer.
		if (type == Integer.class) {
			final Integer requestValue = Integer.valueOf((String) parameter);
			final Integer compareValue = Integer.valueOf(value + "");

			if (requestValue.compareTo(compareValue) < 0) {
				throw new RuntimeException(code, null);
			}
		}

		// validation for long.
		if (type == Long.class) {

			final Long requestValue = Long.valueOf((String) parameter);
			final Long compareValue = Long.valueOf(value + "");

			if (requestValue.compareTo(compareValue) < 0) {
				throw new RuntimeException(code, null);
			}

		}

		// validation for float.
		if (type == Float.class) {

			final Float requestValue = Float.valueOf((String) parameter);
			final Float compareValue = Float.valueOf(value + "");

			if (requestValue.compareTo(compareValue) < 0) {
				throw new RuntimeException(code,null);
			}
		}
	}

	public static boolean isPatternValid(final String parameterValue, final String regex) {
		return parameterValue == null || parameterValue.isEmpty() || parameterValue.matches("(?i)" + regex);
	}

	private void validateMax(final long maxValue, final Object value, final Class type, final String code)
			throws RuntimeException {
		if (value == null || value.toString().isEmpty()) {
			return;
		}

		// validation for double.
		if (type == Double.class) {
			Double requestValue;
			try {
				requestValue = Double.valueOf((String) value);
			} catch (NumberFormatException e) {
				throw new RuntimeException("twm.category.radius.invalid",
						null);
			}
			final Double comapreValue = Double.valueOf(maxValue + "");

			if (requestValue.compareTo(comapreValue) > 0) {
				throw new RuntimeException(code, null);
			}
		}

		// validation for integer.
		if (type == Integer.class) {
			final Integer requestValue = Integer.valueOf((String) value);
			final Integer compareValue = Integer.valueOf(maxValue + "");

			if (requestValue.compareTo(compareValue) > 0) {
				throw new RuntimeException(code,null);
			}
		}

		// validation for long.
		if (type == Long.class) {

			final Long requestValue = Long.valueOf((String) value);
			final Long compareValue = Long.valueOf(maxValue + "");

			if (requestValue.compareTo(compareValue) > 0) {
				throw new RuntimeException(code, null);
			}

		}

		// validation for float.
		if (type == Float.class) {

			final Float requestValue = Float.valueOf((String) value);
			final Float compareValue = Float.valueOf(maxValue + "");

			if (requestValue.compareTo(compareValue) > 0) {
				throw new RuntimeException(code,  null);
			}
		}
	}

	private boolean isParameterTobeValidated(final MethodParameter param) {
		param.initParameterNameDiscovery(this.parameterNameDiscoverer);
		final Annotation[] annotations = param.getParameterAnnotations();
		boolean requestParam = false;
		boolean validParam = false;

		if (annotations != null && annotations.length > 0) {
			for (final Annotation annotation : annotations) {
				if (annotation instanceof RequestParam || annotation instanceof PathVariable || annotation instanceof RequestHeader) {
					requestParam = true;
				} else if (annotation instanceof Valid) {
					validParam = true;
				}
			}
		}

		return requestParam && validParam;
	}

}
