package Spb.Ecom.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Spb.Ecom.models.CartProducts;
import Spb.Ecom.models.Product;
import Spb.Ecom.security.JwtUtil;
import Spb.Ecom.service.AuthService;
import Spb.Ecom.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	AuthService authService;
	
	@Autowired
	JwtUtil jwtutil;
	
	@GetMapping("/consumer/cart")
    public ResponseEntity<?> getCart(@RequestHeader("Authorization") String authorization){
		String token=authorization.substring(7);

		return authService.getCart(jwtutil.extractUserName(token));
	}
	@PostMapping("/consumer/cart")
    public ResponseEntity<?> postProduct(@RequestHeader("Authorization") String authorization, @RequestBody Product p ){
		String token=authorization.substring(7);
		return authService.postProduct(jwtutil.extractUserName(token),p);
	}
	@PutMapping("/consumer/cart")
    public ResponseEntity<?> postProduct(@RequestHeader("Authorization") String authorization, @RequestBody CartProducts p ){
		String token=authorization.substring(7);
		return authService.putProduct(jwtutil.extractUserName(token),p);
	}
	@DeleteMapping("/consumer/cart")
    public ResponseEntity<?> delProduct(@RequestHeader("Authorization") String authorization,  @RequestBody Product p  ){
		String token=authorization.substring(7);
		return authService.delProduct(jwtutil.extractUserName(token),p);
	}
	
	@GetMapping("/seller/product")
	public ResponseEntity<?> sellerProducts(@RequestHeader("Authorization") String authorization){
		String token=authorization.substring(7);
		return authService.sellerProducts(jwtutil.extractUserName(token));
	}
	@GetMapping("/seller/product/{productId}")
	public ResponseEntity<?> productProducts(@PathVariable("productId") Integer productId){
		return authService.productProducts(productId);
	}
	@GetMapping("/seller/product/out")
	public ResponseEntity<?> productProducts(){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error");
	}
	@PostMapping("/seller/product")
	public void postProduct(@RequestBody Product product, HttpServletRequest request,HttpServletResponse response) throws IOException{
		String auth=request.getHeader("Authorization");
		String username=null;
		String token=null;
		token=auth.substring(7);
		username=jwtutil.extractUserName(token);
		System.out.println("Ok inside controlller");
		authService.postProduct(product,username);

		if(product.getProductId()==null){
			response.sendRedirect("/api/auth/seller/product/out");
			return;
		}

		Integer productId=	product.getProductId();
		response.sendRedirect("/api/auth/seller/product/" + productId);
	}
	
	@PutMapping("/seller/product")
	public ResponseEntity<?> putProduct(@RequestBody Product product) {
		return authService.putProduct(product);
	}
	@DeleteMapping("/seller/product")
	public ResponseEntity<?> delProduct(@RequestBody Product product) {
		return authService.delProduct(product);
	}
}
