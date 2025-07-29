package guru.springframework.springrestclientexamples;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RestTemplateExamples {

    public static final String API_ROOT = "https://jsonplaceholder.typicode.com";

    @Test
    public void getUsers() throws Exception {
        String apiUrl = API_ROOT + "/users";

        RestTemplate restTemplate = new RestTemplate();

        JsonNode jsonNode = restTemplate.getForObject(apiUrl, JsonNode.class);

        System.out.println("Response");
        System.out.println(jsonNode.toString());
    }

    @Test
    public void getPosts() throws Exception {
        String apiUrl = API_ROOT + "/posts";

        RestTemplate restTemplate = new RestTemplate();

        JsonNode jsonNode = restTemplate.getForObject(apiUrl, JsonNode.class);

        System.out.println("Response");
        System.out.println(jsonNode.toString());
    }

    @Test
    public void createPost() throws Exception {
        String apiUrl = API_ROOT + "/posts";

        RestTemplate restTemplate = new RestTemplate();

        //Java object to parse to JSON
        Map<String, Object> postMap = new HashMap<>();
        postMap.put("title", "My New Post");
        postMap.put("body", "This is the body of my post");
        postMap.put("userId", 1);

        JsonNode jsonNode = restTemplate.postForObject(apiUrl, postMap, JsonNode.class);

        System.out.println("Response");
        System.out.println(jsonNode.toString());
    }

    @Test
    public void updatePost() throws Exception {
        String apiUrl = API_ROOT + "/posts/";

        RestTemplate restTemplate = new RestTemplate();

        // First create a post
        Map<String, Object> postMap = new HashMap<>();
        postMap.put("title", "Original Title");
        postMap.put("body", "Original Body");
        postMap.put("userId", 1);

        JsonNode jsonNode = restTemplate.postForObject(apiUrl, postMap, JsonNode.class);

        System.out.println("Created Post Response");
        System.out.println(jsonNode.toString());

        String id = jsonNode.get("id").asText();
        System.out.println("Created post id: " + id);

        // Now update it
        postMap.put("title", "Updated Title");
        postMap.put("body", "Updated Body");
        postMap.put("userId", 1);

        restTemplate.put(apiUrl + id, postMap);

        JsonNode updatedNode = restTemplate.getForObject(apiUrl + id, JsonNode.class);

        System.out.println("Updated Post:");
        System.out.println(updatedNode.toString());
    }

    @Test
    public void updatePostUsingPatch() throws Exception {
        String apiUrl = API_ROOT + "/posts/";

        // Use Apache HTTP client factory to support PATCH
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        // Patch existing post (id=1)
        Map<String, Object> patchMap = new HashMap<>();
        patchMap.put("title", "Patched Title");

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(patchMap, headers);

        JsonNode patchedNode = restTemplate.patchForObject(apiUrl + "1", entity, JsonNode.class);

        System.out.println("Patched Post:");
        System.out.println(patchedNode.toString());
    }

    @Test(expected = HttpClientErrorException.class)
    public void deletePost() throws Exception {
        String apiUrl = API_ROOT + "/posts/";

        RestTemplate restTemplate = new RestTemplate();

        // Delete existing post (id=1)
        String postId = "1";

        restTemplate.delete(apiUrl + postId);

        System.out.println("Post deleted");

        // This should throw HttpClientErrorException since JSONPlaceholder
        // simulates deletion but doesn't actually remove resources
        // Try to get a non-existent post (id=999)
        restTemplate.getForObject(apiUrl + "999", JsonNode.class);
    }

    @Test
    public void testErrorHandling() throws Exception {
        String apiUrl = API_ROOT + "/posts/9999"; // Non-existent post

        RestTemplate restTemplate = new RestTemplate();

        try {
            JsonNode jsonNode = restTemplate.getForObject(apiUrl, JsonNode.class);
            System.out.println("Response: " + jsonNode.toString());
        } catch (HttpClientErrorException e) {
            System.out.println("HTTP Error: " + e.getStatusCode());
            System.out.println("Error body: " + e.getResponseBodyAsString());
        }
    }

    @Test
    public void testWithCustomHeaders() throws Exception {
        String apiUrl = API_ROOT + "/posts";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("User-Agent", "Spring RestTemplate Example");

        HttpEntity<String> entity = new HttpEntity<>("", headers);

        JsonNode jsonNode = restTemplate.getForObject(apiUrl, JsonNode.class);

        System.out.println("Response with custom headers:");
        System.out.println(jsonNode.toString());
    }
}