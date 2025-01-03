package com.eshop.setting;

import com.eshop.common.entity.setting.Setting;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Order(-123)
public class SettingFilter implements Filter {
    @Autowired
    private SettingService settingService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest =(HttpServletRequest) request;
        String url = servletRequest.getRequestURL().toString();

        if (url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".png") || url.endsWith(".jpg")) {
            chain.doFilter(request, response);
            return;
        }

        List<Setting> generalSettings = settingService.getGeneralSettings();
        generalSettings.forEach(setting -> {
            //System.out.println(setting);
            request.setAttribute(setting.getKey(), setting.getValue());
        });
        chain.doFilter(request, response);
    }
}
