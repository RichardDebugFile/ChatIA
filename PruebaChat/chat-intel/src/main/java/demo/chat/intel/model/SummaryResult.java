package demo.chat.intel.model;

import java.time.Instant;
import java.util.List;

public class SummaryResult {
    private Instant timestamp;
    private List<String> infractions;
    private String summary;

    public SummaryResult(Instant timestamp,
                         List<String> infractions,
                         String summary) {
        this.timestamp   = timestamp;
        this.infractions = infractions;
        this.summary     = summary;
    }

    public SummaryResult() {}

    public Instant getTimestamp()      { return timestamp; }
    public List<String> getInfractions(){ return infractions; }
    public String getSummary()         { return summary; }

    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public void setInfractions(List<String> infractions) { this.infractions = infractions; }
    public void setSummary(String summary) { this.summary = summary; }
}
