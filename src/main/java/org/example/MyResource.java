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
    @Path("managerLogin")
    public Response managerLogin(String userData) {
        JSONObject json = new JSONObject(userData);
        String email = json.getString("email");
        String password = json.getString("password");

        User user = new User(email, password);
        Manager signedInManager = Manager.login(user);
        JSONObject responseJson = new JSONObject();
        if (signedInManager != null) {
            responseJson.put("signedInManager", signedInManager.getEmail());
        }
        return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("getManagerActions")
    public Response fetchManagerActions(String reqData) {
        JSONObject json = new JSONObject(reqData);
        String email = json.getString("email");

        String managerActions = Manager.fetchManagerActions(email);
        JSONObject responseJson = new JSONObject();
        if (managerActions != null) {
            responseJson.put("managerActions", managerActions);
        }
        return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
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
    @Path("customerLogin")
    public Response customerLogin(String userData) {
        JSONObject json = new JSONObject(userData);
        String email = json.getString("email");
        String password = json.getString("password");

        User user = new User(email, password);
        Customer signedInCustomer = Customer.login(user);
        JSONObject responseJson = new JSONObject();
        if (signedInCustomer != null) {
            Gson gson = new Gson();
            JsonElement customersJson = gson.toJsonTree(signedInCustomer);
            return Response.ok(customersJson.toString(), MediaType.APPLICATION_JSON).build();
        }
        return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("getCustomers")
    public Response fetchCustomers() {
        List<Customer> customers = Customer.fetchCustomers();
        Gson gson = new Gson();
        JsonElement customersJson = gson.toJsonTree(customers);
        return Response.ok(customersJson.toString(), MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("getCustomerById")
    public Response fetchCustomerById(String reqData) {
        JSONObject json = new JSONObject(reqData);
        String customerId = json.getString("customerId");
        Customer customer = Customer.fetchCustomerById(customerId);
        Gson gson = new Gson();
        JsonElement customerJsonElement = gson.toJsonTree(customer);
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

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("addCustomer")
    public Response addCustomer(String reqData) {
        JSONObject json = new JSONObject(reqData);
        String fullName = json.getString("fullName");
        String mobile = json.getString("mobile");
        String email = json.getString("email");
        String password = json.getString("password");
        String address = json.getString("address");
        String customerAddedResponse = Customer.addCustomer(fullName, mobile, email, password, address);
        JSONObject responseJson = new JSONObject();
        if (customerAddedResponse.equals("true")) {
            responseJson.put("success", "true");
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
        } else {
            responseJson.put("failure", "true");
            responseJson.put("errorMessage", customerAddedResponse);
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("editCustomer")
    public Response editCustomer(String reqData) {
        JSONObject json = new JSONObject(reqData);
        String customerId = json.getString("customerId");
        String fullName = json.getString("fullName");
        String mobile = json.getString("mobile");
        String email = json.getString("email");
        String password = json.getString("password");
        String address = json.getString("address");
        String customerEditedResponse = Customer.editCustomer(customerId, fullName, mobile, email, password, address);
        JSONObject responseJson = new JSONObject();
        if (customerEditedResponse.equals("true")) {
            responseJson.put("success", "true");
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
        } else {
            responseJson.put("failure", "true");
            responseJson.put("errorMessage", customerEditedResponse);
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("removeCustomer")
    public Response removeCustomer(String reqData) {
        JSONObject json = new JSONObject(reqData);
        String customerId = json.getString("customerId");
        String customerRemovedResponse = Customer.removeCustomer(customerId);
        JSONObject responseJson = new JSONObject();
        if (customerRemovedResponse.equals("true")) {
            responseJson.put("success", "true");
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
        } else {
            responseJson.put("failure", "true");
            responseJson.put("errorMessage", customerRemovedResponse);
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("getManagers")
    public Response fetchManagers() {
        List<Manager> managers = Manager.fetchManagers();
        Gson gson = new Gson();
        JsonElement managersJson = gson.toJsonTree(managers);
        return Response.ok(managersJson.toString(), MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("getManagerByEmail")
    public Response fetchManagerByEmail(String reqData) {
        JSONObject json = new JSONObject(reqData);
        String email = json.getString("email");
        Manager manager = Manager.fetchManagerByEmail(email);
        Gson gson = new Gson();
        JsonElement managerJsonElement = gson.toJsonTree(manager);
        if (managerJsonElement.isJsonObject()) {
            JsonObject managerJsonObject = managerJsonElement.getAsJsonObject();
            managerJsonObject.addProperty("success", "true");
            return Response.ok(gson.toJson(managerJsonElement), MediaType.APPLICATION_JSON).build();
        } else {
            JsonObject resp = new JsonObject();
            resp.addProperty("error", "true");
            resp.addProperty("errorMessage", "Couldn't fetch manager");
            return Response.ok(gson.toJson(resp), MediaType.APPLICATION_JSON).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("addManager")
    public Response addManager(String reqData) {
        JSONObject json = new JSONObject(reqData);
        String email = json.getString("email");
        String password = json.getString("password");
        String managerAddedResponse = Manager.addManager(email, password);
        JSONObject responseJson = new JSONObject();
        if (managerAddedResponse.equals("true")) {
            responseJson.put("success", "true");
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
        } else {
            responseJson.put("failure", "true");
            responseJson.put("errorMessage", managerAddedResponse);
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("removeManager")
    public Response removeManager(String reqData) {
        JSONObject json = new JSONObject(reqData);
        String email = json.getString("email");
        String managerRemovedResponse = Manager.removeManager(email);
        JSONObject responseJson = new JSONObject();
        if (managerRemovedResponse.equals("true")) {
            responseJson.put("success", "true");
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
        } else {
            responseJson.put("failure", "true");
            responseJson.put("errorMessage", managerRemovedResponse);
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
        }
    }
}
