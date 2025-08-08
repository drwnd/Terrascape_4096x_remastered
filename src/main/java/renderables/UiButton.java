package renderables;

import org.joml.Vector2f;

public class UiButton extends UiBackgroundElement implements Runnable {
    public UiButton(Vector2f sizeToParent, Vector2f offsetToParent, Runnable action) {
        super(sizeToParent, offsetToParent);
        this.action = action;
    }

    public void setAction(Runnable action) {
        this.action = action;
    }

    @Override
    public void run() {
        action.run();
    }

    private Runnable action;
}
