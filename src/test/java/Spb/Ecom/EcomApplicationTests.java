package Spb.Ecom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
//import org.json.parser.JSONParser;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
class EcomApplicationTests {

	@Autowired
	MockMvc mvc;

	@Test
	@Order(1)
	public void productSearchStatus()throws Exception{
		//To check the product returned is not null
		long startTime = System.currentTimeMillis();
		mvc.perform(get("/api/public/product/search").param("name","tablet"))
				.andExpect(status().is(200)).andExpect(jsonPath("$", notNullValue()));
		long endTime = System.currentTimeMillis();
		System.out.println("productSearchStatus execution time: "
				+ (endTime - startTime) + " ms");
	}
	@Test
	@Order(2)
	public void productSearchWithoutKeyword() throws Exception {
		//No parameters passed hence expecting 400 Bad Request
		mvc.perform(get("/api/public/product/search")).andExpect(status().is(400));
	}
	@Test
	@Order(3)
	public void productSearchWithProductName() throws Exception {
		//seraching based on productName and expecting 200
		mvc.perform(get("/api/public/product/search")
						.param("name", "tablet"))
				.andExpect(status().is(200))
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[*].productName",
						everyItem(containsStringIgnoringCase("tablet"))));

	}

	@Test
	@Order(4)
	public void productSearchWithCategoryName() throws Exception {
		//seraching based on categoryName and expecting 200
		mvc.perform(get("/api/public/product/search")
						.param("name", "Medicine"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[*].category.categoryName",
						everyItem(containsStringIgnoringCase("medicine"))));
	}
	@Test
	@Order(5)
	public void productSearchEmptyKeyword() throws Exception {
		mvc.perform(get("/api/public/product/search")
						.param("name", ""))
				.andExpect(status().is(200))
				.andExpect(jsonPath("$", hasSize(2)));
	}
	@Test
	@Order(6)
	public void productSearchSpecialChars() throws Exception {
		mvc.perform(get("/api/public/product/search")
						.param("name", "@@@@"))
				.andExpect(status().is(404));
	}
	@Test
	@Order(7)
	public void SignUpFailure() throws Exception {
		//when no body is passed expecting status 400
		mvc.perform(post("/api/public/signup")).andExpect(status().is(400));
	}
	@Test
	@Order(8)
	public void signUpMissingPassword() throws Exception {
		Map<String, Object> body = new HashMap<>();
		body.put("userName", "X");

		mvc.perform(post("/api/public/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(body)))
				.andExpect(status().isBadRequest());
	}
	@Test
	@Order(9)
	public void signUpInvalidRole() throws Exception {
		Map<String, Object> body = new HashMap<>();
		body.put("userName", "Y");
		body.put("userPassword", "123");
		body.put("role", List.of(Map.of("role","Admin")));

		mvc.perform(post("/api/public/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(body)))
				.andExpect(status().isBadRequest());
	}
	@Test
	@Order(10)
	public void loginFailure() throws Exception {
		//failure of login expected status 401
		Map<String, String> body = new HashMap<>();
		body.put("userName", "D");
		body.put("userPassword", "123");
		//when no body is passed expecting status 400
		mvc.perform(post("/api/public/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(body))).andExpect(status().is(401));
	}
	@Test
	@Order(11)
	public void loginFailureWrongPassword() throws Exception {
		//failure of login expected status 401
		Map<String, String> body = new HashMap<>();
		body.put("userName", "A");
		body.put("userPassword", "12");
		//when no body is passed expecting status 400
		mvc.perform(post("/api/public/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(body))).andExpect(status().is(401));
	}
	@Test
	@Order(12)
	public void SignUpFailureUserAlreadyPresent() throws Exception {
		//Sign Up failure as the id already exits expected status 409
		Map<String, String> body = new HashMap<>();
		body.put("userName", "A");
		body.put("userPassword", "123");
		//when no body is passed expecting status 400
		mvc.perform(post("/api/public/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(body))).andExpect(status().is(409));
	}
	@Test
	@Order(13)
	public void SignUpSuccess() throws Exception {
		//Successful signUp expected status 201
		Map<String, Object> body = new HashMap<>();
		Map<String,String> r=new HashMap<>();
		r.put("role","Seller");
		body.put("userName", "D");
		body.put("userPassword", "123");
		body.put("role", List.of(r));
		//when no body is passed expecting status 400
		mvc.perform(post("/api/public/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(body))).andExpect(status().is(201));
	}
	@Test
	@Order(14)
	public void loginSuccess() throws Exception {
		//Successful login with status 200
		Map<String, String> body = new HashMap<>();
		body.put("userName", "D");
		body.put("userPassword", "123");
		//when no body is passed expecting status 400
		mvc.perform(post("/api/public/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(body))).andExpect(status().is(200));
	}
	@Test
	@Order(15)
	public void consumerGetCartFailure() throws Exception {
		mvc.perform(get("/api/auth/consumer/cart")).andExpect(status().is(401));
	}
	public String getJSONCreds(String u, String p) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<>();
		map.put("userName", u);
		map.put("userPassword", p);
		System.out.println(map);
		System.out.println(mapper.writeValueAsString(map));
		return mapper.writeValueAsString(map);
	}
	public MockHttpServletResponse loginHelper(String u, String p) throws Exception {
		return mvc
				.perform(post("/api/public/login")
						.contentType(MediaType.APPLICATION_JSON)
				.content(getJSONCreds(u, p)))
				.andReturn().getResponse();
	}
	@Test
	@Order(16)
	public void accessWithInvalidToken() throws Exception {
		mvc.perform(get("/api/auth/consumer/cart")
						.header("Authorization",""))
				.andExpect(status().isUnauthorized());
	}
	@Test
	@Order(17)
	public void accessWithoutBearerPrefix() throws Exception {
		mvc.perform(get("/api/auth/consumer/cart")
						.header("Authorization", loginHelper("A","123").getContentAsString()))
				.andExpect(status().isUnauthorized());
	}
	@Test
	@Order(18)
	public void consumerGetCartSuccess() throws Exception {

		mvc.perform(get("/api/auth/consumer/cart")
				.header("Authorization","Bearer "+loginHelper("A","123").getContentAsString()))
						.andExpect(status().is(200))
				.andExpect(jsonPath("$.cartId", is(not(equalTo("")))))
				.andExpect(jsonPath("$.cartProducts[0].quantity", is(2)))
				.andExpect(jsonPath("$.cartProducts[0].product.productName",
						containsStringIgnoringCase("Crocin pain relief tablet")))
				.andExpect(jsonPath("$.cartProducts[0].product.category.categoryName", is("Medicines")));
	}
	@Test
	@Order(19)
	public void sellerOnConsumerEndPtFailure() throws Exception {

		mvc.perform(get("/api/auth/consumer/cart")
						.header("Authorization","Bearer "+loginHelper("B","123").getContentAsString()))
				.andExpect(status().is(403));
	}
	@Test
	@Order(20)
	public void sellerGetProductSuccess() throws Exception {

		mvc.perform(get("/api/auth/seller/product")
						.header("Authorization","Bearer "+loginHelper("B","123").getContentAsString()))
				.andExpect(status().is(200));
	}
	@Test
	@Order(21)
	public void addProductNegativePrice() throws Exception {
		mvc.perform(post("/api/auth/seller/product")
						.header("Authorization","Bearer "+loginHelper("C","123").getContentAsString())
						.contentType(MediaType.APPLICATION_JSON)
						.content(newProduct("Bad Product",-100.0,"Sports")))
				.andExpect(status().is(302)).andExpect(header().string("Location", "/api/auth/seller/product/out"));
	}
	@Test
	@Order(22)
	public void sellerGetProductSuccess2() throws Exception {

		mvc.perform(get("/api/auth/seller/product")
						.header("Authorization","Bearer "+loginHelper("C","123").getContentAsString()))
				.andExpect(status().is(200))
				.andExpect(jsonPath("$", hasSize(2)));
	}
	public String newProduct(String pn, Double p,String cn) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<>();
		map.put("productName", pn);
		map.put("price", p);
		Map<String,String>mp2=new HashMap<>();
		mp2.put("categoryName",cn);
		map.put("category",mp2);
		System.out.println(mapper.writeValueAsString(map));
		return mapper.writeValueAsString(map);
	}
	@Test
	@Order(23)
	public void postProductSuccess() throws Exception {

		mvc.perform(post("/api/auth/seller/product")
						.header("Authorization",
								"Bearer " + loginHelper("C","123").getContentAsString())
						.contentType(MediaType.APPLICATION_JSON)
						.content(newProduct("Cricket Bat",700.0,"Sports")))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("/api/auth/seller/product/*"));
	}
	public String newProduct(Integer id) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<>();
		map.put("productId", id);
		System.out.println(mapper.writeValueAsString(map));
		return mapper.writeValueAsString(map);
	}
	@Test
	@Order(24)
	public void addInvalidProductToCart() throws Exception {
		mvc.perform(post("/api/auth/consumer/cart")
						.header("Authorization","Bearer "+loginHelper("A","123").getContentAsString())
						.contentType(MediaType.APPLICATION_JSON)
						.content(newProduct(9999)))
				.andExpect(status().isNotFound());
	}
	@Test
	@Order(25)
	public void postProductOnCartSuccess() throws Exception {

		mvc.perform(post("/api/auth/consumer/cart")
						.header("Authorization",
								"Bearer " + loginHelper("A","123").getContentAsString())
						.contentType(MediaType.APPLICATION_JSON)
						.content(newProduct(3)))
				.andExpect(status().is(200))
				.andExpect(jsonPath("$.product.productName", is("Cricket Bat")))
				.andExpect(jsonPath("$.product.category.categoryName",is("Sports")));
	}
	public String newProduct(Integer id,String productName) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<>();
		map.put("productId", id);
		map.put("productName",productName);
		System.out.println(mapper.writeValueAsString(map));
		return mapper.writeValueAsString(map);
	}
	@Test
	@Order(26)
	public void updateNonExistingProduct() throws Exception {
		mvc.perform(put("/api/auth/seller/product")
						.header("Authorization","Bearer "+loginHelper("C","123").getContentAsString())
						.contentType(MediaType.APPLICATION_JSON)
						.content(newProduct(9999,"Ghost")))
				.andExpect(status().isNotFound());
	}
	@Test
	@Order(27)
	public void putProductSuccess() throws Exception {

		mvc.perform(put("/api/auth/seller/product")
						.header("Authorization",
								"Bearer " + loginHelper("C","123").getContentAsString())
						.contentType(MediaType.APPLICATION_JSON)
						.content(newProduct(3,"Football")))
				.andExpect(status().is(200))
				.andExpect(jsonPath("$.productName", is("Football")))
				.andExpect(jsonPath("$.category.categoryName",is("Sports")));
	}
	public String newProduct(Integer id,Integer quantity) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<>();
		map.put("cpId", id);
		map.put("quantity",quantity);
		System.out.println(mapper.writeValueAsString(map));
		return mapper.writeValueAsString(map);
	}
	@Test
	@Order(28)
	public void putProductOnCartSuccess() throws Exception {

		mvc.perform(put("/api/auth/consumer/cart")
						.header("Authorization",
								"Bearer " + loginHelper("A","123").getContentAsString())
						.contentType(MediaType.APPLICATION_JSON)
						.content(newProduct(2,2)))
				.andExpect(status().is(200))
				.andExpect(jsonPath("$.cpId", is(2)))
				.andExpect(jsonPath("$.quantity",is(2)));
	}
	@Test
	@Order(30)
	public void DelProductSuccess() throws Exception {

		mvc.perform(delete("/api/auth/seller/product")
						.header("Authorization",
								"Bearer " + loginHelper("C","123").getContentAsString())
						.contentType(MediaType.APPLICATION_JSON)
						.content(newProduct(3)))
				.andExpect(status().is(204));
	}
	@Test
	@Order(29)
	public void DelProductOnCartSuccess() throws Exception {

		mvc.perform(delete("/api/auth/consumer/cart")
						.header("Authorization",
								"Bearer " + loginHelper("A","123").getContentAsString())
						.contentType(MediaType.APPLICATION_JSON)
						.content(newProduct(3)))
				.andExpect(status().is(204));
	}
}
