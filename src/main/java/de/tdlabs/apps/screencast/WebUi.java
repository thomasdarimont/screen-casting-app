package de.tdlabs.apps.screencast;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.Inet4Address;

@Controller
@RequiredArgsConstructor
class WebUi {

  private final Settings settings;

  @GetMapping("/")
  String index(Model model) throws Exception {
    model.addAttribute("name", System.getenv().get("USER"));
    model.addAttribute("hostname", Inet4Address.getLocalHost().getHostName());
    return "index";
  }

  @GetMapping("/admin/ui")
  String admin(Model model) throws Exception {
    model.addAttribute("name", System.getenv().get("USER"));
    model.addAttribute("hostname", Inet4Address.getLocalHost().getHostName());
    model.addAttribute("settings", this.settings);
    return "/admin";
  }

  @PostMapping(path = "/admin/settings")
  String updateSettings(SettingsForm settingsForm) {
    this.settings.setCastEnabled(settingsForm.isCastEnabled());
    return "redirect:/admin/ui";
  }
}
