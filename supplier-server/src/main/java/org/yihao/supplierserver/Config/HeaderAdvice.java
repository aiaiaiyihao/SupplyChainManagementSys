package org.yihao.supplierserver.Config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.yihao.shared.DTOS.UserContext;

//@RestControllerAdvice
//public class HeaderAdvice {
//    @ModelAttribute("userContext")
//    public UserContext populateUserContext(HttpServletRequest request) {
//        UserContext ctx = new UserContext();
//        ctx.setTableId(Long.valueOf(request.getHeader("tableId")));
//        ctx.setRole(request.getHeader("role"));
//        ctx.setUsername(request.getHeader("username"));
//        return ctx;
//    }
//}
