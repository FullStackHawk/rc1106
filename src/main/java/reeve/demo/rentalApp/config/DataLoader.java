package reeve.demo.rentalApp.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import reeve.demo.rentalApp.model.Tool;
import reeve.demo.rentalApp.repository.ToolRepository;

@Configuration
public class DataLoader {

    @Autowired
    private ToolRepository toolRepository;

    @PostConstruct
    private void loadTools() {
        Tool tool1 = new Tool("CHNS", "Chainsaw", "Stihl", 1.49, true, false, true);
        Tool tool2 = new Tool("LADW", "Ladder", "Werner", 1.99, true, true, false);
        Tool tool3 = new Tool("JAKD", "Jackhammer", "DeWalt", 2.99, true, false, false);
        Tool tool4 = new Tool("JAKR", "Jackhammer", "Ridgid", 2.99, true, false, false);

        toolRepository.save(tool1);
        toolRepository.save(tool2);
        toolRepository.save(tool3);
        toolRepository.save(tool4);
    }

}
