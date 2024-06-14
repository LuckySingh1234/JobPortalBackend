package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;

import java.util.List;

@Path("myresource")
public class MyResource {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("adminLogin")
    public Response adminLogin(String userData) {
        JSONObject json = new JSONObject(userData);
        String email = json.getString("email");
        String password = json.getString("password");

        Admin signedInAdmin = Admin.login(email, password);
        JSONObject responseJson = new JSONObject();
        if (signedInAdmin != null) {
            responseJson.put("signedInAdmin", signedInAdmin.getEmail());
        }
        return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("userLogin")
    public Response userLogin(String userData) {
        JSONObject json = new JSONObject(userData);
        String email = json.getString("email");
        String password = json.getString("password");

        User signedInUser = User.login(email, password);
        JSONObject responseJson = new JSONObject();
        if (signedInUser != null) {
            Gson gson = new Gson();
            JsonElement usersJson = gson.toJsonTree(signedInUser);
            return Response.ok(usersJson.toString(), MediaType.APPLICATION_JSON).build();
        }
        return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("addUser")
    public Response addUser(String reqData) {
        JSONObject json = new JSONObject(reqData);
        String firstName = json.getString("firstName");
        String lastName = json.getString("lastName");
        String mobile = json.getString("mobile");
        String email = json.getString("email");
        String password = json.getString("password");
        String interests = json.getString("interests");
        String userAddedResponse = User.addUser(firstName, lastName, mobile, email, password, interests);
        JSONObject responseJson = new JSONObject();
        if (userAddedResponse.equals("true")) {
            responseJson.put("success", "true");
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
        } else {
            responseJson.put("failure", "true");
            responseJson.put("errorMessage", userAddedResponse);
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("getAllCompanies")
    public Response getAllCompanies() {
        List<Company> allCompanies = Company.fetchAllCompanies();
        Gson gson = new Gson();
        JsonElement companyJson = gson.toJsonTree(allCompanies);
        return Response.ok(companyJson.toString(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("getCompanyById")
    public Response getCompanyById(@QueryParam("id") String companyId) {
        Company company = Company.fetchCompanyById(companyId);
        Gson gson = new Gson();
        JsonElement companyJson = gson.toJsonTree(company);
        return Response.ok(companyJson.toString(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("getAllJobsByCompanyId")
    public Response getAllJobsByCompanyId(@QueryParam("id") String companyId) {
        List<Job> allJobs = Job.fetchAllJobByCompanyId(companyId);
        Gson gson = new Gson();
        JsonElement jobsJson = gson.toJsonTree(allJobs);
        return Response.ok(jobsJson.toString(), MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("applyJob")
    public Response applyJob(String reqData) {
        JSONObject json = new JSONObject(reqData);
        String jobId = json.getString("jobId");
        String userId = json.getString("userId");
        String userAddedResponse = JobApplication.applyJob(jobId, userId);
        JSONObject responseJson = new JSONObject();
        if (userAddedResponse.equals("true")) {
            responseJson.put("success", "true");
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
        } else {
            responseJson.put("failure", "true");
            responseJson.put("errorMessage", userAddedResponse);
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
        }
    }








    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("getProductsByCategory")
    public Response fetchProductsByCategory(@QueryParam("category") String category) {
        List<Product> products = Product.fetchProductsByCategory(category);
        Gson gson = new Gson();
        JsonElement productsJson = gson.toJsonTree(products);
        return Response.ok(productsJson.toString(), MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("getProductById")
    public Response fetchProductById(String reqData) {
        JSONObject json = new JSONObject(reqData);
        String productId = json.getString("productId");
        Product product = Product.fetchProductById(productId);
        Gson gson = new Gson();
        JsonElement productJsonElement = gson.toJsonTree(product);
        if (productJsonElement.isJsonObject()) {
            JsonObject productJsonObject = productJsonElement.getAsJsonObject();
            productJsonObject.addProperty("success", "true");
            return Response.ok(gson.toJson(productJsonElement), MediaType.APPLICATION_JSON).build();
        } else {
            JsonObject resp = new JsonObject();
            resp.addProperty("error", "true");
            resp.addProperty("errorMessage", "Couldn't fetch product");
            return Response.ok(gson.toJson(resp), MediaType.APPLICATION_JSON).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("addProduct")
    public Response addProduct(String reqData) {
        JSONObject json = new JSONObject(reqData);
        String name = json.getString("name");
        String price = json.getString("price");
        String stockQuantity = json.getString("stockQuantity");
        String category = json.getString("category");
        String imageUrl = json.getString("imageUrl");
        String productAddedResponse = Product.addProduct(name, price, stockQuantity, category, imageUrl);
        JSONObject responseJson = new JSONObject();
        if (productAddedResponse.equals("true")) {
            responseJson.put("success", "true");
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
        } else {
            responseJson.put("failure", "true");
            responseJson.put("errorMessage", productAddedResponse);
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("editProduct")
    public Response editProduct(String reqData) {
        JSONObject json = new JSONObject(reqData);
        String productId = json.getString("productId");
        String name = json.getString("name");
        String price = json.getString("price");
        String stockQuantity = json.getString("stockQuantity");
        String category = json.getString("category");
        String imageUrl = json.getString("imageUrl");
        String productEditedResponse = Product.editProduct(productId, name, price, stockQuantity, category, imageUrl);
        JSONObject responseJson = new JSONObject();
        if (productEditedResponse.equals("true")) {
            responseJson.put("success", "true");
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
        } else {
            responseJson.put("failure", "true");
            responseJson.put("errorMessage", productEditedResponse);
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("removeProduct")
    public Response removeProduct(String reqData) {
        JSONObject json = new JSONObject(reqData);
        String productId = json.getString("productId");
        String productRemovedResponse = Product.removeProduct(productId);
        JSONObject responseJson = new JSONObject();
        if (productRemovedResponse.equals("true")) {
            responseJson.put("success", "true");
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
        } else {
            responseJson.put("failure", "true");
            responseJson.put("errorMessage", productRemovedResponse);
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("getCustomerById")
    public Response fetchCustomerById(String reqData) {
        JSONObject json = new JSONObject(reqData);
        String customerId = json.getString("customerId");
        User user = User.fetchCustomerById(customerId);
        Gson gson = new Gson();
        JsonElement customerJsonElement = gson.toJsonTree(user);
        if (customerJsonElement.isJsonObject()) {
            JsonObject customerJsonObject = customerJsonElement.getAsJsonObject();
            customerJsonObject.addProperty("success", "true");
            return Response.ok(gson.toJson(customerJsonElement), MediaType.APPLICATION_JSON).build();
        } else {
            JsonObject resp = new JsonObject();
            resp.addProperty("error", "true");
            resp.addProperty("errorMessage", "Couldn't fetch customer");
            return Response.ok(gson.toJson(resp), MediaType.APPLICATION_JSON).build();
        }
    }

//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Path("editCustomer")
//    public Response editCustomer(String reqData) {
//        JSONObject json = new JSONObject(reqData);
//        String customerId = json.getString("customerId");
//        String fullName = json.getString("fullName");
//        String mobile = json.getString("mobile");
//        String email = json.getString("email");
//        String password = json.getString("password");
//        String address = json.getString("address");
//        String customerEditedResponse = User.editCustomer(customerId, fullName, mobile, email, password, address);
//        JSONObject responseJson = new JSONObject();
//        if (customerEditedResponse.equals("true")) {
//            responseJson.put("success", "true");
//            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
//        } else {
//            responseJson.put("failure", "true");
//            responseJson.put("errorMessage", customerEditedResponse);
//            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
//        }
//    }

//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Path("removeCustomer")
//    public Response removeCustomer(String reqData) {
//        JSONObject json = new JSONObject(reqData);
//        String customerId = json.getString("customerId");
//        String customerRemovedResponse = User.removeCustomer(customerId);
//        JSONObject responseJson = new JSONObject();
//        if (customerRemovedResponse.equals("true")) {
//            responseJson.put("success", "true");
//            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
//        } else {
//            responseJson.put("failure", "true");
//            responseJson.put("errorMessage", customerRemovedResponse);
//            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
//        }
//    }
}
