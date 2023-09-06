package com.company.jmixpm.datatype;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ProjectLabels implements Serializable {

    private final List<String> labels;

    public ProjectLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<String> getLabels() {
        return labels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProjectLabels that = (ProjectLabels) o;

        return Objects.equals(labels, that.labels);
    }

    @Override
    public int hashCode() {
        return labels != null ? labels.hashCode() : 0;
    }
}
