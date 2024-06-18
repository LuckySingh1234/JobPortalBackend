package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
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

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("addJob")
    public Response addJob(String reqData) {
        JSONObject json = new JSONObject(reqData);
        String companyId = json.getString("companyId");
        String jobTitle = json.getString("jobTitle");
        String jobDescription = json.getString("jobDescription");
        String jobPackage = json.getString("package");
        String experience = json.getString("experience");
        String skills = json.getString("skills");

        String jobAddedResponse = Job.addJob(companyId, jobTitle, jobDescription, jobPackage, experience, skills);
        JSONObject responseJson = new JSONObject();
        if (jobAddedResponse.equals("true")) {
            responseJson.put("success", "true");
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
        } else {
            responseJson.put("failure", "true");
            responseJson.put("errorMessage", jobAddedResponse);
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("addCompany")
    public Response addCompany(String reqData) {
        JSONObject json = new JSONObject(reqData);
        String companyName = json.getString("companyName");
        String companyDescription = json.getString("companyDescription");
        String location = json.getString("location");
        String imageUrl = json.getString("imageUrl");

        String companyAddedResponse = Company.addCompany(companyName, companyDescription, location, imageUrl);
        JSONObject responseJson = new JSONObject();
        if (companyAddedResponse.equals("true")) {
            responseJson.put("success", "true");
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
        } else {
            responseJson.put("failure", "true");
            responseJson.put("errorMessage", companyAddedResponse);
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("removeJob")
    public Response removeJob(String reqData) {
        JSONObject json = new JSONObject(reqData);
        String jobId = json.getString("jobId");

        String jobRemovedResponse = Job.removeJob(jobId);
        JSONObject responseJson = new JSONObject();
        if (jobRemovedResponse.equals("true")) {
            responseJson.put("success", "true");
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
        } else {
            responseJson.put("failure", "true");
            responseJson.put("errorMessage", jobRemovedResponse);
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("saveResume")
    public Response saveResume(String reqData) {
        JSONObject json = new JSONObject(reqData);
        String userId = json.getString("userId");
        String name = json.getString("name");
        String dob = json.getString("dob");
        String email = json.getString("email");
        String institutionName = json.getString("institutionName");
        String degree = json.getString("degree");
        String yearOfGraduation = json.getString("yearOfGraduation");
        String companyName = json.getString("companyName");
        String role = json.getString("role");
        String duration = json.getString("duration");
        String responsibilities = json.getString("responsibilities");

        String savedResumeResponse = Resume.saveResume(userId, name, dob, email, institutionName,
                degree, yearOfGraduation, companyName, role, duration, responsibilities);
        JSONObject responseJson = new JSONObject();
        if (savedResumeResponse.equals("true")) {
            responseJson.put("success", "true");
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
        } else {
            responseJson.put("failure", "true");
            responseJson.put("errorMessage", savedResumeResponse);
            return Response.ok(responseJson.toString(), MediaType.APPLICATION_JSON).build();
        }
    }
}
