package renderables;

import org.joml.Vector2f;
import org.joml.Vector2i;
import rendering_api.Window;

import java.util.ArrayList;

public abstract class Renderable {

    public Renderable(Vector2f sizeToParent, Vector2f offsetToParent) {
        this.sizeToParent = sizeToParent;
        this.offsetToParent = offsetToParent;
    }

    public static void scaleForFocused(Vector2f position, Vector2f size) {
        final float scalingFactor = 1.05f;
        float dx = (size.x - size.x * scalingFactor) * 0.5f;
        float dy = (size.y - size.y * scalingFactor) * 0.5f;

        size.mul(scalingFactor);
        position.add(dx, dy);
    }


    protected abstract void renderSelf(Vector2f position, Vector2f size);

    protected abstract void resizeSelfTo(int width, int height);


    public void allowScaling(boolean allowScaling) {
        this.allowScaling = allowScaling;
    }

    public void setOnTop() {

    }

    public final void render(Vector2f parentPosition, Vector2f parentSize) {
        if (!isVisible) return;
        Vector2f thisSize = new Vector2f(parentSize).mul(sizeToParent);
        Vector2f thisPosition = new Vector2f(parentPosition).add(new Vector2f(parentSize).mul(offsetToParent));
        if (isFocused && allowScaling) scaleForFocused(thisPosition, thisSize);

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

    public void clickOn(Vector2i pixelCoordinate, int mouseButton, int action) {
        for (Renderable renderable : children)
            if (renderable.isVisible && renderable.containsPixelCoordinate(pixelCoordinate))
                renderable.clickOn(pixelCoordinate, mouseButton, action);
    }

    public void hoverOver(Vector2i pixelCoordinate) {
        if (isFocused) return;
        for (Renderable renderable : children)
            if (renderable.isVisible)
                renderable.setFocused(renderable.containsPixelCoordinate(pixelCoordinate));
    }

    public void dragOver(Vector2i pixelCoordinate) {
        hoverOver(pixelCoordinate);
    }

    public void move(Vector2f offset) {
        offsetToParent.add(offset);
    }

    public boolean containsPixelCoordinate(Vector2i pixelCoordinate) {
        Vector2f position = Window.toPixelCoordinate(getPosition());
        Vector2f size = Window.toPixelSize(getSize());

        return position.x <= pixelCoordinate.x && position.x + size.x >= pixelCoordinate.x
                && position.y <= pixelCoordinate.y && position.y + size.y >= pixelCoordinate.y;
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    public void setFocused(boolean focused) {
        isFocused = focused;

        if (isFocused) return;
        for (Renderable renderable : children) renderable.setFocused(false);
    }

    public Vector2f getPosition() {
        return parent.getPosition().add(parent.getSize().mul(offsetToParent));
    }

    public Vector2f getSize() {
        return parent.getSize().mul(sizeToParent);
    }

    public Vector2f getOffsetToParent() {
        return offsetToParent;
    }

    public Vector2f getSizeToParent() {
        return sizeToParent;
    }

    public ArrayList<Renderable> getChildren() {
        return children;
    }

    public Renderable getParent() {
        return parent;
    }

    public void setOffsetToParent(Vector2f offsetToParent) {
        this.offsetToParent.set(offsetToParent);
    }

    public boolean isVisible() {
        return isVisible;
    }

    private final ArrayList<Renderable> children = new ArrayList<>();
    private boolean isVisible = true, isFocused = false, allowScaling = true;
    private final Vector2f sizeToParent;
    private final Vector2f offsetToParent;
    private Renderable parent = DummyRenderable.dummy;
}
