

public class TimeSlot {
    private String startTime;
    private String endTime;

    // Constructor
    public TimeSlot(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters
    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    // Setters
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    // toString method for easy display
    @Override
    public String toString() {
        return "TimeSlot [Start Time: " + startTime + ", End Time: " + endTime + "]";
    }
}
