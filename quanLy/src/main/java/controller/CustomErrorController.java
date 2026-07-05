package controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {
    
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("title", "Trang Không Tìm Thấy");
                model.addAttribute("errorCode", "404");
                model.addAttribute("errorMessage", "Trang bạn tìm kiếm không tồn tại.");
                return "error/404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("title", "Lỗi Hệ Thống");
                model.addAttribute("errorCode", "500");
                model.addAttribute("errorMessage", "Đã xảy ra lỗi hệ thống. Vui lòng thử lại sau.");
                return "error/500";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("title", "Truy Cập Bị Từ Chối");
                model.addAttribute("errorCode", "403");
                model.addAttribute("errorMessage", "Bạn không có quyền truy cập trang này.");
                return "error/403";
            }
        }
        
        model.addAttribute("title", "Lỗi");
        model.addAttribute("errorCode", "Error");
        model.addAttribute("errorMessage", "Đã xảy ra lỗi không xác định.");
        return "error/general";
    }
}
