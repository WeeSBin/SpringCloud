package wesbin.withkafka;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;

@EnableBinding(Source.class)
public class Producer {

    private Source mySource;

    public Producer(Source mySource) {
        this.mySource = mySource;
    }

    public Source getMySource() {
        return mySource;
    }

    public void setMySource(Source mySource) {
        this.mySource = mySource;
    }
}
