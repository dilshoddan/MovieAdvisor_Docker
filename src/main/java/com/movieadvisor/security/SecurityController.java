package com.movieadvisor.security;

import com.movieadvisor.model.User;
import com.movieadvisor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class SecurityController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = {"login", "login/{error}"})
    public String login(@PathVariable(value = "error", required = false) String error, Model model) {
        if(error != null)
            model.addAttribute("error", "Wrong credentials!");
        return "login";
    }

    @PostMapping("login")
    public String authorize(@RequestBody String postData, HttpSession session) {
        String[] pairs = postData.split("&");
        Map<String, String> data = new HashMap<>();
        for(String p : pairs) {
            String[] pair = p.split("=");
            if(pair.length == 2)
                data.put(pair[0], pair[1]);
        }
        Optional<User> user = userRepository.findByUsername(data.get("username"));
        if(user.isPresent() && user.get().getPassword().equals(data.get("password"))) {
            session.setAttribute("username", user.get().getUsername());
            return "redirect:/";
        }
        else
            return "redirect:/login/error";
    }

    @GetMapping("logout")
    public String logout() {
        return "logout";
    }

    @PostMapping("logout")
    public String postlogout(HttpSession session) {
        session.removeAttribute("username");
        return "redirect:/";
    }


    @GetMapping(value = {"register", "register/{error}"})
    public String register(@PathVariable(value = "error", required = false) String error, Model model) {
        if(error != null)
            model.addAttribute("error", error);
        return "register";
    }

    @PostMapping("register")
    public String registered(@RequestBody String postData) {
        String[] pairs = postData.split("&");
        Map<String, String> data = new HashMap<>();
        for(String p : pairs) {
            String[] pair = p.split("=");
            if(pair.length == 2)
                data.put(pair[0], pair[1]);
        }
        Optional<User> user = userRepository.findByUsername(data.get("username"));
        String pass = data.get("password");
        if(user.isPresent())
            return "redirect:/register/userexists";
        userRepository.save(new User(data.get("username"),
                data.get("password"), "ROLE_USER"));
        return "redirect:/";
    }

}
