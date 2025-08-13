package renderables;

import assets.identifiers.TextureIdentifier;
import org.joml.Vector2f;
import rendering_api.Window;

public class TextField extends UiButton {

    public TextField(Vector2f sizeToParent, Vector2f offsetToParent, String name) {
        super(sizeToParent, offsetToParent);
        setAction(this::setOnTop);

        UiElement blackBox = new UiElement(new Vector2f(0.8f, 0.8f), new Vector2f(0.175f, 0.1f), TextureIdentifier.INVENTORY_OVERLAY);
        textElement = new TextElement(new Vector2f(0.05f, 0.5f));
        blackBox.addRenderable(textElement);

        TextElement nameElement = new TextElement(new Vector2f(0.025f, 0.5f));
        nameElement.setText(name);

        addRenderable(blackBox);
        addRenderable(nameElement);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    @Override
    public void renderSelf(Vector2f position, Vector2f size) {
        textElement.setText(text);
        super.renderSelf(position, size);
    }

    @Override
    public void setOnTop() {
        Window.setInput(new TextFieldInput(this));
    }

    private String text = "";
    private final TextElement textElement;
}
