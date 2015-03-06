package com.gbrosnan.objects;

import java.util.Date;
import java.util.List;

public class ExerciseRaw {
	
	private int id;
	private String type;
	private String username;
	private String exerciseName;
	private int repCount;
	private Date date;
	private List<SensorSample> sensorSampleList;
	
	public ExerciseRaw(String type, String username, String exerciseName, int repCount, Date date, List<SensorSample> sensorSampleList){
		setType(type);
		setUsername(username);
		setExerciseName(exerciseName);
		setRepCount(repCount);
		setDate(date);
		setSensorSampleList(sensorSampleList);
	}
	
	public ExerciseRaw(int id, String username, String exerciseName, Date date, List<SensorSample> sensorSampleList){
		setId(id);
		setUsername(username);
		setExerciseName(exerciseName);
		setDate(date);
		setSensorSampleList(sensorSampleList);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getExerciseName() {
		return exerciseName;
	}
	public void setExerciseName(String exerciseName) {
		this.exerciseName = exerciseName;
	}
	public int getRepCount() {
		return repCount;
	}

	public void setRepCount(int repCount) {
		this.repCount = repCount;
	}

	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public List<SensorSample> getSensorSampleList() {
		return sensorSampleList;
	}
	public void setSensorSampleList(List<SensorSample> sensorSampleList) {
		this.sensorSampleList = sensorSampleList;
	}
}
