package com.Snack_BE.Service;

import java.util.List;
import java.util.Map;
import com.Snack_BE.DTOs.ProductResquestDTO;
import com.Snack_BE.Model.ProductEntity;
import com.Snack_BE.Model.ShopEntity;
import com.Snack_BE.Model.UserEntity;
import com.Snack_BE.Repo.ShopRepo;
import com.Snack_BE.Repo.UserRepo;
import com.Snack_BE.config.ProductMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.Snack_BE.DTOs.ProductDTO;
import com.Snack_BE.Repo.ProductRepo;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final ShopRepo shopRepo;
    private final UserRepo userRepo;
    private final ProductMapper productMapper;
    public ResponseEntity<List<ProductDTO>> getAllEntity() {
        return ResponseEntity.ok(productRepo.findAllProductDTO());
    }

    public ResponseEntity<?> addNewProduct(ProductResquestDTO productResquestDTO,Long userId) {
        if (userId==null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","The user is not found"));

        ShopEntity shop = shopRepo.getShopEntityByUser(userRepo.findById(userId).orElseThrow());
        if (shop==null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","The shop is not found"));

        ProductEntity productEntity=new ProductEntity();

        productEntity.setProductName(productResquestDTO.getProductName());
        productEntity.setPrice(productResquestDTO.getPrice());
        productEntity.setProductName(productResquestDTO.getProductName());
        productEntity.setImage_url(productResquestDTO.getImage_url());
        productEntity.setDescription(productResquestDTO.getDescription());
        productEntity.setShop(shop);

        productRepo.save(productEntity);
        return  ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message","Add product successfully"));
    }

    public ResponseEntity<?> getByUser(Long userId){
        if (userId==null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","The user is not found"));

        ShopEntity shop = shopRepo.getShopEntityByUser(userRepo.findById(userId).orElseThrow());
        if (shop==null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","The shop is not found"));
        List<ProductDTO> productDTOList = productRepo.findAllByShop(shop);
//        List<ProductDTO> productDTOList = productEntityList.stream().map(productMapper::toDto).toList();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productDTOList);
    }
}
