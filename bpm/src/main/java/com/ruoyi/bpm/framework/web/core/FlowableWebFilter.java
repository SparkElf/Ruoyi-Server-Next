package com.ruoyi.bpm.framework.web.core;


import com.ruoyi.bpm.framework.flowable.core.util.FlowableUtils;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import org.flowable.common.engine.impl.identity.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Flowable Web 过滤器，将 userId 设置到 {@link org.flowable.common.engine.impl.identity.Authentication} 中
 *
 * @author jason
 */
public class FlowableWebFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {


            try {
                // 设置工作流的用户 不能直接用SecurityUtils.getLoginUserId()，会报空指针，要分步骤拆开
                LoginUser loginUser = SecurityUtils.getLoginUserSafe();
                if (loginUser != null&&loginUser.getUserId()!=null) {
                    Authentication.setAuthenticatedUserId(loginUser.getUserId().toString());
                }

                // 过滤
                chain.doFilter(request, response);
            } finally {
                // 清理
                Authentication.setAuthenticatedUserId(null);
            }
    }

}
