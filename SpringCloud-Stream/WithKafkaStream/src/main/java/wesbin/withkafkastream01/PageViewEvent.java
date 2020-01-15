package wesbin.withkafkastream01;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageViewEvent {
    private String userId, page;
    private long duration;
}
