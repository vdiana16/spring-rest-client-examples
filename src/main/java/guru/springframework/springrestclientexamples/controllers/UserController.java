package guru.springframework.springrestclientexamples.controllers;

import guru.springframework.springrestclientexamples.services.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ServerWebExchange;

@Slf4j
@Controller
public class UserController {
    private ApiService apiService;

    public UserController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping({"", "/", "/index"})
    public String index() {
        return  "index";
    }

    @PostMapping("/users")
    public String formPost(Model model, ServerWebExchange exchange) {
        MultiValueMap<String, String> body = exchange.getFormData().block();

        Integer limit = new Integer(body.get("limit").get(0));

        log.debug("Received limit: " + limit);
        if (limit == null || limit == 0){
           log.info("Limit set to default value of 10");
           limit = 10; // default limit;
        }
        model.addAttribute("users", apiService.getUsers(limit));
        return "userlist";
    }
}
