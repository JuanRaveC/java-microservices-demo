package co.edu.cedesistemas.commerce.social.controller;

import co.edu.cedesistemas.commerce.social.model.Store;
import co.edu.cedesistemas.commerce.social.repository.StoreRepository;
import co.edu.cedesistemas.commerce.social.service.StoreService;
import co.edu.cedesistemas.common.DefaultResponseBuilder;
import co.edu.cedesistemas.common.model.Status;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@AllArgsConstructor
@Slf4j
public class StoreController {
    private final StoreService service;

    @PostMapping("/stores")
    public ResponseEntity<Status<?>> createStore(@RequestBody Store store) {
        try {
            Store created = service.createStore(store);
            addSelfLink(created);
            return DefaultResponseBuilder.defaultResponse(created, HttpStatus.CREATED);
        } catch (Exception ex) {
            return DefaultResponseBuilder.errorResponse(ex.getMessage(), ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/stores/{id}/products/{productId}")
    public ResponseEntity<Status<?>> addStoreProduct(@PathVariable String id, @PathVariable String productId) {
        try {
            service.addProduct(id, productId);
            return DefaultResponseBuilder.defaultResponse("Product Created", HttpStatus.CREATED);
        } catch (Exception ex) {
            return DefaultResponseBuilder.errorResponse(ex.getMessage(), ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/stores/{id}")
    @HystrixCommand(fallbackMethod = "getByIdFallback")
    public ResponseEntity<Status<?>> getStoreById(@PathVariable String id) {
        Store found = service.getById(id);
        if (found != null) {
            addLinks(found);
            return DefaultResponseBuilder.defaultResponse(found, HttpStatus.OK);
        } else return DefaultResponseBuilder.errorResponse("store not found", null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/stores/{storeId}/products/top")
    public ResponseEntity<Status<?>> getTopNProducts(@PathVariable String storeId,
                                          @RequestParam(required = false, defaultValue = "5") Integer limit) {
        List<StoreRepository.ProductOccurrence> topN = service.getTopNProducts(storeId, limit);
        try{
            if(!topN.isEmpty()) return DefaultResponseBuilder.defaultResponse(topN, HttpStatus.OK);
            else return DefaultResponseBuilder.defaultResponse("No products found", HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return DefaultResponseBuilder.errorResponse("store not found", null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private static void addSelfLink(@NotNull final Store store) {
        Link selfLink = linkTo(methodOn(StoreController.class)
                .getStoreById(store.getId()))
                .withSelfRel().withType("GET");
        store.add(selfLink);
    }

    private static void addLinks(@NotNull final Store store) {
        Link byTypeLink = linkTo(methodOn(StoreController.class)
                .getTopNProducts(store.getId(), 5))
                .withRel("top-5-products")
                .withType("GET");
        store.add(byTypeLink);
    }

    private ResponseEntity<Status<?>> getByIdFallback(final String id) {
        log.error("getting store by id fallback {}", id);
        Status<?> status = Status.builder()
                ._hits(1)
                .message("service unavailable. please try again")
                .code(HttpStatus.SERVICE_UNAVAILABLE.value())
                .build();
        return new ResponseEntity<>(status, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
