package MainApp.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import MainApp.DTOs.ProductListDto;
import MainApp.DTOs.ProductRequestDto;
import MainApp.Entities.Product;
import MainApp.Services.ProductsService;

@RestController
@RequestMapping(path = "/api/products")

public class ProductsController {

	@Autowired
	ProductsService productService;

	@PutMapping("/add")
	public ResponseEntity<String> saveProduct(@RequestBody ProductRequestDto productRequest) {
		try {
			return ResponseEntity.ok(productService.saveProductHandler(productRequest));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.CONFLICT).body("the Product saving was failed !!");
		}
	}

	@GetMapping(path = "/public/list/{isFeatured}")
	public ResponseEntity<List<ProductListDto>> retrieveProducts(@PathVariable boolean isFeatured) {
		try { 
		return ResponseEntity.ok(productService.retrieveProductsHandler(isFeatured));
		 }catch(Exception e) { 
			 e.printStackTrace();
			return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
		 }
	}

	@DeleteMapping(path = "/delete/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable long id) {
		try {
			productService.deleteProductHandler(id);
			return ResponseEntity.ok("the product was successfuly deleted !");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Can not delete the prodcut !");
		}
	}
	
	@GetMapping(path="/public/getProduct/{id}")
	public ResponseEntity<ProductRequestDto> getProduct(@PathVariable long id){
		try { 
			return ResponseEntity.ok(productService.getProductHandler(id));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
		}
		
	}
	
	
	
	
	
}
