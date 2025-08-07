package rendering_api.renderables;

import org.joml.Vector2f;
import org.joml.Vector2i;
import rendering_api.Window;

import java.util.ArrayList;

public abstract class Renderable {

    public Renderable(Vector2f sizeToParent, Vector2f offsetToParent) {
        this.sizeToParent = sizeToParent;
        this.offsetToParent = offsetToParent;
    }


    protected abstract void renderSelf(Vector2f position, Vector2f size);

    protected abstract void resizeSelfTo(int width, int height);


    public void setOnTop() {

    }

    public final void render(Vector2f parentPosition, Vector2f parentSize) {
        if (!isVisible) return;
        Vector2f thisSize = new Vector2f(parentSize).mul(sizeToParent);
        Vector2f thisPosition = new Vector2f(parentPosition).add(new Vector2f(parentSize).mul(offsetToParent));

        if (isFocused) {
            final float scalingFactor = 1.05f;
            float dx = (thisSize.x - thisSize.x * scalingFactor) * 0.5f;
            float dy = (thisSize.y - thisSize.y * scalingFactor) * 0.5f;

            thisSize.mul(scalingFactor);
            thisPosition.add(dx, dy);
        }

        renderSelf(thisPosition, thisSize);
        for (Renderable child : children) child.render(thisPosition, thisSize);
    }

    public final void resize(Vector2i size, Vector2f parentSize) {
        Vector2f thisSize = new Vector2f(parentSize).mul(sizeToParent);
        resizeSelfTo((int) (size.x * thisSize.x), (int) (size.y * thisSize.y));
        for (Renderable child : children) child.resize(size, thisSize);
    }

    public void addRenderable(Renderable renderable) {
        children.add(renderable);
        renderable.parent = this;
    }

    public void clickOn(Vector2i pixelCoordinate) {
        for (Renderable button : children)
            if (button.isVisible && button instanceof UiButton && button.containsPixelCoordinate(pixelCoordinate))
                ((UiButton) button).run();
    }

    public void hoverOver(Vector2i pixelCoordinate) {
        for (Renderable renderable : children)
            if (renderable.isVisible)
                renderable.setFocused(renderable.containsPixelCoordinate(pixelCoordinate));
    }

    public void move(Vector2f offset) {
        offsetToParent.add(offset);
    }

    public boolean containsPixelCoordinate(Vector2i pixelCoordinate) {
        Vector2f position = getPosition().mul(Window.getWidth(), Window.getHeight());
        Vector2f size = getSize().mul(Window.getWidth(), Window.getHeight());

        return position.x <= pixelCoordinate.x && position.x + size.x >= pixelCoordinate.x
                && position.y <= pixelCoordinate.y && position.y + size.y >= pixelCoordinate.y;
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    public void setFocused(boolean focused) {
        isFocused = focused;
    }

    public Vector2f getPosition() {
        return parent.getPosition().add(parent.getSize().mul(offsetToParent));
    }

    public Vector2f getSize() {
        return parent.getSize().mul(sizeToParent);
    }

    public ArrayList<Renderable> getChildren() {
        return children;
    }

    public Renderable getParent() {
        return parent;
    }

    public boolean isVisible() {
        return isVisible;
    }

    private final ArrayList<Renderable> children = new ArrayList<>();
    private boolean isVisible = true, isFocused = false;
    private final Vector2f sizeToParent;
    private final Vector2f offsetToParent;
    private Renderable parent = DummyRenderable.dummy;
}
