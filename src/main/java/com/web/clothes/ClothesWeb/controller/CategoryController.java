package com.web.clothes.ClothesWeb.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.clothes.ClothesWeb.dto.requestDto.CategoryRequestDto;
import com.web.clothes.ClothesWeb.dto.responseDto.CategoryPageResponseDto;
import com.web.clothes.ClothesWeb.dto.responseDto.CategoryResponseDto;
import com.web.clothes.ClothesWeb.entity.Category;
import com.web.clothes.ClothesWeb.service.CategoryService;
import com.web.clothes.ClothesWeb.service.UserService;

import lombok.RequiredArgsConstructor;
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/category")
public class CategoryController {
	
	private final CategoryService categoryService;
	private final UserService userService;
	private static Log log = LogFactory.getLog(CategoryController.class);
	
	// add attribute value
	@PostMapping(value = "/add")
	@ResponseBody
	public ResponseEntity<?> addCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto,
			BindingResult bindingResult) {
		log.info("Save cart with category: " + categoryRequestDto.getCategoryName());
		
		Map<String, Object> errors = new HashMap<>();
		
		// validate input data
		if (bindingResult.hasErrors()) {
			log.error("Save cart with category: " + categoryRequestDto.getCategoryName()+" has error "+ bindingResult.getAllErrors());
			errors.put("bindingErrors", bindingResult.getAllErrors());
		}
	
		// check if Attribute value name is exist
		Optional<Category> categoryByName = categoryService
				.findCategoryByName(categoryRequestDto.getCategoryName().trim());
		//if name is exits and it has deleted =0 && categoryByName.get().getDeleted()==0
		if (categoryByName.isPresent() ) {
			log.error("Save cart with category: " + categoryRequestDto.getCategoryName()+" has error 'name already exists'");
			errors.put("categoryExist", "Category already exists! Please enter a new one!");
		}

		if (!errors.isEmpty()) {
			return ResponseEntity.badRequest().body(errors);
		}

		// map attributeValueRequestDto to attributeValue
		Category category =new Category();
		category.setCategoryName(categoryRequestDto.getCategoryName());
		
		categoryService.save(category);
		String success = "A category added successfully.";
		
		log.info("Save cart with category: " + categoryRequestDto.getCategoryName()+" successful");
		return ResponseEntity.ok().body(success);
	}
	
	@PutMapping(value = "/update")
	@ResponseBody
	public ResponseEntity<?> updateCategory(@RequestParam Integer categoryId,
			@Valid @RequestBody CategoryRequestDto categoryRequestDto, BindingResult bindingResult) {
		log.info("Update cart with category: " + categoryRequestDto.getCategoryName());
		
		Map<String, Object> errors = new HashMap<>();
		// validate input data
		if (bindingResult.hasErrors()) {
			log.error("Update cart with category: " + categoryRequestDto.getCategoryName()+" has error "+ bindingResult.getAllErrors());
			
			errors.put("bindingErrors", bindingResult.getAllErrors());
			return ResponseEntity.badRequest().body(errors);
		}

		
		// check if Attribute value is exist
		Optional<Category> categoryById = categoryService.getCategory(categoryId);
		Optional<Category> categoryByName = categoryService
				.findCategoryByName(categoryRequestDto.getCategoryName().trim());
		
		if (categoryById.isEmpty()) {
			log.error("Update cart with category: " + categoryRequestDto.getCategoryName()+" has error 'category not exists'");
			
			errors.put("error", "Category is not exist! Update failse!");
			return ResponseEntity.badRequest().body(errors);

		} else if (categoryByName.isPresent() && !(categoryByName.get().getId()).equals(categoryId)) {
			log.error("Update cart with category: " + categoryRequestDto.getCategoryName()+" has error 'name already exists'");
			
			// check if Attribute value name is exist
			errors.put("error", "Category name already exists! Please enter a new one!");
			
			return ResponseEntity.badRequest().body(errors);
		}
		// map attributeValueRequestDto to attributeValue
		Category category =new Category();
		category.setCategoryName(categoryRequestDto.getCategoryName());
		category.setId(categoryId);
		
		categoryService.save(category);
		
		log.info("Update cart with category: " + categoryRequestDto.getCategoryName()+" successful");
		return ResponseEntity.ok().body("A category updated successfully.");
	}
	
	@DeleteMapping(value = "/delete")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> deleteCategory(@RequestParam("categoryId") Integer categoryId) {
		log.info("Delete cart with category: " + categoryId);
		
		// check if category is exist
		Optional<Category> categoryById = categoryService.getCategory(categoryId);
		if (categoryById.isEmpty() || categoryById.get().getDeleteAt()!=null) {
			log.info("Delete cart with category: " + categoryId +" has error 'not exist'");
			
			return ResponseEntity.badRequest().body("Category is not exist! Delete failse!");
		}
		
		categoryService.deleteCategory(categoryById.get());
		
		log.info("Delete cart with category: " + categoryId+" successful");
		return ResponseEntity.ok().body("A category deleted successfully.");
	}
	
	
	//return view category
	@GetMapping()
	public String getCategoryView(Model model) {
		log.info("Get category view ");
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("userName", userService.findUserByEmail(authentication.getName()).get().getUserName());
		return "admin/category";
	}
	
	//get page category
	@GetMapping(value = "/getCategoryPage")
	@ResponseBody
	public ResponseEntity<CategoryPageResponseDto> getCategoryPage(
			@RequestParam(defaultValue = "8") int size, @RequestParam(defaultValue = "0") int page, @RequestParam(required = false) String keyword) {
		log.info("Get category page value ");
		
		Page<Category> categoryPage=null;
		
		if(keyword.equals("")) {
			 categoryPage = categoryService.getAllCategory(page, size);
			
		}else {
			categoryPage = categoryService.getCategoryByKey(page, size,keyword);
			
		}
		
		
		List<CategoryResponseDto> categoryResponseDtos = categoryPage.stream()
				.map(category -> new CategoryResponseDto(category.getId(),
						category.getCategoryName()))
				.collect(Collectors.toList());
		
		CategoryPageResponseDto categoryPageResponseDto = new CategoryPageResponseDto(
				categoryPage.getTotalPages(), categoryPage.getNumber(), categoryPage.getSize(),
				categoryResponseDtos);
		
		log.info("Get category page value successful");
		return ResponseEntity.ok(categoryPageResponseDto);

	}
	
	//return view category
		@GetMapping(value = "/getAll")
		public ResponseEntity<?> getAllCategory() {
			log.info("Get category list ");
			List<Category> categories = categoryService.getAll();
			
			List<CategoryResponseDto> categoryResponseDtos = categories.stream()
					.map(category -> new CategoryResponseDto(category.getId(),
							category.getCategoryName()))
					.collect(Collectors.toList());
			
			log.info("Get category list successful");
			return ResponseEntity.ok(categoryResponseDtos);
		}

}
