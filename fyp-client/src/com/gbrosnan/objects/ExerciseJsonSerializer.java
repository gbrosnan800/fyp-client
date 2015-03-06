package com.gbrosnan.objects;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ExerciseJsonSerializer implements JsonSerializer<ExerciseRaw> {

	@Override
	public JsonElement serialize(ExerciseRaw exercise, Type type, JsonSerializationContext context) {
        
		JsonObject object = new JsonObject();             
        object.add("id", context.serialize(exercise.getId()));
        object.add("type", context.serialize(exercise.getType()));
		object.add("username", context.serialize(exercise.getUsername()));
        object.add("exerciseName", context.serialize(exercise.getExerciseName()));
        object.add("weight", context.serialize(exercise.getWeight()));
        object.add("repCount", context.serialize(exercise.getRepCount()));
        object.add("date", context.serialize(exercise.getDate()));
        object.add("sensorSampleList", context.serialize(exercise.getSensorSampleList()));
        return object;
    }
}
