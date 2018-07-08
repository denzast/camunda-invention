package ru.adwise.patterra.bpmn;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.model.bpmn.instance.MessageEventDefinition;
import org.camunda.bpm.model.bpmn.instance.ThrowEvent;

import java.util.Optional;
import java.util.logging.Logger;

/*
from https://forum.camunda.org/t/correlate-message-directly-from-message-event/1482/3 by Markus
 */
public class GenericMessageCorrelation implements JavaDelegate {
    private final static Logger LOGGER = Logger.getLogger("Generic Message Correlation");

    public void execute(DelegateExecution execution) {
        ThrowEvent event = (ThrowEvent) execution.getBpmnModelElementInstance();

        Optional<MessageEventDefinition> messageEventDefinitionOptional = event.getEventDefinitions().stream()
                .filter((ed) -> ed instanceof MessageEventDefinition)
                .map((ed) -> ((MessageEventDefinition) ed))
                .findFirst();

        messageEventDefinitionOptional.ifPresent((messageEventDefinition) -> {
            if (messageEventDefinition.getMessage() != null) {

                if (execution.getProcessBusinessKey() != null) {
                    RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();
                    String messageName = messageEventDefinition.getMessage().getName();

                    runtimeService.createMessageCorrelation(messageName)
                            .processInstanceBusinessKey(execution.getProcessBusinessKey())
                            .correlateAll();
                } else {
                    LOGGER.info(String.format("No message is thrown from execution=%s because no businessKey is defined in process instance", execution.getId()));
                }
            }
        });
    }
}
