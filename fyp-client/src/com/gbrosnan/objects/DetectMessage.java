package com.gbrosnan.objects;

public class DetectMessage {
	
	private String status;
	private String exercise;
	private int reps;
	
	public DetectMessage() {}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getExercise() {
		return exercise;
	}

	public void setExercise(String exercise) {
		this.exercise = exercise;
	}

	public int getReps() {
		return reps;
	}

	public void setReps(int reps) {
		this.reps = reps;
	}
	
	public String toString() {
		return "Status = " + getStatus() + "     Exercise = " + getExercise() + "     Reps = " + getReps();
	}
	
	

}
