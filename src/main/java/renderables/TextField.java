package renderables;

import assets.identifiers.TextureIdentifier;
import org.joml.Vector2f;
import org.joml.Vector2i;
import rendering_api.Window;

public class TextField extends UiButton {

    public TextField(Vector2f sizeToParent, Vector2f offsetToParent, String name) {
        super(sizeToParent, offsetToParent, null);
        setAction(this::setOnTop);

        UiElement blackBox = new UiElement(new Vector2f(0.8f, 0.8f), new Vector2f(0.175f, 0.1f), TextureIdentifier.INVENTORY_OVERLAY);
        textElement = new TextElement(new Vector2f(1.0f, 1.0f), new Vector2f(0.05f, 0.5f), TEXT_SIZE);
        blackBox.addRenderable(textElement);

        TextElement nameElement = new TextElement(new Vector2f(1.0f, 1.0f), new Vector2f(0.025f, 0.5f), TEXT_SIZE);
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

    @Override
    public void clickOn(Vector2i pixelCoordinate) {

    }

    private String text = "";
    private final TextElement textElement;

    private static final Vector2f TEXT_SIZE = new Vector2f(0.008333334f, 0.022222223f);
}
