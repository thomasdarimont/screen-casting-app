package de.tdlabs.apps.screencaster.notes;

import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
class MarkdownFormatter {

  private final Extension autolinkExtension;

  public MarkdownFormatter() {
    autolinkExtension = AutolinkExtension.create();
  }

  public String format(String text) {

    Parser parser = Parser.builder() //
      .extensions(Collections.singletonList(autolinkExtension))
      .build();
    Node document = parser.parse(text);
    HtmlRenderer renderer = HtmlRenderer.builder() //
      .attributeProviderFactory(context -> LinkAttributeProvider.INSTANCE) //
      .build();

    return renderer.render(document);
  }

  enum LinkAttributeProvider implements AttributeProvider {

    INSTANCE;

    @Override
    public void setAttributes(Node node, String tagName, Map<String, String> attributes) {

      if (node instanceof Link) {
        attributes.put("target", "_blank");
      }
    }
  }
}
