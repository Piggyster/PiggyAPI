package me.piggyster.api.menu.component;

import me.piggyster.api.color.ColorAPI;
import net.md_5.bungee.api.chat.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleComponent {

    private StringBuilder text;

    //private ComponentBuilder builder;
    private HoverEvent.Action hoverAction;
    private String hoverValue;
    private ClickEvent.Action clickAction;
    private String clickValue;
    private List<SimpleComponent> extraComponents;
    private Map<String, Object> placeholders;

    public SimpleComponent() {
        text = new StringBuilder();
        //builder = new ComponentBuilder();
        extraComponents = new ArrayList<>();
        placeholders = new HashMap<>();
    }

    public SimpleComponent addText(String text) {
        this.text.append(text);
        //builder.append(text);
        //builder.append(TextComponent.fromLegacyText(ColorAPI.process(text)));
        return this;
    }

    public SimpleComponent addHoverText(String text) {
        hoverAction = HoverEvent.Action.SHOW_TEXT;
        hoverValue = text;
        //builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new SimpleComponent().addText(text).build()));
        return this;
    }

    public SimpleComponent addClickLink(String link) {
        clickAction = ClickEvent.Action.OPEN_URL;
        clickValue = link;
        //builder.event(new ClickEvent(ClickEvent.Action.OPEN_URL, link));
        return this;
    }

    public SimpleComponent addClickSuggestCommand(String command) {
        clickAction = ClickEvent.Action.SUGGEST_COMMAND;
        clickValue = command;
        //builder.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        return this;
    }

    public SimpleComponent addClickCommand(String command) {
        clickAction = ClickEvent.Action.RUN_COMMAND;
        clickValue = command;
        //builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        return this;
    }

    public SimpleComponent addComponent(SimpleComponent component) {
        extraComponents.add(component);
        //builder.append(component.build());
        return this;
    }

    public SimpleComponent setPlaceholder(String placeholder, Object value) {
        /*for(BaseComponent component : builder.getParts()) {
            if(component instanceof TextComponent) {
                TextComponent textComponent = (TextComponent) component;
                if(component.getHoverEvent() != null) {
                    for(BaseComponent hoverComponent : component.getHoverEvent().getValue()) {
                        if(hoverComponent instanceof TextComponent) {
                            TextComponent hoverTextComponent = (TextComponent) hoverComponent;
                            hoverTextComponent.setText(hoverTextComponent.getText().replace(placeholder, value.toString()));
                        }

                    }
                }
                if(component.getClickEvent() != null) {
                    component.setClickEvent(new ClickEvent(component.getClickEvent().getAction(), component.getClickEvent().getValue().replace(placeholder, value.toString())));
                }
                textComponent.setText(textComponent.getText().replace(placeholder, value.toString()));
            }

        }*/
        placeholders.put(placeholder, value);
        return this;
    }

    public BaseComponent[] build() {
        ComponentBuilder builder = new ComponentBuilder();
        String text = this.text.toString();
        for(Map.Entry<String, Object> entry : placeholders.entrySet()) {
            text = text.replace(entry.getKey(), entry.getValue().toString());
        }
        TextComponent component = new TextComponent(TextComponent.fromLegacyText(ColorAPI.process(text)));
        if(hoverAction != null) {
            placeholders.entrySet().iterator().forEachRemaining(entry -> hoverValue = hoverValue.replace(entry.getKey(), entry.getValue().toString()));
            component.setHoverEvent(new HoverEvent(hoverAction, new SimpleComponent().addText(ColorAPI.process(hoverValue)).build()));
        }
        if(clickAction != null) {
            placeholders.entrySet().iterator().forEachRemaining(entry -> clickValue = clickValue.replace(entry.getKey(), entry.getValue().toString()));
            component.setClickEvent(new ClickEvent(clickAction, ColorAPI.process(clickValue)));
        }
        builder.append(component);
        extraComponents.iterator().forEachRemaining(extra -> builder.append(extra.build()));
        return builder.create();
    }
}
