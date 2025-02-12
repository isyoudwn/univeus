package com.example.univeus.common.resolver;


import com.example.univeus.domain.meeting.model.MeetingCategory;
import com.example.univeus.presentation.meeting.dto.request.MainPageRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class MainPageSearchConditionResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter
                .getParameterType()
                .equals(MainPageRequest.MainPageCursor.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        String requestCursor = request.getParameter("id");
        String category = request.getParameter("category");

        MeetingCategory meetingCategory = (!category.equals("none")) ? MeetingCategory.of(category) : null;
        Long cursor = (!requestCursor.equals("none")) ? Long.valueOf(requestCursor) : null;

        return new MainPageRequest.MainPageCursor(cursor, meetingCategory);
    }
}
