package com.company.jmixpm.screen.roadmap;

import io.jmix.ui.screen.*;
import com.company.jmixpm.entity.Roadmap;

@UiController("Roadmap.edit")
@UiDescriptor("roadmap-edit.xml")
@EditedEntityContainer("roadmapDc")
public class RoadmapEdit extends StandardEditor<Roadmap> {
}