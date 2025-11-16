package com.dlsoftware.msvc.products_msvc.utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@Slf4j
@AllArgsConstructor
public class MessageUtil {

    @Autowired
    private MessageSource messageSource;

    /**
     * Retorna el mensaje asociado a la clave recibida.
     * @param key clave de un mensaje
     * @return
     */
    public String getMessage(String key) {
        return getMessage(key, null);
    }

    /**
     * Retorna el mensaje asociado a la clave recibida realizando
     * el reemplazo de los argumentos.
     * @param key clave de un mensaje
     * @param args argumentos para reemplazar en el mensaje
     * @return
     */
    public String getMessage(String key, Object[] args) {
        Locale locale = LocaleContextHolder.getLocale();

        String message = null;
        try {
            message = messageSource.getMessage(key, args, locale);
        } catch (NoSuchMessageException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        return message;
    }
}


