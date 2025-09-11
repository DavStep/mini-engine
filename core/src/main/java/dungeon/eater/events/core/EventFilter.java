package dungeon.eater.events.core;

public interface EventFilter<T extends Event> {

	boolean shouldExecute (T event);

}
