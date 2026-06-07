package edu.barnett.gearwood_puzzle_store.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 404s. NoHandlerFoundException fires when no controller matches; NoResourceFoundException
     * fires when no static resource matches (this is what an unknown URL throws by default).
     */
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
        // Without this the view renders with HTTP 200; set the real status on the response.
        response.setStatus(status.value());

        // Status/error come from the resolved HttpStatus, not from servlet error-dispatch
        // attributes (those are only populated on a dispatch to /error, never here).
        model.addAttribute("status", status.value());
        model.addAttribute("error", status.getReasonPhrase());
        model.addAttribute("path", request.getRequestURI());
        model.addAttribute("query", request.getQueryString());
        model.addAttribute("method", request.getMethod());
        model.addAttribute("userAgent", request.getHeader("User-Agent"));
        model.addAttribute("exceptionType", ex.getClass().getSimpleName());
        model.addAttribute("exceptionMessage", ex.getMessage());

        return "error";
    }

    /** Map an exception to a status: honor @ResponseStatus / ErrorResponse, else 500. */
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
