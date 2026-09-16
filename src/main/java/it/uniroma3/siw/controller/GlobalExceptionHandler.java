package it.uniroma3.siw.controller;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({EntityNotFoundException.class, NoResourceFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(Exception ex, Model model) {
        log.warn("[WARN] Risorsa non trovata: {}", ex.getMessage());
        model.addAttribute("errorCode", "404");
        model.addAttribute("errorMessage", "La risorsa richiesta non è stata trovata.");
        model.addAttribute("errorDetail", ex.getMessage());
        return "error.html";
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public String handleAccessDenied(Exception ex) {
        log.warn("[WARN] Accesso negato: {}", ex.getMessage());
        return "redirect:/403";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericError(Exception ex, Model model) {
        log.error("[ERROR] Errore interno non gestito: {}", ex.getMessage(), ex);
        model.addAttribute("errorCode", "500");
        model.addAttribute("errorMessage", "Si è verificato un errore interno del server.");
        model.addAttribute("errorDetail", ex.getClass().getSimpleName() + ": " + ex.getMessage());
        return "error.html";
    }
}
