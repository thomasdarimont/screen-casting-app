package de.tdlabs.apps.screencast;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.Inet4Address;

@Controller
class ScreenCastWebUi {

  @GetMapping("/")
  String index(Model model) throws Exception {
    model.addAttribute("name", System.getenv().get("USER"));
    model.addAttribute("machine", Inet4Address.getLocalHost().getHostName());
    return "index";
  }
}
