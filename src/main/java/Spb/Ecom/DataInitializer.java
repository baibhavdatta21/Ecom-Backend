package Spb.Ecom;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import Spb.Ecom.models.Cart;
import Spb.Ecom.models.CartProducts;
import Spb.Ecom.models.Category;
import Spb.Ecom.models.Product;
import Spb.Ecom.models.Role;
import Spb.Ecom.models.User;
import Spb.Ecom.repository.CartProductsRepository;
import Spb.Ecom.repository.CartRepository;
import Spb.Ecom.repository.CategoryRepository;
import Spb.Ecom.repository.ProductRepository;
import Spb.Ecom.repository.RoleRepository;
import Spb.Ecom.repository.UserRepository;


@Component
public class DataInitializer implements CommandLineRunner {
	
	@Autowired 
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	CartRepository cartRepository;
	@Autowired
	CartProductsRepository cartProductsRepository;
	
	@Autowired 
	PasswordEncoder passwordEncoder;
	@Override
	public void run(String... args) throws Exception {
		Category category1=new Category("Fashion");
		Category category2=new Category("Electronics");
		Category category3=new Category("Books");
		Category category4=new Category("Groceries");
		Category category5=new Category("Medicines");
		categoryRepository.save(category1);
		categoryRepository.save(category2);
		categoryRepository.save(category3);
		categoryRepository.save(category4);
		categoryRepository.save(category5);
		
		Role role1=new Role("Consumer");
		Role role2=new Role("Seller");
		roleRepository.save(role1);
		roleRepository.save(role2);
		
		List<Role> lst_consumer=new ArrayList<Role>();
		lst_consumer.add(role1);
		List<Role> lst_seller=new ArrayList<Role>();
		lst_seller.add(role2);
		List<Role> lst_all=new ArrayList<Role>();
		lst_all.add(role1);
		lst_all.add(role2);
		
		Cart cart1=new Cart();
		Cart cart2=new Cart();
		Cart cart3=new Cart();
		User user1= new User("A",passwordEncoder.encode("123"),lst_consumer);
		User user2= new User("B",passwordEncoder.encode("123"),lst_seller);
		User user3= new User("C",passwordEncoder.encode("123"),lst_all);
		cart1.setUser(user1);
		cart2.setUser(user2);
		cart3.setUser(user3);
		cart1.setTotalAmount(20.0);
		user1.setCart(cart1);
		user2.setCart(cart2);
		user3.setCart(cart3);
		userRepository.save(user1);
		userRepository.save(user2);
		userRepository.save(user3);
		
		Product product1= new Product("Apple iPad 10.2 8th Gen WiFi iOS Tablet",29190.00,category2,user3);
		Product product2=new Product("Crocin pain relief tablet",10.0,category5,user3);
		productRepository.save(product1);
		productRepository.save(product2);
		
		
		CartProducts cartProducts1=new CartProducts(2,cart1,product2);
		cart1.setCartProducts(List.of(cartProducts1));
		cartProductsRepository.save(cartProducts1);

		//added
//		productRepository.save(new Product("Apple iPad 10.2 8th Gen WiFi iOS Tablet", 29190.00, category2, user3));
//		productRepository.save(new Product("Samsung Galaxy Tab A8", 17999.00, category2, user3));
//		productRepository.save(new Product("Lenovo Tab M10 HD", 14999.00, category2, user3));
//		productRepository.save(new Product("Apple iPhone 13", 59999.00, category2, user3));
//		productRepository.save(new Product("Samsung Galaxy S21 FE", 42999.00, category2, user3));
//		productRepository.save(new Product("OnePlus Nord CE 2", 24999.00, category2, user3));
//		productRepository.save(new Product("Redmi Note 12 Pro", 23999.00, category2, user3));
//		productRepository.save(new Product("Realme Narzo 50", 12999.00, category2, user3));
//		productRepository.save(new Product("Sony WH-1000XM4 Headphones", 24990.00, category2, user3));
//		productRepository.save(new Product("Boat Rockerz 450 Headphones", 1499.00, category2, user3));
//		productRepository.save(new Product("Dell Inspiron 15 Laptop", 55999.00, category2, user3));
//		productRepository.save(new Product("HP Pavilion Gaming Laptop", 68999.00, category2, user3));
//		productRepository.save(new Product("Logitech Wireless Mouse", 899.00, category2, user3));
//		productRepository.save(new Product("Apple Magic Keyboard", 9500.00, category2, user3));
//		productRepository.save(new Product("Samsung 27 inch LED Monitor", 16999.00, category2, user3));
//		productRepository.save(new Product("LG 43 inch Smart LED TV", 31999.00, category2, user3));
//		productRepository.save(new Product("Mi Smart Band 6", 3499.00, category2, user3));
//		productRepository.save(new Product("Noise ColorFit Smartwatch", 2999.00, category2, user3));
//		productRepository.save(new Product("Canon EOS 1500D DSLR Camera", 36999.00, category2, user3));
//		productRepository.save(new Product("Sony Alpha ILCE-6100 Camera", 64999.00, category2, user3));
//		productRepository.save(new Product("Crocin pain relief tablet", 10.00, category5, user3));
//		productRepository.save(new Product("Paracetamol 650 Tablet", 12.00, category5, user3));
//		productRepository.save(new Product("Vicks Action 500 Tablet", 15.00, category5, user3));
//		productRepository.save(new Product("Dolo 650 Tablet", 30.00, category5, user3));
//		productRepository.save(new Product("Benadryl Cough Syrup", 120.00, category5, user3));
//		productRepository.save(new Product("Zincovit Multivitamin Tablet", 105.00, category5, user3));
//		productRepository.save(new Product("Evion 400 Capsule", 35.00, category5, user3));
//		productRepository.save(new Product("Revital H Multivitamin Capsule", 399.00, category5, user3));
//		productRepository.save(new Product("Digene Antacid Liquid", 95.00, category5, user3));
//		productRepository.save(new Product("ORS Electrolyte Powder", 25.00, category5, user3));
//		productRepository.save(new Product("Shelcal 500 Calcium Tablet", 130.00, category5, user3));
//		productRepository.save(new Product("Liv 52 Syrup", 145.00, category5, user3));
//		productRepository.save(new Product("Cetirizine Tablet", 18.00, category5, user3));
//		productRepository.save(new Product("Azithromycin 500 Tablet", 85.00, category5, user3));
//		productRepository.save(new Product("Vitamin C Chewable Tablet", 60.00, category5, user3));
//		productRepository.save(new Product("Pantoprazole Tablet", 40.00, category5, user3));
//		productRepository.save(new Product("Omez Capsule", 72.00, category5, user3));
//		productRepository.save(new Product("Glucon-D Instant Energy Drink", 110.00, category5, user3));
//		productRepository.save(new Product("Supradyn Daily Multivitamin", 125.00, category5, user3));
//		productRepository.save(new Product("Electral Powder", 28.00, category5, user3));
//		productRepository.save(new Product("Prestige Pressure Cooker 5L", 2599.00, category3, user3));
//		productRepository.save(new Product("Pigeon Induction Cooktop", 1899.00, category3, user3));
//		productRepository.save(new Product("Philips Mixer Grinder", 3499.00, category3, user3));
//		productRepository.save(new Product("LG Double Door Refrigerator", 32999.00, category3, user3));
//		productRepository.save(new Product("Samsung Washing Machine 6.5kg", 18499.00, category3, user3));
//		productRepository.save(new Product("Kent RO Water Purifier", 15999.00, category3, user3));
//		productRepository.save(new Product("Havells Ceiling Fan", 2499.00, category3, user3));
//		productRepository.save(new Product("Bajaj Electric Iron", 1299.00, category3, user3));
//		productRepository.save(new Product("Prestige Non Stick Cookware Set", 2999.00, category3, user3));
//		productRepository.save(new Product("Milton Thermosteel Water Bottle", 899.00, category3, user3));
//		productRepository.save(new Product("Cricket Bat", 700.00, category4, user3));
//		productRepository.save(new Product("Football", 499.00, category4, user3));
//		productRepository.save(new Product("Cosco Volleyball", 450.00, category4, user3));
//		productRepository.save(new Product("Yonex Badminton Racket", 1299.00, category4, user3));
//		productRepository.save(new Product("Nivia Football Shoes", 2199.00, category4, user3));
//		productRepository.save(new Product("Puma Running Shoes", 3499.00, category4, user3));
//		productRepository.save(new Product("Yoga Mat Anti Slip", 699.00, category4, user3));
//		productRepository.save(new Product("Dumbbell Set 10kg", 1499.00, category4, user3));
//		productRepository.save(new Product("Resistance Band Set", 499.00, category4, user3));
//		productRepository.save(new Product("Skipping Rope Fitness", 299.00, category4, user3));

	}

}
