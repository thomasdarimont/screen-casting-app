package de.tdlabs.apps.screencaster.web;

import de.tdlabs.apps.screencaster.settings.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;

@Controller
@RequiredArgsConstructor
class WebUi {

  private final SettingsService settingsService;

  private final Environment env;

  @GetMapping("/")
  String index(Model model, HttpServletRequest request) throws Exception {

    model.addAttribute("hostname", Inet4Address.getLocalHost().getHostName());

    model.addAttribute("screencastUrl", "http://" + Inet4Address.getLocalHost().getHostName() + ":" + env.getProperty("server.port") + "/");
    model.addAttribute("settings", this.settingsService);
    model.addAttribute("requestType", isLocalRequest(request) ? "caster" : "watcher");

    return "index";
  }

  private boolean isLocalRequest(HttpServletRequest request) {
    return request.getRemoteAddr().equals(request.getLocalAddr());
  }
}
