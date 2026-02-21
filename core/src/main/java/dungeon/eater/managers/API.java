package dungeon.eater.managers;

import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import dungeon.eater.dialogs.core.DialogManager;
import dungeon.eater.localization.Localization;
import dungeon.eater.events.core.EventModule;
import engine.Resources;
import dungeon.eater.pages.core.PageManager;
import engine.sui.SUIManager;
import engine.services.EngineServices;

public class API implements Disposable {

    private static API api;

    private final ObjectMap<Class<?>, Object> apiMap = new ObjectMap<>();

    public static API Instance () {
        if (api == null) {
            api = new API();
        }
        return api;
    }

    public API () {
        initMinimal();
    }

    public static <U> U get (Class<U> clazz) {
        return clazz.cast(Instance().apiMap.get(clazz));
    }

    public void initMinimal () {
        register(EventModule.class);
        register(Resources.class);
        register(SUIManager.class);
        register(Localization.class);
        register(PageManager.class);
        register(DialogManager.class);
    }

    public <T> void register (Class<T> key, T object) {
        if (apiMap.containsKey(key)) return;
        apiMap.put(key, object);
        EngineServices.register(key, object);
    }

    public <T>  void register (Class<T> clazz) {
        if (apiMap.containsKey(clazz)) return;
        try {
            T instance = ClassReflection.newInstance(clazz);
            apiMap.put(clazz, instance);
            EngineServices.register(clazz, instance);
        } catch (ReflectionException e) {
            throw new RuntimeException("Failed to instantiate class: " + clazz.getName(), e);
        }
    }

    public <T> void register (T object) {
        register((Class<T>) object.getClass(), object);
    }

    @Override
    public void dispose () {
        for (Object value : apiMap.values()) {
            if (value instanceof Disposable) {
                ((Disposable) value).dispose();
            }
        }
        apiMap.clear();
        EngineServices.clear();
    }
}
