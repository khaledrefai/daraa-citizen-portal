package gov.daraa.citizen.service.dto;

import java.util.ArrayList;
import java.util.List;

public class AssistantChatResponse {

    private String answer;
    private List<AssistantSource> sources = new ArrayList<>();

    public AssistantChatResponse() {}

    public AssistantChatResponse(String answer, List<AssistantSource> sources) {
        this.answer = answer;
        if (sources != null) {
            this.sources = sources;
        }
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<AssistantSource> getSources() {
        return sources;
    }

    public void setSources(List<AssistantSource> sources) {
        this.sources = sources;
    }
}
