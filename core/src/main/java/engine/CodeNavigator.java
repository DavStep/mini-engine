package engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ObjectSet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class CodeNavigator {

    private final ObjectSet<Class<?>> excludedClasses = new ObjectSet<>();

    public CodeNavigator (final Stage stage) {
        stage.addCaptureListener(new InputListener() {
            @Override
            public boolean touchDown (final InputEvent event, final float x, final float y, final int pointer, final int button) {
                final boolean commandPressed =
                    Gdx.input.isKeyPressed(Input.Keys.SYM) || // for macOS Command key
                        Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) ||
                        Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT);

                if (!commandPressed) {
                    return false;
                }

                final boolean shiftPressed =
                    Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ||
                        Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);

                final Actor hitActor = stage.hit(x, y, true);
                if (hitActor == null || hitActor == stage.getRoot()) {
                    return false;
                }

                final Actor nonAtomicActor = findNonAtomicUsage(hitActor);
                if (nonAtomicActor == null) {
                    log.warn("Atomic actor has no non-atomic usage");
                    return false;
                }

                if (shiftPressed) {
                    final Actor nonAtomicActorParent = nonAtomicActor.getParent();
                    if (nonAtomicActorParent == null) {
                        log.warn("Non atomic actor has no parent, falling back to non atomic actor itself ({})", nonAtomicActor.getClass().getSimpleName());
                        CodeUtils.navigateToClass(nonAtomicActor.getClass());
                        event.stop();
                        return true;
                    }

                    final Actor nonAtomicParent = findNonAtomicUsage(nonAtomicActorParent);
                    if (nonAtomicParent != null) {
                        CodeUtils.navigateToClass(nonAtomicParent.getClass());
                        event.stop();
                        return true;
                    }

                    log.warn("Failed to find non-atomic parent for target ({})", nonAtomicActorParent.getClass().getSimpleName());

                } else {
                    final Object userObject = hitActor.getUserObject();
                    if (userObject instanceof Class) {
                        CodeUtils.navigateToClass((Class<?>) userObject);
                        event.stop();
                        return true;
                    }

                    CodeUtils.navigateToClass(nonAtomicActor.getClass());
                    event.stop();
                    return true;
                }

                return false;
            }
        });
    }

    public CodeNavigator exclude (final Class<?>... classes) {
        for (final Class<?> clazz : classes) {
            excludedClasses.add(clazz);
        }
        return this;
    }

    private Actor findNonAtomicUsage (final Actor startActor) {
        Actor current = startActor;
        while (current != null) {
            final Class<?> currentClass = current.getClass();
            final boolean isGdxDefault = currentClass.getName().startsWith("com.badlogic.gdx.scenes.scene2d");
            final boolean isExcluded = excludedClasses.contains(currentClass);

            if (!isGdxDefault && !isExcluded) {
                return current;
            }

            current = current.getParent();
        }
        return null;
    }
}
