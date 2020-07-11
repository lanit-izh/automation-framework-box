package utils.allure;

import io.qameta.allure.listener.StepLifecycleListener;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.model.TestResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AllureLogger implements StepLifecycleListener, TestLifecycleListener {

    private static final Logger LOGGER = LogManager.getLogger(AllureLogger.class.getSimpleName());


    @Override
    public void afterStepStop(StepResult result) {
        if (result.getDescription() != null && result.getDescription().contains("SoftAssert")) {
            LOGGER.info("Устанавливаем для шага: '" + result.getName() + "', статус=BROKEN");
            result.setStatus(Status.BROKEN);
            result.setDescription(result.getDescription().replace("SoftAssert:", ""));
        }
    }

    @Override
    public void beforeTestStop(TestResult result) {
        StatusDetails statusDetails = result.getStatusDetails();
        if (result.getStatus().equals(Status.FAILED)) {
            String message = "ОШИБКА ТЕСТА:\n\"" + statusDetails.getMessage().replaceFirst("\n", "") + "\"";
            String brokenStepsMessages = findBrokenStepsMessage(result.getSteps());
            if (brokenStepsMessages != null) {
                message = message + brokenStepsMessages;
            }
            statusDetails.setMessage(message);
            result.setStatusDetails(statusDetails);
        }
    }


    private String findBrokenStepsMessage(List<StepResult> stepResults) {
        ArrayList<StepResult> brokenSteps = stepResults.stream().filter(stepResult -> stepResult.getStatus().equals(Status.BROKEN)).collect(Collectors.toCollection(ArrayList::new));
        if (brokenSteps.size() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append("\n\n");
            builder.append("Другие поломки в тесте (BROKEN STEPS):");
            builder.append("\n[Также в тесте присутствуют ошибки типа SoftAssert : ");
            for (int i = 0; i < brokenSteps.size(); i++) {
                if (brokenSteps.get(i).getDescription() != null) {
                    builder.append("\n").append("Ошибка №:").append(i + 1).append("- ").append(brokenSteps.get(i).getDescription());
                } else {
                    builder.append("\n").append("Ошибка в шаге: ").append(brokenSteps.get(i).getName());
                }
            }
            builder.append("]");
            return builder.toString();
        }
        return null;
    }
}