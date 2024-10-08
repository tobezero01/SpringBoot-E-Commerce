package com.eshop.admin.paging;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PagingAndSortingArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(PagingAndSortingParam.class) != null;
    }
    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer model,
                                  NativeWebRequest request,
                                  WebDataBinderFactory binderFactory) throws Exception {
        String sortDir = request.getParameter("sortDir");
        String sortField = request.getParameter("sortField");
        String keyWord = request.getParameter("keyWord");

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyWord", keyWord);

        PagingAndSortingParam annotation = parameter.getParameterAnnotation(PagingAndSortingParam.class);
        return new PagingAndSortingHelper(model, annotation.listName(), sortField,sortDir,keyWord);
    }
}
