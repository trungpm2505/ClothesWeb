package com.web.clothes.ClothesWeb.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.web.clothes.ClothesWeb.dto.mapper.Mapper;
import com.web.clothes.ClothesWeb.dto.requestDto.ProductRequestDto;
import com.web.clothes.ClothesWeb.dto.responseDto.ProductAttributeValueResponseDto;
import com.web.clothes.ClothesWeb.dto.responseDto.ProductResponseDto;
import com.web.clothes.ClothesWeb.dto.responseDto.ProductPageResponseDto;
import com.web.clothes.ClothesWeb.entity.AttributeValue;
import com.web.clothes.ClothesWeb.entity.Category;
import com.web.clothes.ClothesWeb.entity.Product;
import com.web.clothes.ClothesWeb.entity.ProductAttributeValue;
import com.web.clothes.ClothesWeb.service.AttributeValueService;
import com.web.clothes.ClothesWeb.service.CategoryService;
import com.web.clothes.ClothesWeb.service.ImageService;
import com.web.clothes.ClothesWeb.service.OrderDetailsService;
import com.web.clothes.ClothesWeb.service.ProductAttributeValueService;
import com.web.clothes.ClothesWeb.service.ProductService;
import com.web.clothes.ClothesWeb.service.UserService;
import com.web.clothes.ClothesWeb.dto.requestDto.CartRequestDto;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/product")
public class ProductController {
	private final UserService userService;
	private final ProductService productService;
	private final AttributeValueService attributeValueService;
	private final ProductAttributeValueService productAttributeValueService;
	private final ImageService imageService;
	private final OrderDetailsService orderDetailsService;
	private final CategoryService categoryService;
	private final Mapper mapper;
	private static Log log = LogFactory.getLog(ProductController.class);

	// return view product
	@GetMapping("/admin")
	public String getProductView(Model model) {
		log.info("Get product view for admin");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("userName", userService.findUserByEmail(authentication.getName()).get().getUserName());
		return "admin/product";
	}

	@PostMapping(value = "/add", consumes = "multipart/form-data", produces = { "application/json", "application/xml" })
	@Transactional
	@ResponseBody
	public ResponseEntity<?> upload(@RequestParam(value = "file", required = false) MultipartFile[] files,
			@RequestParam int defaultFileIndex, @Valid @ModelAttribute ProductRequestDto productRequestDto,
			BindingResult bindingResult, HttpSession session) {
		log.info("Save product");
		Map<String, Object> errors = new HashMap<>();

		Optional<AttributeValue> color = attributeValueService
				.getAttributeValue(productRequestDto.getColorAttributeValue());
		Optional<AttributeValue> size = attributeValueService
				.getAttributeValue(productRequestDto.getSizeAttributeValue());

		// validate input data
		if (bindingResult.hasErrors()) {
			log.error("Save product has error "+ bindingResult.getAllErrors());
			errors.put("bindingErrors", bindingResult.getAllErrors());
		}
		if (files == null) {
			log.error("Save product has error 'Please upload at least a file'");
			errors.put("fileErrors", "Please upload at least a file.");
		}
		Optional<Product> productByTitle = productService.getProductByTitle(productRequestDto.getTitle());

		if (productByTitle.isPresent()) {
			log.error("Save product has error 'Title already exists'");
			errors.put("titleDuplicate", "Title already exists! please enter a new title. ");
		}


		if (!errors.isEmpty()) {
			return ResponseEntity.badRequest().body(errors);
		}
		// product
		Product product = mapper.productRequestDtoToProduct(productRequestDto);
		productService.save(product);
		Optional<Product> savedProduct = productService.getProductByTitle(productRequestDto.getTitle());
		// product_attributValue

		if (savedProduct.isPresent()) {
			if(color.isPresent() || size.isPresent()) {
				ProductAttributeValue productAttributeValue = new ProductAttributeValue();
				if (color.isPresent() && size.isPresent()) {
					productAttributeValue.setProduct(savedProduct.get());
					productAttributeValue.setColor(color.get());
					productAttributeValue.setSize(size.get());
				} else if (color.isPresent()) {
					productAttributeValue.setProduct(savedProduct.get());
					productAttributeValue.setColor(color.get());
				} else if (size.isPresent()) {
					productAttributeValue.setProduct(savedProduct.get());
					productAttributeValue.setSize(size.get());
				}

				productAttributeValueService.save(productAttributeValue);
			}
			
			// image
			imageService.uploadFile(files, defaultFileIndex, product);

		}
		log.info("Save product successful");
		return ResponseEntity.ok().body("{\"message\": \"Add product successfully\"}");
	}
	
	@PutMapping(value = "/update", consumes = "multipart/form-data", produces = { "application/json", "application/xml" })
	@Transactional
	@ResponseBody
	public ResponseEntity<?> updateProduct(@RequestParam(value = "file", required = false) MultipartFile[] files,
			@RequestParam int defaultFileIndex, @Valid @ModelAttribute ProductRequestDto productRequestDto,
			BindingResult bindingResult, HttpSession session) {
		log.info("Update product");
		Map<String, Object> errors = new HashMap<>();
		
		Optional<AttributeValue> color = attributeValueService
				.getAttributeValue(productRequestDto.getColorAttributeValue());
		Optional<AttributeValue> size = attributeValueService
				.getAttributeValue(productRequestDto.getSizeAttributeValue());

		if (bindingResult.hasErrors()) {
			log.error("Update product has error "+ bindingResult.getAllErrors());
			errors.put("bindingErrors", bindingResult.getAllErrors());
		}
		if (files == null) {
			log.error("Update product has error 'Please upload at least a file'");
			errors.put("fileErrors", "Please upload at least a file.");
		}
		Optional<Product> productByTitle = productService.getProductByTitle(productRequestDto.getTitle());

		if (productByTitle.isPresent() && !productByTitle.get().getId().equals(productRequestDto.getId())) {
			log.error("Update product has error 'Title already exists'");
			errors.put("titleDuplicate", "Title already exists! please enter a new title. ");
		}


		if (!errors.isEmpty()) {
			return ResponseEntity.badRequest().body(errors);
		}
		
		if(!productService.getProduct(productRequestDto.getId()).isPresent()) {
			errors.put("NotFoundProduct", "This product does not exist! Update failed.");
		}
		if (!errors.isEmpty()) {
			return ResponseEntity.badRequest().body(errors);
		}
		// product
		Product product = mapper.productRequestDtoToProduct(productRequestDto);
		product.setUpdateAt(LocalDate.now());
		productService.save(product);
		Optional<Product> savedProduct = productService.getProductByTitle(productRequestDto.getTitle());
		// product_attributValue

		if (savedProduct.isPresent()) {
			ProductAttributeValueResponseDto productAttributeValueResponseDto = productAttributeValueService.findProductAttributeValueByProduct(product);
			ProductAttributeValue productAttributeValue = new ProductAttributeValue();
			if(productAttributeValueResponseDto.getId()!=null) {
				productAttributeValue.setId(productAttributeValueResponseDto.getId());
			}
			if(color.isPresent() || size.isPresent()) {
				//if there are productAttributeValueResponseDto set id to update
				
				
				if (color.isPresent() && size.isPresent()) {
					
					productAttributeValue.setProduct(savedProduct.get());
					productAttributeValue.setColor(color.get());
					productAttributeValue.setSize(size.get());
				} else if (color.isPresent()) {
					
					productAttributeValue.setProduct(savedProduct.get());
					productAttributeValue.setColor(color.get());
				} else if (size.isPresent()) {
					
					productAttributeValue.setProduct(savedProduct.get());
					productAttributeValue.setSize(size.get());
				}

				productAttributeValueService.save(productAttributeValue);
			} 
			if(size.isEmpty() && color.isEmpty() && productAttributeValueResponseDto.getId()!=null){
				productAttributeValueService.deleteById(productAttributeValueResponseDto.getId());
			}
			
			
			// image
			imageService.deleteByProduct(product);
			imageService.uploadFile(files, defaultFileIndex, product);

		}
		log.info("Update product successful");
		return ResponseEntity.ok().body("{\"message\": \"Update product successfully\"}");
	}
	
	@DeleteMapping(value = "/delete")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> deleteProduct(@RequestParam("productId") Integer productId) {
		
		log.info("Delete product: "+productId);
		
		// check if product is exist
		Optional<Product> productById = productService.getProduct(productId);
		if (productById.isEmpty() || productById.get().getDeleteAt()!=null) {
			log.error("Delete product has error 'Product is not exist! Delete failse!'");
			return ResponseEntity.badRequest().body("Product is not exist! Delete failse!");
		}

		productService.delete(productById.get());
		
		log.info("Delete product: "+productId+" successful");
		return ResponseEntity.ok().body("A product deleted successfully.");
	}
	
	//get page product
	@GetMapping(value = "/getProductPage")
	@ResponseBody
	public ResponseEntity<ProductPageResponseDto> getProductPage(
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "3") int type,@RequestParam(defaultValue = "false") boolean search,@RequestParam(required = false,defaultValue = "0") Integer categoryId,@RequestParam(required = false) String keyword) {
		log.info("Get product page");
		Page<Product> productPage=null;
		Category category=null;
			
			if(categoryId!=0) {
				category= categoryService.getCategory(categoryId).get();
			}
			productPage = productService.searchProduct(page, size,type,category,keyword);

		
		List<ProductResponseDto> productResponseDtos = productPage.stream()
				.map(product -> new ProductResponseDto(product.getId(),
						product.getTitle(),product.getPrice(),product.getCurrentPrice()!=null? product.getCurrentPrice() : null
							,product.getQuantity(),orderDetailsService.getQuantityOfSold(product),product.isGender(),product.getDiscription(),
						product.getCreateAt(),product.getUpdateAt(),product.getCategory().getCategoryName()
						,productAttributeValueService.findProductAttributeValueByProduct(product)!=null ? productAttributeValueService.findProductAttributeValueByProduct(product):null,imageService.getImageByProduct(product)))
				.collect(Collectors.toList());

		ProductPageResponseDto productPageResponseDto = new ProductPageResponseDto(
				productPage.getTotalPages(), productPage.getNumber(), productPage.getSize(),
				productResponseDtos);
		log.info("Get product page successful");
		return ResponseEntity.ok(productPageResponseDto);

	}
	
	@GetMapping(value = "/getBestSeller")
	@ResponseBody
	public ResponseEntity<List<ProductResponseDto>> getBestSellerPage() {
		log.info("Get best seller");
		List<Product> products = productService.getAllProduct();
		List<ProductResponseDto> productResponseDtos =  new ArrayList<>();
		for (Product product : products) {
			productResponseDtos.add(mapper.productToProductResponese(product));
		}
		
		Collections.sort(productResponseDtos, new Comparator<ProductResponseDto>() {
            @Override
            public int compare(ProductResponseDto dto1, ProductResponseDto dto2) {
                return dto2.getQuantityOfSold() - dto1.getQuantityOfSold();
            }
        });
		List<ProductResponseDto> top30Products = productResponseDtos.subList(0, Math.min(productResponseDtos.size(), 30));

		log.info("Get product page successful");
		return ResponseEntity.ok(top30Products);

	}
	
	//get page product
	/*	@GetMapping(value = "/getProductPage")
		@ResponseBody
		public ResponseEntity<ProductPageResponseDto> getProductPage(
				@RequestParam(defaultValue = "30") int size, @RequestParam(defaultValue = "0") int page,@RequestParam int type,@RequestParam Integer categoryId,@RequestParam String key) {
			
			Page<Product> productPage = productService.getProductPage(page, size,type);
			
			
			
			List<ProductResponseDto> productResponseDtos = productPage.stream()
					.map(product -> new ProductResponseDto(product.getId(),
							product.getTitle(),product.getPrice(),product.getCurrentPrice()!=null? product.getCurrentPrice() : null
								,product.getQuantity(),orderDetailsService.getQuantityOfSold(product),product.isGender(),product.getDiscription(),
							product.getCreateAt(),product.getUpdateAt(),product.getCategory().getCategoryName()
							,productAttributeValueService.findProductAttributeValueByProduct(product)!=null ? productAttributeValueService.findProductAttributeValueByProduct(product):null,imageService.getImageByProduct(product)))
					.collect(Collectors.toList());

			ProductPageResponseDto productPageResponseDto = new ProductPageResponseDto(
					productPage.getTotalPages(), productPage.getNumber(), productPage.getSize(),
					productResponseDtos);

			return ResponseEntity.ok(productPageResponseDto);

		}*/
	
//	//view product details
//	@GetMapping(value="/edit")
//	public String editHome() {
//		
//		return "users/home";
//	}
	
	
	//view womenProduct
	@GetMapping(value="/women-fashion")
	public String getWomenFashion(HttpSession session,Model model) {
		log.info("Get women-fashion page");
		model.addAttribute("userName",(String) session.getAttribute("userName"));
		
		return "users/womenFashion";
	}
	
	@GetMapping(value="/best-seller")
	public String getBestSeller(HttpSession session,Model model) {
		log.info("Get best-seller page");
		model.addAttribute("userName",(String) session.getAttribute("userName"));
		
		return "users/bestSeller";
	}
	
	//view womenProduct
	@GetMapping(value="/men-fashion")
	public String getMenFashion(HttpSession session,Model model) {
		log.info("Get men-fashion page");
		model.addAttribute("userName",(String) session.getAttribute("userName"));
		return "users/menFashion";
	}
	//view womenProduct
	@GetMapping(value="/all-roduct")
	public String getAllProduct(HttpSession session,Model model) {
		log.info("Get all-roduct page");
		model.addAttribute("userName",(String) session.getAttribute("userName"));
		return "users/allProduct";
	}
	//view home user
	@GetMapping(value="/user-home")
	public String getHomeView(HttpSession session,Model model) {
		log.info("Get user-home page");
		model.addAttribute("userName",(String) session.getAttribute("userName"));
		model.addAttribute("cartRequestDto", new CartRequestDto());
		return "users/home";
	}
	
	//view product details
	@GetMapping(value="/details")
	public String getProductDetailsView(HttpSession session,@RequestParam Integer productId,Model model) {
		log.info("Get product details view");
		
		model.addAttribute("userName",(String) session.getAttribute("userName"));
		Optional<Product> product = productService.getProduct(productId);
		
		if (product.isEmpty()) {
			log.error("Get product details has error 'Product is not exist'");
			model.addAttribute("error", "Product is not exist!");
		}
		
		ProductResponseDto productResponse = mapper.productToProductResponese(product.get());
		model.addAttribute("productResponse", productResponse);

		model.addAttribute("cartRequestDto",new CartRequestDto());
		
		log.info("Get product details view successful");
		return "users/single-product";
	}
	
	
	@GetMapping(value = "/get")
	@ResponseBody
	public ResponseEntity<?> getProductById(@RequestParam Integer productId) {
		log.info("Get product");
		Optional<Product> product = productService.getProduct(productId);
		
		if (product.isEmpty()) {
			log.error("Get product has error 'Product is not exist'");
			return ResponseEntity.badRequest().body("Product is not exist!");
		}
		
		ProductResponseDto productResponse = mapper.productToProductResponese(product.get());
		
		log.info("Get product successful");
		return ResponseEntity.ok(productResponse);

	}
	
	//view search by key
	@GetMapping(value="/search")
	public String searchByKey(@RequestParam String key,HttpSession session,Model model) {
		log.info("Get search by key view");
		model.addAttribute("userName",(String) session.getAttribute("userName"));
		model.addAttribute("key", key);
		return "users/searchProduct";
	}
		
	//view search by key
	@GetMapping(value="/searchByCategory")
	public String searchByCategory(@RequestParam Integer category,HttpSession session,Model model) {
		log.info("Get search by category view");
		
		model.addAttribute("userName",(String) session.getAttribute("userName"));
		model.addAttribute("category", category);
		model.addAttribute("categoryName", categoryService.getCategory(category).get().getCategoryName());
		
		return "users/searchProductByCategory";
	}

}
