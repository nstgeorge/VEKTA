import org.junit.Test;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import vekta.WorldState;
import vekta.connection.message.Message;

import java.io.Externalizable;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static processing.core.PApplet.println;

public class SerializationTest {
	private static final Reflections REFLECTIONS = new Reflections("vekta");

	/**
	 * Ensure that all fields on serializable world objects are also serializable.
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void testSerialization() throws Exception {
		Set<Class<?>> next = new HashSet<>();
		Set<Class<?>> current = new HashSet<>();
		Set<Class<?>> visited = new HashSet<>();

		next.add(WorldState.class); // Singleplayer save/load states
		next.add(Message.class); // Multiplayer messages

		while(!next.isEmpty()) {
			current.addAll(next);
			next.clear();
			for(Class<?> type : current) {
				println(type);////
				boolean isExternalizable = Externalizable.class.isAssignableFrom(type);
				if(!Serializable.class.isAssignableFrom(type) && !isExternalizable && !type.isEnum() && !type.isPrimitive()) {
					throw new Exception("Non-serializable class: " + type.getName());
				}
				visited.add(type);
				if(!isExternalizable) {
					Stream.concat(
							ReflectionUtils.getAllFields(type).stream() // Check fields
									.filter(f -> !Modifier.isTransient(f.getModifiers()) && !Modifier.isStatic(f.getModifiers()))
									.map(Field::getType),
							REFLECTIONS.getSubTypesOf(type).stream()) // Check subclasses
							.forEach(t -> {
								if(!visited.contains(t) && !t.getName().startsWith("java.util.")) {
									next.add(t);
								}
							});
				}
			}
			current.clear();
		}
	}
}
