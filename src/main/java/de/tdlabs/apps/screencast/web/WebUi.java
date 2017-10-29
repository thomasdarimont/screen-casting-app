package de.tdlabs.apps.screencast.web;

import de.tdlabs.apps.screencast.Settings;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;

@Controller
@RequiredArgsConstructor
class WebUi {

  private final Settings settings;

  private final Environment env;

  @GetMapping("/")
  String index(Model model) throws Exception {
    model.addAttribute("hostname", Inet4Address.getLocalHost().getHostName());
    return "index";
  }

  @GetMapping("/admin")
  @PreAuthorize("#request.getRemoteAddr().equals(#request.getLocalAddr())")
  String admin(Model model, HttpServletRequest request) throws Exception {

    model.addAttribute("hostname", Inet4Address.getLocalHost().getHostName());
    model.addAttribute("url", "http://" + Inet4Address.getLocalHost().getHostName() + ":" + env.getProperty("server.port") + "/");
    model.addAttribute("settings", this.settings);

    return "admin";
  }

  @PostMapping("/admin/settings")
  @PreAuthorize("#request.getRemoteAddr().equals(#request.getLocalAddr())")
  String updateSettings(SettingsForm settingsForm, HttpServletRequest request) {
    this.settings.setCastEnabled(settingsForm.isCastEnabled());
    return "redirect:/admin";
  }
}
