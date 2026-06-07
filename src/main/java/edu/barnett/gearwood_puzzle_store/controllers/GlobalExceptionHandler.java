package edu.barnett.gearwood_puzzle_store.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // The global @ModelAttribute methods that feed the header (firstName / cartCount)
    // are NOT invoked for @ExceptionHandler views, so we replay them here.
    private final GlobalModelAttributes globalModelAttributes;

    public GlobalExceptionHandler(GlobalModelAttributes globalModelAttributes) {
        this.globalModelAttributes = globalModelAttributes;
    }

    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    public String handleNotFound(Exception ex, HttpServletRequest request,
                                 HttpServletResponse response, Model model) {
        return render(ex, request, response, model, HttpStatus.NOT_FOUND);
    }

    /** Catch-all for everything else. */
    @ExceptionHandler(Exception.class)
    public String handleAnyException(Exception ex, HttpServletRequest request,
                                     HttpServletResponse response, Model model) {
        return render(ex, request, response, model, resolveStatus(ex));
    }

    private String render(Exception ex, HttpServletRequest request,
                          HttpServletResponse response, Model model, HttpStatus status) {
        response.setStatus(status.value());

        model.addAttribute("status", status.value());
        model.addAttribute("error", status.getReasonPhrase());
        model.addAttribute("path", request.getRequestURI());
        model.addAttribute("query", request.getQueryString());
        model.addAttribute("method", request.getMethod());
        model.addAttribute("userAgent", request.getHeader("User-Agent"));
        model.addAttribute("exceptionType", ex.getClass().getSimpleName());
        model.addAttribute("exceptionMessage", ex.getMessage());

        // Header data (greeting + cart badge). Guarded so a secondary failure here
        // can never turn the error page itself into a fresh error.
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            model.addAttribute("firstName", globalModelAttributes.firstName(auth));
            model.addAttribute("cartCount", globalModelAttributes.cartCount(auth));
        } catch (Exception ignored) {
            // Leave header data absent; the page still renders.
        }

        return "error";
    }

    private HttpStatus resolveStatus(Exception ex) {
        ResponseStatus annotation = AnnotatedElementUtils.findMergedAnnotation(ex.getClass(), ResponseStatus.class);
        if (annotation != null) {
            return annotation.code();
        }
        if (ex instanceof ErrorResponse errorResponse) {
            HttpStatus resolved = HttpStatus.resolve(errorResponse.getStatusCode().value());
            if (resolved != null) {
                return resolved;
            }
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
