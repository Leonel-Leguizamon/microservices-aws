package com.dlsoftware.msvc.items_msvc.items.api;

import com.dlsoftware.msvc.items_msvc.commons.exceptions.BaseException;
import com.dlsoftware.msvc.items_msvc.commons.exceptions.ProductNotFoundException;
import com.dlsoftware.msvc.items_msvc.commons.model.GeneralErrorResponse;
import com.dlsoftware.msvc.items_msvc.commons.model.GeneralResponseWrapper;
import com.dlsoftware.msvc.items_msvc.items.model.internal.Item;
import com.dlsoftware.msvc.items_msvc.items.service.ItemService;
import com.dlsoftware.msvc.libs.commons.entities.ProductEntity;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RefreshScope
// La anotación @RefreshScope en Spring Cloud sirve para que un bean se vuelva "refrescable" dinámicamente cuando cambian
// las propiedades en el Spring Cloud Config Server (o en cualquier PropertySource que soporte refresco).
// Normalmente, cuando tu aplicación Spring Boot arranca, las propiedades de configuración se inyectan una sola vez en
// tus beans (@Value, @ConfigurationProperties, etc.).
// Si cambiás una propiedad en el Config Server y la aplicación ya está corriendo, los beans no se actualizan automáticamente.
// Cuando anotás un bean con @RefreshScope, este se recrea dinámicamente al hacer un refresh del contexto
// (con el endpoint /actuator/refresh o /actuator/busrefresh si usás Spring Cloud Bus).
// Para cambios masivos o en producción, conviene usar Spring Cloud Bus para propagar el refresh a múltiples instancias automáticamente.
@RequiredArgsConstructor
@RestController
@Slf4j
public class ItemsController {

//    @Qualifier("itemServiceWebClient")
    @Qualifier("itemServiceFeignImpl")
    private final ItemService itemService;

    private final Environment environment;

    private final CircuitBreakerFactory circuitBreakerFactory;

    @GetMapping("/all")
    public ResponseEntity<GeneralResponseWrapper<List<Item>>> findAll(
            @RequestParam(required = false) String name,
            @RequestHeader(name="token-request", required = false) String token
    ) {
        log.info("Items all name: {}", name);
        log.info("Items all token: {}", token);
        return ResponseEntity.ok(
                GeneralResponseWrapper.<List<Item>>builder()
                        .content(this.itemService.findAll())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponseWrapper<Item>> findById(
            @PathVariable Long id
    ) throws ProductNotFoundException {
        log.info("items log id: {}", id);
        return ResponseEntity.ok(
                GeneralResponseWrapper.<Item>builder()
                        .content(
                        circuitBreakerFactory
                                .create("items")
                                .run(
                                        () -> this.itemService.findById(id),
                                        e -> {
                                            log.error("e: {}", e.getMessage());
                                            return new Item(
                                              new ProductEntity(
                                                      0L,
                                                      "name",
                                                      100D,
                                                      "descr",
                                                      0
                                              )
                                            );
                                        }
                                )
                        )
                        .build()
        );
    }

    @CircuitBreaker(name = "items", fallbackMethod = "getFallbackMethodProduct") // Solo aplica para configuraciones en yaml
    @GetMapping("/details/{id}")
    public ResponseEntity<GeneralResponseWrapper<Item>> findDetailsById(
            @PathVariable Long id
    ) throws ProductNotFoundException {
        return ResponseEntity.ok(
                GeneralResponseWrapper.<Item>builder()
                        .content(this.itemService.findById(id))
                        .build()
        );
    }

    // el timelimiter solo establece un tiempo antes de dar timeout exception.
    @CircuitBreaker(name = "items", fallbackMethod = "getFallbackMethodProductLimiter")
    @TimeLimiter(name = "items") // Solo aplica para configuraciones en yaml
    @GetMapping("/details/limiter/{id}")
    public CompletableFuture<?> findDetailsLimitedById(
            @PathVariable Long id
    ) throws ProductNotFoundException {
        return CompletableFuture.supplyAsync(()-> {
            return ResponseEntity.ok(
                    GeneralResponseWrapper.<Item>builder()
                            .content(this.itemService.findById(id))
                            .build()
            );
        });
    }

    @GetMapping("/configs")
    public ResponseEntity<GeneralResponseWrapper<Map<String, String>>> findAllConfigs() {
        Map<String, String> response = new HashMap<>();

        if (environment.getActiveProfiles().length > 0 && environment.getActiveProfiles()[0].equals("dev")) {
            response.put("config.author.name", environment.getProperty("config.author.name"));
            response.put("config.author.email", environment.getProperty("config.author.email"));
        }
        return ResponseEntity.ok()
                    .body(
                        GeneralResponseWrapper.<Map<String, String>>builder()
                                    .content(response)
                                    .build()
                    );
    }

    public CompletableFuture<ResponseEntity<GeneralResponseWrapper<Item>>> getFallbackMethodProductLimiter(
            Throwable t
    ) {
        return CompletableFuture.supplyAsync(()-> {
            log.info("getFallbackMethodProduct: {}", t.getMessage());
            Item responseItem = new Item(
                    new ProductEntity(
                            0L,
                            "name",
                            100D,
                            "descr",
                            0
                    )
            );
            return ResponseEntity.ok().body(
                    GeneralResponseWrapper.<Item>builder()
                            .content(responseItem)
                            .error(
                                    GeneralErrorResponse.builder().errorDescription(t.getMessage()).build()
                            )
                            .build()
            );
        });
    }

    public ResponseEntity<GeneralResponseWrapper<?>> getFallbackMethodProduct(
            Throwable t
    ) {
        log.info("getFallbackMethodProduct: {}", t.getMessage());
        Item responseItem = new Item(
                new ProductEntity(
                        0L,
                        "name",
                        100D,
                        "descr",
                        0
                ));
        return ResponseEntity.ok().body(
                GeneralResponseWrapper.builder()
                            .content(responseItem)
                            .error(
                                    GeneralErrorResponse.builder().errorDescription(t.getMessage()).build()
                            )
                            .build()
                );
    }


    @PostMapping
    public ResponseEntity<GeneralResponseWrapper<ProductEntity>> save(
            @RequestBody ProductEntity product
    ) throws BaseException {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                GeneralResponseWrapper.<ProductEntity>builder()
                        .content(
                                itemService.save(product)
                        )
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id
    ) throws BaseException {
        itemService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                GeneralResponseWrapper.builder()
                        .content(null)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneralResponseWrapper<ProductEntity>> update(
            @PathVariable Long id,
            @RequestBody ProductEntity product
    ) throws BaseException {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                GeneralResponseWrapper.<ProductEntity>builder()
                        .content(itemService.update(product, id))
                        .build()
        );
    }

}
