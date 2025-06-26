package MainApp.Services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import MainApp.DTOs.ProductDetailsDto;
import MainApp.DTOs.ProductListDto;
import MainApp.DTOs.ProductRequestDto;
import MainApp.Entities.Category;
import MainApp.Entities.Product;
import MainApp.Entities.ProductDetails;
import MainApp.Entities.ProductFeatures;
import MainApp.Entities.ProductImages;
import MainApp.Repositories.CategoryRepository;
import MainApp.Repositories.ProductDetailsRepository;
import MainApp.Repositories.ProductFeaturesRepository;
import MainApp.Repositories.ProductImagesRepository;
import MainApp.Repositories.ProductsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor

public class ProductsService {

	private final ProductsRepository productRepository; 
	private final CloudinaryService cloudinaryService;
	private final CategoryRepository categoryRepository;
	private final ProductDetailsRepository productDetailsRepository;
	private final ProductImagesRepository productImageRepository;
	private final ProductFeaturesRepository featureRepository;
	
	

	public String saveProductHandler(ProductRequestDto productRequest) throws IOException {

	    Product product = new Product();
	    Category categoryObject = categoryRepository.findByName(productRequest.getCategoryName())
	        .orElseThrow(() -> new EntityNotFoundException("Category not found"));

	    // Upload and set the main image
	    String mainImageUrl = cloudinaryService.uploadImageHandlerWithURL(productRequest.getImageUrl());
	    product.setImageUrl(mainImageUrl);
	    product.setName(productRequest.getName());
	    product.setDescription(productRequest.getDescription());
	    product.setCategory(categoryObject);
	    product.setPrice(productRequest.getPrice());

	    // Save product first to get an ID for relations
	    productRepository.save(product);

	    // Upload and save each additional image
	    for (String imageUrl : productRequest.getImages()) {
	        String uploadedImageUrl = cloudinaryService.uploadImageHandlerWithURL(imageUrl);

	        ProductImages productImage = new ProductImages();
	        productImage.setProduct(product);
	        productImage.setUrl(uploadedImageUrl);

	        productImageRepository.save(productImage);
	    }

	    // Save features
	    for (String featureStr : productRequest.getFeatures()) {
	        ProductFeatures feature = new ProductFeatures();
	        feature.setFeature(featureStr);
	        feature.setProduct(product);
	        featureRepository.save(feature);
	    }

	    // Save product details
	    ProductDetails details = new ProductDetails();
	    details.setMaterial(productRequest.getDetails().getMaterial());
	    details.setDimensions(productRequest.getDetails().getDimensions());
	    details.setWeight(productRequest.getDetails().getWeight());
	    details.setOrigin(productRequest.getDetails().getOrigin());
	    details.setWarranty(productRequest.getDetails().getWarranty());

	    // Set bidirectional link
	    details.setProduct(product);
	    product.setDetails(details);

	    productDetailsRepository.save(details);
	    productRepository.save(product); // update product with details relation

	    return mainImageUrl;
	}

	
	
	
	public List<ProductListDto> retrieveProductsHandler(boolean isFeatured) {
	    List<ProductListDto> products = productRepository.findByIsFeatured(isFeatured);
	    return products != null ? products : new ArrayList<>();
	}

	
	public void deleteProductHandler(long id) {
		  productRepository.deleteById(id);
	}



	public ProductRequestDto getProductHandler(long id) {
	    Product product = productRepository.findById(id).orElse(new Product());

	    ProductRequestDto dto = new ProductRequestDto();
	    dto.setProductId(product.getProductId());
	    dto.setName(product.getName());
	    dto.setPrice(product.getPrice());
	    dto.setDescription(product.getDescription());
	    dto.setImageUrl(product.getImageUrl());
	    dto.setFeatured(product.isFeatured());
	    dto.setRating(product.getRating());
	    dto.setReviewCount(product.getReviewCount());
	    dto.setNew(product.isNew());
	    dto.setInStock(product.isInStock());
	    dto.setCreatedAt(product.getCreatedAt());
	    dto.setCategoryName(product.getCategory().getName());

	    // Details
	    ProductDetailsDto detailsDto = new ProductDetailsDto();
	    if (product.getDetails() != null) {
	        detailsDto.setMaterial(product.getDetails().getMaterial());
	        detailsDto.setDimensions(product.getDetails().getDimensions());
	        detailsDto.setWeight(product.getDetails().getWeight());
	        detailsDto.setOrigin(product.getDetails().getOrigin());
	        detailsDto.setWarranty(product.getDetails().getWarranty());
	    }
	    dto.setDetails(detailsDto);

	    // Features
	    List<String> featureList = new ArrayList<>();
	    for (ProductFeatures f : product.getFeatures()) {
	        featureList.add(f.getFeature());
	    }
	    dto.setFeatures(featureList);

	    // Images
	    List<String> imageList = new ArrayList<>();
	    for (ProductImages img : product.getImages()) {
	        imageList.add(img.getUrl());
	    }
	    dto.setImages(imageList);

	    return dto;
	}

	
	
}
