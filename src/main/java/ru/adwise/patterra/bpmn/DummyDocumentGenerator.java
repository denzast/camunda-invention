package ru.adwise.patterra.bpmn;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.model.bpmn.instance.Task;

public class DummyDocumentGenerator implements JavaDelegate {
    public void execute(DelegateExecution execution) {
        Task task = (Task) execution.getBpmnModelElementInstance();

        System.out.println(String.format("Running %s...", task.getName()));
    }
}
