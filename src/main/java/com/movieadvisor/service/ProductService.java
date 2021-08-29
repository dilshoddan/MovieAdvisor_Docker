package com.movieadvisor.service;

import com.movieadvisor.model.Product;
import com.movieadvisor.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> readProducts () {
        return productRepository.findAll();
    }

}
